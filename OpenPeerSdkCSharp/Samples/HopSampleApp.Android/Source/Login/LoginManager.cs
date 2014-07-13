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
		#region classes
		public static CallbackHandler mCallbackHandler = new CallbackHandler();
		public static Context mContext;
		public static OPAccount mAccount;
		public static OPAccountDelegate mAccountDelegate;
		public static OPIdentity mIdentity;
		public static OPIdentityLookupDelegate mIdentityLookupDelegate;
		public static OPIdentityDelegate mIdentityDelegate;
		public static CSOPDatastoreDelegate mDataStoreDelegate;
		public static LoginHandlerInterface mLoginHandler;
		public static OPIdentityLookup mIdentityLookup;
		public static OPCacheDelegate mCacheDelegate;
		#endregion

		public static OPAccount getAccount()
		{
			return mAccount;
		}

		public static void setAccount(OPAccount account)
		{
			LoginManager.mAccount = account;
		}

		#region Op lists
		public static List<OPIdentityContact> mSelfContacts;
		public static List<OPIdentity> mIdentitiesList;
		public static List<OPIdentityContact> mIdentityContacts = new List<OPIdentityContact> ();
		public static IList<OPRolodexContact> mRolodexContact = new List<OPRolodexContact> ();
		public static List<OPMessage> mMessages = new List<OPMessage> ();
		#endregion

		public void setHandlerListener(LoginHandlerInterface listener)
		{
			mLoginHandler=listener;
		}

		#region Initialize Context			
		public static void initializeContext(Context context)
		{
			mContext = context;
		}
		#endregion

		#region Simple app Login
		public void Login()
		{   
			CSOPHelper.SharedCSOPHelper ().InitializeSettings (mContext,mCallbackHandler,mDataStoreDelegate);
			mCacheDelegate = new CSOPCacheDelegate();
			mCallbackHandler.RegisterCacheDelegate(mCacheDelegate);
			OPCache.Setup(mCacheDelegate);

			mAccountDelegate = new CSOPAccountDelegate ();
			mAccount = new OPAccount ();
			mCallbackHandler.RegisterAccountDelegate (mAccount,mAccountDelegate);
			CSSettings settings = CSSettings.SharedCSSettings();
			mAccount = OPAccount.Login
				(null, null, null,
					settings.getNamespaceGrantServiceUrl(),
					settings.getGrantID(), 
					settings.getIdentityProviderDomain(),
					false);

			CSOPDataManager.SharedCSOPDataManager ().setSharedAccount (mAccount);
			StartIdentityLogin ();
		}
		#endregion

		#region Simple app Identity Login
		public void StartIdentityLogin()
		{

			mIdentityDelegate = new CSOPIdentityDelegate();
			mIdentity = new OPIdentity();
			mCallbackHandler.RegisterIdentityDelegate(mIdentity, mIdentityDelegate);
			CSSettings settings = CSSettings.SharedCSSettings();
			settings.getIdentityBaseUri ();
			mIdentity = OPIdentity.Login(
				mAccount,
				null,
				settings.getIdentityProviderDomain(),
				settings.getIdentityBaseUri(),
				settings.getOuterFrameURL()
			);

		}
		#endregion

		#region Simple app ReLogin
		public void ReLogin(String relogininfo)
		{
			mAccountDelegate = new CSOPAccountDelegate();
			if (CSOPDataManager.SharedCSOPDataManager().getSharedAccount() != null)
			{
				mAccount = CSOPDataManager.SharedCSOPDataManager().getSharedAccount();
			} 
			else
			{
				mAccount = new OPAccount();
			}

			mAccount = OPAccount.Relogin (null, null, null, "http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html", relogininfo);

			mCallbackHandler.RegisterAccountDelegate (mAccount,mAccountDelegate);

			CSOPDataManager.SharedCSOPDataManager().setSharedAccount(mAccount);

		}
		#endregion


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

