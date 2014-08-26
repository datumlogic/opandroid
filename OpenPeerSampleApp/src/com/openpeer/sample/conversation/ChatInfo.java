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
