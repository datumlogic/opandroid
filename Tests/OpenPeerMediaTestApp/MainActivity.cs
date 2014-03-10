using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Util;
using Com.Openpeer.Javaapi;
using Com.Openpeer.Javaapi.Test;
using Org.Webrtc.Videoengine;

namespace OpenPeerMediaTestApp
{
	[Activity (Label = "OpenPeerMediaTestApp", MainLauncher = true)]
	public class MainActivity : Activity
	{
		OPTestMediaEngine mediaEngine = null;
		SurfaceView localView = null;
		int mediaEngineStatus = 0;

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

			OPMediaEngine.Init (Android.App.Application.Context);

			button.Text = string.Format ("Start Video Capture");
			
			button.Click += delegate {
				Log.Debug("MainActivity", "button.Click");
				mediaEngine = (OPTestMediaEngine)OPTestMediaEngine.Instance;
				switch (mediaEngineStatus) {
				case 0:
					Java.Lang.Boolean ecEnabled = new Java.Lang.Boolean (false);
					mediaEngine.SetEcEnabled (ecEnabled);
					mediaEngine.StartVideoCapture ();
					button.Text = string.Format ("Start Audio/Video Channel");
					mediaEngineStatus++;
					break;
				case 1:
					mediaEngine.StartVoice ();
					mediaEngine.StartVideoChannel ();
					button.Text = string.Format ("Stop Audio/Video Channel");
					mediaEngineStatus++;
					break;
				case 2:
					mediaEngine.StopVoice ();
					mediaEngine.StopVideoChannel ();
					button.Text = string.Format ("Stop Video Capture");
					mediaEngineStatus++;
					break;
				case 3:
					mediaEngine.StopVideoCapture ();
					button.Text = string.Format ("Start Video Capture");
					mediaEngineStatus = 0;
					break;
				default:
					break;
				}
			};
		}
	}
}


