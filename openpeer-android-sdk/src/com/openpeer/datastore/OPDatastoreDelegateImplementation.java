package com.openpeer.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.format.Time;
import android.util.Log;

import static com.openpeer.datastore.DatabaseContracts.*;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.javaapi.OPRolodexContact.OPAvatar;
import com.openpeer.model.OPHomeUser;
import com.openpeer.app.OPSession;

/**
 * The data being stored in preference: -- Relogin information for account --
 * stableId
 * 
 * The data being stored in database Contacts Conversation history Call history
 * 
 */
public class OPDatastoreDelegateImplementation implements OPDatastoreDelegate {
	private static final String PREF_DATASTORE = "model_data";
	private static final String PREF_KEY_RELOGIN_INFO = "relogin_info";
	private static final String PREF_KEY_HOMEUSER_STABLEID = "homeuser_stable_id";

	private static OPDatastoreDelegateImplementation instance;
	OPDatabaseHelper mOpenHelper;
	private SQLiteDatabase mDatabase;

	private SharedPreferences mPreferenceStore;
	Context mContext;

	private OPDatastoreDelegateImplementation() {

	}

	private OPDatastoreDelegateImplementation(Context context) {
		mContext = context;
		mOpenHelper = new OPDatabaseHelper(context,
				OPDatabaseHelper.DATABASE_NAME, // the name of the database)
				null, // uses the default SQLite cursor
				OPDatabaseHelper.DATABASE_VERSION);
		mDatabase = mOpenHelper.getWritableDatabase();

		mPreferenceStore = context.getSharedPreferences(PREF_DATASTORE,
				Context.MODE_PRIVATE);
	}

	public static OPDatastoreDelegateImplementation getInstance() {
		if (instance == null) {
			instance = new OPDatastoreDelegateImplementation();
		}
		return instance;
	}

	public OPDatastoreDelegateImplementation init(Context context) {
		mContext = context;

		mOpenHelper = new OPDatabaseHelper(context,
				OPDatabaseHelper.DATABASE_NAME, // the name of the database)
				null, // uses the default SQLite cursor
				OPDatabaseHelper.DATABASE_VERSION);
		mDatabase = mOpenHelper.getWritableDatabase();

		mPreferenceStore = context.getSharedPreferences(PREF_DATASTORE,
				Context.MODE_PRIVATE);
		return this;// fluent API

	}

	@Override
	public String getReloginInfo() {
		return mPreferenceStore.getString(PREF_KEY_RELOGIN_INFO, null);
	}

	@Override
	public Hashtable<Long, OPIdentityContact> getSelfIdentityContacts() {
		String columns[] = new String[] {
				IdentityEntry.COLUMN_NAME_IDENTITY_ID,
				IdentityEntry.COLUMN_NAME_IDENTITY_CONTACT_ID };
		Cursor cursor = mOpenHelper.getWritableDatabase()
				.query(IdentityEntry.TABLE_NAME, columns, null, null, null,
						null, null);
		if (cursor != null) {
			Hashtable<Long, OPIdentityContact> contacts = new Hashtable<Long, OPIdentityContact>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				contacts.put(cursor.getLong(0),
						getIdentityContact(cursor.getString(1)));
				cursor.moveToNext();
			}
			cursor.close();
			Log.d("test",
					"getSelfIdentityContacts "
							+ Arrays.deepToString(contacts.values().toArray()));
			return contacts;
		}
		return null;
	}

	@Override
	public List<OPIdentity> getIdentities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OPIdentity getIdentity() {
		return null;
	}

	@Override
	public List<OPRolodexContact> getContacts(long identityId) {
		List<OPRolodexContact> contacts = new ArrayList<OPRolodexContact>();
		String selection = null;
		if (identityId != 0) {
			selection = ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + "="
					+ identityId;
		}
		Cursor cursor = mOpenHelper.getWritableDatabase().query(
				ContactEntry.TABLE_NAME, null, selection, null, null, null,
				null);
		if (cursor != null) {

			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				contacts.add(contactFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}

		return contacts;
	}

	@Override
	public boolean saveOrUpdateAccount(OPAccount account) {
		Log.d("test",
				"DatastoreDelegate saving account id " + account.getStableID()
						+ " relogin " + account.getReloginInformation());
		SharedPreferences.Editor editor = mPreferenceStore.edit();
		editor.putString(PREF_KEY_RELOGIN_INFO, account.getReloginInformation());
		editor.putLong(PREF_KEY_HOMEUSER_STABLEID, account.getStableID());
		editor.apply();
		return true;
	}

	@Override
	public boolean saveOrUpdateIdentities(List<OPIdentity> identities,
			long accountId) {
		for (OPIdentity identity : identities) {
			saveOrUpdateIdentity(identity, accountId);
		}

		return true;
	}

	@Override
	public boolean saveOrUpdateContacts(
			List<? extends OPRolodexContact> contacts, long identityId) {
		for (OPRolodexContact contact : contacts) {
			Log.d("OPDatastoreDelegateImplementation", "saveOrUpdateContacts "
					+ contact + " identityId " + identityId);
			saveOrUpdateContact(contact, identityId);
		}
		mContext.getContentResolver().notifyChange(DatabaseContracts.ContactsViewEntry.CONTENT_URI, null);
		return true;
	}

	@Override
	public boolean saveOrUpdateIdentity(OPIdentity identity, long accountId) {
		OPIdentityContact selfContact = identity.getSelfIdentityContact();
		ContentValues values = new ContentValues();
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_ID,
				identity.getStableID());
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_PROVIDER,
				identity.getIdentityProviderDomain());
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_URI,
				identity.getIdentityURI());
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_CONTACT_ID,
				selfContact.getStableID());
		long rowId = mOpenHelper.getWritableDatabase().insertWithOnConflict(
				IdentityEntry.TABLE_NAME, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		saveOrUpdateContact(identity.getSelfIdentityContact(),
				identity.getStableID());

		return rowId != 0;
	}

	@Override
	public boolean saveOrUpdateContact(OPRolodexContact contact, long identityId) {
		ContentValues values = new ContentValues();
		values.put(ContactEntry.COLUMN_NAME_CONTACT_ID, contact.getId());
		if (contact instanceof OPIdentityContact) {
			values.put(ContactsViewEntry.COLUMN_NAME_STABLE_ID,
					((OPIdentityContact) contact).getStableID());
		}
		values.put(ContactEntry.COLUMN_NAME_CONTACT_NAME, contact.getName());
		values.put(ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID, identityId);
		values.put(ContactEntry.COLUMN_NAME_IDENTITY_URI,
				contact.getIdentityURI());
		values.put(ContactEntry.COLUMN_NAME_IDENTITY_PROVIDER,
				contact.getIdentityProvider());
		values.put(ContactEntry.COLUMN_NAME_URL, contact.getProfileURL());
		values.put(ContactEntry.COLUMN_NAME_VPROFILE_URL,
				contact.getVProfileURL());

		boolean result = upsert(
				DatabaseContracts.ContactEntry.TABLE_NAME,
				values,
				ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + "="
						+ identityId + " and "
						+ ContactEntry.COLUMN_NAME_CONTACT_ID + "="
						+ contact.getId(), null);

		if (result && contact.getAvatars() != null) {
			// insert or update avatar
			for (OPAvatar avatar : contact.getAvatars()) {
				ContentValues avatarValues = new ContentValues();
				avatarValues.put(AvatarEntry.COLUMN_NAME_CONTACT_ID,
						contact.getId());
				avatarValues.put(AvatarEntry.COLUMN_NAME_AVATAR_NAME,
						avatar.getName());
				avatarValues.put(AvatarEntry.COLUMN_NAME_AVATAR_URL,
						avatar.getURL());
				long avatarRowId = mOpenHelper.getWritableDatabase()
						.insertWithOnConflict(AvatarEntry.TABLE_NAME, null,
								avatarValues, SQLiteDatabase.CONFLICT_REPLACE);
			}
		}
		if (contact instanceof OPIdentityContact) {
			OPIdentityContact ic = (OPIdentityContact) contact;
			ContentValues icValues = new ContentValues();

			icValues.put(IdentityContactEntry.COLUMN_NAME_STABLE_ID,
					ic.getStableID());
			icValues.put(
					IdentityContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID,
					identityId);
			icValues.put(IdentityContactEntry.COLUMN_NAME_PEERFILE_PUBLIC, ic
					.getPeerFilePublic().getPeerFileString());

			icValues.put(
					IdentityContactEntry.COLUMN_NAME_IDENTITY_PROOF_BUNDLE,
					ic.getIdentityProofBundle());
			icValues.put(IdentityContactEntry.COLUMN_NAME_PRORITY,
					ic.getPriority());
			icValues.put(IdentityContactEntry.COLUMN_NAME_LAST_UPDATE_TIME, ic
					.getLastUpdated().toMillis(false));
			icValues.put(IdentityContactEntry.COLUMN_NAME_EXPIRE, ic
					.getExpires().toMillis(false));
			return upsert(
					IdentityContactEntry.TABLE_NAME,
					icValues,
					IdentityContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID
							+ "=" + identityId + " and "
							+ IdentityContactEntry.COLUMN_NAME_STABLE_ID + "=?",
					new String[] { ((OPIdentityContact) contact).getStableID() });

		} else {
			return true;
		}

	}

	@Override
	public boolean deleteIdentity(long id) {

		mOpenHelper.getWritableDatabase().delete(IdentityEntry.TABLE_NAME,
				IdentityEntry.COLUMN_NAME_IDENTITY_ID + "=" + id, null);
		mOpenHelper.getWritableDatabase().delete(
				IdentityContactEntry.TABLE_NAME,
				IdentityContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + "="
						+ id, null);
		mOpenHelper.getWritableDatabase().delete(ContactEntry.TABLE_NAME,
				ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + "=" + id,
				null);
		return false;
	}

	@Override
	public boolean flushContactsForIdentity(long id) {
		String selection = IdentityContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID
				+ "=" + id;
		String cSelection = ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID
				+ "=" + id;
		mOpenHelper.getWritableDatabase().delete(
				IdentityContactEntry.TABLE_NAME, selection, null);
		mOpenHelper.getWritableDatabase().delete(ContactEntry.TABLE_NAME,
				cSelection, null);
		return true;
	}

	@Override
	public boolean deleteContact(long id) {
		String rawSql = "delete from " + IdentityContactEntry.TABLE_NAME
				+ " where " + IdentityContactEntry.COLUMN_NAME_STABLE_ID
				+ " in (select " + ContactsViewEntry.COLUMN_NAME_STABLE_ID
				+ " from " + ContactEntry.TABLE_NAME + " where "
				+ ContactEntry.COLUMN_NAME_CONTACT_ID + "=" + id + ")";
		mOpenHelper.getWritableDatabase().execSQL(rawSql);
		mOpenHelper.getWritableDatabase().delete(ContactEntry.TABLE_NAME,
				ContactEntry.COLUMN_NAME_CONTACT_ID + "=" + id, null);
		// if (contact instanceof OPIdentityContact) {
		// mOpenHelper.getWritableDatabase()
		// .delete(IdentityContactEntry.TABLE_NAME,
		// IdentityContactEntry.COLUMN_NAME_STABLE_ID
		// + "="
		// + ((OPIdentityContact) contact)
		// .getStableID(), null);
		// }
		return true;
	}

	@Override
	public OPHomeUser getHomeUser() {
		String reloginInfo = this.mPreferenceStore.getString(
				PREF_KEY_RELOGIN_INFO, null);
		if (reloginInfo != null) {
			return new OPHomeUser(reloginInfo, mPreferenceStore.getLong(
					PREF_KEY_HOMEUSER_STABLEID, 0L));
		}
		return null;
	}

	@Override
	public boolean saveHomeUser(OPHomeUser user) {
		SharedPreferences.Editor editor = mPreferenceStore.edit();
		editor.putString(PREF_KEY_RELOGIN_INFO, user.getReloginInfo());
		editor.putLong(PREF_KEY_HOMEUSER_STABLEID, user.getStableId());
		return true;
	}

	public String getDownloadedContactsVersion(long identityId) {
		String version = null;
		Cursor cursor = mOpenHelper
				.getWritableDatabase()
				.query(IdentityEntry.TABLE_NAME,
						new String[] { IdentityEntry.COLUMN_NAME_IDENTITY_CONTACTS_VERSION },
						IdentityEntry.COLUMN_NAME_IDENTITY_ID + "="
								+ identityId, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			version = cursor
					.getString(cursor
							.getColumnIndex(IdentityEntry.COLUMN_NAME_IDENTITY_CONTACTS_VERSION));
		}
		Log.d("test", "datastore contacts version " + version);
		return version;
	}

	@Override
	public List<OPRolodexContact> getOPContacts(String identityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDownloadedContactsVersion(long identityId, String version) {
		ContentValues values = new ContentValues();
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_CONTACTS_VERSION, version);
		String whereClause = IdentityEntry.COLUMN_NAME_IDENTITY_ID + "="
				+ identityId;
		long rowId = mOpenHelper.getWritableDatabase().update(
				IdentityEntry.TABLE_NAME, values, whereClause, null);
		Log.d("test", "setDownloadedContactsVersion " + rowId + " version "
				+ version + " id " + identityId);
	}

	@Override
	public OPIdentityContact getIdentityContact(String identityContactId) {
		String iSelection = IdentityContactEntry.COLUMN_NAME_STABLE_ID + "=?";
		String cSelection = ContactsViewEntry.COLUMN_NAME_STABLE_ID + "=?";
		String[] selectionParams = new String[] { identityContactId };

		Cursor iCursor = mOpenHelper.getWritableDatabase().query(
				IdentityContactEntry.TABLE_NAME, null, iSelection,
				selectionParams, null, null, null);
		Cursor cCursor = mOpenHelper.getWritableDatabase().query(
				ContactEntry.TABLE_NAME, null, cSelection, selectionParams,
				null, null, null);
		if (cCursor != null && iCursor != null) {
			cCursor.moveToFirst();
			iCursor.moveToFirst();
			return identityContactFromCursor(contactFromCursor(cCursor),
					iCursor);
		}
		return null;

	}

	private OPRolodexContact contactFromCursor(Cursor cursor) {
		int identityUrlIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_IDENTITY_URI);
		int identityProviderIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_IDENTITY_PROVIDER);
		int nameIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_CONTACT_NAME);
		int profileURLIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_URL);
		int vprofileURLIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_VPROFILE_URL);
		int assoiatedIdentityIdIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID);

		OPRolodexContact contact = new OPRolodexContact(
				cursor.getString(identityUrlIndex),
				cursor.getString(identityProviderIndex),
				cursor.getString(nameIndex), cursor.getString(profileURLIndex),
				cursor.getString(vprofileURLIndex), null,
				cursor.getLong(assoiatedIdentityIdIndex));

		contact.setAvatars(this.getAvatars(contact.getId()));
		String stableIdentityId = cursor.getString(cursor
				.getColumnIndex(ContactsViewEntry.COLUMN_NAME_STABLE_ID));
		if (stableIdentityId != null) {
			Log.d("test", "retrieving identity contact of id "
					+ stableIdentityId);
			Cursor iCursor = mOpenHelper.getWritableDatabase().query(
					IdentityContactEntry.TABLE_NAME, null,
					IdentityContactEntry.COLUMN_NAME_STABLE_ID + "=?",
					new String[] { stableIdentityId }, null, null, null);
			if (iCursor != null) {
				iCursor.moveToFirst();
				contact = identityContactFromCursor(contact, iCursor);
				iCursor.close();
			}
		}
		return contact;
	}

	private OPIdentityContact identityContactFromCursor(
			OPRolodexContact contact, Cursor iCursor) {
		OPIdentityContact iContact = new OPIdentityContact(contact);
		return iContact
				.setIdentityParams(
						iCursor.getString(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_STABLE_ID)),
						iCursor.getString(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_PEERFILE_PUBLIC)),
						iCursor.getString(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_IDENTITY_PROOF_BUNDLE)),
						iCursor.getInt(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_PRORITY)),
						iCursor.getInt(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_WEIGHT)),
						iCursor.getLong(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_LAST_UPDATE_TIME)),
						iCursor.getLong(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_EXPIRE)),
						iCursor.getLong(0));
	}

	private List<OPAvatar> getAvatars(long contactId) {
		Cursor cursor = mOpenHelper.getWritableDatabase().query(
				AvatarEntry.TABLE_NAME, null,
				AvatarEntry.COLUMN_NAME_CONTACT_ID + "=" + contactId, null,
				null, null, null);
		if (cursor != null) {
			List<OPAvatar> avatars = new ArrayList<OPAvatar>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				OPAvatar avatar = new OPAvatar(
						cursor.getString(cursor
								.getColumnIndex(AvatarEntry.COLUMN_NAME_AVATAR_NAME)),
						cursor.getString(cursor
								.getColumnIndex(AvatarEntry.COLUMN_NAME_AVATAR_URL)),
						0, 0);
				avatars.add(avatar);
				cursor.moveToNext();
			}
			cursor.close();
			return avatars;
		}
		return null;
	}

	@Override
	public boolean saveSession(OPSession session) {
		Log.d("TODO", "OPDatastoreDelegate saveSession " + session);
		ContentValues values = new ContentValues();
		values.put(ConversationWindowEntry.COLUMN_NAME_WINDOW_ID,
				session.getCurrentWindowId());
		values.put(ConversationWindowEntry.COLUMN_NAME_LAST_READ_MSG_ID,
				session.getReadMessageId());
		long rowId = mOpenHelper.getWritableDatabase().insertWithOnConflict(
				ConversationWindowEntry.COLUMN_NAME_WINDOW_ID, null, values,
				SQLiteDatabase.CONFLICT_ABORT);
		return rowId != 0;
	}

	@Override
	public List<OPSession> getRecentSessions() {
//		Cursor sessionCursor = mOpenHelper.getWritableDatabase().query(
//				ConversationWindowEntry.TABLE_NAME,
//				new String[] { ConversationWindowEntry.COLUMN_NAME_WINDOW_ID,
//						ConversationWindowEntry.COLUMN_NAME_LAST_READ_MSG_ID }, null,
//				null, null, null, null);
//		if (sessionCursor != null) {
//			List<OPSession> sessions = new ArrayList<OPSession>();
//			sessionCursor.moveToFirst();
//			int sessionIdIndex = sessionCursor
//					.getColumnIndex(ConversationWindowEntry.COLUMN_NAME_WINDOW_ID);
//			int lastReadMsgIndex = sessionCursor
//					.getColumnIndex(ConversationWindowEntry.COLUMN_NAME_LAST_READ_MSG_ID);
//
//			while (!sessionCursor.isAfterLast()) {
//				OPSession session = new OPSession();
//				long sessionId = sessionCursor.getLong(sessionIdIndex);
//				Cursor participantCursor = mOpenHelper
//						.getWritableDatabase()
//						.query(WindowParticipantEntry.TABLE_NAME,
//								new String[] { WindowParticipantEntry.COLUMN_NAME_IDENTITY_ID },
//								WindowParticipantEntry.COLUMN_NAME_WINDOW_ID
//										+ "=" + sessionId, null, null, null,
//								null);
//				if (participantCursor != null) {
//					List<OPIdentityContact> contacts = new ArrayList<OPIdentityContact>();
//					participantCursor.moveToFirst();
//					while (!participantCursor.isAfterLast()) {
//						String id = participantCursor
//								.getString(participantCursor
//										.getColumnIndex(WindowParticipantEntry.COLUMN_NAME_IDENTITY_ID));
//						OPIdentityContact contact = this.getIdentityContact(id);
//						contacts.add(contact);
//					}
//					participantCursor.close();
//				}
//				session.setLastMessage(getLastMessageForSession(sessionId));
//				sessions.add(session);
//			}
//			sessionCursor.close();
//			return sessions;
//		}
		return null;
	}

	public OPMessage getLastMessageForSession(long sessionId) {
		Cursor cursor = mOpenHelper.getWritableDatabase().query(
				MessageEntry.TABLE_NAME, null,
				MessageEntry.COLUMN_NAME_WINDOW_ID + "=" + sessionId, null,
				null, null, MessageEntry._ID, "1");

		if (cursor != null) {
			cursor.moveToFirst();
			int senderIdIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_SENDER_ID);
			int messageTypeIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TYPE);
			int timeIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TIME);
			int textIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TEXT);
			int messageIdIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_ID);
			OPMessage message = new OPMessage(cursor.getString(senderIdIndex),
					cursor.getString(messageTypeIndex),
					cursor.getString(textIndex), cursor.getLong(timeIndex));
			message.setMessageId(cursor.getString(messageIdIndex));
			cursor.close();
			return message;
		}
		return null;
	}

	@Override
	public boolean saveMessage(OPMessage message, long sessionId, String threadId) {
		Log.d("TODO", "OPDatastoreDelegate saveMessage " + message.getMessage()
				+ " sessionId " + sessionId);
		ContentValues values = new ContentValues();
		values.put(MessageEntry.COLUMN_NAME_MESSAGE_ID, message.getMessageId());
		values.put(MessageEntry.COLUMN_NAME_MESSAGE_TEXT, message.getMessage());
		values.put(MessageEntry.COLUMN_NAME_MESSAGE_TYPE,
				message.getMessageType());
		values.put(MessageEntry.COLUMN_NAME_SENDER_ID, message.getSenderId());
		values.put(MessageEntry.COLUMN_NAME_WINDOW_ID, sessionId);

		long rowId = mOpenHelper.getWritableDatabase().insert(
				MessageEntry.TABLE_NAME, null, values);
		return rowId != 0;
	}

	/**
	 * Get all private messages with the contact. This function retrieves all
	 * private sessions with the contact and messages associated
	 * 
	 * @param contactId
	 *            stableId of the OPContact(or OPIdentityContact?)
	 * @return
	 */
	@Override
	public List<OPMessage> getMessagesWithSession(long sessionId, int max,
			String lastMessageId) {
		Log.d("TODO", "OPDatastoreDelegate getMessagesForContact " + sessionId);
		Cursor cursor = mOpenHelper.getWritableDatabase().query(
				MessageEntry.TABLE_NAME, null,
				MessageEntry.COLUMN_NAME_WINDOW_ID + "=" + sessionId, null,
				null, null, null);
		if (cursor != null) {
			List<OPMessage> messages = new ArrayList<OPMessage>();

			cursor.moveToFirst();
			int senderIdIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_SENDER_ID);
			int messageTypeIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TYPE);
			int timeIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TIME);
			int textIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_TEXT);
			int messageIdIndex = cursor
					.getColumnIndex(MessageEntry.COLUMN_NAME_MESSAGE_ID);
			while (!cursor.isAfterLast()) {
				OPMessage message = new OPMessage(
						cursor.getString(senderIdIndex),
						cursor.getString(messageTypeIndex),
						cursor.getString(textIndex), cursor.getLong(timeIndex));
				message.setMessageId(cursor.getString(messageIdIndex));
				messages.add(message);
			}
			return messages;
		}
		return null;
	}

	/**
	 * Get all private messages with the contact. This function retrieves all
	 * private sessions with the contact and messages associated
	 * 
	 * @param contactId
	 *            stableId of the OPContact(or OPIdentityContact?)
	 * @return
	 */
	@Override
	public List<OPMessage> getMessagesWithContact(long contactId, int max,
			String lastMessageId) {
		Log.d("TODO", "OPDatastoreDelegate getMessagesForContact " + contactId);
		return null;
	}

	@Override
	public int getNumberofUnreadMessages(String contactId) {
		Log.d("TODO", "OPDatastoreDelegate getMessagesForContact " + contactId);
		return 5;
	}

	boolean upsert(String tableName, ContentValues values, String whereClause,
			String[] whereArgs) {
		int updated = mOpenHelper.getWritableDatabase().update(tableName,
				values, whereClause, whereArgs);
		if (updated == 0) {
			long rowId = mOpenHelper.getWritableDatabase().insert(tableName,
					null, values);
			return rowId != -1;
		} else {
			return true;
		}
	}

}
