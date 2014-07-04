package com.openpeer.datastore;

import com.openpeer.sdk.BuildConfig;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import static com.openpeer.datastore.DatabaseContracts.*;

public class OPContentProvider extends ContentProvider {

	private OPDatabaseHelper mOpenHelper;
	static final int MESSAGES = 0;
	static final int CONTACTS = 1;
	static final int WINDOWS = 2;
	static final int GROUPS = 3;
	static final int USERS = 4;

	static final int USER = 100;
	private static final int CONTACT = 4;
	private static final String TAG = OPContentProvider.class.getSimpleName();

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
		case USERS:
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
	public int bulkInsert(Uri uri, ContentValues[] values) {
		int result = super.bulkInsert(uri, values);
		Log.d("test", "bulkinsert " + uri + " values" + values.length + " result " + result);
//		test();
		return result;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri result = null;
		switch (mUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		// case CONTACTS:
		// result = insertContacts(uri, values);
		case MESSAGES:
			return insertMessage(uri, values);

			// case WINDOWS:
			// result = insertWindow(uri, values);
			// case USERS:
			// result = insertWindow(uri, values);
		default:
			// TODO: this is a blatant lazy implementation. values and uri needs
			// to
			// be validated for sure.
			String table = uri.getLastPathSegment();
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			long rowId = db.insert(table, null, values);
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "insert " + uri + " result " + rowId);
			}
			if (rowId != -1) {
				getContext().getContentResolver().notifyChange(uri, null);
				return ContentUris.withAppendedId(uri, rowId);
			}
		}
		return result;
	}

	private Uri insertWindow(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(DatabaseContracts.ConversationWindowEntry.TABLE_NAME, null, values);
		Log.d("test", "insert result " + rowId + " for uri " + uri);
		return uri;
	}

	private Uri insertMessage(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long result = db.insert(DatabaseContracts.MessageEntry.TABLE_NAME, null, values);
		Log.d("test", "result " + result + " inserting uri " + uri);
		getContext().getContentResolver().notifyChange(uri, null);
		getContext().getContentResolver().notifyChange(WindowViewEntry.CONTENT_URI, null);
//		test();
		return uri;
	}

	private Uri insertContacts(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	private Uri insertUser(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long id = mOpenHelper.getWritableDatabase().insert(UserEntry.TABLE_NAME, null, values);
		if (id > 1) {
			Uri noteUri = ContentUris.withAppendedId(uri, id);

			// Notifies observers registered against this provider that the
			// datachanged.
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		} else {
			return null;
		}
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

			return queryMessages(uri, projection, selection, selectionArgs, sortOrder);
		case WINDOWS:
			return queryWindows(uri, projection, selection, selectionArgs, sortOrder);
		case USERS:
			return queryUsers(uri, projection, selection, selectionArgs, sortOrder);
		case USER:

		}
		return null;
	}

	/**
	 * Should be used to retrieve user details like identityContact info
	 * 
	 * @param uri
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	private Cursor queryUserDetail(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor cursor = mOpenHelper.getReadableDatabase().query(true, ContactsViewEntry.TABLE_NAME, projection, selection, selectionArgs,
				null, null, sortOrder, null, null);
		return null;
	}

	/**
	 * Should be used to check if the user exists
	 * 
	 * @param uri
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	private Cursor queryUsers(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		Cursor cursor = mOpenHelper.getReadableDatabase().query(true, UserEntry.TABLE_NAME, projection, selection, selectionArgs, null,
				null, sortOrder, null, null);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// switch (mUriMatcher.match(uri)) {
		// // If the incoming URI is for notes, chooses the Notes projection
		// case CONTACT:
		// result = updateContacts(uri, values, selection, selectionArgs);
		// break;
		// case MESSAGES:
		// result = updateMessages(uri, values, selection, selectionArgs);
		// break;
		// case WINDOWS:
		// return 0;
		// }
		String table = uri.getLastPathSegment();
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int result = db.update(table, values, selection, selectionArgs);
		Log.d("test", "query uri for messages " + uri + " result " + result);

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
				MessageEntry.TABLE_NAME + "/window/#", MESSAGES);
		mUriMatcher.addURI(DatabaseContracts.AUTHORITY,
				ContactsViewEntry.TABLE_NAME, CONTACTS);
		mUriMatcher.addURI(DatabaseContracts.AUTHORITY,
				WindowViewEntry.TABLE_NAME, WINDOWS);
		mUriMatcher.addURI(DatabaseContracts.AUTHORITY,
				UserEntry.TABLE_NAME, USERS);
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
		String finalWhere = MessageEntry.COLUMN_NAME_WINDOW_ID + "=" + uri.getLastPathSegment();
		if (selection != null) {
			finalWhere = finalWhere + " AND " + selection;
		}
		Cursor cursor = db.query(
				DatabaseContracts.MessageEntry.TABLE_NAME,
				projection, // The columns to return from the query
				finalWhere, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		Log.d("test", "query uri for messages " + uri + " result " + cursor.getCount());
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
		Log.d("test", "query uri " + uri + " result " + cursor.getCount());
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	void test() {
		Cursor cursor = mOpenHelper.getReadableDatabase().query(false, MessageEntry.TABLE_NAME, null, null, null, null, null, null, null,
				null);
		Log.d("test", "found messages " + cursor.getCount());
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Log.d("test", "message " + cursor.getLong(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_WINDOW_ID)) + " message id " +
						cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_ID)) + " text " +
						cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TEXT)));
				cursor.moveToNext();
			}
			cursor.close();
		}
		cursor = mOpenHelper.getReadableDatabase().query(false, WindowViewEntry.TABLE_NAME, null, null, null, null, null, null, null,
				null);
		Log.d("test", "found windows " + cursor.getCount());
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Log.d("test", "window " + cursor.getLong(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_WINDOW_ID)) + " message id " +
						cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_LAST_MESSAGE)) + " text " +
						cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_PARTICIPANT_NAMES)) +
						cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_USER_ID)) + " text "
						);
				cursor.moveToNext();
			}
			cursor.close();
		}

	}

}
