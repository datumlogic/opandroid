
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
	 class CSOPDataManager
	{
		private OPAccount mAccount;
		private String mReloginInfo;
		private List<OPIdentity> mIdentities;
		private List<OPIdentityContact> mSelfContacts;
		private DatastoreInterfaces mDatastoreDelegate;

		public String getReloginInfo()
		{
			return mReloginInfo;
		}

		public void setSharedAccount(OPAccount account)
		{
			mAccount = account;
			mDatastoreDelegate.saveOrUpdateAccount (account);
		
		}

		public OPAccount getSharedAccount()
		{
			return mAccount;
		}

		public void setIdentityContacts(long identityId,OPDownloadedRolodexContacts downloadedContacts)
		{
			LoginManager.mRolodexContact = downloadedContacts.RolodexContacts.ToList();

			if (LoginManager.mRolodexContact == null)
			{
				return;
			}
			//dblogic

		}


		public void init(CSOPDatastoreDelegate datastore)
		{

			//assert (datastore != null);
			mDatastoreDelegate = datastore;
			mReloginInfo = datastore.getReloginInfo();
			Log.Debug("test", "LoginManager.init relogin info " + mReloginInfo);

		}

		#region Singleton pattern

		private static CSOPDataManager instance;

		private CSOPDataManager(){	}

		public static CSOPDataManager SharedCSOPDataManager()
		{
			if (instance == null)
				instance = new CSOPDataManager();
			return instance;
		}

		#endregion
	}
}

