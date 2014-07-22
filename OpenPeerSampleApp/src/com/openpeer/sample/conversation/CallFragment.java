package com.openpeer.sample.conversation;

import java.util.List;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.CameraTypes;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.VideoOrientations;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.OPNotificationBuilder;
import com.openpeer.sample.OPSessionManager;
import com.openpeer.sample.R;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.model.OPUser;
import com.squareup.picasso.Picasso;

public class CallFragment extends BaseFragment {
	public static final String TAG = CallFragment.class.getSimpleName();
	TextView mNameView;
	ImageView mPeerAvatarView;

	OPCall mCall;
	OPCallDelegateImplementation mDelegate;
	private SurfaceView myLocalSurface;
	private SurfaceView myRemoteSurface;
	private View mVideoView;
	private boolean mAudio, mVideo;
	private long[] userIDs;
	private String peerUri;
	LinearLayout localViewLinearLayout;
	private LinearLayout remoteViewLinearLayout;
	private ImageView audioButton;
	private ImageView videoButton;
	private ImageView cameraSwitchButton;
	private ImageView speakerButton;
	private ImageView recordButton;
	private CallStatus state;
	private View mStatusOverlay;
	private View mCallView;

	public static CallFragment newInstance(long[] peerContactId, boolean audio, boolean video) {
		CallFragment fragment = new CallFragment();
		Bundle args = new Bundle();
		args.putLongArray(IntentData.ARG_PEER_USER_IDS, peerContactId);
		args.putBoolean(IntentData.ARG_AUDIO, audio);
		args.putBoolean(IntentData.ARG_VIDEO, video);

		fragment.setArguments(args);
		return fragment;
	}

	public static CallFragment newInstance(long[] peerContactId, String peerUri, boolean audio, boolean video) {
		CallFragment fragment = new CallFragment();
		Bundle args = new Bundle();
		args.putLongArray(IntentData.ARG_PEER_USER_IDS, peerContactId);

		args.putString(IntentData.ARG_PEER_URI, peerUri);
		args.putBoolean(IntentData.ARG_AUDIO, audio);
		args.putBoolean(IntentData.ARG_VIDEO, video);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_call, null);

		setupView(view);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();

		peerUri = args.getString(IntentData.ARG_PEER_URI);
		userIDs = args.getLongArray(IntentData.ARG_PEER_USER_IDS);

		mDelegate = new OPCallDelegateImplementation();
		Log.d("test", "CallFragment received peerUri" + peerUri);
		if (peerUri != null) {
			mCall = OPSessionManager.getInstance().getOngoingCallForPeer(peerUri);
		} else if (userIDs != null) {
			List<OPUser> users = OPDataManager.getDatastoreDelegate().getUsers(userIDs);
			if (users != null && users.size() > 0) {
				peerUri = users.get(0).getPeerUri();
				mCall = OPSessionManager.getInstance().getOngoingCallForPeer(peerUri);
			}
		}
		if (mCall != null) {
			// long userIDs[] = args.getLongArray(IntentData.ARG_PEER_USER_IDS);
			//
			// mAudio = args.getBoolean(IntentData.ARG_AUDIO, true);
			// mVideo = args.getBoolean(IntentData.ARG_VIDEO, true);
			//
			// mCall = OPSessionManager.getInstance().placeCall(userIDs, mAudio, mVideo);
			// }
			CallbackHandler.getInstance().registerCallDelegate(mCall, mDelegate);
			mVideo = mCall.hasVideo();
		} else {

			mAudio = args.getBoolean(IntentData.ARG_AUDIO, true);
			mVideo = args.getBoolean(IntentData.ARG_VIDEO, true);
		}

	}

	private View setupView(View view) {
		mCallView = view.findViewById(R.id.status);
		mVideoView = view.findViewById(R.id.video);
		mStatusOverlay = view.findViewById(R.id.controls);

		mPeerAvatarView = (ImageView) view.findViewById(R.id.peer_image);

		mStatusOverlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
			}
		});
		mNameView = (TextView) view.findViewById(R.id.name);
		mStatusView = (TextView) view.findViewById(R.id.time);

		CallControlView mCallControlView = (CallControlView) view.findViewById(R.id.call_control);
		// mCallControlView.registerActionListener(new CallControlView.CallActionListener() {
		//
		// @Override
		// public void onAnswerClick() {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onEndClick() {
		// // TODO: we should call parent activity onCallEndListener if we want to use this fragment in multipane layout
		// getActivity().finish();
		// }
		// });
		Bundle args = getArguments();
		// mVideo = args.getBoolean(IntentData.ARG_VIDEO, false);
		if (mVideo) {
			mVideoView.setVisibility(View.VISIBLE);
			mVideoView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mStatusOverlay.setVisibility(View.VISIBLE);
				}
			});
		} else {
			mCallView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mStatusOverlay.setVisibility(View.VISIBLE);
				}
			});
			mVideoView.setVisibility(View.GONE);
		}

		ViewStub mediaControlViewStub = (ViewStub) mStatusOverlay.findViewById(R.id.media_control);
		if (mVideo) {
			mediaControlViewStub.setLayoutResource(R.layout.layout_media_control_video);
			View layout = mediaControlViewStub.inflate();// getActivity(), R.layout.layout_media_control_video, null);
			audioButton = (ImageView) layout.findViewById(R.id.audio);
			speakerButton = (ImageView) layout.findViewById(R.id.speaker);

			videoButton = (ImageView) layout.findViewById(R.id.video);
			cameraSwitchButton = (ImageView) layout.findViewById(R.id.camera);

		} else {
			mediaControlViewStub.setLayoutResource(R.layout.layout_media_control_audio);

			View layout = mediaControlViewStub.inflate();// getActivity(), R.layout.layout_media_control_audio, null);
			audioButton = (ImageView) layout.findViewById(R.id.audio);
			speakerButton = (ImageView) layout.findViewById(R.id.speaker);
			recordButton = (ImageView) layout.findViewById(R.id.record);
		}
		setupMediaControl();

		initMedia(view);
		if (mCall == null) {

			mCall = OPSessionManager.getInstance().placeCall(userIDs, mAudio, mVideo);
			CallbackHandler.getInstance().registerCallDelegate(mCall, mDelegate);
			// if (mVideo) {
			// OPMediaEngine.getInstance().startVideoCapture();
			// }
		}
		mCallControlView.bindCall(mCall);
		mNameView.setText(mCall.getPeerUser().getName());
		Picasso.with(getActivity())
				.load(mCall.getPeerUser().getAvatarUri())
				.into(mPeerAvatarView);

		return view;
	}

	void setupMediaControl() {
		state = OPSessionManager.getInstance().getMediaStateForCall(peerUri);
		audioButton.setImageResource(state.isMuted() ? R.drawable.ic_action_mic_muted : R.drawable.ic_action_mic);
		audioButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				state.setMuted(!state.isMuted());
				audioButton.setImageResource(state.isMuted() ? R.drawable.ic_action_mic_muted : R.drawable.ic_action_mic);
				OPMediaEngine.getInstance().setMuteEnabled(state.isMuted());
			}
		});

		speakerButton.setImageResource(state.isSpeakerOn() ? R.drawable.ic_action_speaker : R.drawable.ic_action_speaker_off);

		speakerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				state.setSpeakerOn(!state.isSpeakerOn());
				audioButton.setImageResource(state.isSpeakerOn() ? R.drawable.ic_action_speaker : R.drawable.ic_action_speaker_off);

				OPMediaEngine.getInstance().setLoudspeakerEnabled(state.isSpeakerOn());

			}
		});
		if (mVideo) {
			videoButton.setImageResource(state.isCapturing() ? R.drawable.ic_action_video : R.drawable.ic_action_video);
			videoButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					state.setCapturing(!state.isCapturing());
					videoButton.setImageResource(state.isCapturing() ? R.drawable.ic_action_video : R.drawable.ic_action_video);
					if (state.isCapturing()) {
						OPMediaEngine.getInstance().startVideoCapture();
					} else {
						OPMediaEngine.getInstance().stopVideoCapture();

					}
				}
			});
			cameraSwitchButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					state.setUseFrontCamera(!state.useFrontCamera());
					if (state.useFrontCamera()) {
						OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Front);
					} else {
						OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Back);
					}

				}
			});
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mCall != null) {
			updateCallView(mCall.getState());
			if (mVideo && localViewLinearLayout.getChildCount() == 0) {
				localViewLinearLayout.addView(myLocalSurface);
				remoteViewLinearLayout.addView(myRemoteSurface);
				OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);
			}
		}
	}

	void updateCallView(CallStates state) {
		int strResId = STATE_STRINGS[state.ordinal()];
		if (strResId != 0) {
			mStatusView.setText(getActivity().getText(strResId));
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mCall != null && mCall.getState() != CallStates.CallState_Closing && mCall.getState() != CallStates.CallState_Closed) {
			OPNotificationBuilder.showNotificationForCall(mCall);
		}
		if (mVideo) {
			localViewLinearLayout.removeAllViews();
			remoteViewLinearLayout.removeAllViews();
		}

	}

	public class OPCallDelegateImplementation extends OPCallDelegate {

		@Override
		public void onCallStateChanged(OPCall call, final CallStates state) {
			Log.d(TAG, "onCallStateChanged " + state.toString() + " call "
					+ call);
			if (getActivity() == null || isDetached()) {
				return;
			}
			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					updateCallView(state);
					switch (state) {
					case CallState_Placed:
						if (!isDetached()) {
							mNameView.setText("" + state);
						}
						break;
					case CallState_Incoming:
						break;
					case CallState_Early:
						break;
					case CallState_Ringing:
						break;
					case CallState_Ringback:
						break;
					case CallState_Open: // call is open
						onCallAnswered();
						break;
					case CallState_Active:
						break;
					case CallState_Inactive:
						break;
					case CallState_Hold:
						break;
					case CallState_Closing: // call is hanging up
						break;
					case CallState_Closed:
						onCallClosed();
						break;
					}
				}

				private void onCallAnswered() {

					startTime = SystemClock.uptimeMillis();
					mStatusView.postDelayed(timerThread, 1000);
					if (mVideo) {
						mCallView.setVisibility(View.GONE);
						mStatusOverlay.setVisibility(View.VISIBLE);
					}
				}

				private void onCallClosed() {
					// OPSessionManager.getInstance().onCallEnd(mCall);
					CallbackHandler.getInstance().unregisterCallDelegate(mCall, mDelegate);
					getActivity().finish();
				}
			});
		}
	}

	int STATE_STRINGS[] = { 0,// CallState_None, // call has no state yet
			0,// CallState_Preparing, // call is negotiating in the background - do not present this call to a user yet...
			R.string.CallState_Incoming, // call is incoming from a remote party
			R.string.CallState_Placed, // call has been placed to the remote party
			0,// CallState_Early, // call is outgoing to a remote party and is receiving early media (media before being answered)
			R.string.CallState_Ringing,// CallState_Ringing, // call is incoming from a remote party and is ringing
			R.string.CallState_Ringback, // call is outgoing to a remote party and remote party is ringing
			0,// R.string.CallState_Open, // call is open
			0, R.string.CallState_Active, // call is open, and participant is actively communicating
			R.string.CallState_Inactive, // call is open, and participant is inactive
			R.string.CallState_Hold, // call is open but on hold
			R.string.CallState_Closing, // call is hanging up
			R.string.CallState_Closed }; // call has ended};

	void initMedia(View view) {

		if (state.useFrontCamera())
			OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Front);
		else
			OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Back);
		OPMediaEngine.getInstance().setEcEnabled(true);
		OPMediaEngine.getInstance().setAgcEnabled(true);
		OPMediaEngine.getInstance().setNsEnabled(false);
		OPMediaEngine.getInstance().setMuteEnabled(false);
		OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
		if (mVideo) {
			myLocalSurface = SurfaceViewFactory.getLocalView(getActivity().getApplicationContext());
			myRemoteSurface = SurfaceViewFactory.getRemoteSurfaceView(getActivity().getApplicationContext(), peerUri);
			localViewLinearLayout = (LinearLayout) view.findViewById(R.id.localChatViewLinearLayout);
			remoteViewLinearLayout = (LinearLayout) view.findViewById(R.id.remoteChatViewLinearLayout);

			OPMediaEngine.getInstance().setContinuousVideoCapture(true);
			OPMediaEngine.getInstance().setDefaultVideoOrientation(VideoOrientations.VideoOrientation_Portrait);
			OPMediaEngine.getInstance().setRecordVideoOrientation(VideoOrientations.VideoOrientation_LandscapeRight);
			OPMediaEngine.getInstance().setFaceDetection(false);
			OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);
			OPMediaEngine.getInstance().setCaptureRenderView(myLocalSurface);
			// if (mCall != null) {
			// OPMediaEngine.getInstance().startVideoCapture();
			// }
		}
	}

	private long startTime;
	private TextView mStatusView;
	private Runnable timerThread = new Runnable() {

		public void run() {

			long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			int secs = (int) (timeInMilliseconds / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			mStatusView.setText("" + mins + ":"
					+ String.format("%02d", secs));
			mStatusView.postDelayed(this, 0);
		}
	};

}
