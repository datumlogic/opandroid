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
			public static  String mInnerFrameUrl;
			public static  String mMessageForInnerFrame;
			public static String mNamespaceGrantUrl = "";
			public static  Boolean mInnerFrameLoaded = false;
			public static  Boolean mNamespaceGrantInnerFrameLoaded = false;
			public static Boolean mNamespaceGrantStarted = false;

			protected override void OnCreate (Bundle bundle)
			{
				base.OnCreate (bundle);
				try
				{
					Console.WriteLine("Start loading z_shared and openpeer library...");


					try
					{
						Console.WriteLine("z_shared start loading....");
						Java.Lang.JavaSystem.LoadLibrary("z_shared");
						Console.WriteLine("Library z_shared is loaded!");
					}
					catch(Exception error)
					{
						Console.WriteLine(String.Format("z_share library is not loaded! Error:{0}",error.Message));
					}

					try
					{
						Console.WriteLine("openpeer start loading....");
						Java.Lang.JavaSystem.LoadLibrary("openpeer");
						Console.WriteLine("Library openpeer is loaded");
					}
					catch(Exception error)
					{
						Console.WriteLine(String.Format("openpeer library is not loaded! Error:{0}",error.Message));
					}
				}
				catch(Exception error)
				{
					Console.WriteLine (String.Format("Error Loading z_shared and openpeer library",error.Message));
				}
				// Set our view from the "main" layout resource
				SetContentView (Resource.Layout.Login);
				OPMediaEngine.Init(Android.App.Application.Context);

				setupFacebookButton();
				setupAccountButton();
				setupIdentityButton();
				setupWebView();
				//LoginManager.setHandlerListener(this);
				initializeCore();

			}
			private  void setupFacebookButton() {
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

			private class MyWebViewClient:WebViewClient
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
							LoginManager.SharedLoginManager().initInnerFrame();
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
							LoginManager.SharedLoginManager().initNamespaceGrantInnerFrame();
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

			}

			private class CoreLogin:AsyncTask
			{

				protected  override Java.Lang.Object DoInBackground (params Java.Lang.Object[] @params)
				{
					try
					{
						LoginManager.LoginWithFacebook();
					} 
					catch (Exception e)
					{

						Console.WriteLine (String.Format ("Error CoreLogin:{0}", e.StackTrace));
					}

					return null;
					//throw new NotImplementedException ();
				}
				protected override void OnPostExecute (Java.Lang.Object result)
				{
					//base.OnPostExecute (result);
				}

				protected override void OnPreExecute ()
				{
					//base.OnPreExecute ();
				}

				protected override void OnProgressUpdate (params Java.Lang.Object[] values)
				{
					//base.OnProgressUpdate (values);
				}


			}
			private class AccountLogin:AsyncTask
			{
				protected override Java.Lang.Object DoInBackground (params Java.Lang.Object[] @params)
				{
					try
					{
						LoginManager.SharedLoginManager().AccountLogin();
					} 
					catch (Exception e) 
					{
						Console.WriteLine (String.Format ("Error AccountLogin:{0}", e.StackTrace));

					}

					return null;
					//throw new NotImplementedException ();
				}

				protected override void OnPostExecute (Java.Lang.Object result)
				{
					//base.OnPostExecute (result);
				}

				protected override void OnPreExecute ()
				{
					//base.OnPreExecute ();
				}



				protected override void OnProgressUpdate (params Java.Lang.Object[] values)
				{
					//base.OnProgressUpdate (values);
				}
			}
			private class IdentityLogin:AsyncTask
			{
				protected override Java.Lang.Object DoInBackground (params Java.Lang.Object[] @params)
				{

					try
					{
						LoginManager.SharedLoginManager().StartIdentityLogin();
					}
					catch (Exception e)
					{
						Console.WriteLine (String.Format ("Error IdentityLogin:{0}", e.StackTrace));
					}

					return null;
				}

				protected override void OnPostExecute (Java.Lang.Object result)
				{
					//base.OnPostExecute (result);
				}

				protected override void OnPreExecute ()
				{
					//base.OnPreExecute ();
				}

				protected override void OnProgressUpdate (params Java.Lang.Object[] values)
				{
					//base.OnProgressUpdate (values);
				}


			}
			public override bool OnCreateOptionsMenu (IMenu menu)
			{
				// Inflate the menu; this adds items to the action bar if it is present.
				//getMenuInflater().inflate(R.menu.login_screen, menu);
				//return base.OnCreateOptionsMenu (menu);
				//MenuInflater.Inflate()
				return true;
			}


			//this need to override
			public void onLoadOuterFrameHandle(Object obj) {

				if (obj != null)
				{

					mNamespaceGrantUrl = obj.ToString();

				}

				this.RunOnUiThread (()=>
					{
						if(string.IsNullOrEmpty(mNamespaceGrantUrl))
						{
							Log.Debug("JNI", "DEBUG: Load outer frame");

							myWebView.LoadUrl("http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose");

						}
						else if (mNamespaceGrantUrl.Contains("grant"))
						{
							Log.Debug("JNI", "DEBUG: Load namespace grant frame");

							mNamespaceGrantStarted = true;

							myWebView.LoadUrl(mNamespaceGrantUrl);
						}
					});



			}

			public void  onInnerFrameInitialized(string innerFrameUrl)
			{
				mInnerFrameUrl =innerFrameUrl;

				mInnerFrameLoaded = true;
				this.RunOnUiThread (() => 
					{

						String cmd = String.Format("javascript:initInnerFrame('{0}')",mInnerFrameUrl);
						Log.Warn("JNI", "INIT INNER FRAME: " + cmd);
						myWebView.LoadUrl(cmd);
					}
				);


			}


			public void onNamespaceGrantInnerFrameInitialized(string innerFrameUrl)
			{
				Console.WriteLine (String.Format("onnamespace:{0}",innerFrameUrl));
				mNamespaceGrantUrl = innerFrameUrl;
				mNamespaceGrantInnerFrameLoaded = true;
				this.RunOnUiThread (() =>
					{
						String cmd = String.Format("javascript:initInnerFrame('{0}')",mNamespaceGrantUrl);
						Log.Warn("JNI", "INIT NAMESPACE GRANT INNER FRAME: " + cmd);
						myWebView.LoadUrl(cmd);
					}
				);





			}
			public void onAccountStateReady() {



			}
			public void onLookupCompleted() {



			}

			public void onDownloadedRolodexContacts(OPIdentity identity)
			{

			}

			public void passMessageToJS(String msg)
			{
				mMessageForInnerFrame = msg;
				this.RunOnUiThread (() =>
					{
						String cmd = String.Format("javascript:sendBundleToJS('{0}')",mMessageForInnerFrame);
						Log.Warn("JNI", "Pass to JS: " + cmd);
						myWebView.LoadUrl(cmd);
					}
				);

			}

		}

	}
	interface LoginHandlerInterface
	{
		void onLoadOuterFrameHandle(Object obj);
		void onInnerFrameInitialized(String innerFrameUrl);
		void passMessageToJS(String msg);
		void onNamespaceGrantInnerFrameInitialized(String innerFrameUrl);
		void onDownloadedRolodexContacts(OPIdentity identity);
		void onAccountStateReady();
		void onLookupCompleted();
	}
}
