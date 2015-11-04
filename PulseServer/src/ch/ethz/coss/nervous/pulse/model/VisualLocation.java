package ch.ethz.coss.nervous.pulse.model;

import java.io.Serializable;

public class VisualLocation implements Serializable {

	public double[] latnLong ={0.0,0.0};
	
	public VisualLocation() {
	}

	public VisualLocation(double[] location) {
		latnLong = location;
	}
}
