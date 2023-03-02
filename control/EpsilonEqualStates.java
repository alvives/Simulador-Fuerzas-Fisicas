package simulator.control;

import org.json.JSONObject;
import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator {
	private double eps;
	protected Vector2D v;

	//CONSTRUCTOR
	public EpsilonEqualStates(double eps) {
		this.eps = eps;
	}
	
	//MÉTODO
	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		boolean b;
		int cont = 0;	
		
		b = s1.getDouble("time") == s2.getDouble("time") && s1.getJSONArray("bodies").length() == s2.getJSONArray("bodies").length();
		
		while(b && cont < s1.getJSONArray("bodies").length()) {
			Vector2D v1_pos = new Vector2D(s1.getJSONArray("bodies").getJSONObject(cont).getJSONArray("p").getDouble(0), s1.getJSONArray("bodies").getJSONObject(cont).getJSONArray("p").getDouble(1));
			Vector2D v2_pos = new Vector2D(s2.getJSONArray("bodies").getJSONObject(cont).getJSONArray("p").getDouble(0), s2.getJSONArray("bodies").getJSONObject(cont).getJSONArray("p").getDouble(1));
			Vector2D v1_f = new Vector2D(s1.getJSONArray("bodies").getJSONObject(cont).getJSONArray("f").getDouble(0), s1.getJSONArray("bodies").getJSONObject(cont).getJSONArray("f").getDouble(1));
			Vector2D v2_f = new Vector2D(s2.getJSONArray("bodies").getJSONObject(cont).getJSONArray("f").getDouble(0), s2.getJSONArray("bodies").getJSONObject(cont).getJSONArray("f").getDouble(1));
			Vector2D v1_v = new Vector2D(s1.getJSONArray("bodies").getJSONObject(cont).getJSONArray("v").getDouble(0), s1.getJSONArray("bodies").getJSONObject(cont).getJSONArray("v").getDouble(1));
			Vector2D v2_v = new Vector2D(s2.getJSONArray("bodies").getJSONObject(cont).getJSONArray("v").getDouble(0), s2.getJSONArray("bodies").getJSONObject(cont).getJSONArray("v").getDouble(1));
			
			
			if(!s1.getJSONArray("bodies").getJSONObject(cont).getString("id").equals(s2.getJSONArray("bodies").getJSONObject(cont).getString("id"))
					|| Math.abs(s1.getJSONArray("bodies").getJSONObject(cont).getDouble("m")-s2.getJSONArray("bodies").getJSONObject(cont).getDouble("m")) > eps
					|| v1_pos.distanceTo(v2_pos) > eps || v1_v.distanceTo(v2_v) > eps
					
					||  v1_f.distanceTo(v2_f) > eps //Si se comenta esta linea no da fallo al comparar con el epsilon, ya que la fuerza 
		            //al estar elevada a un número tan alto, los decimales que sean diferentes van a dar fallo
					) {
				b = false;
			}
			cont++;
		}

		return b;
	}
	

}
