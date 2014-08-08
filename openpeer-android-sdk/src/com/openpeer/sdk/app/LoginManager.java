package com.openpeer.sdk.app;

import java.util.List;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.sdk.delegates.OPAccountDelegateImplementation;
import com.openpeer.sdk.delegates.OPIdentityDelegateImplementation;

public class LoginManager {
	private static LoginUIListener mListener;
	private OPCallDelegate mCallDelegate;
	OPConversationThreadDelegate conversationThreadDelegate;

	private static LoginManager instance;

	public static LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	public LoginManager setup(LoginUIListener listener, WebView mAccountLoginWebView, WebView identityLoginWebView,
			OPCallDelegate callDelegate, OPConversationThreadDelegate conversationThreadDelegate) {
		mListener = listener;
		// mIdentityLoginWebView = identityLoginWebView;
		this.mCallDelegate = callDelegate;
		this.conversationThreadDelegate = conversationThreadDelegate;
		return this;
	}

	private LoginManager() {
		// TODO Auto-generated constructor stub
	}

	public boolean isLoginInProgress() {
		return instance != null;
	}

	/**
	 * this function should be called after logging to release resources
	 */
	void destroy() {

	}

	public void login() {
		OPAccountDelegateImplementation accountDelegate = OPAccountDelegateImplementation.getInstance();
		accountDelegate.bind(mListener);
		OPAccount account = OPAccount.login(accountDelegate, conversationThreadDelegate, mCallDelegate, OPSdkConfig.getInstance()
				.getNamespaceGrantServiceUrl(), OPSdkConfig.getInstance().getGrantId(),
				OPSdkConfig.getInstance().getLockboxServiceDomain(), false);
		OPDataManager.getInstance().setSharedAccount(account);
		startIdentityLogin();
	}

	public void relogin(String reloginInfo) {

		OPAccountDelegateImplementation accountDelegate = OPAccountDelegateImplementation.getInstance();
		accountDelegate.bind(mListener);
		OPAccount account = OPAccount.relogin(accountDelegate, conversationThreadDelegate, mCallDelegate, OPSdkConfig.getInstance()
				.getNamespaceGrantServiceUrl(), reloginInfo);

		OPDataManager.getInstance().setSharedAccount(account);
	}

	public void startIdentityLogin() {
		// TODO Auto-generated method stub
		OPAccount account = OPDataManager.getInstance().getSharedAccount();

		OPIdentity identity = new OPIdentity();
		OPIdentityLoginWebview mIdentityLoginWebView = mListener.getIdentityWebview(null);

		OPIdentityDelegateImplementation identityDelegate = OPIdentityDelegateImplementation.getInstance(null);// new
																												// OPIdentityDelegateImplementation(mIdentityLoginWebView,
																												// identity);
		identityDelegate.bindListener(mListener);
		identityDelegate.setWebview(mIdentityLoginWebView);

		OPSdkConfig config = OPSdkConfig.getInstance();
		identity = OPIdentity.login(account, identityDelegate, config.getIdentityProviderDomain(),// "identity-v1-rel-lespaul-i.hcs.io",
				config.getIdentityBaseUri(),// "identity://identity-v1-rel-lespaul-i.hcs.io/",
				config.getOuterFrameUrl());// "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true");
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
}
