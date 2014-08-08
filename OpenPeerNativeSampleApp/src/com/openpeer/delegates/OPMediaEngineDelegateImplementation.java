package com.openpeer.delegates;

import android.util.Log;

import com.openpeer.javaapi.OPMediaEngineDelegate;
import com.openpeer.javaapi.OutputAudioRoutes;

public class OPMediaEngineDelegateImplementation extends OPMediaEngineDelegate {

	@Override
	public void onMediaEngineAudioRouteChanged(OutputAudioRoutes audioRoute) {
		// TODO Auto-generated method stub
		Log.i("output", "onMediaEngineAudioRouteChanged route = " + audioRoute.toString());
	}

	@Override
	public void onMediaEngineAudioSessionInterruptionBegan() {
		// TODO Auto-generated method stub
		Log.i("output", "onMediaEngineAudioSessionInterruptionBegan");
	}

	@Override
	public void onMediaEngineAudioSessionInterruptionEnded() {
		// TODO Auto-generated method stub
		Log.i("output", "onMediaEngineAudioSessionInterruptionEnded");
	}

	@Override
	public void onMediaEngineFaceDetected() {
		// TODO Auto-generated method stub
		Log.i("output", "onMediaEngineFaceDetected");
	}

	@Override
	public void onMediaEngineVideoCaptureRecordStopped() {
		// TODO Auto-generated method stub
		Log.i("output", "onMediaEngineVideoCaptureRecordStopped");
	}

}
