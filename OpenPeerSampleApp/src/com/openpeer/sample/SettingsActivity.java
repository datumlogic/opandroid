package com.openpeer.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.openpeer.sdk.app.OPHelper;

public class SettingsActivity extends Activity {
	static final String KEY_OUT_TELNET_LOGGER = "out_telnet_logger";
	static final String KEY_LOCAL_TELNET_LOGGER = "local_telnet_logger";
	static final String KEY_FILE_LOGGER = "file_logger";

	public static void launch(Context context) {
		Intent intent = new Intent(context, SettingsActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		this.getFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, new SettingsFragment()).commit();
	}

	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preference);
			SwitchPreference outTelnetLogging = (SwitchPreference) findPreference(KEY_OUT_TELNET_LOGGER);
			SwitchPreference localTelnetLogging = (SwitchPreference) findPreference(KEY_LOCAL_TELNET_LOGGER);
			SwitchPreference fileLogging = (SwitchPreference) findPreference(KEY_FILE_LOGGER);

			outTelnetLogging.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					OPHelper.getInstance().toggleOutgoingTelnetLogging(((SwitchPreference) preference).isChecked());
					return true;
				}
			});
		}
	}
}
