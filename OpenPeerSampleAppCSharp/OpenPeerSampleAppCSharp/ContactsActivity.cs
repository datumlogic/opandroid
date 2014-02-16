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
	[Activity (Label = "Open Peer Sample App - Contact List", MainLauncher = true)]			
	public class ContactsActivity : ListActivity
	{
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			SetContentView (Resource.Layout.Contacts);

			// ListView view = FindViewById<ListView> (Android.Resource.Id.List);

			//			view.ItemClick += OnMyListItemClicked;

			this.ListAdapter = new ContactAdapter (this);
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
		/*
		protected void OnMyListItemClicked( object sender, ListView.ItemClickEventArgs e)
		{
			Android.Widget.Toast.MakeText(this, "item " + e.Position.ToString(), Android.Widget.ToastLength.Short).Show();
		}
		*/

		protected override void OnListItemClick(ListView l, View v, int position, long id)
		{
			base.OnListItemClick (l, v, position, id);
			Android.Widget.Toast.MakeText(this, "hello " + position.ToString(), Android.Widget.ToastLength.Short).Show();
		}
	}
}

