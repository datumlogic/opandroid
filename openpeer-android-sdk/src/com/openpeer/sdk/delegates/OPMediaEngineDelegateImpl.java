package com.openpeer.sdk.delegates;

import android.util.Log;

import com.openpeer.javaapi.OPMediaEngineDelegate;
import com.openpeer.javaapi.OutputAudioRoutes;

public class OPMediaEngineDelegateImpl extends OPMediaEngineDelegate {
	private static final String TAG = OPMediaEngineDelegateImpl.class.getSimpleName();

	@Override
	public void onMediaEngineAudioRouteChanged(OutputAudioRoutes audioRoute) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onMediaEngineAudioRouteChanged route = " + audioRoute.toString());
	}

	@Override
	public void onMediaEngineAudioSessionInterruptionBegan() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onMediaEngineAudioSessionInterruptionBegan");
	}

	@Override
	public void onMediaEngineAudioSessionInterruptionEnded() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onMediaEngineAudioSessionInterruptionEnded");
	}

	@Override
	public void onMediaEngineFaceDetected() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onMediaEngineFaceDetected");
	}

	@Override
	public void onMediaEngineVideoCaptureRecordStopped() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onMediaEngineVideoCaptureRecordStopped");
	}

}
