package ch.ethz.coss.nervous.pulse.model;

import java.io.Serializable;
import java.util.Arrays;

public class NoiseReading extends Visual implements Serializable {

	public double soundVal;

	public NoiseReading(String uuid, double soundVal, long timestamp, VisualLocation loc) {
		type = 1;
		this.uuid = uuid;
		this.soundVal = soundVal;
		this.timestamp = timestamp;
		this.location = loc;
		serialVersionUID = 3L;
	}

	@Override
	public String toString() {
		return "NoiseReading = (" + timestamp + ") -> " + "(" + soundVal
				+ ") @ " + Arrays.toString(location.latnLong);
	}
}
