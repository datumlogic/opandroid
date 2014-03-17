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
		//OPSettings.setup(null);
		OPSettings.applyDefaults();
		
		String appSettings = createFakeApplicationSettings();
		OPSettings.apply(appSettings);
		
		//TODO: After interception is done, we can call setup
		stack = new OPStack();
		stack.setup(null, null);
//		
//		//LoginManager.stackMessageQueue = new OPStackMessageQueue();
//		//mCallbackHandler.regi
//		//LoginManager.stackMessageQueue.interceptProcessing(null);
//		//LoginManager.mLogger = new OPLogger();
//		//OPLogger.setLogLevel(OPLogLevel.Trace);
//		
//		
//		//LoginManager.stack = new OPStack();
//		//LoginManager.stack.setup(null, null, "bojan", "bojan1", "bojan2", "bojan3", "bojan4", "bojan5", "bojan6", "bojan7");
//		
//		//prepare account delegate
		
//		//register delegates and class for callback from native code
//		mCallbackHandler.registerAccountDelegate(mAccount, mAccountDelegate);
//		
//		mIdentityDelegate = new OPIdentityDelegateImplementation();
//		mIdentity = new OPIdentity();
//		mCallbackHandler.registerIdentityDelegate(mIdentity, mIdentityDelegate);
//		//TODO: Now we can start login procedure
//		//OPAccount.login(null, null, null, null, null, null, null);//delegate, conversationThreadDelegate, callDelegate, namespaceGrantOuterFrameURLUponReload, namespaceGrantServiceDomain, grantID, grantSecret, lockboxServiceDomain, forceCreateNewLockboxAccount)
//		
//		//OPIdentity.login(mAccount, mIdentityDelegate, "idprovider-javascript.hookflash.me", "identity://idprovider-javascript.hookflash.me/", "https://app-javascript.hookflash.me/outer.html?reload=true");
//		
//		//mMediaEngine = new OPMediaEngine();
//		//OPMediaEngine.singleton().setEcEnabled(true);
//		mMediaEngine = OPMediaEngine.getInstance();
//		mMediaEngine.setEcEnabled(true);
		
		//Time t = new Time();
		//t.
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
		
		   
		   
      //if(mLoginHandler!=null)
    	  //mLoginHandler.onLoadOuterFrameHandle(null);
		
	}
	
	public static void startIdentityLogin() {
		// TODO Auto-generated method stub
		
		   
		   
      if(mLoginHandler!=null)
    	  mLoginHandler.onLoadOuterFrameHandle(null);
		
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
		mAccountDelegate = new OPAccountDelegateImplementation();
		mAccount = new OPAccount();
		mCallbackHandler.registerAccountDelegate(mAccount, mAccountDelegate);
		
		OPAccount.login(null, null, null, "http://jsouter-v1-beta-1-i.hcs.io/grant.html", "bojanGrantID", "lockbox-v1-beta-1-i.hcs.io", false);
		
	}
	
}
