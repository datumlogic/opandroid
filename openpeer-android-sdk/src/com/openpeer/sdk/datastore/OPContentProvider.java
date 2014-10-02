/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.sdk.datastore;

import static com.openpeer.sdk.datastore.DatabaseContracts.COLUMN_CBC_ID;

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
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.openpeer.sdk.app.OPSdkConfig;
import com.openpeer.sdk.datastore.DatabaseContracts.AccountEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.AssociatedIdentityEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.AvatarEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.CallEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.ContactsViewEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.ConversationEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.IdentityContactEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.MessageEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.OpenpeerContactEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.ParticipantEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.RolodexContactEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.WindowViewEntry;
import com.openpeer.sdk.model.GroupChatMode;

public class OPContentProvider extends ContentProvider {

    private OPDatabaseHelper mOpenHelper;

    static final int USER = 100;
    private static final int CONTACT = 6;
    private static final String TAG = OPContentProvider.class.getSimpleName();

    UriMatcher mUriMatcher;
    static String sAuthority = "";
    static final String SCHEME = "content://";
    private static OPContentProvider instance;

    static enum MatcherInfo {
        ACCOUNTS(AccountEntry.TABLE_NAME),
        ACCOUNT(AccountEntry.TABLE_NAME + "/#"),
        IDENTTIIES(AssociatedIdentityEntry.TABLE_NAME),
        IDENTITY(AssociatedIdentityEntry.TABLE_NAME + "/#"),

        MESSAGES_WINDOW(MessageEntry.URI_PATH_INFO_WINDOW),
        MESSAGES_CONTEXT(MessageEntry.URI_PATH_INFO_CONTEXT),

        MESSAGE_WINDOW(MessageEntry.URI_PATH_INFO_WINDOW_ID),
        MESSAGE_THREAD(MessageEntry.URI_PATH_INFO_CONTEXT_ID),

        MESSAGES(MessageEntry.TABLE_NAME),
        MESSAGE(MessageEntry.TABLE_NAME + "/#"),

        // CONTACTS(DatabaseContracts.ContactsViewEntry.URI_PATH_CONTACTS),
        // CONTACT(DatabaseContracts.ContactsViewEntry.URI_PATH_CONTACTS_ID),
        // CONTACTS_OPENPEER(
        // DatabaseContracts.ContactsViewEntry.URI_PATH_OP_CONTACTS),
        // CONTACT_OPENPEER(
        // DatabaseContracts.ContactsViewEntry.URI_PATH_OP_CONTACTS_ID),

        CONVERSATIONS(ConversationEntry.URI_PATH_INFO),
        CONVERSATION(ConversationEntry.URI_PATH_INFO_ID),

        WINDOW_PARTICIPANTS(ParticipantEntry.URI_PATH_INFO),
        WINDOW_PARTICIPANT(ParticipantEntry.URI_PATH_INFO_ID),

        HISTORY_CONTACT_BASED(WindowViewEntry.URI_PATH_INFO_CBC),
        HISTORY_CONTEXT_BASED(WindowViewEntry.URI_PATH_INFO_CONTEXT),
        OPENPEER_CONTACT(OpenpeerContactEntry.URI_PATH_INFO_ID),
        OPENPEER_CONTACT_DETAIL(OpenpeerContactEntry.URI_PATH_INFO_DETAIL),
        OPENPEER_CONTACT_DETAIL_ID(OpenpeerContactEntry.URI_PATH_INFO_DETAIL_ID),

        // USER(UserEntry.TABLE_NAME + "/#"),

        IDENTITY_CONTACTS(IdentityContactEntry.TABLE_NAME),
        IDENTITY_CONTACT(IdentityContactEntry.TABLE_NAME + "/#"),

        ROLODEX_CONTACTS(RolodexContactEntry.TABLE_NAME),
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

    private Uri insertMessage(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long result = db.insert(DatabaseContracts.MessageEntry.TABLE_NAME,
                null, values);
        getContext().getContentResolver().notifyChange(
                ContentUris.withAppendedId(uri, result), null);
        notifyChatHistoryChange();
        return uri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new OPDatabaseHelper(getContext(),
                OPDatabaseHelper.DATABASE_NAME, // the name of the database)
                null, // uses the default SQLite cursor
                OPDatabaseHelper.DATABASE_VERSION);
        initMatcher();
        instance = this;
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        MatcherInfo value = MatcherInfo.values()[mUriMatcher.match(uri)];
        StringBuilder stringBuilder = new StringBuilder();

        switch (value) {
        case MESSAGES_WINDOW:
            stringBuilder
                    .append(DatabaseContracts.QUERY_MESSAGES_CONTACTS_BASED);
            int windowId = Integer.parseInt(uri.getLastPathSegment());
            stringBuilder.append("where " + DatabaseContracts.COLUMN_CBC_ID
                    + "="
                    + windowId);
            if (!TextUtils.isEmpty(selection)) {
                stringBuilder.append(" and ").append(selection);
            }
            stringBuilder.append(
                    " union " + DatabaseContracts.QUERY_CALL_CONTACTS_BASED)
                    .append(" where ")
                    .append(DatabaseContracts.COLUMN_CBC_ID + "=" + windowId)
                    .append(" order by time");
            Cursor cursor = mOpenHelper.getReadableDatabase().rawQuery(
                    stringBuilder.toString(), selectionArgs);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
            // return queryMessages(uri, projection, stringBuilder.toString(),
            // selectionArgs, sortOrder);
        case MESSAGE_WINDOW:
            return queryMessage(uri, projection, selection, selectionArgs,
                    sortOrder);
        case MESSAGES_CONTEXT:
            String contextId = uri.getLastPathSegment();
            stringBuilder.append(MessageEntry.COLUMN_CONTEXT_ID + "=?");
            if (!TextUtils.isEmpty(selection)) {
                stringBuilder.append(" and ").append(selection);
            }
            String args[];
            if (selectionArgs != null) {
                args = new String[selectionArgs.length + 1];
                args[0] = contextId;
                for (int i = 0; i < selectionArgs.length; i++) {
                    args[i + 1] = selectionArgs[i];
                }
            } else {
                args = new String[] { contextId };
            }
            return queryMessages(uri, projection, stringBuilder.toString(),
                    args, sortOrder);
        case MESSAGE_THREAD:
            return queryMessage(uri, projection, selection, selectionArgs,
                    sortOrder);
        case OPENPEER_CONTACT:
            return queryOpenPeerContact(uri, projection, selection,
                    selectionArgs,
                    sortOrder);
        case OPENPEER_CONTACT_DETAIL:

            return queryOpenPeerContactDetail(uri, projection,
                    selection,
                    selectionArgs,
                    sortOrder);
        case OPENPEER_CONTACT_DETAIL_ID:
            if (TextUtils.isEmpty(selection)) {
                selection = "oc._id=" + uri.getLastPathSegment();
            } else {
                selection = "oc._id=" + uri.getLastPathSegment() + " and "
                        + selection;
            }
            return queryOpenPeerContactDetail(uri, projection, selection,
                    selectionArgs,
                    sortOrder);
        case ROLODEX_CONTACTS:
            return queryRolodexContacts(uri, projection, selection,
                    selectionArgs,
                    sortOrder);
        case HISTORY_CONTACT_BASED:
            return queryContactBasedChatHistory(uri, projection, selection,
                    selectionArgs,
                    sortOrder);
        case HISTORY_CONTEXT_BASED:
            return queryContextBasedChatHistory(uri, projection, selection,
                    selectionArgs,
                    sortOrder);

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
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } else {
            return null;
        }
    }

    /**
     * @return
     */
    private Cursor queryContextBasedChatHistory(Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        String rawQuery = DatabaseContracts.QUERY_OPENPEER_CHATS_CONTEXT_BASED;
        if (!TextUtils.isEmpty(selection)) {
            rawQuery = rawQuery + " where " + selection;
        }
        Cursor cursor = mOpenHelper.getReadableDatabase().rawQuery(rawQuery,
                selectionArgs);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * @return
     */
    private Cursor queryContactBasedChatHistory(Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        String rawQuery = DatabaseContracts.QUERY_OPENPEER_CHATS_CONTACT_BASED;
        if (!TextUtils.isEmpty(selection)) {
            rawQuery = rawQuery + " where " + selection;
        }
        Cursor cursor = mOpenHelper.getReadableDatabase().rawQuery(rawQuery,
                selectionArgs);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
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
    private Cursor queryOpenPeerContact(Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        long id = Integer.parseInt(uri.getLastPathSegment());
        // String rawQuery =
        // "select oc._id as openpeer_id, oc.stable_id, pf.peer_uri as peer_uri,pf.peerfile_public as peerfile_public,oc.name as name, oc.identity_uri as identity_uri,ip.identity_provider_domain as identity_provider_domain from openpeer_contact oc"
        // +
        // " left join peefile_public pf on oc.peerfile_id=left join rolodex_contact rc on oc._id=rc.openpeer_contact_id left join identity_contact ic on rc.identity_contact_id=ic._id left join identity_provider ip on rc.identity_provider_id=ip._id"
        // + " where oc.id=" + id;
        String rawQuery = "select oc._id as openpeer_contact_id, oc.stable_id as stable_id, pf.peer_uri as peer_uri,pf.peerfile_public as peerfile_public from openpeer_contact oc left join peerfile_public pf on oc.peerfile_id=pf._id where oc._id="
                + id;
        Cursor cursor = mOpenHelper.getReadableDatabase().rawQuery(rawQuery,
                null);
        return cursor;
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
    private Cursor queryOpenPeerContactDetail(Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        String rawQuery = null;

        if (!TextUtils.isEmpty(selection)) {
            rawQuery = DatabaseContracts.QUERY_OPENPEER_CONTACT_DETAIL
                    + " where "
                    + selection;
        } else {
            rawQuery = DatabaseContracts.QUERY_OPENPEER_CONTACT_DETAIL;
        }
        Cursor cursor = mOpenHelper.getReadableDatabase().rawQuery(
                rawQuery,
                selectionArgs);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int result;
        MatcherInfo value = MatcherInfo.values()[mUriMatcher.match(uri)];
        StringBuilder stringBuilder = new StringBuilder();

        switch (value) {
        case MESSAGES_WINDOW:
            int windowId = Integer.parseInt(uri.getLastPathSegment());
            stringBuilder.append(DatabaseContracts.COLUMN_CBC_ID + "="
                    + windowId);
            if (!TextUtils.isEmpty(selection)) {
                stringBuilder.append(" and ").append(selection);
            }
            return updateMessages(uri, values, stringBuilder.toString(),
                    selectionArgs);
        case MESSAGES:
            return updateMessage(uri, values, selection, selectionArgs);
        default:
            String table = uri.getLastPathSegment();
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            result = db.update(table, values, selection, selectionArgs);
        }
        return result;
    }

    private int updateMessages(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        int result = db.update(MessageEntry.TABLE_NAME, values, selection,
                selectionArgs);
        if (result != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            notifyChatHistoryChange();
        }
        return result;
    }

    private int updateMessage(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        int result = db.update(MessageEntry.TABLE_NAME, values, selection,
                selectionArgs);
        if (result != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            notifyChatHistoryChange();
            Cursor cursor = db.query(
                    MessageEntry.TABLE_NAME,
                    null, // The columns to return from the query
                    selection, // The columns for the where clause
                    selectionArgs, // The values for the where clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
                    );
            if (cursor == null || cursor.getCount() == 0) {
                return result;
            }
            switch (getGroupChatMode()) {
            case ContactsBased:
                cursor.moveToFirst();
                long windowId = cursor
                        .getLong(cursor
                                .getColumnIndex(DatabaseContracts.COLUMN_CBC_ID));
                long rowId = cursor.getLong(0);
                String url = MessageEntry.URI_PATH_WINDOW_ID_URI_BASE
                        + windowId + "/" + rowId;
                cursor.close();
                getContext().getContentResolver().notifyChange(
                        getContentUri(url), null);
                break;
            default:
                break;
            }
        }
        return result;
    }

    private int updateContacts(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    void initMatcher() {
        try {
            ProviderInfo providers[] = this
                    .getContext()
                    .getPackageManager()
                    .getPackageInfo(getContext().getPackageName(),
                            PackageManager.GET_PROVIDERS).providers;
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

    Cursor queryRolodexContacts(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        if (projection == null) {
            projection = new String[] {
                    RolodexContactEntry._ID,
                    RolodexContactEntry.COLUMN_OPENPEER_CONTACT_ID,
                    RolodexContactEntry.COLUMN_CONTACT_NAME,
                    RolodexContactEntry.COLUMN_IDENTITY_URI };
        }
        String where = "associated_identity_id in (select _id from associated_identity where account_id=(select _id from account where logged_in=1))";
        if (selection != null) {
            where = where + " and " + selection;
        }
        Cursor cursor = db.query(
                DatabaseContracts.RolodexContactEntry.TABLE_NAME,
                projection, // The columns to return from the query
                where, // The columns for the where clause
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
        Log.d("test",
                "query uri for messages " + uri + " result "
                        + cursor.getCount());
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor queryMessage(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
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
        Log.d("test",
                "query uri for message " + uri + " result " + cursor.getCount());
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

    void notifyChatHistoryChange() {
        switch (OPSdkConfig.getInstance().getGroupChatMode()) {
        case ContactsBased:
            getContext().getContentResolver().notifyChange(
                    getContentUri(WindowViewEntry.URI_PATH_INFO_CBC), null);
            break;
        case ContextBased:
            getContext().getContentResolver().notifyChange(
                    getContentUri(WindowViewEntry.URI_PATH_INFO_CONTEXT), null);
        default:
            break;
        }

    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
        super.shutdown();
        instance.mOpenHelper.close();

    }

    public static void clear() {
        instance.shutdown();
        instance.getContext().deleteDatabase(OPDatabaseHelper.DATABASE_NAME);
    }

    /**
     * @return
     */
    public static OPContentProvider getInstance() {
        // TODO Auto-generated method stub
        return instance;
    }

    private void notifyMessageChanged(String messageId) {
        String where = MessageEntry.COLUMN_MESSAGE_ID + "=?";
        String args[] = new String[] { messageId };
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContracts.ContactsViewEntry.TABLE_NAME,
                null, // The columns to return from the query
                where, // The columns for the where clause
                args, // The values for the where clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
                );
        switch (getGroupChatMode()) {
        case ContactsBased:
            long windowId = cursor.getLong(cursor
                    .getColumnIndex(DatabaseContracts.COLUMN_CBC_ID));
            String url = MessageEntry.URI_PATH_WINDOW_ID_URI_BASE
                    + windowId + "/" + messageId;
            getContext().getContentResolver().notifyChange(
                    getContentUri(url), null);
            break;
        default:
            break;
        }
    }

    GroupChatMode getGroupChatMode() {
        return GroupChatMode.ContactsBased;
    }

    static String getIdClause(Uri uri) {
        return BaseColumns._ID + "=" + uri.getLastPathSegment();
    }

    static String combinedIdClause(String selection, Uri uri) {
        if (TextUtils.isEmpty(selection)) {
            selection = getIdClause(uri);
        } else {
            selection = selection + getIdClause(uri);
        }
        return selection;
    }
}
