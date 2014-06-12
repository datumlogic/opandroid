package com.openpeer.sample.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.openpeer.app.OPSdkConfig;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;
import com.openpeer.javaapi.OPIdentityLookupInfo;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackMessageQueue;
import com.openpeer.sample.delegates.OPAccountDelegateImplementation;
import com.openpeer.sample.delegates.OPIdentityDelegateImplementation;
import com.openpeer.sample.delegates.OPIdentityLookupDelegateImplementation;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginManager {
	private static String TAG = LoginManager.class.getSimpleName();

	public static CallbackHandler mCallbackHandler = CallbackHandler
			.getInstance();
	private static Context mContext;
	private static LoginManager instance;

	private LoginManager() {
	}

	public static LoginManager getInstance(Context context) {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	/**
	 * Call this function to destroy to save memory when login is done
	 */
	public void destroy() {
		instance = null;
	}

	public static OPAccount getAccount() {
		return mAccount;
	}

	public static void setAccount(OPAccount account) {
		LoginManager.mAccount = account;
	}

	public static OPStack stack;
	public static OPStackMessageQueue stackMessageQueue;
	public static OPAccount mAccount;
	public static OPAccountDelegate mAccountDelegate;
	public static OPIdentity mIdentity;
	public static OPIdentityDelegate mIdentityDelegate;
	public static OPMediaEngine mMediaEngine;
	public static OPIdentityLookupDelegate mIdentityLookupDelegate;
	// public static OPLogger mLogger;
	static LoginHandlerInterface mLoginHandler;
	public static OPIdentityLookup mIdentityLookup;
	public static OPConversationThread mConvThread;
	public static OPCacheDelegate mCacheDelegate;
	public static OPCall mCall;
	public static OPCallDelegate mCallDelegate;

	public static void setHandlerListener(LoginHandlerInterface listener) {
		mLoginHandler = listener;
	}

	public static void loadOuterFrame() {
		if (mLoginHandler != null) {
			mLoginHandler.onLoadOuterFrameHandle(null);
		}
	}

	public static void initInnerFrame() {
		mLoginHandler.onInnerFrameInitialized(mIdentity
				.getInnerBrowserWindowFrameURL());
	}

	public static void pendingMessageForInnerFrame() {
		String msg = mIdentity.getNextMessageForInnerBrowerWindowFrame();
		mLoginHandler.passMessageToJS(msg);

	}

	public static void pendingMessageForNamespaceGrantInnerFrame() {
		String msg = mAccount.getNextMessageForInnerBrowerWindowFrame();
		mLoginHandler.passMessageToJS(msg);

	}

	public static void startIdentityLogin() {
		// TODO Auto-generated method stub

		mIdentityDelegate = new OPIdentityDelegateImplementation();
		mIdentity = new OPIdentity();
		mCallbackHandler.registerIdentityDelegate(mIdentity, mIdentityDelegate);

		OPSdkConfig config = OPSdkConfig.getInstance();
		mIdentity = mIdentity.login(mAccount, null,
				config.getIdentityProviderDomain(),// "identity-v1-rel-lespaul-i.hcs.io",
				config.getIdentityBaseUri(),// "identity://identity-v1-rel-lespaul-i.hcs.io/",
				config.getOuterFrameUrl());// "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true");

	}

	public static String createHttpSettings() {
		try {
			JSONObject parent = new JSONObject();
			JSONObject jsonObject = new JSONObject();

			jsonObject
					.put("openpeer/stack/bootstrapper-force-well-known-over-insecure-http",
							"true");
			jsonObject.put(
					"openpeer/stack/bootstrapper-force-well-known-using-post",
					"true");
			parent.put("root", jsonObject);
			Log.d("output", parent.toString(2));
			return parent.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void AccountLogin() {
		// TODO Auto-generated method stub
		if (mAccount == null
				|| mAccount.getState(0, "") == AccountStates.AccountState_Shutdown) {
			mAccountDelegate = new OPAccountDelegateImplementation();
			mAccount = new OPAccount();
			mCallbackHandler
					.registerAccountDelegate(mAccount, mAccountDelegate);

			mAccount = OPAccount.login(null, null, null,
					"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html",
					"bojanGrantID", "identity-v1-rel-lespaul-i.hcs.io", false);
			startIdentityLogin();
		}

	}

	public static void startAccountRelogin(String reloginInfo) {

		mAccountDelegate = new OPAccountDelegateImplementation();
		mAccount = new OPAccount();
		mCallbackHandler.registerAccountDelegate(mAccount, mAccountDelegate);

		mAccount = OPAccount.relogin(null, null, null,
				"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html",// namespaceGrantOuterFrameURLUponReload
				reloginInfo);
	}

	public static void loadNameSpaceGrantOuterFrame() {
		if (mLoginHandler != null)
			mLoginHandler
					.onLoadOuterFrameHandle("http://jsouter-v1-rel-dev2-i.hcs.io/grant.html");
	}

	public static void initNamespaceGrantInnerFrame() {
		// TODO Auto-generated method stub
		mLoginHandler.onNamespaceGrantInnerFrameInitialized(mAccount
				.getInnerBrowserWindowFrameURL());
	}

	public static void onAccountStateReady(OPAccount account) {
		// TODO Auto-generated method stub
		// mLoginHandler.onAccountStateReady();
		try {
			AccountStates state = AccountStates.AccountState_Pending;
			int outErrorCode = 0;
			String outErrorReason = "";
			state = account.getState(outErrorCode, outErrorReason);
			if (state != AccountStates.AccountState_Ready) {
				Log.d("TODO", "Account test FAILED state = " + state.toString());
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
					mIdentity.startRolodexDownload("");
				} else {
				}
			}
		} catch (Exception e) {
			Log.d("TODO", "Account error " + e);
			return;
		}

	}

	public static void onDownloadedRolodexContacts(OPIdentity identity) {
		// TODO Auto-generated method stub

		identityLookup(identity);

		mLoginHandler.onDownloadedRolodexContacts(identity);

	}

	public static void onIdentityLookupCompleted(OPIdentityLookup lookup) {
		// TODO Auto-generated method stub
		LoginManager.mIdentityLookup = lookup;

		mLoginHandler.onLookupCompleted();
	}

	public static void identityLookup(OPIdentity identity) {
		LoginManager.mIdentityLookupDelegate = new OPIdentityLookupDelegateImplementation();
		LoginManager.mIdentityLookup = new OPIdentityLookup();

		mCallbackHandler.registerIdentityLookupDelegate(
				LoginManager.mIdentityLookup,
				LoginManager.mIdentityLookupDelegate);

		OPDownloadedRolodexContacts rolodexContacts = identity
				.getDownloadedRolodexContacts();

		List<OPIdentityLookupInfo> inputLookupList = new ArrayList<OPIdentityLookupInfo>();

		for (OPRolodexContact contact : rolodexContacts.getRolodexContacts()) {
			Log.d(TAG, "contact " + contact.toString());
			OPIdentityLookupInfo ilInfo = new OPIdentityLookupInfo();
			ilInfo.initWithRolodexContact(contact);
			inputLookupList.add(ilInfo);
		}

		mIdentityLookup = OPIdentityLookup.create(mAccount,
				mIdentityLookupDelegate, inputLookupList, OPSdkConfig
						.getInstance().getIdentityProviderDomain());// "identity-v1-rel-lespaul-i.hcs.io");
	}

	void registerDelegates() {

	}

	public static void showAccountWebview(OPAccount account) {
		account.notifyBrowserWindowVisible();
	}

	public static void closeAccountWebview(OPAccount account) {
		account.notifyBrowserWindowClosed();
	}
}
