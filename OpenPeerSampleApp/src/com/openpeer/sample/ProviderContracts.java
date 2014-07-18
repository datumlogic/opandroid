package com.openpeer.sample;

import android.net.Uri;
import static com.openpeer.sdk.datastore.DatabaseContracts.*;

/**
 * 
 */
public abstract class ProviderContracts {

	public static final String AUTHORITY = "com.openpeer.sample.provider";
	static final String SCHEME = "content://";
	public static final String URI_PREFIX = SCHEME + AUTHORITY;
	public static final String CONTENT_URI_CONTACTS = ContactEntry.TABLE_NAME;

	// beginning of UserEntry
	public static final Uri CONTENT_URI_USER = Uri.parse(URI_PREFIX + UserEntry.URI_PATH_INFO);
	public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + UserEntry.URI_PATH_INFO + "/#");

	// Ending of UserEntry

	// Beginning ConversationWindowEntry
	public static final Uri CONTENT_URI_WINDOW = Uri.parse(URI_PREFIX + ConversationWindowEntry.URI_PATH_INFO);
	public static final Uri CONTENT_ID_URI_PATTERN_WINDOW = Uri.parse(SCHEME + AUTHORITY + ConversationWindowEntry.URI_PATH_INFO + "/#");

	// End of ConversationWindowEntry

	// Beginning MessageEntry

	// content://com.afzaln.restclient.provider/messages
	public static final Uri CONTENT_URI_MESSAGE = Uri.parse(URI_PREFIX + MessageEntry.URI_PATH_INFO_WINDOW);
	public static final Uri CONTENT_ID_URI_PATTERN_MESSAGE = Uri.parse(SCHEME + AUTHORITY + MessageEntry.URI_PATH_INFO_WINDOW + "/#");
	

	// End of MessageEntry

	// Beginning of WindowParticipantEntry

	public static final Uri CONTENT_URI_PARTICIPANT = Uri.parse(URI_PREFIX + WindowParticipantEntry.URI_PATH_INFO);
	public static final Uri CONTENT_ID_URI_PATTERN_PARTICIPANT = Uri
			.parse(SCHEME + AUTHORITY + WindowParticipantEntry.URI_PATH_INFO + "/#");

	// End of WindowParticipantEntry

	// Beginning WindowViewEntry
	public static final Uri CONTENT_URI_WINDOW_VIEW = Uri.parse(URI_PREFIX + WindowViewEntry.URI_PATH_INFO);
	public static final Uri CONTENT_ID_URI_PATTERN_WINDOW_VIEW = Uri.parse(SCHEME + AUTHORITY + WindowViewEntry.URI_PATH_INFO + "/#");

	// End of WindowViewEntry

	// Beginning of ContactsViewEntry
	public static final Uri CONTENT_URI_CONTACTS_VIEW = Uri.parse(URI_PREFIX + ContactsViewEntry.URI_PATH_INFO);
	public static final Uri CONTENT_ID_URI_PATTERN_CONTACTS_VIEW = Uri.parse(SCHEME + AUTHORITY + ContactsViewEntry.URI_PATH_INFO + "/#");
	// End of ContactsViewEntry
	
	public static Uri getContentUri(String path) {
		return Uri.parse(SCHEME + AUTHORITY + path);
	}
}
