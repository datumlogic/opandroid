package com.openpeer.sdk.datastore;

import android.provider.BaseColumns;

/**
 * Database definitions and create statements.
 */
public class DatabaseContracts {
	public static final String TEXT_TYPE = " TEXT";
	public static final String INTEGER_TYPE = " INTEGER";
	public static final String INTEGER_PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY";
	public static final String UNIQUE_TYPE = " UNIQUE";
	public static final String PRIMARY_KEY_TYPE = " PRIMARY KEY";
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
	public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS ";

	public static final String COMMA_SEP = ",";

	public static final String AUTHORITY = "com.openpeer.sample.provider";
	static final String SCHEME = "content://";
	public static final String URI_PREFIX = SCHEME + AUTHORITY;
	public static final String COLUMN_NAME_IDENTITY_URI = "identity_uri";
	public static final String COLUMN_NAME_AVATAR_URI = "avatar_uri";

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
	// The public key and secret should be stored in system keychain for
	// security.
	public static abstract class AccountEntry implements BaseColumns {
		public static final String TABLE_NAME = "account";
		public static final String COLUMN_NAME_RELOGIN_INFO = "relogin_info";

	}

	/**
	 * To store OPRolodexContact
	 * 
	 */
	public static abstract class ContactEntry implements BaseColumns {

		public static final String TABLE_NAME = "contact";

		public static final String COLUMN_NAME_ASSOCIATED_IDENTITY_ID = "associated_ientity_id";
		public static final String COLUMN_NAME_IDENTITY_PROVIDER = "identity_provider";
		public static final String COLUMN_NAME_CONTACT_NAME = "name";
		public static final String COLUMN_NAME_URL = "profile_url";
		public static final String COLUMN_NAME_VPROFILE_URL = "vprofile_url";
	}

	public static abstract class IdentityContactEntry implements BaseColumns {
		public static final String TABLE_NAME = "identity_contacts";
		public static final String COLUMN_NAME_ASSOCIATED_IDENTITY_ID = "associated_ientity_id";
		public static final String COLUMN_NAME_USER_ID = "user_id";
		public static final String COLUMN_NAME_STABLE_ID = "stable_id";
		// This is acutally hash of identityUri of OPRolodexContact, kinda redundant but keeping it for now
		public static final String COLUMN_NAME_PEERFILE_PUBLIC = "peerfile_public";
		public static final String COLUMN_NAME_IDENTITY_PROOF_BUNDLE = "identity_proof_bundle";
		public static final String COLUMN_NAME_PRORITY = "priority";
		public static final String COLUMN_NAME_WEIGHT = "weight";
		public static final String COLUMN_NAME_LAST_UPDATE_TIME = "last_update_time";
		public static final String COLUMN_NAME_EXPIRE = "expire";
	}

	public static abstract class IdentityEntry implements BaseColumns {
		public static final String TABLE_NAME = "identities";
		public static final String COLUMN_NAME_IDENTITY_ID = "stable_id";
		public static final String COLUMN_NAME_IDENTITY_PROVIDER = "provider";
		// The "selfContact" of the identity
		public static final String COLUMN_NAME_IDENTITY_CONTACT_ID = "contact_id";
		public static final String COLUMN_NAME_IDENTITY_CONTACTS_VERSION = "contacts_version";
	}

	public static abstract class AvatarEntry implements BaseColumns {
		public static final String TABLE_NAME = "avatars";
		public static final String COLUMN_NAME_AVATAR_NAME = "name";
		public static final String COLUMN_NAME_WIDTH = "width";
		public static final String COLUMN_NAME_HEIGHT = "height";

	}

	public static abstract class UserEntry implements BaseColumns {
		public static final String TABLE_NAME = "users";

		public static final String URI_PATH_INFO = "/" + TABLE_NAME;

		// Note the slash on the end of this one, as opposed to the
		// URI_PATH_INFO, which has no slash.
		public static final String URI_PATH_INFO_ID = "/" + TABLE_NAME + "/";
		public static final int INFO_ID_PATH_POSITION = 1;

		public static final String CONTENT_ID_URI_BASE = SCHEME + AUTHORITY + URI_PATH_INFO + "/";

		public static final String COLUMN_NAME_STABLE_ID = "stable_id";
		public static final String COLUMN_NAME_PEER_URI = "peer_uri";

		// A bit redundant informatin but to simplify querying
		public static final String COLUMN_NAME_USER_NAME = "name";
	}

	public static abstract class GroupEntry implements BaseColumns {
		public static final String TABLE_NAME = "group";
		// Group id -- not used now
		public static final String COLUMN_NAME_GROUP_ID = "group_id";
		public static final String COLUMN_NAME_GROUP_NAME = "group_name";
	}

	public static abstract class ConversationWindowEntry implements BaseColumns {
		static final String TABLE_NAME = "conversation_window";
		public static final String URI_PATH_INFO = "/" + TABLE_NAME;

		public static final String URI_PATH_INFO_ID = "/" + TABLE_NAME + "/";
		public static final int INFO_ID_PATH_POSITION = 1;

		public static final String CONTENT_ID_URI_BASE = SCHEME + AUTHORITY + URI_PATH_INFO + "/";
		// Group id -- not used now
		public static final String COLUMN_NAME_GROUP_ID = "group_id";
		// This is window id based on participants
		public static final String COLUMN_NAME_WINDOW_ID = "window_id";
		// public static final String COLUMN_NAME_LAST_READ_MSG_ID = "lrm_id";
		// Am I host of group chat, not used now
		public static final String COLUMN_NAME_THREAD_HOST = "is_host";
		public static final String COLUMN_NAME_START_TIME = "start_time";
		public static final String COLUMN_NAME_END_TIME = "end_time";

	}

	/**
	 * Record the state snapshot: when and what contact is added/deleted TODO: refine when requirements are ready
	 * 
	 * Not used for now
	 * 
	 */
	public static abstract class ConversationStateChangeEntry implements BaseColumns {
		public static final String TABLE_NAME = "conversation_state_change";
		public static final String COLUMN_NAME_THREAD_ID = "thread_id";
		public static final String COLUMN_NAME_STATE_TIME = "time";
	}

	/**
	 * state_type: ADD_CONTACT, REMOVE_CONTACT, CALL_ENDED, etc
	 * 
	 * Not used for now
	 * 
	 */
	public static abstract class ConversationStateEntry implements BaseColumns {
		public static final String TABLE_NAME = "conversation_state";
		public static final String COLUMN_NAME_state_change_ID = "change_id";
		public static final String COLUMN_NAME_STATE_TYPE = "state_type";
	}

	public static abstract class MessageEntry implements BaseColumns {
		public static final String TABLE_NAME = "message";
		public static final String URI_PATH_INFO = "/" + TABLE_NAME;

		public static final String URI_PATH_INFO_ID = "/" + TABLE_NAME + "/";
		public static final int INFO_ID_PATH_POSITION = 2;

		// for content provider insert() call
		public static final String CONTENT_ID_URI_BASE = SCHEME + AUTHORITY + URI_PATH_INFO + "/";

		public static final String COLUMN_NAME_MESSAGE_ID = "message_id";
		public static final String COLUMN_NAME_WINDOW_ID = "window_id";
		// for now only text is supported
		public static final String COLUMN_NAME_MESSAGE_TYPE = "type";
		// id of peer contact. use your own id or "self" for outgoing message
		public static final String COLUMN_NAME_SENDER_ID = "sender_id";
		// Message content text
		public static final String COLUMN_NAME_MESSAGE_TEXT = "text";
		// sent or receive time
		public static final String COLUMN_NAME_MESSAGE_TIME = "time";
		// Whether the message has been read/presented, default 0 means not read
		public static final String COLUMN_NAME_MESSAGE_READ = "read";

		public static final String COLUMN_NAME_MESSAGE_DELIVERY_STATUS = "delivery_status";
		public String[] PROJECTIONS = { COLUMN_NAME_MESSAGE_ID, COLUMN_NAME_WINDOW_ID, COLUMN_NAME_MESSAGE_TYPE, COLUMN_NAME_SENDER_ID,
				COLUMN_NAME_MESSAGE_TEXT, COLUMN_NAME_MESSAGE_TIME, COLUMN_NAME_MESSAGE_READ, COLUMN_NAME_MESSAGE_DELIVERY_STATUS };
	}

	public static abstract class CallEntry implements BaseColumns {
		public static final String TABLE_NAME = "calls";
		public static final String COLUMN_NAME_CALL_ID = "call_id";
		public static final String COLUMN_NAME_THREAD_ID = "thread_id";
		public static final String COLUMN_NAME_CALLER = "caller";
		public static final String COLUMN_NAME_CALLEE = "callee";
		// The "selfContact" of the CALL
		public static final String COLUMN_NAME_ANSWER_TIME = "answer_time";
		public static final String COLUMN_NAME_CLOSE_TIME = "closed_time";
	}

	public static abstract class WindowParticipantEntry implements BaseColumns {
		public static final String TABLE_NAME = "window_participants";
		public static final String URI_PATH_INFO = "/" + TABLE_NAME;

		// Note the slash on the end of this one, as opposed to the
		// URI_PATH_INFO, which has no slash.
		public static final String URI_PATH_INFO_ID = "/" + TABLE_NAME + "/";
		public static final int INFO_ID_PATH_POSITION = 1;

		// content://com.afzaln.restclient.provider/message/
		// for content provider insert() call
		public static final String CONTENT_ID_URI_BASE = SCHEME + AUTHORITY + URI_PATH_INFO + "/";

		// The session id this pariticipant is associated with
		public static final String COLUMN_NAME_WINDOW_ID = "window_id";
		public static final String COLUMN_NAME_USER_ID = "user_id";
		// this is neccessary in case group chat with unknown participants
		public static final String COLUMN_NAME_USER_NAME = "name";
		// This is the default avatar
	}

	public static abstract class WindowViewEntry implements BaseColumns {
		public static final String TABLE_NAME = "windows";
		public static final String URI_PATH_INFO = "/" + TABLE_NAME;
		public static final String URI_PATH_INFO_ID = "/" + TABLE_NAME + "/";
		public static final int INFO_ID_PATH_POSITION = 1;

		public static final String COLUMN_NAME_GROUP_ID = "group_id";
		// This is window id based on participants
		public static final String COLUMN_NAME_WINDOW_ID = "window_id";
		public static final String COLUMN_NAME_LAST_READ_MSG_ID = "lrm_id";
		public static final String COLUMN_NAME_LAST_MESSAGE = "last_message";
		public static final String COLUMN_NAME_LAST_MESSAGE_TIME = "last_message_time";
		public static final String COLUMN_NAME_USER_ID = "user_id";
		public static final String COLUMN_NAME_PARTICIPANT_NAMES = "name";
		public static final String COLUMN_NAME_UNREAD_COUNT = "unread_count";

		public static final String COLUMNS = "a." + BaseColumns._ID + " as " + BaseColumns._ID + "," + "a."
				+ ConversationWindowEntry.COLUMN_NAME_WINDOW_ID + " as " + COLUMN_NAME_WINDOW_ID + "," + "group_concat(" + "b."
				+ COLUMN_NAME_USER_ID + "," + "',')" + " as " + COLUMN_NAME_USER_ID + ","
				+ "group_concat(" + "b." + COLUMN_NAME_PARTICIPANT_NAMES + "," + "',')" + " as " + COLUMN_NAME_PARTICIPANT_NAMES + ","
				+ "group_concat(" + "b." + COLUMN_NAME_AVATAR_URI + "," + "',')" + " as " + COLUMN_NAME_AVATAR_URI;
	}

	/**
	 * Not used for now.
	 * 
	 * 
	 */
	public static abstract class GroupParticipantEntry implements BaseColumns {
		public static final String TABLE_NAME = "window_participants";

		// The session id this pariticipant is associated with
		public static final String COLUMN_NAME_SESSION_ID = "group_id";
		public static final String COLUMN_NAME_IDENTITY_ID = "identity_id";
		// this is neccessary in case group chat with unknown participants
		public static final String COLUMN_NAME_IDENTITY_NAME = "name";
	}

	/**
	 * View to get all the information of a contact
	 * 
	 * @author brucexia
	 * 
	 */
	public static abstract class ContactsViewEntry implements BaseColumns {
		public static final String TABLE_NAME = "contacts";
		public static final String URI_PATH_INFO = "/" + TABLE_NAME;

		// Note the slash on the end of this one, as opposed to the
		// URI_PATH_INFO, which has no slash.
		public static final String URI_PATH_INFO_ID = "/" + TABLE_NAME + "/";
		public static final int INFO_ID_PATH_POSITION = 1;

		public static final String COLUMN_NAME_USER_ID = "user_id";
		// HashCode of identityUri.
		public static final String COLUMN_NAME_ASSOCIATED_IDENTITY_ID = "associated_ientity_id";
		// public static final String COLUMN_NAME_IDENTITY_CONTACT_ID =
		// "identity_contact_id";
		public static final String COLUMN_NAME_IDENTITY_URI = "identity_uri";
		public static final String COLUMN_NAME_IDENTITY_PROVIDER = "identity_provider";

		public static final String COLUMN_NAME_CONTACT_NAME = "name";
		public static final String COLUMN_NAME_URL = "profile_url";
		public static final String COLUMN_NAME_VPROFILE_URL = "vprofile_url";

		// colums for identity contact
		public static final String COLUMN_NAME_STABLE_ID = "stable_id";
		public static final String COLUMN_NAME_PEERFILE_PUBLIC = "peerfile_public";
		public static final String COLUMN_NAME_IDENTITY_PROOF_BUNDLE = "identity_proof_bundle";
		public static final String COLUMN_NAME_PRORITY = "priority";
		public static final String COLUMN_NAME_WEIGHT = "weight";
		public static final String COLUMN_NAME_LAST_UPDATE_TIME = "last_update_time";
		public static final String COLUMN_NAME_EXPIRE = "expire";

		public static final String COLUMNS = ContactEntry.TABLE_NAME + "." + BaseColumns._ID + " as " + BaseColumns._ID + ","
				+ ContactEntry.TABLE_NAME + "." + COLUMN_NAME_ASSOCIATED_IDENTITY_ID + " as " + COLUMN_NAME_ASSOCIATED_IDENTITY_ID + ","
				+ ContactEntry.TABLE_NAME + "." + COLUMN_NAME_CONTACT_NAME + " as " + COLUMN_NAME_CONTACT_NAME + ","
				+ ContactEntry.TABLE_NAME + "." + COLUMN_NAME_IDENTITY_URI + " as " + COLUMN_NAME_IDENTITY_URI + ","
				+ ContactEntry.TABLE_NAME + "." + COLUMN_NAME_IDENTITY_PROVIDER + " as " + COLUMN_NAME_IDENTITY_PROVIDER + ","
				+ ContactEntry.TABLE_NAME + "." + COLUMN_NAME_URL + " as " + COLUMN_NAME_URL + ","
				+ ContactEntry.TABLE_NAME + "." + COLUMN_NAME_VPROFILE_URL + " as " + COLUMN_NAME_VPROFILE_URL + ","
				+ AvatarEntry.TABLE_NAME + "." + COLUMN_NAME_AVATAR_URI + " as " + COLUMN_NAME_AVATAR_URI + ","

				// IdentityContact table
				+ IdentityContactEntry.TABLE_NAME + "." + COLUMN_NAME_USER_ID + " as " + COLUMN_NAME_USER_ID + ","
				+ IdentityContactEntry.TABLE_NAME + "." + COLUMN_NAME_STABLE_ID + " as " + COLUMN_NAME_STABLE_ID + ","
				+ IdentityContactEntry.TABLE_NAME + "." + COLUMN_NAME_PEERFILE_PUBLIC + " as " + COLUMN_NAME_PEERFILE_PUBLIC + ","
				+ IdentityContactEntry.TABLE_NAME + "." + COLUMN_NAME_IDENTITY_PROOF_BUNDLE + " as " + COLUMN_NAME_IDENTITY_PROOF_BUNDLE
				+ "," + IdentityContactEntry.TABLE_NAME + "." + COLUMN_NAME_PRORITY + " as " + COLUMN_NAME_PRORITY + ","
				+ IdentityContactEntry.TABLE_NAME + "." + COLUMN_NAME_WEIGHT + " as " + COLUMN_NAME_WEIGHT + ","
				+ IdentityContactEntry.TABLE_NAME + "." + COLUMN_NAME_LAST_UPDATE_TIME + " as " + COLUMN_NAME_LAST_UPDATE_TIME + ","
				+ IdentityContactEntry.TABLE_NAME + "." + COLUMN_NAME_EXPIRE + " as " + COLUMN_NAME_EXPIRE;
	}

	// Beginning of create table statements. new statements should be added at the end and marked with the DB version created
	public static final String SQL_CREATE_VIEW_WINDOW = CREATE_VIEW + WindowViewEntry.TABLE_NAME + " AS SELECT d." + BaseColumns._ID
			+ " as " + BaseColumns._ID + "," + "d." + ConversationWindowEntry.COLUMN_NAME_WINDOW_ID + " as "
			+ ConversationWindowEntry.COLUMN_NAME_WINDOW_ID + "," + "d." + WindowViewEntry.COLUMN_NAME_USER_ID + " as "
			+ WindowViewEntry.COLUMN_NAME_USER_ID + "," + "d." + WindowViewEntry.COLUMN_NAME_PARTICIPANT_NAMES + " as "
			+ WindowViewEntry.COLUMN_NAME_PARTICIPANT_NAMES + ","
			+ WindowViewEntry.COLUMN_NAME_USER_ID + "," + "d." + COLUMN_NAME_AVATAR_URI + " as " + COLUMN_NAME_AVATAR_URI + ","
			+ "c." + MessageEntry.COLUMN_NAME_MESSAGE_TEXT + " as "
			+ WindowViewEntry.COLUMN_NAME_LAST_MESSAGE + "," + "c." + MessageEntry.COLUMN_NAME_MESSAGE_TIME + " as "
			+ WindowViewEntry.COLUMN_NAME_LAST_MESSAGE_TIME + "," + " e.count as " + WindowViewEntry.COLUMN_NAME_UNREAD_COUNT
			+ " from (SELECT " + WindowViewEntry.COLUMNS + " from " + ConversationWindowEntry.TABLE_NAME + " a " + " left join "
			+ WindowParticipantEntry.TABLE_NAME + " b " + " using(" + WindowViewEntry.COLUMN_NAME_WINDOW_ID + ") group by window_id)  d"
			+ " inner join (select window_id,text,time from " + MessageEntry.TABLE_NAME + " group by window_id) c using(window_id) "
			+ " left join (select count(*) as count,window_id from message where read=0 group by window_id) e " + " using("
			+ WindowViewEntry.COLUMN_NAME_WINDOW_ID + ")" + " group by " + WindowViewEntry.COLUMN_NAME_WINDOW_ID;
	public static final String SQL_CREATE_VIEW_CONTACT = CREATE_VIEW + ContactsViewEntry.TABLE_NAME + " AS SELECT "
			+ ContactsViewEntry.COLUMNS + " from " + ContactEntry.TABLE_NAME + " left join " + IdentityContactEntry.TABLE_NAME + " using("
			+ COLUMN_NAME_IDENTITY_URI + ")" + " left join " + AvatarEntry.TABLE_NAME + " on " + AvatarEntry.TABLE_NAME
			+ "." + COLUMN_NAME_IDENTITY_URI + "=" + ContactEntry.TABLE_NAME + "." + COLUMN_NAME_IDENTITY_URI
			+ " group by " + COLUMN_NAME_IDENTITY_URI;

	public static final String SQL_CREATE_CONTACT = CREATE_TABLE + ContactEntry.TABLE_NAME + " (" + ContactEntry._ID
			+ INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + INTEGER_TYPE + " default -1 " + COMMA_SEP
			// ContactEntry.COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP +
			+ ContactsViewEntry.COLUMN_NAME_STABLE_ID + TEXT_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ ContactEntry.COLUMN_NAME_CONTACT_NAME + TEXT_TYPE + " COLLATE NOCASE" + COMMA_SEP
			+ ContactEntry.COLUMN_NAME_IDENTITY_PROVIDER + TEXT_TYPE + COMMA_SEP
			+ COLUMN_NAME_IDENTITY_URI + TEXT_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ ContactEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP
			+ ContactEntry.COLUMN_NAME_VPROFILE_URL + TEXT_TYPE + " )";
	public static final String SQL_CREATE_IDENTITY = CREATE_TABLE + IdentityEntry.TABLE_NAME + " (" + IdentityEntry._ID + INTEGER_TYPE
			+ COMMA_SEP + IdentityEntry.COLUMN_NAME_IDENTITY_ID + INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ IdentityEntry.COLUMN_NAME_IDENTITY_PROVIDER + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_IDENTITY_URI + TEXT_TYPE
			+ COMMA_SEP + IdentityEntry.COLUMN_NAME_IDENTITY_CONTACT_ID + INTEGER_TYPE + COMMA_SEP
			+ IdentityEntry.COLUMN_NAME_IDENTITY_CONTACTS_VERSION + TEXT_TYPE +
			// ... Any other options for the CREATE command
			" )";
	public static final String SQL_CREATE_AVATAR = CREATE_TABLE + AvatarEntry.TABLE_NAME + " ("
			+ AvatarEntry._ID + INTEGER_TYPE + COMMA_SEP
			+ COLUMN_NAME_IDENTITY_URI + INTEGER_TYPE + COMMA_SEP
			+ AvatarEntry.COLUMN_NAME_AVATAR_NAME + TEXT_TYPE + COMMA_SEP
			+ COLUMN_NAME_AVATAR_URI + TEXT_TYPE + COMMA_SEP
			+ AvatarEntry.COLUMN_NAME_WIDTH + INTEGER_TYPE + COMMA_SEP
			+ AvatarEntry.COLUMN_NAME_HEIGHT + INTEGER_TYPE + COMMA_SEP
			+ getCompositePrimaryKey(new String[] { COLUMN_NAME_IDENTITY_URI, COLUMN_NAME_AVATAR_URI }) + " )";
	public static final String SQL_CREATE_IDENTITY_CONTACT = CREATE_TABLE + IdentityContactEntry.TABLE_NAME + " ("
			+ IdentityContactEntry._ID + INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP + IdentityContactEntry.COLUMN_NAME_STABLE_ID + TEXT_TYPE
			+ COMMA_SEP + IdentityContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + INTEGER_TYPE + " default -1 " + COMMA_SEP
			+ ContactsViewEntry.COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP
			+ COLUMN_NAME_IDENTITY_URI + TEXT_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ IdentityContactEntry.COLUMN_NAME_PEERFILE_PUBLIC + TEXT_TYPE + COMMA_SEP
			+ IdentityContactEntry.COLUMN_NAME_IDENTITY_PROOF_BUNDLE + TEXT_TYPE + COMMA_SEP + IdentityContactEntry.COLUMN_NAME_PRORITY
			+ INTEGER_TYPE + COMMA_SEP + IdentityContactEntry.COLUMN_NAME_WEIGHT + INTEGER_TYPE + COMMA_SEP
			+ IdentityContactEntry.COLUMN_NAME_LAST_UPDATE_TIME + INTEGER_TYPE + COMMA_SEP + IdentityContactEntry.COLUMN_NAME_EXPIRE + " )";
	public static final String SQL_CREATE_USER = CREATE_TABLE + UserEntry.TABLE_NAME + " ("
			+ UserEntry._ID + INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ UserEntry.COLUMN_NAME_STABLE_ID + INTEGER_TYPE + COMMA_SEP
			+ UserEntry.COLUMN_NAME_PEER_URI + TEXT_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ COLUMN_NAME_IDENTITY_URI + TEXT_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ UserEntry.COLUMN_NAME_USER_NAME + TEXT_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ COLUMN_NAME_AVATAR_URI + TEXT_TYPE + " )";

	public static final String SQL_CREATE_WINDOW = CREATE_TABLE + ConversationWindowEntry.TABLE_NAME + " (" + BaseColumns._ID
			+ INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP + ConversationWindowEntry.COLUMN_NAME_WINDOW_ID + INTEGER_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ ConversationWindowEntry.COLUMN_NAME_GROUP_ID + TEXT_TYPE + " )";

	public static final String SQL_CREATE_GROUP = CREATE_TABLE + BaseColumns._ID + INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ GroupEntry.TABLE_NAME + " (" + BaseColumns._ID + INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP + GroupEntry.COLUMN_NAME_GROUP_ID
			+ TEXT_TYPE + COMMA_SEP + GroupEntry.COLUMN_NAME_GROUP_NAME + TEXT_TYPE + " )";
	public static final String SQL_CREATE_CALL = CREATE_TABLE + CallEntry.TABLE_NAME + " ("
			+ BaseColumns._ID + INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ CallEntry.COLUMN_NAME_CALL_ID + TEXT_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ CallEntry.COLUMN_NAME_CALLER + INTEGER_TYPE + COMMA_SEP
			+ CallEntry.COLUMN_NAME_CALLEE + INTEGER_TYPE + COMMA_SEP
			+ CallEntry.COLUMN_NAME_ANSWER_TIME + INTEGER_TYPE + COMMA_SEP
			+ CallEntry.COLUMN_NAME_CLOSE_TIME + INTEGER_TYPE + " )";

	public static final String SQL_CREATE_CONVERSATION_PARTICIPANT = CREATE_TABLE + WindowParticipantEntry.TABLE_NAME
			+ " (" + WindowParticipantEntry._ID + INTEGER_TYPE + COMMA_SEP
			+ WindowParticipantEntry.COLUMN_NAME_WINDOW_ID + INTEGER_TYPE + COMMA_SEP
			+ WindowParticipantEntry.COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP
			+ WindowParticipantEntry.COLUMN_NAME_USER_NAME + TEXT_TYPE + COMMA_SEP
			+ ConversationWindowEntry.COLUMN_NAME_GROUP_ID + TEXT_TYPE + COMMA_SEP
			+ COLUMN_NAME_AVATAR_URI + TEXT_TYPE + COMMA_SEP
			+ getCompositePrimaryKey(new String[] { WindowParticipantEntry.COLUMN_NAME_WINDOW_ID,
					WindowParticipantEntry.COLUMN_NAME_USER_ID }) + " )";

	public static final String SQL_CREATE_MESSAGES = CREATE_TABLE + MessageEntry.TABLE_NAME + " ("
			+ MessageEntry._ID + INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP
			+ MessageEntry.COLUMN_NAME_MESSAGE_ID + TEXT_TYPE + UNIQUE_TYPE + COMMA_SEP
			+ MessageEntry.COLUMN_NAME_WINDOW_ID + INTEGER_TYPE + COMMA_SEP
			+ MessageEntry.COLUMN_NAME_MESSAGE_TYPE + TEXT_TYPE + COMMA_SEP
			+ MessageEntry.COLUMN_NAME_SENDER_ID + INTEGER_TYPE + COMMA_SEP
			+ MessageEntry.COLUMN_NAME_MESSAGE_TEXT + TEXT_TYPE + COMMA_SEP
			+ MessageEntry.COLUMN_NAME_MESSAGE_TIME + TEXT_TYPE + COMMA_SEP
			+ MessageEntry.COLUMN_NAME_MESSAGE_READ + INTEGER_TYPE + " default 0" + COMMA_SEP
			+ MessageEntry.COLUMN_NAME_MESSAGE_DELIVERY_STATUS + INTEGER_TYPE + " )";

	public static final String CREATE_STATEMENTS[] = { DatabaseContracts.SQL_CREATE_IDENTITY,
			DatabaseContracts.SQL_CREATE_IDENTITY_CONTACT, DatabaseContracts.SQL_CREATE_CONTACT, DatabaseContracts.SQL_CREATE_AVATAR,
			DatabaseContracts.SQL_CREATE_USER,

			DatabaseContracts.SQL_CREATE_CONVERSATION_PARTICIPANT, DatabaseContracts.SQL_CREATE_WINDOW,
			DatabaseContracts.SQL_CREATE_MESSAGES,
			// Note: always keep view creation at end
			DatabaseContracts.SQL_CREATE_VIEW_CONTACT, DatabaseContracts.SQL_CREATE_VIEW_WINDOW, };
	// END of create statement

}
