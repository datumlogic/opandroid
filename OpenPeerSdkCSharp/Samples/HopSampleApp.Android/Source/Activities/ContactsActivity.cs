
using System;
using System.Collections.Generic;

using Android.App;
using Android.Content;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.OS;
using Android.Views;
using Android.Widget;

using OpenPeerSdk.Helpers;

using PullToRefresharp.Android.Views;

using HopSampleApp.Services;

using BitmapType = Android.Graphics.Drawables.BitmapDrawable;

namespace HopSampleApp
{
	namespace Activities
	{
		[LoggerSubsystem("hop_sample_app")]
		[Activity (Label = "Open Peer Sample App - Contact List",Icon="@drawable/op" /*,MainLauncher = true*/)]			
		public class ContactsActivity : ListActivity
		{
			private ImageCachingServiceDownloader downloader = new ImageCachingServiceDownloader ();
			private IPullToRefresharpView refreshListView;
			private ServiceConnection<ImageCachingService> imageCachingServiceConnection;

			private DateTime lastRefresh;
			private static TimeSpan redownloadIfRefreshWithin = TimeSpan.FromSeconds (15);
			private static TimeSpan redownloadOlderThan = TimeSpan.FromMinutes (5);

			protected override void OnCreate (Bundle bundle)
			{
				base.OnCreate (bundle);
				SetContentView (Resource.Layout.ContactsWithPull);

				imageCachingServiceConnection = ServiceConnection<Services.ImageCachingService>.Bind (this, downloader);

				refreshListView = this.ListView as IPullToRefresharpView;

				if (refreshListView != null) {
					refreshListView.RefreshActivated += pullview_RefreshActivated;
				}

				this.ListAdapter = new ContactsAdapter (this, downloader);
			}

			protected override void OnDestroy ()
			{
				if (imageCachingServiceConnection != null) {
					imageCachingServiceConnection.Dispose ();
					imageCachingServiceConnection = null;
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
				this.downloader.RedownloadMissingUponNextFetch ();

				if (lastRefresh != default(DateTime)) {
					if (lastRefresh + redownloadIfRefreshWithin < DateTime.UtcNow) {
						this.downloader.RedownloadOlderThan (DateTime.UtcNow - redownloadOlderThan);
					}
				}

				lastRefresh = DateTime.UtcNow;

				ListView.InvalidateViews ();

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

