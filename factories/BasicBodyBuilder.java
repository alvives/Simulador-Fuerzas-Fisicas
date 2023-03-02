package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder <Body> {

	public BasicBodyBuilder() {
		tipo = "basic";
		desc = "Es un cuerpo basico";
	}
	
	@Override
	public Body createTheInstance(JSONObject data) {
		try {
			String id = data.getString("id");	
			Vector2D p = new Vector2D(data.getJSONArray("p").getDouble(0), data.getJSONArray("p").getDouble(1));
			Vector2D v = new Vector2D(data.getJSONArray("v").getDouble(0), data.getJSONArray("v").getDouble(1));
			double m = data.getDouble("m");
			
			return new Body (id, v, p, m);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Falla el data");
		}	
	}
	//Si el no es un basic devuelve null
	//Si es un basic pero está mal lanza una excepcion

	
	@Override
	public JSONObject createData() {
		JSONObject jo = new JSONObject();
		Vector2D p = new Vector2D(0.0e00,0.0e00);
		Vector2D v = new Vector2D(0.05e04,0.0e00);
		
		jo.put("id", "b1");
		jo.put("p", p.asJSONArray());
		jo.put("v", v.asJSONArray());
		jo.put("m", 5.97e24);

		
		return jo;
	}
	//Metemos valores por defecto (figura 2), para los data
}
