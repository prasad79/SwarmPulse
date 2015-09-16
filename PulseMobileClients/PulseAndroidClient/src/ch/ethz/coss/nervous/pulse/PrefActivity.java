package ch.ethz.coss.nervous.pulse;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class PrefActivity extends PreferenceActivity {

	private static int prefs = R.xml.preferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			getClass().getMethod("getFragmentManager");
			AddResourceApi11AndGreater();
		} catch (NoSuchMethodException e) { // Api < 11
			AddResourceApiLessThan11();
		}
	}

	@SuppressWarnings("deprecation")
	protected void AddResourceApiLessThan11() {
		addPreferencesFromResource(prefs);
	}

	protected void AddResourceApi11AndGreater() {
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PF()).commit();
	}

	public static class PF extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(PrefActivity.prefs); // outer class
			// private members seem to be visible for inner class, and
			// making it static made things so much easier
		}
	}

}