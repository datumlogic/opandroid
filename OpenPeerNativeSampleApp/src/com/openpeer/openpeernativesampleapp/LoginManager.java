package com.openpeer.openpeernativesampleapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.provider.Settings.Secure;
import android.text.format.Time;
import android.util.Log;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.delegates.OPAccountDelegateImplementation;
import com.openpeer.delegates.OPIdentityDelegateImplementation;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPCache;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPSettings;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackMessageQueue;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPMediaEngineDelegate;

public class LoginManager {
	
	static CallbackHandler mCallbackHandler = new CallbackHandler();
	public static Context mContext;

	public static void LoginWithFacebook (){
		//TODO: Add delegate when implement mechanism to post events to the android GUI thread
		stackMessageQueue = OPStackMessageQueue.singleton(); 
		//stackMessageQueue = new OPStackMessageQueue();
		//stackMessageQueue.interceptProcessing(null);
		OPLogger.installTelnetLogger(59999, 60, true);
		stack = OPStack.singleton();
		
		//OPCache.setup(null);
		
		//OPSettings.setup(null);
		OPSettings.applyDefaults();
		
		String httpSettings = createHttpSettings();
		OPSettings.apply(httpSettings);
		
		String appSettings = createFakeApplicationSettings();
		OPSettings.apply(appSettings);
		
		//TODO: After interception is done, we can call setup
		
		stack.setup(null, null);
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
	//public static OPLogger mLogger;
	static LoginHandlerInterface mLoginHandler;
	
	public static void setHandlerListener(LoginHandlerInterface listener)
   {
	   mLoginHandler=listener;
   }
	
	public static void loadOuterFrame() {
		// TODO Auto-generated method stub
		
		   
		   
		if(mLoginHandler!=null)
	    	  mLoginHandler.onLoadOuterFrameHandle(null);
		
	}
	
	public static void initInnerFrame()
	{
		mLoginHandler.onInnerFrameInitialized(mIdentity.getInnerBrowserWindowFrameURL());
	}
	
	public static void pendingMessageForInnerFrame()
	{
		String msg = mIdentity.getNextMessageForInnerBrowerWindowFrame();
		mLoginHandler.passMessageToJS(msg);
		
	}
	
	public static void pendingMessageForNamespaceGrantInnerFrame()
	{
		String msg = mAccount.getNextMessageForInnerBrowerWindowFrame();
		mLoginHandler.passMessageToJS(msg);
		
	}
	
	public static void startIdentityLogin() {
		// TODO Auto-generated method stub
		
		   
	  mIdentityDelegate = new OPIdentityDelegateImplementation();
	  mIdentity = new OPIdentity();
	  mCallbackHandler.registerIdentityDelegate(mIdentity, mIdentityDelegate);
		   
      mIdentity = OPIdentity.login(mAccount, null,
    		  "identity-v1-beta-1-i.hcs.io", 
    		  "identity://identity-v1-beta-1-i.hcs.io/",
    		  "http://jsouter-v1-beta-1-i.hcs.io/identity.html?view=choose&federated=false?reload=true");
		
	}
	
	public static String createHttpSettings()
	{
		try {
            JSONObject parent = new JSONObject();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("openpeer/stack/bootstrapper-force-well-known-over-insecure-http", "true");
            jsonObject.put("openpeer/stack/bootstrapper-force-well-known-using-post", "true");
            parent.put("root", jsonObject);
            Log.d("output", parent.toString(2));
            return parent.toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
	}
	
	public static String createFakeApplicationSettings()
	{
//		{
//			"root": {
//				"outerFrameURL": "http://jsouter-v1-beta-1-i.hcs.io/identity.html?view=choose&federated=false",
//				"identityProviderDomain": "identity-v1-beta-1-i.hcs.io",
//				"identityFederateBaseURI": "identity://identity-v1-beta-1-i.hcs.io/",
//				"namespaceGrantServiceURL": "http://jsouter-v1-beta-1-i.hcs.io/grant.html",
//				"lockBoxServiceDomain": "lockbox-v1-beta-1-i.hcs.io",
//				"defaultOutgoingTelnetServer": "tcp-logger-v1-beta-1-i.hcs.io:8055"
//			}
//		}
		
		try {
            JSONObject parent = new JSONObject();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("openpeer/common/application-name", "OpenPeer Native Sample App");
            jsonObject.put("openpeer/common/application-image-url", "http://fake-image-url");
            jsonObject.put("openpeer/common/application-url", "http://fake-application-url");
            Time expires = new Time ();
            expires.set(30, 3, 2014);
			jsonObject.put("openpeer/calculated/authorizated-application-id", OPStack.createAuthorizedApplicationID("nativeApp", "shared-secret", expires ));
            jsonObject.put("openpeer/calculated/user-agent", "OpenPeerNativeSampleApp");
            jsonObject.put("openpeer/calculated/device-id", Secure.getString(mContext.getContentResolver(),
                    Secure.ANDROID_ID));
            jsonObject.put("openpeer/calculated/os", android.os.Build.VERSION.RELEASE);
            jsonObject.put("openpeer/calculated/system", android.os.Build.MODEL);
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
//		OPLogger.installTelnetLogger(59999, 60, true);
//		stackMessageQueue = OPStackMessageQueue.singleton(); 
//
//		//stackMessageQueue.interceptProcessing(null);
//		
//		//TODO: After interception is done, we can call setup
//		stack = new OPStack();
//		stack.setup(null, null);
		mContext = context;
		
	}



	public static void AccountLogin() {
		// TODO Auto-generated method stub
		long stableId = 0;
		if (mAccount == null)
		{
		mAccountDelegate = new OPAccountDelegateImplementation();
		mAccount = new OPAccount();
		mCallbackHandler.registerAccountDelegate(mAccount, mAccountDelegate);
		
		
		mAccount = OPAccount.login(null, null, null, 
				"http://jsouter-v1-beta-1-i.hcs.io/grant.html", 
				"bojanGrantID", 
				"identity-v1-beta-1-i.hcs.io", false);
		}
		else
		{
			stableId = mAccount.getStableID();
			
		}
		
	}



	public static void startAccountLogin() {
		// TODO Auto-generated method stub
		if(mLoginHandler!=null)
	    	  mLoginHandler.onLoadOuterFrameHandle("http://jsouter-v1-beta-1-i.hcs.io/grant.html");
	}



	public static void initNamespaceGrantInnerFrame() {
		// TODO Auto-generated method stub
		mLoginHandler.onNamespaceGrantInnerFrameInitialized(mAccount.getInnerBrowserWindowFrameURL());
	}
	
}
