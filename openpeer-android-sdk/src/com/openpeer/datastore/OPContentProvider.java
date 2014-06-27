package com.openpeer.datastore;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import static com.openpeer.datastore.DatabaseContracts.*;

public class OPContentProvider extends ContentProvider {

	private OPDatabaseHelper mOpenHelper;
	static final int MESSAGES = 0;
	static final int CONTACTS = 1;
	static final int WINDOWS = 2;
	static final int GROUPS = 3;
	private static final int CONTACT = 4;

	UriMatcher mUriMatcher;

	@Override
	public int delete(Uri uri, String selection, String[] arg2) {
		switch (mUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case CONTACTS:
			return 0;
		case MESSAGES:
			return 0;
		case WINDOWS:
			return 0;
		}
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri result = null;
		switch (mUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case CONTACTS:
			result = insertContacts(uri, values);
		case MESSAGES:
			result = inserMessage(uri, values);
		case WINDOWS:
			result = insertWindow(uri, values);
		}
		if (result != null) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return result;
	}

	private Uri insertWindow(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	private Uri inserMessage(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	private Uri insertContacts(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new OPDatabaseHelper(getContext(),
				OPDatabaseHelper.DATABASE_NAME, // the name of the database)
				null, // uses the default SQLite cursor
				OPDatabaseHelper.DATABASE_VERSION);
		initMatcher();

		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (mUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case CONTACTS:
			return queryContacts(uri, projection, selection, selectionArgs, sortOrder);
		case MESSAGES:
			String finalWhere = MessageEntry.COLUMN_NAME_WINDOW_ID + " = " + uri.getPathSegments().get(2);
			if (selection != null) {
				finalWhere = finalWhere + " AND " + selection;
			}
			return queryMessages(uri, projection, finalWhere, selectionArgs, sortOrder);
		case WINDOWS:

			return queryWindows(uri, projection, selection, selectionArgs, sortOrder);
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int result = 0;
		switch (mUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case CONTACT:
			result = updateContacts(uri, values, selection, selectionArgs);
			break;
		case MESSAGES:
			result = updateMessages(uri, values, selection, selectionArgs);
			break;
		case WINDOWS:
			return 0;
		}
		if (result != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return result;
	}

	private int updateMessages(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int updateContacts(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	void initMatcher() {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(DatabaseContracts.AUTHORITY,
				MessageEntry.TABLE_NAME + "/#", MESSAGES);
		mUriMatcher.addURI(DatabaseContracts.AUTHORITY,
				ContactsViewEntry.TABLE_NAME, CONTACTS);
		mUriMatcher.addURI(DatabaseContracts.AUTHORITY,
				WindowViewEntry.TABLE_NAME + "/window/#", WINDOWS);
	}

	Cursor queryContacts(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = db.query(
				DatabaseContracts.ContactsViewEntry.TABLE_NAME,
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	Cursor queryMessages(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		Cursor cursor = db.query(
				DatabaseContracts.MessageEntry.TABLE_NAME,
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	Cursor queryWindows(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = db.query(
				DatabaseContracts.WindowViewEntry.TABLE_NAME,
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
}
