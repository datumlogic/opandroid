package com.openpeer.openpeernativesampleapp;

import org.webrtc.videoengine.ViERenderer;

import com.openpeer.openpeernativesampleapp.R;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackMessageQueue;
import com.openpeer.javaapi.test.OPTestMediaEngine;
import com.openpeer.openpeernativesampleapp.LoginManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LoginScreen extends Activity implements LoginHandlerInterface{

	//LoginHandlerInterface loginHandler;
	WebView myWebView;
	SurfaceView myLocalSurface = null;
	SurfaceView myRemoteSurface = null;
	int mediaEngineStatus = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		
		setupFacebookButton();
		setupWebView();
		setupVideo();
	}

	private void setupFacebookButton() {
		final Button facebookButton = (Button) findViewById(R.id.buttonMedia);
		facebookButton.setText("Start Video Capture");
		
		facebookButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				//OPStack stack = new OPStack();
				//LoginManager loginManager = new LoginManager();
				
				//
				//new CoreLogin().execute();
				//stack.setup(stackDelegate, mediaEngineDelegate, appID, appName, appImageURL, appURL, userAgent, deviceID, os, system)
				switch (mediaEngineStatus) {
				case 0:
					OPMediaEngine.init(getApplicationContext());
					OPMediaEngine.getInstance().startVideoCapture();
					facebookButton.setText("Start Audio/Video Channel");
					mediaEngineStatus++;
					break;
				case 1:
					OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);
					((OPTestMediaEngine) OPMediaEngine.getInstance()).setReceiverAddress("127.0.0.1");
					((OPTestMediaEngine) OPMediaEngine.getInstance()).startVoice();
					((OPTestMediaEngine) OPMediaEngine.getInstance()).startVideoChannel();
					facebookButton.setText("Stop Audio/Video Channel");
					mediaEngineStatus++;
					break;
				case 2:
					((OPTestMediaEngine) OPMediaEngine.getInstance()).stopVoice();
					((OPTestMediaEngine) OPMediaEngine.getInstance()).stopVideoChannel();
					facebookButton.setText("Stop Video Capture");
					mediaEngineStatus++;
					break;
				case 3:
					OPMediaEngine.getInstance().stopVideoCapture();
					facebookButton.setText("Start Video Capture");
					mediaEngineStatus = 0;
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void setupWebView()
	{
	}
	
	private void setupVideo()
	{
		myLocalSurface = ViERenderer.CreateLocalRenderer(this);
		myRemoteSurface = ViERenderer.CreateRenderer(this, true);
		LinearLayout localViewLinearLayout = (LinearLayout) findViewById(R.id.localViewLinearLayout);
		LinearLayout remoteViewLinearLayout = (LinearLayout) findViewById(R.id.remoteViewLinearLayout);
		localViewLinearLayout.addView(myLocalSurface);
		remoteViewLinearLayout.addView(myRemoteSurface);
		//Button facebookButton = (Button) findViewById(R.id.buttonMedia);
		//facebookButton.bringToFront();
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
