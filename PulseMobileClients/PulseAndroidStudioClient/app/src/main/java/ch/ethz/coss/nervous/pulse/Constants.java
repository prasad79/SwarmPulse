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
package ch.ethz.coss.nervous.pulse;

public class Constants {

	public static boolean DUMMY_DATA_COLLECT = false;

	protected static final int SENSOR_TYPE_LIGHT = 0;
	protected static final int SENSOR_TYPE_NOISE = 1;
	protected static final int SENSOR_TYPE_ACCELEROMETER = 2;
	protected static final int SENSOR_TYPE_TEMPERATURE = 3;
	protected static final int SENSOR_TYPE_MESSAGES = 4;


	protected static final int MEDIA_VISUAL_TYPE_TEXT = 0;
	protected static final int MEDIA_VISUAL_TYPE_PHOTO = 1;
	protected static final int MEDIA_VISUAL_TYPE_VIDEO = 2;

	public static final String helpHtml = "The <b>SwarmPulse</b> service allows for collective visualization and sharing of mobile sensor data, text messages, media files and more.. <br><br><b>*******HOW-TO*******</b><br><p style=\"text-align: left; padding: 10px\"><b>Share</b><br>Use the SwarmPulse mobile app:<br>1. To view current Light and Sound sensor readings on your mobile phone.<br>2. To share or upload the sensor reading to the server as and when you want to.<br>3. Android and iOS versions available on respective App Stores.<br><br><b>Visualize</b><br>1. On you desktop or laptop browser visit <b><i>www.swarmpulse.net</i></b><br>2. Use the Real-Time mode or Time-Machine mode to visualize Messages / Light sensor / Sound sensor data.<br>3. The \"Clock\" icon on the top right corner is used to switch between Real-Time and Time-Machine modes.<br>4. Real-Time mode:<br>&nbsp;&nbsp;    .  Allows for visualizing data as and when it is shared by users.<br>5. Time-Machine mode:<br>&nbsp;&nbsp;     .  Allows the user to go back in time and visualize the data in the past.<br>&nbsp;&nbsp;     .  Select a time and date by using the date input fields at the bottom of the browser and click on fetch to view the data.<br> &nbsp;&nbsp;     .  This mode shows data that is 30 minutes from the time chosen by the user.</p><b>********************</b><br></div>";

}
