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

using PullToRefresharp.Android.Views;

namespace OpenPeerSampleAppCSharp
{
	[Activity (Label = "Open Peer Sample App - Contact List", MainLauncher = true)]			
	public class ContactsActivity : ListActivity
	{
		private IPullToRefresharpView refreshListView;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			SetContentView (Resource.Layout.ContactsWithPull);

			refreshListView = this.ListView as IPullToRefresharpView;

			if (refreshListView != null) {
				refreshListView.RefreshActivated += pullview_RefreshActivated;
			}

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

		protected override void OnListItemClick(ListView l, View v, int position, long id)
		{
			base.OnListItemClick (l, v, position, id);
			Intent intent = new Intent (this, typeof(ChatActivity));
			StartActivity (intent);
			//			this.OverridePendingTransition (Android.Resource.Animation.SlideInLeft, Android.Resource.Animation.SlideOutRight);
			this.OverridePendingTransition (Resource.Animation.SlideInRight, Resource.Animation.SlideOutLeft);
			//			Android.Widget.Toast.MakeText(this, "hello " + position.ToString(), Android.Widget.ToastLength.Short).Show();
		}

		private void pullview_RefreshActivated(object sender, EventArgs args)
		{
			ListView.PostDelayed(() => {
				if (refreshListView != null) {
					// When you are done refreshing your content, let PullToRefresharp know you're done.
					refreshListView.OnRefreshCompleted();
				}
			}, 2000);
		}

	}
}

