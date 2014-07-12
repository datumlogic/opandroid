package com.openpeer.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.MenuItem;

import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.sdk.app.OPHelper;

public class SettingsActivity extends Activity {
	static final String KEY_OUT_TELNET_LOGGER = "out_telnet_logger";
	static final String KEY_LOCAL_TELNET_LOGGER = "local_telnet_logger";
	static final String KEY_FILE_LOGGER = "file_logger";
	static final String KEY_LOG_LEVEL = "log_level";
	static final String KEY_OUT_LOG_SERVER = "log_server_url";
	static final String KEY_FILE_LOGGER_PATH = "log_file";

	public static void launch(Context context) {
		Intent intent = new Intent(context, SettingsActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		getActionBar().setDisplayHomeAsUpEnabled(true);
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

			final ListPreference logLevelPref = (ListPreference) findPreference(KEY_LOG_LEVEL);
			final EditTextPreference logServerPref = (EditTextPreference) findPreference(KEY_OUT_LOG_SERVER);
			final EditTextPreference logFilePref = (EditTextPreference) findPreference(KEY_FILE_LOGGER_PATH);

			outTelnetLogging.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					String url = logServerPref.getText();
					OPHelper.getInstance().toggleOutgoingTelnetLogging(((SwitchPreference) preference).isChecked(), url);
					return true;
				}
			});
			localTelnetLogging.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					OPHelper.getInstance().toggleTelnetLogging(((SwitchPreference) preference).isChecked(), 59999);
					return true;
				}
			});
			fileLogging.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					String fileName = logFilePref.getText();
					OPHelper.getInstance().toggleOutgoingTelnetLogging(((SwitchPreference) preference).isChecked(), fileName);
					return true;
				}
			});
			logLevelPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					int intValue = Integer.parseInt(((ListPreference) preference).getValue());
					OPLogLevel level = OPLogLevel.values()[intValue];
					OPLogger.setLogLevel(level);
					return true;
				}
			});
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
