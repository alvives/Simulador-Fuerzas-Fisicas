package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLosingBody;

public class MassLosingBodyBuilder extends Builder <Body>{
	
	public MassLosingBodyBuilder () {
		tipo = "mlb";
		desc = "cuerpo que pierde masa";
	}

	@Override
	public Body createTheInstance(JSONObject datos) {
		try {
			String id = datos.getString("id");	
			Vector2D p = new Vector2D(datos.getJSONArray("p").getDouble(0), datos.getJSONArray("p").getDouble(1));
			Vector2D v = new Vector2D(datos.getJSONArray("v").getDouble(0), datos.getJSONArray("v").getDouble(1));
			double m = datos.getDouble("m");
			double freq = datos.getDouble("freq");
			double factor = datos.getDouble("factor");
			
			return new MassLosingBody (id, v, p, m, factor, freq);
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
		jo.put("freq", 1e3);
		jo.put("factor", 1e-3);

		
		return jo;
	}
	//Metemos valores por defecto (figura 2), para los data

}
