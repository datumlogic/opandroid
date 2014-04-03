/*
 
 Copyright (c) 2014, SMB Phone Inc.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.
 
 */

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
		mAccountDelegate = new OPAccountDelegateImplementation();
		mAccount = new OPAccount();
		mCallbackHandler.registerAccountDelegate(mAccount, mAccountDelegate);
		
		mAccount = OPAccount.login(null, null, null, 
				"http://jsouter-v1-beta-1-i.hcs.io/grant.html", 
				"bojanGrantID", 
				"identity-v1-beta-1-i.hcs.io", false);
		
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
