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

import static com.openpeer.sdk.datastore.DatabaseContracts.COLUMN_PEER_URI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.javaapi.OPRolodexContact.OPAvatar;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPSdkConfig;
import com.openpeer.sdk.datastore.DatabaseContracts.AccountEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.AssociatedIdentityEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.AvatarEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.CallEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.CallEventEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.ConversationEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.ConversationEventEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.IdentityContactEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.IdentityProviderEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.MessageEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.MessageEventEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.OpenpeerContactEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.ParticipantEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.RolodexContactEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.ThreadEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.WindowViewEntry;
import com.openpeer.sdk.model.CallEvent;
import com.openpeer.sdk.model.MessageEditState;
import com.openpeer.sdk.model.MessageEvent;
import com.openpeer.sdk.model.OPConversation;
import com.openpeer.sdk.model.OPConversationEvent;
import com.openpeer.sdk.model.OPUser;
import com.openpeer.sdk.utils.DbUtils;

/**
 * The data being stored in database Contacts Conversation history Call history
 * 
 */
public class OPDatastoreDelegateImpl implements OPDatastoreDelegate {
    private static final String TAG = OPDatastoreDelegateImpl.class
            .getSimpleName();

    private static OPDatastoreDelegateImpl instance;

    private Context mContext;

    private OPDatabaseHelper mOpenHelper;
    OPUser mLoggedInUser;
    /**
     * Users cache using peer uri as index
     */
    private Hashtable<String, OPUser> mUsers = new Hashtable<String, OPUser>();
    private Hashtable<Long, OPUser> mUsersById = new Hashtable<Long, OPUser>();

    private OPDatastoreDelegateImpl() {
    }

    public static OPDatastoreDelegateImpl getInstance() {
        if (instance == null) {
            instance = new OPDatastoreDelegateImpl();
        }
        return instance;
    }

    public OPDatastoreDelegateImpl init(Context context) {
        mContext = context;
        /**
         * We use shared instance so we don't have to worry about synchronization
         */
        mOpenHelper = OPDatabaseHelper.getInstance(context);

        return this;// fluent API
    }

    public void setupForTest() {
        mLoggedInUser = new OPUser();
        mLoggedInUser.setUserId(1);
    }

    @Override
    public OPUser getLoggedinUser() {
        if (mLoggedInUser != null) {
            return mLoggedInUser;
        }
        SQLiteDatabase db = getWritableDB();
        String rawQuery = "select openpeer_contact_id from rolodex_contact where _id=(select self_contact_id from associated_identity where account_id =(select _id from account where logged_in=1))";
        Cursor cursor = db.rawQuery(rawQuery, null);
        if (cursor == null) {
            OPLogger.debug(OPLogLevel.LogLevel_Debug,
                    "getLoggedinUser retrieved 0 row");
            return null;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long selfContactId = cursor.getLong(0);
            cursor.close();
            OPUser user = getUserById(selfContactId);
            cacheUser(user);
            return user;
        }
        return null;
    }

    @Override
    public String getReloginInfo() {
        String selection = AccountEntry.COLUMN_LOGGED_IN + "=1";
        Cursor cursor = query(AccountEntry.TABLE_NAME, null, selection, null);
        if (cursor != null) {
            String reloginInfo = null;
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                reloginInfo = cursor.getString(cursor
                        .getColumnIndex(AccountEntry.COLUMN_RELOGIN_INFO));
            }
            cursor.close();
            return reloginInfo;
        } else {
            OPLogger.debug(OPLogLevel.LogLevel_Debug,
                    "getReloginInfo retrieved 0 row");
            return null;
        }
    }

    public String getDownloadedContactsVersion(String identityUri) {
        String version = null;
        SQLiteDatabase db = getWritableDB();
        Cursor cursor = simpleQuery(
                db,
                AssociatedIdentityEntry.TABLE_NAME,
                new String[] { AssociatedIdentityEntry.COLUMN_IDENTITY_CONTACTS_VERSION },
                AssociatedIdentityEntry.COLUMN_IDENTITY_URI + "=?",
                new String[] { identityUri }
                );
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                version = cursor.getString(0);
            }
            cursor.close();
        }
        Log.d("test", "datastore contacts version " + version);
        return version;
    }

    @Override
    public String getAvatarUri(long rolodexId, int width, int height) {
        SQLiteDatabase db = getWritableDB();
        // String rawQuery = "select * from avatar where rolodex_id=" + rolodexId;
        String orderBy = " width-" + width;
        String limit = "1";
        Cursor cursor = db.query(AvatarEntry.TABLE_NAME,
                new String[] { AvatarEntry.COLUMN_AVATAR_URI },
                AvatarEntry.COLUMN_ROLODEX_ID + "=" + rolodexId, null, null,
                null, orderBy, limit);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String url = cursor.getString(0);

            cursor.close();
            return url;
        }
        return null;
    }

    public List<OPAvatar> getAvatars(long contactId) {
        Cursor cursor = query(AvatarEntry.TABLE_NAME, null,
                "rolodex_id=" + contactId, null);
        if (cursor != null) {
            List<OPAvatar> avatars = new ArrayList<OPAvatar>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                OPAvatar avatar = new OPAvatar(
                        cursor.getString(cursor
                                .getColumnIndex(AvatarEntry.COLUMN_AVATAR_NAME)),
                        cursor.getString(cursor
                                .getColumnIndex(AvatarEntry.COLUMN_AVATAR_URI)),
                        cursor.getInt(cursor
                                .getColumnIndex(AvatarEntry.COLUMN_WIDTH)),
                        cursor.getInt(cursor
                                .getColumnIndex(AvatarEntry.COLUMN_HEIGHT)));
                avatars.add(avatar);
                cursor.moveToNext();
            }
            cursor.close();
            return avatars;
        }
        return null;
    }

    @Override
    public OPMessage getMessage(String messageId) {
        String messageIDs[] = new String[] { messageId };
        OPMessage message = null;
        Cursor cursor = query(DatabaseContracts.MessageEntry.TABLE_NAME, null,
                MessageEntry.COLUMN_MESSAGE_ID + "=?", messageIDs);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                message = OPModelCursorHelper.messageFromCursor(cursor);
            }
            cursor.close();
        }
        return message;
    }

    @Override
    public OPUser getUserByPeerUri(String peerUri) {
        OPUser user = mUsers.get(peerUri);
        if (user != null) {
            return user;
        }
        String selection = COLUMN_PEER_URI + "=?";
        String args[] = new String[] { peerUri };
        Cursor cursor = mContext
                .getContentResolver()
                .query(OPContentProvider.getContentUri(OpenpeerContactEntry.URI_PATH_INFO_DETAIL),
                        null, selection, args, null);
        if (cursor.getCount() > 0) {
            user = fromDetailCursor(cursor);
            cacheUser(user);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public OPUser getUser(OPContact contact,
            List<OPIdentityContact> identityContacts) {
        String peerUri = contact.getPeerURI();
        OPUser user = mUsers.get(peerUri);
        if (user != null) {
            return user;
        }
        SQLiteDatabase db = getWritableDB();
        user = new OPUser(contact, identityContacts);
        long contactRecordId = 0;
        if (identityContacts == null || identityContacts.size() == 0) {
            OPLogger.error(OPLogLevel.LogLevel_Basic,
                    "No identity attached to contact " + contact.getPeerURI());
            throw new RuntimeException("No identity attached to contact "
                    + contact.getPeerURI());
        }
        // find the existing record of the account
        String stableId = identityContacts.get(0).getStableID();
        String where = OpenpeerContactEntry.COLUMN_STABLE_ID + "=?" + " or "
                + OpenpeerContactEntry.COLUMN_PEERURI + "=?";
        String args[] = new String[] { stableId, peerUri };
        contactRecordId = DbUtils.simpleQueryForId(getWritableDB(),
                OpenpeerContactEntry.TABLE_NAME,
                where, args);
        if (contactRecordId == 0) {// No existing op contact found
            boolean isExisting = false;
            // look for the identity uri matches
            for (OPIdentityContact ic : identityContacts) {

                String params[] = new String[] { ic.getIdentityURI() };
                String opIdStr = simpleQueryForString(getWritableDB(),
                        RolodexContactEntry.TABLE_NAME,
                        RolodexContactEntry.COLUMN_OPENPEER_CONTACT_ID,
                        RolodexContactEntry.COLUMN_IDENTITY_URI + "=?", params);
                if (!TextUtils.isEmpty(opIdStr)) {
                    contactRecordId = Long.parseLong(opIdStr);
                    // Found OP contact. now update the openpeer_contact record
                    if (contactRecordId > 0) {
                        user.setUserId(contactRecordId);
                        updateOPTable(contactRecordId, stableId, peerUri,
                                contact.getPeerFilePublic());
                        isExisting = true;
                        break;
                    }
                }
            }
            if (!isExisting) {
                saveUser(user, 0);
            }
        } else {
            user.setUserId(contactRecordId);
            updateOPTable(contactRecordId, stableId, peerUri,
                    contact.getPeerFilePublic());
            // add/delete identities associated
            for (OPIdentityContact ic : identityContacts) {
                if (DbUtils.simpleQueryForId(db, RolodexContactEntry.TABLE_NAME,
                        RolodexContactEntry.COLUMN_IDENTITY_URI + "=?",
                        new String[] { ic.getIdentityURI() }) > 0) {

                }
            }
        }
        cacheUser(user);
        return user;
    }

    @Override
    public List<OPUser> getUsers(long[] userIDs) {
        List<OPUser> users = new ArrayList<OPUser>();
        for (long userId : userIDs) {
            OPUser user = getUserById(userId);
            users.add(user);
        }
        return users;
    }

    public OPUser getUserById(long id) {
        OPUser user = mUsersById.get(id);
        if (user != null) {
            return user;
        }
        Cursor cursor = mContext
                .getContentResolver()
                .query(OPContentProvider.getContentUri(OpenpeerContactEntry.URI_PATH_INFO_DETAIL
                        + "/" + id), null, null, null, null);
        if(cursor.getCount()>0) {
            user = fromDetailCursor(cursor);
            cacheUser(user);
        }
        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.openpeer.sdk.datastore.OPDatastoreDelegate#getConversationEvents(long)
     */
    @Override
    public List<OPConversationEvent> getConversationEvents(OPConversation conversation) {
        List<OPConversationEvent> events = null;
        Cursor cursor = getWritableDB().query(ConversationEventEntry.TABLE_NAME, null,
                ConversationEventEntry.COLUMN_CONVERSATION_ID + "=" + conversation.getId(), null,
                null,
                null, null);
        if (cursor.getCount() > 0) {
            events = new ArrayList<OPConversationEvent>();
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                OPConversationEvent event = new OPConversationEvent(conversation,
                        OPConversationEvent.EventTypes.valueOf(cursor.getString(cursor
                                .getColumnIndex(ConversationEventEntry.COLUMN_EVENT))),
                        cursor.getString(cursor
                                .getColumnIndex(ConversationEventEntry.COLUMN_CONTENT)));
                events.add(event);
            }
        }
        cursor.close();
        return events;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.openpeer.sdk.datastore.OPDatastoreDelegate#getMessageEvents(java.lang.String)
     */
    @Override
    public List<MessageEvent> getMessageEvents(String messageId) {
        List<MessageEvent> events = null;
        // TODO: fix the message id being replaced when editting/deleting. This shouldn't be a problemm since we'll always be using the
        // current message id
        Cursor cursor = getWritableDB().query(
                MessageEventEntry.TABLE_NAME,
                null,
                MessageEventEntry.COLUMN_MESSAGE_ID
                        + "=(select _id from message where message_id=?)",
                new String[] { messageId },
                null,
                null, null);
        if (cursor.getCount() > 0) {
            events = new ArrayList<MessageEvent>();
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                MessageEvent event = new MessageEvent(messageId,
                        MessageEvent.EventType.valueOf(cursor.getString(cursor
                                .getColumnIndex(MessageEventEntry.COLUMN_EVENT))),
                        cursor.getString(cursor
                                .getColumnIndex(MessageEventEntry.COLUMN_DESCRIPTION)),
                        cursor.getLong(cursor.getColumnIndex(MessageEventEntry.COLUMN_TIME)));
                events.add(event);
            }
        }
        cursor.close();
        return events;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.openpeer.sdk.datastore.OPDatastoreDelegate#getCallEvents(java.lang.String)
     */
    @Override
    public List<CallEvent> getCallEvents(String callId) {
        List<CallEvent> events = null;
        Cursor cursor = getWritableDB().query(CallEventEntry.TABLE_NAME, null,
                CallEventEntry.COLUMN_CALL_ID + "= (select _id from call where call_id=?)",
                new String[] { callId },
                null, null, null);
        if (cursor.getCount() > 0) {
            events = new ArrayList<CallEvent>();
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                CallEvent event = new CallEvent(callId,
                        CallStates.valueOf(cursor.getString(cursor
                                .getColumnIndex(CallEventEntry.COLUMN_EVENT))),
                        cursor.getLong(cursor.getColumnIndex(CallEventEntry.COLUMN_TIME)));
                events.add(event);
            }
        }
        cursor.close();
        return events;
    }

    @Override
    public void markMessagesRead(OPConversation conversation) {
        ContentValues values = new ContentValues();
        values.put(MessageEntry.COLUMN_MESSAGE_READ, 1);
        String where = MessageEntry.COLUMN_MESSAGE_READ + "=0";
        String url = null;
        switch (conversation.getType()) {
        case ContactsBased:
            url = DatabaseContracts.MessageEntry.URI_PATH_WINDOW_ID_URI_BASE
                    + conversation.getCurrentWindowId();
            break;
        case ContextBased:
            url = DatabaseContracts.MessageEntry.URI_PATH_INFO_CONTEXT_URI_BASE
                    + conversation.getContextId();
            break;
        default:
            return;
        }

        int count = mContext.getContentResolver().update(
                OPContentProvider.getContentUri(url), values, where, null);
        OPLogger.debug(OPLogLevel.LogLevel_Debug, "markMessagesRead update count " + count);
    }

    @Override
    public int updateMessage(OPMessage message, OPConversation conversation) {
        int count = 0;
        SQLiteDatabase db = getWritableDB();
        try {
            db.beginTransaction();
            MessageEvent event = null;
            if (TextUtils.isEmpty(message.getMessage())) {

                message.setEditState(MessageEditState.Deleted);
                event = new MessageEvent(message.getMessageId(), MessageEvent.EventType.Delete, "",
                        System.currentTimeMillis());
                saveMessageEvent(event);
            } else {
                // this is an edit. put in the edited flag and new message text
                message.setEditState(MessageEditState.Edited);
                event = new MessageEvent(message.getMessageId(), MessageEvent.EventType.Delete, "",
                        System.currentTimeMillis());
                saveMessageEvent(event);
            }
            String where = MessageEntry.COLUMN_MESSAGE_ID + "=?";
            String args[] = new String[] { message.getReplacesMessageId() };
            String url = DatabaseContracts.MessageEntry.URI_PATH_WINDOW_ID_URI_BASE
                    + conversation.getCurrentWindowId();

            ContentValues values = new ContentValues();
            values.put(MessageEntry.COLUMN_MESSAGE_ID, message.getMessageId());
            values.put(MessageEntry.COLUMN_MESSAGE_TEXT, message.getMessage());

            values.put(MessageEntry.COLUMN_MESSAGE_TYPE,
                    message.getMessageType());
            values.put(MessageEntry.COLUMN_SENDER_ID, message.getSenderId());
            values.put(MessageEntry.COLUMN_CBC_ID, conversation.getCurrentWindowId());
            values.put(MessageEntry.COLUMN_CONTEXT_ID, conversation.getContextId());

            values.put(MessageEntry.COLUMN_MESSAGE_READ, message.isRead());
            values.put(MessageEntry.COLUMN_EDIT_STATUS, message.getEditState()
                    .ordinal());

            count = mContext.getContentResolver().update(
                    OPContentProvider.getContentUri(url), values, where, args);
            if (count == 0) {
                OPLogger.error(OPLogLevel.LogLevel_Basic,
                        "updating message failed " + message.getMessageId());
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {

        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public Uri saveMessage(OPMessage message, OPConversation conversation) {
        SQLiteDatabase db = getWritableDB();
        ContentValues values = new ContentValues();
        values.put(MessageEntry.COLUMN_MESSAGE_ID, message.getMessageId());
        values.put(MessageEntry.COLUMN_MESSAGE_TEXT, message.getMessage());

        values.put(MessageEntry.COLUMN_MESSAGE_TYPE, message.getMessageType());
        values.put(MessageEntry.COLUMN_SENDER_ID, message.getSenderId());
        values.put(MessageEntry.COLUMN_CBC_ID, conversation.getCurrentWindowId());
        values.put(MessageEntry.COLUMN_CONTEXT_ID, conversation.getContextId());
        values.put(MessageEntry.COLUMN_MESSAGE_READ, message.isRead());
        values.put(MessageEntry.COLUMN_EDIT_STATUS, message.getEditState().ordinal());

        // we set the time here because we don't want to update the message time for edit/delete
        values.put(MessageEntry.COLUMN_MESSAGE_TIME, message.getTime().toMillis(false));
        values.put(MessageEntry.COLUMN_CONVERSATION_EVENT_ID, conversation.getLastEvent().getId());
        long id = db.insert(MessageEntry.TABLE_NAME, null, values);

        Uri uri = notifyMessageChanged(conversation.getContextId(),
                conversation.getCurrentWindowId(), id);

        return uri;
    }

    @Override
    public boolean updateMessageDeliveryStatus(String messageId,
            MessageDeliveryStates deliveryStatus) {
        int count = 0;
        SQLiteDatabase db = getWritableDB();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(MessageEntry.COLUMN_MESSAGE_ID, messageId);
            values.put(MessageEntry.COLUMN_MESSAGE_DELIVERY_STATUS, deliveryStatus.name());
            String selection = MessageEntry.COLUMN_MESSAGE_ID + "=?";
            String args[] = new String[] { messageId };
            count = update(MessageEntry.TABLE_NAME, values, selection, args);
            MessageEvent event = new MessageEvent(messageId,
                    MessageEvent.EventType.DeliveryStateChange,
                    MessageEvent.getStateChangeJsonBlob(deliveryStatus),
                    System.currentTimeMillis());
            saveMessageEvent(event);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {

        } finally {
            db.endTransaction();
        }
        return count > 0;
    }

    @Override
    public boolean saveAccount(OPAccount account) {
        boolean result = false;
        SQLiteDatabase db = getWritableDB();
        try {
            db.beginTransaction();
            long accountRecordId = 0;
            OPContact contact = OPContact.getForSelf(account);
            // find the existing record of the account
            String peerUri = contact.getPeerURI();
            String peerfile = contact.getPeerFilePublic();
            String stableId = account.getStableID();
            List<OPIdentity> identities = account.getAssociatedIdentities();
            String where = AccountEntry.COLUMN_STABLE_ID + "=?";
            String args[] = new String[] { stableId };

            accountRecordId = DbUtils.simpleQueryForId(db, AccountEntry.TABLE_NAME, where, args);
            if (accountRecordId == 0) {
                if (identities.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (OPIdentity identity : identities) {
                        sb.append(identity.getIdentityURI() + ",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    Cursor cursor = db
                            .query(AssociatedIdentityEntry.TABLE_NAME,
                                    new String[] { AssociatedIdentityEntry.COLUMN_ACCOUNT_ID },
                                    AssociatedIdentityEntry.COLUMN_IDENTITY_URI
                                            + " in (?)",
                                    new String[] { sb.toString() },
                                    null, null, null);
                    // There could be multiple records when we support multiple identities but they should all point to same account.
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        accountRecordId = cursor.getLong(0);
                    }
                    cursor.close();

                } else {
                    Cursor cursor = db.query(AssociatedIdentityEntry.TABLE_NAME,
                            new String[] { AssociatedIdentityEntry.COLUMN_ACCOUNT_ID },
                            AssociatedIdentityEntry.COLUMN_IDENTITY_URI + " =?",
                            new String[] { identities.get(0).getIdentityURI() }, null,
                            null, null);
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        accountRecordId = cursor.getLong(0);
                    }
                    cursor.close();
                }
            }
            // This is a new account, insert it
            if (accountRecordId == 0) {
                ContentValues values = new ContentValues();
                values.put(AccountEntry.COLUMN_LOGGED_IN, 0);
                update(AccountEntry.TABLE_NAME, values, null, null);

                values.put(AccountEntry.COLUMN_LOGGED_IN, 1);
                values.put(AccountEntry.COLUMN_STABLE_ID, stableId);
                values.put(AccountEntry.COLUMN_RELOGIN_INFO,
                        account.getReloginInformation());

                accountRecordId = db.insert(AccountEntry.TABLE_NAME, null, values);

                // insert openpeer_contact entry of myself
                long opId = saveOPContactTable(stableId, peerUri, peerfile);
                saveOrUpdateIdentities(identities, accountRecordId, opId);
                account.setAccountId(accountRecordId);
                account.setSelfContactId(opId);
                result = true;
            } else {
                account.setAccountId(accountRecordId);
                String rawQuery = "select openpeer_contact_id from rolodex_contact where _id=(select self_contact_id from associated_identity where account_id =(select _id from account where logged_in=1))";
                Cursor cursor = db.rawQuery(rawQuery, null);
                if (cursor == null) {
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    long selfContactId = cursor.getLong(0);
                    account.setSelfContactId(selfContactId);

                    ContentValues values = new ContentValues();
                    values.put(AccountEntry.COLUMN_LOGGED_IN, 0);
                    update(AccountEntry.TABLE_NAME, values, null, null);

                    values.put(AccountEntry.COLUMN_LOGGED_IN, 1);
                    values.put(AccountEntry.COLUMN_STABLE_ID, stableId);
                    values.put(AccountEntry.COLUMN_RELOGIN_INFO, account.getReloginInformation());

                    accountRecordId = db.update(AccountEntry.TABLE_NAME, values, "_id="
                            + accountRecordId, null);
                    values.clear();
                    values = new ContentValues();
                    values.put(OpenpeerContactEntry.COLUMN_STABLE_ID, stableId);
                    values.put(OpenpeerContactEntry.COLUMN_PEERURI, peerUri);
                    values.put(OpenpeerContactEntry.COLUMN_PEERFILE_PUBLIC, peerfile);
                    getWritableDB().update(OpenpeerContactEntry.TABLE_NAME, values,
                            "_id=" + selfContactId, null);
                    saveOrUpdateIdentities(identities, accountRecordId, selfContactId);
                    result = true;
                } else {
                    OPLogger.error(OPLogLevel.LogLevel_Basic,
                            "Something went terribly wrong because the self contact is empty for account!");
                }

            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return result;
    }

    @Override
    public List<OPRolodexContact> saveDownloadedRolodexContacts(
            OPIdentity identity,
            List<OPRolodexContact> contacts, String contactsVersion) {
        SQLiteDatabase db = getWritableDB();
        try {
            db.beginTransaction();
            long identityId = DbUtils.simpleQueryForId(db,
                    AssociatedIdentityEntry.TABLE_NAME,
                    AssociatedIdentityEntry.COLUMN_IDENTITY_URI + "=?",
                    new String[] { identity.getIdentityURI() });

            setDownloadedContactsVersion(
                    identity.getIdentityURI(), contactsVersion);
            List<OPRolodexContact> contactsToLookup = new ArrayList<OPRolodexContact>();
            for (OPRolodexContact contact : contacts) {
                switch (contact.getDisposition()) {
                case Disposition_Remove:
                    deleteContact(contact.getIdentityURI());
                    contactsToLookup.add(contact);
                    break;
                case Disposition_Update:
                    // updateRolodexContactTable(contact, 0, 0, identityId);
                    // break;
                default:
                    saveRolodexContactTable(contact, 0, 0, identityId);
                }
            }
            if (!contactsToLookup.isEmpty()) {
                contacts.removeAll(contactsToLookup);
            }
            db.setTransactionSuccessful();
            notifyContactsChanged();
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return contacts;
    }

    @Override
    public long saveConversation(OPConversation conversation) {
        SQLiteDatabase db = getWritableDB();
        long id = 0;
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(ConversationEntry.COLUMN_TYPE, conversation.getType().name());
            values.put(ConversationEntry.COLUMN_START_TIME,
                    System.currentTimeMillis());
            values.put(ConversationEntry.COLUMN_PARTICIPANTS,
                    conversation.getCurrentWindowId());
            values.put(ConversationEntry.COLUMN_CONTEXT_ID,
                    conversation.getContextId());
            values.put(ConversationEntry.COLUMN_ACCOUNT_ID, getLoggedinUser().getUserId());
            id = db.insert(ConversationEntry.TABLE_NAME, null, values);
            conversation.setId(id);

            saveParticipants(conversation.getCurrentWindowId(), conversation.getParticipants());
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public long updateConversation(OPConversation conversation) {
        SQLiteDatabase db = getWritableDB();
        ContentValues values = new ContentValues();
        values.put(ConversationEntry.COLUMN_TYPE, conversation.getType().name());
        values.put(ConversationEntry.COLUMN_START_TIME,
                System.currentTimeMillis());
        values.put(ConversationEntry.COLUMN_PARTICIPANTS,
                conversation.getCurrentWindowId());
        values.put(ConversationEntry.COLUMN_CONTEXT_ID,
                conversation.getContextId());
        values.put(ConversationEntry.COLUMN_ACCOUNT_ID, getLoggedinUser().getUserId());
        long id = db.update(ConversationEntry.TABLE_NAME, values,
                BaseColumns._ID + "=" + conversation.getId(), null);

        return id;
    }

    @Override
    public long saveConversationEvent(OPConversationEvent event) {
        SQLiteDatabase db = getWritableDB();
        ContentValues values = new ContentValues();
        OPConversation conversation = event.getConversation();
        values.put(ConversationEventEntry.COLUMN_CONVERSATION_ID,
                conversation.getId());
        values.put(ConversationEventEntry.COLUMN_EVENT, event.getEvent().name());
        values.put(ConversationEventEntry.COLUMN_CONTENT, event.getContent());
        values.put(ConversationEventEntry.COLUMN_PARTICIPANTS,
                conversation.getCurrentWindowId());
        values.put(ConversationEventEntry.COLUMN_TIME,
                System.currentTimeMillis());
        long id = db.insert(ConversationEventEntry.TABLE_NAME, null, values);
        saveParticipants(conversation.getCurrentWindowId(), conversation.getParticipants());
        return id;
    }

    @Override
    public long saveMessageEvent(MessageEvent event) {
        SQLiteDatabase db = getWritableDB();
        long messageRecordId = DbUtils.simpleQueryForId(db, MessageEntry.TABLE_NAME,
                MessageEntry.COLUMN_MESSAGE_ID + "=?",
                new String[] { event.getMessageId() });
        ContentValues values = new ContentValues();
        values.put(MessageEventEntry.COLUMN_MESSAGE_ID, messageRecordId);
        values.put(MessageEventEntry.COLUMN_EVENT, event.getEvent().name());
        values.put(MessageEventEntry.COLUMN_DESCRIPTION, event.getDescription());
        values.put(MessageEventEntry.COLUMN_TIME, event.getTime());
        return db.insert(MessageEventEntry.TABLE_NAME, null, values);
    }

    @Override
    public long saveCall(OPCall call,
            OPConversation conversation) {
        SQLiteDatabase db = getWritableDB();
        String callId = call.getCallID();
        long callRecordId = DbUtils.simpleQueryForId(db, CallEntry.TABLE_NAME,
                CallEntry.COLUMN_CALL_ID + "=?", new String[] { callId });
        if (callRecordId != 0) {
            return callRecordId;
        }
        ContentValues values = new ContentValues();
        values.put(CallEntry.COLUMN_CALL_ID, callId);
        values.put(CallEntry.COLUMN_CBC_ID, conversation.getCurrentWindowId());
        values.put(CallEntry.COLUMN_CONTEXT_ID, conversation.getContextId());

        values.put(CallEntry.COLUMN_CONVERSATION_EVENT_ID, conversation.getLastEvent().getId());
        values.put(CallEntry.COLUMN_PEER_ID, call.getPeerUser().getUserId());
        // 0 for outgoing,1 for incoming
        values.put(CallEntry.COLUMN_DIRECTION, call.getCaller().isSelf() ? 0
                : 1);
        values.put(CallEntry.COLUMN_TIME, System.currentTimeMillis());
        if (call.hasVideo()) {
            values.put(CallEntry.COLUMN_TYPE,
                    OPMessage.OPMessageType.TYPE_INERNAL_CALL_VIDEO);
        } else {
            values.put(CallEntry.COLUMN_TYPE,
                    OPMessage.OPMessageType.TYPE_INERNAL_CALL_AUDIO);
        }
        return db.insert(CallEntry.TABLE_NAME, null, values);
    }

    @Override
    public long saveCallEvent(String callId,
            CallEvent event) {
        long eventRecordId = 0;
        SQLiteDatabase db = getWritableDB();
        try {
            db.beginTransaction();
            Cursor cursor = db.query(CallEntry.TABLE_NAME, null,
                    CallEntry.COLUMN_CALL_ID + "=?",
                    new String[] { callId }, null, null, null);
            if (cursor == null) {

            }
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                long callRecordId = cursor.getLong(0);
                String contextId = cursor.getString(cursor
                        .getColumnIndex(CallEntry.COLUMN_CONTEXT_ID));
                long cbcId = cursor.getLong(cursor
                        .getColumnIndex(CallEntry.COLUMN_CBC_ID));
                ContentValues values = new ContentValues();
                values.put(CallEventEntry.COLUMN_CALL_ID, callRecordId);
                values.put(CallEventEntry.COLUMN_EVENT, event.getState().name());
                values.put(MessageEventEntry.COLUMN_TIME, event.getTime());
                if (event.getState() == CallStates.CallState_Open) {
                    ContentValues callValues = new ContentValues();
                    callValues.put(CallEntry.COLUMN_ANSWER_TIME,
                            System.currentTimeMillis());

                    int count = db
                            .update(CallEntry.TABLE_NAME, values, "_id=" + callRecordId, null);
                }
                if (event.getState() == CallStates.CallState_Closed) {
                    ContentValues callValues = new ContentValues();
                    callValues.put(CallEntry.COLUMN_END_TIME, System.currentTimeMillis());
                    updateCallTable(callRecordId, callValues);
                }

                eventRecordId = db.insert(CallEventEntry.TABLE_NAME, null, values);
                notifyMessageChanged(contextId, cbcId, 30000 + eventRecordId);
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            // TODO: handle exception
        } finally {
            db.endTransaction();
        }

        return eventRecordId;
    }

    @Override
    public void saveIdentityContact(List<OPIdentityContact> iContacts,
            long associatedIdentityId) {
        SQLiteDatabase db = getWritableDB();
        try {
            db.beginTransaction();
            for (OPIdentityContact contact : iContacts) {
                saveIdentityContact(db, contact, associatedIdentityId);
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean deleteIdentity(String identityUri) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.openpeer.sdk.datastore.OPDatastoreDelegate#shutdown()
     */
    @Override
    public void onSignOut() {
        OPContentProvider.getInstance().shutdown();
        mOpenHelper.closeAndDeleteDB();
    }

    private OPUser saveUser(OPUser user, long associatedIdentityId) {
        SQLiteDatabase db = getWritableDB();
        try {
            db.beginTransaction();
            OPContact opContact = user.getOPContact();
            List<OPIdentityContact> identityContacts = user
                    .getIdentityContacts();

            long opId = saveOPContactTable(identityContacts.get(0)
                    .getStableID(), opContact.getPeerURI(),
                    opContact.getPeerFilePublic());

            for (OPIdentityContact contact : identityContacts) {
                long identityContactId = saveIdentityContactTable(contact);

                long rolodexId = saveRolodexContactTable(contact, opId,
                        identityContactId, associatedIdentityId);

            }
            user.setUserId(opId);
            notifyContactsChanged();
            db.setTransactionSuccessful();
            return user;
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return user;

    }

    private void updateCallTable(long id, ContentValues values) {

    }

    private void saveThread(long conversationId,
            OPConversationThread thread) {
        SQLiteDatabase db = getWritableDB();
        ContentValues values = new ContentValues();
        values.put(ThreadEntry.COLUMN_CONVERSATION_ID, conversationId);
        values.put(ThreadEntry.COLUMN_THREAD_ID, thread.getThreadID());
        values.put(ThreadEntry.COLUMN_TIME, System.currentTimeMillis());
        db.insert(ThreadEntry.TABLE_NAME, null, values);
    }

    private long saveRolodexContactTable(OPRolodexContact contact,
            long opId,
            long identityContactId,
            long associatedIdentityId) throws SQLiteException {
        SQLiteDatabase db = getWritableDB();

        long identityProviderId = DbUtils.simpleQueryForId(db,
                IdentityProviderEntry.TABLE_NAME,
                IdentityProviderEntry.COLUMN_DOMAIN + "=?",
                new String[] { contact.getIdentityProvider() });
        if (identityProviderId == 0) {
            identityProviderId = saveIdentityProviderTable(contact
                    .getIdentityProvider());
        }
        ContentValues values = new ContentValues();
        if (opId != 0) {
            values.put(RolodexContactEntry.COLUMN_OPENPEER_CONTACT_ID, opId);
        }
        if (identityContactId != 0) {
            values.put(RolodexContactEntry.COLUMN_IDENTITY_CONTACT_ID,
                    identityContactId);
        }
        if (associatedIdentityId != 0) {
            values.put(RolodexContactEntry.COLUMN_ASSOCIATED_IDENTITY_ID,
                    associatedIdentityId);
        }
        values.put(RolodexContactEntry.COLUMN_IDENTITY_PROVIDER_ID,
                identityProviderId);
        values.put(RolodexContactEntry.COLUMN_CONTACT_NAME,
                contact.getName());

        values.put(RolodexContactEntry.COLUMN_IDENTITY_URI,
                contact.getIdentityURI());
        values.put(RolodexContactEntry.COLUMN_PROFILE_URL,
                contact.getProfileURL());
        values.put(RolodexContactEntry.COLUMN_VPROFILE_URL,
                contact.getVProfileURL());

        long rolodexId = DbUtils.simpleQueryForId(db, RolodexContactEntry.TABLE_NAME,
                RolodexContactEntry.COLUMN_IDENTITY_URI + "=?",
                new String[] { contact.getIdentityURI() });
        if (rolodexId == 0) {
            rolodexId = db.insert(RolodexContactEntry.TABLE_NAME, null,
                    values);
            if (rolodexId == -1) {
                throw new SQLiteException("Inserting rolodex contact failed "
                        + values.toString());
            }
            // insert avatars
            List<OPAvatar> avatars = contact.getAvatars();
            if (avatars != null && !avatars.isEmpty()) {
                for (OPAvatar avatar : avatars) {
                    saveAvatarTable(avatar, rolodexId);
                }
            }
        } else {
            db.update(RolodexContactEntry.TABLE_NAME, values, BaseColumns._ID
                    + "=" + rolodexId, null);
            List<OPAvatar> avatars = contact.getAvatars();
            if (avatars != null && !avatars.isEmpty()) {
                for (OPAvatar avatar : avatars) {
                    long avatarRecordId = DbUtils.simpleQueryForId(db,
                            AvatarEntry.TABLE_NAME,
                            AvatarEntry.COLUMN_AVATAR_URI + "=? and "
                                    + AvatarEntry.COLUMN_ROLODEX_ID + "="
                                    + rolodexId,
                            new String[] { avatar.getURL() });
                    if (avatarRecordId == 0)
                        avatarRecordId = saveAvatarTable(avatar, rolodexId);
                }
                // TODO: delete avatars
            }
        }

        return rolodexId;
    }

    private long updateRolodexContactTable(OPRolodexContact contact,
            long opId,
            long identityContactId,
            long associatedIdentityId) throws SQLiteException {
        SQLiteDatabase db = getWritableDB();

        long identityProviderId = DbUtils.simpleQueryForId(db,
                IdentityProviderEntry.TABLE_NAME,
                IdentityProviderEntry.COLUMN_DOMAIN + "=?",
                new String[] { contact.getIdentityProvider() });
        if (identityProviderId == 0) {
            identityProviderId = saveIdentityProviderTable(contact
                    .getIdentityProvider());
        }
        ContentValues values = new ContentValues();
        if (opId != 0) {
            values.put(RolodexContactEntry.COLUMN_OPENPEER_CONTACT_ID, opId);
        }
        if (identityContactId != 0) {
            values.put(RolodexContactEntry.COLUMN_IDENTITY_CONTACT_ID,
                    identityContactId);
        }
        if (associatedIdentityId != 0) {
            values.put(RolodexContactEntry.COLUMN_ASSOCIATED_IDENTITY_ID,
                    associatedIdentityId);
        }
        values.put(RolodexContactEntry.COLUMN_IDENTITY_PROVIDER_ID,
                identityProviderId);

        values.put(RolodexContactEntry.COLUMN_CONTACT_NAME,
                contact.getName());
        values.put(RolodexContactEntry.COLUMN_IDENTITY_URI,
                contact.getIdentityURI());
        values.put(RolodexContactEntry.COLUMN_PROFILE_URL,
                contact.getProfileURL());
        values.put(RolodexContactEntry.COLUMN_VPROFILE_URL,
                contact.getVProfileURL());
        long rolodexId = db.insert(RolodexContactEntry.TABLE_NAME, null,
                values);
        // insert avatars
        List<OPAvatar> avatars = contact.getAvatars();
        if (avatars != null && !avatars.isEmpty()) {
            for (OPAvatar avatar : avatars) {
                saveAvatarTable(avatar, rolodexId);
            }
        }

        return rolodexId;
    }

    private long saveIdentityContactTable(OPIdentityContact contact)
            throws SQLiteException {
        ContentValues values = new ContentValues();
        values.put(IdentityContactEntry.COLUMN_PRORITY,
                contact.getPriority());
        values.put(IdentityContactEntry.COLUMN_WEIGHT,
                contact.getWeight());
        values.put(IdentityContactEntry.COLUMN_IDENTITY_PROOF_BUNDLE,
                contact.getIdentityProofBundle());
        values.put(IdentityContactEntry.COLUMN_LAST_UPDATE_TIME, contact
                .getLastUpdated().toMillis(false));
        values.put(IdentityContactEntry.COLUMN_EXPIRE, contact.getExpires()
                .toMillis(false));

        return getWritableDB().insert(IdentityContactEntry.TABLE_NAME, null,
                values);
    }

    private long saveIdentityTable(OPIdentity contact, long accountId,
            long selfContactId)
            throws SQLiteException {
        SQLiteDatabase db = getWritableDB();
        String identityProvider = contact.getIdentityProviderDomain();
        ContentValues values = new ContentValues();
        values.put(AssociatedIdentityEntry.COLUMN_ACCOUNT_ID,
                accountId);
        values.put(AssociatedIdentityEntry.COLUMN_IDENTITY_URI,
                contact.getIdentityURI());
        values.put(AssociatedIdentityEntry.COLUMN_SELF_CONTACT_ID,
                selfContactId);
        long identityProviderId = DbUtils.simpleQueryForId(db,
                IdentityProviderEntry.TABLE_NAME,
                IdentityProviderEntry.COLUMN_DOMAIN + "=?",
                new String[] { identityProvider });
        if (identityProviderId == 0) {
            identityProviderId = saveIdentityProviderTable(identityProvider);
        }
        values.put(AssociatedIdentityEntry.COLUMN_IDENTITY_PROVIDER_ID,
                identityProviderId);
        return db.insert(AssociatedIdentityEntry.TABLE_NAME, null,
                values);
    }

    private SQLiteDatabase getWritableDB() {
        return mOpenHelper.getWritableDatabase();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.openpeer.sdk.datastore.OPDatastoreDelegate#notifyContactsChanged()
     */
    private void notifyContactsChanged() {
        mContext.getContentResolver()
                .notifyChange(
                        OPContentProvider
                                .getContentUri(RolodexContactEntry.URI_PATH_INFO),
                        null);
    }

    /**
     * Construct user object from contacts table
     * 
     * @param cursor
     * @return
     */
    private OPUser fromDetailCursor(Cursor cursor) {

        if (cursor != null & cursor.getCount() > 0) {
            OPUser user = new OPUser();
            List<OPIdentityContact> contacts = new ArrayList<OPIdentityContact>();
            cursor.moveToFirst();

            user.setUserId(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
            user.setPeerUri(cursor.getString(cursor.getColumnIndex(OpenpeerContactEntry.COLUMN_PEERURI)));
            while (!cursor.isAfterLast()) {
                contacts.add(contactFromCursor(cursor));
                cursor.moveToNext();
            }
            user.setIdentityContacts(contacts);
            return user;
        }
        return null;
    }

    private void setDownloadedContactsVersion(String identityUri, String version) {
        ContentValues values = new ContentValues();
        values.put(AssociatedIdentityEntry.COLUMN_IDENTITY_CONTACTS_VERSION,
                version);
        String whereClause = AssociatedIdentityEntry.COLUMN_IDENTITY_URI + "=?";
        String args[] = new String[] { identityUri };
        long rowId = update(AssociatedIdentityEntry.TABLE_NAME, values,
                whereClause, args);
        OPLogger.debug(OPLogLevel.LogLevel_Debug, "setDownloadedContactsVersion " + rowId
                + " version " + version + " id " + identityUri);
    }

    /**
     * Do NOT use this method if you wish to implement your own data store. This function is bound the default datastore implementation
     * 
     * @param cursor
     * @return
     */
    private OPIdentityContact contactFromCursor(Cursor cursor) {
        int identityUrlIndex = cursor
                .getColumnIndex(RolodexContactEntry.COLUMN_IDENTITY_URI);
        int identityProviderIndex = cursor
                .getColumnIndex(RolodexContactEntry.COLUMN_IDENTITY_PROVIDER);
        int nameIndex = cursor
                .getColumnIndex(RolodexContactEntry.COLUMN_CONTACT_NAME);
        int profileURLIndex = cursor
                .getColumnIndex(RolodexContactEntry.COLUMN_PROFILE_URL);
        int vprofileURLIndex = cursor
                .getColumnIndex(RolodexContactEntry.COLUMN_VPROFILE_URL);
        long rolodexId = cursor.getLong(cursor.getColumnIndex("rolodex_id"));

        OPIdentityContact contact = new OPIdentityContact(rolodexId,
                cursor.getString(identityUrlIndex),
                cursor.getString(identityProviderIndex),
                cursor.getString(nameIndex), cursor.getString(profileURLIndex),
                cursor.getString(vprofileURLIndex), null);
        List<OPAvatar> avatars = getAvatars(rolodexId);

        contact.setAvatars(avatars);
        contact.setIdentityParams(
                cursor.getString(cursor
                        .getColumnIndex(OpenpeerContactEntry.COLUMN_STABLE_ID)),
                cursor.getString(cursor
                        .getColumnIndex(OpenpeerContactEntry.COLUMN_PEERFILE_PUBLIC)),
                cursor.getString(cursor
                        .getColumnIndex(IdentityContactEntry.COLUMN_IDENTITY_PROOF_BUNDLE)),
                cursor.getInt(cursor
                        .getColumnIndex(IdentityContactEntry.COLUMN_PRORITY)),
                cursor.getInt(cursor
                        .getColumnIndex(IdentityContactEntry.COLUMN_WEIGHT)),
                cursor.getLong(cursor
                        .getColumnIndex(IdentityContactEntry.COLUMN_LAST_UPDATE_TIME)),
                cursor.getLong(cursor
                        .getColumnIndex(IdentityContactEntry.COLUMN_EXPIRE)));

        return contact;
    }

    private Uri notifyMessageChanged(String contextId, long cbcId,
            long id) {

        StringBuilder sb = new StringBuilder();
        switch (OPSdkConfig.getInstance().getGroupChatMode()) {
        case ContactsBased:

            sb.append(MessageEntry.URI_PATH_WINDOW_ID_URI_BASE + cbcId);
            mContext.getContentResolver()
                    .notifyChange(
                            OPContentProvider
                                    .getContentUri(WindowViewEntry.URI_PATH_INFO_CBC),
                            null);
            break;
        case ContextBased:
            sb.append(MessageEntry.URI_PATH_INFO_CONTEXT_URI_BASE + contextId);

            break;
        default:
            break;
        }

        if (id != 0) {
            sb.append("/" + id);
        }

        Uri uri = OPContentProvider.getContentUri(sb.toString());
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    /**
     * Put the user object in cache so we have to query db later on.
     * 
     * @param user
     */
    private void cacheUser(OPUser user) {
        mUsers.put(user.getPeerUri(), user);
        mUsersById.put(user.getUserId(), user);
    }

    private void saveParticipants(long windowId, List<OPUser> userList) {
        SQLiteDatabase db = getWritableDB();
        long id = DbUtils.simpleQueryForId(db, ParticipantEntry.TABLE_NAME,
                ParticipantEntry.COLUMN_CBC_ID + "=" + windowId, null);
        if (id != 0) {
            OPLogger.debug(OPLogLevel.LogLevel_Debug, "saveParticipants existed" + windowId);
            return;
        }
        // now insert the participants
        ContentValues contentValues[] = new ContentValues[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            OPUser user = userList.get(i);
            contentValues[i] = new ContentValues();
            contentValues[i].put(ParticipantEntry.COLUMN_CBC_ID, windowId);
            contentValues[i].put(
                    ParticipantEntry.COLUMN_CONTACT_ID,
                    user.getUserId());
        }
        int count = mContext
                .getContentResolver()
                .bulkInsert(
                        OPContentProvider.getContentUri(ParticipantEntry.URI_PATH_INFO),
                        contentValues);
        Log.d("test", "Inserted window participants " + count + " values "
                + Arrays.deepToString(contentValues));

        mContext.getContentResolver()
                .notifyChange(
                        OPContentProvider
                                .getContentUri(WindowViewEntry.URI_PATH_INFO_CBC),
                        null);
    }

    private boolean saveOrUpdateIdentities(List<OPIdentity> identities,
            long accountId, long opId) {
        for (OPIdentity identity : identities) {
            OPIdentityContact iContact = identity.getSelfIdentityContact();
            long identityContactId = saveIdentityContactTable(iContact);
            long rolodexId = saveRolodexContactTable(iContact, opId,
                    identityContactId, 0);
            saveIdentityTable(identity, accountId, rolodexId);
        }
        return true;
    }

    private void deleteById(SQLiteDatabase db, String tableName, long id) {
        db.delete(tableName, BaseColumns._ID + "=" + id, null);
    }

    private Cursor simpleQuery(SQLiteDatabase db, String tableName,
            String[] columns, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return db.query(tableName, columns, selection, selectionArgs, null,
                null, null);
    }

    private String simpleQueryForString(SQLiteDatabase db, String tableName,
            String column,
            String where, String[] args) {
        String value = null;
        Cursor cursor = simpleQuery(db, tableName, new String[] { column },
                where, args);
        if (cursor == null) {

        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            value = cursor.getString(0);
        }
        cursor.close();
        return value;
    }

    private long saveOPContactTable(String stableId, String peerUri,
            String peerfile) {
        ContentValues values = new ContentValues();
        values.put(OpenpeerContactEntry.COLUMN_STABLE_ID, stableId);
        values.put(OpenpeerContactEntry.COLUMN_PEERURI, peerUri);
        values.put(OpenpeerContactEntry.COLUMN_PEERFILE_PUBLIC, peerfile);
        return getWritableDB().insert(OpenpeerContactEntry.TABLE_NAME, null,
                values);
    }

    /**
     * @param avatar
     * @param rolodexId
     */
    private long saveAvatarTable(OPAvatar avatar, long rolodexId) {
        ContentValues values = new ContentValues();
        values.put(AvatarEntry.COLUMN_ROLODEX_ID, rolodexId);
        values.put(AvatarEntry.COLUMN_AVATAR_NAME, avatar.getName());
        values.put(AvatarEntry.COLUMN_AVATAR_URI, avatar.getURL());
        values.put(AvatarEntry.COLUMN_HEIGHT, avatar.getHeight());
        values.put(AvatarEntry.COLUMN_WIDTH, avatar.getWidth());
        return getWritableDB().insert(AvatarEntry.TABLE_NAME, null,
                values);
    }

    private long saveIdentityProviderTable(String providerDomain) {
        ContentValues values = new ContentValues();
        values.put(IdentityProviderEntry.COLUMN_DOMAIN, providerDomain);
        return getWritableDB().insert(IdentityProviderEntry.TABLE_NAME, null,
                values);
    }

    private void updateOPTable(long id, String stableId, String peerUri,
            String peerfile) {
        ContentValues values = new ContentValues();
        values.put(OpenpeerContactEntry.COLUMN_STABLE_ID, stableId);
        values.put(OpenpeerContactEntry.COLUMN_PEERURI, peerUri);
        values.put(OpenpeerContactEntry.COLUMN_PEERFILE_PUBLIC,
                peerfile);
        getWritableDB().update(OpenpeerContactEntry.TABLE_NAME,
                values, BaseColumns._ID + "=" + id,
                null);
    }

    private long insertIfNonExist(SQLiteDatabase db, String table,
            ContentValues values, String where, String params[]) {
        long id = DbUtils.simpleQueryForId(db, table, where, params);
        if (id == 0) {
            return db.insert(table, null, values);
        }
        return -1l;
    }

    private long saveIdentityContact(SQLiteDatabase db,
            OPIdentityContact contact, long associatedIdentityId) {

        String where = RolodexContactEntry.COLUMN_IDENTITY_URI + "=?";
        String args[] = new String[] { contact.getIdentityURI() };
        String columns[] = new String[] {
                BaseColumns._ID,
                RolodexContactEntry.COLUMN_OPENPEER_CONTACT_ID,
                RolodexContactEntry.COLUMN_IDENTITY_CONTACT_ID };
        Cursor cursor = db.query(RolodexContactEntry.TABLE_NAME,
                columns, where, args, null, null, null);
        if (cursor == null) {
        }
        if (cursor.getCount() > 0) {
            ContentValues values = new ContentValues();

            cursor.moveToFirst();

            long rolodexId = cursor.getLong(0);
            long opId = cursor.getLong(1);
            long identityContactId = cursor.getLong(2);
            cursor.close();

            if (identityContactId == 0) {
                identityContactId = saveIdentityContactTable(contact);
                values.put(
                        RolodexContactEntry.COLUMN_IDENTITY_CONTACT_ID,
                        identityContactId);
            } else {
                // TODO: update
            }
            // Insert openpeer_contact table entry
            if (opId == 0) {
                // now update rolodex_contact.identity_contact_id
                String peerfile = contact.getPeerFilePublic()
                        .getPeerFileString();
                OPContact opContact = OPContact
                        .createFromPeerFilePublic(OPDataManager
                                .getInstance().getSharedAccount(),
                                peerfile);
                String peerUri = opContact.getPeerURI();

                opId = saveOPContactTable(contact.getStableID(),
                        peerUri, peerfile);
                values.put(
                        RolodexContactEntry.COLUMN_OPENPEER_CONTACT_ID,
                        opId);
            } else {
                // TODO: update

            }
            if (values.size() > 0) {
                int count = db.update(RolodexContactEntry.TABLE_NAME,
                        values,
                        RolodexContactEntry._ID + "=" + rolodexId,
                        null);
            }

            mContext.getContentResolver()
                    .notifyChange(
                            OPContentProvider
                                    .getContentUri(RolodexContactEntry.URI_PATH_INFO),
                            null);

            return identityContactId;

        }
        return 0;

    }

    private boolean saveOrUpdateIdentity(OPIdentity identity, long accountId) {
        SQLiteDatabase db = getWritableDB();

        long identityId = DbUtils.simpleQueryForId(db,
                DatabaseContracts.AssociatedIdentityEntry.TABLE_NAME,
                AssociatedIdentityEntry.COLUMN_IDENTITY_URI + "=?",
                new String[] { identity.getIdentityURI() });
        if (identityId == 0) {
            long opId = 0;

            OPIdentityContact iContact = identity.getSelfIdentityContact();
            long identityContactId = saveIdentityContactTable(iContact);
            long rolodexId = saveRolodexContactTable(iContact, opId,
                    identityContactId, 0);
            saveIdentityTable(identity, accountId, rolodexId);
        } else if (accountId != 0) {
            ContentValues values = new ContentValues();
            values.put(AssociatedIdentityEntry.COLUMN_ACCOUNT_ID, accountId);
            db.update(AssociatedIdentityEntry.TABLE_NAME, values,
                    BaseColumns._ID + "=" + identityId, null);
        }
        return true;
    }

    private boolean flushContactsForIdentity(long id) {
        String selection = IdentityContactEntry.COLUMN_ASSOCIATED_IDENTITY_ID
                + "=" + id;
        String cSelection = RolodexContactEntry.COLUMN_ASSOCIATED_IDENTITY_ID
                + "=" + id;
        // mOpenHelper.getWritableDatabase().
        delete(IdentityContactEntry.TABLE_NAME, selection, null);
        delete(RolodexContactEntry.TABLE_NAME, cSelection, null);
        return true;
    }

    private boolean deleteContact(String identityUri) {
        SQLiteDatabase db = getWritableDB();
        try {
            db.beginTransaction();
            String fields[] = new String[] { RolodexContactEntry._ID,
                    RolodexContactEntry.COLUMN_OPENPEER_CONTACT_ID,
                    RolodexContactEntry.COLUMN_IDENTITY_CONTACT_ID };
            String where = RolodexContactEntry.COLUMN_IDENTITY_URI + "=?";
            String whereArgs[] = new String[] { identityUri };
            Cursor cursor = db.query(RolodexContactEntry.TABLE_NAME, fields,
                    where,
                    whereArgs, null, null, null);
            if (cursor == null) {

            }
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                long rolodexId = cursor.getLong(0);
                long opId = cursor.getLong(1);
                if (opId > 0) {
                    OPUser user = mUsersById.get(opId);
                    if (user != null) {
                        mUsersById.remove(opId);
                        mUsers.remove(user.getPeerUri());
                    }
                }
                long identityContactId = cursor.getLong(2);
                if (identityContactId > 0) {
                    deleteById(db, IdentityContactEntry.TABLE_NAME,
                            identityContactId);
                }
                // TODO: delete openpeer_contact entry if no more rolodex left
                // delete avatars and rolodex contact entry
                db.delete(AvatarEntry.TABLE_NAME, AvatarEntry.COLUMN_ROLODEX_ID
                        + "=" + rolodexId, null);
                deleteById(db, RolodexContactEntry.TABLE_NAME, rolodexId);
            }
            db.setTransactionSuccessful();
        } catch (SQLiteException ex) {

        } finally {
            db.endTransaction();
        }

        return true;
    }

    private int delete(String tableName, String whereClause, String[] whereArgs) {
        return mContext.getContentResolver().delete(
                OPContentProvider.getContentUri("/" + tableName), whereClause,
                whereArgs);

    }

    private Uri insert(String tableName, ContentValues values) {
        return mContext.getContentResolver().insert(
                OPContentProvider.getContentUri("/" + tableName), values);

    }

    private int update(String tableName, ContentValues values, String whereClause,
            String[] whereArgs) {
        Uri uri = OPContentProvider.getContentUri("/" + tableName);
        return mContext.getContentResolver().update(uri, values, whereClause,
                whereArgs);

    }

    private Cursor query(String tableName, String columns[], String whereClause,
            String[] whereArgs) {
        Uri uri = OPContentProvider.getContentUri("/" + tableName);

        return mContext.getContentResolver().query(uri, columns, whereClause,
                whereArgs, null);
    }

    private boolean upsert(String tableName, ContentValues values, String whereClause,
            String[] whereArgs) {
        boolean result;
        Uri uri = OPContentProvider.getContentUri("/" + tableName);
        Cursor cursor = mContext.getContentResolver().query(uri, null,
                whereClause, whereArgs, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {

                int updatedCount = mContext.getContentResolver().update(uri,
                        values, whereClause, whereArgs);
                result = (updatedCount > 0);
            }
            cursor.close();
        }

        Uri _uri = mContext.getContentResolver().insert(uri, values);
        result = (_uri != null);
        return result;

    }
}
