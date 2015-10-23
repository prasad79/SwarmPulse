package ch.ethz.coss.nervous.pulse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ch.ethz.coss.nervous.pulse.model.TextVisual;
import ch.ethz.coss.nervous.pulse.model.VisualLocation;

@SuppressLint({ "Wakelock", "InlinedApi" })
public class TextMessageUploadActivity extends SensorReadingActivity {

	public static final String DEBUG_TAG = "NoiseSensorReadingActivityPulse";

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");
		
		super.onCreate(savedInstanceState);
		String message = null;
		
		Bundle extras = getIntent().getExtras();
		  if (extras != null) {
		   message = extras.getString("MESSAGE");
		
			   
		   }  
		   
		   
		setContentView(R.layout.activity_msg_upload);
		final TextView msgTV = (TextView) findViewById(R.id.messageTF);
		// Sign up button click handler
		((Button) findViewById(R.id.submit))
				.setOnClickListener(new OnClickListener() {

					
					
					@Override
					public void onClick(View v) {
						String message = (msgTV.getText()).toString();
						if (message.length() >= 2) {
							TextVisual txtMsg = new TextVisual(Application.uuid.toString(), message, System
									.currentTimeMillis(), new VisualLocation(
									GPSLocation.getInstance(
											TextMessageUploadActivity.this)
											.getLocation()));
							if (txtMsg != null) {
								Application.pushReadingToServer(txtMsg);
								msgTV.setText("");
							}
						}

					}
				});
		
		if(message != null){
			msgTV.setText(message);
			
		}

	}

}
