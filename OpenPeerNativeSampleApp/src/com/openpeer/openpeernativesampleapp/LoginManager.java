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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.text.format.Time;
import android.util.Log;

import com.openpeer.delegates.OPAccountDelegateImplementation;
import com.openpeer.delegates.OPCacheDelegateImplementation;
import com.openpeer.delegates.OPCallDelegateImplementation;
import com.openpeer.delegates.OPConversationThreadDelegateImplementation;
import com.openpeer.delegates.OPIdentityDelegateImplementation;
import com.openpeer.delegates.OPIdentityLookupDelegateImplementation;
import com.openpeer.delegates.OPMediaEngineDelegateImplementation;
import com.openpeer.delegates.OPSettingsDelegateImplementation;
import com.openpeer.delegates.OPStackDelegateImplementation;
import com.openpeer.delegates.OPStackMessageQueueDelegateImplementation;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPAccountDelegate;
import com.openpeer.javaapi.OPCache;
import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityDelegate;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupDelegate;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPSettings;
import com.openpeer.javaapi.OPSettingsDelegate;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackDelegate;
import com.openpeer.javaapi.OPStackMessageQueue;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPMediaEngineDelegate;
import com.openpeer.javaapi.OPStackMessageQueueDelegate;

public class LoginManager {

	//public static Context mContext;
	public static String mInstanceId;
	public static String mDeviceId;

	public static void LoginWithFacebook (){
		
		mInstanceId = java.util.UUID.randomUUID().toString();
		mInstanceId = mInstanceId.replace("-", "");
		
		Log.d("output", "instance ID = " + mInstanceId);
		//TODO: Add delegate when implement mechanism to post events to the android GUI thread
		stackMessageQueueDelegate = new OPStackMessageQueueDelegateImplementation();
		stackMessageQueue = OPStackMessageQueue.singleton();
		stackMessageQueue.interceptProcessing(stackMessageQueueDelegate);
		OPLogger.setLogLevel(OPLogLevel.LogLevel_Trace);
		OPLogger.setLogLevel("openpeer_webrtc", OPLogLevel.LogLevel_Basic);
		//OPLogger.setLogLevel("zsLib_socket", OPLogLevel.LogLevel_Insane);
		//OPLogger.setLogLevel("openpeer_services_transport_stream", OPLogLevel.LogLevel_Insane);
		//OPLogger.setLogLevel("openpeer_stack", OPLogLevel.LogLevel_Insane);
		String telnetLogString = mDeviceId + "-" + mInstanceId + "\n";
		Log.d("output", "Outgoing log string = " + telnetLogString);
		//OPLogger.installOutgoingTelnetLogger("logs.opp.me:8115", true, telnetLogString);
		OPLogger.installTelnetLogger(59999, 60, true);
		//OPLogger.installFileLogger("/storage/emulated/0/HFLog.txt", true);
		mStack = OPStack.singleton();

		mCacheDelegate = new OPCacheDelegateImplementation();
		OPCache.setup(mCacheDelegate);

		OPSettings.applyDefaults();
		//mSettingsDelegate = new OPSettingsDelegateImplementation();
		//OPSettings.setup(mSettingsDelegate);
		

		String httpSettings = createHttpSettings();
		OPSettings.apply(httpSettings);

		String forceDashSettings = createForceDashSetting();
		OPSettings.apply(forceDashSettings);

		String appSettings = createFakeApplicationSettings();
		OPSettings.apply(appSettings);

		//TODO: After interception is done, we can call setup

		mStackDelegate = new OPStackDelegateImplementation();
		mMediaEngineDelegate = new OPMediaEngineDelegateImplementation();
		mStack.setup(mStackDelegate, mMediaEngineDelegate);
	}



	public static OPAccount getAccount() {
		return mAccount;
	}
	public static void setAccount(OPAccount account) {
		LoginManager.mAccount = account;
	}

	public static OPStack mStack;
	public static OPStackDelegate mStackDelegate;
	public static OPMediaEngineDelegate mMediaEngineDelegate;
	public static OPStackMessageQueue stackMessageQueue;
	public static OPStackMessageQueueDelegate stackMessageQueueDelegate;
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
	public static OPConversationThreadDelegate mConversationThreadDelegate;
	public static OPCacheDelegate mCacheDelegate;
	public static OPSettingsDelegate mSettingsDelegate;
	public static OPCall mCall;
	public static OPCallDelegate mCallDelegate;
	public static IChatMessageReceiver mChatMessageReceiver;
	
	public static List<OPIdentityContact> mIdentityContacts = new ArrayList<OPIdentityContact> ();
	public static List<OPMessage> mMessages = new ArrayList<OPMessage> ();
	
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
		//mIdentity = new OPIdentity();
		//mCallbackHandler.registerIdentityDelegate(mIdentity, mIdentityDelegate);

		mIdentity = OPIdentity.login(mAccount, mIdentityDelegate,
				"identity-v1-rel-lespaul-i.hcs.io", 
				"identity://identity-v1-rel-lespaul-i.hcs.io/",
				"http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true");

	}

	public static String createHttpSettings()
	{
		try {
			JSONObject parent = new JSONObject();
			JSONObject jsonObject = new JSONObject();
//"setting-name": {"$type:"bool","#text": "true"}
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

	public static String createForceDashSetting()
	{
		try {
			JSONObject parent = new JSONObject();
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("openpeer/core/authorized-application-id-split-char", "-");
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
			expires.set(30, 8, 2014);
			//rel-dev2
			//jsonObject.put("openpeer/calculated/authorizated-application-id", OPStack.createAuthorizedApplicationID("com.openpeer.nativeApp", "8f1ff375433b6e11026cb806a32ae4d04a59d7b1", expires ));
			//lespaul
			jsonObject.put("openpeer/calculated/authorizated-application-id", OPStack.createAuthorizedApplicationID("com.openpeer.nativeApp", "14b2c9df6713df465d97d0736863c42964faa678", expires ));

			jsonObject.put("openpeer/calculated/user-agent", "OpenPeerNativeSampleApp");
			jsonObject.put("openpeer/calculated/device-id", mDeviceId);
			jsonObject.put("openpeer/calculated/os", android.os.Build.VERSION.RELEASE);
			jsonObject.put("openpeer/calculated/system", android.os.Build.MODEL);
			jsonObject.put("openpeer/calculated/instance-id", mInstanceId);
			parent.put("root", jsonObject);
			Log.d("output", parent.toString(2));
			return parent.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}


	}



	public static void setDeviceId(String deviceId) {
		// TODO Auto-generated method stub
		mDeviceId = deviceId;

	}



	public static void AccountLogin() {
		// TODO Auto-generated method stub
		long stableId = 0;
		if (mAccount == null)
		{

			SharedPreferences sharedPref = OpenPeerApplication.getAppContext().getSharedPreferences(
					OpenPeerApplication.getAppContext().getString( R.string.preference_file_key), Context.MODE_PRIVATE);

			String reloginInfo = sharedPref.getString("/openpeer/reloginInformation", "");

			mAccountDelegate = new OPAccountDelegateImplementation();
			mConversationThreadDelegate = new OPConversationThreadDelegateImplementation();
			mCallDelegate = new OPCallDelegateImplementation();
			if(reloginInfo.length() == 0)
			{

				
				mAccount = OPAccount.login(mAccountDelegate, mConversationThreadDelegate, mCallDelegate, 
						"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html", 
						"bojanGrantID", 
						"identity-v1-rel-lespaul-i.hcs.io", false);

			}
			else
			{
				//stableId = mAccount.getStableID();
				mAccount = OPAccount.relogin(mAccountDelegate, mConversationThreadDelegate, mCallDelegate, 
						"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html", 
						reloginInfo);

			}
		}

	}



	public static void startAccountLogin() {
		// TODO Auto-generated method stub
		if(mLoginHandler!=null)
			mLoginHandler.onLoadOuterFrameHandle("http://jsouter-v1-rel-dev2-i.hcs.io/grant.html");
	}



	public static void initNamespaceGrantInnerFrame() {
		// TODO Auto-generated method stub
		mLoginHandler.onNamespaceGrantInnerFrameInitialized(mAccount.getInnerBrowserWindowFrameURL());
	}



	public static void onAccountStateReady() {
		// TODO Auto-generated method stub
		//Store relogin information
		SharedPreferences sharedPref = OpenPeerApplication.getAppContext().getSharedPreferences(
				OpenPeerApplication.getAppContext().getString( R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("/openpeer/reloginInformation", mAccount.getReloginInformation());
		editor.commit();
		
		OPContact.getForSelf(mAccount);

		//mIdentity = 
		List<OPIdentity> identityList = mAccount.getAssociatedIdentities();
		for (OPIdentity ident :identityList)
		{
			if (!ident.isDelegateAttached())
			{
				mIdentityDelegate = new OPIdentityDelegateImplementation();
				//mIdentity = new OPIdentity();
				//mCallbackHandler.registerIdentityDelegate(ident, mIdentityDelegate);
				ident.attachDelegate(mIdentityDelegate, "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true");
			}
			mIdentity = ident;
		}
		
		//Notigy GUI that account is in ready state
		mLoginHandler.onAccountStateReady();

	}

	public static void onDownloadedRolodexContacts(OPIdentity identity) {
		// TODO Auto-generated method stub
		LoginManager.mIdentityLookupDelegate = new OPIdentityLookupDelegateImplementation();
//		LoginManager.mIdentityLookup = new OPIdentityLookup();
//		mCallbackHandler.registerIdentityLookupDelegate(LoginManager.mIdentityLookup, LoginManager.mIdentityLookupDelegate);

		mLoginHandler.onDownloadedRolodexContacts(identity);

	}



	public static void onIdentityLookupCompleted(OPIdentityLookup lookup) {
		// TODO Auto-generated method stub
		//LoginManager.mIdentityLookup = lookup;
		LoginManager.mIdentityContacts = LoginManager.mIdentityLookup.getUpdatedIdentities();

		mLoginHandler.onLookupCompleted();
	}
}
