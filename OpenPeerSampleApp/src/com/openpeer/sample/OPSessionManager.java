package com.openpeer.sample;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.openpeer.app.OPDataManager;
import com.openpeer.app.OPHelper;
import com.openpeer.app.OPSession;
import com.openpeer.app.OPUser;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.ContactStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sample.conversation.ConversationActivity;

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

	public OPSession getSessionById(String id) {
		return null;
	}

	/**
	 * Look up existing session for thread. Uses thread id to look up
	 * 
	 * @param thread
	 * @return
	 */
	public OPSession getSessionOfThread(OPConversationThread thread) {
		Log.d("test", "search session for thread " + thread.getThreadID() + " sessions " + mSessions.size());

		for (OPSession session : mSessions) {
			if (session.getThread() != null && thread.getThreadID().equals(session.getThread().getThreadID())) {
				Log.d("test", "found session for thread " + thread.getThreadID() + " sessions " + mSessions.size());
				return session;

			}
		}
		// No existing session with the thread, now lets try to find
		return new OPSession(thread);
	}

	public List<OPSession> getRecentSessions() {
		return OPDataManager.getDatastoreDelegate().getRecentSessions();
	}

	/**
	 * Look up the session "for" users. This call use calculated window id to find the session.
	 * 
	 * @param userIDs
	 * @return
	 */
	public OPSession getSessionForUsers(long[] userIDs) {
		for (OPSession session : mSessions) {
			if (session.isForUsers(userIDs)) {
				return session;
			}
		}
		return null;
	}

	/**
	 * Find the session "including" the users, if not create one. Return ongoing call in the session or make a new call
	 * 
	 * @param ids
	 * @param audio
	 * @param video
	 * @return
	 */
	public OPCall findOrMakeCall(long[] ids, boolean audio, boolean video) {
		OPSession session = getSessionWithUsers(ids);
		if (session != null && session.getCurrentCall() != null) {
			return session.getCurrentCall();
		} else {
			List<OPUser> users = OPDataManager.getDatastoreDelegate().getUsers(ids);
			session = new OPSession(users);
			if (session != null) {
				session.placeCall(users, audio, video);
			}
		}
		return null;
	}

	public OPCall findAndReplaceCall(long[] ids, boolean audio, boolean video) {
		return null;
	}

	/**
	 * Find existing session "including" the users
	 * 
	 * @param ids
	 *            user ids
	 * @return
	 */
	private OPSession getSessionWithUsers(long[] ids) {
		//TODO: implement proper look up
		return getSessionForUsers(ids);
	}

	OPCall mActiveCall;
	Hashtable<Long, OPCall> mStandbyCalls;

	private OPCallDelegate mBackgroundCallHandler;

	public void onEnteringBackground() {
	}

	public void onEnteringForeground() {
	}

	public void onCallStateChanged(OPCall call, CallStates state) {
		if (OPHelper.getInstance().isAppInBackground()) {
			mBackgroundCallHandler.onCallStateChanged(call, state);
		}
	}

	/**
	 * Application should provide a background call handler to show an notification for incoming call, and all other fany stuff
	 * 
	 * @param delegate
	 */
	public void setBackgroundCallDelegate(OPCallDelegate delegate) {
		mBackgroundCallHandler = delegate;
	}

	void init() {
		OPConversationThreadDelegate threadDelegate =new  OPConversationThreadDelegate() {

			@Override
			public void onConversationThreadNew(OPConversationThread conversationThread) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onConversationThreadContactsChanged(OPConversationThread conversationThread) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onConversationThreadContactStateChanged(OPConversationThread conversationThread, OPContact contact,
					ContactStates state) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onConversationThreadMessage(OPConversationThread conversationThread, String messageID) {
				OPMessage message = conversationThread.getMessage(messageID);
				getSessionOfThread(conversationThread).onMessageReceived(message);
			}

			@Override
			public void onConversationThreadMessageDeliveryStateChanged(OPConversationThread conversationThread, String messageID,
					MessageDeliveryStates state) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onConversationThreadPushMessage(OPConversationThread conversationThread, String messageID, OPContact contact) {
				// TODO Auto-generated method stub

			}
		};
		OPCallDelegate callDelegate = new OPCallDelegate(){

			@Override
			public void onCallStateChanged(OPCall call, CallStates state) {
				switch(state){
				case CallState_Incoming:
					ConversationActivity.launchForIncomingCall(OPApplication.getInstance(),call.getCallID());
				}
				
			}
			
		};

		
	}
}
