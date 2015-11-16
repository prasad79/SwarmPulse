/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ETH Zurich.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Contributors:
 *     Prasad Pulikal - prasad.pulikal@gess.ethz.ch  - Initial design and implementation
 *******************************************************************************/
package ch.ethz.coss.nervous.pulse.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ch.ethz.coss.nervous.pulse.Constants;
import ch.ethz.coss.nervous.pulse.R;

public class AboutActivity extends ParentActivity {

	public static final String DEBUG_TAG = "AboutActivityPulse";

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TextView txtVersion = (TextView) findViewById(R.id.version);

		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = pInfo.versionName;
		int verCode = pInfo.versionCode;

		String versionText = "v" + version + " (" + verCode + ")" + (Constants.DUMMY_DATA_COLLECT ? " - dev " : "");

		txtVersion.setText(versionText);

		((Button) findViewById(R.id.rateButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String appPackageName = getPackageName(); // getPackageName()
																// from Context
																// or Activity
																// object
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("https://play.google.com/store/apps/details?id=ch.ethz.coss.nervous.pulse")));
				}

			}
		});

	}

}
