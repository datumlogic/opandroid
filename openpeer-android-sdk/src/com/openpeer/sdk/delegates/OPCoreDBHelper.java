package com.openpeer.sdk.delegates;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.openpeer.sdk.BuildConfig;

/**
 * Helper class for cache and setting database.
 */
public class OPCoreDBHelper extends SQLiteOpenHelper {
	private static OPCoreDBHelper instance;
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "opcore.db";
	SQLiteDatabase mDB;

	/**
	 * Get writable DB and make it thread safe/optimized
	 * @return
	 */
	public SQLiteDatabase getWritableDB() {

		if (mDB == null || !mDB.isOpen()) {
			mDB = instance.getWritableDatabase();
			if (android.os.Build.VERSION.SDK_INT > 15) {
				mDB.enableWriteAheadLogging();
			} else {
				mDB.setLockingEnabled(true);
			}
		}
		return mDB;
	}

	public static OPCoreDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new OPCoreDBHelper(context,
					DATABASE_NAME,
					null,
					DATABASE_VERSION);
		}
		return instance;
	}

	public OPCoreDBHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String sql : CREATE_STATEMENTS) {
			if (BuildConfig.DEBUG) {
				Log.d("test", "create statement" + sql);
			}
			db.execSQL(sql);
		}
	}

	static final String CREATE_STATEMENTS[] = { OPCacheDelegateImpl.SQL_CREATE_CACHE,
			OPSettingsDelegateImpl.SQL_CREATE_SETTINGS };

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onDowngrade(db, oldVersion, newVersion);
	}

}
