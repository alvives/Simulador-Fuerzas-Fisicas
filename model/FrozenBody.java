package simulator.model;

import simulator.misc.Vector2D;

public class FrozenBody extends Body {
	private double cont;
	private double change;
	private boolean frozen;
	
	public FrozenBody(String id, Vector2D v, Vector2D p, double m, double change) {
		super(id, v, p, m);
		this.change = change;
		this.cont = 0;
		this.frozen = false;
	}


	void move(double t) {
		if(frozen) {
			cont = cont + t;
			if(cont >= change) {
				frozen = false;
				cont = 0;
			}			
		}
		else {
			super.move(t);
			cont = cont + t;
			if(cont >= change){
				frozen = true;
				cont = 0;
			}
				
		}	
	}
}
