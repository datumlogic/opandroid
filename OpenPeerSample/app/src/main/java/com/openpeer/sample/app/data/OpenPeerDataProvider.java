package com.openpeer.sample.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by brucexia on 2014-06-03.
 */
public class OpenPeerDataProvider extends ContentProvider {
    OPDatabaseHelper mOpenHelper;
    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {

        mOpenHelper = new OPDatabaseHelper(
                getContext(),        // the application context
                OPDatabaseHelper.DATABASE_NAME,              // the name of the database)
                null,                // uses the default SQLite cursor
                OPDatabaseHelper.DATABASE_VERSION                    // the version number
        );

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Insert code here to determine which table to open, handle error-checking, and so forth



        /*
         * Gets a writeable database. This will trigger its creation if it doesn't already exist.
         *
         */
        mDatabase = mOpenHelper.getWritableDatabase();
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
