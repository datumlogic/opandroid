package com.openpeer.delegates;

import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.openpeernativesampleapp.LoginManager;

public class OPAccountDelegateImplementation extends OPAccountDelegate {

	@Override
	public void onAccountStateChanged(OPAccount account, AccountStates state) {
		// TODO Auto-generated method stub
		switch (state){
		case AccountState_WaitingForAssociationToIdentity:
			break;
		case AccountState_WaitingForBrowserWindowToBeLoaded:
			LoginManager.startAccountLogin();
			break;
		case AccountState_WaitingForBrowserWindowToBeMadeVisible:
			LoginManager.mAccount.notifyBrowserWindowVisible();
			break;
		case AccountState_WaitingForBrowserWindowToClose:
			LoginManager.mAccount.notifyBrowserWindowClosed();
			break;
		case AccountState_Ready:
			Log.w("JNI", "READY !!!!!!!!!!!!");
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
