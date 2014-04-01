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

namespace HopSampleApp
{

	[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op",ScreenOrientation = Android.Content.PM.ScreenOrientation.Portrait)]			
	public class VideoCallActivity : Activity
	{

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.VideoCall);

			RelativeLayout localViewLayout = FindViewById<RelativeLayout> (Resource.Id.myLocalViewLinearLayout);//Local cam view
			RelativeLayout remoteViewLayout = FindViewById<RelativeLayout> (Resource.Id.myRemoteViewLinearLayout);//Remote cam view

			ImageButton SwichCam = FindViewById<ImageButton> (Resource.Id.ButtonSwitchCam);//SwichCam button
			ImageButton MuteMic = FindViewById<ImageButton> (Resource.Id.ButtonMuteMic);//Mute mic button
			ImageButton StartChat = FindViewById<ImageButton> (Resource.Id.ButtonStartChat);//StartChat button

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
			};

		}
	}
}

