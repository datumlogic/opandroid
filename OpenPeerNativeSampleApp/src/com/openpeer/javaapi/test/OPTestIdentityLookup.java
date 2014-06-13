package com.openpeer.javaapi.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.openpeer.delegates.OPIdentityLookupDelegateImplementation;
import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupInfo;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.openpeernativesampleapp.LoginManager;

public class OPTestIdentityLookup {
	
	public static boolean isContactsDownloaded = false;
	public static OPIdentity mIdentity;
	public static boolean isRolodexContactsRefreshed = false;
	//public static OPIdentityLookup mIdentityLookup;
	public static boolean execute (OPIdentity identity)
		{
			try
			{
				if (mIdentity == null)
				{
					Log.d("output", "Identity lookup test Identity = " + identity.toString());
					mIdentity = identity;
				}
				Log.d("output", "Identity lookup test mIdentity = " + mIdentity.toString());
				if(!isContactsDownloaded)
				{
					Log.d("output", "Identity lookup test is preparing, please wait...");
					mIdentity.startRolodexDownload("");
					return false;
				}
				else if (!isRolodexContactsRefreshed)
				{
					Log.d("output", "Rolodex contacts is being refreshed...");
					mIdentity.refreshRolodexContacts();
					return false;
				}
				
				Log.d("output", "Identity lookup test started...");
				OPDownloadedRolodexContacts rolodexContacts = mIdentity.getDownloadedRolodexContacts();
				
				List<OPIdentityLookupInfo> inputLookupList = new ArrayList<OPIdentityLookupInfo>();;
				for(OPRolodexContact contact : rolodexContacts.getRolodexContacts())
				{
					OPIdentityLookupInfo ilInfo = new OPIdentityLookupInfo();
					ilInfo.initWithRolodexContact(contact);
					inputLookupList.add(ilInfo);
					contact.printInfo();
				}
				
				LoginManager.mIdentityLookup = OPIdentityLookup.create(LoginManager.mAccount,
						LoginManager.mIdentityLookupDelegate,
						inputLookupList,
						"identity-v1-rel-lespaul-i.hcs.io");
				LoginManager.mCallbackHandler.registerIdentityLookupDelegate(LoginManager.mIdentityLookup, LoginManager.mIdentityLookupDelegate);
				Log.d("output", "Idenity lookup test RolodexContacts = " + Arrays.deepToString(rolodexContacts.getRolodexContacts().toArray()));
				Log.d("output", "Identity lookup test Send lookup");
				
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
			
		}
	
}
