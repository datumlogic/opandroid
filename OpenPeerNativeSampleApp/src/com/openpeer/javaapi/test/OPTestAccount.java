package com.openpeer.javaapi.test;

import java.util.Arrays;

import android.util.Log;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.OPAccount;

public class OPTestAccount {
	public static boolean execute (OPAccount account)
	{
		try
		{
			long stableID = 0;
			stableID = account.getStableID();
			if (stableID == 0)
			{
				Log.d("output", "Account test FAILED stableID = " + stableID);
				return false;
			}
			Log.d("output", "Account test stableID = " + stableID);
			
			AccountStates state = AccountStates.AccountState_Pending;
			int outErrorCode = 0;
			String outErrorReason = "";
			state = account.getState(outErrorCode, outErrorReason );
			if (state == AccountStates.AccountState_Pending)
			{
				Log.d("output", "Account test FAILED state = " + state.toString());
				return false;
			}
			Log.d("output", "Account test state = " + state.toString());
			
			String reloginInfo = "";
			reloginInfo = account.getReloginInformation();
			if(reloginInfo == "")
			{
				Log.d("output", "Account test FAILED reloginInfo = " + reloginInfo.toString());
				return false;
			}
			Log.d("output", "Account test reloginInfo = " + reloginInfo.toString());
			
			String locationID = "";
			locationID = account.getLocationID();
			if(locationID == "")
			{
				Log.d("output", "Account test FAILED locationID = " + locationID.toString());
				return false;
			}
			Log.d("output", "Account test locationID = " + locationID.toString());
			
			String peerFilePrivate = "";
			peerFilePrivate = account.getPeerFilePrivate();
			if(peerFilePrivate == "")
			{
				Log.d("output", "Account test FAILED peerFilePrivate = " + peerFilePrivate.toString());
				return false;
			}
			Log.d("output", "Account test peerFilePrivate = " + peerFilePrivate.toString());
			
			byte[] peerFilePrivateSecret;;
			peerFilePrivateSecret = account.getPeerFilePrivateSecret();
			if(peerFilePrivateSecret.length == 0)
			{
				Log.d("output", "Account test FAILED peerFilePrivateSecret = " + peerFilePrivateSecret.toString());
				return false;
			}
			Log.d("output", "Account test peerFilePrivateSecret = " + peerFilePrivateSecret.toString());
			Log.d("output", "Account test peerFilePrivateSecret = " + Arrays.toString(peerFilePrivateSecret));
			
			
			
			
			Log.d("output", "Account test PASSED");
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		
	}
}
