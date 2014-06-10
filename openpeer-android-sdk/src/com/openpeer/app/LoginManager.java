package com.openpeer.app;

import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.webkit.WebView;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;

public class LoginManager {
	// private static LoginManager instance;
	private LoginUIListener mListener;
	private WebView mAccountLoginWebView;
	private WebView mIdentityLoginWebView;

	public LoginManager(LoginUIListener mListener,
			WebView mAccountLoginWebView, WebView mIdentityLoginWebView) {
		super();
		this.mListener = mListener;
		this.mAccountLoginWebView = mAccountLoginWebView;
		this.mIdentityLoginWebView = mIdentityLoginWebView;
	}

	public void login() {

		// if (OPDatastoreDelegateImplementation.getInstance().getReloginInfo()
		// == null) {
		OPAccount mAccount = OPDataManager.getInstance().getSharedAccount();
		mAccountLoginWebView.setWebViewClient(new OPAccountLoginWebViewClient(
				mAccount));
		OPAccountDelegateImplementation accountDelegate = new OPAccountDelegateImplementation(
				mAccountLoginWebView, mAccount);
		CallbackHandler.getInstance().registerAccountDelegate(mAccount,
				accountDelegate);

		mAccount = OPAccount.login(null, null, null,
				"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html",
				"bojanGrantID", "identity-v1-rel-lespaul-i.hcs.io", false);
		startIdentityLogin();
	}

	public void relogin(String reloginInfo) {
		OPAccount mAccount = OPDataManager.getInstance().getSharedAccount();
		OPAccountDelegateImplementation mAccountDelegate = new OPAccountDelegateImplementation(
				mAccountLoginWebView, mAccount);
		CallbackHandler.getInstance().registerAccountDelegate(mAccount,
				mAccountDelegate);
		mAccount = OPAccount.relogin(null, null, null,
				"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html",// namespaceGrantOuterFrameURLUponReload
				reloginInfo);
	}

	public void startIdentityLogin() {
		// TODO Auto-generated method stub
		OPAccount mAccount = OPDataManager.getInstance().getSharedAccount();

		OPIdentity identity = new OPIdentity();
		mIdentityLoginWebView
				.setWebViewClient(new OPIdentityLoginWebViewClient(identity));

		OPIdentityDelegateImplementation identityDelegate = new OPIdentityDelegateImplementation(
				mIdentityLoginWebView, identity);
		CallbackHandler.getInstance().registerIdentityDelegate(identity,
				identityDelegate);

		OPSdkConfig config = OPSdkConfig.getInstance();
		identity = OPIdentity.login(mAccount, null,
				config.getIdentityProviderDomain(),// "identity-v1-rel-lespaul-i.hcs.io",
				config.getIdentityBaseUri(),// "identity://identity-v1-rel-lespaul-i.hcs.io/",
				config.getOuterFrameUrl());// "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true");

	}

	// public static LoginManager getInstance(){
	// if(instance == null){
	// instance = new LoginManager();
	// }
	// return instance;
	// }
	public class OPIdentityDelegateImplementation extends OPIdentityDelegate {
		WebView mLoginView;
		OPIdentity mIdentity;// somehow the identity passed in the callback
								// function
								// isn't same as the one created.

		public OPIdentityDelegateImplementation(WebView loginView,
				OPIdentity identity) {
			this.mLoginView = loginView;
			mIdentity = identity;
		}

		@Override
		public void onIdentityStateChanged(OPIdentity identity,
				IdentityStates state) {
			// TODO Auto-generated method stub
			Log.d("state", "identity state " + state);

			switch (state) {
			case IdentityState_WaitingForBrowserWindowToBeLoaded:
				// LoginManager.loadOuterFrame();//load identity.html
				mLoginView.post(new Runnable() {
					public void run() {
						Log.d("login", "loading identity webview");
						mLoginView
								.loadUrl("http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose");
					}
				});
				break;
			case IdentityState_WaitingForBrowserWindowToBeMadeVisible:

				mIdentity.notifyBrowserWindowVisible();
				break;
			case IdentityState_WaitingForBrowserWindowToClose:
				mIdentity.notifyBrowserWindowClosed();
				break;
			case IdentityState_Ready:
				// LoginManager.mIdentity.;
				break;
			}

		}

		@Override
		public void onIdentityPendingMessageForInnerBrowserWindowFrame(
				OPIdentity identity) {
			// TODO Auto-generated method stub
			String msg = mIdentity.getNextMessageForInnerBrowerWindowFrame();
			Log.d("login", "pendingMessageForInnerFrame " + msg);

			passMessageToJS(msg);
		}

		@Override
		public void onIdentityRolodexContactsDownloaded(OPIdentity identity) {
			// TODO Auto-generated method stub
			ContactsManager.getInstance().onDownloadedRolodexContacts(identity);
		}

		public void passMessageToJS(final String msg) {
			mLoginView.post(new Runnable() {
				public void run() {
					String cmd = String.format(
							"javascript:sendBundleToJS(\'%s\')", msg);
					Log.w("login", "Identity webview Pass to JS: " + cmd);
					mLoginView.loadUrl(cmd);
				}
			});
		}
	}

	public class OPAccountDelegateImplementation extends OPAccountDelegate {
		WebView mLoginView;
		OPAccount mAccount;// somehow the orignal account created is different
							// from
							// the account passed in the callback.

		public OPAccountDelegateImplementation(WebView mLoginView,
				OPAccount mAccount) {
			super();
			this.mLoginView = mLoginView;
			this.mAccount = mAccount;
		}

		@Override
		public void onAccountStateChanged(OPAccount account, AccountStates state) {
			Log.d("state", "Account state " + state);
			switch (state) {
			case AccountState_WaitingForAssociationToIdentity:
				break;
			case AccountState_WaitingForBrowserWindowToBeLoaded:
				mLoginView.post(new Runnable() {
					public void run() {
						mLoginView.loadUrl(OPSdkConfig.getInstance()
								.getNamespaceGrantServiceUrl());// "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose");
					}
				});
				break;
			case AccountState_WaitingForBrowserWindowToBeMadeVisible:
				mAccount.notifyBrowserWindowVisible();
				break;
			case AccountState_WaitingForBrowserWindowToClose:
				mAccount.notifyBrowserWindowClosed();
				break;
			case AccountState_Ready:
				Log.w("login", "Account READY !!!!!!!!!!!!");
				onAccountStateReady(account);
				break;
			// LoginManager.loadOuterFrame();
			}
		}

		public void onAccountStateReady(OPAccount account) {

			try {
				AccountStates state = AccountStates.AccountState_Pending;
				int outErrorCode = 0;
				String outErrorReason = "";
				state = mAccount.getState(outErrorCode, outErrorReason);
				if (state != AccountStates.AccountState_Ready) {
					Log.d("TODO",
							"Account test FAILED state = " + state.toString());
					// TODO: error handling
					return;
				}

				List<OPIdentity> identities = account.getAssociatedIdentities();
				if (identities.size() == 0) {
					Log.d("TODO",
							"Account test FAILED identities = "
									+ Arrays.deepToString(identities.toArray()));
					return;
				}

				for (OPIdentity identity : identities) {
					if (identity.getDownloadedRolodexContacts() == null) {
						Log.d("output",
								"Identity lookup test is preparing, please wait...");
						identity.startRolodexDownload("");
					} else {
					}
				}
			} catch (Exception e) {
				Log.d("TODO", "Account error " + e);
				return;
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
			// LoginManager.pendingMessageForNamespaceGrantInnerFrame();
			String msg = mAccount.getNextMessageForInnerBrowerWindowFrame();
			Log.d("output", "pendingMessageForNamespaceGrantInnerFrame " + msg);
			passMessageToJS(msg);
		}

		void passMessageToJS(final String msg) {
			mLoginView.post(new Runnable() {
				public void run() {
					String cmd = String.format(
							"javascript:sendBundleToJS(\'%s\')", msg);
					Log.w("JNI", "Pass to JS: " + cmd);
					mLoginView.loadUrl(cmd);
				}
			});
		}

	}

}
