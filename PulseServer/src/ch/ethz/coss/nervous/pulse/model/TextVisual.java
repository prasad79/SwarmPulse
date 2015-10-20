package ch.ethz.coss.nervous.pulse.model;

import java.io.Serializable;
import java.util.Arrays;

public class TextVisual extends Visual implements Serializable {

	public String textMsg = null;

	
	public TextVisual() {
	}
	
	public TextVisual(String uuid, String txtMsg, long timestamp, VisualLocation loc) {
		type = 2;
		this.uuid = uuid;
		this.textMsg = txtMsg;
		this.timestamp = timestamp;
		this.location = loc;
		serialVersionUID = 4L;
	}

	@Override
	public String toString() {
		return "TextVisual = (" + timestamp + ") -> " + "(" + textMsg + ") @ "
				+ Arrays.toString(location.latnLong);
	}
}
