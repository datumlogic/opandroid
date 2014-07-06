
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
	class CSOPTestIdentity
	{
		public static Boolean execute (OPIdentity identity)
		{
			try
			{
				Log.Debug("output", "Identity test started...");

				long stableID = 0;
				stableID = identity.StableID;
				if (stableID == 0)
				{
					Log.Debug("output", "Identity test FAILED stableID = " + stableID);
					return false;
				}
				Log.Debug("output", "Identity test stableID = " + stableID);

				IdentityStates state = IdentityStates.IdentityStatePending;
				int outErrorCode = 0;
				String outErrorReason = "";
				state = identity.GetState(outErrorCode, outErrorReason );
				if (state == IdentityStates.IdentityStatePending)
				{
					Log.Debug("output", "Identity test FAILED state = " + state.ToString());
					return false;
				}
				Log.Debug("output", "Identity test state = " + state.ToString());

				Boolean isDelegateAttached = identity.IsDelegateAttached;
				Log.Debug("output", "Identity test isDelegateAttached = " + isDelegateAttached);

				String identityUri = "";
				identityUri = identity.IdentityURI;
				if(identityUri == "")
				{
					Log.Debug("output", "Identity test FAILED identityUri = " + identityUri.ToString());
					return false;
				}
				Log.Debug("output", String.Format("Identity test identityUri = {0}", identityUri.ToString()));

				String identityProviderDomain = "";
				identityProviderDomain = identity.IdentityProviderDomain;
				if(identityProviderDomain == "")
				{
					Log.Debug("output", "Identity test FAILED identityProviderDomain = " + identityProviderDomain.ToString());
					return false;
				}
				Log.Debug("output", "Identity test identityProviderDomain = " + identityProviderDomain.ToString());

				OPIdentityContact contact = identity.SelfIdentityContact;
				if (contact == null)
				{
					Log.Debug("output", "Identity test FAILED contact = NULL");
					return false;
				}
				Log.Debug("output", "Identity test contact = " + contact.ToString());

				String innerBrowserFrameURL = "";
				innerBrowserFrameURL = identity.InnerBrowserWindowFrameURL;
				if(innerBrowserFrameURL == "")
				{
					Log.Debug("output", "Identity test FAILED innerBrowserFrameURL = " + innerBrowserFrameURL.ToString());
					return false;
				}
				Log.Debug("output", "Identity test innerBrowserFrameURL = " + innerBrowserFrameURL.ToString());

				Log.Debug("output", "Identity test PASSED");

				Log.Debug("output", "Calling Identity lookup test...");

				CSOPTestIdentityLookup.execute(identity);

				return true;
			}
			catch (Exception e)
			{
				return false;
			}

		}
	}
}

