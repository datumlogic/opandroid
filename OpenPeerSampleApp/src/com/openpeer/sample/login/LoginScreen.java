package com.openpeer.sample.login;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.openpeer.app.OPSdkConfig;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.test.OPTestAccount;
import com.openpeer.javaapi.test.OPTestConversationThread;
import com.openpeer.javaapi.test.OPTestIdentityLookup;
import com.openpeer.sample.R;
import com.openpeer.sample.contacts.ContactsActivity;

public class LoginScreen extends Activity implements LoginHandlerInterface {

	// LoginHandlerInterface loginHandler;
	WebView myWebView;
	WebViewClient myWebViewClient = new MyWebViewClient();
	String mInnerFrameUrl;
	String mMessageForInnerFrame;
	String mNamespaceGrantUrl = "";
	boolean mInnerFrameLoaded = false;
	boolean mNamespaceGrantInnerFrameLoaded = false;
	boolean mNamespaceGrantStarted = false;

	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		progressBar = (ProgressBar) findViewById(R.id.progress);
		// OPMediaEngine.init(getApplicationContext());

		// setupAccountButton();
		// setupIdentityButton();
		setupWebView();
		new AccountLogin().execute();
	}

	private void setupWebView() {
		myWebView = (WebView) findViewById(R.id.webViewLogin);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		LoginManager.setHandlerListener(this);
		myWebView.setWebViewClient(myWebViewClient);
		//
	}

	private class MyWebViewClient extends WebViewClient {
		private String DATAPASS = "datapass";

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
		public void onLoadResource(WebView view, String url) {
			// view.loadUrl(url);
			if (url.contains(DATAPASS)) {
				int i = 1;
				i++;
			} else {
				super.onLoadResource(view, url);
			}

		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			if (url.contains(DATAPASS)) {
				Log.w("JNI", url);
				String data = url.substring(url.lastIndexOf("data="));
				data = data.substring(5);
				// Log.w("JNI", data);
				try {
					data = URLDecoder.decode(data, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!mNamespaceGrantStarted) {
					Log.w("JNI", "Identity Received from JS: " + data);
					LoginManager.mIdentity
							.handleMessageFromInnerBrowserWindowFrame(data);
				} else {
					Log.w("JNI", "NS GRANT Received from JS: " + data);
					LoginManager.mAccount
							.handleMessageFromInnerBrowserWindowFrame(data);
				}
				return null;
			} else if (url.contains("?reload=true")) {
				int i = 1;
				i++;
				return null;
			} else {
				return super.shouldInterceptRequest(view, url);
			}

		}

		@Override
		public void onPageFinished(WebView view, String url) {

			if (!mNamespaceGrantStarted) {
				if (!mInnerFrameLoaded) {
					// RelativeLayout.LayoutParams params = new
					// RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
					// RelativeLayout.LayoutParams.FILL_PARENT);
					// view.setLayoutParams(params);
					LoginManager.initInnerFrame();
				} else {
					super.onPageFinished(view, url);
				}
			} else {
				if (!mNamespaceGrantInnerFrameLoaded) {
					// RelativeLayout.LayoutParams params = new
					// RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
					// RelativeLayout.LayoutParams.FILL_PARENT);
					// view.setLayoutParams(params);
					LoginManager.initNamespaceGrantInnerFrame();
				} else {
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

	private class AccountLogin extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			try {
				LoginManager.getInstance(LoginScreen.this).AccountLogin();
			} catch (Exception e) { // TO DO: LBOJAN fix this code
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TextView txt = (TextView) findViewById(R.id.output);
			// txt.setText("Executed");
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onLoadOuterFrameHandle(final String namespaceGrantUrl) {

		this.runOnUiThread(new Runnable() {
			public void run() {
				progressBar.setVisibility(View.GONE);
				myWebView.setVisibility(View.VISIBLE);
				if (null == namespaceGrantUrl || namespaceGrantUrl.isEmpty()) {
					Log.d("JNI", "DEBUG: Load outer frame");
					myWebView.loadUrl(OPSdkConfig.getInstance()
							.getNamespaceGrantServiceUrl());// "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose");
				} else if (namespaceGrantUrl.contains("grant")) {
					Log.d("JNI", "DEBUG: Load namespace grant frame");
					mNamespaceGrantStarted = true;
					myWebView.loadUrl(namespaceGrantUrl);
				}
			}
		});
	}

	@Override
	public void onInnerFrameInitialized(final String innerFrameUrl) {
		mInnerFrameLoaded = true;
		this.runOnUiThread(new Runnable() {
			public void run() {
				String cmd = String.format("javascript:initInnerFrame(\'%s\')",
						innerFrameUrl);
				Log.w("JNI", "INIT INNER FRAME: " + cmd);
				myWebView.loadUrl(cmd);
			}
		});

	}

	@Override
	public void onNamespaceGrantInnerFrameInitialized(final String innerFrameUrl) {
		mNamespaceGrantUrl = innerFrameUrl;
		mNamespaceGrantInnerFrameLoaded = true;
		this.runOnUiThread(new Runnable() {
			public void run() {
				String cmd = String.format("javascript:initInnerFrame(\'%s\')",
						innerFrameUrl);
				Log.w("JNI", "INIT NAMESPACE GRANT INNER FRAME: " + cmd);
				myWebView.loadUrl(cmd);
			}
		});

	}

	@Override
	public void passMessageToJS(final String msg) {
		// mMessageForInnerFrame = msg;
		this.runOnUiThread(new Runnable() {
			public void run() {
				String cmd = String.format("javascript:sendBundleToJS(\'%s\')",
						msg);
				Log.w("JNI", "Pass to JS: " + cmd);
				myWebView.loadUrl(cmd);
			}
		});
	}

	@Override
	public void onAccountStateReady() {
		myWebView.setVisibility(View.GONE);
	}

	@Override
	public void onDownloadedRolodexContacts(OPIdentity identity) {
		// TODO Auto-generated method stub
		OPTestIdentityLookup.isContactsDownloaded = true;
		// OPTestIdentityLookup.execute(identity);

		ContactsActivity.launch(this);

	}

	@Override
	public void onLookupCompleted() {
		// TODO Auto-generated method stub
		OPTestConversationThread.execute();

	}

}

interface LoginHandlerInterface {
	void onLoadOuterFrameHandle(final String nameSpaceGrantUrl);

	void onInnerFrameInitialized(String innerFrameUrl);

	void passMessageToJS(String msg);

	void onNamespaceGrantInnerFrameInitialized(String innerFrameUrl);

	void onAccountStateReady();

	void onDownloadedRolodexContacts(OPIdentity identity);

	void onLookupCompleted();
}
