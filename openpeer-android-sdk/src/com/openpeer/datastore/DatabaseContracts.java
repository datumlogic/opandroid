package com.openpeer.datastore;

import android.provider.BaseColumns;

/**
 * Created by brucexia on 2014-06-03.
 */
public class DatabaseContracts {
	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String INTEGER_PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY";
	private static final String UNIQUE_TYPE = " UNIQUE";
	private static final String PRIMARY_KEY_TYPE = " PRIMARY KEY";

	private static final String COMMA_SEP = ",";

	static String getCompositePrimaryKey(String[] columnNames) {
		StringBuilder stringBuilder = new StringBuilder("PRIMARY KEY (");
		for (int i = 0; i < columnNames.length; i++) {
			String columnName = columnNames[i];
			stringBuilder.append(columnName);
			if (i < columnNames.length - 1) {
				stringBuilder.append(COMMA_SEP);
			} else {
				stringBuilder.append(")");
			}
		}
		return stringBuilder.toString();
	}

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
		public static final String COLUMN_NAME_CONTACT_ID = "contact_id";
		public static final String COLUMN_NAME_ASSOCIATED_IDENTITY_ID = "associated_ientity_id";
		public static final String COLUMN_NAME_IDENTITY_CONTACT_ID = "identity_contact_id";
		public static final String COLUMN_NAME_IDENTITY_URI = "identity_uri";
		public static final String COLUMN_NAME_IDENTITY_PROVIDER = "identity_provider";

		public static final String COLUMN_NAME_CONTACT_NAME = "name";
		public static final String COLUMN_NAME_URL = "profile_url";
		public static final String COLUMN_NAME_VPROFILE_URL = "vprofile_url";
	}

	public static abstract class IdentityContactEntry implements BaseColumns {
		public static final String TABLE_NAME = "identity_contact";
		public static final String COLUMN_NAME_STABLE_ID = "stable_id";
		public static final String COLUMN_NAME_ASSOCIATED_IDENTITY_ID = "associated_identity_id";
		public static final String COLUMN_NAME_PEERFILE_PUBLIC = "peerfile_public";
		public static final String COLUMN_NAME_IDENTITY_PROOF_BUNDLE = "identity_proof_bundle";
		public static final String COLUMN_NAME_PRORITY = "priority";
		public static final String COLUMN_NAME_WEIGHT = "weight";
		public static final String COLUMN_NAME_LAST_UPDATE_TIME = "last_update_time";
		public static final String COLUMN_NAME_EXPIRE = "expire";
	}

	public static abstract class IdentityEntry implements BaseColumns {
		public static final String TABLE_NAME = "identity";
		public static final String COLUMN_NAME_IDENTITY_ID = "stable_id";
		public static final String COLUMN_NAME_IDENTITY_PROVIDER = "provider";
		public static final String COLUMN_NAME_IDENTITY_URI = "uri";
		// The "selfContact" of the identity
		public static final String COLUMN_NAME_IDENTITY_CONTACT_ID = "contact_id";
		public static final String COLUMN_NAME_IDENTITY_CONTACTS_VERSION = "contacts_version";

		// TODO add other columns
	}

	public static abstract class AvatarEntry implements BaseColumns {
		public static final String TABLE_NAME = "avatar";
		public static final String COLUMN_NAME_CONTACT_ID = "contact_id";

		public static final String COLUMN_NAME_AVATAR_NAME = "name";
		public static final String COLUMN_NAME_AVATAR_URL = "url";

		// TODO add other columns
	}

	public static abstract class ConversationEntry implements BaseColumns {
		public static final String TABLE_NAME = "conversation";
		// Group id -- not used now
		public static final String COLUMN_NAME_GROUP_ID = "group_id";
		// Group id -- not used now
		public static final String COLUMN_NAME_SESSION_ID = "session_id";
		public static final String COLUMN_NAME_LAST_READ_MSG_ID = "lrm_id";
		// Am I host of group chat, not used now
		public static final String COLUMN_NAME_THREAD_HOST = "is_host";
		public static final String COLUMN_NAME_START_TIME = "start_time";
		public static final String COLUMN_NAME_END_TIME = "end_time";

	}

	/**
	 * Record the state snapshot: when and what contact is added/deleted
	 * 
	 * 
	 */
	public static abstract class ConversationStateChangeEntry implements
			BaseColumns {
		public static final String TABLE_NAME = "conversation_state_change";
		public static final String COLUMN_NAME_THREAD_ID = "thread_id";
		public static final String COLUMN_NAME_STATE_TIME = "time";
	}

	/**
	 * state_type: ADD_CONTACT, REMOVE_CONTACT, CONTACT_QUIT, etc
	 * 
	 */
	public static abstract class ConversationStateEntry implements BaseColumns {
		public static final String TABLE_NAME = "conversation_state";
		public static final String COLUMN_NAME_state_change_ID = "state_change_id";
		public static final String COLUMN_NAME_STATE_TYPE = "state_type";
	}

	public static abstract class MessageEntry implements BaseColumns {
		public static final String TABLE_NAME = "message";
		public static final String COLUMN_NAME_MESSAGE_ID = "message_id";
		public static final String COLUMN_NAME_SESSION_ID = "session_id";
		// for now only text is supported
		public static final String COLUMN_NAME_MESSAGE_TYPE = "type";
		// id of peer contact. use your own id or "self" for outgoing message
		public static final String COLUMN_NAME_SENDER_ID = "sender_id";
		// Message content text
		public static final String COLUMN_NAME_MESSAGE_TEXT = "text";
		// sent or receive time
		public static final String COLUMN_NAME_MESSAGE_TIME = "time";
	}

	public static abstract class CallEntry implements BaseColumns {
		public static final String TABLE_NAME = "CALL";
		public static final String COLUMN_NAME_CALL_ID = "stable_id";
		public static final String COLUMN_NAME_CALL_PROVIDER = "provider";
		public static final String COLUMN_NAME_CALL_URI = "uri";
		// The "selfContact" of the CALL
		public static final String COLUMN_NAME_CALL_CONTACT_ID = "contact_id";
		public static final String COLUMN_NAME_CALL_CONTACTS_VERSION = "contacts_version";

		// TODO add other columns
	}

	public static abstract class ConversationParticipantEntry implements
			BaseColumns {
		public static final String TABLE_NAME = "session_participants";

		// The session id this pariticipant is associated with
		public static final String COLUMN_NAME_SESSION_ID = "session_id";

		public static final String COLUMN_NAME_IDENTITY_ID = "stable_id";
		// this is neccessary in case group chat with unknown participants
		public static final String COLUMN_NAME_IDENTITY_NAME = "name";
	}

	public static abstract class CallParticipantEntry implements BaseColumns {
		public static final String TABLE_NAME = "call_participants";
		public static final String COLUMN_NAME_CALL_ID = "call_id";
		public static final String COLUMN_NAME_IDENTITY_ID = "stable_id";
	}

	public static final String SQL_CREATE_CONTACT = "CREATE TABLE "
			+ ContactEntry.TABLE_NAME + " (" + ContactEntry._ID + INTEGER_TYPE
			+ COMMA_SEP + ContactEntry.COLUMN_NAME_CONTACT_ID
			+ INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + INTEGER_TYPE
			+ COMMA_SEP + ContactEntry.COLUMN_NAME_IDENTITY_CONTACT_ID
			+ TEXT_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ ContactEntry.COLUMN_NAME_CONTACT_NAME + TEXT_TYPE + COMMA_SEP
			+ ContactEntry.COLUMN_NAME_IDENTITY_PROVIDER + TEXT_TYPE
			+ COMMA_SEP + ContactEntry.COLUMN_NAME_IDENTITY_URI + TEXT_TYPE
			+ COMMA_SEP + ContactEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP
			+ ContactEntry.COLUMN_NAME_VPROFILE_URL + TEXT_TYPE + " )";
	public static final String SQL_CREATE_IDENTITY = "CREATE TABLE "
			+ IdentityEntry.TABLE_NAME + " (" + IdentityEntry._ID
			+ INTEGER_TYPE + COMMA_SEP + IdentityEntry.COLUMN_NAME_IDENTITY_ID
			+ INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ IdentityEntry.COLUMN_NAME_IDENTITY_PROVIDER + TEXT_TYPE
			+ COMMA_SEP + IdentityEntry.COLUMN_NAME_IDENTITY_URI + TEXT_TYPE
			+ COMMA_SEP + IdentityEntry.COLUMN_NAME_IDENTITY_CONTACT_ID
			+ INTEGER_TYPE + COMMA_SEP
			+ IdentityEntry.COLUMN_NAME_IDENTITY_CONTACTS_VERSION + TEXT_TYPE +
			// ... Any other options for the CREATE command
			" )";
	public static final String SQL_CREATE_AVATAR = "CREATE TABLE "
			+ AvatarEntry.TABLE_NAME
			+ " ("
			+ AvatarEntry._ID
			+ INTEGER_TYPE
			+ COMMA_SEP
			+ AvatarEntry.COLUMN_NAME_CONTACT_ID
			+ INTEGER_TYPE
			+ COMMA_SEP
			+ AvatarEntry.COLUMN_NAME_AVATAR_NAME
			+ TEXT_TYPE
			+ COMMA_SEP
			+ AvatarEntry.COLUMN_NAME_AVATAR_URL
			+ TEXT_TYPE
			+ COMMA_SEP
			+ getCompositePrimaryKey(new String[] {
					AvatarEntry.COLUMN_NAME_CONTACT_ID,
					AvatarEntry.COLUMN_NAME_AVATAR_URL }) + " )";
	public static final String SQL_CREATE_IDENTITY_CONTACT = "CREATE TABLE "
			+ IdentityContactEntry.TABLE_NAME + " (" + IdentityContactEntry._ID
			+ INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ IdentityContactEntry.COLUMN_NAME_STABLE_ID + TEXT_TYPE
			+ UNIQUE_TYPE + COMMA_SEP
			+ IdentityContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID
			+ INTEGER_TYPE + COMMA_SEP
			+ IdentityContactEntry.COLUMN_NAME_PEERFILE_PUBLIC + TEXT_TYPE
			+ COMMA_SEP
			+ IdentityContactEntry.COLUMN_NAME_IDENTITY_PROOF_BUNDLE
			+ TEXT_TYPE + COMMA_SEP + IdentityContactEntry.COLUMN_NAME_PRORITY
			+ INTEGER_TYPE + COMMA_SEP
			+ IdentityContactEntry.COLUMN_NAME_WEIGHT + INTEGER_TYPE
			+ COMMA_SEP + IdentityContactEntry.COLUMN_NAME_LAST_UPDATE_TIME
			+ INTEGER_TYPE + COMMA_SEP
			+ IdentityContactEntry.COLUMN_NAME_EXPIRE +
			// ... Any other options for the CREATE command
			" )";

	public static final String SQL_CREATE_SESSION = "CREATE TABLE "
			+ ConversationEntry.TABLE_NAME + " (" + ConversationEntry._ID
			+ INTEGER_TYPE + COMMA_SEP
			+ ConversationEntry.COLUMN_NAME_SESSION_ID
			+ INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ ConversationEntry.COLUMN_NAME_GROUP_ID + TEXT_TYPE + " )";
	public static final String SQL_CREATE_CONVERSATION_PARTICIPANT = "CREATE TABLE "
			+ ConversationParticipantEntry.TABLE_NAME
			+ " ("
			+ ConversationParticipantEntry._ID
			+ INTEGER_TYPE
			+ COMMA_SEP
			+ ConversationParticipantEntry.COLUMN_NAME_SESSION_ID
			+ INTEGER_TYPE
			+ COMMA_SEP
			+ ConversationParticipantEntry.COLUMN_NAME_IDENTITY_ID
			+ INTEGER_TYPE
			+ COMMA_SEP
			+ ConversationEntry.COLUMN_NAME_GROUP_ID
			+ TEXT_TYPE
			+ COMMA_SEP
			+ getCompositePrimaryKey(new String[] {
					ConversationParticipantEntry.COLUMN_NAME_SESSION_ID,
					ConversationParticipantEntry.COLUMN_NAME_IDENTITY_ID })
			+ " )";

	public static final String SQL_CREATE_MESSAGES = "CREATE TABLE "
			+ MessageEntry.TABLE_NAME + " (" + MessageEntry._ID
			+ INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ MessageEntry.COLUMN_NAME_MESSAGE_ID + TEXT_TYPE + UNIQUE_TYPE
			+ COMMA_SEP + MessageEntry.COLUMN_NAME_SESSION_ID + INTEGER_TYPE
			+ COMMA_SEP + MessageEntry.COLUMN_NAME_MESSAGE_TYPE + TEXT_TYPE
			+ COMMA_SEP + MessageEntry.COLUMN_NAME_SENDER_ID + TEXT_TYPE
			+ COMMA_SEP + MessageEntry.COLUMN_NAME_MESSAGE_TEXT + TEXT_TYPE
			+ COMMA_SEP + MessageEntry.COLUMN_NAME_MESSAGE_TIME + TEXT_TYPE
			+ " )";

}
