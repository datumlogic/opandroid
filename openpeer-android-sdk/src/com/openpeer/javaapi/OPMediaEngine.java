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
