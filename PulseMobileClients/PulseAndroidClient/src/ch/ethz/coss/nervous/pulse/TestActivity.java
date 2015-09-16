package ch.ethz.coss.nervous.pulse;

import java.security.GeneralSecurityException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint({ "Wakelock", "InlinedApi" })
public class TestActivity extends SensorReadingActivity {

	public static final String DEBUG_TAG = "TestActivityPulse";

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

//		((Button) findViewById(R.id.encrypt))
//				.setOnClickListener(new OnClickListener() {
//
//					TextView msgTV = (TextView) findViewById(R.id.orgMsgTF);
//
//					@Override
//					public void onClick(View v) {
//						String message = (msgTV.getText()).toString();
//
//						for (int i = 0; i < 10; i++)
//							message += message;
//
//						System.out.println("Message incremented");
//
//						if (message.length() >= 1) {
//
//							TextView msgEncTV = (TextView) findViewById(R.id.textView2);
//							TextView msgDecTV = (TextView) findViewById(R.id.textViewDec);
//							TextView msgTimeTV = (TextView) findViewById(R.id.textView4);
//							long encTime = 0, decTime = 0;
//							String encString = "*";
//							try {
//								encTime = System.currentTimeMillis();
//								encString = StaticCryptoHandler.encrypt(
//										TestActivity.this, "password123",
//										message);
//								encTime = System.currentTimeMillis() - encTime;
//							} catch (GeneralSecurityException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								encString = e.getMessage();
//							}
//
//							msgEncTV.setText(encString);
//
//							String decString = "*";
//							try {
//								decTime = System.currentTimeMillis();
//								decString = StaticCryptoHandler.decrypt(
//										TestActivity.this, "password123",
//										encString);
//								decTime = System.currentTimeMillis() - decTime;
//							} catch (GeneralSecurityException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								decString = e.getMessage();
//							}
//
//							msgDecTV.setText(decString);
//							msgTimeTV.setText("Time = " + encTime + ", "
//									+ decTime + "\n" + " Length = "
//									+ message.length() + ", "
//									+ encString.length());
//						}
//
//					}
//				});

	}

}
