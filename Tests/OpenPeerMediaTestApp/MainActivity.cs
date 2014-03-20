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
	[Activity (Label = "OpenPeerMediaTestApp", MainLauncher = true, ScreenOrientation = Android.Content.PM.ScreenOrientation.Portrait)]
	public class MainActivity : Activity
	{
		OPTestMediaEngine mediaEngine = null;
		SurfaceView localView = null;
		SurfaceView remoteView = null;
		int mediaEngineStatus = 0;
		bool speakerphoneEnabled = false;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			Java.Lang.JavaSystem.LoadLibrary("z_shared");
			Java.Lang.JavaSystem.LoadLibrary("openpeer");

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			// Get our button from the layout resource,
			// and attach an event to it
			LinearLayout localViewLayout = FindViewById<LinearLayout> (Resource.Id.myLocalViewLinearLayout);
			LinearLayout remoteViewLayout = FindViewById<LinearLayout> (Resource.Id.myRemoteViewLinearLayout);
			Button mediaControlButton = FindViewById<Button> (Resource.Id.myMediaControlButton);
			Button audioOutputButton = FindViewById<Button> (Resource.Id.myAudioOutputButton);

			localView = ViERenderer.CreateLocalRenderer(this);
			localViewLayout.AddView (localView);

			remoteView = ViERenderer.CreateRenderer(this, true);
			remoteViewLayout.AddView (remoteView);

			OPMediaEngine.Init (Android.App.Application.Context);
			mediaEngine = OPTestMediaEngine.TestInstance;
			mediaEngine.CameraType = CameraTypes.CameraTypeFront;
			mediaEngine.SetEcEnabled (Java.Lang.Boolean.True);
			mediaEngine.SetAgcEnabled (Java.Lang.Boolean.True);
			mediaEngine.SetNsEnabled (Java.Lang.Boolean.False);
			mediaEngine.SetNsEnabled (Java.Lang.Boolean.False);
			mediaEngine.MuteEnabled = Java.Lang.Boolean.False;
			mediaEngine.LoudspeakerEnabled = Java.Lang.Boolean.False;
			mediaEngine.ContinuousVideoCapture = Java.Lang.Boolean.True;
			mediaEngine.DefaultVideoOrientation = VideoOrientations.VideoOrientationPortrait;
			mediaEngine.RecordVideoOrientation = VideoOrientations.VideoOrientationLandscapeRight;
			mediaEngine.FaceDetection = Java.Lang.Boolean.False;
			mediaEngine.SetChannelRenderView (remoteView);
			mediaEngine.ReceiverAddress = "127.0.0.1";

			mediaControlButton.Text = string.Format ("Start Video Capture");
			audioOutputButton.Text = string.Format ("Speakerphone");

			mediaControlButton.Click += delegate {
				switch (mediaEngineStatus) {
				case 0:
					mediaEngine.StartVideoCapture ();
					mediaControlButton.Text = string.Format ("Start Media Channel");
					mediaEngineStatus++;
					break;
				case 1:
					mediaEngine.StartVoice ();
					mediaEngine.StartVideoChannel ();
					mediaControlButton.Text = string.Format ("Stop Media Channel");
					mediaEngineStatus++;
					break;
				case 2:
					mediaEngine.StopVoice ();
					mediaEngine.StopVideoChannel ();
					mediaControlButton.Text = string.Format ("Stop Video Capture");
					mediaEngineStatus++;
					break;
				case 3:
					mediaEngine.StopVideoCapture ();
					mediaControlButton.Text = string.Format ("Start Video Capture");
					mediaEngineStatus = 0;
					break;
				default:
					break;
				}
			};

			audioOutputButton.Click += delegate {
				if (speakerphoneEnabled) {
					mediaEngine.LoudspeakerEnabled = Java.Lang.Boolean.False;
					audioOutputButton.Text = string.Format ("Speakerphone");
				} else {
					mediaEngine.LoudspeakerEnabled = Java.Lang.Boolean.True;
					audioOutputButton.Text = string.Format ("Ear Speaker");
				}
				speakerphoneEnabled = !speakerphoneEnabled;
			};
		}
	}
}


