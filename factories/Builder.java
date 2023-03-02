package simulator.factories;

import org.json.JSONObject;

public abstract class Builder <T> {
	String tipo;
	String desc;
	
	public T createInstance(JSONObject info) {
		if(!info.getString("type").equals(tipo))
			return null;
		
		return createTheInstance(info.getJSONObject("data"));
	}
	public abstract T createTheInstance (JSONObject data);
	
	
	
	public JSONObject getBuilderInfo() {
		JSONObject jo = new JSONObject();
		jo.put("type", tipo);
		jo.put("data", createData());
		jo.put("desc", desc);
		
		return jo;
	}	
	public JSONObject createData() {
		return new JSONObject();
	}
	
}