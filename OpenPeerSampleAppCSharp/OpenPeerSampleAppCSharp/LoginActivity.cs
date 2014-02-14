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

using Android.Webkit;
using Java.Interop;

namespace OpenPeerSampleAppCSharp
{
	[Activity (Label = "LoginActivity")]			
	public class LoginActivity : Activity
	{
		public void OnMessageFromInner(string message)
		{
			if (message == "finish") {
				Finish ();
				return;
			}
			Console.WriteLine ("LoginActivity.OnMessageFromInner invoked!");
			Toast.MakeText (this, "Message from inner: " + message, ToastLength.Short).Show ();
		}

		const string html = @"
	<html>
    <head>
    <script>function sendToInner(message) {alert(message);}</script>
    </head>
	<body>
	<p>This is a paragraph.</p>
	<button type=""button"" onClick=""LoginActivity.handle('some json message')"">Fake sendToOuter</button>
	<button type=""button"" onClick=""sendToInner('hello')"">Fake sendToInner</button>
	<button type=""button"" onClick=""LoginActivity.handle('finish')"">Finish</button>
	</body>
	</html>";

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			SetContentView (Resource.Layout.Login);

			WebView view = FindViewById<WebView> (Resource.Id.loginWebView);
			view.Settings.JavaScriptEnabled = true;
			view.SetWebChromeClient (new WebChromeClient ());
			view.AddJavascriptInterface (new LoginActivityRedirect (this), "LoginActivity");
			view.LoadData (html, "text/html", null);
		}

		protected void sendToInner(string message)
		{
			string escaped = message.Replace ("\\", "\\\\").Replace ("'", "\\'");

			WebView view = FindViewById<WebView> (Resource.Id.loginWebView);
			view.LoadUrl("javascript:sendToInner('" + escaped + "')");
		}
	}

	class LoginActivityRedirect : Java.Lang.Object
	{
		public LoginActivityRedirect (Context context)
		{
			this.context = context;
		}

		public LoginActivityRedirect (IntPtr handle, JniHandleOwnership transfer)
			: base (handle, transfer)
		{
		}

		Context context;

		[Export ("handle")]
		// to become consistent with Java/JS interop convention, the argument cannot be System.String.
		public void handle (Java.Lang.String message)
		{
			LoginActivity outer = (LoginActivity)context;

			outer.OnMessageFromInner (message.ToString ());
		}
	}
}
