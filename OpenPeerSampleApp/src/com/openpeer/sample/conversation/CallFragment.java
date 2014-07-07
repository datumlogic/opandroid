package com.openpeer.sample.conversation;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.webrtc.videoengine.ViERenderer;

import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openpeer.app.OPChatWindow;
import com.openpeer.app.OPDataManager;
import com.openpeer.app.OPSession;
import com.openpeer.app.OPUser;
import com.openpeer.datastore.DatabaseContracts.ContactsViewEntry;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.CallClosedReasons;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.CameraTypes;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.VideoOrientations;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.OPApplication;
import com.openpeer.sample.OPNotificationBuilder;
import com.openpeer.sample.OPSessionManager;
import com.openpeer.sample.R;
import com.squareup.picasso.Picasso;

public class CallFragment extends BaseFragment {
	public static final String TAG = CallFragment.class.getSimpleName();
	TextView mStatusView;
	ImageView mPeerAvatarView;
	Button mEndButton, mAnswerButton;
	private OPIdentityContact mPeerContact, mSelfContact;
	private OPSession mSession;

	OPCall mCall;
	OPCallDelegateImplementation mDelegate;
	private long mWindowId;
	private SurfaceView myLocalSurface;
	private SurfaceView myRemoteSurface;
	private View mVideoView;
	private boolean mAudio, mVideo;
	private long[] userIDs;

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

		String peerUri = args.getString(IntentData.ARG_PEER_URI);
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
			//			long userIDs[] = args.getLongArray(IntentData.ARG_PEER_USER_IDS);
			//
			//			mAudio = args.getBoolean(IntentData.ARG_AUDIO, true);
			//			mVideo = args.getBoolean(IntentData.ARG_VIDEO, true);
			//
			//			mCall = OPSessionManager.getInstance().placeCall(userIDs, mAudio, mVideo);
			//		}
			CallbackHandler.getInstance().registerCallDelegate(mCall, mDelegate);
			mVideo = mCall.hasVideo();
		} else {

			mAudio = args.getBoolean(IntentData.ARG_AUDIO, true);
			mVideo = args.getBoolean(IntentData.ARG_VIDEO, true);
		}

	}

	private View setupView(View view) {
		mPeerAvatarView = (ImageView) view.findViewById(R.id.peer_image);
		mStatusView = (TextView) view.findViewById(R.id.text);
		mTimingView = (TextView) view.findViewById(R.id.time);
		mVideoView = view.findViewById(R.id.video);
		mAnswerButton = (Button) view.findViewById(R.id.answer);
		mEndButton = (Button) view.findViewById(R.id.end);
		mAnswerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (mCall.getState()) {
				case CallState_Incoming:
					mCall.answer();

					break;
				case CallState_Hold:
					break;
				case CallState_Closing:
				case CallState_Closed:
					break;
				default:

				}
			}
		});

		mEndButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (mCall.getState()) {

				case CallState_Closing:
				case CallState_Closed:
					break;
				default:
					OPSessionManager.getInstance().hangupCall(mCall, CallClosedReasons.CallClosedReason_User);
					CallbackHandler.getInstance().unregisterCallDelegate(mCall, mDelegate);
				}

				if (mCall.getState() == CallStates.CallState_Incoming) {
					mCall.answer();
				} else {
					mCall.hangup(CallClosedReasons.CallClosedReason_User);
					OPSessionManager.getInstance().onCallEnd(mCall);

				}
			}
		});
		Bundle args = getArguments();
		//		mVideo = args.getBoolean(IntentData.ARG_VIDEO, false);
		if (mVideo) {
			mVideoView.setVisibility(View.VISIBLE);
		}
		initMedia(true, view);
		if (mCall == null) {

			mCall = OPSessionManager.getInstance().placeCall(userIDs, mAudio, mVideo);
			CallbackHandler.getInstance().registerCallDelegate(mCall, mDelegate);
		}
		Picasso.with(getActivity())
				.load(mCall.getPeerUser().getAvatarUri())
				.into(mPeerAvatarView);

		//		mCall = mSession.placeCall(new OPCallDelegateImplementation(),
		//				args.getBoolean(IntentData.ARG_AUDIO, true), mVideo);
		return view;
	}

	//	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mCall != null) {
			updateCallView(mCall.getState());
		}
	}

	void updateCallView(CallStates state) {
		setStateText(state);
		switch (state) {
		case CallState_Incoming:
			mAnswerButton.setText(R.string.label_answer);
			mEndButton.setText(R.string.label_decline);

			break;
		case CallState_Hold:
			mAnswerButton.setText(R.string.label_unhold);
			mEndButton.setText(R.string.label_hangup);
			break;
		case CallState_Closing:
		case CallState_Closed:
			break;
		default:
			mAnswerButton.setText(R.string.hint_call);
			//			mAnswerButton.setEnabled(false);

			mEndButton.setText(R.string.label_hangup);

		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mCall != null && mCall.getState() != CallStates.CallState_Closing && mCall.getState() != CallStates.CallState_Closed) {
			OPNotificationBuilder.showNotificationForCall(mCall);
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
							mStatusView.setText("" + state);
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
					mTimingView.postDelayed(timerThread, 1000);
				}

				private void onCallClosed() {
					OPSessionManager.getInstance().onCallEnd(mCall);
					CallbackHandler.getInstance().unregisterCallDelegate(mCall, mDelegate);
				}
			});
		}
	}

	void setStateText(CallStates state) {
		int strResId = STATE_STRINGS[state.ordinal()];
		if (strResId != 0) {
			mStatusView.setText(getActivity().getText(strResId));
		}
	}

	int STATE_STRINGS[] = { 0,//CallState_None,       // call has no state yet
			0,//CallState_Preparing,  // call is negotiating in the background - do not present this call to a user yet...
			R.string.CallState_Incoming, // call is incoming from a remote party
			R.string.CallState_Placed, // call has been placed to the remote party
			0,//CallState_Early,      // call is outgoing to a remote party and is receiving early media (media before being answered)
			R.string.CallState_Ringing,//CallState_Ringing,    // call is incoming from a remote party and is ringing
			R.string.CallState_Ringback, // call is outgoing to a remote party and remote party is ringing
			R.string.CallState_Open, // call is open
			R.string.CallState_Active, // call is open, and participant is actively communicating
			R.string.CallState_Inactive, // call is open, and participant is inactive
			R.string.CallState_Hold, // call is open but on hold
			R.string.CallState_Closing, // call is hanging up
			R.string.CallState_Closed }; // call has ended}; 

	void initMedia(boolean useFrontCamera, View view) {

		if (useFrontCamera)
			OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Front);
		else
			OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Back);
		OPMediaEngine.getInstance().setEcEnabled(true);
		OPMediaEngine.getInstance().setAgcEnabled(true);
		OPMediaEngine.getInstance().setNsEnabled(false);
		OPMediaEngine.getInstance().setMuteEnabled(false);
		OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
		if (mVideo) {
			myLocalSurface = ViERenderer.CreateLocalRenderer(getActivity());
			myRemoteSurface = ViERenderer.CreateRenderer(getActivity(), true);
			LinearLayout localViewLinearLayout = (LinearLayout) view.findViewById(R.id.localChatViewLinearLayout);
			LinearLayout remoteViewLinearLayout = (LinearLayout) view.findViewById(R.id.remoteChatViewLinearLayout);
			localViewLinearLayout.addView(myLocalSurface);
			remoteViewLinearLayout.addView(myRemoteSurface);

			OPMediaEngine.getInstance().setContinuousVideoCapture(true);
			OPMediaEngine.getInstance().setDefaultVideoOrientation(VideoOrientations.VideoOrientation_Portrait);
			OPMediaEngine.getInstance().setRecordVideoOrientation(VideoOrientations.VideoOrientation_LandscapeRight);
			OPMediaEngine.getInstance().setFaceDetection(false);
			OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);
			OPMediaEngine.getInstance().setCaptureRenderView(myLocalSurface);
		}

		//		OPMediaEngine.init(OPApplication.getInstance());
	}

	private long startTime;
	private TextView mTimingView;
	private Runnable timerThread = new Runnable() {

		public void run() {

			long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

			int secs = (int) (timeInMilliseconds / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (timeInMilliseconds % 1000);
			mTimingView.setText("" + mins + ":"
					+ String.format("%02d", secs) + ":"
					+ String.format("%03d", milliseconds));
			mTimingView.postDelayed(this, 0);
		}
	};

}
