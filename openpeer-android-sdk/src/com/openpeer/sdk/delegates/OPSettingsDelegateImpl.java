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

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.openpeer.javaapi.OPSettingsDelegate;

/**
 * @ExcludeFromJavadoc Default implmentation of OPSettingsDelegate based on SQLiteDatabase. This class is thread safe
 * 
 */
public class OPSettingsDelegateImpl extends OPSettingsDelegate {
	private final static String TAG = OPSettingsDelegateImpl.class.getSimpleName();
	private static final String PREF_CACHE_NAME = "core_setting";
	private Context mContext;
	private static OPSettingsDelegateImpl instance;
	private static final String TABLE_SETTINGS = "settings";
	static final String QUERY_STRING = "select value from settings where key=?";
	private OPCoreDBHelper mDBHelper;

	private static final String COLUMN_KEY = "key";
	private static final String COLUMN_VALUE = "value";

	public static final String SQL_CREATE_SETTINGS = "create table if not exists " + TABLE_SETTINGS + " ("
			+ COLUMN_KEY + " text primary key,"
			+ COLUMN_VALUE + " text not null)";

	public static OPSettingsDelegateImpl getInstance(Context context) {
		if (instance == null) {
			instance = new OPSettingsDelegateImpl();
			instance.mContext = context;
			instance.mDBHelper = OPCoreDBHelper.getInstance(context);
		}
		return instance;
	}

	private String simpleQueryForString(String key, String defaultValue) {
		SQLiteStatement query = mDBHelper.getReadableDatabase().compileStatement(QUERY_STRING);
		query.bindString(1, key);
		try {
			return query.simpleQueryForString();
		} catch (SQLiteDoneException e) {

		}
		return defaultValue;
	}

	private long save(String key, String value) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_KEY, key);
		values.put(COLUMN_VALUE, value);
		return mDBHelper.getWritableDB()
				.insertWithOnConflict(TABLE_SETTINGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	@Override
	public String getString(String key) {
		return simpleQueryForString(key, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getInt(java.lang.String)
	 */
	@Override
	public long getInt(String key) {
		Log.d(TAG, "getInt key " + key);
		return Long.parseLong(simpleQueryForString(key, "0"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getUInt(java.lang.String)
	 */
	@Override
	public long getUInt(String key) {
		Log.d(TAG, "getUInt key " + key);
		return Long.parseLong(simpleQueryForString(key, "0"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getBool(java.lang.String)
	 */
	@Override
	public boolean getBool(String key) {
		Log.d(TAG, "getBool key " + key);
		return Boolean.parseBoolean(simpleQueryForString(key, "false"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(String key) {
		Log.d(TAG, "getFloat key " + key);
		return Float.parseFloat(simpleQueryForString(key, "0.0"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String key) {
		Log.d(TAG, "getDouble key " + key);
		return Float.parseFloat(simpleQueryForString(key, "0.0"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setString(java.lang.String, java.lang.String)
	 */
	@Override
	public void setString(String key, String value) {
		Log.d(TAG, "setString key " + key + " value " + value);
		save(key, value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setInt(java.lang.String, long)
	 */
	@Override
	public void setInt(String key, long value) {
		Log.d(TAG, "setInt key " + key + " value " + value);
		save(key, "" + value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setUInt(java.lang.String, long)
	 */
	@Override
	public void setUInt(String key, long value) {
		Log.d(TAG, "setUInt key " + key + " value " + value);
		save(key, "" + value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setBool(java.lang.String, boolean)
	 */
	@Override
	public void setBool(String key, boolean value) {
		Log.d(TAG, "setBool key " + key + " value " + value);
		save(key, "" + value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setFloat(java.lang.String, float)
	 */
	@Override
	public void setFloat(String key, float value) {
		Log.d(TAG, "setFloat key " + key + " value " + value);
		save(key, "" + value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#setDouble(java.lang.String, double)
	 */
	@Override
	public void setDouble(String key, double value) {
		Log.d(TAG, "setDouble key " + key + " value " + value);

		save(key, "" + value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openpeer.javaapi.OPSettingsDelegate#clear(java.lang.String)
	 */
	@Override
	public void clear(String key) {
		String where = COLUMN_KEY + "=?";
		String args[] = new String[] { key };
		mDBHelper.getWritableDB().delete(TABLE_SETTINGS, where, args);
	}

}
