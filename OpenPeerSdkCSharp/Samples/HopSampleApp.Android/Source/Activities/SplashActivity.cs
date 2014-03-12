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



namespace HopSampleApp
{
	/// <summary>
	/// Splash activity which has a built-in ZXing QRScanner who runs or skips with the click of a button.
	/// </summary>
	[Activity (Theme = "@style/Theme.Splash",MainLauncher = true,NoHistory = true,Icon="@drawable/op")]			
	public class SplashActivity : Activity 
	{
		MobileBarcodeScanner scanner;//qr code scanner
		View zxingOverlay;//View of qr code scanner
		Button flashButton;//Button for turn flash ON/OFF
		string Opapp="OP QR Scanner";//Title for error-s

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Run Splash View on start
			SetContentView (Resource.Layout.Splash);
			scanner = new MobileBarcodeScanner (this);

			//QRScanner Button listener
			Button QRScannerButton = (Button)FindViewById (Resource.Id.QRButton1);
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

		public void HandleScanResult(ZXing.Result result)
		{
			//The logic for settings goes hire ,i make it now

			/*
			string msg = "";
			if (result != null && !string.IsNullOrEmpty (result.Text))
				msg = "QR Code Scanned: " + result.Text;
			else
				msg="Scanning Canceled!";
			this.RunOnUiThread (()=>Toast.MakeText(this,msg,ToastLength.Short).Show());
			*/
			/*  JUST EXAMPLE  */


			//parser (result.Text);

		}
	


	}


}

