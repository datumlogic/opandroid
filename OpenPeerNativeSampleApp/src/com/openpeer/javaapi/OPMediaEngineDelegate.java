package com.openpeer.javaapi;


public abstract class OPMediaEngineDelegate {

	public abstract void onMediaEngineAudioRouteChanged(OutputAudioRoutes audioRoute);
	public abstract void onMediaEngineFaceDetected();
	public abstract void onMediaEngineVideoCaptureRecordStopped();
}
