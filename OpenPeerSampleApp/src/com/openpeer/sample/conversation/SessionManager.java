package com.openpeer.sample.conversation;

import java.util.ArrayList;
import java.util.List;

import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentityContact;

public class SessionManager {
	List<Session> mSessions;

	private static SessionManager instance;

	public static SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
			instance.mSessions = new ArrayList<Session>();
		}
		return instance;
	}

	public Session addSession(Session session) {
		mSessions.add(session);
		return session;
	}

	Session getSessionForContact(OPIdentityContact contact) {
		for (Session session : mSessions) {
			if (session.hasContact(contact)) {
				return session;
			}
		}
		return new Session(contact);
	}
}
