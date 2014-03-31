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
	public class EndCallActivity : Activity
	{
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.EndCall);
			TextView DurationCall = FindViewById<TextView> (Resource.Id.DurationText);
			ImageButton MicButton = FindViewById<ImageButton> (Resource.Id.MicButton);
			ImageButton SpikerButton = FindViewById<ImageButton> (Resource.Id.SpikerButton);
			ImageButton AddButton = FindViewById<ImageButton> (Resource.Id.AddButton);
			ImageButton RecordingButton = FindViewById<ImageButton> (Resource.Id.RecordButton);

			string time="00:00:05";
			DurationCall.Text = String.Format ("Duracion:{0}",time);

			MicButton.Click += delegate {
			
				Console.WriteLine("Mic button");
			};
			SpikerButton.Click += delegate {
			
				Console.WriteLine("Spiker button");
			};
			AddButton.Click += delegate {
			
				Console.WriteLine("Add button");
			};
			RecordingButton.Click += delegate {
			
				Console.WriteLine("Recording button");
			};
		}
	}
}

