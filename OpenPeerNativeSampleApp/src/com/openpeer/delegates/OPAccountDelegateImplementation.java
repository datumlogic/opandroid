package com.openpeer.delegates;

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
			LoginManager.loadOuterFrame();
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
		
	}

}
