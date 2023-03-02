package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.control.StateComparator;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.EpsilonEqualStatesBuilder;
import simulator.factories.Factory;
import simulator.factories.FrozenBodyBuilder;
import simulator.factories.MassEqualStatesBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.MovingTowardsFixedPointBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoForceBuilder;
import simulator.factories.RepulsiveForceBuilder;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _stateComparatorDefaultValue = "epseq";
	//nuestras
	private final static String _stepsDefaultValue = "150";
	private final static String _outFileDefaultValue = null;	//Hay que poner la salida por consola

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static JSONObject _forceLawsInfo = null;
	private static JSONObject _stateComparatorInfo = null;
	//nuestras
	private static int _steps;
	private static String _outFile = null;
	private static String _expectedOutFile = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;

	
	//Inicializamos tres arrays con los tipos de cuerpos, fuerzas y comparadores
	private static void init() {		
		// Initialize the bodies factory
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		bodyBuilders.add(new FrozenBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
		
		// Initialize the force laws factory
		ArrayList<Builder<ForceLaws>> forceLawsBuilders = new ArrayList<>();
		forceLawsBuilders.add(new NewtonUniversalGravitationBuilder());
		forceLawsBuilders.add(new MovingTowardsFixedPointBuilder());
		forceLawsBuilders.add(new NoForceBuilder());
		forceLawsBuilders.add(new RepulsiveForceBuilder());
		_forceLawsFactory = new BuilderBasedFactory<ForceLaws>(forceLawsBuilders);
		
		// Initialize the state comparator
		ArrayList<Builder<StateComparator>>  stateComparatorBuilders = new ArrayList<>();
		stateComparatorBuilders.add(new MassEqualStatesBuilder());
		stateComparatorBuilders.add(new EpsilonEqualStatesBuilder());
		 _stateComparatorFactory = new BuilderBasedFactory<StateComparator>(stateComparatorBuilders);
		
	}

	private static void parseArgs(String[] args) {
		// define the valid command line options
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			
			// TODO add support of -o, -eo, and -s (define corresponding parse methods)
			parseOutFileOption(line);
			parseExpectedOutFileOption(line);
			parseStepsOption(line);
			
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);

			// if there are some remaining arguments, then something wrong is provided in the command line!
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	//define qué comandos acepta nuestra aplicación, devolviendo todos ellos dentro de una instancia de Options
	private static Options buildOptions() {
		Options cmdLineOptions = new Options();	//En esta variable guardaremos qué comandos acepta q nuestra aplicación

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// TODO add support for -o, -eo, and -s (add corresponding information to cmdLineOptions)
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is written. Default value: the standard output.").build());
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expected-output").hasArg().desc("The expected output file. If not provided no comparison is applied.").build());
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("An integer representing the number of simulation steps. Default value: 150..").build());
		
		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: " + _dtimeDefaultValue + ".").build());

		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: " + factoryPossibleValues(_forceLawsFactory) 
				+ ". Default value: '" + _forceLawsDefaultValue + "'.").build());

		// gravity laws
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.")
				.build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("In batch mode an input file of bodies is required");
		}
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert(_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	//Creado por nosotros
	private static void parseOutFileOption(CommandLine line) {
		_outFile = line.getOptionValue("o", _outFileDefaultValue);
	}	//Suponemos que sólo mira que esté bien sintácticamente, como el inFile, solo que para esta hay un valor por defecto

	//Creado por nosotros
	private static void parseExpectedOutFileOption(CommandLine line) {
		_expectedOutFile = line.getOptionValue("eo");
	}
		
	//Creado por nosotros
	private static void parseStepsOption(CommandLine line) throws ParseException {
		String s = line.getOptionValue("s", _stepsDefaultValue);
		try {
			_steps = Integer.parseInt(s);
			assert(_steps >= 0);			//Suponemos que puede dar 0 pasos
		} catch (Exception e) {
			throw new ParseException("Invalid steps value: " + s);
		}
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {
		// the value of v is either a tag for the type, or a tag:data where data is a JSON structure corresponding 
		//to the data of that type. We split this information into variables 'type' and 'data'
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);
		if (_stateComparatorInfo == null) {
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}

	private static void startBatchMode() throws Exception {
		//Creamos el simulador:
		PhysicsSimulator ps = new PhysicsSimulator(_forceLawsFactory.createInstance(_forceLawsInfo),_dtime);
		
		InputStream in = new FileInputStream(new File(_inFile));	

		//COMO HACER PARA PONER SYSTEM.OUT POR DEFECTO
		OutputStream out = _outFile == null ? System.out : new FileOutputStream(new File(_outFile));
		//si no nos pasan ningun -o, el out lo mandaremos a la consola
		//otra forma de hacerlo habría sido:
		/*
		OutputStream out;
		if(_outFile == null)
			out = System.out;
		else
			out = new FileOutputStream(new File(_outFile)); 
		 */
		InputStream expOut = null;
		try {
			expOut =  new FileInputStream(new File(_expectedOutFile)); 
		}
		catch (Exception e) {}
		
		StateComparator sc = _stateComparatorFactory.createInstance(_stateComparatorInfo);

		Controller controller = new Controller (ps, _bodyFactory);
		
		controller.loadBodies(in);
		
		controller.run(_steps, out, expOut, sc);
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		startBatchMode();
	}

	//LAUNCHER
	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}

/*
Apache Commons CLI es una librería q facilita el análisis de los parámetros con los que el usuario puede invocar la aplicación.
Normalmente el tratamiento de la línea de comandos se realiza en tres pasos:
   - Definición (nuestro buildOptions())
   - Parseo (La librería lo hace automáticamente)
   - Análisis del parseo
link: https://www.adictosaltrabajo.com/2007/11/30/commons-cli/
*/