package com.openpeer.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brucexia on 2014-06-03.
 */
public class OPDatabaseHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "OpenPeer.db";

	public OPDatabaseHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseContracts.SQL_CREATE_IDENTITY);
		db.execSQL(DatabaseContracts.SQL_CREATE_IDENTITY_CONTACT);
		db.execSQL(DatabaseContracts.SQL_CREATE_CONTACT);
		db.execSQL(DatabaseContracts.SQL_CREATE_AVATAR);
		db.execSQL(DatabaseContracts.SQL_CREATE_CONVERSATION_PARTICIPANT);
		db.execSQL(DatabaseContracts.SQL_CREATE_SESSION);
		db.execSQL(DatabaseContracts.SQL_CREATE_MESSAGES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onDowngrade(db, oldVersion, newVersion);
	}

}
