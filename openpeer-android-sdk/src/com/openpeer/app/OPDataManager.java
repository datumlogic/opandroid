package com.openpeer.app;

import com.openpeer.javaapi.OPAccount;

public class OPDataManager {
	private static OPDataManager instance;
	private OPAccount mAccount;

	public static OPDataManager getInstance() {
		if (instance == null) {
			instance = new OPDataManager();
		}
		return instance;
	}

	public void setSharedAccount(OPAccount account) {
		mAccount = account;
	}

	public OPAccount getSharedAccount() {
		if (mAccount == null) {
			mAccount = new OPAccount();
		}
		return mAccount;
	}

}
