/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
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
