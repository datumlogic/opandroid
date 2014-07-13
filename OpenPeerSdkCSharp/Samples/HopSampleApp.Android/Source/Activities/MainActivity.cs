using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using System.Net;
using System.IO;
using System.Text;



using OpenPeerSdk.Helpers;

namespace HopSampleApp
{
	namespace Activities
	{
		[LoggerSubsystem("hop_sample_app")]
		[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op")]
		public class MainActivity : ActivityGroup
		{
			protected override void OnCreate (Bundle bundle)
			{
				base.OnCreate (bundle);

				// Set our view from the "main" layout resource
				SetContentView (Resource.Layout.Main);//Main

				TabHost tabHost = FindViewById<TabHost> (Resource.Id.tabHost);
				tabHost.Setup (this.LocalActivityManager);


				TabHost.TabSpec tabSpec1 = tabHost.NewTabSpec ("Contacts");
				TabHost.TabSpec tabSpec2 = tabHost.NewTabSpec ("Settings");

				Intent intent;

				tabSpec1.SetIndicator ("Contacts");
				intent = new Intent (this, typeof(AndroidLookContactActivity));
				intent.AddFlags (ActivityFlags.NewTask);
				tabSpec1.SetContent (intent);

				tabSpec2.SetIndicator ("Settings");
				intent = new Intent (this, typeof(SettingsActivity));
				intent.AddFlags (ActivityFlags.NewTask);
				tabSpec2.SetContent (intent);

				tabHost.AddTab (tabSpec1);
				tabHost.AddTab (tabSpec2);

				tabHost.CurrentTab = 0;

				intent = new Intent (this, typeof(PopupCallActivity));
				StartActivity (intent);

			}
		}
	}
}
