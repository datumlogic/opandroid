package com.openpeer.app;

import java.util.ArrayList;
import java.util.List;

import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentityContact;

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
		mSessions.add(session);
		return session;
	}

	public OPSession getSessionForContact(OPIdentityContact contact) {
		for (OPSession session : mSessions) {
			if (session.hasContact(contact)) {
				return session;
			}
		}
		return new OPSession(contact);
	}

	public OPSession getSessionOfThread(OPConversationThread thread) {
		for (OPSession session : mSessions) {
			if (session.getThread()!=null){
				if(thread.getThreadID().equals( session.getThread().getStableID()) ){
					return session;
				}
			} else {
				// Compare the participants, same participants is treated as same session
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
}
