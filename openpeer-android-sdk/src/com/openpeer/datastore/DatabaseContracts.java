package com.openpeer.datastore;

import android.provider.BaseColumns;

/**
 * Created by brucexia on 2014-06-03.
 */
public class DatabaseContracts {
	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";

	private static final String COMMA_SEP = ",";

	public DatabaseContracts() {
	}

	// We shouldn't really need a table for account since we support one account
	// only,
	// probably better off storing it in a preference file
	// The private key and secret should be stored in system keychain for
	// security.
	public static abstract class AccountEntry implements BaseColumns {
		public static final String TABLE_NAME = "account";
		public static final String COLUMN_NAME_RELOGIN_INFO = "relogin_info";

	}

	public static abstract class ContactEntry implements BaseColumns {
		public static final String TABLE_NAME = "contact";
		public static final String COLUMN_NAME_CONTACT_ID = "stable_id";
		public static final String COLUMN_NAME_IDENTITY_ID = "ientity_id";

		public static final String COLUMN_NAME_CONTACT_NAME = "name";
		public static final String COLUMN_NAME_URL = "url";

		// TODO add other columns
	}

	public static abstract class IdentityEntry implements BaseColumns {
		public static final String TABLE_NAME = "identity";
		public static final String COLUMN_NAME_IDENTITY_ID = "stable_id";
		public static final String COLUMN_NAME_IDENTITY_PROVIDER = "provider";
		public static final String COLUMN_NAME_IDENTITY_URI = "uri";
		// The "selfContact" of the identity
		public static final String COLUMN_NAME_IDENTITY_CONTACT_ID = "contact_id";

		// TODO add other columns
	}

	public static abstract class AvatarEntry implements BaseColumns {
		public static final String TABLE_NAME = "avatar";
		public static final String COLUMN_NAME_CONTACT_ID = "contact_id";

		public static final String COLUMN_NAME_AVATAR_NAME = "name";
		public static final String COLUMN_NAME_AVATAR_URL = "url";

		// TODO add other columns
	}

	public static final String SQL_CREATE_CONTACT = "CREATE TABLE "
			+ ContactEntry.TABLE_NAME + " (" + ContactEntry._ID
			+ " INTEGER PRIMARY KEY," + ContactEntry.COLUMN_NAME_CONTACT_ID
			+ INTEGER_TYPE + COMMA_SEP + ContactEntry.COLUMN_NAME_IDENTITY_ID
			+ INTEGER_TYPE + COMMA_SEP + ContactEntry.COLUMN_NAME_CONTACT_NAME
			+ TEXT_TYPE + COMMA_SEP + ContactEntry.COLUMN_NAME_URL + TEXT_TYPE +
			// ... Any other options for the CREATE command
			" )";
	public static final String SQL_CREATE_IDENTITY = "CREATE TABLE "
			+ IdentityEntry.TABLE_NAME + " (" + IdentityEntry._ID
			+ " INTEGER PRIMARY KEY," + IdentityEntry.COLUMN_NAME_IDENTITY_ID
			+ INTEGER_TYPE + COMMA_SEP
			+ IdentityEntry.COLUMN_NAME_IDENTITY_PROVIDER + TEXT_TYPE
			+ COMMA_SEP + IdentityEntry.COLUMN_NAME_IDENTITY_URI + TEXT_TYPE
			+ COMMA_SEP + IdentityEntry.COLUMN_NAME_IDENTITY_CONTACT_ID
			+ INTEGER_TYPE +
			// ... Any other options for the CREATE command
			" )";
	public static final String SQL_CREATE_AVATAR = "CREATE TABLE "
			+ AvatarEntry.TABLE_NAME + " (" + AvatarEntry._ID
			+ " INTEGER PRIMARY KEY," + AvatarEntry.COLUMN_NAME_CONTACT_ID
			+ INTEGER_TYPE + COMMA_SEP + AvatarEntry.COLUMN_NAME_AVATAR_NAME
			+ TEXT_TYPE + COMMA_SEP + AvatarEntry.COLUMN_NAME_AVATAR_URL
			+ TEXT_TYPE +
			// ... Any other options for the CREATE command
			" )";

	// public static final String SQL_INSERT_CONTACT = "INSERT I"

}
