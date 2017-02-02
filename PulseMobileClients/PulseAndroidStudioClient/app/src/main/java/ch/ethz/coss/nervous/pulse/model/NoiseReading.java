/*******************************************************************************
 *     SwarmPulse - A service for collective visualization and sharing of mobile 
 *     sensor data, text messages and more.
 *
 *     Copyright (C) 2015 ETH Zürich, COSS
 *
 *     This file is part of SwarmPulse.
 *
 *     SwarmPulse is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SwarmPulse is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SwarmPulse. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * 	Author:
 * 	Prasad Pulikal - prasad.pulikal@gess.ethz.ch  - Initial design and implementation
 *******************************************************************************/
package ch.ethz.coss.nervous.pulse.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

public class NoiseReading extends Visual implements Serializable{

	public double soundVal;

	public NoiseReading() {

	}

	public NoiseReading(String uuid, double soundVal, long timestamp, long volatility, VisualLocation loc) {
		type = 5;
		this.uuid = uuid;
		this.soundVal = soundVal;
		this.timestamp = timestamp;
		this.volatility = volatility;
		this.location = loc;
		serialVersionUID = 3L;
	}



	public  String getJsonString() {
		JSONObject json = new JSONObject();
		try {
			json.put("uuid", uuid);
			json.put("id", type);
			json.put("Db", soundVal);
			json.put("timestamp", timestamp);
			json.put("lat", location.latnLong[0]);
			json.put("long", location.latnLong[1]);
			json.put("volatility", volatility);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();

	}

	@Override
	public String toString() {
		return "NoiseReading = (" + timestamp + ") -> " + "(" + soundVal + ") @ " + Arrays.toString(location.latnLong);
	}
}
