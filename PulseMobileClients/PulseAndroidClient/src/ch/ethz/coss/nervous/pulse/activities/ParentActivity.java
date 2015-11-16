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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ch.ethz.coss.nervous.pulse.GPSLocation;

@SuppressLint({ "Wakelock" })
public class ParentActivity extends Activity {
	public static final String DEBUG_TAG = "ParentActivityPulse";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		String action = intent.getAction();
		String type = intent.getType();

		Log.d(DEBUG_TAG, action + " -- " + type);
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			}
		}

		intent = null;

	}

	protected void handleShareIntent() {
		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			}
		}
	}

	private void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {
			Intent msgIntent = new Intent();
			msgIntent.setClass(this, TextMessageUploadActivity.class);
			msgIntent.putExtra("MESSAGE", sharedText);

			if (GPSLocation.GPS_AVAILABLE) {
				System.out.println("Message " + sharedText + " received.");
				startActivity(msgIntent);
			} else {
				GPSLocation.getInstance(ParentActivity.this);
				if (!GPSLocation.GPS_AVAILABLE) {
					showLocationAlert();
				} else {
					startActivity(msgIntent);
				}
			}
		}

	}

	protected void showLocationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ParentActivity.this);
		builder.setTitle("Location settings disabled"); // GPS not found
		builder.setMessage(
				"This application requires the usage of location features. Please change your location settings."); // Want
		// to
		// enable?
		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
		builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				System.exit(0);
			}
		});
		builder.create().show();
		Toast.makeText(ParentActivity.this,
				"You location could not be determined. Please enable your Network Providers.", Toast.LENGTH_LONG)
				.show();

	}

	protected void showVisualizationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ParentActivity.this);
		builder.setTitle("Information"); // GPS not found
		builder.setMessage(
				"Please note that visualization of the SwarmPulse website (http://www.swarmpulse.net) is best viewed on desktop/laptop browser.\n The SwarmPulse website is not currently optimized for mobile browsers."); // Want
		// to
		// enable?
		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				// writeVisualAlertPrefs();

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.swarmpulse.net"));
				startActivity(browserIntent);
			}
		});

		builder.create().show();

	}

	@Override
	protected synchronized void onDestroy() {
		Log.d(DEBUG_TAG, "onDestroy");

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		Log.d(DEBUG_TAG, "onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.d(DEBUG_TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onStop() {
		Log.d(DEBUG_TAG, "onStop");
		super.onStop();
	}

	@Override
	public void onStart() {
		Log.d(DEBUG_TAG, "onStart");
		super.onStart();
	}
}