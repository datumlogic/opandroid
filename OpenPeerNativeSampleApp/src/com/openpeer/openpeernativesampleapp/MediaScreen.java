package com.openpeer.openpeernativesampleapp;

import java.util.ArrayList;
import java.util.List;

import org.webrtc.videoengine.ViERenderer;

import com.openpeer.openpeernativesampleapp.R;
import com.openpeer.javaapi.CameraTypes;
import com.openpeer.javaapi.OPCaptureCapability;
import com.openpeer.javaapi.OPLogger;
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
	CameraTypes cameraType = CameraTypes.CameraType_Front;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_screen);
		
		List<String> list = new ArrayList<String>();
		list.add("");
		
		EditText remoteIPAddressEditText = (EditText) findViewById(R.id.remoteIPAddressEditText);
		remoteIPAddressEditText.setText("127.0.0.1");
		
		OPLogger.installTelnetLogger(59999, 60, true);
		
		OPMediaEngine.init(getApplicationContext());
		OPMediaEngine.getInstance().setCameraType(cameraType);
		OPMediaEngine.getInstance().setEcEnabled(true);
		OPMediaEngine.getInstance().setAgcEnabled(true);
		OPMediaEngine.getInstance().setNsEnabled(false);
		OPMediaEngine.getInstance().setMuteEnabled(false);
		OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
		OPMediaEngine.getInstance().setContinuousVideoCapture(true);
		OPMediaEngine.getInstance().setDefaultVideoOrientation(VideoOrientations.VideoOrientation_Portrait);
		OPMediaEngine.getInstance().setRecordVideoOrientation(VideoOrientations.VideoOrientation_LandscapeRight);
		OPMediaEngine.getInstance().setFaceDetection(false);
		
		setupMediaControlButton();
		setupAudioOutputButton();
	}

	private void setupMediaControlButton() {
		final Button mediaControlButton = (Button) findViewById(R.id.buttonMedia);
		final EditText remoteIPAddressEditText = (EditText) findViewById(R.id.remoteIPAddressEditText);
		mediaControlButton.setText("Start Video Capture");
		final MediaScreen screen = this;
		
		mediaControlButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				LinearLayout localViewLinearLayout;
				LinearLayout remoteViewLinearLayout;
				switch (mediaEngineStatus) {
				case 0:
					myLocalSurface = ViERenderer.CreateRenderer(screen, true);
					localViewLinearLayout = (LinearLayout) findViewById(R.id.localViewLinearLayout);
					localViewLinearLayout.addView(myLocalSurface);
					List<OPCaptureCapability> capabilities = 
							OPMediaEngine.getInstance().getCaptureCapabilities(cameraType);
					for (OPCaptureCapability capability : capabilities) {
						String capabilityString = String.format("Capability - width: %d, height: %d, fps: %d", 
								capability.getWidth(), capability.getHeight(), capability.getMaxFPS());
						Log.d("JNI", capabilityString);
					}
					OPCaptureCapability captureCapability = new OPCaptureCapability();
					if (cameraType == CameraTypes.CameraType_Front) {
						captureCapability.setWidth(720);
						captureCapability.setHeight(480);
						captureCapability.setMaxFPS(15);
					} else {
						captureCapability.setWidth(720);
						captureCapability.setHeight(480);
						captureCapability.setMaxFPS(15);
					}
					OPMediaEngine.getInstance().setCaptureCapability(captureCapability, cameraType);
					OPMediaEngine.getInstance().setCaptureRenderView(myLocalSurface);
					OPMediaEngine.getInstance().startVideoCapture();
					mediaControlButton.setText("Start Media Channel");
					mediaEngineStatus++;
					break;
				case 1:
					myRemoteSurface = ViERenderer.CreateRenderer(screen, true);
					remoteViewLinearLayout = (LinearLayout) findViewById(R.id.remoteViewLinearLayout);
					remoteViewLinearLayout.addView(myRemoteSurface);
					OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);
					((OPTestMediaEngine) OPMediaEngine.getInstance()).setReceiverAddress(remoteIPAddressEditText.getText().toString());
					((OPTestMediaEngine) OPMediaEngine.getInstance()).startVoice();
					((OPTestMediaEngine) OPMediaEngine.getInstance()).startVideoChannel();
					mediaControlButton.setText("Stop Media Channel");
					mediaEngineStatus++;
					break;
				case 2:
					((OPTestMediaEngine) OPMediaEngine.getInstance()).stopVoice();
					((OPTestMediaEngine) OPMediaEngine.getInstance()).stopVideoChannel();
					remoteViewLinearLayout = (LinearLayout) findViewById(R.id.remoteViewLinearLayout);
					remoteViewLinearLayout.removeView(myRemoteSurface);
					myRemoteSurface = null;
					mediaControlButton.setText("Stop Video Capture");
					mediaEngineStatus++;
					break;
				case 3:
					OPMediaEngine.getInstance().stopVideoCapture();
					localViewLinearLayout = (LinearLayout) findViewById(R.id.localViewLinearLayout);
					localViewLinearLayout.removeView(myLocalSurface);
					myLocalSurface = null;
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