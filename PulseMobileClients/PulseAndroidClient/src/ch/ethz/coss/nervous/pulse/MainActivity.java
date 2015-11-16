/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ETH Zurich.
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import ch.ethz.coss.nervous.pulse.R;
import ch.ethz.coss.nervous.pulse.R.id;
import ch.ethz.coss.nervous.pulse.R.layout;
import ch.ethz.coss.nervous.pulse.R.menu;
import ch.ethz.coss.nervous.pulse.activities.AboutActivity;
import ch.ethz.coss.nervous.pulse.activities.HelpActivity;
import ch.ethz.coss.nervous.pulse.activities.LightSensorReadingActivity;
import ch.ethz.coss.nervous.pulse.activities.NoiseSensorReadingActivity;
import ch.ethz.coss.nervous.pulse.activities.ParentActivity;
import ch.ethz.coss.nervous.pulse.activities.SettingsActivity;
import ch.ethz.coss.nervous.pulse.activities.TextMessageUploadActivity;

@SuppressLint({ "Wakelock" })
public class MainActivity extends ParentActivity {

	public static final String DEBUG_TAG = "MainActivityPulse";

	WakeLock wakeLock;
	WifiLock wifiLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Application.initSensorService(MainActivity.this);

		((ImageButton) findViewById(R.id.icon_light)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (GPSLocation.GPS_AVAILABLE) {
					Application.registerListener(Sensor.TYPE_LIGHT);
					// Start and intent for the logged out activity
					startActivity(new Intent(MainActivity.this, LightSensorReadingActivity.class));
				} else {
					GPSLocation.getInstance(MainActivity.this);
					if (!GPSLocation.GPS_AVAILABLE) {
						showLocationAlert();
					} else {
						Application.registerListener(Sensor.TYPE_LIGHT);
						startActivity(new Intent(MainActivity.this, LightSensorReadingActivity.class));
					}
				}

			}
		});

		((ImageButton) findViewById(R.id.icon_sound)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (GPSLocation.GPS_AVAILABLE) {
					Application.registerListener(0); // FOR SOUND

					startActivity(new Intent(MainActivity.this, NoiseSensorReadingActivity.class));
				} else {
					GPSLocation.getInstance(MainActivity.this);
					if (!GPSLocation.GPS_AVAILABLE) {
						showLocationAlert();
					} else {
						Application.registerListener(0);
						startActivity(new Intent(MainActivity.this, NoiseSensorReadingActivity.class));
					}
				}

			}
		});

		ImageButton txtButton = ((ImageButton) findViewById(R.id.icon_text));
		txtButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (GPSLocation.GPS_AVAILABLE) {
					startActivity(new Intent(MainActivity.this, TextMessageUploadActivity.class));
				} else {
					GPSLocation.getInstance(MainActivity.this);
					if (!GPSLocation.GPS_AVAILABLE) {
						showLocationAlert();
					} else {
						startActivity(new Intent(MainActivity.this, TextMessageUploadActivity.class));
					}
				}
			}
		});

		ImageButton helpButton = ((ImageButton) findViewById(R.id.icon_help));
		helpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, HelpActivity.class));
			}
		});

		((ImageButton) findViewById(R.id.icon_visual)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showVisualizationAlert();

			}
		});

		((ImageButton) findViewById(R.id.icon_settings)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Start and intent for the logged out activity
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));

			}
		});

		((ImageButton) findViewById(R.id.icon_about)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Start and intent for the logged out activity
				startActivity(new Intent(MainActivity.this, AboutActivity.class));

			}
		});

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		wakeLock.acquire();

		WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "WifyLock");
		wifiLock.acquire();

		// GPSLocation.getInstance(this);

		handleShareIntent();

	}

	private OnSharedPreferenceChangeListener listener;

	protected synchronized void onDestroy() {
		Log.d(DEBUG_TAG, "onDestroy");
		if (Application.sensorManager != null)
			Application.sensorManager.unregisterListener(Application.sensorService);
		if (Application.synchWriter != null)
			Application.synchWriter.stop();
		if (wakeLock != null)
			wakeLock.release();
		if (wifiLock != null)
			wifiLock.release();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onPause() {
		Log.d(DEBUG_TAG, "onPause");
		super.onPause();
	}

	public void onResume() {
		Log.d(DEBUG_TAG, "onResume");
		Application.unregisterSensorListeners();
		super.onResume();

	}

	public void onStop() {
		Log.d(DEBUG_TAG, "onStop");
		super.onStop();
	}

	public void onStart() {
		Log.d(DEBUG_TAG, "onStart");
		Application.unregisterSensorListeners();
		super.onStart();

	}

}
