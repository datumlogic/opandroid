package com.openpeer.javaapi.test;

import java.util.ArrayList;
import java.util.List;

import com.openpeer.sample.delegates.OPCallDelegateImplementation;
import com.openpeer.sample.delegates.OPConversationThreadDelegateImplementation;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.sample.login.LoginManager;

import android.util.Log;

public class OPTestConversationThread {

	public static List<OPIdentityContact> callContacts = new ArrayList<OPIdentityContact>();
	public static List<OPContact> contacts = new ArrayList<OPContact>();
	public static boolean isCall = false;
	public static boolean execute ()
	{
		try
		{
			Log.d("output", "Conversation thread test started...");
			List<OPIdentityContact> updated = LoginManager.mIdentityLookup.getUpdatedIdentities();

			for(OPIdentityContact idContact : updated)
			{
				Log.d("output","OPTestConversationThread identityContact "+idContact);
//				if (idContact.getName().contains("Kocic"))
				if(null!=idContact.getPeerFilePublic())
				{
					callContacts.add(idContact);
					OPConversationThreadDelegate delegate = new OPConversationThreadDelegateImplementation();
					LoginManager.mCallbackHandler.registerConversationThreadDelegate(delegate);
				}
				LoginManager.mConvThread = OPConversationThread.create(LoginManager.mAccount, callContacts);
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

			//LoginManager.mConvThread.sendMessage(java.util.UUID.randomUUID().toString(), "text/x-application-hookflash-message-text", message, false);

			if(!isCall)
			{
				contacts = LoginManager.mConvThread.getContacts();
				LoginManager.mCallDelegate = new OPCallDelegateImplementation();
				Log.d("output", "contact size = " + contacts.size());
				Log.d("output", contacts.get(0).getPeerURI());
				Log.d("output", contacts.get(0).getPeerFilePublic());
				Log.d("output", "stable Id = " + contacts.get(0).getStableID());
				List<OPIdentityContact> identityContactList = new ArrayList<OPIdentityContact>();
				identityContactList = LoginManager.mConvThread.getIdentityContactList(contacts.get(0));
				Log.d("output", "contact size = " + identityContactList.size());
				Log.d("output", identityContactList.get(0).getName());
				Log.d("output", identityContactList.get(0).getIdentityURI());
				
				LoginManager.mCallbackHandler.registerCallDelegate(LoginManager.mCall, LoginManager.mCallDelegate);
				LoginManager.mCall = OPCall.placeCall(LoginManager.mConvThread, contacts.get(0), true, false);
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
