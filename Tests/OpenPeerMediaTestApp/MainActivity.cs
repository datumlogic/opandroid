using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Com.Openpeer.Javaapi;

namespace OpenPeerMediaTestApp
{
	[Activity (Label = "OpenPeerMediaTestApp", MainLauncher = true)]
	public class MainActivity : Activity
	{
		int count = 1;
		OPMediaEngine mediaEngine = null;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			Java.Lang.JavaSystem.LoadLibrary("z_shared");
			Java.Lang.JavaSystem.LoadLibrary("openpeer");

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			// Get our button from the layout resource,
			// and attach an event to it
			Button button = FindViewById<Button> (Resource.Id.myButton);
			
			button.Click += delegate {
				Java.Lang.Boolean ecEnabled = new Java.Lang.Boolean (false);
				mediaEngine = OPMediaEngine.Singleton ();
				mediaEngine.SetEcEnabled (ecEnabled);
				//Com.Example.Hellojni.HelloJni hello = new Com.Example.Hellojni.HelloJni();
				//string msg = hello.StringFromJNI();
				string msg = "";
				button.Text = string.Format ("{0} {1}", count++, msg);
			};
		}
	}
}


