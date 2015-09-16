package ch.ethz.coss.nervous.pulse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends SensorReadingActivity {

	public static final String DEBUG_TAG = "SettingsActivityPulse";

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

	}

}
