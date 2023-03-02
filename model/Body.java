package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

//Crear clases extras
public class Body {
	//ATRIBUTOS
	protected String id;
	protected Vector2D v;
	protected Vector2D f;
	protected Vector2D p;
	protected double m;
	
	//CONSTRUCTOR:
	public Body(String id, Vector2D v, Vector2D p, double m) {
		this.id = id;
		this.v = v;
		this.f = new Vector2D();
		this.p = p;
		this.m = m;
	}	
	
	
	//GETTERS
	public String getId() {return this.id;}
	public Vector2D getVelocity(){return this.v;} 
	public Vector2D getForce() {return this.f;}
	public Vector2D getPosition() {return this.p;}
	public double getMass() {return this.m;}
	
	//SETTERS
	void addForce(Vector2D fuerza) {
		//añade la fuerza fuerza al vector de fuerza del cuerpo (usando el método plus de la clase Vector2D).
		this.f = f.plus(fuerza);
	}
	void resetForce() {
		//pone el valor del vector de fuerza a (0, 0)
		f = new Vector2D();
	}
	void move(double t) {
		Vector2D a;
		
		//calculo la aceleración
		if(m == 0) {
			a = new Vector2D();
		}
		else {
			a = new Vector2D(f.getX()/m, f.getY()/m);
		}
		
		//Calculamos la posición:
		this.p = p.plus(v.scale(t).plus(a.scale(t*t/2)));
	
		//Calculamos la velocidad:
		this.v = v.plus(a.scale(t));
	}

	
	//devuelve la siguiente información del cuerpo en formato JSON (como JSONObject): { “id": id, "m": m, "p": ~p, "v": ~v, "f": ~ f }
	public JSONObject getState() {
		JSONObject jo1 = new JSONObject();
		jo1.put("id", id);
		jo1.put("m", m);
		jo1.put("p", p.asJSONArray());
		jo1.put("v", v.asJSONArray());
		jo1.put("f", f.asJSONArray());
		return jo1;
	}
	
	public String toString() {
		return getState().toString();
	}
	
	
	

	
}
