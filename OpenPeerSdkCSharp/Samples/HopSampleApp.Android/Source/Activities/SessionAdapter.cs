using System;
using System.Diagnostics;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using Android.App;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.Views;
using Android.Widget;
using OpenPeerSdk.Helpers;
using HopSampleApp.Services;
using HopSampleApp.Views;
using System.Linq;
using System.Globalization;
using Helpers = OpenPeerSdk.Helpers;
using BitmapType = Android.Graphics.Drawables.BitmapDrawable;

namespace HopSampleApp
{
	namespace Activities
	{
		[LoggerSubsystem("hop_sample_app")]
		class SessionAdapter: BaseAdapter<SessionItem> {
			List<SessionItem> items;
			Activity context;
			public SessionAdapter(Activity context, List<SessionItem> items)
				: base()
			{
				this.context = context;
				this.items = items;
			}
			public override long GetItemId(int position)
			{
				return position;
			}
			public override SessionItem this[int position]
			{
				get { return items[position]; }
			}
			public override int Count
			{
				get { return items.Count; }
			}
			public override View GetView(int position, View convertView, ViewGroup parent)
			{
				var item = items[position];
				SocialMediaFeature sm = new SocialMediaFeature ();
				View view = convertView;
				if (view == null) // no view to re-use, create new
					view = context.LayoutInflater.Inflate(Resource.Layout.ListItemSession, null);
				view.FindViewById<TextView>(Resource.Id.Name).Text = item.SessionMyName;
				view.FindViewById<TextView>(Resource.Id.Username).Text = item.SesisonUserName;
				view.FindViewById<TextView> (Resource.Id.SessionDate).Text =sm.Time_stamp(item.SessionDate);
				view.FindViewById<ImageView>(Resource.Id.Image).SetImageResource(item.UserImg);
				/* if (item.FacebookAccount == true) {
					view.FindViewById<ImageView> (Resource.Id.FacebookIcon).SetImageResource (item.fbImage);
				}
				if (item.LinkedinAccount == true) {
					view.FindViewById<ImageView> (Resource.Id.LinkedinIcon).SetImageResource (item.LinkImage);
				} 
				if (item.TwiterAccount == true) {
					view.FindViewById<ImageView> (Resource.Id.TwiterIcon).SetImageResource (item.twImage);
				} */
				return view;
			}

		}
	}
}

