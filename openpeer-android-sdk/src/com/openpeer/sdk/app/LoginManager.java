package com.openpeer.sdk.app;

import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.sdk.delegates.OPAccountDelegateImplementation;
import com.openpeer.sdk.delegates.OPIdentityDelegateImplementation;

public class LoginManager {
	private static LoginUIListener mListener;

	private static LoginManager instance;

	public static LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	private LoginManager() {
	}

	public void login(LoginUIListener listener,
			OPCallDelegate callDelegate,
			OPConversationThreadDelegate conversationThreadDelegate) {
		mListener = listener;

		OPAccountDelegateImplementation accountDelegate = OPAccountDelegateImplementation.getInstance();
		accountDelegate.bind(mListener);
		OPAccount account = OPAccount.login(accountDelegate, conversationThreadDelegate, callDelegate, false);
		OPDataManager.getInstance().setSharedAccount(account);
		startIdentityLogin();
	}

	public void relogin(LoginUIListener listener,
			OPCallDelegate callDelegate,
			OPConversationThreadDelegate conversationThreadDelegate,
			String reloginInfo) {
		mListener = listener;
		OPAccountDelegateImplementation accountDelegate = OPAccountDelegateImplementation.getInstance();
		accountDelegate.bind(mListener);
		OPAccount account = OPAccount.relogin(accountDelegate, conversationThreadDelegate, callDelegate, reloginInfo);

		OPDataManager.getInstance().setSharedAccount(account);
	}

	public void startIdentityLogin() {
		OPAccount account = OPDataManager.getInstance().getSharedAccount();

		OPIdentity identity = new OPIdentity();
		OPIdentityLoginWebview mIdentityLoginWebView = mListener.getIdentityWebview(null);

		OPIdentityDelegateImplementation identityDelegate = OPIdentityDelegateImplementation.getInstance(null);
		identityDelegate.bindListener(mListener);
		identityDelegate.setWebview(mIdentityLoginWebView);

		identity = OPIdentity.login(account, identityDelegate);
		mIdentityLoginWebView.getClient().setIdentity(identity);
	}

	public void onAccountStateReady(OPAccount account) {

		OPDataManager.getInstance().saveAccount();

		List<OPIdentity> identities = account.getAssociatedIdentities();
		if (identities.size() == 0) {
			Log.d("TODO", "Account test FAILED identities emppty ");

			return;
		}

		for (OPIdentity identity : identities) {
			if (!identity.isDelegateAttached()) {
				OPIdentityLoginWebview webview = mListener.getIdentityWebview(identity);
				webview.getClient().setIdentity(identity);

				OPIdentityDelegateImplementation identityDelegate = OPIdentityDelegateImplementation.getInstance(identity);// new
																															// OPIdentityDelegateImplementation(mIdentityLoginWebView,
																															// identity);
				identityDelegate.bindListener(mListener);
				identityDelegate.setWebview(webview);
				identity.attachDelegate(identityDelegate, OPSdkConfig.getInstance().getOuterFrameUrl());

			} else {
				OPDataManager.getInstance().setIdentities(identities);

				// mListener.onLoginComplete();

				String version = OPDataManager.getDatastoreDelegate().getDownloadedContactsVersion(identity.getStableID());
				if (TextUtils.isEmpty(version)) {
					Log.d("login", "start download initial contacts");
					identity.startRolodexDownload("");
				} else {
					// check for new contacts
					Log.d("login", "start download  contacts since version " + version);
					identity.startRolodexDownload(version);
				}
			}
		}

	}

	public static void onLoginComplete() {
		instance = null;
	}

	/**
	 * @return
	 */
	public static boolean isLogIn() {
		// TODO Auto-generated method stub
		return instance != null;
	}

	/**
	 * 
	 */
	public static void onAccountShutdown() {
		// release resources
		instance = null;
	}
}
