package com.openpeer.javaapi.test;

import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;

public class OPTestIdentityLookup {
	
	public static boolean isContactsDownloaded = false;
	public static OPIdentity mIdentity;
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
				
				Log.d("output", "Identity lookup test started...");
				OPDownloadedRolodexContacts rolodexContacts = mIdentity.getDownloadedRolodexContacts();
				Log.d("output", "Idenity lookup test RolodexContacts = " + Arrays.deepToString(rolodexContacts.getRolodexContacts().toArray()));
				Log.d("output", "Identity lookup test PASSED");
				
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
			
		}
	
}
