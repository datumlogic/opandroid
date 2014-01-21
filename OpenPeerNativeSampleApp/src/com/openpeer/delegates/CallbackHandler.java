package com.openpeer.delegates;

import java.util.ArrayList;

import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.ContactStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackDelegate;

public class CallbackHandler{
	
	/////////////////////////////////////////////////////////////////////
	// STACK DELEGATE GLUE
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

	public void onAccountAssociatedIdentitiesChanged(OPAccount account) {

		for (OPAccountDelegate delegate : accountDelegates)
		{
			if (mAccount != null && delegate != null)
			{
				delegate.onAccountAssociatedIdentitiesChanged(mAccount);
			}
		}
		
	}

	public void onAccountPendingMessageForInnerBrowserWindowFrame(
			OPAccount account) {

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
	//OPIdentityDelegate support
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

	public void unregisterIdentityDelegate(OPCallDelegate delegate)
	{
		mCall = null;
		this.callDelegates.remove(delegate);

	}
	
	/////////////////////////////////////////////////////////////////////
	// CONVERSATION THREAD DELEGATE GLUE
	/////////////////////////////////////////////////////////////////////
	//OPIdentityDelegate support
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
	////
	public static void onJniCallback()
	{
		int i = 0;
		i++;
	}

}
