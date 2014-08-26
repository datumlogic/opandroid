package com.openpeer.javaapi;

import java.util.List;

import android.util.Log;


public class OPMediaEngine {
	
	private long nativeClassPointer;
	
	private long nativeDelegatePointer;
	
	protected static OPMediaEngine   _instance;

    protected OPMediaEngine()
    {

    }
    
	public static native void init(Object context);

    public static OPMediaEngine getInstance()
    {
        if (_instance == null)
        {
            _instance = OPMediaEngine.singleton();
        }
        return _instance;
    }

	protected static native OPMediaEngine singleton();

	public native void setDefaultVideoOrientation(VideoOrientations orientation);
	public native VideoOrientations getDefaultVideoOrientation();
	public native void setRecordVideoOrientation(VideoOrientations orientation);
	public native VideoOrientations getRecordVideoOrientation();
	public native void setVideoOrientation();

	public native void setCaptureRenderView(Object renderView);
	public native void setChannelRenderView(Object renderView);
	
	public native void setCaptureCapability(OPCaptureCapability capability, CameraTypes cameraType);
	public native List<OPCaptureCapability> getCaptureCapabilities(CameraTypes cameraType);

	public native void setEcEnabled(boolean enabled);
	public native void setAgcEnabled(boolean enabled);
	public native void setNsEnabled(boolean enabled);
	public native void setVoiceRecordFile(String fileName);
	public native String getVoiceRecordFile();

	public native void setMuteEnabled(boolean enabled);
	public native boolean getMuteEnabled();
	public native void setLoudspeakerEnabled(boolean enabled);
	public native boolean getLoudspeakerEnabled();
	public native OutputAudioRoutes getOutputAudioRoute();
    
	public native void setContinuousVideoCapture(boolean continuousVideoCapture);
	public native boolean getContinuousVideoCapture();
    
	public native void setFaceDetection(boolean faceDetection);
	public native boolean getFaceDetection();

	public native CameraTypes getCameraType();
	public native void setCameraType(CameraTypes type);
    
	public native void startVideoCapture();
	public native void stopVideoCapture();
    
	public native void startRecordVideoCapture(String fileName, boolean saveToLibrary);
	public native void stopRecordVideoCapture();

	public native int getVideoTransportStatistics(OPRtpRtcpStatistics stat);
	public native int getVoiceTransportStatistics(OPRtpRtcpStatistics stat);
	
    private native void releaseCoreObjects(); 
    
    protected void finalize() throws Throwable {
    	
    	if (nativeClassPointer != 0 || nativeDelegatePointer != 0)
    	{
    		Log.d("output", "Cleaning media engine core objects");
    		releaseCoreObjects();
    	}
    		
    	super.finalize();
    }
}
