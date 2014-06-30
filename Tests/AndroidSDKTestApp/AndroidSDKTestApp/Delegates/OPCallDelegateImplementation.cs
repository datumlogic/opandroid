
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
namespace AndroidSDKTestApp
{
	public class OPCallDelegateImplementation:OPCallDelegate
	{
		public override void OnCallStateChanged (OPCall call, CallStates state)
		{
			Log.Debug("output", "Call State = " + state.ToString());
		}
	}
}

