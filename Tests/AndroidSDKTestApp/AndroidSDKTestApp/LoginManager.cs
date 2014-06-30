using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;
using Android.Text.Format;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Com;
using Com.Openpeer;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;
using Org.Json;
using Java.Util;
using Android.Util;
using AndroidSDKTestApp;

namespace AndroidSDKTestApp
{
	class LoginManager
	{

		static CallbackHandler mCallbackHandler = new CallbackHandler();
		public static Context mContext;

		public static String mInstanceId;
		public static String mDeviceId;

		public static void LoginWithFacebook ()
		{

			stackMessageQueue = OPStackMessageQueue.Singleton(); 
			//stackMessageQueue = new OPStackMessageQueue();
			//stackMessageQueue.interceptProcessing(null);
			OPLogger.InstallTelnetLogger(59999, 60, true);
			stack = OPStack.Singleton();

			mCacheDelegate = new OPCacheDelegateImplementation();
			mCallbackHandler.RegisterCacheDelegate(mCacheDelegate);
			OPCache.Setup(mCacheDelegate);

			//OPSettings.setup(null);
			OPSettings.ApplyDefaults();

			String httpSettings = createHttpSettings();
			OPSettings.Apply(httpSettings);

			String forceDashSettings = createForceDashSetting();
			OPSettings.Apply(forceDashSettings);

			String appSettings = createFakeApplicationSettings();
			OPSettings.Apply(appSettings);

			//TODO: After interception is done, we can call setup

			stack.Setup(null, null);

		}





		public static OPAccount getAccount()
		{
			return mAccount;
		}
		public static void setAccount(OPAccount account)
		{
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
		//public static OPLogger mLogger;
		static LoginHandlerInterface mLoginHandler;
		public static OPIdentityLookup mIdentityLookup;
		public static OPConversationThread mConvThread;
		public static OPCacheDelegate mCacheDelegate;
		public static OPCall mCall;
		public static OPCallDelegate mCallDelegate;

		public static void setHandlerListener(LoginHandlerInterface listener)
		{
			mLoginHandler=listener;
		}

		public static void loadOuterFrame() 
		{
			if (mLoginHandler != null)
			{
				mLoginHandler.onLoadOuterFrameHandle (null);
			}

		}

		public static void initInnerFrame()
		{
			//Java - mLoginHandler.onInnerFrameInitialized(mIdentity.getInnerBrowserWindowFrameURL());
			mLoginHandler.onInnerFrameInitialized(mIdentity.InnerBrowserWindowFrameURL);
		}


		public static void pendingMessageForInnerFrame()
		{
			//Java - String msg = mIdentity.getNextMessageForInnerBrowerWindowFrame();
			String msg = mIdentity.NextMessageForInnerBrowerWindowFrame;
			mLoginHandler.passMessageToJS(msg);

		}

		public static void pendingMessageForNamespaceGrantInnerFrame()
		{
			//Java - String msg = mIdentity.getNextMessageForInnerBrowerWindowFrame();
			String msg = mAccount.NextMessageForInnerBrowerWindowFrame;
			mLoginHandler.passMessageToJS(msg);

		}

		public static void StartIdentityLogin() {


			mIdentityDelegate = new OPIdentityDelegateImplementation();
			mIdentity = new OPIdentity();
			mCallbackHandler.RegisterIdentityDelegate(mIdentity, mIdentityDelegate);

			mIdentity = OPIdentity.Login(mAccount, null,
				"identity-v1-beta-1-i.hcs.io", 
				"identity://identity-v1-beta-1-i.hcs.io/",
				"http://jsouter-v1-beta-1-i.hcs.io/identity.html?view=choose&federated=false?reload=true");


		}

		/*
		public static void startIdentityLogin() {

			if(mLoginHandler!=null)
				mLoginHandler.onLoadOuterFrameHandle(null);

		}*/

		public static String createHttpSettings()
		{
			try {
				JSONObject parent = new JSONObject();
				JSONObject jsonObject = new JSONObject();

				jsonObject.Put("openpeer/stack/bootstrapper-force-well-known-over-insecure-http", "true");
				jsonObject.Put("openpeer/stack/bootstrapper-force-well-known-using-post", "true");
				parent.Put("root", jsonObject);

				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			} catch (JSONException e) {
				e.PrintStackTrace();
				return "";
			}
		}


		public static String createForceDashSetting()
		{
			try {
				JSONObject parent = new JSONObject();
				JSONObject jsonObject = new JSONObject();

				jsonObject.Put("openpeer/core/authorized-application-id-split-char", "-");
				parent.Put("root", jsonObject);
				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			} catch (JSONException e) {
				e.PrintStackTrace();
				return "";
			}
		}
		public static String createFakeApplicationSettings()
		{
			// {
			// "root": {
			// "outerFrameURL": "http://jsouter-v1-beta-1-i.hcs.io/identity.html?view=choose&federated=false",
			// "identityProviderDomain": "identity-v1-beta-1-i.hcs.io",
			// "identityFederateBaseURI": "identity://identity-v1-beta-1-i.hcs.io/",
			// "namespaceGrantServiceURL": "http://jsouter-v1-beta-1-i.hcs.io/grant.html",
			// "lockBoxServiceDomain": "lockbox-v1-beta-1-i.hcs.io",
			// "defaultOutgoingTelnetServer": "tcp-logger-v1-beta-1-i.hcs.io:8055"
			// }
			// }

			try {
				JSONObject parent = new JSONObject();
				JSONObject jsonObject = new JSONObject();

				jsonObject.Put("openpeer/common/application-name", "OpenPeer Native Sample App");
				jsonObject.Put("openpeer/common/application-image-url", "http://fake-image-url");
				jsonObject.Put("openpeer/common/application-url", "http://fake-application-url");
				//Android.Text.Format.Time OP="30.3.2014";
				Time expires=new Time();
				expires.Set(30,9,2014);

				jsonObject.Put("openpeer/calculated/authorizated-application-id", OPStack.CreateAuthorizedApplicationID("nativeApp", "shared-secret", expires ));
				jsonObject.Put("openpeer/calculated/user-agent", "OpenPeerNativeSampleApp");
				jsonObject.Put("openpeer/calculated/device-id",Android.Provider.Settings.Secure.GetString(mContext.ContentResolver,
					Android.Provider.Settings.Secure.AndroidId));
				jsonObject.Put("openpeer/calculated/os",Android.OS.Build.VERSION.Release);
				jsonObject.Put("openpeer/calculated/system",Android.OS.Build.Model);
				parent.Put("root", jsonObject);
				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			} catch (JSONException e) {
				e.PrintStackTrace();
				return "";
			}


		}


		public static void initializeContext(Context context)
		{
			mContext = context;
		}



		public static void AccountLogin()
		{
			long stableId = 0;
			if (mAccount == null)
			{
				mAccountDelegate = new OPAccountDelegateImplementation();
				mAccount = new OPAccount();
				mCallbackHandler.RegisterAccountDelegate(mAccount, mAccountDelegate);


				mAccount = OPAccount.Login(null, null, null, 
					"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html", 
					"bojanGrantID", 
					"identity-v1-rel-lespaul-i.hcs.io", false);
			}
			else
			{
				stableId = mAccount.StableID;

			}

		}



		public static void startAccountLogin()
		{
			if (mLoginHandler != null)
			{
				mLoginHandler.onLoadOuterFrameHandle ("http://jsouter-v1-rel-dev2-i.hcs.io/grant.html");
			}		
		}



		public static void initNamespaceGrantInnerFrame()
		{
			//Java - mLoginHandler.onNamespaceGrantInnerFrameInitialized(mAccount.getInnerBrowserWindowFrameURL());
			mLoginHandler.onNamespaceGrantInnerFrameInitialized(mAccount.InnerBrowserWindowFrameURL);
		}

		public static void onIdentityLookupCompleted(OPIdentityLookup lookup) {

			LoginManager.mIdentityLookup = lookup;

			mLoginHandler.onLookupCompleted();
		}

		public static void onDownloadedRolodexContacts(OPIdentity identity) {

			//LoginManager.mIdentityLookupDelegate = new OPIdentityDelegateImplementation();
			//LoginManager.mIdentityLookup = new OPIdentityLookup();
			//mCallbackHandler.RegisterIdentityLookupDelegate(LoginManager.mIdentityLookup, LoginManager.mIdentityLookupDelegate);

			//mLoginHandler.onDownloadedRolodexContacts(identity);

		}
		public static void onAccountStateReady() {

			mLoginHandler.onAccountStateReady();

		}




	}
}

