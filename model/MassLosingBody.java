package simulator.model;

import simulator.misc.Vector2D;

public class MassLosingBody extends Body{
	private double lossFactor;
	private double lossFrequency;
	private double c = 0.0;

	public MassLosingBody(String id, Vector2D v, Vector2D p, double m, double lossFactor, double lossFrequency) {
		super(id, v, p, m);
		this.lossFactor = lossFactor;
		this.lossFrequency = lossFrequency;
	}
	
	void move(double t) {
		super.move(t);
		
		c = c + t;
		
		if( c >= lossFrequency) {
			m = m * (1 - lossFactor);
			c = 0.0;
		}
			
		/*
		 while(c >= lossFrequency) {
			m = m * (1 - lossFactor);
			c = c - lossFrequency;
		}*/
		
	}
	
}

