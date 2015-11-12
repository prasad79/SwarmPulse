package ch.ethz.coss.nervous.pulse.model;

import java.io.Serializable;

import ch.ethz.coss.nervous.pulse.utils.Utils;

public abstract class Visual implements Serializable {
	public int type = 0; // 0-light, 1- sound, 2 - text
	protected long serialVersionUID;
	public VisualLocation location;
	public long timestamp;
	public String uuid;
	public long volatility = -1; // Data Volatility in seconds not milliseconds:
									// -2 = Important never clear from map and
									// database, -1 = never erase from database,
									// 0 = do not store in database, 1 or more =
									// milliseconds to keeps

}
