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
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Webkit;
using Android.OS;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;
using Java.Net;
using Org.Json;
using Java.Util;
using System.Runtime.InteropServices;
using System.Threading;
using Android.Util;
using Java.Interop;
using OpenPeerSdk.Helpers;
using HopSampleApp.CSAccountLogin;
using HopSampleApp.CSCoreLogin;
using HopSampleApp.CSIdentityLogin;

namespace HopSampleApp
{
	namespace Activities
	{
		[LoggerSubsystem("hop_sample_app")]
		[Activity (Label = "LoginActivity")]			
		public class LoginActivity : Activity , LoginHandlerInterface
		{
			public WebView myWebView;
			public WebViewClient myWebViewClient = new MyWebViewClient();

			protected override void OnCreate (Bundle bundle)
			{
				base.OnCreate (bundle);
				//Load OP *.so openpeer and z_shared library
				CSOPLoadLibrary.SharedLoadLibrary ().OPLoadLibrary ();
				// Set our view from the "main" layout resource
				SetContentView (Resource.Layout.Login);
				OPMediaEngine.Init(Android.App.Application.Context);

				setupFacebookButton();
				setupAccountButton();
				setupIdentityButton();
				setupWebView();
				initializeCore();

			}
			private  void setupFacebookButton()
			{
				Button facebookButton = FindViewById<Button> (Resource.Id.btnFacebookLogin);
				facebookButton.Text="Facebook Login";
				facebookButton.Click += delegate 
				{
					new CoreLogin().Execute();

				};
			}

			private void setupAccountButton()
			{
				Button accountButton = FindViewById<Button> (Resource.Id.buttonAccountLogin);
				accountButton.Text = "Account Login";
				accountButton.Click += delegate 
				{
					new AccountLogin().Execute();

				};
			}

			private void setupIdentityButton()
			{
				Button accountButton = FindViewById<Button>(Resource.Id.buttonIdentityLogin);
				accountButton.Text = "Identity Login";
				accountButton.Click += delegate 
				{
					new IdentityLogin().Execute();

				};
			}

			private void initializeCore()
			{
				LoginManager.initializeContext(Android.App.Application.Context);
			}

			private void setupWebView()
			{
				myWebView = FindViewById<WebView> (Resource.Id.webViewLogin);
				myWebView.Settings.JavaScriptEnabled = true;
				myWebView.Settings.JavaScriptCanOpenWindowsAutomatically = true;
				myWebView.Settings.DomStorageEnabled = true;
				LoginManager.SharedLoginManager().setHandlerListener(this);
				myWebView.SetWebViewClient (myWebViewClient);
			}
					
			public override bool OnCreateOptionsMenu (IMenu menu)
			{
				// Inflate the menu; this adds items to the action bar if it is present.
				//getMenuInflater().inflate(R.menu.login_screen, menu);
				//return base.OnCreateOptionsMenu (menu);
				//MenuInflater.Inflate()
				return true;
			}

			public void onLoadOuterFrameHandle(Object obj)
			{
				if (obj != null)
				{
					PropertiesForWebClient.SharedProperty().mNamespaceGrantUrl = obj.ToString();
				}

				this.RunOnUiThread (()=>
					{
						if(string.IsNullOrEmpty(PropertiesForWebClient.SharedProperty().mNamespaceGrantUrl))
						{
							Log.Debug("JNI", "DEBUG: Load outer frame");

							myWebView.LoadUrl("http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose");

						}
						else if (PropertiesForWebClient.SharedProperty().mNamespaceGrantUrl.Contains("grant"))
						{
							Log.Debug("JNI", "DEBUG: Load namespace grant frame");

							PropertiesForWebClient.SharedProperty().mNamespaceGrantStarted = true;

							myWebView.LoadUrl(PropertiesForWebClient.SharedProperty().mNamespaceGrantUrl);
						}
					});
			}

			public void  onInnerFrameInitialized(string innerFrameUrl)
			{
				PropertiesForWebClient.SharedProperty().mInnerFrameUrl =innerFrameUrl;

				PropertiesForWebClient.SharedProperty().mInnerFrameLoaded = true;
				this.RunOnUiThread (() => 
					{

						String cmd = String.Format("javascript:initInnerFrame('{0}')",PropertiesForWebClient.SharedProperty().mInnerFrameUrl);
						Log.Warn("JNI", "INIT INNER FRAME: " + cmd);
						myWebView.LoadUrl(cmd);
					}
				);
			}

			public void onNamespaceGrantInnerFrameInitialized(string innerFrameUrl)
			{
				Console.WriteLine (String.Format("onnamespace:{0}",innerFrameUrl));
				PropertiesForWebClient.SharedProperty().mNamespaceGrantUrl = innerFrameUrl;
				PropertiesForWebClient.SharedProperty().mNamespaceGrantInnerFrameLoaded = true;
				this.RunOnUiThread (() =>
					{
						String cmd = String.Format("javascript:initInnerFrame('{0}')",PropertiesForWebClient.SharedProperty().mNamespaceGrantUrl);
						Log.Warn("JNI", "INIT NAMESPACE GRANT INNER FRAME: " + cmd);
						myWebView.LoadUrl(cmd);
					}
				);
			}

			public void onAccountStateReady()
			{
				CSOPTestIdentityLookup.execute(LoginManager.mIdentity);
			}

			public void onIdentityLookupCompleted()
			{
				Intent intent = new Intent (this, typeof(InviteActivity));
				StartActivity (intent);
			}

			public void onDownloadedRolodexContacts(OPIdentity identity)
			{
				CSOPTestIdentityLookup.isContactsDownloaded = true;
				CSOPTestIdentityLookup.execute(identity);
			}

			public void passMessageToJS(String msg)
			{
				PropertiesForWebClient.SharedProperty().mMessageForInnerFrame = msg;
				this.RunOnUiThread (() =>
					{
						String cmd = String.Format("javascript:sendBundleToJS('{0}')",PropertiesForWebClient.SharedProperty().mMessageForInnerFrame);
						Log.Warn("JNI", "Pass to JS: " + cmd);
						myWebView.LoadUrl(cmd);
					}
				);

			}

		}

	}

}
