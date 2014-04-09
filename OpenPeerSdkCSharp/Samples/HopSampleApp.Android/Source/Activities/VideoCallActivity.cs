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
//using Com.Openpeer.Javaapi;
//using Com.Openpeer.Javaapi.Test;
//using Org.Webrtc.Videoengine;


namespace HopSampleApp
{

	[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op",ScreenOrientation = Android.Content.PM.ScreenOrientation.Portrait)]			
	public class VideoCallActivity : Activity
	{
		//OPTestMediaEngine mediaEngine = null;
		SurfaceView localView = null;
		SurfaceView remoteView = null;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			//Java.Lang.JavaSystem.LoadLibrary("z_shared");
			//Java.Lang.JavaSystem.LoadLibrary("openpeer");
			// Create your application here
			SetContentView (Resource.Layout.VideoCall);

			RelativeLayout localViewLayout = FindViewById<RelativeLayout> (Resource.Id.myLocalViewLinearLayout);//Local cam view
			RelativeLayout remoteViewLayout = FindViewById<RelativeLayout> (Resource.Id.myRemoteViewLinearLayout);//Remote cam view

			ImageButton SwichCam = FindViewById<ImageButton> (Resource.Id.ButtonSwitchCam);//SwichCam button
			ImageButton MuteMic = FindViewById<ImageButton> (Resource.Id.ButtonMuteMic);//Mute mic button
			ImageButton StartChat = FindViewById<ImageButton> (Resource.Id.ButtonStartChat);//StartChat button

			/*
			//Media            
			localView = ViERenderer.CreateLocalRenderer(this);
			localViewLayout.AddView (localView);

			remoteView = ViERenderer.CreateRenderer(this, true);
			remoteViewLayout.AddView (remoteView);

			OPMediaEngine.Init (Android.App.Application.Context);
			mediaEngine = OPTestMediaEngine.TestInstance;

			mediaEngine.CameraType = CameraTypes.CameraTypeBack;
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
			//End media
            */
			//switching from the front to the back camera
			SwichCam.Click += delegate {
				Console.WriteLine("Swich camera");
			};
			//Mute mic
			MuteMic.Click += delegate {

				Console.WriteLine("Mute mic");
			};
			//Start chat with current user
			StartChat.Click += delegate {

				Console.WriteLine("Start Chat with user");
				//This is only for test
				//mediaEngine.StartVideoCapture ();
				//mediaEngine.ReceiverAddress = "127.0.0.1";
				//mediaEngine.StartVoice ();
				//mediaEngine.StartVideoChannel ();
			};

		}
	}
}

