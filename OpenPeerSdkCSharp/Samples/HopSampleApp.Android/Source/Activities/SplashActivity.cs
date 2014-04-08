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
using System.Threading;
using Android.Util;
/* QRCode Scanner namespaces  */
using ZXing;
using ZXing.QrCode;
using ZXing.Mobile;
using System.Net;
using Newtonsoft.Json.Linq;//Need to add this source in project
using HopSampleApp.Services;
using HopSampleApp.Activities;
using Android.Support.V4.View;
using Android.Support.V4;

namespace HopSampleApp
{
	/// <summary>
	/// Splash activity which has a built-in ZXing QRScanner who runs or skips with the click of a button.
	/// </summary>

	[Activity (Theme = "@style/Theme.Splash",MainLauncher = true,NoHistory = true,Icon="@drawable/op")]			
	public class SplashActivity : Activity
	{
		private GestureDetector _gestureDetector;

		private ProgressBar mProgress;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Run Splash View on start
			SetContentView (Resource.Layout.Splash);
			//ToggleButton QRCodeSettingOption=(ToggleButton)

			JSONParserProperty.GestureOption = true;

			_gestureDetector = new GestureDetector (this,new GestureListener());

		
			mProgress = (ProgressBar)FindViewById(Resource.Id.progress_bar);


			new Thread(new ThreadStart(() => {
				for (int i = 0; i <= 100; i++) {
					this.RunOnUiThread ( () => {
						mProgress.Progress = i;
					});
					Thread.Sleep(30);
				}
				this.RunOnUiThread(() => {
					//Need to implement logic to load 

					if(JSONParserProperty.GestureOption !=false)
					{
						StartActivity(typeof(HopSampleApp.GestureActivity));//Skip Splash View and go to ContactsActivity

					}else
					{

						//StartActivity(typeof(HopSampleApp.Activities.ChatActivity));
						StartActivity(typeof(HopSampleApp.VideoCallActivity));

					}
				});
			})).Start();
		}
			
		public override bool OnTouchEvent(MotionEvent e) 
			{ _gestureDetector.OnTouchEvent(e); 
				return false;
			}




	


	}




}

