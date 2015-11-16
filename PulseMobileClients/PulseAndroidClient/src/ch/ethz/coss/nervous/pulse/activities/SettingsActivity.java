/*******************************************************************************
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ETH Zürich.
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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.ethz.coss.nervous.pulse.R;
import ch.ethz.coss.nervous.pulse.utils.Utils;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

		ListPreference lp = (ListPreference) findPreference("time_limit_data_retention");

		if (lp.getValue() == null) {
			// to ensure we don't get a null value
			// set first value by default
			lp.setValueIndex(0);

		}
		int index = lp.findIndexOfValue(prefs.getString("time_limit_data_retention", "Forever"));

		CharSequence[] entries = lp.getEntries();

		lp.setSummary(index >= 0 ? entries[index] : null);
		lp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String textValue = newValue.toString();

				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(textValue);

				CharSequence[] entries = listPreference.getEntries();

				preference.setSummary(index >= 0 ? entries[index] : null);

				return true;
			}
		});

		// Preference preference = (Preference)findPreference("UUID");
		// if(preference != null){
		//
		// LinearLayout reset_uuid_layout = (LinearLayout)
		// findViewById(preference.getLayoutResource());
		// System.out.println("reset_uuid_layout "+reset_uuid_layout);
		// if(reset_uuid_layout != null) {
		// Button resetButton = (Button)
		// reset_uuid_layout.findViewById(R.id.reset);
		//
		//
		// resetButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// TextView txtUUID = (TextView) v.findViewById(R.id.UUID);
		// System.out.println("txtUUID "+txtUUID.getText());
		// txtUUID.setText("Setting UUID here");
		//
		// }
		// });
		// }
		// }
		//

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub

	}

}