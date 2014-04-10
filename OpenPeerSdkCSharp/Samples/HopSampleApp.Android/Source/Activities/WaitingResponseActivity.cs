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
using Android.Graphics;
using System.Drawing;
using Android.Graphics.Drawables;
using System.Net;
namespace HopSampleApp
{
	[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op",ScreenOrientation = Android.Content.PM.ScreenOrientation.Portrait)]			
	public class WaitingResponseActivity : Activity
	{
		AnimationDrawable _LoadingDrawable;//For loading animation
		AnimationDrawable _SkyDrawable;//For sky animation
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.WaitingResponse);
			//Local avatar image
			var LocalAvatarImage = GetImageBitmapFromUrl ("http://www.ultraconsultants.com/wp-content/uploads/2012/12/Gary-McGregor-vThumb-298x300.jpeg");//Download avatar image
			//Remote avatar image
			var RemoteAvatarImage = GetImageBitmapFromUrl ("http://www.thehebrewcafe.com/images/me_thumb.jpg");

			//Loading Animacion
			_SkyDrawable=(Android.Graphics.Drawables.AnimationDrawable)Resources.GetDrawable(Resource.Drawable.animacionskybg);//use animacionskybg.xml
			_LoadingDrawable = (Android.Graphics.Drawables.AnimationDrawable)Resources.GetDrawable(Resource.Drawable.animacionshapes);//use animacionshapes.xml
			ImageView LoadingImage = FindViewById<ImageView>(Resource.Id.LoadingImage);
			LoadingImage.SetImageDrawable((Android.Graphics.Drawables.Drawable) _LoadingDrawable);
			//Start animation
			_LoadingDrawable.Start ();

			//Sky animation
			RelativeLayout LayoutBg = FindViewById<RelativeLayout> (Resource.Id.LayoutBg);
			LayoutBg.SetBackgroundDrawable ((Android.Graphics.Drawables.Drawable) _SkyDrawable);
			//Start animation
			_SkyDrawable.Start ();

			//Set remote user avatar image
			ImageView RemoteAvatar = FindViewById<ImageView> (Resource.Id.RemoteImageCallAvatar);
			RemoteAvatar.SetImageBitmap (RemoteAvatarImage);
			//Set remote user id
			TextView RemoteUserID = FindViewById<TextView> (Resource.Id.RemoteCallerIDCenter);
			RemoteUserID.Text = "Sergej Jovanovic";

			//Set local user avatar image
			ImageView LocalAvatar = FindViewById<ImageView> (Resource.Id.LocalImageCallAvatar);
			LocalAvatar.SetImageBitmap (LocalAvatarImage);
			//Set local user id
			TextView LocalUserID = FindViewById<TextView> (Resource.Id.LocalCallerIDCenter);
			LocalUserID.Text="Petar Srdanovic";

		}
		/* Download image form url */
		private Bitmap GetImageBitmapFromUrl(string url)
		{
			Bitmap imageBitmap = null;

			using (var webClient = new WebClient())
			{
				var imageBytes = webClient.DownloadData(url);
				if (imageBytes != null && imageBytes.Length > 0)
				{
					imageBitmap = BitmapFactory.DecodeByteArray(imageBytes, 0, imageBytes.Length);
				}
			}

			return imageBitmap;
		}
	}
}

