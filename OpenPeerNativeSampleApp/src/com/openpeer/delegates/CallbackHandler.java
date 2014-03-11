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
	private OPAccount mAccount;
	ArrayList<OPAccountDelegate> accountDelegates = new ArrayList<OPAccountDelegate> ();

	//Account Delegate glue methods
	public void onAccountStateChanged(int state) {
		for (OPAccountDelegate delegate : accountDelegates)
		{
			if (mAccount != null && delegate != null)
			{
				delegate.onAccountStateChanged(mAccount, AccountStates.values()[state] );
			}
		}
	}

	public void onAccountAssociatedIdentitiesChanged() {

		for (OPAccountDelegate delegate : accountDelegates)
		{
			if (mAccount != null && delegate != null)
			{
				delegate.onAccountAssociatedIdentitiesChanged(mAccount);
			}
		}
		
	}

	public void onAccountPendingMessageForInnerBrowserWindowFrame() {

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
	ArrayList<OPStackDelegate> stackDelegates = new ArrayList<OPStackDelegate> ();
	
	public void onStackShutdown() {
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
	private OPIdentity mIdentity;
	ArrayList<OPIdentityDelegate> identityDelegates = new ArrayList<OPIdentityDelegate> ();
	
	public void onIdentityStateChanged(int state) {
		
		for (OPIdentityDelegate delegate : identityDelegates)
		{
			if (mIdentity != null && delegate != null)
			{
				delegate.onIdentityStateChanged(mIdentity, IdentityStates.values()[state] );
			}
		}
	}

	public void onIdentityPendingMessageForInnerBrowserWindowFrame() {
		for (OPIdentityDelegate delegate : identityDelegates)
		{
			if (mIdentity != null && delegate != null)
			{
				delegate.onIdentityPendingMessageForInnerBrowserWindowFrame(mIdentity);
			}
		}
		
	}
	
	public void onIdentityRolodexContactsDownloaded() {
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
	private OPCall mCall;
	ArrayList<OPCallDelegate> callDelegates = new ArrayList<OPCallDelegate> ();

	public void onCallStateChanged(int state) {

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
	private OPConversationThread mConversationThread;
	ArrayList<OPConversationThreadDelegate> conversationThreadDelegates = new ArrayList<OPConversationThreadDelegate> ();
	
	public void onConversationThreadNew() {
		//TODO: Fix for creating new conversation thread object 
		for (OPConversationThreadDelegate delegate : conversationThreadDelegates)
		{
			if (mConversationThread == null && delegate != null)
			{
				mConversationThread = new OPConversationThread();
				delegate.onConversationThreadNew(mConversationThread);
			}
			else if (mConversationThread != null && delegate != null)
			{
				delegate.onConversationThreadNew(mConversationThread);
			}
			else
			{
				Log.e("openpeer-android-sdk", "No conversation thread listener available!!!");
			}
		}
	}
	
	public void onConversationThreadContactsChanged() { 
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
	
	public void onConversationThreadContactStateChanged(OPContact contact, int state) { 
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
	
	public void onConversationThreadMessage(String messageID) { 
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
	
	public void onConversationThreadMessageDeliveryStateChanged(String messageID, int state) { 
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
	
	public void onConversationThreadPushMessage(String messageID, OPContact contact) { 
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
	//private OPCache mCache;
	ArrayList<OPCacheDelegate> cacheDelegates = new ArrayList<OPCacheDelegate> ();
	
	public void fetch(String cookieNamePath) {

		for (OPCacheDelegate delegate : cacheDelegates)
		{
			if (delegate != null)
			{
				delegate.fetch(cookieNamePath );
			}
		}
	}
	
	public void store(String cookieNamePath, int time, String dataToStore) {
		//TODO: Fix time
		for (OPCacheDelegate delegate : cacheDelegates)
		{
			if (delegate != null)
			{
				Time t = new Time();
				delegate.store(cookieNamePath, t, dataToStore);
			}
		}
	}
	
	public void clear(String cookieNamePath) {

		for (OPCacheDelegate delegate : cacheDelegates)
		{
			if (delegate != null)
			{
				delegate.clear(cookieNamePath);
			}
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
		this.cacheDelegates.add(delegate);

		return true;
	}

	public void unregisterCacheDelegate(OPCacheDelegate delegate)
	{
		this.cacheDelegates.remove(delegate);

	}
	

	/////////////////////////////////////////////////////////////////////
	// IDENTITY LOOKUP DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPIdentityLookupDelegate support
	private OPIdentityLookup mIdentityLookup;
	ArrayList<OPIdentityLookupDelegate> identityLookupDelegates = new ArrayList<OPIdentityLookupDelegate> ();

	public void onIdentityLookupCompleted() {

		for (OPIdentityLookupDelegate delegate : identityLookupDelegates)
		{
			if (mIdentityLookup != null && delegate != null)
			{
				delegate.onIdentityLookupCompleted(mIdentityLookup );
			}
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

		// Store the delegate object
		this.identityLookupDelegates.add(delegate);

		return true;
	}

	public void unregisterCallDelegate(OPIdentityLookupDelegate delegate)
	{
		mIdentityLookup = null;
		this.identityLookupDelegates.remove(delegate);

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
	// CACHE DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPCacheDelegate support
	//private OPCache mCache;
	ArrayList<OPSettingsDelegate> settingsDelegates = new ArrayList<OPSettingsDelegate> ();

//	public String getString(String key) {
//
//		for (OPSettingsDelegate delegate : settingsDelegates)
//		{
//			if (delegate != null)
//			{
//				delegate.getString(key);
//			}
//		}
//	}
//
//	public long getInt(String key) {
//		//TODO: Fix time
//		for (OPSettingsDelegate delegate : settingsDelegates)
//		{
//			if (delegate != null)
//			{
//				delegate.getInt(key);
//			}
//		}
//	}
//
//	public long getUInt(String key) {
//
//		for (OPSettingsDelegate delegate : settingsDelegates)
//		{
//			if (delegate != null)
//			{
//				delegate.getUInt(key);
//			}
//		}
//	}
	public void setString(
			String key,
			String value
			) {

		for (OPSettingsDelegate delegate : settingsDelegates)
		{
			if (delegate != null)
			{
				delegate.setString(key, value);
			}
		}
	}

	public void setInt(String key,
			long value
			) {
		//TODO: Fix time
		for (OPSettingsDelegate delegate : settingsDelegates)
		{
			if (delegate != null)
			{
				delegate.setInt(key, value);
			}
		}
	}

	public void setUInt(String key,
			long value
			) {

		for (OPSettingsDelegate delegate : settingsDelegates)
		{
			if (delegate != null)
			{
				delegate.setUInt(key, value);
			}
		}
	}
	public void setBool(
			String key,
			boolean value
			) {

		for (OPSettingsDelegate delegate : settingsDelegates)
		{
			if (delegate != null)
			{
				delegate.setBool(key, value);
			}
		}
	}

	public void setFloat(
			String key,
			float value
			) {
		//TODO: Fix time
		for (OPSettingsDelegate delegate : settingsDelegates)
		{
			if (delegate != null)
			{
				delegate.setFloat(key, value);
			}
		}
	}

	public void setDouble(
			String key,
			double value
			) {

		for (OPSettingsDelegate delegate : settingsDelegates)
		{
			if (delegate != null)
			{
				delegate.setDouble(key, value);
			}
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
		this.settingsDelegates.add(delegate);

		return true;
	}

	public void unregisterSettingsDelegate(OPSettingsDelegate delegate)
	{
		this.settingsDelegates.remove(delegate);

	}
	////
	public static void onJniCallback()
	{
		int i = 0;
		i++;
	}

}
