using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;

namespace OpenPeerSampleAppCSharp
{
	[Activity (Label = "Open Peer Sample App")]
	public class MainActivity : ActivityGroup
	{
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			TabHost tabHost = FindViewById<TabHost> (Resource.Id.tabHost);
			tabHost.Setup (this.LocalActivityManager);

			TabHost.TabSpec tabSpec1 = tabHost.NewTabSpec ("Contacts");
			TabHost.TabSpec tabSpec2 = tabHost.NewTabSpec ("Settings");
			TabHost.TabSpec tabSpec3 = tabHost.NewTabSpec ("MediaTest");

			Intent intent;

			tabSpec1.SetIndicator ("Contacts");
			intent = new Intent (this, typeof(ContactsActivity));
			intent.AddFlags (ActivityFlags.NewTask);
			tabSpec1.SetContent (intent);

			tabSpec2.SetIndicator ("Settings");
			intent = new Intent (this, typeof(SettingsActivity));
			intent.AddFlags (ActivityFlags.NewTask);
			tabSpec2.SetContent (intent);

			tabSpec3.SetIndicator ("MediaTest");
			intent = new Intent (this, typeof(MediaTestActivity));
			intent.AddFlags (ActivityFlags.NewTask);
			tabSpec3.SetContent (intent);

			tabHost.AddTab (tabSpec1);
			tabHost.AddTab (tabSpec2);
			tabHost.AddTab (tabSpec3);

			tabHost.CurrentTab = 0;

			intent = new Intent (this, typeof(LoginActivity));
			StartActivity (intent);
		}
	}
}
