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
package ch.ethz.coss.nervous.pulse.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ch.ethz.coss.nervous.pulse.Application;
import ch.ethz.coss.nervous.pulse.MainActivity;
import ch.ethz.coss.nervous.pulse.R;
import ch.ethz.coss.nervous.pulse.SensorService;
import ch.ethz.coss.nervous.pulse.R.id;
import ch.ethz.coss.nervous.pulse.R.layout;
import ch.ethz.coss.nervous.pulse.model.LightReading;
import ch.ethz.coss.nervous.pulse.utils.Utils;

@SuppressLint({ "Wakelock", "InlinedApi" })
public class LightSensorReadingActivity extends SensorReadingActivity {

	public static final String DEBUG_TAG = "LightSensorReadingActivityPulse";

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light);

		TextView tv = (TextView) findViewById(R.id.data_storage_hint);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LightSensorReadingActivity.this);

		tv.setText(
				"Please change the 'Data Retention' settings to allow for data storage onto the SwarmPulse servers. Click here to change the settings");

		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LightSensorReadingActivity.this, SettingsActivity.class));

			}
		});
		// Sign up button click handler
		final Button submitButton = (Button) findViewById(R.id.submit);

		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (reading != null) {

					if (reading.location == null) {
						Utils.showAlert(LightSensorReadingActivity.this, "Location not found.",
								"Unable to find the location which is required for visualizing the data on the map. Please restart you app.");
						return;
					}

					Utils.showProgress(LightSensorReadingActivity.this);

					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(LightSensorReadingActivity.this);
					if (prefs.getBoolean("data_rentention", true)) {
						reading.volatility = Long.parseLong(prefs.getString("time_limit_data_retention", "-1"));
					} else
						reading.volatility = 0;

					Application.pushReadingToServer(reading, LightSensorReadingActivity.this);
					submitButton.setEnabled(false);
					submitButton.setText("Please wait for 5 seconds.");
					final Timer buttonTimer = new Timer();

					buttonTimer.scheduleAtFixedRate(new TimerTask() {
						int counter = 5;

						@Override
						public void run() {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (counter < 1) {

										submitButton.setText("Share sensor data");
										submitButton.setEnabled(true);
										buttonTimer.cancel();
									} else {
										submitButton.setText("Please wait for " + counter + " seconds.");
										counter--;
									}

								}
							});
						}
					}, 1000, 1000);
				}

			}
		});

		intent = new Intent(this, SensorService.class);

	}

	@Override
	public void onResume() {
		super.onResume();
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(SensorService.BROADCAST_READING_ACTION));
	}

	@Override
	public void onPause() {
		super.onPause();

		// try {
		// unregisterReceiver(broadcastReceiver);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// stopService(intent);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			refreshReading(intent);
		}
	};

	LightReading reading = null;

	private void refreshReading(Intent intent) {
		reading = (LightReading) intent.getSerializableExtra("LightReading");

		TextView txtLightValue = (TextView) findViewById(R.id.lightValueTF);
		if (reading != null)
			txtLightValue.setText("" + reading.lightVal + " lux");
		else
			txtLightValue.setText("Error in reading the light sensor readings");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		unregisterReceiver(broadcastReceiver);
		stopService(intent);
		Application.unregisterSensorListeners();
	}

}
