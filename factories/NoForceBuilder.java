package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder <ForceLaws> {
	
	public NoForceBuilder() {
		tipo = "ng";
		desc = "No hay fuerza";
	}

	@Override
	public ForceLaws createTheInstance(JSONObject datos) {
		return new NoForce();
	}

}
