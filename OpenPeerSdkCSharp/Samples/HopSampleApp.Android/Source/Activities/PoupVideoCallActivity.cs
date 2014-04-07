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
using System.Net;
using Android.Graphics.Drawables;
using Android.Graphics;
using System.Drawing;
using System.IO;
using Java.Net;
using System.Threading.Tasks;
using Android.Net;


namespace HopSampleApp
{
	[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op")]			
	public class PoupVideoCallActivity : Activity
	{
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.PopupVideoCall);

			var imageBitmap = GetImageBitmapFromUrl("http://xamarin.com/resources/design/home/devices.png");//Download avatar image

			ImageView avatar = (ImageView)FindViewById(Resource.Id.ImageVideoCallAvatar);//Avatar image for user
			avatar.SetImageBitmap(imageBitmap);//Set avatar

			//var imageBitmap = GetImageBitmapFromUrl("http://xamarin.com/resources/design/home/devices.png");//Download avatar image

			//ImageView avatar = (ImageView)FindViewById(Resource.Id.ImageVideoCallAvatar);//Avatar image for user
			//avatar.SetImageBitmap(imageBitmap); //Set avatar

			TextView UserIdTop = (TextView)FindViewById (Resource.Id.VideoCallerIDTop);//User name id located at the top
			TextView UserIdCenter = (TextView)FindViewById (Resource.Id.CallerVideoIDCenter);//User name id located at the bottom
			TextView CallInfo = (TextView)FindViewById (Resource.Id.videocallInfo);//Call info for incoming and outgoing calls

			CallInfo.Text = "Incoming video call from";//Set call info for incoming and outgoing calls
			UserIdTop.Text = "Petar Srdanovic";//Set user id top
			UserIdCenter.Text = "Petar Srdanovic";//Set user id bottom

			Button AnswerButton = (Button)FindViewById (Resource.Id.btnAnswerVideoCall);
			AnswerButton.Click += delegate {
				Console.WriteLine("Answer button");
				//Logic for Answer to call
			};

			Button DeclineButton = (Button)FindViewById (Resource.Id.btnDeclineVideoCall);
			DeclineButton.Click += delegate {

				Console.WriteLine("Decline button");
			};
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

