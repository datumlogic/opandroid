package com.openpeer.sample.login;

import android.content.Context;
import android.util.Log;

import com.openpeer.app.OPSdkConfig;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;
import com.openpeer.javaapi.OPMediaEngine;
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
		if (mLoginHandler != null)
			mLoginHandler.onLoadOuterFrameHandle(null);

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

		OPSdkConfig config = OPSdkConfig.getInstance(mContext);
		mIdentity = OPIdentity.login(mAccount, null,
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

	public static void initializeContext(Context context) {
		// TODO Auto-generated method stub
		// OPLogger.installTelnetLogger(59999, 60, true);
		// stackMessageQueue = OPStackMessageQueue.singleton();
		//
		// //stackMessageQueue.interceptProcessing(null);
		//
		// //TODO: After interception is done, we can call setup
		// stack = new OPStack();
		// stack.setup(null, null);
		mContext = context;

	}

	public static void AccountLogin() {
		Log.d(TAG, "AccountLogin START");

		// TODO Auto-generated method stub
		long stableId = 0;
		if (mAccount == null) {
			mAccountDelegate = new OPAccountDelegateImplementation();
			mAccount = new OPAccount();
			mCallbackHandler
					.registerAccountDelegate(mAccount, mAccountDelegate);

			mAccount = OPAccount.login(null, null, null,
					"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html",
					"bojanGrantID", "identity-v1-rel-lespaul-i.hcs.io", false);
			startIdentityLogin();
		} else {
			stableId = mAccount.getStableID();

		}
		Log.d(TAG, "AccountLogin END");

	}

	public static void AccountRelogin(String reloginInfo) {
		Log.d(TAG, "AccountLogin START");

		// TODO Auto-generated method stub
		long stableId = 0;
		if (mAccount == null) {
			mAccountDelegate = new OPAccountDelegateImplementation();
			mAccount = new OPAccount();
			mCallbackHandler
					.registerAccountDelegate(mAccount, mAccountDelegate);

			mAccount = OPAccount.relogin(null, null, null,
					"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html",// namespaceGrantOuterFrameURLUponReload
					reloginInfo);
		} else {
			stableId = mAccount.getStableID();

		}
		Log.d(TAG, "AccountLogin END");

	}

	public static void startAccountLogin() {
		// TODO Auto-generated method stub
		Log.d(TAG, "startAccountLogin START");
		if (mLoginHandler != null)
			mLoginHandler
					.onLoadOuterFrameHandle("http://jsouter-v1-rel-dev2-i.hcs.io/grant.html");
		Log.d(TAG, "startAccountLogin END");

	}

	public static void initNamespaceGrantInnerFrame() {
		// TODO Auto-generated method stub
		mLoginHandler.onNamespaceGrantInnerFrameInitialized(mAccount
				.getInnerBrowserWindowFrameURL());
	}

	public static void onAccountStateReady() {
		// TODO Auto-generated method stub
		mLoginHandler.onAccountStateReady();

	}

	public static void onDownloadedRolodexContacts(OPIdentity identity) {
		// TODO Auto-generated method stub
		LoginManager.mIdentityLookupDelegate = new OPIdentityLookupDelegateImplementation();
		LoginManager.mIdentityLookup = new OPIdentityLookup();
		mCallbackHandler.registerIdentityLookupDelegate(
				LoginManager.mIdentityLookup,
				LoginManager.mIdentityLookupDelegate);

		mLoginHandler.onDownloadedRolodexContacts(identity);

	}

	public static void onIdentityLookupCompleted(OPIdentityLookup lookup) {
		// TODO Auto-generated method stub
		LoginManager.mIdentityLookup = lookup;

		mLoginHandler.onLookupCompleted();
	}
}
