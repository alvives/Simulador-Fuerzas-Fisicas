package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.RepulsiveForce;

public class RepulsiveForceBuilder extends Builder <ForceLaws> {
	double g = 6.67E-11;

	public RepulsiveForceBuilder() {
		tipo = "rf";
		desc = "repulsive force between bodies";
	}
	
	@Override
	public ForceLaws createTheInstance(JSONObject datos) {
		try {
			double x = datos.getDouble("G");
			g = x;
		}
		catch (Exception e) {}	
		
		return new RepulsiveForce(g);
	}
	
	@Override
	public JSONObject createData() {
		JSONObject jo = new JSONObject();
		
		jo.put("G", 6.67e10-11);

		
		return jo;
	}
	//Metemos valores por defecto (figura 2), para los data

}
