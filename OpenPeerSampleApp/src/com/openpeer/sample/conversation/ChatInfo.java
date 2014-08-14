package com.openpeer.sample.conversation;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.openpeer.sdk.datastore.DatabaseContracts;
import com.openpeer.sdk.datastore.DatabaseContracts.WindowParticipantEntry;
import com.openpeer.sdk.datastore.DatabaseContracts.WindowViewEntry;

public class ChatInfo {

	private String mNameString;
	private String mLastMessage;
	private long mLastMessageTime;
	private int mUnreadCount;
	private long mUserIDs[];
	private String mAvatarUri[];

	public String getmNameString() {
		return mNameString;
	}

	public String getmLastMessage() {
		return mLastMessage;
	}

	public long getmLastMessageTime() {
		return mLastMessageTime;
	}

	public int getmUnreadCount() {
		return mUnreadCount;
	}

	public long[] getmUserIDs() {
		return mUserIDs;
	}

	public ChatInfo(long[] userIDs, String mNameString, String mLastMessage, long mLastMessageTime, int mUnreadCount, String avatarUri[]) {
		super();
		this.mUserIDs = userIDs;
		this.mNameString = mNameString;
		this.mLastMessage = mLastMessage;
		this.mLastMessageTime = mLastMessageTime;
		this.mUnreadCount = mUnreadCount;
		this.mAvatarUri = avatarUri;
	}

	public static ChatInfo fromCursor(Cursor cursor) {
		String mNameString = cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_PARTICIPANT_NAMES));
		String mLastMessage = cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_LAST_MESSAGE));
		long mLastMessageTime = cursor.getLong(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_LAST_MESSAGE_TIME));
		// TODO: join avatars properly
		// String avatarUri = cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_AVATARS));
		String avatarUri = cursor.getString(cursor.getColumnIndex(DatabaseContracts.COLUMN_NAME_AVATAR_URI));
		String avatarUris[] = null;
		if (!TextUtils.isEmpty(avatarUri)) {
			avatarUris = avatarUri.split(",");
		}
		String idListString = cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_USER_ID));
		String idStrings[] = idListString.split(",");

		long IDs[] = new long[idStrings.length];
		for (int i = 0; i < IDs.length; i++) {
			IDs[i] = Long.parseLong(idStrings[i]);
		}
		int mUnreadCount = cursor.getInt(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_UNREAD_COUNT));
		return new ChatInfo(IDs, mNameString, mLastMessage, mLastMessageTime, mUnreadCount, avatarUris);
	}

	public String getAvatarUri() {
		// todo: Support proper selection of avatar url
		if (mAvatarUri != null && mAvatarUri.length > 0) {
			return mAvatarUri[0];
		}
		return null;
	}

}
