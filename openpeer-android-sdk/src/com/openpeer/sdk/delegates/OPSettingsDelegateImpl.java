/**
 * Copyright (c) 2013, SMB Phone Inc. / Hookflash Inc.
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p/>
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */
package com.openpeer.sdk.delegates;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.openpeer.javaapi.OPSettingsDelegate;

/**
 * @author brucexia
 * 
 */
public class OPSettingsDelegateImpl extends OPSettingsDelegate {
	private final static String TAG = OPSettingsDelegateImpl.class.getSimpleName();
	private static final String PREF_CACHE_NAME = "core_setting";
	private Context mContext;
	private static OPSettingsDelegateImpl instance;
	private SharedPreferences preference;
	private static final String TABLE_SETTINGS = "settings";

	private static final String COLUMN_KEY = "key";
	private static final String COLUMN_VALUE = "value";

	public static final String SQL_CREATE_SETTINGS = "create table if not exists " + TABLE_SETTINGS + " ("
			+ COLUMN_KEY + " text primary key,"
			+ COLUMN_VALUE + " text not null)";

	SharedPreferences.Editor getEditor() {
		return preference.edit();
	}

	public static OPSettingsDelegateImpl getInstance(Context context) {
		if (instance == null) {
			instance = new OPSettingsDelegateImpl();
			instance.mContext = context;
			instance.preference = instance.mContext.getSharedPreferences(PREF_CACHE_NAME, Context.MODE_PRIVATE);
		}
		return instance;
	}

	@Override
	public synchronized String getString(String key) {
		Log.d(TAG, "getString key " + key);
		return preference.getString(key, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getInt(java.lang.String)
	 */
	@Override
	public synchronized long getInt(String key) {
		Log.d(TAG, "getInt key " + key);
		return preference.getLong(key, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getUInt(java.lang.String)
	 */
	@Override
	public synchronized long getUInt(String key) {
		Log.d(TAG, "getUInt key " + key);
		return preference.getLong(key, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getBool(java.lang.String)
	 */
	@Override
	public synchronized boolean getBool(String key) {
		Log.d(TAG, "getBool key " + key);
		return preference.getBoolean(key, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getFloat(java.lang.String)
	 */
	@Override
	public synchronized float getFloat(String key) {
		Log.d(TAG, "getFloat key " + key);
		return preference.getFloat(key, 0.0f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getDouble(java.lang.String)
	 */
	@Override
	public synchronized double getDouble(String key) {
		Log.d(TAG, "getDouble key " + key);
		return preference.getFloat(key, 0.0f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setString(java.lang.String, java.lang.String)
	 */
	@Override
	public synchronized void setString(String key, String value) {
		Log.d(TAG, "setString key " + key + " value " + value);
		getEditor().putString(key, value).apply();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setInt(java.lang.String, long)
	 */
	@Override
	public synchronized void setInt(String key, long value) {
		Log.d(TAG, "setInt key " + key + " value " + value);
		getEditor().putLong(key, value).apply();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setUInt(java.lang.String, long)
	 */
	@Override
	public synchronized void setUInt(String key, long value) {
		Log.d(TAG, "setUInt key " + key + " value " + value);
		getEditor().putLong(key, value).apply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setBool(java.lang.String, boolean)
	 */
	@Override
	public synchronized void setBool(String key, boolean value) {
		Log.d(TAG, "setBool key " + key + " value " + value);
		getEditor().putBoolean(key, value).apply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setFloat(java.lang.String, float)
	 */
	@Override
	public synchronized void setFloat(String key, float value) {
		Log.d(TAG, "setFloat key " + key + " value " + value);
		getEditor().putFloat(key, value).apply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setDouble(java.lang.String, double)
	 */
	@Override
	public synchronized void setDouble(String key, double value) {
		Log.d(TAG, "setDouble key " + key + " value " + value);

		getEditor().putFloat(key, (float) value).apply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#clear(java.lang.String)
	 */
	@Override
	public synchronized void clear(String key) {
		getEditor().clear().apply();
	}

}
