
using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Util;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;

namespace HopSampleApp
{
	class CSOPTestIdentityLookup
	{
		public static Boolean isContactsDownloaded = false;
		public static OPIdentity mIdentity;
		public static Boolean isRolodexContactsRefreshed = false;
		//public static OPIdentityLookup mIdentityLookup;
		public static Boolean execute (OPIdentity identity)
		{
			try
			{
				if (mIdentity == null)
				{
					Log.Debug("output", "Identity lookup test Identity = " + identity.ToString());
					mIdentity = identity;
				}
				Log.Debug("output", "Identity lookup test mIdentity = " + mIdentity.ToString());
				if(!isContactsDownloaded)
				{
					Log.Debug("output", "Identity lookup test is preparing, please wait...");
					mIdentity.StartRolodexDownload("");
					return false;
				}
				// else if (!isRolodexContactsRefreshed)
				// {
				// Log.d("output", "Rolodex contacts is being refreshed...");
				// mIdentity.refreshRolodexContacts();
				// return false;
				// }

				Log.Debug("output", "Identity lookup test started...");
				OPDownloadedRolodexContacts rolodexContacts = mIdentity.DownloadedRolodexContacts;

				IList<OPIdentityLookupInfo> inputLookupList = new List<OPIdentityLookupInfo>();;

				foreach(OPRolodexContact contact in rolodexContacts.RolodexContacts)
				{
					OPIdentityLookupInfo ilInfo = new OPIdentityLookupInfo();
					ilInfo.InitWithRolodexContact(contact);
					inputLookupList.Add(ilInfo);
					LoginManager.mRolodexContact.Add (contact);
					contact.PrintInfo();
				}

				LoginManager.mIdentityLookup = OPIdentityLookup.Create(LoginManager.mAccount,
					LoginManager.mIdentityLookupDelegate,
					inputLookupList,
					"identity-v1-rel-lespaul-i.hcs.io");
				LoginManager.mCallbackHandler.RegisterIdentityLookupDelegate(LoginManager.mIdentityLookup, LoginManager.mIdentityLookupDelegate);
				Log.Debug("output", "Idenity lookup test RolodexContacts = " + Java.Util.Arrays.DeepToString(rolodexContacts.RolodexContacts.ToArray()));
				Log.Debug("output", "Identity lookup test Send lookup");

				return true;
			}
			catch (Exception e)
			{
				return false;
			}

		}
	}
}

