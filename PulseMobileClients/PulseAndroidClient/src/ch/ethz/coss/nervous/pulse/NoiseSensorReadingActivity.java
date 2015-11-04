package ch.ethz.coss.nervous.pulse;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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

		// Sign up button click handler
		final Button submitButton = ((Button) findViewById(R.id.submit));
		
		submitButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (reading != null){
							Utils.showProgress(NoiseSensorReadingActivity.this);
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
		registerReceiver(broadcastReceiver, new IntentFilter(
				SensorService.BROADCAST_READING_ACTION));
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
