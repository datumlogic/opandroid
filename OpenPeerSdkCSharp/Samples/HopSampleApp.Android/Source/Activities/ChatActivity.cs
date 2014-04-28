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
using Android.Views.InputMethods;
using Android.Text.Method;
using HopSampleApp.Activities;
using HopSampleApp.Services;
using OpenPeerSdk.Helpers;

namespace HopSampleApp
{
	namespace Activities
	{
		[LoggerSubsystem("hop_sample_app")]
		[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op",WindowSoftInputMode = SoftInput.AdjustPan)]	
		//
		public class ChatActivity : ListActivity,
									View.IOnKeyListener
		{
			private ImageCachingServiceDownloader downloader = new ImageCachingServiceDownloader ();
			private EditText editText;
			private ServiceConnection<Services.ImageCachingService> imageCachingServiceConnection;

			protected override void OnCreate (Bundle bundle)
			{
				base.OnCreate (bundle);

				// Create your application here
				SetContentView (Resource.Layout.Chat);

				imageCachingServiceConnection = ServiceConnection<Services.ImageCachingService>.Bind (this, downloader);

				this.ListAdapter = new ChatAdapter (this, downloader);

				ListView listView = FindViewById<ListView> (Android.Resource.Id.List);
				listView.ItemsCanFocus = true;

				editText = FindViewById<EditText> (Resource.Id.editText);
				ImageButton StartVideoCall=FindViewById<ImageButton>(Resource.Id.ButtonCallVideo);
				ImageButton StartCallOnly = FindViewById<ImageButton> (Resource.Id.ButtonCallOnly);

				ImageButton sendButton = FindViewById<ImageButton> (Resource.Id.sendButton);

				listView.Touch += (object sender, View.TouchEventArgs e) => {
					Logger.Trace ("touch event");
					ClearEditFocus ();
				};

				sendButton.Click += (object sender, EventArgs e) => {
					OnSend ();
				};

				//Event for start video call
				StartVideoCall.Click += delegate {
					Console.WriteLine("Start Video Call");
					Intent intent = new Intent (this, typeof(HopSampleApp.PoupVideoCallActivity));
					StartActivity (intent);


				
				};

				//Event for start call only
				StartCallOnly.Click += delegate {
				
					Console.WriteLine("Start Call only");
					Intent intent = new Intent (this, typeof(HopSampleApp.PopupCallActivity));
					StartActivity (intent);


				};


				//

				editText.SetOnKeyListener (this);
			}

			protected override void OnDestroy ()
			{
				if (imageCachingServiceConnection != null) {
					imageCachingServiceConnection.Dispose ();
					imageCachingServiceConnection = null;
				}

				base.OnDestroy ();
			}

			public override void OnBackPressed ()
			{
				// prevent back button during login process
				Finish ();
				this.OverridePendingTransition (Resource.Animation.SlideInLeft, Resource.Animation.SlideOutRight);
			}

			bool View.IOnKeyListener.OnKey (View v, Keycode keyCode, KeyEvent e)
			{
				if (e.Action == KeyEventActions.Down && keyCode == Keycode.Enter) {
					Logger.Debug ("entered pressed");
					OnSend ();
					return true;
				}
				return false;
			}

			protected void ClearEditFocus()
			{
				if (editText.HasFocus) {
					CloseSoftInput ();
					editText.ClearFocus ();
					Logger.Debug("focus cleared");
				}
			}

			protected void CloseSoftInput()
			{
				View currentView = this.CurrentFocus;
				if (currentView == null)
					return;

				InputMethodManager inputManager = 
					(InputMethodManager)GetSystemService (InputMethodService);

				inputManager.HideSoftInputFromWindow(
					currentView.WindowToken,
					HideSoftInputFlags.NotAlways); 
			}

			protected void OnSend()
			{
				Logger.Debug("send clicked");

				editText.Text = "";
				editText.RequestFocus ();
			}
		}
	}
}

