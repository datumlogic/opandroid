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
using System.IO;
using Java.Net;
using System.Threading.Tasks;
using Android.Net;
using Android.Graphics.Drawables;
using Android.Graphics;
using System.Net;

namespace HopSampleApp
{
	[Activity (Label = "ProfileActivity")]			
	public class ProfileActivity : Activity
	{
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.Profile);//Main

			TextView ProfileUserID = FindViewById<TextView> (Resource.Id.ProfileID);
			ProfileUserID.Text = "My Profile Info";
			TextView ProfileUserName = FindViewById<TextView> (Resource.Id.ProfileUserName);
			ProfileUserName.Text="Petar Srdanovic";
			TextView ProfileUserEmail = FindViewById<TextView> (Resource.Id.ProfileUserEmail);
			ProfileUserEmail.Text = "petar.srdanovic88@gmail.com";
			var imageBitmap = GetImageBitmapFromUrl("http://s3-us-west-2.amazonaws.com/s.cdpn.io/4971/profile/profile-512_2.jpg");//Download avatar image
			ImageView ProfileAvatarImage = FindViewById<ImageView> (Resource.Id.ProfileAvatarImage);
			ProfileAvatarImage.SetImageBitmap (imageBitmap);
		}
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

