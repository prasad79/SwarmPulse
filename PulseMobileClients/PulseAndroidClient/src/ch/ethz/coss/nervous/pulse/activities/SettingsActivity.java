package ch.ethz.coss.nervous.pulse.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.ethz.coss.nervous.pulse.R;

public class SettingsActivity extends PreferenceActivity {
	
	
	
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    addPreferencesFromResource(R.xml.preferences);
	    
	    Preference preference = 	(Preference)findPreference("resetUUID");
	   LinearLayout reset_uuid_layout = (LinearLayout) preference.getView(null, null);
	   System.out.println("reset_uuid_layout "+reset_uuid_layout);
	   TextView txtUUID = (TextView) reset_uuid_layout.findViewById(R.id.UUID);
	   System.out.println("txtUUID "+txtUUID.getText());
	   txtUUID.setText("Setting UUID here");
		
	  }
	
}