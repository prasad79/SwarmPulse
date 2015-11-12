package ch.ethz.coss.nervous.pulse.model;

import java.io.Serializable;

public class AccReading extends Visual implements Serializable {

	public double x, y, z;

	public AccReading(String uuid, double x, double y, double z, long timestamp, VisualLocation location) {
		this.uuid = uuid;
		this.x = x;
		this.y = y;
		this.z = z;
		this.timestamp = timestamp;
		this.location = location;
		serialVersionUID = 1L;
	}

	@Override
	public String toString() {
		return "AccReading = (" + "," + timestamp + ") -> " + "(" + x + "," + y + "," + z + ")";
	}
}
