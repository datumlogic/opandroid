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

//using Newtonsoft.Json.Linq;//Need to add this source in project
using HopSampleApp.Services;

namespace HopSampleApp
{
	[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op")]			
	public class GestureActivity : Activity
	{
		MobileBarcodeScanner scanner;//qr code scanner
		View zxingOverlay;//View of qr code scanner
		//View main;
		Button flashButton;//Button for turn flash ON/OFF
		string Opapp="OP QR Scanner";//Title for error-s
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.Gesture);
			scanner = new MobileBarcodeScanner (this);

			//QRScanner Button listener
			Button QRScannerButton = (Button)FindViewById (Resource.Id.QRButton2);
			QRScannerButton.Click += async delegate {
				try
				{

					scanner.UseCustomOverlay=true;//Options TRUE or FALSE
					zxingOverlay=LayoutInflater.FromContext(this).Inflate(Resource.Layout.QROverlay,null);//Start QRScanner Overlay


					flashButton=zxingOverlay.FindViewById<Button>(Resource.Id.buttonZxingFlash);//Instance button for flashlight
					flashButton.Click +=(sender,e)=>scanner.ToggleTorch();//Event to start flashlight

					scanner.CustomOverlay=zxingOverlay;//Display QRScanner View
					var result=await scanner.Scan();//Await scaning result from qr scanner
					HandleScanResult(result);//forwards QRScanner result to HandleScanResult where we can continue to play with him.
					//SetContentView (Resource.Layout.Splash);
					//main=LayoutInflater.FromContext(this).Inflate(Resource.Layout.Splash);
					//StartActivity(typeof(HopSampleApp.Activities.LoginActivity));
				}
				catch(Exception error)
				{
					Log.Error(Opapp,String.Format("Error fetching data: {0}",error.Message));//Print Error if scanning fail.

				}
			};
			//Skip Button listener
			Button SKIPButton = (Button)FindViewById (Resource.Id.SkipButton1);
			SKIPButton.Click += delegate {

				StartActivity(typeof(HopSampleApp.Activities.ContactsActivity));//Skip Splash View and go to ContactsActivity
			};
		}

		//
		public void HandleScanResult(ZXing.Result result)
		{
			//The logic for settings


			string msg = "";
			if (result != null && !string.IsNullOrEmpty (result.Text)) {
				if (result.Text.Contains (".json")) {
					JSONParser.ParseDataCompleteURL (result.Text);
				} 
				else {
					JSONParser.ParseData (result.Text);

				}
				msg = "QR Code Settings are set!";
			}
			else
				msg="Scanning Canceled!";
			this.RunOnUiThread (()=>Toast.MakeText(this,msg,ToastLength.Short).Show());








			//http://profiles.hookflash.com/itunes-appstore/config.json
		}
	}
}

