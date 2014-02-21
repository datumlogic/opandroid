using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Com.Openpeer.Javaapi;
using Org.Webrtc.Videoengine;

namespace OpenPeerMediaTestApp
{
	[Activity (Label = "OpenPeerMediaTestApp", MainLauncher = true)]
	public class MainActivity : Activity
	{
		OPMediaEngine mediaEngine = null;
		SurfaceView localView = null;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			Java.Lang.JavaSystem.LoadLibrary("z_shared");
			Java.Lang.JavaSystem.LoadLibrary("openpeer");

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			// Get our button from the layout resource,
			// and attach an event to it
			LinearLayout layout = FindViewById<LinearLayout> (Resource.Id.myLinearLayout);
			Button button = FindViewById<Button> (Resource.Id.myButton);

			localView = ViERenderer.CreateLocalRenderer(this);
			layout.AddView (localView);
			
			button.Click += delegate {
				Java.Lang.Boolean ecEnabled = new Java.Lang.Boolean (false);
				mediaEngine = OPMediaEngine.Singleton ();
				mediaEngine.SetEcEnabled (ecEnabled);
				mediaEngine.StartVideoCapture ();
				button.Text = string.Format ("Video Capture Started");
			};
		}
	}
}


