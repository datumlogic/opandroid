package com.openpeer.openpeernativesampleapp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.openpeer.openpeernativesampleapp.R;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackMessageQueue;
import com.openpeer.openpeernativesampleapp.LoginManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

public class LoginScreen extends Activity implements LoginHandlerInterface{

	//LoginHandlerInterface loginHandler;
	WebView myWebView;
	WebViewClient myWebViewClient = new MyWebViewClient();
	String mInnerFrameUrl;
	String mMessageForInnerFrame;
	String mNamespaceGrantUrl = "";
	boolean mInnerFrameLoaded = false;
	boolean mNamespaceGrantInnerFrameLoaded = false;
	boolean mNamespaceGrantStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		
		OPMediaEngine.init(getApplicationContext());

		setupFacebookButton();
		setupAccountButton();
		setupIdentityButton();
		setupWebView();
		initializeCore();
	}

	private void setupFacebookButton() {
		Button facebookButton = (Button) findViewById(R.id.btnFacebookLogin);

		facebookButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { 
				//OPStack stack = new OPStack();
				//LoginManager loginManager = new LoginManager();

				//
				new CoreLogin().execute();
				//stack.setup(stackDelegate, mediaEngineDelegate, appID, appName, appImageURL, appURL, userAgent, deviceID, os, system)
			}
		});
	}

	private void setupAccountButton() {
		Button accountButton = (Button) findViewById(R.id.buttonAccountLogin);

		accountButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { 
				//OPStack stack = new OPStack();
				//LoginManager loginManager = new LoginManager();

				//
				new AccountLogin().execute();
				//stack.setup(stackDelegate, mediaEngineDelegate, appID, appName, appImageURL, appURL, userAgent, deviceID, os, system)
			}
		});
	}

	private void setupIdentityButton() {
		Button accountButton = (Button) findViewById(R.id.buttonIdentityLogin);

		accountButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { 
				//OPStack stack = new OPStack();
				//LoginManager loginManager = new LoginManager();

				//
				new IdentityLogin().execute();
				//stack.setup(stackDelegate, mediaEngineDelegate, appID, appName, appImageURL, appURL, userAgent, deviceID, os, system)
			}
		});
	}

	private void initializeCore()
	{
		LoginManager.initializeContext(getApplicationContext());
	}

	private void setupWebView()
	{
		myWebView = (WebView) findViewById(R.id.webViewLogin);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		LoginManager.setHandlerListener(this);
		myWebView.setWebViewClient(myWebViewClient);
		//
	}

	private class MyWebViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
		@Override
		public void onLoadResource(WebView view, String url)
		{
			//view.loadUrl(url);
			if (url.contains("datapass"))
			{
				int i = 1;
				i++;
			}
			else
			{
				super.onLoadResource(view, url);
			}

		}
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url)
		{
			if (url.contains("datapass"))
			{
				//Log.w("JNI", url);
				String data = url.substring(url.lastIndexOf("data="));
				data = data.substring(5);
				//Log.w("JNI", data);
				try {
					data = URLDecoder.decode(data, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!mNamespaceGrantStarted)
				{
					Log.w("JNI", "Identity Received from JS: " + data);
					LoginManager.mIdentity.handleMessageFromInnerBrowserWindowFrame(data);
				}
				else
				{
					Log.w("JNI", "NS GRANT Received from JS: " + data);
					LoginManager.mAccount.handleMessageFromInnerBrowserWindowFrame(data);
				}
				return null;
			}
			else if(url.contains("?reload=true"))
			{
				int i = 1;
				i++;
				return null;
			}
			else
			{
				return super.shouldInterceptRequest(view, url);
			}

		}
		@Override
		public void onPageFinished(WebView view, String url) {

			if (!mNamespaceGrantStarted)
			{
				if(!mInnerFrameLoaded)
				{
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
					view.setLayoutParams(params);
					LoginManager.initInnerFrame();
				}
				else
				{
					super.onPageFinished(view, url);
				}
			}
			else
			{
				if(!mNamespaceGrantInnerFrameLoaded)
				{
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
					view.setLayoutParams(params);
					LoginManager.initNamespaceGrantInnerFrame();
				}
				else
				{
					super.onPageFinished(view, url);
				}
			}
		}
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			int i = 1;
			i++;
		}
	}

	private class CoreLogin extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			try {
				LoginManager.LoginWithFacebook();
			} catch (Exception e) { //TO DO: LBOJAN fix this code
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TextView txt = (TextView) findViewById(R.id.output);
			//txt.setText("Executed");
			return null;
		}        

		@Override
		protected void onPostExecute(Void result) {             
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private class AccountLogin extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			try {
				LoginManager.AccountLogin();
			} catch (Exception e) { //TO DO: LBOJAN fix this code
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TextView txt = (TextView) findViewById(R.id.output);
			//txt.setText("Executed");
			return null;
		}        

		@Override
		protected void onPostExecute(Void result) {             
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private class IdentityLogin extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			try {
				LoginManager.startIdentityLogin();
			} catch (Exception e) { //TO DO: LBOJAN fix this code
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TextView txt = (TextView) findViewById(R.id.output);
			//txt.setText("Executed");
			return null;
		}        

		@Override
		protected void onPostExecute(Void result) {             
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	static {
		try {
			System.loadLibrary("z_shared");
			System.loadLibrary("openpeer");
		} catch (UnsatisfiedLinkError use) {
			Log.e("JNI", "WARNING: Could not load libopenpeer.so");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_screen, menu);
		return true;
	}

	@Override
	public void onLoadOuterFrameHandle(Object obj) {
		// TODO Auto-generated method stub
		if (obj != null)
		{
			mNamespaceGrantUrl = (String) obj;
		}
		this.runOnUiThread(new Runnable() {
			public void run() {
				if(mNamespaceGrantUrl.isEmpty())
				{
					Log.d("JNI", "DEBUG: Load outer frame");
					myWebView.loadUrl("http://jsouter-v1-beta-1-i.hcs.io/identity.html?view=choose&federated=false");
				}
				else if (mNamespaceGrantUrl.contains("grant"))
				{
					Log.d("JNI", "DEBUG: Load namespace grant frame");
					mNamespaceGrantStarted = true;
					myWebView.loadUrl(mNamespaceGrantUrl);
				}
			}
		});
	}
	@Override
	public void onInnerFrameInitialized(String innerFrameUrl)
	{
		mInnerFrameUrl = innerFrameUrl;
		mInnerFrameLoaded = true;
		this.runOnUiThread(new Runnable() {
			public void run() {
				String cmd = String.format("javascript:initInnerFrame(\'%s\')",mInnerFrameUrl);
				Log.w("JNI", "INIT INNER FRAME: " + cmd);
				myWebView.loadUrl( cmd);
			}
		});

	}
	@Override
	public void onNamespaceGrantInnerFrameInitialized(String innerFrameUrl)
	{
		mNamespaceGrantUrl = innerFrameUrl;
		mNamespaceGrantInnerFrameLoaded = true;
		this.runOnUiThread(new Runnable() {
			public void run() {
				String cmd = String.format("javascript:initInnerFrame(\'%s\')",mNamespaceGrantUrl);
				Log.w("JNI", "INIT NAMESPACE GRANT INNER FRAME: " + cmd);
				myWebView.loadUrl( cmd);
			}
		});

	}

	@Override
	public void passMessageToJS(String msg)
	{
		mMessageForInnerFrame = msg;
		this.runOnUiThread(new Runnable() {
			public void run() {
				String cmd = String.format("javascript:sendBundleToJS(\'%s\')",mMessageForInnerFrame);
				Log.w("JNI", "Pass to JS: " + cmd);
				myWebView.loadUrl( cmd);
			}
		});
	}


}

interface LoginHandlerInterface
{
	void onLoadOuterFrameHandle(Object obj);
	void onInnerFrameInitialized(String innerFrameUrl);
	void passMessageToJS(String msg);
	void onNamespaceGrantInnerFrameInitialized(String innerFrameUrl);
}
