package com.openpeer.javaapi;


public class OPMediaEngine {

	public static native OPMediaEngine singleton();

	public native void setDefaultVideoOrientation(VideoOrientations orientation);
	
	public native VideoOrientations getDefaultVideoOrientation();
	
	public native void setRecordVideoOrientation(VideoOrientations orientation);
	
	public native VideoOrientations getRecordVideoOrientation();
	
	public native void setVideoOrientation();

	public native void setCaptureRenderView(Object renderView);
	
	public native void setChannelRenderView(Object renderView);

	public native void setEcEnabled(Boolean enabled);
	
	public native void setAgcEnabled(Boolean enabled);
	
	public native void setNsEnabled(Boolean enabled);
	
	public native void setVoiceRecordFile(String fileName);
	
	public native String getVoiceRecordFile();

	public native void setMuteEnabled(Boolean enabled);
	
	public native Boolean getMuteEnabled();
	
	public native void setLoudspeakerEnabled(Boolean enabled);
	
	public native Boolean getLoudspeakerEnabled();
	
	public native OutputAudioRoutes getOutputAudioRoute();
    
	public native void setContinuousVideoCapture(Boolean continuousVideoCapture);
	
	public native Boolean getContinuousVideoCapture();
    
	public native void setFaceDetection(Boolean faceDetection);
	
	public native Boolean getFaceDetection();

	public native CameraTypes getCameraType();
	
	public native void setCameraType(CameraTypes type);
    
	public native void startVideoCapture();
	
	public native void stopVideoCapture();
    
	public native void startRecordVideoCapture(String fileName, Boolean saveToLibrary);
	
	public native void stopRecordVideoCapture();

	public native int getVideoTransportStatistics(OPRtpRtcpStatistics stat);
	
	public native int getVoiceTransportStatistics(OPRtpRtcpStatistics stat);
}
