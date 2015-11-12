package ch.ethz.coss.nervous.pulse.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ch.ethz.coss.nervous.pulse.R;
import ch.ethz.coss.nervous.pulse.R.id;
import ch.ethz.coss.nervous.pulse.R.layout;

@SuppressLint({ "Wakelock", "InlinedApi" })
public abstract class SensorReadingActivity extends Activity {

	public static final String DEBUG_TAG = "SensorReadingActivityPulse";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light);

		// Sign up button click handler
		((Button) findViewById(R.id.submit))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});

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
