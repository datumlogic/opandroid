package com.openpeer.javaapi.test;

import java.util.ArrayList;
import java.util.List;

import com.openpeer.delegates.OPCallDelegateImplementation;
import com.openpeer.delegates.OPConversationThreadDelegateImplementation;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPContactProfileInfo;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.openpeernativesampleapp.LoginManager;

import android.util.Log;

public class OPTestConversationThread {

	public static List<OPIdentityContact> selfContacts = new ArrayList<OPIdentityContact>();
	public static List<OPIdentityContact> callContacts = new ArrayList<OPIdentityContact>();
	public static List<OPContact> contacts = new ArrayList<OPContact>();
	public static List<OPContactProfileInfo> contactProfiles = new ArrayList<OPContactProfileInfo>();
	public static boolean isCall = false;
	public static boolean execute ()
	{
		try
		{
			Log.d("output", "Conversation thread test started...");
			OPIdentityContact self = LoginManager.mIdentity.getSelfIdentityContact();
			selfContacts.add(self);

			OPConversationThreadDelegate delegate = new OPConversationThreadDelegateImplementation();
			LoginManager.mCallbackHandler.registerConversationThreadDelegate(delegate);
			LoginManager.mConvThread = OPConversationThread.create(LoginManager.mAccount, selfContacts);


			List<OPIdentityContact> updated = LoginManager.mIdentityLookup.getUpdatedIdentities();

			for(OPIdentityContact idContact : updated)
			{
				if (idContact.getName().toLowerCase().contains("sire"))
				{
					Log.d("output", "ID contact name = " + idContact.getName());
					Log.d("output", "ID contact peerFile = " + idContact.getPeerFilePublic().getPeerFileString());
					Log.d("output", "ID contact identity URI = " + idContact.getIdentityURI());
					Log.d("output", "ID contact profile URL = " + idContact.getProfileURL());
					Log.d("output", "ID contact V profile URL = " + idContact.getVProfileURL());
					callContacts.add(idContact);


					OPContactProfileInfo info = new OPContactProfileInfo();
					OPContact newContact = OPContact.createFromPeerFilePublic(LoginManager.mAccount, idContact.getPeerFilePublic().getPeerFileString());

					info.setIdentityContacts(callContacts);
					info.setContact(newContact);

					contactProfiles.add(info);

					LoginManager.mConvThread.addContacts(contactProfiles);


				}
			}


			Log.d("output", "Conversation thread test PASSED");				
			return true;
		}
		catch (Exception e)
		{
			return false;
		}

	}

	public static boolean testCall(String message)
	{
		try
		{
			Log.d("output", "Call test started...");



			if(!isCall)
			{
				LoginManager.mConvThread.sendMessage(java.util.UUID.randomUUID().toString(), "text/x-application-hookflash-message-text", message, false);
				contacts = LoginManager.mConvThread.getContacts();

				Log.d("output", "AFTER ADD contacts size = " + contacts.size());
				OPContact callContact = new OPContact();

				for (OPContact iter : contacts)
				{
					if (iter.isSelf())
					{
						Log.d("output", "Evo jedan je self = " + iter.getPeerURI());
					}
					else
					{
						callContact = iter;
						Log.d("output", "ovaj nije self = " + iter.getPeerURI());
					}
				}
				LoginManager.mCallDelegate = new OPCallDelegateImplementation();

				Log.d("output", "java contact peer uri = "+callContact.getPeerURI());
				Log.d("output", "java contact peer file = "+callContact.getPeerFilePublic());
				Log.d("output", "stable Id = " + callContact.getStableID());

				LoginManager.mCallbackHandler.registerCallDelegate(LoginManager.mCall, LoginManager.mCallDelegate);
				LoginManager.mCall = OPCall.placeCall(LoginManager.mConvThread, callContact, true, false);
				isCall = true;
				Log.d("output", "Call test PASSED");	
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}

	}

}
