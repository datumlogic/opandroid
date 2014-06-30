package com.openpeer.delegates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.text.format.Time;
import android.util.Log;

import com.openpeer.app.OPDataManager;
import com.openpeer.app.OPHelper;
import com.openpeer.app.OPSessionManager;
import com.openpeer.datastore.OPDatastoreDelegate;
import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.ContactStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPSettingsDelegate;
import com.openpeer.javaapi.OutputAudioRoutes;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPCache;
import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPMediaEngineDelegate;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackDelegate;

public class CallbackHandler {

	// ///////////////////////////////////////////////////////////////////
	// ACCOUNT DELEGATE GLUE
	// ///////////////////////////////////////////////////////////////////
	// OPAccountDelegate support
	private static OPAccount mAccount;
	static ArrayList<OPAccountDelegate> accountDelegates = new ArrayList<OPAccountDelegate>();
	private static CallbackHandler instance;
	private static OPDatastoreDelegate sDatastoreDelegate;
	private static OPConversationThreadDelegate mBackgroundConversationHandler;
	private static OPCallDelegate mBackgroundCallHandler;

	private CallbackHandler() {
	}

	public static CallbackHandler getInstance() {
		if (instance == null) {
			instance = new CallbackHandler();
		}
		return instance;
	}

	// Account Delegate glue methods
	public static void onAccountStateChanged(int state) {
		for (OPAccountDelegate delegate : accountDelegates) {
			if (mAccount != null && delegate != null) {
				delegate.onAccountStateChanged(mAccount,
						AccountStates.values()[state]);
			}
		}
	}

	public static void onAccountAssociatedIdentitiesChanged() {

		for (OPAccountDelegate delegate : accountDelegates) {
			if (mAccount != null && delegate != null) {
				delegate.onAccountAssociatedIdentitiesChanged(mAccount);
			}
		}

	}

	public static void onAccountPendingMessageForInnerBrowserWindowFrame() {

		for (OPAccountDelegate delegate : accountDelegates) {
			if (mAccount != null && delegate != null) {
				delegate.onAccountPendingMessageForInnerBrowserWindowFrame(mAccount);
			}
		}

	}

	// Account delegate register/unregister methods
	public boolean registerAccountDelegate(OPAccount account,
			OPAccountDelegate delegate) {
		if (account == null || delegate == null) {
			return false;
		}

		if (mAccount == null) {
			mAccount = account;
		}

		// Store the delegate object
		this.accountDelegates.add(delegate);

		return true;
	}

	public void unregisterAccountDelegate(OPAccountDelegate delegate) {
		mAccount = null;
		this.accountDelegates.remove(delegate);

	}

	// ///////////////////////////////////////////////////////////////////
	// STACK DELEGATE GLUE
	// ///////////////////////////////////////////////////////////////////
	// OPStackDelegate support
	// private OPStack mStack;
	static ArrayList<OPStackDelegate> stackDelegates = new ArrayList<OPStackDelegate>();

	public static void onStackShutdown() {
		for (OPStackDelegate delegate : stackDelegates) {
			if (delegate != null) {
				delegate.onStackShutdown();
			}
		}

	}

	// Stack delegate register/unregister methods
	public boolean registerStackDelegate(OPStackDelegate delegate) {
		if (delegate == null) {
			return false;
		}

		// Store the delegate object
		this.stackDelegates.add(delegate);

		return true;
	}

	public void unregisterStackDelegate(OPStackDelegate delegate) {
		this.stackDelegates.remove(delegate);

	}

	// ///////////////////////////////////////////////////////////////////
	// IDENTITY DELEGATE GLUE
	// ///////////////////////////////////////////////////////////////////
	// OPIdentityDelegate support
	private static OPIdentity mIdentity;
	static ArrayList<OPIdentityDelegate> identityDelegates = new ArrayList<OPIdentityDelegate>();

	public static void onIdentityStateChanged(int state) {

		for (OPIdentityDelegate delegate : identityDelegates) {
			if (mIdentity != null && delegate != null) {
				delegate.onIdentityStateChanged(mIdentity,
						IdentityStates.values()[state]);
			}
		}
	}

	public static void onIdentityPendingMessageForInnerBrowserWindowFrame() {
		for (OPIdentityDelegate delegate : identityDelegates) {
			if (mIdentity != null && delegate != null) {
				delegate.onIdentityPendingMessageForInnerBrowserWindowFrame(mIdentity);
			}
		}

	}

	public static void onIdentityRolodexContactsDownloaded() {
		for (OPIdentityDelegate delegate : identityDelegates) {
			if (mIdentity != null && delegate != null) {
				delegate.onIdentityRolodexContactsDownloaded(mIdentity);
			}
		}

	}

	// Identity delegate register/unregister methods
	public boolean registerIdentityDelegate(OPIdentity identity,
			OPIdentityDelegate delegate) {
		if (identity == null || delegate == null) {
			return false;
		}

		if (mIdentity == null) {
			mIdentity = identity;
		}

		// Store the delegate object
		this.identityDelegates.add(delegate);

		return true;
	}

	public void unregisterIdentityDelegate(OPIdentityDelegate delegate) {
		mIdentity = null;
		this.identityDelegates.remove(delegate);

	}

	// ///////////////////////////////////////////////////////////////////
	// CALL DELEGATE GLUE
	// ///////////////////////////////////////////////////////////////////
	// OPCallDelegate support
	private static OPCall mCall;
	static ArrayList<OPCallDelegate> callDelegates = new ArrayList<OPCallDelegate>();

	public static void onCallStateChanged(int state) {

		if (callDelegates.size() == 0 && mBackgroundCallHandler != null) {
			mBackgroundCallHandler.onCallStateChanged(mCall,
					CallStates.values()[state]);
		}
		for (OPCallDelegate delegate : callDelegates) {
			if (mCall != null && delegate != null) {
				delegate.onCallStateChanged(mCall, CallStates.values()[state]);
			}
		}
	}

	public boolean registerBackgroundCallDelegate(OPCallDelegate delegate) {
		mBackgroundCallHandler = delegate;
		return true;
	}

	public boolean registerBackgroundCallDelegate(
			OPConversationThreadDelegate delegate) {
		mBackgroundConversationHandler = delegate;
		return true;
	}

	// Call delegate register/unregister methods
	public boolean registerCallDelegate(OPCall call, OPCallDelegate delegate) {
		if (call == null || delegate == null) {
			return false;
		}

		if (mCall == null) {
			mCall = call;
		}

		// Store the delegate object
		this.callDelegates.add(delegate);

		return true;
	}

	public void unregisterCallDelegate(OPCallDelegate delegate) {
		mCall = null;
		this.callDelegates.remove(delegate);

	}

	// ///////////////////////////////////////////////////////////////////
	// CONVERSATION THREAD DELEGATE GLUE
	// ///////////////////////////////////////////////////////////////////
	// OPConversationThreadDelegate support
	// private static OPConversationThread mConversationThread;
	static ArrayList<OPConversationThreadDelegate> conversationThreadDelegates = new ArrayList<OPConversationThreadDelegate>();

	static HashMap<Long, OPConversationThread> mThreads = new HashMap<Long, OPConversationThread>();

	public static void onConversationThreadNew(OPConversationThread convThread) {
		mThreads.put(convThread.getNativeClassPtr(), convThread);
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates) {
			if (convThread != null && delegate != null) {
				// mConversationThread = convThread;
				delegate.onConversationThreadNew(convThread);
			} else {
				Log.e("openpeer-android-sdk",
						"No conversation thread listener available!!!");
			}
		}
	}

	public static void onConversationThreadContactsChanged(
			OPConversationThread convThread) {
		// OPSessionManager.getInstance().getSessionOfThread(convThread).onContactsChanged();
		OPConversationThread thread = mThreads.get(convThread.getNativeClassPtr());
		if (thread == null) {
			mThreads.put(convThread.getNativeClassPtr(), convThread);
		} else {
			convThread = thread;
		}
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates) {
			if (convThread != null && delegate != null) {
				delegate.onConversationThreadContactsChanged(convThread);
			} else {
				Log.e("openpeer-android-sdk",
						"No conversation thread or listener available!!!");
			}
		}
	}

	public static void onConversationThreadContactStateChanged(
			OPConversationThread convThread, OPContact contact, int state) {
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates) {
			if (convThread != null && delegate != null) {
				delegate.onConversationThreadContactStateChanged(convThread,
						contact, ContactStates.values()[state]);
			} else {
				Log.e("openpeer-android-sdk",
						"No conversation thread or listener available!!!");
			}
		}
	}

	public static void onConversationThreadMessage(
			OPConversationThread convThread, String messageID) {
		convThread = mThreads.get(convThread.getNativeClassPtr());
		OPMessage message = convThread.getMessage(messageID);

//		OPAccount account = convThread.getAssociatedAccount();
//		if (account != null) {
//			List<OPIdentity> identities = account.getAssociatedIdentities();
//			if (identities != null && identities.size() > 0) {
//				Log.d("test", "found identities for account " + identities.size() + identities.get(0).getIdentityURI());
//			}
//		}
		for (OPContact contact : convThread.getContacts()) {
			List<OPIdentityContact> iContacts = convThread.getIdentityContactList(contact);
			if (iContacts != null) {
				for (OPIdentityContact oContact : iContacts) {
					Log.d("test", "onConversationThreadMessage identityContact name " + oContact.getName() + " opcontact peer uri "
							+ contact.getPeerURI());
				}
			}
		}
		if (conversationThreadDelegates.size() == 0
				&& mBackgroundConversationHandler != null) {
			mBackgroundConversationHandler.onConversationThreadMessage(
					convThread, messageID);

		}
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates) {
			if (convThread != null && delegate != null) {
				delegate.onConversationThreadMessage(convThread, messageID);
			} else {
				Log.e("openpeer-android-sdk",
						"No conversation thread or listener available!!!");
			}
		}

		Log.d("test", "received message " + message);
		OPSessionManager.getInstance().getSessionOfThread(convThread).onMessageReceived(message);
	}

	public static void onConversationThreadMessageDeliveryStateChanged(
			OPConversationThread convThread, String messageID, int state) {
		if (conversationThreadDelegates.size() == 0
				&& mBackgroundConversationHandler != null) {
			mBackgroundConversationHandler
					.onConversationThreadMessageDeliveryStateChanged(
							convThread, messageID,
							MessageDeliveryStates.values()[state]);
		}
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates) {
			if (convThread != null && delegate != null) {
				delegate.onConversationThreadMessageDeliveryStateChanged(
						convThread, messageID,
						MessageDeliveryStates.values()[state]);
			} else {
				Log.e("openpeer-android-sdk",
						"No conversation thread or listener available!!!");
			}
		}
	}

	public static void onConversationThreadPushMessage(
			OPConversationThread convThread, String messageID, OPContact contact) {
		if (conversationThreadDelegates.size() == 0
				&& mBackgroundConversationHandler != null) {
			mBackgroundConversationHandler.onConversationThreadPushMessage(
					convThread, messageID, contact);

		}
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates) {
			if (convThread != null && delegate != null) {
				delegate.onConversationThreadPushMessage(convThread, messageID,
						contact);
			} else {
				Log.e("openpeer-android-sdk",
						"No conversation thread or listener available!!!");
			}
		}
	}

	// Conversation Thread delegate register/unregister methods
	public boolean registerConversationThreadDelegate(
			OPConversationThreadDelegate delegate) {
		if (delegate == null) {
			return false;
		}

		// Store the delegate object
		this.conversationThreadDelegates.add(delegate);

		return true;
	}

	public void unregisterConversationThreadDelegate(
			OPConversationThreadDelegate delegate) {
		conversationThreadDelegates.remove(delegate);

	}

	// ///////////////////////////////////////////////////////////////////
	// CACHE DELEGATE GLUE
	// ///////////////////////////////////////////////////////////////////
	// OPCacheDelegate support
	static OPCacheDelegate cacheDelegate;

	public static String fetch(String cookieNamePath) {
		String ret = "";
		if (cacheDelegate != null) {
			ret = cacheDelegate.fetch(cookieNamePath);
		}
		return ret;
	}

	public static void store(String cookieNamePath, int time, String dataToStore) {

		if (cacheDelegate != null) {
			Time t = new Time();
			cacheDelegate.store(cookieNamePath, t, dataToStore);
		}
	}

	public static void clear(String cookieNamePath) {

		if (cacheDelegate != null) {
			cacheDelegate.clear(cookieNamePath);
		}
	}

	// Cache delegate register/unregister methods
	public boolean registerCacheDelegate(OPCacheDelegate delegate) {
		if (delegate == null) {
			return false;
		}

		// Store the delegate object
		cacheDelegate = delegate;
		return true;
	}

	public void unregisterCacheDelegate(OPCacheDelegate delegate) {
		cacheDelegate = null;
	}

	// ///////////////////////////////////////////////////////////////////
	// IDENTITY LOOKUP DELEGATE GLUE
	// ///////////////////////////////////////////////////////////////////
	// OPIdentityLookupDelegate support
	private static OPIdentityLookup mIdentityLookup;
	static OPIdentityLookupDelegate identityLookupDelegate;

	public static void onIdentityLookupCompleted() {

		if (mIdentityLookup != null && identityLookupDelegate != null) {
			identityLookupDelegate.onIdentityLookupCompleted(mIdentityLookup);
		}

	}

	// Identity lookup delegate register/unregister methods
	public boolean registerIdentityLookupDelegate(
			OPIdentityLookup identityLookup, OPIdentityLookupDelegate delegate) {
		if (identityLookup == null || delegate == null) {
			return false;
		}

		if (mIdentityLookup == null) {
			mIdentityLookup = identityLookup;
		}
		identityLookupDelegate = delegate;

		return true;
	}

	public void unregisterIdentityLookupDelegate(
			OPIdentityLookupDelegate delegate) {
		mIdentityLookup = null;
		identityLookupDelegate = null;

	}

	// ///////////////////////////////////////////////////////////////////
	// MEDIA ENGINE DELEGATE GLUE
	// ///////////////////////////////////////////////////////////////////
	// OPMediaEngineDelegate support
	private OPMediaEngine mMediaEngine;
	ArrayList<OPMediaEngineDelegate> mediaEngineDelegates = new ArrayList<OPMediaEngineDelegate>();

	public void onMediaEngineAudioRouteChanged(int audioRoute) {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates) {
			if (mMediaEngine != null && delegate != null) {
				delegate.onMediaEngineAudioRouteChanged(OutputAudioRoutes
						.values()[audioRoute]);
			}
		}
	}

	public void onMediaEngineAudioSessionInterruptionBegan() {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates) {
			if (mMediaEngine != null && delegate != null) {
				delegate.onMediaEngineAudioSessionInterruptionBegan();
			}
		}
	}

	public void onMediaEngineAudioSessionInterruptionEnded() {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates) {
			if (mMediaEngine != null && delegate != null) {
				delegate.onMediaEngineAudioSessionInterruptionEnded();
			}
		}
	}

	public void onMediaEngineFaceDetected() {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates) {
			if (mMediaEngine != null && delegate != null) {
				delegate.onMediaEngineFaceDetected();
			}
		}
	}

	public void onMediaEngineVideoCaptureRecordStopped() {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates) {
			if (mMediaEngine != null && delegate != null) {
				delegate.onMediaEngineVideoCaptureRecordStopped();
			}
		}
	}

	// Media engine delegate register/unregister methods
	public boolean registerMediaEngineDelegate(OPMediaEngine mediaEngine,
			OPMediaEngineDelegate delegate) {
		if (mediaEngine == null || delegate == null) {
			return false;
		}

		if (mMediaEngine == null) {
			mMediaEngine = mediaEngine;
		}

		// Store the delegate object
		this.mediaEngineDelegates.add(delegate);

		return true;
	}

	public void unregisterMediaEngineDelegate(OPMediaEngineDelegate delegate) {
		mMediaEngine = null;
		this.mediaEngineDelegates.remove(delegate);

	}

	// ///////////////////////////////////////////////////////////////////
	// SETTINGS DELEGATE GLUE
	// ///////////////////////////////////////////////////////////////////
	// OPCacheDelegate support
	// private OPCache mCache;
	static OPSettingsDelegate settingsDelegate;

	public static String getString(String key) {

		String ret = null;
		if (settingsDelegate != null) {
			ret = settingsDelegate.getString(key);
		}
		return ret;
	}

	public static long getInt(String key) {
		long ret = 0;
		if (settingsDelegate != null) {
			ret = settingsDelegate.getInt(key);
		}
		return ret;
	}

	public static long getUInt(String key) {
		long ret = 0;
		if (settingsDelegate != null) {
			ret = settingsDelegate.getUInt(key);
		}
		return ret;
	}

	public static boolean getBool(String key) {
		boolean ret = false;
		if (settingsDelegate != null) {
			ret = settingsDelegate.getBool(key);
		}
		return ret;
	}

	public static float getFloat(String key) {
		float ret = 0;
		if (settingsDelegate != null) {
			ret = settingsDelegate.getFloat(key);
		}
		return ret;
	}

	public static double getUIntSetting(String key) {
		double ret = 0;
		if (settingsDelegate != null) {
			ret = settingsDelegate.getDouble(key);
		}
		return ret;
	}

	public static void setString(String key, String value) {

		if (settingsDelegate != null) {
			settingsDelegate.setString(key, value);
		}
	}

	public static void setInt(String key, long value) {

		if (settingsDelegate != null) {
			settingsDelegate.setInt(key, value);
		}
	}

	public static void setUInt(String key, long value) {

		if (settingsDelegate != null) {
			settingsDelegate.setUInt(key, value);
		}
	}

	public static void setBool(String key, boolean value) {

		if (settingsDelegate != null) {
			settingsDelegate.setBool(key, value);
		}
	}

	public static void setFloat(String key, float value) {

		if (settingsDelegate != null) {
			settingsDelegate.setFloat(key, value);
		}
	}

	public static void setDouble(String key, double value) {

		if (settingsDelegate != null) {
			settingsDelegate.setDouble(key, value);
		}

	}

	public static void clearSettings(String key) {

		if (settingsDelegate != null) {
			settingsDelegate.clear(key);
		}

	}

	// Settings delegate register/unregister methods
	public boolean registerSettingsDelegate(OPSettingsDelegate delegate) {
		if (delegate == null) {
			return false;
		}

		// Store the delegate object
		settingsDelegate = delegate;

		return true;
	}

	public void unregisterSettingsDelegate(OPSettingsDelegate delegate) {
		settingsDelegate = null;
	}

	public void registerDatastoreDelegate(OPDatastoreDelegate datastoreDelegate) {
		this.sDatastoreDelegate = datastoreDelegate;
	}
}
