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
		public class MainActivity :TabActivity
		{
			private void CreateTab(Type activityType, string tag, string label, int drawableId )
			{
				var intent = new Intent(this, activityType);
				intent.AddFlags(ActivityFlags.NewTask);

				var spec = TabHost.NewTabSpec(tag);
				var drawableIcon = Resources.GetDrawable(drawableId);
				spec.SetIndicator(label, drawableIcon);
				spec.SetContent(intent);

				TabHost.AddTab(spec);
			}

			protected override void OnCreate (Bundle bundle)
			{
				base.OnCreate (bundle);

				// Set our view from the "main" layout resource
				SetContentView (Resource.Layout.Main);//Main
				CreateTab(typeof(AndroidLookContactActivity), "contacts", "Contacts", Resource.Drawable.hookflash);
				CreateTab(typeof(SettingsActivity), "settings", "Settings", Resource.Drawable.hookflashsettings);



			}
		}
	}
}
