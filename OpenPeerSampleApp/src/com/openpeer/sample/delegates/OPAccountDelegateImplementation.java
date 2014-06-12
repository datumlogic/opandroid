package com.openpeer.sample.delegates;

import java.util.List;



import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.sample.login.LoginManager;

public class OPAccountDelegateImplementation extends OPAccountDelegate {

	@Override
	public void onAccountStateChanged(OPAccount account, AccountStates state) {
		// TODO Auto-generated method stub
		switch (state){
		case AccountState_WaitingForAssociationToIdentity:
			break;
		case AccountState_WaitingForBrowserWindowToBeLoaded:
			LoginManager.loadOuterFrame();
			break;
		case AccountState_WaitingForBrowserWindowToBeMadeVisible:
			LoginManager.mAccount.notifyBrowserWindowVisible();
			break;
		case AccountState_WaitingForBrowserWindowToClose:
			LoginManager.mAccount.notifyBrowserWindowClosed();
			break;
		case AccountState_Ready:
			Log.w("JNI", "READY !!!!!!!!!!!!");
			LoginManager.onAccountStateReady(account);
			break;
			//LoginManager.loadOuterFrame();
		}
			
	}

	@Override
	public void onAccountAssociatedIdentitiesChanged(OPAccount account) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccountPendingMessageForInnerBrowserWindowFrame(
			OPAccount account) {
		// TODO Auto-generated method stub
		LoginManager.pendingMessageForNamespaceGrantInnerFrame();
	}

}
