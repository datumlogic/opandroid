package com.openpeer.sample.conversation;

public class CallStatus {
	long answerTime;
	boolean Muted;
	boolean Capturing = true;
	boolean SpeakerOn;
	private boolean mFrontCamera = true;

	public void setAnswerTime(long time) {
		answerTime = time;
	}

	public long getDuration() {
		return System.currentTimeMillis() - answerTime;
	}

	public boolean isMuted() {
		return Muted;
	}

	public void setMuted(boolean muted) {
		Muted = muted;
	}

	public boolean isCapturing() {
		return Capturing;
	}

	public void setCapturing(boolean capturing) {
		Capturing = capturing;
	}

	public boolean isSpeakerOn() {
		return SpeakerOn;
	}

	public void setSpeakerOn(boolean speakerOn) {
		SpeakerOn = speakerOn;
	}

	public boolean useFrontCamera() {
		// TODO Auto-generated method stub
		return mFrontCamera;

	}

	public void setUseFrontCamera(boolean useFront) {
		mFrontCamera = useFront;
	}

}
