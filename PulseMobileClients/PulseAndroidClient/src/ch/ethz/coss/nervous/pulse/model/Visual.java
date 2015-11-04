package ch.ethz.coss.nervous.pulse.model;

import java.io.Serializable;

import ch.ethz.coss.nervous.pulse.utils.Utils;

public abstract class Visual implements Serializable {
	public int type = 0; // 0-light, 1- sound, 2 - text
	protected long serialVersionUID;
	public VisualLocation location;
	public long timestamp;
	public String uuid;
//	public long volatility; //Data Volatility in seconds not milliseconds
	
	
	
	
}
