package com.openpeer.openpeernativesampleapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.webrtc.videoengine.ViERenderer;

import com.openpeer.openpeernativesampleapp.R;
import com.openpeer.javaapi.CameraTypes;
import com.openpeer.javaapi.OPCaptureCapability;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.VideoOrientations;
import com.openpeer.javaapi.test.OPTestMediaEngine;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MediaScreen extends Activity {

	SurfaceView localSurface = null;
	SurfaceView remoteSurface = null;
	int mediaEngineStatus = 0;
	boolean speakerphoneEnabled = false;
	boolean videoSwitched = false;
	CameraTypes cameraType = CameraTypes.CameraType_Front;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_screen);
		
		@SuppressWarnings("unused")
		List<String> list = new ArrayList<String>();
		
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
		setupSwitchSurfaceButton();
		setupButton01();
	}

	private void setupMediaControlButton() {
		final Button mediaControlButton = (Button) findViewById(R.id.buttonMedia);
		final EditText remoteIPAddressEditText = (EditText) findViewById(R.id.remoteIPAddressEditText);
		mediaControlButton.setText("Start Video Capture");
		final MediaScreen screen = this;
		
		mediaControlButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				LinearLayout leftViewLinearLayout;
				LinearLayout rightViewLinearLayout;
				switch (mediaEngineStatus) {
				case 0:
					OPMediaEngine.getInstance().setCaptureRenderViewCropping(0.0F, 0.5F, 1.0F, 1.0F);
					localSurface = ViERenderer.CreateRenderer(screen, true);
					leftViewLinearLayout = (LinearLayout) findViewById(R.id.leftViewLinearLayout);
					leftViewLinearLayout.addView(localSurface);
					List<OPCaptureCapability> capabilities = 
							OPMediaEngine.getInstance().getCaptureCapabilities(cameraType);
					for (OPCaptureCapability capability : capabilities) {
						String capabilityString = String.format(Locale.US, "Capability - width: %d, height: %d, fps: %d", 
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
					OPMediaEngine.getInstance().setCaptureRenderView(localSurface);
					OPMediaEngine.getInstance().startVideoCapture();
					mediaControlButton.setText("Start Media Channel");
					mediaEngineStatus++;
					break;
				case 1:
					OPMediaEngine.getInstance().setChannelRenderViewCropping(0.0F, 0.0F, 1.0F, 1.0F);
					remoteSurface = ViERenderer.CreateRenderer(screen, true);
					rightViewLinearLayout = (LinearLayout) findViewById(R.id.rightViewLinearLayout);
					rightViewLinearLayout.addView(remoteSurface);
					OPMediaEngine.getInstance().setChannelRenderView(remoteSurface);
					((OPTestMediaEngine) OPMediaEngine.getInstance()).setReceiverAddress(remoteIPAddressEditText.getText().toString());
					((OPTestMediaEngine) OPMediaEngine.getInstance()).startVoice();
					((OPTestMediaEngine) OPMediaEngine.getInstance()).startVideoChannel();
					mediaControlButton.setText("Stop Media Channel");
					mediaEngineStatus++;
					break;
				case 2:
					((OPTestMediaEngine) OPMediaEngine.getInstance()).stopVoice();
					((OPTestMediaEngine) OPMediaEngine.getInstance()).stopVideoChannel();
					rightViewLinearLayout = (LinearLayout) findViewById(R.id.rightViewLinearLayout);
					rightViewLinearLayout.removeView(remoteSurface);
					remoteSurface = null;
					mediaControlButton.setText("Stop Video Capture");
					mediaEngineStatus++;
					break;
				case 3:
					OPMediaEngine.getInstance().stopVideoCapture();
					leftViewLinearLayout = (LinearLayout) findViewById(R.id.leftViewLinearLayout);
					leftViewLinearLayout.removeView(localSurface);
					localSurface = null;
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

	private void setupSwitchSurfaceButton() {
		final Button switchSurfaceButton = (Button) findViewById(R.id.buttonSwitchSurface);
		final MediaScreen screen = this;
		
		switchSurfaceButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				LinearLayout leftViewLinearLayout = (LinearLayout) findViewById(R.id.leftViewLinearLayout);
				LinearLayout rightViewLinearLayout = (LinearLayout) findViewById(R.id.rightViewLinearLayout);
				
				leftViewLinearLayout.removeAllViews();
				rightViewLinearLayout.removeAllViews();

				localSurface = ViERenderer.CreateRenderer(screen, true);
				remoteSurface = ViERenderer.CreateRenderer(screen, true);

				if (videoSwitched) {
					leftViewLinearLayout.addView(localSurface);
					rightViewLinearLayout.addView(remoteSurface);
				} else {
					leftViewLinearLayout.addView(remoteSurface);
					rightViewLinearLayout.addView(localSurface);
				}
				videoSwitched = !videoSwitched;

				OPMediaEngine.getInstance().setCaptureRenderView(localSurface);
				OPMediaEngine.getInstance().setChannelRenderView(remoteSurface);
			}
		});
	}

	private void setupButton01() {
		final Button button01 = (Button) findViewById(R.id.button01);
		
		button01.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
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