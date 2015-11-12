package ch.ethz.coss.nervous.pulse.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import ch.ethz.coss.nervous.pulse.R;

public class PrefsFragment extends PreferenceFragment {

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  
	  // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.prefs);
	 }

	}