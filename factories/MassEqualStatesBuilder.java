package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;

public class MassEqualStatesBuilder extends Builder <StateComparator> {
	
	public MassEqualStatesBuilder () {
		tipo = "masseq";
		desc = "comparador de masas";
	}

	@Override
	public StateComparator createTheInstance(JSONObject datos) {
		return new MassEqualStates();
	}

}
