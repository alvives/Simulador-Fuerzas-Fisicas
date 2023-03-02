package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator {
	private double dt;
	private ForceLaws fuerza;
	
	private double at;
	private List<Body> bodies = new ArrayList<Body>(); 
	
	//CONSTRUCTOR
	public PhysicsSimulator(ForceLaws fuerza, double dt) {
		if(dt < 0 || fuerza == null)
			throw new IllegalArgumentException();
		else {
			this.dt = dt;
			this.fuerza = fuerza;
			this.at = 0.0;
		}
	}		
	
	//METODOS
	public void advance() {
		for(int i = 0; i < bodies.size(); i++) {
			bodies.get(i).resetForce();
		}
		fuerza.apply(bodies);
		for(int i = 0; i < bodies.size(); i++) {
			bodies.get(i).move(dt);
		}
		at += dt;
	}
	public void addBody(Body b) {
		int cont = 0;
		boolean encontrado = false;
		
		while(!encontrado && cont < bodies.size()) {
			if (bodies.get(cont).getId() == b.getId()) {
				encontrado = true;
			}
			else 
				cont++;
		}
		
		if(!encontrado)
			bodies.add(b);
		else
			throw new IllegalArgumentException();
	}
	
	public JSONObject getState() {
		JSONObject jo = new JSONObject();
		jo.put("time", at);
		
		JSONArray ja = new JSONArray();
		for(int i = 0; i < bodies.size(); i++) {
			ja.put(bodies.get(i).getState());
		}
		jo.put("bodies", ja);

		return jo;
	}

	public String toString() {
		return getState().toString();
	}
	
}
