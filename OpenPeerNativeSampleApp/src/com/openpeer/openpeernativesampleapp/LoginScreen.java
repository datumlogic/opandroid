package com.openpeer.openpeernativesampleapp;

import org.webrtc.videoengine.ViERenderer;

import com.openpeer.openpeernativesampleapp.R;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackMessageQueue;
import com.openpeer.openpeernativesampleapp.LoginManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

public class LoginScreen extends Activity implements LoginHandlerInterface{

	//LoginHandlerInterface loginHandler;
	WebView myWebView;
	SurfaceView myLocalSurface = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		
		setupFacebookButton();
		setupWebView();
		setupVideo();
	}

	private void setupFacebookButton() {
		Button facebookButton = (Button) findViewById(R.id.btnFacebookLogin);
		
		facebookButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				//OPStack stack = new OPStack();
				//LoginManager loginManager = new LoginManager();
				
				//
				//new CoreLogin().execute();
				//stack.setup(stackDelegate, mediaEngineDelegate, appID, appName, appImageURL, appURL, userAgent, deviceID, os, system)
				OPMediaEngine.init(getApplicationContext());
				OPMediaEngine.getInstance().startVideoCapture();
			}
		});
	}
	
	private void setupWebView()
	{
		myWebView = (WebView) findViewById(R.id.webViewLogin);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		LoginManager.setHandlerListener(this);
		myWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			          view.loadUrl(url);
			          return true;
			           }});
		//
	}
	
	private void setupVideo()
	{
		myLocalSurface = ViERenderer.CreateLocalRenderer(this);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		//RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		//layoutParams.addRule(RelativeLayout.BELOW, R.id.btnFacebookLogin);
		//myLocalSurface.setLayoutParams(layoutParams);
		relativeLayout.addView(myLocalSurface);
		Button facebookButton = (Button) findViewById(R.id.btnFacebookLogin);
		facebookButton.bringToFront();
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
		myWebView.loadUrl("https://app-javascript.hookflash.me/outer.html");
	}


}

interface LoginHandlerInterface
{
   void onLoadOuterFrameHandle(Object obj);
}
