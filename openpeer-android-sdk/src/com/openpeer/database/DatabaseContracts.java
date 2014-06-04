package com.openpeer.database;

import android.provider.BaseColumns;

/**
 * Created by brucexia on 2014-06-03.
 */
public class DatabaseContracts {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public DatabaseContracts() {
    }

    // We shouldn't really need a table for account since we support one account only,
    // probably better off storing it in a preference file
    // The private key and secret should be stored in system keychain for security.
    public static abstract class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_RELOGIN_INFO = "relogin_info";

    }

    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_CONTACT_ID = "contact_id";
        public static final String COLUMN_NAME_IDENTITY_ID = "ientity_id";
        //TODO add other columns
    }

    public static abstract class IdentityEntry implements BaseColumns {
        public static final String TABLE_NAME = "identity";
        public static final String COLUMN_NAME_IDENTITY_ID = "identity_id";
        public static final String COLUMN_NAME_IDENTITY_PROVIDER = "provider";
        //TODO add other columns
    }


    public static final String SQL_CREATE_CONTACT =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactEntry.COLUMN_NAME_CONTACT_ID + TEXT_TYPE + COMMA_SEP +
                    ContactEntry.COLUMN_NAME_IDENTITY_ID + TEXT_TYPE + COMMA_SEP +
                    //... Any other options for the CREATE command
                    " )";
}
