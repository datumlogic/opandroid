package com.openpeer.javaapi;

import android.util.Log;

public class OPMediaEngine {

	private long nativeClassPointer;

	private long nativeDelegatePointer;

	protected static OPMediaEngine _instance;

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

	/**
	 * @ExcludeFromJavadoc
	 * @param renderView
	 */
	public native void setCaptureRenderView(Object renderView);

	/**
	 * Set the surface view for remote video. This method must be called before call is placed.
	 * 
	 * @param renderView
	 */
	public native void setChannelRenderView(Object renderView);

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

	/**
	 * 
	 * @param continuousVideoCapture
	 *            True -- do not stop video capture after call ends false -- Automatically stop video capture after call ends
	 */
	public native void setContinuousVideoCapture(boolean continuousVideoCapture);

	public native boolean getContinuousVideoCapture();

	public native void setFaceDetection(boolean faceDetection);

	public native boolean getFaceDetection();

	public native CameraTypes getCameraType();

	public native void setCameraType(CameraTypes type);

	public native void startVideoCapture();

	/**
	 * Stop video capture. Cutrently calling this function during call has no effect
	 */
	public native void stopVideoCapture();

	/**
	 * @ExcludeFromJavadoc
	 * @param fileName
	 * @param saveToLibrary
	 */
	public native void startRecordVideoCapture(String fileName, boolean saveToLibrary);

	/**
	 * @ExcludeFromJavadoc
	 */
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
