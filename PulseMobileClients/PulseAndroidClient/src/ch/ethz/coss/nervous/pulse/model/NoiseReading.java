package ch.ethz.coss.nervous.pulse.model;

import java.io.Serializable;
import java.util.Arrays;

public class NoiseReading extends Visual implements Serializable {

	public double soundVal;

	public NoiseReading() {

	}

	public NoiseReading(String uuid, double soundVal, long timestamp, long volatility, VisualLocation loc) {
		type = 1;
		this.uuid = uuid;
		this.soundVal = soundVal;
		this.timestamp = timestamp;
		this.volatility = volatility;
		this.location = loc;
		serialVersionUID = 3L;
	}

	@Override
	public String toString() {
		return "NoiseReading = (" + timestamp + ") -> " + "(" + soundVal + ") @ " + Arrays.toString(location.latnLong);
	}
}
