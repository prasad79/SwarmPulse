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

import java.io.Serializable;
import java.util.Arrays;

public class TextVisual extends Visual implements Serializable {

	public String textMsg = null;

	public TextVisual() {
	}

	public TextVisual(String uuid, String txtMsg, long timestamp, long volatility, VisualLocation loc) {
		type = 2;
		this.uuid = uuid;
		this.textMsg = txtMsg;
		this.timestamp = timestamp;
		this.volatility = volatility;
		this.location = loc;
		serialVersionUID = 4L;
	}

	@Override
	public String toString() {
		return "TextVisual = (" + timestamp + ") -> " + "(" + textMsg + ") @ " + Arrays.toString(location.latnLong);
	}
}
