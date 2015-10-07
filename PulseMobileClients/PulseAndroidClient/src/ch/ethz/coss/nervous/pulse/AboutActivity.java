package ch.ethz.coss.nervous.pulse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint({ "Wakelock", "InlinedApi" })
public class AboutActivity extends SensorReadingActivity {

	public static final String DEBUG_TAG = "NoiseSensorReadingActivityPulse";

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		((Button) findViewById(R.id.rateButton))
		.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=ch.ethz.coss.nervous.pulse")));
				}

			}
		});
		
		((Button) findViewById(R.id.termsButton))
		.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}

}
