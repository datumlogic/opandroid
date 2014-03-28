package com.openpeer.delegates;

import java.util.ArrayList;

import android.text.format.Time;
import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.ContactStates;
import com.openpeer.javaapi.MessageDeliveryStates;
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

public class CallbackHandler{

	/////////////////////////////////////////////////////////////////////
	// ACCOUNT DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPAccountDelegate support
	private static OPAccount mAccount;
	static ArrayList<OPAccountDelegate> accountDelegates = new ArrayList<OPAccountDelegate> ();

	//Account Delegate glue methods
	public static void onAccountStateChanged(int state) {
		for (OPAccountDelegate delegate : accountDelegates)
		{
			if (mAccount != null && delegate != null)
			{
				delegate.onAccountStateChanged(mAccount, AccountStates.values()[state] );
			}
		}
	}

	public static void onAccountAssociatedIdentitiesChanged() {

		for (OPAccountDelegate delegate : accountDelegates)
		{
			if (mAccount != null && delegate != null)
			{
				delegate.onAccountAssociatedIdentitiesChanged(mAccount);
			}
		}

	}

	public static void onAccountPendingMessageForInnerBrowserWindowFrame() {

		for (OPAccountDelegate delegate : accountDelegates)
		{
			if (mAccount != null && delegate != null)
			{
				delegate.onAccountPendingMessageForInnerBrowserWindowFrame(mAccount);
			}
		}

	}

	//Account delegate register/unregister methods
	public boolean registerAccountDelegate(OPAccount account, OPAccountDelegate delegate)
	{
		if (account == null || delegate == null)
		{
			return false;
		}

		if (mAccount == null)
		{
			mAccount = account;
		}

		// Store the delegate object
		this.accountDelegates.add(delegate);

		return true;
	}

	public void unregisterAccountDelegate(OPAccountDelegate delegate)
	{
		mAccount = null;
		this.accountDelegates.remove(delegate);

	}

	/////////////////////////////////////////////////////////////////////
	// STACK DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPStackDelegate support
	//private OPStack mStack;
	static ArrayList<OPStackDelegate> stackDelegates = new ArrayList<OPStackDelegate> ();

	public static void onStackShutdown() {
		for (OPStackDelegate delegate : stackDelegates)
		{
			if (delegate != null)
			{
				delegate.onStackShutdown();
			}
		}

	}

	//Stack delegate register/unregister methods
	public boolean registerStackDelegate(OPStackDelegate delegate)
	{
		if (delegate == null)
		{
			return false;
		}

		// Store the delegate object
		this.stackDelegates.add(delegate);

		return true;
	}

	public void unregisterStackDelegate(OPStackDelegate delegate)
	{
		this.stackDelegates.remove(delegate);

	}


	/////////////////////////////////////////////////////////////////////
	// IDENTITY DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPIdentityDelegate support
	private static OPIdentity mIdentity;
	static ArrayList<OPIdentityDelegate> identityDelegates = new ArrayList<OPIdentityDelegate> ();

	public static void onIdentityStateChanged(int state) {

		for (OPIdentityDelegate delegate : identityDelegates)
		{
			if (mIdentity != null && delegate != null)
			{
				delegate.onIdentityStateChanged(mIdentity, IdentityStates.values()[state] );
			}
		}
	}

	public static void onIdentityPendingMessageForInnerBrowserWindowFrame() {
		for (OPIdentityDelegate delegate : identityDelegates)
		{
			if (mIdentity != null && delegate != null)
			{
				delegate.onIdentityPendingMessageForInnerBrowserWindowFrame(mIdentity);
			}
		}

	}

	public static void onIdentityRolodexContactsDownloaded() {
		for (OPIdentityDelegate delegate : identityDelegates)
		{
			if (mIdentity != null && delegate != null)
			{
				delegate.onIdentityRolodexContactsDownloaded(mIdentity);
			}
		}

	}

	//Identity delegate register/unregister methods
	public boolean registerIdentityDelegate(OPIdentity identity, OPIdentityDelegate delegate)
	{
		if (identity == null || delegate == null)
		{
			return false;
		}

		if (mIdentity == null)
		{
			mIdentity = identity;
		}

		// Store the delegate object
		this.identityDelegates.add(delegate);

		return true;
	}

	public void unregisterIdentityDelegate(OPIdentityDelegate delegate)
	{
		mIdentity = null;
		this.identityDelegates.remove(delegate);

	}

	/////////////////////////////////////////////////////////////////////
	// CALL DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPCallDelegate support
	private static OPCall mCall;
	static ArrayList<OPCallDelegate> callDelegates = new ArrayList<OPCallDelegate> ();

	public static void onCallStateChanged(int state) {

		for (OPCallDelegate delegate : callDelegates)
		{
			if (mCall != null && delegate != null)
			{
				delegate.onCallStateChanged(mCall, CallStates.values()[state] );
			}
		}
	}

	//Call delegate register/unregister methods
	public boolean registerCallDelegate(OPCall call, OPCallDelegate delegate)
	{
		if (call == null || delegate == null)
		{
			return false;
		}

		if (mCall == null)
		{
			mCall = call;
		}

		// Store the delegate object
		this.callDelegates.add(delegate);

		return true;
	}

	public void unregisterCallDelegate(OPCallDelegate delegate)
	{
		mCall = null;
		this.callDelegates.remove(delegate);

	}

	/////////////////////////////////////////////////////////////////////
	// CONVERSATION THREAD DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPConversationThreadDelegate support
	private static OPConversationThread mConversationThread;
	static ArrayList<OPConversationThreadDelegate> conversationThreadDelegates = new ArrayList<OPConversationThreadDelegate> ();

	public static void onConversationThreadNew(OPConversationThread convThread) {
		//TODO: Fix for creating new conversation thread object 
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates)
		{
			if (convThread != null && delegate != null)
			{
				mConversationThread = convThread;
				delegate.onConversationThreadNew(mConversationThread);
			}
			else
			{
				Log.e("openpeer-android-sdk", "No conversation thread listener available!!!");
			}
		}
	}

	public static void onConversationThreadContactsChanged() { 
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates)
		{
			if (mConversationThread != null && delegate != null)
			{
				delegate.onConversationThreadContactsChanged(mConversationThread);
			}
			else
			{
				Log.e("openpeer-android-sdk", "No conversation thread or listener available!!!");
			}
		}
	}

	public static void onConversationThreadContactStateChanged(OPContact contact, int state) { 
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates)
		{
			if (mConversationThread != null && delegate != null)
			{
				delegate.onConversationThreadContactStateChanged(mConversationThread, contact, ContactStates.values()[state]);
			}
			else
			{
				Log.e("openpeer-android-sdk", "No conversation thread or listener available!!!");
			}
		}
	}

	public static void onConversationThreadMessage(String messageID) { 
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates)
		{
			if (mConversationThread != null && delegate != null)
			{
				delegate.onConversationThreadMessage(mConversationThread, messageID);
			}
			else
			{
				Log.e("openpeer-android-sdk", "No conversation thread or listener available!!!");
			}
		}
	}

	public static void onConversationThreadMessageDeliveryStateChanged(String messageID, int state) { 
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates)
		{
			if (mConversationThread != null && delegate != null)
			{
				delegate.onConversationThreadMessageDeliveryStateChanged(mConversationThread, messageID, MessageDeliveryStates.values()[state]);
			}
			else
			{
				Log.e("openpeer-android-sdk", "No conversation thread or listener available!!!");
			}
		}
	}

	public static void onConversationThreadPushMessage(String messageID, OPContact contact) { 
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates)
		{
			if (mConversationThread != null && delegate != null)
			{
				delegate.onConversationThreadPushMessage(mConversationThread, messageID, contact);
			}
			else
			{
				Log.e("openpeer-android-sdk", "No conversation thread or listener available!!!");
			}
		}
	}

	//Conversation Thread delegate register/unregister methods
	public boolean registerConversationThreadDelegate(OPConversationThread conversationThread, OPConversationThreadDelegate delegate)
	{
		if (conversationThread == null || delegate == null)
		{
			return false;
		}

		if (mConversationThread == null)
		{
			mConversationThread = conversationThread;
		}

		// Store the delegate object
		this.conversationThreadDelegates.add(delegate);

		return true;
	}

	public void unregisterConversationThreadDelegate(OPCallDelegate delegate)
	{
		mConversationThread = null;
		this.conversationThreadDelegates.remove(delegate);

	}

	/////////////////////////////////////////////////////////////////////
	// CACHE DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPCacheDelegate support
	static OPCacheDelegate cacheDelegate;

	public static String fetch (String cookieNamePath)
	{
		String ret = "";
		if (cacheDelegate != null)
		{
			ret = cacheDelegate.fetch(cookieNamePath);
		}
		return ret;
	}

	public static void store(String cookieNamePath, int time, String dataToStore) {

		if (cacheDelegate != null)
		{
			Time t = new Time();
			cacheDelegate.store(cookieNamePath, t, dataToStore);
		}
	}

	public static void clear(String cookieNamePath) {

		if (cacheDelegate != null)
		{
			cacheDelegate.clear(cookieNamePath);
		}
	}

	//Cache delegate register/unregister methods
	public boolean registerCacheDelegate(OPCacheDelegate delegate)
	{
		if (delegate == null)
		{
			return false;
		}

		// Store the delegate object
		cacheDelegate = delegate;
		return true;
	}

	public void unregisterCacheDelegate(OPCacheDelegate delegate)
	{
		cacheDelegate = null;
	}


	/////////////////////////////////////////////////////////////////////
	// IDENTITY LOOKUP DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPIdentityLookupDelegate support
	private static OPIdentityLookup mIdentityLookup;
	static OPIdentityLookupDelegate identityLookupDelegate;

	public static void onIdentityLookupCompleted() {

		if (mIdentityLookup != null && identityLookupDelegate != null)
		{
			identityLookupDelegate.onIdentityLookupCompleted(mIdentityLookup );
		}

	}

	//Identity lookup delegate register/unregister methods
	public boolean registerCallDelegate(OPIdentityLookup identityLookup, OPIdentityLookupDelegate delegate)
	{
		if (identityLookup == null || delegate == null)
		{
			return false;
		}

		if (mIdentityLookup == null)
		{
			mIdentityLookup = identityLookup;
		}
		identityLookupDelegate = delegate;

		return true;
	}

	public void unregisterCallDelegate(OPIdentityLookupDelegate delegate)
	{
		mIdentityLookup = null;
		identityLookupDelegate = null;

	}

	/////////////////////////////////////////////////////////////////////
	// MEDIA ENGINE DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPMediaEngineDelegate support
	private OPMediaEngine mMediaEngine;
	ArrayList<OPMediaEngineDelegate> mediaEngineDelegates = new ArrayList<OPMediaEngineDelegate> ();

	public void onMediaEngineAudioRouteChanged(int audioRoute) {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates)
		{
			if (mMediaEngine != null && delegate != null)
			{
				delegate.onMediaEngineAudioRouteChanged( OutputAudioRoutes.values()[audioRoute]);
			}
		}
	}

	public void onMediaEngineAudioSessionInterruptionBegan() {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates)
		{
			if (mMediaEngine != null && delegate != null)
			{
				delegate.onMediaEngineAudioSessionInterruptionBegan();
			}
		}
	}

	public void onMediaEngineAudioSessionInterruptionEnded() {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates)
		{
			if (mMediaEngine != null && delegate != null)
			{
				delegate.onMediaEngineAudioSessionInterruptionEnded();
			}
		}
	}

	public void onMediaEngineFaceDetected() {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates)
		{
			if (mMediaEngine != null && delegate != null)
			{
				delegate.onMediaEngineFaceDetected();
			}
		}
	}

	public void onMediaEngineVideoCaptureRecordStopped() {

		for (OPMediaEngineDelegate delegate : mediaEngineDelegates)
		{
			if (mMediaEngine != null && delegate != null)
			{
				delegate.onMediaEngineVideoCaptureRecordStopped();
			}
		}
	}

	//Media engine delegate register/unregister methods
	public boolean registerMediaEngineDelegate(OPMediaEngine mediaEngine, OPMediaEngineDelegate delegate)
	{
		if (mediaEngine == null || delegate == null)
		{
			return false;
		}

		if (mMediaEngine == null)
		{
			mMediaEngine = mediaEngine;
		}

		// Store the delegate object
		this.mediaEngineDelegates.add(delegate);

		return true;
	}

	public void unregisterMediaEngineDelegate(OPMediaEngineDelegate delegate)
	{
		mMediaEngine = null;
		this.mediaEngineDelegates.remove(delegate);

	}


	/////////////////////////////////////////////////////////////////////
	// SETTINGS DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPCacheDelegate support
	//private OPCache mCache;
	static OPSettingsDelegate settingsDelegate;

	public static String getString(String key) 
	{

		String ret = null;
		if (settingsDelegate != null)
		{
			ret = settingsDelegate.getString(key);
		}
		return ret;
	}

	public static long getInt(String key) 
	{
		long ret = 0;
		if (settingsDelegate != null)
		{
			ret = settingsDelegate.getInt(key);
		}
		return ret;
	}

	public static long getUInt(String key) 
	{
		long ret = 0;
		if (settingsDelegate != null)
		{
			ret = settingsDelegate.getUInt(key);
		}
		return ret;
	}

	public static boolean getBool(String key) 
	{
		boolean ret = false;
		if (settingsDelegate != null)
		{
			ret = settingsDelegate.getBool(key);
		}
		return ret;
	}

	public static float getFloat(String key) 
	{
		float ret = 0;
		if (settingsDelegate != null)
		{
			ret = settingsDelegate.getFloat(key);
		}
		return ret;
	}

	public static double getUIntSetting(String key) 
	{
		double ret = 0;
		if (settingsDelegate != null)
		{
			ret = settingsDelegate.getDouble(key);
		}
		return ret;
	}

	public static void setString(
			String key,
			String value
			) {

		if (settingsDelegate != null)
		{
			settingsDelegate.setString(key, value);
		}
	}

	public static void setInt(String key,
			long value
			) {

		if (settingsDelegate != null)
		{
			settingsDelegate.setInt(key, value);
		}
	}

	public static void setUInt(String key,
			long value
			) {

		if (settingsDelegate != null)
		{
			settingsDelegate.setUInt(key, value);
		}
	}
	public static void setBool(
			String key,
			boolean value
			) {

		if (settingsDelegate != null)
		{
			settingsDelegate.setBool(key, value);
		}
	}

	public static void setFloat(
			String key,
			float value
			) {

		if (settingsDelegate != null)
		{
			settingsDelegate.setFloat(key, value);
		}
	}

	public static void setDouble(
			String key,
			double value
			) 
	{

		if (settingsDelegate != null)
		{
			settingsDelegate.setDouble(key, value);
		}

	}

	public static void clearSettings(
			String key
			) 
	{

		if (settingsDelegate != null)
		{
			settingsDelegate.clear(key);
		}

	}

	//Settings delegate register/unregister methods
	public boolean registerSettingsDelegate(OPSettingsDelegate delegate)
	{
		if (delegate == null)
		{
			return false;
		}

		// Store the delegate object
		settingsDelegate = delegate;

		return true;
	}

	public void unregisterSettingsDelegate(OPSettingsDelegate delegate)
	{
		settingsDelegate = null;
	}
}
