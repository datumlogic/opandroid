
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
	class CSOPTestAccount
	{
		public static Boolean execute (OPAccount account)
		{
			try
			{
				Log.Debug("output", "Account test started...");

				long stableID = 0;
				stableID = account.StableID;
				if (stableID == 0)
				{
					Log.Debug("output", "Account test FAILED stableID = " + stableID);
					return false;
				}
				Log.Debug("output", "Account test stableID = " + stableID);

				AccountStates state = AccountStates.AccountStatePending;
				int outErrorCode = 0;
				String outErrorReason = "";
				state = account.GetState(outErrorCode, outErrorReason );
				if (state == AccountStates.AccountStatePending)
				{
					Log.Debug("output",String.Format("Account test FAILED state = {0}", state.ToString()));
					return false;
				}
				Log.Debug("output",String.Format("Account test state = {0}",state.ToString()));

				String reloginInfo = "";
				reloginInfo = account.ReloginInformation;
				if(reloginInfo == "")
				{
					Log.Debug("output",String.Format("Account test FAILED reloginInfo = {0} ", reloginInfo.ToString()));
					return false;
				}
				Log.Debug("output", "Account test reloginInfo = " + reloginInfo.ToString());

				String locationID = "";
				locationID = account.LocationID;
				if(locationID == "")
				{
					Log.Debug("output",String.Format("Account test FAILED locationID = {0} ", locationID.ToString()));
					return false;
				}
				Log.Debug("output", "Account test locationID = " + locationID.ToString());

				String peerFilePrivate = "";
				peerFilePrivate = account.PeerFilePrivate;
				if(peerFilePrivate == "")
				{
					Log.Debug("output",String.Format("Account test FAILED peerFilePrivate = {0} ", peerFilePrivate.ToString()));
					return false;
				}
				Log.Debug("output",String.Format("Account test peerFilePrivate = {0}" + peerFilePrivate.ToString()));

				byte[] peerFilePrivateSecret;;
				peerFilePrivateSecret = account.GetPeerFilePrivateSecret();
				if(peerFilePrivateSecret.Length == 0)
				{
					Log.Debug("output",String.Format("Account test FAILED peerFilePrivateSecret = {0} ", peerFilePrivateSecret.ToString()));
					return false;
				}
				Log.Debug("output",String.Format("Account test peerFilePrivateSecret = {0} ", peerFilePrivateSecret.ToString()));
				Log.Debug("output",String.Format("Account test peerFilePrivateSecret = {0} ",Java.Util.Arrays.ToString(peerFilePrivateSecret)));

				List<OPIdentity> identities = account.AssociatedIdentities.ToList();
				int Size=identities.Count();
				if(Size == 0)
				{
					Log.Debug("output",String.Format("Account test FAILED identities = {0}",Java.Util.Arrays.DeepToString(identities.ToArray())));
					return false;
				}
				Log.Debug("output",String.Format("Account test identities ={0}",identities.ToString()));

				Log.Debug("output", "Account test PASSED");
				Log.Debug("output", "Calling Identity test...");

				CSOPTestIdentity.execute(identities[0]);
				return true;
			}
			catch (Exception e)
			{
				return false;
			}

		}
	}
}

