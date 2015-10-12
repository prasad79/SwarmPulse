package ch.ethz.coss.nervous.pulse;

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
		((Button) findViewById(R.id.submit))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (reading != null)
							Application.pushReadingToServer(reading);
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
		unregisterReceiver(broadcastReceiver);
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
