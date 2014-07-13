/*
Copyright (c) 2014, hookflash Inc.
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FreeBSD Project.
*/
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
	public class MyWebViewClient:WebViewClient
	{
		#region ShouldOverrideUrlLoading
		public override bool ShouldOverrideUrlLoading (WebView view, string url)
		{
			view.LoadUrl (url);
			return true;
		}
		#endregion

		#region OnPageStarted
		public override void OnPageStarted (WebView view, string url, Android.Graphics.Bitmap favicon)
		{
			base.OnPageStarted (view, url, favicon);
		}
		#endregion

		#region OnLoadResource
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
		#endregion

		#region ShouldInterceptRequest
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
			
				if(!PropertiesForWebClient.SharedProperty().mNamespaceGrantStarted)
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
		#endregion

		#region OnPageFinished

		public override void OnPageFinished (WebView view, string url)
		{
			if (!PropertiesForWebClient.SharedProperty().mNamespaceGrantStarted)
			{
				if(!PropertiesForWebClient.SharedProperty().mInnerFrameLoaded)
				{
					RelativeLayout.LayoutParams linearLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FillParent,RelativeLayout.LayoutParams.FillParent);
					linearLayoutParams.SetMargins(0, 0, 0, 0);
					view.LayoutParameters = linearLayoutParams;
					CSFramesHandler.SharedCSFramesHandler ().initInnerFrame ();
				}
				else
				{
					base.OnPageFinished (view, url);
				}
			}
			else
			{
				if(!PropertiesForWebClient.SharedProperty().mNamespaceGrantInnerFrameLoaded)
				{
					RelativeLayout.LayoutParams linearLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FillParent,RelativeLayout.LayoutParams.FillParent);
					linearLayoutParams.SetMargins(0, 0, 0, 0);
					view.LayoutParameters = linearLayoutParams;
					CSFramesHandler.SharedCSFramesHandler ().initNamespaceGrantInnerFrame ();
				}
				else
				{
					base.OnPageFinished (view, url);
				}
			}

		}
		#endregion

		#region OnReceivedError
		public void OnReceivedError (WebView view, int errorCode, string description, string failingUrl)
		{
			errorCode = 1;
			errorCode++;
			//base.OnReceivedError (view, errorCode, description, failingUrl);
		}
		#endregion
	}
}

