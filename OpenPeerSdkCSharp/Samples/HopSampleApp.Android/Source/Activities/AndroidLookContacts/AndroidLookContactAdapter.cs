using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.Util;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Graphics.Drawables;
using HopSampleApp.Services;
using BitmapType = Android.Graphics.Drawables.BitmapDrawable;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;

namespace HopSampleApp
{
	class AndroidLookContactAdapter:BaseAdapter<AndroidContactItem>
	{
		//IImageCachingDownloader downloader;//Net to swich on it
		List<AndroidContactItem> items;
		Activity context;
		public AndroidLookContactAdapter(Activity context, List<AndroidContactItem> items)
			: base()
		{

			this.context = context;
			this.items = items;
		}

		public override long GetItemId(int position)
		{
			return position;
		}
		public override AndroidContactItem this[int position]
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

			View view = convertView;
			if (view == null) {
				view = context.LayoutInflater.Inflate (Resource.Layout.AndroidLookContctItem, null);
			}
			view.FindViewById<TextView>(Resource.Id.ACUserName).Text =item.UserName;
			view.FindViewById<TextView>(Resource.Id.ACIdentity).Text =item.Identity(item.IdentityUri);

			view.FindViewById<ImageView>(Resource.Id.ACContactAvatarImage).SetImageBitmap(CSUtility.GetImageBitmapFromUrl(item.avatar));

			if (item.FacebookAccount == true)
			{
				view.FindViewById<ImageView> (Resource.Id.ACFacebookIcon).SetImageResource (item.fbImage);
			}

			if (item.LinkedinAccount == true) 
			{
				view.FindViewById<ImageView> (Resource.Id.ACLinkedinIcon).SetImageResource (item.LinkImage);
			} 

			if (item.TwiterAccount == true) 
			{
				view.FindViewById<ImageView> (Resource.Id.ACTwiterIcon).SetImageResource (item.twImage);
			}
			return view;
		}
	}
}

