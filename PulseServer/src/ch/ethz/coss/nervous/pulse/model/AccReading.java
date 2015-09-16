package ch.ethz.coss.nervous.pulse.model;

import java.io.Serializable;

public class AccReading extends Visual implements Serializable {

	public double x, y, z;

	public AccReading(double x, double y, double z, long timestamp,
			VisualLocation location) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.timestamp = timestamp;
		this.location = location;
		serialVersionUID = 1L;
	}

	public String toString() {
		return "AccReading = (" + "," + timestamp + ") -> " + "(" + x + "," + y
				+ "," + z + ")";
	}
}
