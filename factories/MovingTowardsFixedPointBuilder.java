package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder  extends Builder <ForceLaws>{
	Vector2D c = new Vector2D();
	double g = 9.81;
	
	public MovingTowardsFixedPointBuilder () {
		tipo = "mtfp";
		desc = "movimiento hacia el centro del Universo";
	}
	
	@Override
	public ForceLaws createTheInstance(JSONObject datos) {
		try {
			Vector2D x = new Vector2D(datos.getJSONArray("c").getDouble(0), datos.getJSONArray("c").getDouble(1));
			c = x;
		}
		catch (Exception e) {}
		
		try {
			double y = datos.getDouble("g");
			g = y;
		}
		catch (Exception e) {}
		
		
		return new MovingTowardsFixedPoint (g, c);
	}
	
	
	@Override
	public JSONObject createData() {
		JSONObject jo = new JSONObject();
		Vector2D c = new Vector2D();
		
		jo.put("c", c.asJSONArray());
		jo.put("g", 9.81);

		return jo;
	}
	//Metemos valores por defecto (figura 2), para los data

}
