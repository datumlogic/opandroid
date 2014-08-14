package com.openpeer.sdk.delegates;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
import android.text.format.Time;
import android.util.Log;

import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.sdk.BuildConfig;

/**
 * @ExcludeFromJavadoc We use SQLiteDatabase to store the cache. Since SQLiteDatabase is thread safe, there's no need to synchronize the
 *                     methods
 * 
 * 
 */
public class OPCacheDelegateImpl extends OPCacheDelegate {
	public static final String TABLE_CACHE = "cache";
	public static final String COLUMN_KEY = "key";
	public static final String COLUMN_EXPIRES = "expires";
	public static final String COLUMN_VALUE = "value";

	public static final String COLUMN_VALUE_TYPE = "valueType";
	public static final String SQL_CREATE_CACHE = "create table if not exists " + TABLE_CACHE + " ("
			+ COLUMN_KEY + " text primary key,"
			+ COLUMN_EXPIRES + " integer default 0, "
			+ COLUMN_VALUE + " text not null" + ")";
	static final String QUERY_STRING = "select value  from   cache  where  key=? and (expires>? or expires=0)";
	private static final String TAG = OPCacheDelegateImpl.class.getSimpleName();
	private static OPCacheDelegateImpl instance;
	private OPCoreDBHelper mDBHelper;

	private OPCacheDelegateImpl() {
	}

	public static OPCacheDelegateImpl getInstance(Context context) {
		if (instance == null) {
			instance = new OPCacheDelegateImpl();
			instance.mDBHelper = OPCoreDBHelper.getInstance(context);
			instance.clearExpiredEntriesOnStart();
		}
		return instance;
	}

	@Override
	public String fetch(String cookieNamePath) {
		String value = "";

		SQLiteStatement query = mDBHelper.getReadableDatabase().compileStatement(QUERY_STRING);
		query.bindString(1, cookieNamePath);
		query.bindLong(2, System.currentTimeMillis());
		try {
			value = query.simpleQueryForString();
		} catch (SQLiteDoneException e) {

		}
		query.close();
		if (BuildConfig.DEBUG) {
			// Log.d(TAG, String.format("fetch key %s,string %s", cookieNamePath, value));
		}
		return value;

	}

	@Override
	public void store(String cookieNamePath, Time expires, String str) {
		// We use value 0 to indicate this cache is per app run session only.
		long timeInMillis = 0;
		if (expires != null) {
			timeInMillis = expires.toMillis(true);
		}
		ContentValues values = new ContentValues();
		values.put(COLUMN_KEY, cookieNamePath);
		values.put(COLUMN_EXPIRES, timeInMillis);
		values.put(COLUMN_VALUE, str);
		long rowid = mDBHelper.getWritableDatabase()
				.insertWithOnConflict(TABLE_CACHE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		if (BuildConfig.DEBUG) {
			// Log.d(TAG, String.format("store key %s,expires %d,string %s,result %d", cookieNamePath, expires.toMillis(true), str, rowid));
		}
	}

	@Override
	public void clear(String cookieNamePath) {
		String where = COLUMN_KEY + "=?";
		String args[] = new String[] { cookieNamePath };
		mDBHelper.getWritableDB().delete(TABLE_CACHE, where, args);
	}

	private void clearExpiredEntriesOnStart() {
		String where = COLUMN_EXPIRES + "<" + System.currentTimeMillis();
		int count = mDBHelper.getWritableDB().delete(TABLE_CACHE, where, null);
		if (BuildConfig.DEBUG) {
			Log.d(TAG, String.format("clearExpiredEntriesOnStart deleted %d entries", count));
		}
	}

	private void clearExpiredEntries() {
		String where = COLUMN_EXPIRES + "<" + System.currentTimeMillis() + " and " + COLUMN_EXPIRES + "!=0";
		int count = mDBHelper.getWritableDB().delete(TABLE_CACHE, where, null);
		if (BuildConfig.DEBUG) {
			Log.d(TAG, String.format("clearExpiredEntries deleted %d entries", count));
		}
	}

}
