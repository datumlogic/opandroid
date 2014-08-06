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
	public String getString(String key) {
		// TODO Auto-generated method stub
		return preference.getString(key, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getInt(java.lang.String)
	 */
	@Override
	public long getInt(String key) {
		// TODO Auto-generated method stub
		return preference.getLong(key, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getUInt(java.lang.String)
	 */
	@Override
	public long getUInt(String key) {
		// TODO Auto-generated method stub
		return preference.getLong(key, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getBool(java.lang.String)
	 */
	@Override
	public boolean getBool(String key) {
		// TODO Auto-generated method stub
		return preference.getBoolean(key, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(String key) {
		// TODO Auto-generated method stub
		return preference.getFloat(key, 0.0f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String key) {
		// TODO Auto-generated method stub
		return preference.getFloat(key, 0.0f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setString(java.lang.String, java.lang.String)
	 */
	@Override
	public void setString(String key, String value) {
		Log.d(TAG, "key " + key + " value " + value);
		getEditor().putString(key, value).apply();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setInt(java.lang.String, long)
	 */
	@Override
	public void setInt(String key, long value) {
		Log.d(TAG, "key " + key + " value " + value);
		getEditor().putLong(key, value).apply();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setUInt(java.lang.String, long)
	 */
	@Override
	public void setUInt(String key, long value) {
		Log.d(TAG, "setUInt key " + key + " value " + value);
		getEditor().putLong(key, value).apply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setBool(java.lang.String, boolean)
	 */
	@Override
	public void setBool(String key, boolean value) {
		Log.d(TAG, "key " + key + " value " + value);
		getEditor().putBoolean(key, value).apply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setFloat(java.lang.String, float)
	 */
	@Override
	public void setFloat(String key, float value) {
		Log.d(TAG, "key " + key + " value " + value);
		getEditor().putFloat(key, value).apply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setDouble(java.lang.String, double)
	 */
	@Override
	public void setDouble(String key, double value) {
		getEditor().putFloat(key, (float) value).apply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#clear(java.lang.String)
	 */
	@Override
	public void clear(String key) {
		getEditor().clear().apply();
	}

}
