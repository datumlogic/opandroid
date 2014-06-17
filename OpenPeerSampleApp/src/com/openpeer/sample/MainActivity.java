package com.openpeer.sample;

import android.os.Bundle;

import com.openpeer.app.OPDataManager;
import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.sample.contacts.ContactsFragment;
import com.openpeer.sample.util.NetworkUtil;

public class MainActivity extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		OPAccount account = OPDataManager.getInstance().getSharedAccount();
		String reloginInfo = OPDataManager.getInstance().getReloginInfo();
		// Launching app so account hasn't been constructed, and login process
		// hasn't started
		if (account == null) {
//			if (reloginInfo == null) {
				doLogin();
//			} else {
//				doRelogin();
//			}
		} else if (account.getState(0, "") == AccountStates.AccountState_Shutdown
				&& reloginInfo != null && NetworkUtil.isConnected()) {
			doRelogin();
		} else {
			// present UI
			this.setContentFragment(ContactsFragment.newInstance());
		}
	}

	void doLogin() {
		LoginFragment loginFragment = LoginFragment.newInstance();
		this.setContentFragment(loginFragment);
	}

	void doRelogin() {
	}
}
