
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.Text.Format;
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
	public class OPCacheDelegateImplementation:OPCacheDelegate
	{
		public override string Fetch (String cookieNamePath)
		{
			return "Stored";
		}

		public override void Store (String cookieNamePath,Time expires, String str)
		{
			//logic
		}
		public override void Clear (String cookieNamePath)
		{
			//logic
		}
	}
}

