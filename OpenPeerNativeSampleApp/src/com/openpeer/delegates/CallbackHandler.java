package com.openpeer.delegates;

import java.util.ArrayList;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackDelegate;

public class CallbackHandler{
	
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
	//OPAccountDelegate support
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
	//OPAccountDelegate support
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
	
	public static void onJniCallback()
	{
		int i = 0;
		i++;
	}

}
