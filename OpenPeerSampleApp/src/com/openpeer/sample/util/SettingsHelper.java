package com.openpeer.sample.util;

import com.openpeer.sample.OPApplication;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

public class SettingsHelper {

	private static final String KEY_RINGTONE = "ringtone";
    static final String KEY_NOTIFICATION_SOUND_SWITCH = "notification_sound_switch";
    static final String KEY_NOTIFICATION_SOUND_SELECT = "notification_sound_select";

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

    public static boolean isSoundNotificationOnForNewMessage(){
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(OPApplication.getInstance());

        return mPreferences.getBoolean(KEY_NOTIFICATION_SOUND_SWITCH, true);
    }
    public static Uri getNotificationSound() {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(OPApplication.getInstance());

        String ringtoneStr = mPreferences.getString(KEY_NOTIFICATION_SOUND_SELECT, null);
        if(ringtoneStr==null){
            return null;
        }
        return Uri.parse(ringtoneStr);
    }

}
