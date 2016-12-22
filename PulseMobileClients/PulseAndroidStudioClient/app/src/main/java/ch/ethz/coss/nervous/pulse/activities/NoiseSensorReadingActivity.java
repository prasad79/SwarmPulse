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
import ch.ethz.coss.nervous.pulse.R;
import ch.ethz.coss.nervous.pulse.SensorService;
import ch.ethz.coss.nervous.pulse.R.id;
import ch.ethz.coss.nervous.pulse.R.layout;
import ch.ethz.coss.nervous.pulse.model.NoiseReading;
import ch.ethz.coss.nervous.pulse.utils.Utils;

@SuppressLint({ "Wakelock", "InlinedApi" })
public class NoiseSensorReadingActivity extends SensorReadingActivity {

	public static final String DEBUG_TAG = "NoiseSensorReadingActivityPulse";

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound);

		TextView tv = (TextView) findViewById(R.id.data_storage_hint2);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(NoiseSensorReadingActivity.this);

		tv.setText(
				"Please change the 'Data Retention' settings to allow for data storage onto the SwarmPulse servers. Click here to change the settings");

		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(NoiseSensorReadingActivity.this, SettingsActivity.class));

			}
		});

		// Sign up button click handler
		final Button submitButton = ((Button) findViewById(R.id.submit));

		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (reading != null) {
					Utils.showProgress(NoiseSensorReadingActivity.this);

					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(NoiseSensorReadingActivity.this);
					if (prefs.getBoolean("data_rentention", false)) {
						reading.volatility = Long.parseLong(prefs.getString("time_limit_data_retention", "-1"));
					} else
						reading.volatility = 0;

					Application.pushReadingToServer(reading, NoiseSensorReadingActivity.this);
					submitButton.setEnabled(false);
					submitButton.setText("Please wait for 5 seconds.");
					Timer buttonTimer = new Timer();
					buttonTimer.schedule(new TimerTask() {

						@Override
						public void run() {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									submitButton.setText("Share sensor data");
									submitButton.setEnabled(true);
								}
							});
						}
					}, 5000);
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
		try {
			unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stopService(intent);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			refreshReading(intent);
		}
	};

	NoiseReading reading;

	private void refreshReading(Intent intent) {
		reading = (NoiseReading) intent.getSerializableExtra("NoiseReading");

		TextView txtLightValue = (TextView) findViewById(R.id.noiseValueTF);
		if (reading != null)
			txtLightValue.setText("" + reading.soundVal + " dB");
		else
			txtLightValue.setText("Error in reading the Noise sensor readings");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Application.stopSensor();
	}

}
