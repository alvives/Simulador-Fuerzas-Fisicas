package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.FrozenBody;

public class FrozenBodyBuilder extends Builder <Body> {
	
	public FrozenBodyBuilder () {
		tipo = "fb";
		desc = "cuerpo que se congela cada cierto tiempo";
	}

	@Override
	public Body createTheInstance(JSONObject datos) {
		try {
			String id = datos.getString("id");	
			Vector2D p = new Vector2D(datos.getJSONArray("p").getDouble(0), datos.getJSONArray("p").getDouble(1));
			Vector2D v = new Vector2D(datos.getJSONArray("v").getDouble(0), datos.getJSONArray("v").getDouble(1));
			double m = datos.getDouble("m");
			double change = datos.getDouble("change");
			
			return new FrozenBody (id, v, p, m, change);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Falla el data");
		}	
	}

	@Override
	public JSONObject createData() {
		JSONObject jo = new JSONObject();
		Vector2D p = new Vector2D(-3.5e10,0.0e00);
		Vector2D v = new Vector2D(0.0e00,1.4e03);
		
		
		jo.put("id", "b1");
		jo.put("p", p.asJSONArray());
		jo.put("v", v.asJSONArray());
		jo.put("m", 3.0e28);
		jo.put("change", 2e4);

		
		return jo;
	}
	//Metemos valores por defecto (figura 2), para los data
}
