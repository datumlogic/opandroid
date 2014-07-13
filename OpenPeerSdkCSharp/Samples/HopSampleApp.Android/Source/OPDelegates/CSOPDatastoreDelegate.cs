
using System;
using System.Collections.Generic;
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
	public class CSOPDatastoreDelegate:DatastoreInterfaces
	{
		Context mContext;
		private static String PREF_DATASTORE = "model_data";
		private static String PREF_KEY_RELOGIN_INFO = "relogin_info";
		private static String PREF_KEY_HOMEUSER_STABLEID = "homeuser_stable_id";

		private ISharedPreferences mPreferenceStore; 

		private CSOPDatastoreDelegate(Context context)
		{
			mContext = context;

			mPreferenceStore = context.GetSharedPreferences(PREF_DATASTORE,FileCreationMode.Private);
		}

		public CSOPDatastoreDelegate init(Context context)
		{
			mContext = context;
			mPreferenceStore = context.GetSharedPreferences(PREF_DATASTORE,FileCreationMode.Private);
			return this;

		}

		public String getReloginInfo()
		{
			return IsolatedStorageSettings.StringForKey (PREF_KEY_RELOGIN_INFO,"RELOGIN");

		}

		public Boolean saveOrUpdateAccount(OPAccount account)
		{
			Log.Debug("test","DatastoreDelegate saving account id " + account.StableID	+ " relogin " + account.ReloginInformation);
			IsolatedStorageSettings.AddSettingsToSpecificSettings ("RELOGIN",PREF_KEY_RELOGIN_INFO,account.ReloginInformation);
			IsolatedStorageSettings.AddSettingsToSpecificSettings ("HOMEUSER_STABLEID",PREF_KEY_HOMEUSER_STABLEID,account.StableID);


			return true;
		}









		#region Singleton pattern

		private static CSOPDatastoreDelegate instance;

		private CSOPDatastoreDelegate(){	}

		public static CSOPDatastoreDelegate SharedCSOPDatastoreDelegate()
		{
			if (instance == null)
				instance = new CSOPDatastoreDelegate();
			return instance;
		}

		#endregion
	}
}

