package simulator.control;

import org.json.JSONObject;

public class MassEqualStates implements StateComparator {

	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		boolean b;
		int cont = 0;	
		
		b = s1.getDouble("time") == s2.getDouble("time") && s1.getJSONArray("bodies").length() == s2.getJSONArray("bodies").length();
		
		while(b && cont < s1.getJSONArray("bodies").length()) {
			if(s1.getJSONArray("bodies").getJSONObject(cont).getString("id") != s2.getJSONArray("bodies").getJSONObject(cont).getString("id") 
					|| s1.getJSONArray("bodies").getJSONObject(cont).getDouble("m") != s2.getJSONArray("bodies").getJSONObject(cont).getDouble("m")) {
				b = false;
			}
			cont++;
		}
		
		return b;
	}

}

