using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.OS;
using Android.Runtime;
using Android.Text;
using Android.Views;

using Android.Widget;
using OpenPeerSampleAppCSharp.Services;

using BitmapType = Android.Graphics.Drawables.BitmapDrawable;
using Debug = System.Diagnostics.Debug;

namespace OpenPeerSampleAppCSharp
{
	public class ChatAdapter : BaseAdapter<object>
	{
		Activity context;
		IImageCachingDownloader downloader;

		enum ListItemType
		{
			LeftHeader,
			Left,
			RightHeader,
			Right,
			Margin,

			Total
		}

		class AvatarDownloader
		{
			public Helpers.WeakReference<DataViewHolder> binding;

			public AvatarDownloader (DataViewHolder binding)
			{
				this.binding = binding;
			}

			public void HandleDownloaded (BitmapType bitmap)
			{
				DataViewHolder holder = (DataViewHolder)binding;
				if (this != holder.CurrentDownloader) {
					Debug.WriteLine ("original view is no longer bound");
					return;
				}
				object temp = bitmap;
				if (bitmap is Drawable) {
					holder.AvatarImageView.SetImageDrawable ((Drawable)temp);
				} else {
					holder.AvatarImageView.SetImageBitmap ((Bitmap)temp);
				}
			}
		}

		class ViewHolder : Java.Lang.Object
		{
		}

		class DataViewHolder : ViewHolder
		{
			public int AvatarWidth { get; set; }
			public int AvatarHeight { get; set; }

			public TextView Message { get; set; }
			public ImageView AvatarImageView { get; set; }
			public Drawable OriginalEmptyAvatarDrawable { get; set; }

			public AvatarDownloader CurrentDownloader { get; set; }
		}

		class HeaderViewHolder : ViewHolder
		{
			public TextView Name { get; set; }
			public TextView Time { get; set; }
		}

		public ChatAdapter(Activity context, IImageCachingDownloader downloader) : base()
		{
			this.context = context;
			this.downloader = downloader;
		}

		public override long GetItemId(int position)
		{
			return position;
		}

		public override bool AreAllItemsEnabled ()
		{
			return false;
		}

		public override bool IsEnabled(int position)
		{
			return false;
		}

		public override int GetItemViewType (int position)
		{
			switch (position % 6) {
			case 0:	return (int)ListItemType.LeftHeader;
			case 1:	return (int)ListItemType.Left;
			case 2:	return (int)ListItemType.RightHeader;
			case 3:	return (int)ListItemType.Right;
			case 4:	return (int)ListItemType.Margin;
			case 5:	return (int)ListItemType.Right;
			default:
				break;					
			}
			return (int)ListItemType.Margin;
		}

		public override object this[int position] {  
			get {
				return null;
			}
		}

		public override int Count {
			get { return 7 * 2; }
		}

		public override int ViewTypeCount{
			get { return (int)ListItemType.Total; }
		}

		static string[] bogusUrls = {
			"http://biodegradablegeek.com/wp-content/uploads/2008/06/26.png",
			"http://vz.iminent.com/vz/9357179a-e957-4ab3-b043-d60448ed16fd/2/devil-smiley-avatars.gif"
		};

		public override View GetView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView; // re-use an existing view, if one is available

			ViewHolder holder = null;
			HeaderViewHolder headerHolder = null;
			DataViewHolder dataHolder = null;

			bool firstTimeResourceLoaded = false;

			ListItemType @type = (ListItemType)GetItemViewType (position);

			if (view == null) { // otherwise create a new one
				firstTimeResourceLoaded = true;

				{
					switch (@type) {
					case ListItemType.LeftHeader:
						view = context.LayoutInflater.Inflate (Resource.Layout.ChatLeftSideHeaderListItem, null);
						holder = headerHolder = new HeaderViewHolder ();
						headerHolder.Name = view.FindViewById<TextView> (Resource.Id.nameTextView);
						headerHolder.Time = view.FindViewById<TextView> (Resource.Id.timeTextView);
						break;
					case ListItemType.RightHeader:
						view = context.LayoutInflater.Inflate (Resource.Layout.ChatRightSideHeaderListItem, null);
						holder = headerHolder = new HeaderViewHolder ();
						headerHolder.Name = view.FindViewById<TextView> (Resource.Id.nameTextView);
						headerHolder.Time = view.FindViewById<TextView> (Resource.Id.timeTextView);
						break;
					case ListItemType.Left:
						view = context.LayoutInflater.Inflate (Resource.Layout.ChatLeftSideListItem, null);
						holder = dataHolder = new DataViewHolder ();
						dataHolder.AvatarImageView = view.FindViewById<ImageView> (Resource.Id.avatarImageView);
						dataHolder.Message = view.FindViewById<TextView> (Resource.Id.bubbleTextView);
						break;
					case ListItemType.Right:
						view = context.LayoutInflater.Inflate (Resource.Layout.ChatRightSideListItem, null);
						holder = dataHolder = new DataViewHolder ();
						dataHolder.AvatarImageView = view.FindViewById<ImageView> (Resource.Id.avatarImageView);
						dataHolder.Message = view.FindViewById<TextView> (Resource.Id.bubbleTextView);
						break;
					case ListItemType.Margin:
						view = context.LayoutInflater.Inflate (Resource.Layout.ChatMarginHeaderListItem, null);
						break;
					default:
						throw new NotImplementedException ();
					}
				}

				if (null != dataHolder) {
					dataHolder.OriginalEmptyAvatarDrawable = dataHolder.AvatarImageView.Drawable;
					dataHolder.AvatarWidth = dataHolder.AvatarImageView.LayoutParameters.Width;
					dataHolder.AvatarHeight = dataHolder.AvatarImageView.LayoutParameters.Height;

					// if these fail, you'll need to recode the source to delay the fetching of the avatar until after the render figures out the exact dimensions of the image
					Contract.Assume ( ((dataHolder.AvatarWidth != ViewGroup.LayoutParams.MatchParent) && (dataHolder.AvatarWidth != ViewGroup.LayoutParams.WrapContent)) );
					Contract.Assume ( ((dataHolder.AvatarHeight != ViewGroup.LayoutParams.MatchParent) && (dataHolder.AvatarHeight != ViewGroup.LayoutParams.WrapContent)) );
				}

				//				holder.LabelTextView = view.FindViewById<TextView> (Resource.Id.labelTextView);

				view.Tag = holder;
			} else {
				holder = view.Tag as ViewHolder;
				switch (@type) {
				case ListItemType.LeftHeader:
				case ListItemType.RightHeader:
					holder = headerHolder = (HeaderViewHolder)view.Tag;
					break;
				case ListItemType.Left:
				case ListItemType.Right:
					holder = dataHolder = (DataViewHolder)view.Tag;
					break;
				case ListItemType.Margin:
					break;
				default:
					throw new NotImplementedException();
				}
			}

			int person = 0;

			{
				switch (position % (7 * 2)) {
				case 0:
					headerHolder.Name.Text = "Alice Apples";
					headerHolder.Time.Text = "2014-02-08 11:11 am";
					break;
				case 1:
					dataHolder.Message.Text = "Hello?";
					break;

				case 2:
					headerHolder.Name.Text = "Bob Baker";
					headerHolder.Time.Text = "2014-02-08 11:12 am";
					break;
				case 3:
					person = 1;
					dataHolder.Message.Text = "Hello!";
					break;
				case 4:
					break;
				case 5:
					person = 1;
					dataHolder.Message.Text = "Wassup?";
					break;

				case 6:
					headerHolder.Name.Text = "Alice Apples";
					headerHolder.Time.Text = "2014-02-08 11:12 am";
					break;
				case 7:
					dataHolder.Message.Text = "Can you come get me at the steak house? My date turned out to be a real jerk.";
					break;

				case 8:
					headerHolder.Name.Text = "Bob Baker";
					headerHolder.Time.Text = "2014-02-08 11:13 am";
					break;
				case 9:
					person = 1;
					dataHolder.Message.Text = "Of course";
					break;
				case 10:
					break;
				case 11:
					person = 1;
					dataHolder.Message.Text = "Are you somewhere warm? It's sure cold out this time of the year.";
					break;

				case 12:
					headerHolder.Name.Text = "Alice Apples";
					headerHolder.Time.Text = "2014-02-08 11:14 am";
					break;
				case 13:
					dataHolder.Message.Text = "Yes, I am warm and safe. Thanks for asking.";
					break;

				default:
					break;
				}
			}

			if (null != dataHolder) {
				dataHolder.CurrentDownloader = new AvatarDownloader (dataHolder);

				BitmapType bitmap = downloader.FetchNowOrAsyncDownload (
					bogusUrls [person],
					dataHolder.AvatarWidth,
					dataHolder.AvatarHeight,
					dataHolder.CurrentDownloader.HandleDownloaded
				);

				if (null != bitmap) {
					dataHolder.AvatarImageView.SetImageDrawable (bitmap);
				} else {
					if (!firstTimeResourceLoaded) {
						dataHolder.AvatarImageView.SetImageDrawable (dataHolder.OriginalEmptyAvatarDrawable);	// reset back to original drawable
					}
				}
			}

			return view;
		}
	}
}

