using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.Webkit;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;
using Android.Util;
using Java.Util;

using Org.Json;
using Java.Net;
using HopSampleApp;
namespace HopSampleApp
{
	/*
	public class MyWebViewClient:WebViewClient
	{
		public override bool ShouldOverrideUrlLoading (WebView view, string url)
		{
			view.LoadUrl (url);
			return true;
		}
		public override void OnPageStarted (WebView view, string url, Android.Graphics.Bitmap favicon)
		{
			base.OnPageStarted (view, url, favicon);
		}
		public override void OnLoadResource (WebView view, string url)
		{
			//view.loadUrl(url);
			if (url.Contains("datapass"))
			{
				int i = 1;
				i++;
			}
			else
			{
				base.OnLoadResource (view, url);
			}


		}

		public override WebResourceResponse ShouldInterceptRequest (WebView view, string url)
		{
			if (url.Contains("datapass"))
			{
				Log.Warn("JNI", url);
				String data = url.Substring(url.LastIndexOf("data="));
				data = data.Substring(5);
				Log.Warn("JNI", data);
				try
				{
					data = URLDecoder.Decode(data, "UTF-8");
				} 
				catch
				{

					Console.WriteLine ("Error ShouldInterceptRequest");
				}
			
				if(!mNamespaceGrantStarted)
				{
					Log.Warn("JNI", "Identity Received from JS: " + data);
					LoginManager.mIdentity.HandleMessageFromInnerBrowserWindowFrame(data);
				}
				else
				{
					Log.Warn("JNI", "NS GRANT Received from JS: " + data);
					LoginManager.mAccount.HandleMessageFromInnerBrowserWindowFrame(data);
				}
				return null;
			}
			else if(url.Contains("?reload=true"))
			{
				int i = 1;
				i++;
				return null;
			}
			else
			{
				return base.ShouldInterceptRequest (view, url);
			}

		}

		public override void OnPageFinished (WebView view, string url)
		{
			if (!mNamespaceGrantStarted)
			{
				if(!mInnerFrameLoaded)
				{
					RelativeLayout.LayoutParams linearLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FillParent,RelativeLayout.LayoutParams.FillParent);
					linearLayoutParams.SetMargins(0, 0, 0, 0);
					view.LayoutParameters = linearLayoutParams;
					LoginManager.initInnerFrame();
				}
				else
				{
					base.OnPageFinished (view, url);
				}
			}
			else
			{
				if(!mNamespaceGrantInnerFrameLoaded)
				{
					RelativeLayout.LayoutParams linearLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FillParent,RelativeLayout.LayoutParams.FillParent);
					linearLayoutParams.SetMargins(0, 0, 0, 0);
					view.LayoutParameters = linearLayoutParams;
					LoginManager.initNamespaceGrantInnerFrame();
				}
				else
				{
					base.OnPageFinished (view, url);
				}
			}

		}
		public void OnReceivedError (WebView view, int errorCode, string description, string failingUrl)
		{
			errorCode = 1;
			errorCode++;
			//base.OnReceivedError (view, errorCode, description, failingUrl);
		}
	}*/
}

