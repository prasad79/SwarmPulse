/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ETH Zürich.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Contributors:
 *     Prasad Pulikal - prasad.pulikal@gess.ethz.ch  - Initial design and implementation
 *******************************************************************************/
package ch.ethz.coss.nervous.pulse;

public class Constants {

	public static boolean DUMMY_DATA_COLLECT = false;

	protected static final int SENSOR_TYPE_ACCELEROMETER = 0;
	protected static final int SENSOR_TYPE_LIGHT = 1;
	protected static final int SENSOR_TYPE_NOISE = 2;

	protected static final int MEDIA_VISUAL_TYPE_TEXT = 0;
	protected static final int MEDIA_VISUAL_TYPE_PHOTO = 1;
	protected static final int MEDIA_VISUAL_TYPE_VIDEO = 2;

	public static final String helpHtml = "The <b>SwarmPulse</b> service allows for collective visualization and sharing of mobile sensor data, text messages, media files and more.. <br><br><b>*******HOW-TO*******</b><br><p style=\"text-align: left; padding: 10px\"><b>Share</b><br>Use the SwarmPulse mobile app:<br>1. To view current Light and Sound sensor readings on your mobile phone.<br>2. To share or upload the sensor reading to the server as and when you want to.<br>3. Android and iOS versions available on respective App Stores.<br><br><b>Visualize</b><br>1. On you desktop or laptop browser visit <b><i>www.swarmpulse.net</i></b><br>2. Use the Real-Time mode or Time-Machine mode to visualize Messages / Light sensor / Sound sensor data.<br>3. The \"Clock\" icon on the top right corner is used to switch between Real-Time and Time-Machine modes.<br>4. Real-Time mode:<br>&nbsp;&nbsp;    .  Allows for visualizing data as and when it is shared by users.<br>5. Time-Machine mode:<br>&nbsp;&nbsp;     .  Allows the user to go back in time and visualize the data in the past.<br>&nbsp;&nbsp;     .  Select a time and date by using the date input fields at the bottom of the browser and click on fetch to view the data.<br> &nbsp;&nbsp;     .  This mode shows data that is 30 minutes from the time chosen by the user.</p><b>********************</b><br></div>";

}
