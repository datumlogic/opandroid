/*
 
 Copyright (c) 2014, SMB Phone Inc.
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

package com.openpeer.openpeernativesampleapp;

import org.webrtc.videoengine.ViERenderer;

import com.openpeer.openpeernativesampleapp.R;
import com.openpeer.javaapi.CameraTypes;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackMessageQueue;
import com.openpeer.javaapi.VideoOrientations;
import com.openpeer.javaapi.test.OPTestMediaEngine;
import com.openpeer.openpeernativesampleapp.LoginManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.sax.TextElementListener;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MediaScreen extends Activity {

	SurfaceView myLocalSurface = null;
	SurfaceView myRemoteSurface = null;
	int mediaEngineStatus = 0;
	boolean speakerphoneEnabled = false;
	boolean useFrontCamera = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_screen);

		myLocalSurface = ViERenderer.CreateLocalRenderer(this);
		myRemoteSurface = ViERenderer.CreateRenderer(this, true);
		LinearLayout localViewLinearLayout = (LinearLayout) findViewById(R.id.localViewLinearLayout);
		LinearLayout remoteViewLinearLayout = (LinearLayout) findViewById(R.id.remoteViewLinearLayout);
		localViewLinearLayout.addView(myLocalSurface);
		remoteViewLinearLayout.addView(myRemoteSurface);
		
		OPMediaEngine.init(getApplicationContext());
		if (useFrontCamera)
			OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Front);
		else
			OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Back);
		OPMediaEngine.getInstance().setEcEnabled(true);
		OPMediaEngine.getInstance().setAgcEnabled(true);
		OPMediaEngine.getInstance().setNsEnabled(false);
		OPMediaEngine.getInstance().setMuteEnabled(false);
		OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
		OPMediaEngine.getInstance().setContinuousVideoCapture(true);
		OPMediaEngine.getInstance().setDefaultVideoOrientation(VideoOrientations.VideoOrientation_Portrait);
		OPMediaEngine.getInstance().setRecordVideoOrientation(VideoOrientations.VideoOrientation_LandscapeRight);
		OPMediaEngine.getInstance().setFaceDetection(false);
		OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);
		
		setupMediaControlButton();
		setupAudioOutputButton();
	}

	private void setupMediaControlButton() {
		final Button mediaControlButton = (Button) findViewById(R.id.buttonMedia);
		final EditText remoteIPAddressEditText = (EditText) findViewById(R.id.remoteIPAddressEditText);
		mediaControlButton.setText("Start Video Capture");
		
		mediaControlButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				switch (mediaEngineStatus) {
				case 0:
					OPMediaEngine.getInstance().startVideoCapture();
					mediaControlButton.setText("Start Media Channel");
					mediaEngineStatus++;
					break;
				case 1:
					((OPTestMediaEngine) OPMediaEngine.getInstance()).setReceiverAddress(remoteIPAddressEditText.getText().toString());
					((OPTestMediaEngine) OPMediaEngine.getInstance()).startVoice();
					((OPTestMediaEngine) OPMediaEngine.getInstance()).startVideoChannel();
					mediaControlButton.setText("Stop Media Channel");
					mediaEngineStatus++;
					break;
				case 2:
					((OPTestMediaEngine) OPMediaEngine.getInstance()).stopVoice();
					((OPTestMediaEngine) OPMediaEngine.getInstance()).stopVideoChannel();
					mediaControlButton.setText("Stop Video Capture");
					mediaEngineStatus++;
					break;
				case 3:
					OPMediaEngine.getInstance().stopVideoCapture();
					mediaControlButton.setText("Start Video Capture");
					mediaEngineStatus = 0;
					break;
				default:
					break;
				}
			}
		});
	}

	private void setupAudioOutputButton() {
		final Button audioOutputButton = (Button) findViewById(R.id.buttonAudioOutput);
		audioOutputButton.setText("Speakerphone");
		
		audioOutputButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (speakerphoneEnabled) {
					OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
					audioOutputButton.setText("Speakerphone");
				} else {
					OPMediaEngine.getInstance().setLoudspeakerEnabled(true);
					audioOutputButton.setText("Ear Speaker");
				}
				speakerphoneEnabled = !speakerphoneEnabled;
			}
		});
	}
	
	static {
		try {
			System.loadLibrary("z_shared");
			System.loadLibrary("openpeer");
		} catch (UnsatisfiedLinkError use) {
			Log.e("JNI", "WARNING: Could not load libopenpeer.so");
		}
	}
}