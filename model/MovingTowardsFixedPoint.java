package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {
	protected double G;
	protected Vector2D c;

	
	
	public MovingTowardsFixedPoint(double G, Vector2D c) {
		super();
		this.G = G;
		this.c = c;
	}


	
	
	@Override
	public void apply(List<Body> bs) {
		for(int i = 0; i < bs.size(); i++) {
			//la fuerza la hemos reseteado
			//d = bs.get(i).getPosition().minus(c)     y    .scale(-G) y .scale(masa)
			bs.get(i).addForce((bs.get(i).getPosition().minus(c)).direction().scale(-G).scale(bs.get(i).getMass()));
			}
	}

}
