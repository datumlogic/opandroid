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
using Android.Util;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;

namespace HopSampleApp
{
	public class CSStatesHandler
	{
		#region Start Account Login
		public void startAccountLogin()
		{
			if (LoginManager.mLoginHandler != null)
			{
				LoginManager.mLoginHandler.onLoadOuterFrameHandle ("http://jsouter-v1-rel-dev2-i.hcs.io/grant.html");
			}		
		}
		#endregion

		#region On Identity Lookup Completed
		public void onIdentityLookupCompleted(OPIdentityLookup lookup)
		{

			LoginManager.mIdentityLookup = lookup;
			LoginManager.mIdentityContacts = LoginManager.mIdentityLookup.UpdatedIdentities.ToList();

			LoginManager.mLoginHandler.onIdentityLookupCompleted();
		}
		#endregion

		#region On Downloaded Rolodex Contacts
		public void onDownloadedRolodexContacts(OPIdentity identity)
		{
			LoginManager.mIdentityLookupDelegate = new CSOPIdentityLookupDelegate();
			LoginManager.mIdentityLookup = new OPIdentityLookup();
			LoginManager.mCallbackHandler.RegisterIdentityLookupDelegate(LoginManager.mIdentityLookup,LoginManager.mIdentityLookupDelegate);

			LoginManager.mLoginHandler.onDownloadedRolodexContacts(identity);

		}
		#endregion

		#region On Account State Ready
		public void onAccountStateReady()
		{
			onAStateReady (LoginManager.mAccount);
		}
		#endregion

		#region On AStateReady
		public void onAStateReady(OPAccount account)
		{
			CSOPDataManager.SharedCSOPDataManager().setSharedAccount(account);
			List<OPIdentity> identityList = LoginManager.mAccount.AssociatedIdentities.ToList();
			if (identityList.Count == 0)
			{
				Log.Debug("Identity List Test", "Account test FAILED identities emppty ");

				return;
			}

			foreach (OPIdentity identity in identityList)
			{
				if (!identity.IsDelegateAttached)
				{
					LoginManager.mIdentityDelegate = new CSOPIdentityDelegate ();
					LoginManager.mCallbackHandler.RegisterIdentityDelegate (identity,LoginManager.mIdentityDelegate);
					identity.AttachDelegate (LoginManager.mIdentityDelegate, "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true");
				}
				else 
				{
					setIdentities (identityList);
					LoginManager.mIdentity.StartRolodexDownload ("");
					LoginManager.mLoginHandler.onIdentityLookupCompleted ();

					LoginManager.mCallbackHandler.UnregisterAccountDelegate (LoginManager.mAccountDelegate);
				}
				//mIdentity = identity;
			}


			LoginManager.mLoginHandler.onAccountStateReady();

		}
		#endregion

		#region On Identity State Ready
		public void onIdentityStateReady()
		{
			Log.Debug ("STATE READY","IDENTITY STATE READY LOGIC LOADED");
			if (LoginManager.mAccount.GetState(0, "") == AccountStates.AccountStateReady)
			{
				setIdentities(LoginManager.mAccount.AssociatedIdentities.ToList());

				Log.Debug("login", "start download initial contacts");
				LoginManager.mIdentity.StartRolodexDownload ("");


			}
		}
		#endregion

		#region Methods that i need to move i datalayer
		public void setIdentities(List<OPIdentity> identities)
		{
			LoginManager.mIdentitiesList = identities;

			LoginManager.mSelfContacts = new List<OPIdentityContact>();
			foreach (OPIdentity identity in identities)
			{
				LoginManager.mSelfContacts.Add(identity.SelfIdentityContact);
			}
			//store in database
		}
		#endregion


		#region Singleton pattern

		private static CSStatesHandler instance;

		private CSStatesHandler(){	}

		public static CSStatesHandler SharedCSStatesHandler()
		{
			if (instance == null)
				instance = new CSStatesHandler();
			return instance;
		}

		#endregion
	}
}

