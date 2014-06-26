package com.openpeer.app;

import java.util.Arrays;
import java.util.List;

import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentityContact;

public class OPChatWindow {
	OPConversationThread thread;
	List<OPIdentityContact> mParticipants;
	long mUniqueId;
 
	public long getWindowId() {
		if (mUniqueId == 0) {
			long IDs[] = new long[mParticipants.size()];
			for (int i = 0; i < mParticipants.size() - 1; i++) {
				OPIdentityContact contact = mParticipants.get(i);
				IDs[i] = contact.getUserId();
			}
			Arrays.sort(IDs);
			mUniqueId = IDs.hashCode();
		}
		return mUniqueId;
	}

}
