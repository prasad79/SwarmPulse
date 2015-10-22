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

@SuppressLint({ "Wakelock" })
public class MainActivity extends Activity {

	public static final String DEBUG_TAG = "MainActivityPulse";

	WakeLock wakeLock;
	WifiLock wifiLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Application.initSensorService(MainActivity.this);

		((ImageButton) findViewById(R.id.icon_light))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(GPSLocation.GPS_AVAILABLE){
							Application.registerListener(Sensor.TYPE_LIGHT);
							// Start and intent for the logged out activity
							startActivity(new Intent(MainActivity.this,
									LightSensorReadingActivity.class));
						}else{
							GPSLocation.getInstance(MainActivity.this);
							if(!GPSLocation.GPS_AVAILABLE){
								showLocationAlert();
							}else {
								Application.registerListener(Sensor.TYPE_LIGHT);
								startActivity(new Intent(MainActivity.this,
										LightSensorReadingActivity.class));
							}	
						}
					
					}
				});

		((ImageButton) findViewById(R.id.icon_sound))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(GPSLocation.GPS_AVAILABLE){
						Application.registerListener(0); // FOR SOUND

						startActivity(new Intent(MainActivity.this,
								NoiseSensorReadingActivity.class));
						}else{
							GPSLocation.getInstance(MainActivity.this);
							if(!GPSLocation.GPS_AVAILABLE){
								showLocationAlert();
							}else {
								Application.registerListener(0);
								startActivity(new Intent(MainActivity.this,
										NoiseSensorReadingActivity.class));
							}	
						}

					}
				});

		ImageButton txtButton = ((ImageButton) findViewById(R.id.icon_text));
		txtButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(GPSLocation.GPS_AVAILABLE){
					startActivity(new Intent(MainActivity.this,
						TextMessageUploadActivity.class));
				}else{
					GPSLocation.getInstance(MainActivity.this);
					if(!GPSLocation.GPS_AVAILABLE){
						showLocationAlert();
					}else {
						startActivity(new Intent(MainActivity.this,
								TextMessageUploadActivity.class));
					}
				}
			}
		});

//		ImageButton testButton = ((ImageButton) findViewById(R.id.icon_test));
//		testButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(MainActivity.this, TestActivity.class));
//			}
//		});

	

		((ImageButton) findViewById(R.id.icon_visual))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse("http://pulse.inn.ac"));
						startActivity(browserIntent);

					}
				});

		((ImageButton) findViewById(R.id.icon_about))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						// Start and intent for the logged out activity
						startActivity(new Intent(MainActivity.this,
								AboutActivity.class));

					}
				});

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		wakeLock.acquire();

		WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF,
				"WifyLock");
		wifiLock.acquire();

		GPSLocation.getInstance(this);

	}

	private OnSharedPreferenceChangeListener listener;

	protected synchronized void onDestroy() {
		Log.d(DEBUG_TAG, "onDestroy");
		if (Application.sensorManager != null)
			Application.sensorManager
					.unregisterListener(Application.sensorService);
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
	
	
	 private void showLocationAlert() {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setTitle("Location settings disabled"); // GPS not found
			builder.setMessage("This application requires the usage of location features. Please change your location settings."); // Want
																		// to
																		// enable?
			builder.setPositiveButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialogInterface,
								int i) {
							startActivity(new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						}
					});
			builder.setNegativeButton("Exit", 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogInterface,
						int i) {
					System.exit(0);
				}
			});
			builder.create().show();
			Toast.makeText(MainActivity.this, "You location could not be determined. Please enable your Network Providers.", Toast.LENGTH_LONG).show();

		
	}
}
