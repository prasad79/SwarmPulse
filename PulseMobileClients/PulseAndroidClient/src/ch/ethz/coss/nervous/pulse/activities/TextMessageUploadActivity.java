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
package ch.ethz.coss.nervous.pulse.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ch.ethz.coss.nervous.pulse.Application;
import ch.ethz.coss.nervous.pulse.Constants;
import ch.ethz.coss.nervous.pulse.GPSLocation;
import ch.ethz.coss.nervous.pulse.R;
import ch.ethz.coss.nervous.pulse.R.id;
import ch.ethz.coss.nervous.pulse.R.layout;
import ch.ethz.coss.nervous.pulse.model.TextVisual;
import ch.ethz.coss.nervous.pulse.model.VisualLocation;
import ch.ethz.coss.nervous.pulse.utils.Utils;

@SuppressLint({ "Wakelock", "InlinedApi" })
public class TextMessageUploadActivity extends SensorReadingActivity {

	public static final String DEBUG_TAG = "NoiseSensorReadingActivityPulse";

	private Intent intent;
	public static int counter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		String message = null;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			message = extras.getString("MESSAGE");

		}

		setContentView(R.layout.activity_msg_upload);
		final TextView msgTV = (TextView) findViewById(R.id.messageTF);

		TextView tv = (TextView) findViewById(R.id.data_storage_hint3);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TextMessageUploadActivity.this);

		tv.setText(
				"Please change the 'Data Retention' settings to allow for data storage onto the SwarmPulse servers. Click here to change the settings");

		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(TextMessageUploadActivity.this, SettingsActivity.class));

			}
		});

		// Sign up button click handler

		final Button submitButton = ((Button) findViewById(R.id.submit));
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String message = (msgTV.getText()).toString();

				if (Constants.DUMMY_DATA_COLLECT) {
					counter++;

					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(TextMessageUploadActivity.this);
					boolean volatility = prefs.getBoolean("data_rentention", true);

					TextVisual txtMsg = new TextVisual(Application.uuid.toString(), Utils.University_links[counter],
							System.currentTimeMillis(), volatility ? -1 : 0, Utils.University_link_coords[counter]);

					Application.pushReadingToServer(txtMsg, TextMessageUploadActivity.this);

				} else {
					message = message.trim();
					if (message.length() >= 2) {
						TextVisual txtMsg = new TextVisual(Application.uuid.toString(), message,
								System.currentTimeMillis(), -1, new VisualLocation(
										GPSLocation.getInstance(TextMessageUploadActivity.this).getLocation()));
						if (txtMsg != null) {
							Utils.showProgress(TextMessageUploadActivity.this);

							Application.pushReadingToServer(txtMsg, TextMessageUploadActivity.this);

							msgTV.setText("");

							submitButton.setEnabled(false);
							submitButton.setText("Please wait for 5 seconds.");
							Timer buttonTimer = new Timer();
							buttonTimer.schedule(new TimerTask() {

								@Override
								public void run() {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											submitButton.setText("Share message");
											submitButton.setEnabled(true);
										}
									});
								}
							}, 5000);
						}
					}
				}

			}
		});

		if (message != null) {
			msgTV.setText(message);

		}

	}

}
