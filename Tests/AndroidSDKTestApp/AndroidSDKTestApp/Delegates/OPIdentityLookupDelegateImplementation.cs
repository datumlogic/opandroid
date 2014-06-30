
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
namespace AndroidSDKTestApp
{
	public class OPIdentityLookupDelegateImplementation:OPIdentityLookupDelegate
	{
		public override void OnIdentityLookupCompleted (OPIdentityLookup lookup)
		{
			Log.Debug("output", "Identity lookup test PASSED");
			LoginManager.onIdentityLookupCompleted(lookup);
		}
	}
}

