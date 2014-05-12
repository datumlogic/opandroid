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
	public class VideoCallActivity : Activity,View.IOnTouchListener
	{
		//OPTestMediaEngine mediaEngine = null;
		//SurfaceView localView = null;
		//SurfaceView remoteView = null;
		int MicrophoneMute=0;
		bool useFrontCamera = true;
		RelativeLayout localViewLayout;
		ImageView imageArrow;
		float x = 0;
		float y = 0;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			//Java.Lang.JavaSystem.LoadLibrary("z_shared");
			//Java.Lang.JavaSystem.LoadLibrary("openpeer");
			// Create your application here
			SetContentView (Resource.Layout.VideoCall);

			#region Layouts and Controls
			localViewLayout = FindViewById<RelativeLayout> (Resource.Id.myLocalViewLinearLayout);//Local cam view
			RelativeLayout remoteViewLayout = FindViewById<RelativeLayout> (Resource.Id.myRemoteViewLinearLayout);//Remote cam view
			imageArrow = FindViewById<ImageView>(Resource.Id.arrowimage);//Arrow image
			ImageButton SwichCam = FindViewById<ImageButton> (Resource.Id.ButtonSwitchCam);//SwichCam button
			ImageButton MuteMic = FindViewById<ImageButton> (Resource.Id.ButtonMuteMic);//Mute mic button
			ImageButton StartChat = FindViewById<ImageButton> (Resource.Id.ButtonStartChat);//StartChat button
			#endregion

			#region Move local view on screen

			localViewLayout.SetOnTouchListener (this);
			imageArrow.SetOnTouchListener(this);
			#endregion
			#region Media engine
			/*
			//Media start           
			localView = ViERenderer.CreateLocalRenderer(this);
			localViewLayout.AddView (localView);

			remoteView = ViERenderer.CreateRenderer(this, true);
			remoteViewLayout.AddView (remoteView);

			OPMediaEngine.Init (Android.App.Application.Context);
			mediaEngine = OPTestMediaEngine.TestInstance;
			if (useFrontCamera)
				mediaEngine.CameraType = CameraTypes.CameraTypeFront;
			else

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
		   #endregion
          
		

		   #region Events and Delegates
			//switching from the front to the back camera
			SwichCam.Click += delegate {

				switch(useFrontCamera)
				{
				case true:
					//mediaEngine.CameraType = CameraTypes.CameraTypeBack;//Back camera.
					useFrontCamera=false;
					Console.WriteLine("Swich to Back camera");
					break;
				case false:
					//mediaEngine.CameraType = CameraTypes.CameraTypeFront;//Front camera.
					useFrontCamera=true;
					Console.WriteLine("Swich to Front camera");
					break;
				default:break;
				}
			};
			//Microphone enable/disable
			MuteMic.Click += delegate {

				switch(MicrophoneMute)
				{
				case 0:
					//mediaEngine.MuteEnabled=Java.Lang.Boolean.True;//Disable microphone
					MicrophoneMute++;
					Console.WriteLine("Microphone are now disabled.");
					break;
				case 1:
					//mediaEngine.MuteEnabled=Java.Lang.Boolean.False;//Enable microphone
					MicrophoneMute=0;
					Console.WriteLine("Microphone are now enabled.");
					break;
				default:break;
				}

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
			#endregion

		}
		#region OnTouch
		public bool OnTouch(View v, MotionEvent e)
		{
			switch (e.Action) {

			case MotionEventActions.Down:
				x = e.GetX ();
				y = e.GetY ();
				//imageArrow.Visibility = ViewStates.Visible;

				break;

			case MotionEventActions.Move:
				var left = (int)(e.RawX - x);
				var right = (left + v.Width);
				//var top = (int)(e.RawY - y);
				//var bottom = (top - v.Height);
				imageArrow.Visibility = ViewStates.Visible;
				v.Layout (left, v.Top, right, v.Bottom);//move left to right and inversely

				break;
			case MotionEventActions.Up:
				imageArrow.Visibility = ViewStates.Invisible;
				break;

			}

			return true;
		}

		#endregion

	}

}

