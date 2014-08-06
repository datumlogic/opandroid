package com.openpeer.sdk.app;

import java.util.List;

import javax.security.auth.callback.CallbackHandler;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;

public class LoginManager {
	// private static LoginManager instance;
	private static LoginUIListener mListener;
	private WebView mAccountLoginWebView;
	private static WebView mIdentityLoginWebView;
	private OPCallDelegate mCallDelegate;
	OPConversationThreadDelegate conversationThreadDelegate;

	private static LoginManager instance;

	public static LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	public LoginManager setup(LoginUIListener mListener, WebView mAccountLoginWebView, WebView mIdentityLoginWebView,
			OPCallDelegate callDelegate, OPConversationThreadDelegate conversationThreadDelegate) {
		this.mListener = mListener;
		this.mAccountLoginWebView = mAccountLoginWebView;
		this.mIdentityLoginWebView = mIdentityLoginWebView;
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
//		instance = null;
		mAccountLoginWebView = null;
		mIdentityLoginWebView = null;
	}

	public void login() {

		// if (OPDatastoreDelegateImplementation.getInstance().getReloginInfo()
		// == null) {
		OPAccount account = new OPAccount();
		final OPAccountLoginWebViewClient client = new OPAccountLoginWebViewClient(account);
		mAccountLoginWebView.post(new Runnable() {
			public void run() {
				mAccountLoginWebView.setWebViewClient(client);
			}
		});

		OPAccountDelegateImplementation accountDelegate = new OPAccountDelegateImplementation(mAccountLoginWebView, account);

		account = OPAccount.login(accountDelegate, conversationThreadDelegate, mCallDelegate, OPSdkConfig.getInstance()
				.getNamespaceGrantServiceUrl(), OPSdkConfig.getInstance().getGrantId(),
				OPSdkConfig.getInstance().getLockboxServiceDomain(), false);
		OPDataManager.getInstance().setSharedAccount(account);
		client.mAccount = account;
		startIdentityLogin();
	}

	public void relogin(String reloginInfo) {
		OPAccount account;
		if (OPDataManager.getInstance().getSharedAccount() != null) {
			account = OPDataManager.getInstance().getSharedAccount();
		} else {
			account = new OPAccount();
		}
		final OPAccountLoginWebViewClient client = new OPAccountLoginWebViewClient(account);
		mAccountLoginWebView.post(new Runnable() {
			public void run() {
				mAccountLoginWebView.setWebViewClient(client);
			}
		});
		OPAccountDelegateImplementation accountDelegate = new OPAccountDelegateImplementation(mAccountLoginWebView, account);

		account = OPAccount.relogin(accountDelegate, conversationThreadDelegate, mCallDelegate, OPSdkConfig.getInstance()
				.getNamespaceGrantServiceUrl(), reloginInfo);

		OPDataManager.getInstance().setSharedAccount(account);
		client.mAccount = account;
	}

	public void startIdentityLogin() {
		// TODO Auto-generated method stub
		OPAccount account = OPDataManager.getInstance().getSharedAccount();

		OPIdentity identity = new OPIdentity();
		OPIdentityLoginWebViewClient client = new OPIdentityLoginWebViewClient(identity);
		mIdentityLoginWebView.setWebViewClient(client);

		OPIdentityDelegateImplementation identityDelegate = new OPIdentityDelegateImplementation(mIdentityLoginWebView, identity);

		OPSdkConfig config = OPSdkConfig.getInstance();
		Log.d("login", "identity initial " + identity);
		identity = identity.login(account, identityDelegate, config.getIdentityProviderDomain(),// "identity-v1-rel-lespaul-i.hcs.io",
				config.getIdentityBaseUri(),// "identity://identity-v1-rel-lespaul-i.hcs.io/",
				config.getOuterFrameUrl());// "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true");
		client.setIdentity(identity);
	}

	// public static LoginManager getInstance(){
	// if(instance == null){
	// instance = new LoginManager();
	// }
	// return instance;
	// }
	public static class OPIdentityDelegateImplementation extends OPIdentityDelegate {
		WebView mLoginView;

		// OPIdentity mIdentity;// somehow the identity passed in the callback
		// function
		// isn't same as the one created.

		public void setmIdentity(OPIdentity mIdentity) {
			// this.mIdentity = mIdentity;
		}

		public  OPIdentityDelegateImplementation(WebView loginView, OPIdentity identity) {
			this.mLoginView = loginView;
			// mIdentity = identity;
		}

		@Override
		public void onIdentityStateChanged(OPIdentity identity, IdentityStates state) {
			// TODO Auto-generated method stub
			Log.d("state", "identity state " + state);

			switch (state) {
			case IdentityState_WaitingForBrowserWindowToBeLoaded:
				// LoginManager.loadOuterFrame();//load identity.html
				mLoginView.post(new Runnable() {
					public void run() {
						Log.d("login", "loading identity webview");
						mListener.onStartIdentityLogin();
						mLoginView.loadUrl("http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose&federated=false");
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
				if (OPDataManager.getInstance().getSharedAccount().getState(0, "") == AccountStates.AccountState_Ready) {
					OPDataManager.getInstance().setIdentities(OPDataManager.getInstance().getSharedAccount().getAssociatedIdentities());

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
				break;
			case IdentityState_Shutdown:
				// Temporary defensive code. Proper logic will be put in place soon.
				if (mListener != null) {
					mLoginView.post(new Runnable() {
						public void run() {
							mListener.onLoginError();
							mLoginView = null;
						}
					});
				}
				break;
			}
		}

		@Override
		public void onIdentityPendingMessageForInnerBrowserWindowFrame(OPIdentity identity) {
			// TODO Auto-generated method stub
			String msg = identity.getNextMessageForInnerBrowerWindowFrame();
			Log.d("login", "identity pendingMessageForInnerFrame " + msg);

			passMessageToJS(msg);
		}

		@Override
		public void onIdentityRolodexContactsDownloaded(OPIdentity identity) {
			OPDataManager.getInstance().onDownloadedRolodexContacts(identity);
			mListener.onLoginComplete();
			// destroy();
		}

		public void passMessageToJS(final String msg) {
			mLoginView.post(new Runnable() {
				public void run() {
					String cmd = String.format("javascript:sendBundleToJS(\'%s\')", msg);
					Log.w("login", "Identity webview Pass to JS: " + cmd);
					mLoginView.loadUrl(cmd);
				}
			});
		}
	}

	public static class OPAccountDelegateImplementation extends OPAccountDelegate {
		WebView mLoginView;
		

		public OPAccountDelegateImplementation(WebView mLoginView, OPAccount mAccount) {
			super();
			this.mLoginView = mLoginView;
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
						mLoginView.loadUrl(OPSdkConfig.getInstance().getNamespaceGrantServiceUrl());
					}
				});
				break;
			case AccountState_WaitingForBrowserWindowToBeMadeVisible:
				mLoginView.post(new Runnable() {
					public void run() {
						mListener.onAccountLoginWebViewMadeVisible();
					}
				});
				account.notifyBrowserWindowVisible();
				break;
			case AccountState_WaitingForBrowserWindowToClose:
				mLoginView.post(new Runnable() {
					public void run() {
						mListener.onAccountLoginWebViewMadeClose();
					}
				});
				account.notifyBrowserWindowClosed();
				break;
			case AccountState_Ready:
				Log.w("login", "Account READY !!!!!!!!!!!!");
				OPDataManager.getInstance().setAccountReady(true);

				onAccountStateReady(account);
				break;
			case AccountState_Shutdown:
				OPDataManager.getInstance().setAccountReady(false);
				if (mListener != null) {
					mLoginView.post(new Runnable() {
						public void run() {
							mListener.onLoginError();
						}
					});
				}
				break;
			}
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
					final OPIdentityLoginWebViewClient client = new OPIdentityLoginWebViewClient(identity);
					mIdentityLoginWebView.post(new Runnable() {
						public void run() {
							mIdentityLoginWebView.setWebViewClient(client);

						}
					});
					OPIdentityDelegateImplementation identityDelegate = new OPIdentityDelegateImplementation(mIdentityLoginWebView,
							identity);
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

		@Override
		public void onAccountAssociatedIdentitiesChanged(OPAccount account) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAccountPendingMessageForInnerBrowserWindowFrame(OPAccount account) {
			// TODO Auto-generated method stub
			// LoginManager.pendingMessageForNamespaceGrantInnerFrame();
			String msg = account.getNextMessageForInnerBrowerWindowFrame();
			Log.d("output", "pendingMessageForNamespaceGrantInnerFrame " + msg);
			passMessageToJS(msg);
		}

		void passMessageToJS(final String msg) {
			mLoginView.post(new Runnable() {
				public void run() {
					String cmd = String.format("javascript:sendBundleToJS(\'%s\')", msg);
					Log.w("JNI", "Pass to JS: " + cmd);
					mLoginView.loadUrl(cmd);
				}
			});
		}
	}
}
