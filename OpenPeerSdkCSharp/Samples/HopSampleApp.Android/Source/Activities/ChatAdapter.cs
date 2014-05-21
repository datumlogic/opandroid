using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.Linq;
using System.Text;
using Android.Util;
using Android.App;
using Android.Content;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.OS;
using Android.Runtime;
using Android.Text;
using Android.Views;
using Android.Widget;
using Android.Webkit;
using OpenPeerSdk.Helpers;
using Android.Net;
using HopSampleApp.Activities;
using HopSampleApp.Services;
using BitmapType = Android.Graphics.Drawables.BitmapDrawable;
using Helpers = OpenPeerSdk.Helpers;

namespace HopSampleApp
{
	namespace Activities
	{
		[LoggerSubsystem("hop_sample_app")]
		public class ChatAdapter : BaseAdapter<object>
		{
			Activity context;
			IImageCachingDownloader downloader;
			SocialMediaFeature sm=new SocialMediaFeature();

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
						Logger.Trace ("ChatAdapter original view is no longer bound");
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
				public VideoView video{ get; set;}
				public WebView WEB{ get; set;}
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
				    ConnectivityManager connManager = (ConnectivityManager)context.GetSystemService (Context.ConnectivityService);
				if (connManager.GetNetworkInfo (ConnectivityType.Mobile).IsConnected || connManager.GetNetworkInfo (ConnectivityType.Wifi).IsConnected) 
				{
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
								dataHolder.WEB = (WebView)view.FindViewById (Resource.Id.web);
								break;
							case ListItemType.Right:
								view = context.LayoutInflater.Inflate (Resource.Layout.ChatRightSideListItem, null);
								holder = dataHolder = new DataViewHolder ();
								dataHolder.AvatarImageView = view.FindViewById<ImageView> (Resource.Id.avatarImageView);
								dataHolder.Message = view.FindViewById<TextView> (Resource.Id.bubbleTextView);
								dataHolder.WEB = (WebView)view.FindViewById (Resource.Id.web);
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
							Contract.Assume (((dataHolder.AvatarWidth != ViewGroup.LayoutParams.MatchParent) && (dataHolder.AvatarWidth != ViewGroup.LayoutParams.WrapContent)));
							Contract.Assume (((dataHolder.AvatarHeight != ViewGroup.LayoutParams.MatchParent) && (dataHolder.AvatarHeight != ViewGroup.LayoutParams.WrapContent)));
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
							throw new NotImplementedException ();
						}
					}

					int person = 0;

					{

						switch (position % (7 * 2)) {
						case 0:
							headerHolder.Name.Text = "Alice Apples";
							headerHolder.Time.Text = sm.Time_stamp(new DateTime(2008,1,8));
							break;
						case 1:
							dataHolder.Message.Text = "Hello?";
							break;

						case 2:
							headerHolder.Name.Text = "Bob Baker";
							headerHolder.Time.Text = sm.Time_stamp (new DateTime (2008, 1, 9));//"2014-02-08 11:12 am";
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
							headerHolder.Time.Text = sm.Time_stamp (new DateTime(2014,3,2));//"2014-02-08 11:12 am";
							break;
						case 7:
							dataHolder.Message.Text = "Can you come get me at the steak house? My date turned out to be a real jerk.";
							break;

						case 8:
							headerHolder.Name.Text = "Bob Baker";
							headerHolder.Time.Text = sm.Time_stamp (new DateTime(2014,3,2));//"2014-02-08 11:13 am";
							break;
						case 9:
							person = 1;
							string data_message = dataHolder.Message.Text;
							/*data_message = "https://www.youtube.com/watch?v=7II2wAb9CeQ";
							if (data_message != null) {
								if (data_message.IndexOf ("youtube", 0) > 0 || data_message.IndexOf ("youtu", 0) > 0 | data_message.IndexOf ("vimeo", 0) > 0) {
									dataHolder.WEB.Visibility = ViewStates.Visible;
									dataHolder.WEB.Settings.JavaScriptEnabled = true;
									dataHolder.WEB.Settings.AllowContentAccess = true;
									dataHolder.Message.Text = data_message;
									dataHolder.WEB.SetWebChromeClient (new WebChromeClient ());
									string data = sm.Expands_URL (data_message);
									dataHolder.WEB.LoadData (data, "text/html", "utf-8");

								} else {
									dataHolder.Message.Text = data_message;
									dataHolder.WEB.Visibility = ViewStates.Invisible;
								}
							}*/
							dataHolder.Message.Text = data_message;
							dataHolder.WEB.Visibility = ViewStates.Invisible;

							break;
						case 10:
							break;
						case 11:
							person = 1;
							dataHolder.Message.Text = "Are you somewhere warm? It's sure cold out this time of the year.";
							break;

						case 12:
							headerHolder.Name.Text = "Alice Apples";
							headerHolder.Time.Text = sm.Time_stamp (new DateTime (2014, 4, 20));//"2014-02-08 11:14 am";
							break;
						case 13:

							string data_message1 = dataHolder.Message.Text;
							data_message1 = "https://www.youtube.com/watch?v=7II2wAb9CeQ";
							if (data_message1 != null) {
								if (data_message1.IndexOf ("youtube", 0) > 0 || data_message1.IndexOf ("youtu", 0) > 0 | data_message1.IndexOf ("vimeo", 0) > 0) {
									dataHolder.WEB.Visibility = ViewStates.Visible;
									dataHolder.WEB.Settings.JavaScriptEnabled = true;
									dataHolder.WEB.Settings.AllowContentAccess = true;
									dataHolder.Message.Text = data_message1;
									dataHolder.WEB.SetWebChromeClient (new WebChromeClient ());
									string data = sm.Expands_URL (data_message1);
									dataHolder.WEB.LoadData (data, "text/html", "utf-8");

								} else {
									dataHolder.Message.Text = data_message1;
									//dataHolder.WEB.Visibility = ViewStates.Invisible;
								}
							}
							//dataHolder.Message.Text = data_message1;
							//dataHolder.WEB.Visibility = ViewStates.Invisible;
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
				} else {

					return null;
				}//End
				

			}
		}
	}
}

