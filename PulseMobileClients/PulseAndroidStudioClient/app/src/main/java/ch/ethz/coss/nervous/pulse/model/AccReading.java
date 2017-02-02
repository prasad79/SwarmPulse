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

public class AccReading extends Visual implements Serializable{

	public double x, y, z;

	public AccReading(String uuid, double x, double y, double z, long timestamp, VisualLocation location) {
		type = 1;
		this.uuid = uuid;
		this.x = x;
		this.y = y;
		this.z = z;
		this.timestamp = timestamp;
		this.location = location;
		serialVersionUID = 1L;
	}

	public  String getJsonString() {
		JSONObject json = new JSONObject();
		try {
			json.put("uuid", uuid);
			json.put("id", type);
			json.put("x", x);
			json.put("y", y);
			json.put("z", z);
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
		return "AccReading = (" + "," + timestamp + ") -> " + "(" + x + "," + y + "," + z + "), GPS coordinates -> " + Arrays.toString(location.latnLong);
	}





}
