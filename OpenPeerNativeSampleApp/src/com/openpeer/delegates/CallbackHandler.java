package com.openpeer.delegates;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPStackDelegate;

public class CallbackHandler{

	public static void onJniCallback()
	{
		int i = 0;
		i++;
	}
	//Account Delegate static methods
	public static void onAccountStateChanged(int state) {
		// TODO Auto-generated method stub
		if (mAccount != null && mAccountDelegate != null)
		{
			mAccountDelegate.onAccountStateChanged(mAccount, AccountStates.values()[state] );
		}
		
	}

	public static void onAccountAssociatedIdentitiesChanged(OPAccount account) {
		// TODO Auto-generated method stub
		
	}

	public static void onAccountPendingMessageForInnerBrowserWindowFrame(
			OPAccount account) {
		// TODO Auto-generated method stub
		
	}
	
	//Account delegate register/unregister methods
	public static boolean registerAccountDelegate(OPAccount account, OPAccountDelegate delegate)
	{
		if (account == null || delegate == null)
		{
			return false;
		}
		
		if (mAccount == null)
		{
			mAccount = account;
		}
		
		if (mAccountDelegate == null)
		{
			mAccountDelegate = delegate;
		}
		
		return true;
	}
	
	public static void unregisterAccountDelegate()
	{
		mAccount = null;
		mAccountDelegate = null;
		
	}

	public static void onStackShutdown() {
		// TODO Auto-generated method stub
		
	}
	
	//Stack delegate register/unregister methods
	public static boolean registerStackDelegate(OPStackDelegate delegate)
	{
		if (delegate == null)
		{
			return false;
		}
		
		if (mStackDelegate == null)
		{
			mStackDelegate = delegate;
		}
		
		return true;
	}
	
	public static void unregisterStackDelegate()
	{
		mStackDelegate = null;
		
	}

	public static void onIdentityStateChanged(int state) {
		// TODO Auto-generated method stub
		if (mIdentity != null && mIdentityDelegate != null)
		{
			mIdentityDelegate.onIdentityStateChanged(mIdentity, IdentityStates.values()[state] );
		}
		
	}

	public static void onIdentityPendingMessageForInnerBrowserWindowFrame() {
		// TODO Auto-generated method stub
		
	}
	
	//Identity delegate register/unregister methods
	public static boolean registerIdentityDelegate(OPIdentity identity, OPIdentityDelegate delegate)
	{
		if (identity == null || delegate == null)
		{
			return false;
		}
		
		if (mIdentity == null)
		{
			mIdentity = identity;
		}
		
		if (mIdentityDelegate == null)
		{
			mIdentityDelegate = delegate;
		}
		
		return true;
	}
	
	public static void unregisterIdentityDelegate()
	{
		mIdentity = null;
		mIdentityDelegate = null;
		
	}
	
	private static OPAccountDelegate mAccountDelegate;
	private static OPAccount mAccount;
	
	private static OPStackDelegate mStackDelegate;
	
	private static OPIdentityDelegate mIdentityDelegate;
	private static OPIdentity mIdentity;
}
