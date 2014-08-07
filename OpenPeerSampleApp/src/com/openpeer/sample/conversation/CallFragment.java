package com.openpeer.sample.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.openpeer.javaapi.CallClosedReasons;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.CameraTypes;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.VideoOrientations;
import com.openpeer.sample.AppConfig;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.OPNotificationBuilder;
import com.openpeer.sample.OPSessionManager;
import com.openpeer.sample.R;
import com.openpeer.sample.util.SettingsHelper;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.model.OPUser;
import com.squareup.picasso.Picasso;

import org.webrtc.videoengine.ViERenderer;

import java.util.List;

public class CallFragment extends BaseFragment {
    public static final String TAG = CallFragment.class.getSimpleName();
    // TextView mNameView;
    ImageView mPeerAvatarView;

    OPCall mCall;
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
    private CallStatus mCallStatus;
    private View mStatusOverlay;
    private View mCallView;
    Ringtone mRingtone;

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
            mVideo = mCall.hasVideo();
        } else {

            mAudio = args.getBoolean(IntentData.ARG_AUDIO, true);
            mVideo = args.getBoolean(IntentData.ARG_VIDEO, true);
        }
        mVideo = mVideo && AppConfig.FEATURE_CALL;

    }

    private View setupView(View view) {
        mCallView = view.findViewById(R.id.status);
        mVideoView = view.findViewById(R.id.video);
        mStatusOverlay = view.findViewById(R.id.controls);

        mPeerAvatarView = (ImageView) view.findViewById(R.id.peer_image);

        // No point in hiding the overlay for audio call
        if (mVideo) {
            mStatusOverlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    hideOverlay();
                }
            });
        }

        CallControlView mCallControlView = (CallControlView) view.findViewById(R.id.call_control);

        if (mVideo) {
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showOverlay();
                }
            });
        } else {
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
            // if (mVideo) {
            // OPMediaEngine.getInstance().startVideoCapture();
            // }
        } else {
            if (mCall.getState() == CallStates.CallState_Incoming || mCall.getState() == CallStates.CallState_Ringing) {

                playRingtone();
            } else if (mCall.getState() == CallStates.CallState_Open) {
                OPNotificationBuilder.cancelNotificationForCall(mCall);
                startShowDuration();
                if (mVideo) {
                    mCallView.setVisibility(View.GONE);
                }
            }
        }
        mCallControlView.bindCall(mCall);
        // mNameView.setText(mCall.getPeerUser().getName());
        getActivity().getActionBar().setTitle(mCall.getPeerUser().getName());
        String avatarUri = mCall.getPeerUser().getAvatarUri();
        if (avatarUri != null) {
            Picasso.with(getActivity())
                    .load(avatarUri)
                    .into(mPeerAvatarView);
        }

        return view;
    }

    void setupMediaControl() {
        mCallStatus = OPSessionManager.getInstance().getMediaStateForCall(peerUri);
        audioButton.setImageResource(mCallStatus.isMuted() ? R.drawable.ic_action_mic_muted : R.drawable.ic_action_mic);
        audioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCallStatus.setMuted(!mCallStatus.isMuted());
                audioButton.setImageResource(mCallStatus.isMuted() ? R.drawable.ic_action_mic_muted : R.drawable.ic_action_mic);
                OPMediaEngine.getInstance().setMuteEnabled(mCallStatus.isMuted());
            }
        });

        speakerButton.setImageResource(mCallStatus.isSpeakerOn() ? R.drawable.ic_action_speaker_on : R.drawable.ic_action_speaker_off);

        speakerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCallStatus.setSpeakerOn(!mCallStatus.isSpeakerOn());
                speakerButton.setImageResource(mCallStatus.isSpeakerOn() ? R.drawable.ic_action_speaker_on
                        : R.drawable.ic_action_speaker_off);

                OPMediaEngine.getInstance().setLoudspeakerEnabled(mCallStatus.isSpeakerOn());

            }
        });
        if (mVideo) {
            videoButton.setImageResource(mCallStatus.isCapturing() ? R.drawable.ic_action_video : R.drawable.ic_action_video);
            videoButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCallStatus.setCapturing(!mCallStatus.isCapturing());
                    videoButton.setImageResource(mCallStatus.isCapturing() ? R.drawable.ic_action_video_on : R.drawable.ic_action_video_off);
                    if (mCallStatus.isCapturing()) {
                        OPMediaEngine.getInstance().startVideoCapture();
                    } else {
                        OPMediaEngine.getInstance().stopVideoCapture();

                    }
                }
            });
            cameraSwitchButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCallStatus.setUseFrontCamera(!mCallStatus.useFrontCamera());
                    if (mCallStatus.useFrontCamera()) {
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
//                remoteViewLinearLayout.addView(myRemoteSurface);
//                OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);
            }
        }
        getActivity().registerReceiver(receiver, new IntentFilter(IntentData.ACTION_CALL_STATE_CHANGE));

    }

    void updateCallView(CallStates state) {
        int strResId = STATE_STRINGS[state.ordinal()];
        if (strResId != 0) {
            getActivity().getActionBar().setSubtitle(getActivity().getText(strResId));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

    }

    public void onDestroy() {
        super.onDestroy();
        if (mCall != null) {
            CallStates state = mCall.getState();
            switch (state) {
                case CallState_Open:
                case CallState_Active:
                case CallState_Hold:
                    OPNotificationBuilder.showNotificationForCall(mCall);
                    break;

                case CallState_Closing:
                case CallState_Closed:
                    break;
                default:
                    mCall.hangup(CallClosedReasons.CallClosedReason_User);
                    OPSessionManager.getInstance().onCallEnd(mCall);
                    break;

            }
        }
        if (mVideo) {
            localViewLinearLayout.removeAllViews();
//            remoteViewLinearLayout.removeAllViews();
            OPMediaEngine.getInstance().setChannelRenderView(null);
//            OPMediaEngine.getInstance().setCaptureRenderView(null);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String callId = intent.getStringExtra(IntentData.ARG_CALL_ID);
            CallStates state = (CallStates) intent.getSerializableExtra(IntentData.ARG_CALL_STATE);
            if (callId.equals(mCall.getCallID())) {
                onCallStateChanged(mCall, state);
            }
        }
    };

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
                            // mNameView.setText("" + state);
                        }
                        break;
                    case CallState_Incoming:
                        playRingtone();
                        break;
                    case CallState_Early:
                        break;
                    case CallState_Ringing:
                        playRingtone();
                        break;
                    case CallState_Ringback:
                        playRingtone();
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
                mCallStatus.setAnswerTime(System.currentTimeMillis());
                startShowDuration();
                if (mVideo) {
                    mCallView.setVisibility(View.GONE);
                    hideOverlay();
                }
                if (mRingtone != null) {
                    mRingtone.stop();
                }
            }

            private void onCallClosed() {
                if (mRingtone != null) {
                    mRingtone.stop();
                }
                getActivity().finish();
            }
        });
    }


    int STATE_STRINGS[] = {0,// CallState_None, // call has no state yet
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
            R.string.CallState_Closed}; // call has ended};

    void initMedia(View view) {

        if (mCallStatus.useFrontCamera())
            OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Front);
        else
            OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Back);
        OPMediaEngine.getInstance().setEcEnabled(true);
        OPMediaEngine.getInstance().setAgcEnabled(true);
        OPMediaEngine.getInstance().setNsEnabled(false);
        OPMediaEngine.getInstance().setMuteEnabled(false);
        OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
        if (mVideo) {
//            myLocalSurface = ViERenderer.CreateLocalRenderer(getActivity());
            myLocalSurface=SurfaceViewFactory.getLocalView(getActivity().getApplicationContext());
            myRemoteSurface = ViERenderer.CreateRenderer(getActivity(),true);
            localViewLinearLayout = (LinearLayout) view.findViewById(R.id.localChatViewLinearLayout);
            remoteViewLinearLayout = (LinearLayout) view.findViewById(R.id.remoteChatViewLinearLayout);
            localViewLinearLayout.addView(myLocalSurface);
            remoteViewLinearLayout.addView(myRemoteSurface);
            OPMediaEngine.getInstance().setContinuousVideoCapture(true);
            OPMediaEngine.getInstance().setDefaultVideoOrientation(VideoOrientations.VideoOrientation_Portrait);
            OPMediaEngine.getInstance().setRecordVideoOrientation(VideoOrientations.VideoOrientation_LandscapeRight);
            OPMediaEngine.getInstance().setFaceDetection(false);
            OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);
//            OPMediaEngine.getInstance().setCaptureRenderView(myLocalSurface);
            // if (mCall != null) {
            // OPMediaEngine.getInstance().startVideoCapture();
            // }
        }
    }

    // private long startTime;
    // private TextView mStatusView;

    private void startShowDuration() {
        mCallView.postDelayed(timerThread, 1000);
    }

    private Runnable timerThread = new Runnable() {

        public void run() {
            if (!isAdded()) {
                return;
            }

            long timeInMilliseconds = mCallStatus.getDuration();

            int secs = (int) (timeInMilliseconds / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            getActivity().getActionBar().setSubtitle(mins + ":" + String.format("%02d", secs));
            mCallView.postDelayed(this, 1000);
        }
    };

    void playRingtone() {
        if (mRingtone == null) {
            // mRingtone = RingtoneManager.getRingtone(getActivity(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
            mRingtone = SettingsHelper.getRingtone();
            Log.d(TAG, "play ringtone " + mRingtone.getTitle(getActivity()));
        }
        mRingtone.play();
    }

    void showOverlay() {
        getActivity().getActionBar().show();
        mStatusOverlay.setVisibility(View.VISIBLE);
    }

    void hideOverlay() {
        getActivity().getActionBar().hide();
        mStatusOverlay.setVisibility(View.GONE);
    }

}
