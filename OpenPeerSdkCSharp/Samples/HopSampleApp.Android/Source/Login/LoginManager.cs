/*
Copyright (c) 2014, hookflash Inc.
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
using HopSampleApp;

namespace HopSampleApp
{
	class LoginManager
	{
		public static CallbackHandler mCallbackHandler = new CallbackHandler();
		public static Context mContext;

		public void LoginWithFacebook ()
		{
			mediaenginedelegate = new CSOPMediaEngineDelegate();
			stackdelegate = new CSOPStackDelegate();
			Log.Debug ("output","instance ID =" + Utility.GetGUIDInstanceID());
			stackMessageQueue = OPStackMessageQueue.Singleton(); 
			OPLogger.SetLogLevel (OPLogLevel.LogLevelTrace);
			OPLogger.SetLogLevel("openpeer_webrtc", OPLogLevel.LogLevelBasic);
			OPLogger.SetLogLevel("zsLib_socket", OPLogLevel.LogLevelInsane);
			String telnetLogString = Utility.GetDeviceID(mContext) + "-" + Utility.GetGUIDInstanceID() + "\n";
			Log.Debug("output", "Outgoing log string = " + telnetLogString);
			OPLogger.InstallTelnetLogger(59999, 60, true);
			OPLogger.InstallFileLogger("/storage/emulated/0/HFLog.txt", true);
			stack = OPStack.Singleton();

			mCacheDelegate = new CSOPCacheDelegate();
			mCallbackHandler.RegisterCacheDelegate(mCacheDelegate);
			OPCache.Setup(mCacheDelegate);

			//OPSettings.setup(null);
			OPSettings.ApplyDefaults();

			String httpSettings =CSOPFakeSettings.createHttpSettings();
			OPSettings.Apply(httpSettings);

			String forceDashSettings =CSOPFakeSettings.createForceDashSetting();
			OPSettings.Apply(forceDashSettings);

			String appSettings = CSOPFakeSettings.createFakeApplicationSettings();
			OPSettings.Apply(appSettings);

			stack.Setup(stackdelegate,mediaenginedelegate);

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
		//public static OPMediaEngine mMediaEngine;
		//public static OPIdentityLookupDelegate mIdentityLookupDelegate;
		//public static OPLogger mLogger;
		static LoginHandlerInterface mLoginHandler;
		public static OPIdentityLookup mIdentityLookup;
		//public static OPConversationThread mConvThread;
		public static OPCacheDelegate mCacheDelegate;
		//public static OPCall mCall;
		public static OPStackDelegate stackdelegate;
		public static OPMediaEngineDelegate mediaenginedelegate;
		//public static OPCallDelegate mCallDelegate;

		public void setHandlerListener(LoginHandlerInterface listener)
		{
			mLoginHandler=listener;
		}

		public void loadOuterFrame() 
		{
			if (mLoginHandler != null)
			{
				mLoginHandler.onLoadOuterFrameHandle (null);
			}

		}

		public void initInnerFrame()
		{
			//Java - mLoginHandler.onInnerFrameInitialized(mIdentity.getInnerBrowserWindowFrameURL());
			mLoginHandler.onInnerFrameInitialized(mIdentity.InnerBrowserWindowFrameURL);
		}


		public void pendingMessageForInnerFrame()
		{
			//Java - String msg = mIdentity.getNextMessageForInnerBrowerWindowFrame();
			String msg = mIdentity.NextMessageForInnerBrowerWindowFrame;
			mLoginHandler.passMessageToJS(msg);

		}

		public void pendingMessageForNamespaceGrantInnerFrame()
		{
			//Java - String msg = mIdentity.getNextMessageForInnerBrowerWindowFrame();
			String msg = mAccount.NextMessageForInnerBrowerWindowFrame;
			mLoginHandler.passMessageToJS(msg);

		}

		public void StartIdentityLogin()
		{
			mIdentityDelegate = new CSOPIdentityDelegate();
			mIdentity = new OPIdentity();
			mCallbackHandler.RegisterIdentityDelegate(mIdentity, mIdentityDelegate);

			mIdentity = OPIdentity.Login(mAccount, null,
				"identity-v1-beta-1-i.hcs.io", 
				"identity://identity-v1-beta-1-i.hcs.io/",
				"http://jsouter-v1-beta-1-i.hcs.io/identity.html?view=choose&federated=false?reload=true");

		}
			
		public static void initializeContext(Context context)
		{
			mContext = context;
		}

		public void AccountLogin()
		{
			mAccountDelegate = new CSOPAccountDelegate();
			mAccount = new OPAccount();
			mCallbackHandler.RegisterAccountDelegate(mAccount, mAccountDelegate);

			mAccount = OPAccount.Login(null, null, null, 
				"http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html", 
				"bojanGrantID", 
				"identity-v1-rel-lespaul-i.hcs.io", false);

		}

		public void startAccountLogin()
		{
			if (mLoginHandler != null)
			{
				mLoginHandler.onLoadOuterFrameHandle ("http://jsouter-v1-rel-dev2-i.hcs.io/grant.html");
			}		
		}

		public void initNamespaceGrantInnerFrame()
		{
			//Java - mLoginHandler.onNamespaceGrantInnerFrameInitialized(mAccount.getInnerBrowserWindowFrameURL());
			mLoginHandler.onNamespaceGrantInnerFrameInitialized(mAccount.InnerBrowserWindowFrameURL);
		}

		public void onIdentityLookupCompleted(OPIdentityLookup lookup) {

			LoginManager.mIdentityLookup = lookup;

			mLoginHandler.onLookupCompleted();
		}

		public void onDownloadedRolodexContacts(OPIdentity identity) {

			//LoginManager.mIdentityLookupDelegate = new OPIdentityDelegateImplementation();
			//LoginManager.mIdentityLookup = new OPIdentityLookup();
			//mCallbackHandler.RegisterIdentityLookupDelegate(LoginManager.mIdentityLookup, LoginManager.mIdentityLookupDelegate);

			//mLoginHandler.onDownloadedRolodexContacts(identity);

		}
		public void onAccountStateReady() {

			mLoginHandler.onAccountStateReady();

		}

		#region Singleton pattern

		private static LoginManager instance;

		private LoginManager(){	}

		public static LoginManager SharedLoginManager()
		{
			if (instance == null)
				instance = new LoginManager();
			return instance;
		}

		#endregion
	}
}

