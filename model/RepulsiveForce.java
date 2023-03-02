package simulator.model;

import java.util.List;

public class RepulsiveForce implements ForceLaws {
	protected double G;

	//CONSTRUCTORES
	public RepulsiveForce (double g) {
		super();
		G = g;
	}


	@Override
	public void apply(List<Body> bs) {
		double f;
		
		for(int i = 0; i < bs.size(); i++) {
			if(bs.get(i).getMass() != 0.0) {
				for(int j = 0; j < bs.size();j++) {
					if(j != i) {
						double d = bs.get(j).getPosition().distanceTo(bs.get(i).getPosition());	//usamos el distanceTo de Vector2D
						f = G*bs.get(i).getMass()*bs.get(j).getMass()/(d*d);
						//Ya hemos reseteado la fuerza en el simulador
						bs.get(i).addForce(bs.get(i).getPosition().minus(bs.get(j).getPosition()).direction().scale(f));
						}
				}
			}
		}
	}
}
