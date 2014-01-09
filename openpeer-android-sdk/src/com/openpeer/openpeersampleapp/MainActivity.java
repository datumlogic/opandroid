package com.openpeer.openpeersampleapp;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import com.openpeer.javaapi.OPStack;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupFacebookButton();
		setupWebView();
	}

	private void setupFacebookButton() {
		Button facebookButton = (Button) findViewById(R.id.btnFacebookLogin);
		
		facebookButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				//OPStack stack = new OPStack();
				//LoginManager loginManager = new LoginManager();
				LoginManager.LoginWithFacebook();
				//stack.setup(stackDelegate, mediaEngineDelegate, appID, appName, appImageURL, appURL, userAgent, deviceID, os, system)
			}
		});
	}
	
	private void setupWebView()
	{
		WebView myWebView = (WebView) findViewById(R.id.webViewLogin);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		myWebView.loadUrl("http://www.google.com");
	}
	
	static {
//		try {
//		    //System.loadLibrary("mysharedlibrary");
//		    System.loadLibrary("stlport_shared");
//		} catch (UnsatisfiedLinkError use) {
//		    Log.e("JNI", "WARNING: Could not load libstlport_shared.so");
//		}
		try {
		    //System.loadLibrary("mysharedlibrary");
		    System.loadLibrary("opjni");
		} catch (UnsatisfiedLinkError use) {
		    Log.e("JNI", "WARNING: Could not load libopjni.so");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
