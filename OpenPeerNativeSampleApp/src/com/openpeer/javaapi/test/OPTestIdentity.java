package com.openpeer.javaapi.test;

import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.IdentityStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;

public class OPTestIdentity {
		public static boolean execute (OPIdentity identity)
		{
			try
			{
				Log.d("output", "Identity test started...");
				
				long stableID = 0;
				stableID = identity.getStableID();
				if (stableID == 0)
				{
					Log.d("output", "Identity test FAILED stableID = " + stableID);
					return false;
				}
				Log.d("output", "Identity test stableID = " + stableID);
				
				IdentityStates state = IdentityStates.IdentityState_Pending;
				int outErrorCode = 0;
				String outErrorReason = "";
				state = identity.getState(outErrorCode, outErrorReason );
				if (state == IdentityStates.IdentityState_Pending)
				{
					Log.d("output", "Identity test FAILED state = " + state.toString());
					return false;
				}
				Log.d("output", "Identity test state = " + state.toString());
				
				boolean isDelegateAttached = identity.isDelegateAttached();
				Log.d("output", "Identity test isDelegateAttached = " + isDelegateAttached);
				
				String identityUri = "";
				identityUri = identity.getIdentityURI();
				if(identityUri == "")
				{
					Log.d("output", "Identity test FAILED identityUri = " + identityUri.toString());
					return false;
				}
				Log.d("output", "Identity test identityUri = " + identityUri.toString());
				
				String identityProviderDomain = "";
				identityProviderDomain = identity.getIdentityProviderDomain();
				if(identityProviderDomain == "")
				{
					Log.d("output", "Identity test FAILED identityProviderDomain = " + identityProviderDomain.toString());
					return false;
				}
				Log.d("output", "Identity test identityProviderDomain = " + identityProviderDomain.toString());
				
				OPIdentityContact contact = identity.getSelfIdentityContact();
				if (contact == null)
				{
					Log.d("output", "Identity test FAILED contact = NULL");
					return false;
				}
				Log.d("output", "Identity test contact = " + contact.toString());
				
				String innerBrowserFrameURL = "";
				innerBrowserFrameURL = identity.getInnerBrowserWindowFrameURL();
				if(innerBrowserFrameURL == "")
				{
					Log.d("output", "Identity test FAILED innerBrowserFrameURL = " + innerBrowserFrameURL.toString());
					return false;
				}
				Log.d("output", "Identity test innerBrowserFrameURL = " + innerBrowserFrameURL.toString());
				
				Log.d("output", "Identity test PASSED");
				
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
			
		}
	
}
