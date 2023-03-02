package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.PhysicsSimulator;

public class Controller {
	private PhysicsSimulator ps;
	private Factory<Body> factory;	//Tiene dentro, en el tipo dinamico, un BuilderBasedFactory<Body>(bodyBuilders)
	
	//CONSTRUCTOR
	public Controller(PhysicsSimulator ps, Factory<Body> factory) {
		super();
		this.ps = ps;
		this.factory = factory;
	}
	
	
	
	public void loadBodies(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));	//transforma JSON en JSONObject
		
		for(int i = 0; i < jsonInput.getJSONArray("bodies").length(); i++) {
			ps.addBody(factory.createInstance(jsonInput.getJSONArray("bodies").getJSONObject(i)));
		}
	}
	//leer los cuerpos desde un InputStream dado y añadirlos al simulador
	
	
	
	public void run(int n, OutputStream out, InputStream expOut, StateComparator cmp) throws NewException {
		//Una instancia de una clase que sirve para meter lo que pintaremos en el out
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("\"states\": [");
		
		//SI expout es null no hace falta compararlos, solo pintamos el out
		if (expOut == null) {
			for(int i = 0; i <= n; i++) {
				if(i != 0)
					p.print(",");
				p.println(ps.toString());
				ps.advance();		
			}
		}
		else {
			JSONObject jsonExpOut = new JSONObject(new JSONTokener(expOut));
			
			for(int i = 0; i <= n; i++) {
				if(cmp.equal(ps.getState(), jsonExpOut.getJSONArray("states").getJSONObject(i))) {
					p.println(ps.getState());
					ps.advance();		
				}
				else {
					throw new NewException("Estados diferentes: \n" + ps.toString() + "\n" +
							jsonExpOut.getJSONArray("states").getJSONObject(i).toString() + 
					"\n Paso de la ejecución: " + i); 
				}
			}
		}
		
		p.println("]");
		p.println("}");
	}
	//ejecutar el simulador un número determinado de pasos y mostrar los diferentes estados de cada paso en un OutputStream dado.
}
