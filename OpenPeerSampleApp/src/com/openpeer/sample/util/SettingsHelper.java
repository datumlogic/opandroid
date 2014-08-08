package com.openpeer.sample.util;

import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.sample.OPApplication;
import com.openpeer.sdk.app.OPHelper;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.PreferenceManager;

public class SettingsHelper {

    private static final String KEY_RINGTONE = "ringtone";
    static final String KEY_NOTIFICATION_SOUND_SWITCH = "notification_sound_switch";
    static final String KEY_NOTIFICATION_SOUND_SELECT = "notification_sound_select";
    static final String KEY_OUT_TELNET_LOGGER = "out_telnet_logger";
    static final String KEY_LOCAL_TELNET_LOGGER = "local_telnet_logger";
    static final String KEY_FILE_LOGGER = "file_logger";
    static final String KEY_LOG_LEVEL = "log_level";
    static final String KEY_OUT_LOG_SERVER = "log_server_url";
    static final String KEY_FILE_LOGGER_PATH = "log_file";
    static final String KEY_TELENT_LOGGER = "local_telnet_logger";

    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(OPApplication.getInstance());

    static SettingsHelper instance;

    public static SettingsHelper getInstance() {
        if (instance == null) {
            instance = new SettingsHelper();
        }
        return instance;
    }

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

    public static boolean isSoundNotificationOnForNewMessage() {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(OPApplication.getInstance());

        return mPreferences.getBoolean(KEY_NOTIFICATION_SOUND_SWITCH, true);
    }

    public static Uri getNotificationSound() {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(OPApplication.getInstance());

        String ringtoneStr = mPreferences.getString(KEY_NOTIFICATION_SOUND_SELECT, null);
        if (ringtoneStr == null) {
            return null;
        }
        return Uri.parse(ringtoneStr);
    }

    public void initLoggers() {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(OPApplication.getInstance());
        OPLogger.setLogLevel(getLogLevel());
        if (isOutgoingLoggerOn()) {
            OPHelper.getInstance().toggleOutgoingTelnetLogging(true, getLogServer());
        }
        if (isTelnetLoggerOn()) {
            OPHelper.getInstance().toggleTelnetLogging(true, 59999);
        }
        if (isFileLoggerOn()) {
            OPHelper.getInstance().toggleFileLogger(true, getLogFile());
        }
    }

    /**
     * <boolean name="local_telnet_logger" value="true" />
     * <string name="log_file">/storage/emulated/0/oplog.txt</string>
     * <boolean name="file_logger" value="true" />
     * <string name="log_server_url">log.opp.me:8115</string>
     * <boolean name="out_telnet_logger" value="true" />
     * <string name="ringtone">content://settings/system/ringtone</string>
     * <string name="log_level">1</string>
     * <boolean name="notification_sound_switch" value="true" />
     */
    public OPLogLevel getLogLevel() {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(OPApplication.getInstance());
        int intValue = Integer.parseInt(mPreferences.getString(KEY_LOG_LEVEL, "0"));
        OPLogLevel level = OPLogLevel.values()[intValue];
        return level;
    }

    public boolean isOutgoingLoggerOn() {
        return mPreferences.getBoolean(KEY_OUT_TELNET_LOGGER, false);
    }

    public String getLogServer() {
        return mPreferences.getString(KEY_OUT_LOG_SERVER, null);
    }

    public boolean isTelnetLoggerOn() {
        return mPreferences.getBoolean(KEY_TELENT_LOGGER, false);
    }

    public boolean isFileLoggerOn() {
        return mPreferences.getBoolean(KEY_TELENT_LOGGER, false);
    }

    public String getLogFile() {
        return mPreferences.getString(KEY_FILE_LOGGER_PATH, null);
    }
}
