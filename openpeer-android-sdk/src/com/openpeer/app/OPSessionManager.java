package com.openpeer.app;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.openpeer.javaapi.OPConversationThread;

public class OPSessionManager {
	List<OPSession> mSessions;

	private static OPSessionManager instance;

	public static OPSessionManager getInstance() {
		if (instance == null) {
			instance = new OPSessionManager();
			instance.mSessions = new ArrayList<OPSession>();
		}
		return instance;
	}

	public OPSession addSession(OPSession session) {
		Log.d("test", "add session for thread " + session.getThread().getThreadID() + " window " + session.getCurrentWindowId());
		mSessions.add(session);
		return session;
	}

	public OPSession getSessionOfThread(OPConversationThread thread) {
		Log.d("test", "search session for thread " + thread.getThreadID() + " sessions " + mSessions.size());

		for (OPSession session : mSessions) {
			if (session.getThread() != null && thread.getThreadID().equals(session.getThread().getThreadID())) {
				Log.d("test", "found session for thread " + thread.getThreadID() + " sessions " + mSessions.size());
				return session;

			} else {
				// Compare the participants, same participants is treated as
				// same session
				// if(session.hasSameParticipants()){
				// return session;
				// }
			}
		}
		// No existing session with the thread, now lets try to find
		return new OPSession(thread);
	}

	public List<OPSession> getRecentSessions() {
		return OPDataManager.getDatastoreDelegate().getRecentSessions();
	}

	public OPSession getSessionForUsers(long[] userIDs) {
		for (OPSession session : mSessions) {
			if (session.isForUsers(userIDs)) {
				return session;
			}
		}
		return null;
	}
}
