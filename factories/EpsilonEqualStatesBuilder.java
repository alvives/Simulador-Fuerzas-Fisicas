package simulator.factories;

import org.json.JSONObject;
import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStatesBuilder extends Builder <StateComparator> {
	double eps = 0.0;
	
	public EpsilonEqualStatesBuilder() {
		tipo = "epseq";
		desc = "comparador de epsilon";
	}
	@Override
	public StateComparator createTheInstance(JSONObject data) {
		try {
			double x = data.getDouble("eps");
			eps = x;
			}
		catch (Exception e) {}
		
		
		return new EpsilonEqualStates (eps);
	}

	
	@Override
	public JSONObject createData() {
		JSONObject jo = new JSONObject();

		jo.put("eps",0.1);

		return jo;
	}
	//Metemos valores por defecto (figura 2), para los data

}
