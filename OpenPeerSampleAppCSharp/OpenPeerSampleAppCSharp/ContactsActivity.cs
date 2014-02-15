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

namespace OpenPeerSampleAppCSharp
{
	[Activity (Label = "ContactsActivity", MainLauncher = true)]			
	public class ContactsActivity : Activity
	{
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.Contacts);
		}

		public override bool OnCreateOptionsMenu(IMenu menu)
		{
			MenuInflater.Inflate (Resource.Menu.main_menu, menu);
			return true;
		}

		public override bool OnOptionsItemSelected(IMenuItem item)
		{
			switch (item.ItemId) {
			case Resource.Id.settingsMenuItem:
				{
					Intent intent = new Intent (this, typeof(SettingsActivity));
					StartActivity (intent);
					return true;
				}
			case Resource.Id.logoutMenuItem:
				{
					// this is a temporary hack to get to the login page, it will spawn automatically based on need in the future
					Intent intent = new Intent (this, typeof(LoginActivity));
					StartActivity (intent);
					return true;
				}
			}
			return base.OnOptionsItemSelected (item);
		}
	}
}

