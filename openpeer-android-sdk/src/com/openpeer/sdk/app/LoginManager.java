package com.openpeer.sdk.app;

import java.util.Arrays;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;

public class LoginManager {
	// private static LoginManager instance;
	private LoginUIListener mListener;
	private WebView mAccountLoginWebView;
	private WebView mIdentityLoginWebView;
	private OPCallDelegate mCallDelegate;

	private static LoginManager instance;

	public static LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	public LoginManager setup(LoginUIListener mListener,
			WebView mAccountLoginWebView, WebView mIdentityLoginWebView, OPCallDelegate callDelegate) {
		this.mListener = mListener;
		this.mAccountLoginWebView = mAccountLoginWebView;
		this.mIdentityLoginWebView = mIdentityLoginWebView;
		this.mCallDelegate = callDelegate;
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
	static void destroy() {
		instance = null;
	}

	public void login() {

		// if (OPDatastoreDelegateImplementation.getInstance().getReloginInfo()
		// == null) {
		OPAccount account = new OPAccount();
		OPAccountLoginWebViewClient client = new OPAccountLoginWebViewClient(
				account);
		mAccountLoginWebView.setWebViewClient(client);
		OPAccountDelegateImplementation accountDelegate = new OPAccountDelegateImplementation(
				mAccountLoginWebView, account);
		CallbackHandler.getInstance().registerAccountDelegate(account,
				accountDelegate);

		account = OPAccount.login(null, null, null,
				"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html",
				"bojanGrantID", "identity-v1-rel-lespaul-i.hcs.io", false);
		OPDataManager.getInstance().setSharedAccount(account);
		client.mAccount = account;
		accountDelegate.mAccount = account;
		startIdentityLogin();
	}

	public void relogin(String reloginInfo) {
		OPAccount account;
		if (OPDataManager.getInstance().getSharedAccount() != null) {
			account = OPDataManager.getInstance().getSharedAccount();
		} else {
			account = new OPAccount();
		}
		OPAccountLoginWebViewClient client = new OPAccountLoginWebViewClient(
				account);
		mAccountLoginWebView.setWebViewClient(client);

		account = OPAccount.relogin(null, null, null,
				"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html",// "http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html"
																	// namespaceGrantOuterFrameURLUponReload
				reloginInfo);
		OPAccountDelegateImplementation accountDelegate = new OPAccountDelegateImplementation(
				mAccountLoginWebView, account);
		CallbackHandler.getInstance().registerAccountDelegate(account,
				accountDelegate);
		OPDataManager.getInstance().setSharedAccount(account);
		client.mAccount = account;
		accountDelegate.mAccount = account;
	}

	public void startIdentityLogin() {
		// TODO Auto-generated method stub
		OPAccount account = OPDataManager.getInstance().getSharedAccount();

		OPIdentity identity = new OPIdentity();
		OPIdentityLoginWebViewClient client = new OPIdentityLoginWebViewClient(
				identity);
		mIdentityLoginWebView.setWebViewClient(client);

		OPIdentityDelegateImplementation identityDelegate = new OPIdentityDelegateImplementation(
				mIdentityLoginWebView, identity);
		CallbackHandler.getInstance().registerIdentityDelegate(identity,
				identityDelegate);

		OPSdkConfig config = OPSdkConfig.getInstance();
		Log.d("login", "identity initial " + identity);
		identity = identity.login(account, null,
				config.getIdentityProviderDomain(),// "identity-v1-rel-lespaul-i.hcs.io",
				config.getIdentityBaseUri(),// "identity://identity-v1-rel-lespaul-i.hcs.io/",
				config.getOuterFrameUrl());// "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true");
		client.setIdentity(identity);
		identityDelegate.setmIdentity(identity);
		Log.d("login", "identity after login call " + identity);

	}

	// public static LoginManager getInstance(){
	// if(instance == null){
	// instance = new LoginManager();
	// }
	// return instance;
	// }
	public class OPIdentityDelegateImplementation extends OPIdentityDelegate {
		WebView mLoginView;

		// OPIdentity mIdentity;// somehow the identity passed in the callback
		// function
		// isn't same as the one created.

		public void setmIdentity(OPIdentity mIdentity) {
			// this.mIdentity = mIdentity;
		}

		public OPIdentityDelegateImplementation(WebView loginView,
				OPIdentity identity) {
			this.mLoginView = loginView;
			// mIdentity = identity;
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
						mListener.onStartIdentityLogin();
						mLoginView
								.loadUrl("http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose");
					}
				});
				break;
			case IdentityState_WaitingForBrowserWindowToBeMadeVisible:
				mLoginView.post(new Runnable() {
					public void run() {
						mListener.onIdentityLoginWebViewMadeVisible();
					}
				});
				identity.notifyBrowserWindowVisible();
				break;
			case IdentityState_WaitingForBrowserWindowToClose:
				mLoginView.post(new Runnable() {
					public void run() {
						mListener.onIdentityLoginWebViewClose();
					}
				});
				identity.notifyBrowserWindowClosed();
				break;
			case IdentityState_Ready:
				// LoginManager.mIdentity.;
				if (OPDataManager.getInstance().getSharedAccount()
						.getState(0, "") == AccountStates.AccountState_Ready) {
					OPDataManager.getInstance().setIdentities(
							OPDataManager.getInstance().getSharedAccount()
									.getAssociatedIdentities());

					mListener.onLoginComplete();

					String version = OPDataManager.getDatastoreDelegate()
							.getDownloadedContactsVersion(
									identity.getStableID());
					if (TextUtils.isEmpty(version)) {
						Log.d("login", "start download initial contacts");
						identity.startRolodexDownload("");
					} else {
						// check for new contacts
						Log.d("login",
								"start download  contacts since version "
										+ version);
						identity.startRolodexDownload(version);
					}

				}
				break;
			}

		}

		@Override
		public void onIdentityPendingMessageForInnerBrowserWindowFrame(
				OPIdentity identity) {
			// TODO Auto-generated method stub
			String msg = identity.getNextMessageForInnerBrowerWindowFrame();
			Log.d("login", "identity pendingMessageForInnerFrame " + msg);

			passMessageToJS(msg);
		}

		@Override
		public void onIdentityRolodexContactsDownloaded(OPIdentity identity) {
			OPDataManager.getInstance().onDownloadedRolodexContacts(identity);
			CallbackHandler.getInstance().unregisterIdentityDelegate(this);
			// destroy();
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
				mLoginView.post(new Runnable() {
					public void run() {
						mListener.onAccountLoginWebViewMadeVisible();
					}
				});
				mAccount.notifyBrowserWindowVisible();
				break;
			case AccountState_WaitingForBrowserWindowToClose:
				mLoginView.post(new Runnable() {
					public void run() {
						mListener.onAccountLoginWebViewMadeClose();
					}
				});
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

			OPDataManager.getInstance().setSharedAccount(mAccount);

			List<OPIdentity> identities = mAccount.getAssociatedIdentities();
			if (identities.size() == 0) {
				Log.d("TODO", "Account test FAILED identities emppty ");

				return;
			}

			for (OPIdentity identity : identities) {
				if (!identity.isDelegateAttached()) {
					final OPIdentityLoginWebViewClient client = new OPIdentityLoginWebViewClient(
							identity);
					mIdentityLoginWebView.post(new Runnable() {
						public void run() {
							mIdentityLoginWebView.setWebViewClient(client);

						}
					});
					OPIdentityDelegateImplementation identityDelegate = new OPIdentityDelegateImplementation(
							mIdentityLoginWebView, identity);
					CallbackHandler.getInstance().registerIdentityDelegate(
							identity, identityDelegate);
					identity.attachDelegate(identityDelegate,
							"http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true");

				} else {
					OPDataManager.getInstance().setIdentities(identities);

					mListener.onLoginComplete();

					String version = OPDataManager.getDatastoreDelegate()
							.getDownloadedContactsVersion(
									identity.getStableID());
					if (TextUtils.isEmpty(version)) {
						Log.d("login", "start download initial contacts");
						identity.startRolodexDownload("");
					} else {
						// check for new contacts
						Log.d("login",
								"start download  contacts since version "
										+ version);
						identity.startRolodexDownload(version);
					}
					CallbackHandler.getInstance().unregisterAccountDelegate(
							this);
				}
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
