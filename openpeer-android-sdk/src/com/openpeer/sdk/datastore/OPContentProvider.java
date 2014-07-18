package com.openpeer.sdk.datastore;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import static com.openpeer.sdk.datastore.DatabaseContracts.*;

public class OPContentProvider extends ContentProvider {

	private OPDatabaseHelper mOpenHelper;
	static final int MESSAGES = 0;
	static final int CONTACTS = 1;
	static final int WINDOWS = 2;
	static final int GROUPS = 3;
	static final int USERS = 4;
	static final int MESSAGE = 5;

	static final int USER = 100;
	private static final int CONTACT = 6;
	private static final String TAG = OPContentProvider.class.getSimpleName();

	UriMatcher mUriMatcher;
	static String sAuthority = "";
	static final String SCHEME = "content://";

	static enum MatcherInfo {
		ACCOUNTS(AccountEntry.TABLE_NAME),
		ACCOUNT(AccountEntry.TABLE_NAME + "/#"),
		IDENTTIIES(IdentityEntry.TABLE_NAME),
		IDENTITY(IdentityEntry.TABLE_NAME + "/#"),

		MESSAGES_WINDOW(MessageEntry.URI_PATH_INFO_WINDOW),
		MESSAGES_THREAD(MessageEntry.URI_PATH_INFO_THREAD),
		MESSAGES_GROUP(MessageEntry.URI_PATH_INFO_GROUP),

		MESSAGE_WINDOW(MessageEntry.URI_PATH_INFO_WINDOW_ID),
		MESSAGE_THREAD(MessageEntry.URI_PATH_INFO_THREAD_ID),
		MESSAGE_GROUP(MessageEntry.URI_PATH_INFO_GROUP_ID),

		CONTACTS(DatabaseContracts.ContactsViewEntry.TABLE_NAME),
		CONTACT(DatabaseContracts.ContactsViewEntry.TABLE_NAME + "/#"),
		
		CONVERSATION_WINDOWS(ConversationWindowEntry.URI_PATH_INFO),
		CONVERSATION_WINDOW(ConversationWindowEntry.URI_PATH_INFO_ID),
		
		WINDOW_PARTICIPANTS(WindowParticipantEntry.URI_PATH_INFO),
		WINDOW_PARTICIPANT(WindowParticipantEntry.URI_PATH_INFO_ID),

		HISTORY_WINDOW(WindowViewEntry.TABLE_NAME),
		HISTORY_THREAD(""),
		HISTORY_GROUP(""),
		USERS(UserEntry.TABLE_NAME),
		USER(UserEntry.TABLE_NAME + "/#"),
		
		IDENTITY_CONTACTS(IdentityContactEntry.TABLE_NAME),
		IDENTITY_CONTACT(IdentityContactEntry.TABLE_NAME + "/#"),
		
		ROLODEX_CONTACTS(ContactEntry.TABLE_NAME),
		ROLODEX_CONTACT(IdentityContactEntry.TABLE_NAME + "/#"),

		AVATARS(AvatarEntry.TABLE_NAME),
		AVATAR(AvatarEntry.TABLE_NAME + "/#"),

		CALLS(CallEntry.TABLE_NAME),
		CALL(CallEntry.TABLE_NAME + "/#");

		private final String mPath;

		private MatcherInfo(final String path) {
			mPath = path;
		}

		public String getPath() {
			return mPath;
		}
	}

	/**
	 * Helper class to get the content URI with the authority provided by application and path string defined in DatabaseContracts
	 * 
	 * @param path
	 * @return
	 */
	public static Uri getContentUri(String path) {
		return Uri.parse(SCHEME + sAuthority + path);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		MatcherInfo value = MatcherInfo.values()[mUriMatcher.match(uri)];
		StringBuilder stringBuilder = new StringBuilder();
		switch (value) {

		case MESSAGES_WINDOW:
			return 0;

		}
		String table = uri.getLastPathSegment();
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int result = db.delete(table, selection, selectionArgs);
		Log.d("test", "update uri for messages " + uri + " result " + result);
		return result;
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
		// test();
		return result;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri result = null;
		MatcherInfo value = MatcherInfo.values()[mUriMatcher.match(uri)];
		StringBuilder stringBuilder = new StringBuilder();
		switch (value) {
		case MESSAGES_WINDOW:
			return insertMessage(uri, values);

		default:
			String tableName = null;
			List<String> pathSegments = uri.getPathSegments();
			switch (pathSegments.size()) {
			case 1:
				tableName = pathSegments.get(0);
				break;
			case 2:
				tableName = pathSegments.get(1);
				break;
			}
			if (tableName != null) {
				SQLiteDatabase db = mOpenHelper.getWritableDatabase();
				long rowId = db.insert(tableName, null, values);

				if (rowId != -1) {
					getContext().getContentResolver().notifyChange(uri, null);
					return ContentUris.withAppendedId(uri, rowId);
				}
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
		getContext().getContentResolver().notifyChange(ContentUris.withAppendedId(uri, result), null);
		notifyChatGroupChange();
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
		MatcherInfo value = MatcherInfo.values()[mUriMatcher.match(uri)];
		StringBuilder stringBuilder = new StringBuilder();

		switch (value) {
		case MESSAGES_WINDOW:
			int windowId = Integer.parseInt(uri.getLastPathSegment());
			stringBuilder.append(DatabaseContracts.COLUMN_NAME_WINDOW_ID + "=" + windowId);
			if (!TextUtils.isEmpty(selection)) {
				stringBuilder.append(" and ").append(selection);
			}
			return queryMessages(uri, projection, stringBuilder.toString(), selectionArgs, sortOrder);
		case MESSAGE_WINDOW:
			return queryMessage(uri, projection, selection, selectionArgs, sortOrder);
		case MESSAGES_THREAD:
			int threadId = Integer.parseInt(uri.getLastPathSegment());
			stringBuilder.append(DatabaseContracts.COLUMN_NAME_THREAD_ID + "=" + threadId);
			if (!TextUtils.isEmpty(selection)) {
				stringBuilder.append(" and ").append(selection);
			}
			return queryMessages(uri, projection, stringBuilder.toString(), selectionArgs, sortOrder);
		case MESSAGE_THREAD:
			return queryMessage(uri, projection, selection, selectionArgs, sortOrder);
		case MESSAGES_GROUP:
		case MESSAGE_GROUP:
			return null;
		default:
			break;

		}

		String tableName = null;
		List<String> pathSegments = uri.getPathSegments();
		switch (pathSegments.size()) {
		case 1:
			tableName = pathSegments.get(0);
			break;
		case 2:
			tableName = pathSegments.get(1);
			break;
		}
		if (tableName != null) {
			SQLiteDatabase db = mOpenHelper.getReadableDatabase();

			Cursor cursor = db.query(
					tableName,
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
		} else {
			return null;
		}
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
		int result;
		MatcherInfo value = MatcherInfo.values()[mUriMatcher.match(uri)];
		StringBuilder stringBuilder = new StringBuilder();

		switch (value) {
		// If the incoming URI is for notes, chooses the Notes projection
		// case CONTACT:
		// result = updateContacts(uri, values, selection, selectionArgs);
		// break;
		case MESSAGES_WINDOW:
			int windowId = Integer.parseInt(uri.getLastPathSegment());
			stringBuilder.append(DatabaseContracts.COLUMN_NAME_WINDOW_ID + "=" + windowId);
			if (!TextUtils.isEmpty(selection)) {
				stringBuilder.append(" and ").append(selection);
			}
			return updateMessages(uri, values, stringBuilder.toString(), selectionArgs);
			// case WINDOWS:
			// return 0;
		default:
			String table = uri.getLastPathSegment();
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			result = db.update(table, values, selection, selectionArgs);
			Log.d("test", "update uri for messages " + uri + " result " + result);
		}
		return result;
	}

	private int updateMessages(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		int result = db.update(MessageEntry.TABLE_NAME, values, selection, selectionArgs);
		if (result != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
			notifyChatGroupChange();
		}
		return result;
	}

	private int updateContacts(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	void initMatcher() {
		try {
			ProviderInfo providers[] = this.getContext().getPackageManager()
					.getPackageInfo(getContext().getPackageName(), PackageManager.GET_PROVIDERS).providers;
			if (providers != null && providers.length > 0) {
				String myName = OPContentProvider.class.getCanonicalName();
				for (ProviderInfo provider : providers) {
					if (myName.equals(provider.name)) {
						sAuthority = provider.authority;
					}
				}
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		for (MatcherInfo mi : MatcherInfo.values()) {
			if (!TextUtils.isEmpty(mi.getPath())) {
				mUriMatcher.addURI(sAuthority, mi.getPath(), mi.ordinal());
			}
		}
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
		String finalWhere = COLUMN_NAME_WINDOW_ID + "=" + uri.getLastPathSegment();
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

	private Cursor queryMessage(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		String finalWhere = MessageEntry._ID + "=" + uri.getLastPathSegment();
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
		Log.d("test", "query uri for message " + uri + " result " + cursor.getCount());
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

	void notifyChatGroupChange() {
		getContext().getContentResolver().notifyChange(getContentUri(WindowViewEntry.URI_PATH_INFO), null);
	}
}
