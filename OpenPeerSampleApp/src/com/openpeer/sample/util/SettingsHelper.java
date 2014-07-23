package com.openpeer.sample.util;

import com.openpeer.sample.OPApplication;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

public class SettingsHelper {

	private static final String KEY_RINGTONE = "ringtone";

	public static Ringtone getRingtone() {
		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(OPApplication.getInstance());

		String ringtoneStr = mPreferences.getString(KEY_RINGTONE, null);
		if (ringtoneStr == null) {
			ringtoneStr = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString();
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putString(KEY_RINGTONE, ringtoneStr);
			editor.apply();
		}
		return RingtoneManager.getRingtone(OPApplication.getInstance(), Uri.parse(ringtoneStr));
	}

}
