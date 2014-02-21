
using System;
using System.Collections.Generic;

using Android.App;
using Android.Content;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.OS;
using Android.Views;
using Android.Widget;

using PullToRefresharp.Android.Views;

using OpenPeerSampleAppCSharp.Services;

using BitmapType = Android.Graphics.Drawables.BitmapDrawable;

namespace OpenPeerSampleAppCSharp
{
	namespace Activities
	{
		[Activity (Label = "Open Peer Sample App - Contact List", MainLauncher = true)]			
		public class ContactsActivity : ListActivity
		{
			private ImageCachingServiceDownloader downloader = new ImageCachingServiceDownloader ();
			private IPullToRefresharpView refreshListView;
			private ServiceConnection<ImageCachingService> avatarServiceConnection;

			protected override void OnCreate (Bundle bundle)
			{
				base.OnCreate (bundle);
				SetContentView (Resource.Layout.ContactsWithPull);

				avatarServiceConnection = ServiceConnection<Services.ImageCachingService>.Bind (this, downloader);

				refreshListView = this.ListView as IPullToRefresharpView;

				if (refreshListView != null) {
					refreshListView.RefreshActivated += pullview_RefreshActivated;
				}

				this.ListAdapter = new ContactsAdapter (this, downloader);
			}

			protected override void OnDestroy ()
			{
				if (avatarServiceConnection != null) {
					avatarServiceConnection.Dispose ();
					avatarServiceConnection = null;
				}

				base.OnDestroy ();
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

				this.OverridePendingTransition (Resource.Animation.SlideInRight, Resource.Animation.SlideOutLeft);
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
}

