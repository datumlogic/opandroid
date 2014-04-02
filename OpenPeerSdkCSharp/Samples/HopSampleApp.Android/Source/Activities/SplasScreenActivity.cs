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
using System.Threading;



namespace HopSampleApp
{
	[Activity (Theme = "@style/Theme.Splash",MainLauncher = true,NoHistory = true,Icon="@drawable/op")]			
	public class SplasScreenActivity : Activity 

	{
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			SetContentView (Resource.Layout.Splash);
			// Create your application here
			Thread.Sleep(10000); // Simulate a long loading process on app startup.
			StartActivity(typeof(HopSampleApp.Activities.ContactsActivity));

		}
	}
}

