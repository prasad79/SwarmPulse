package ch.ethz.coss.nervous.pulse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

@SuppressLint({ "Wakelock", "InlinedApi" })
public class AboutActivity extends SensorReadingActivity {

	public static final String DEBUG_TAG = "NoiseSensorReadingActivityPulse";

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

	}

}
