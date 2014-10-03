/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.sample.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.CameraTypes;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.VideoOrientations;
import com.openpeer.sample.AppConfig;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.OPNotificationBuilder;
import com.openpeer.sample.OPSessionManager;
import com.openpeer.sample.R;
import com.openpeer.sample.util.CallUtil;
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
    private SurfaceView mLocalSurface;
    private SurfaceView mRemoteSurface;
    private RelativeLayout mVideoView;
    private boolean mAudio, mVideo;
    private long[] userIDs;
    private String peerUri;
    private String mContextId;
    FrameLayout previewLayout;
    private FrameLayout remoteView;
    private ImageView audioButton;
    private ImageView videoButton;
    private ImageView cameraSwitchButton;
    private ImageView speakerButton;
    private ImageView recordButton;
    private CallStatus mCallStatus;
    private View mStatusOverlay;
    private View mCallView;
    Ringtone mRingtone;

    public static CallFragment newInstance(long[] peerContactId, boolean audio,
            boolean video) {
        CallFragment fragment = new CallFragment();
        Bundle args = new Bundle();
        args.putLongArray(IntentData.ARG_PEER_USER_IDS, peerContactId);
        args.putBoolean(IntentData.ARG_AUDIO, audio);
        args.putBoolean(IntentData.ARG_VIDEO, video);

        fragment.setArguments(args);
        return fragment;
    }

    public static CallFragment newInstance(long[] peerContactId,
            String peerUri, String contextId, boolean audio, boolean video) {
        CallFragment fragment = new CallFragment();
        Bundle args = new Bundle();
        args.putLongArray(IntentData.ARG_PEER_USER_IDS, peerContactId);
        args.putString(IntentData.ARG_CONTEXT_ID, contextId);

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
        // obtainCameraRatios();
        peerUri = args.getString(IntentData.ARG_PEER_URI);
        userIDs = args.getLongArray(IntentData.ARG_PEER_USER_IDS);
        mContextId = args.getString(IntentData.ARG_CONTEXT_ID);

        if (peerUri != null) {
            mCall = OPSessionManager.getInstance().getOngoingCallForPeer(
                    peerUri);
        } else if (userIDs != null) {
            List<OPUser> users = OPDataManager.getDatastoreDelegate().getUsers(
                    userIDs);
            if (users != null && users.size() > 0) {
                peerUri = users.get(0).getPeerUri();
                mCall = OPSessionManager.getInstance().getOngoingCallForPeer(
                        peerUri);
            }
        } else {
            Log.e(TAG, "no peerUri nor userIDs");
        }
        if (mCall != null) {
            mVideo = mCall.hasVideo();
        } else {

            mAudio = args.getBoolean(IntentData.ARG_AUDIO, true);
            mVideo = args.getBoolean(IntentData.ARG_VIDEO, true);
        }
        mVideo = mVideo && AppConfig.FEATURE_CALL;
        getActivity().registerReceiver(receiver,
                new IntentFilter(IntentData.ACTION_CALL_STATE_CHANGE));

    }

    private View setupView(View view) {
        mCallView = view.findViewById(R.id.status);
        mVideoView = (RelativeLayout) view.findViewById(R.id.video);
        previewLayout = (FrameLayout) mVideoView
                .findViewById(R.id.localVideoView);
        remoteView = (FrameLayout) mVideoView
                .findViewById(R.id.remoteVideoView);
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

        CallControlView mCallControlView = (CallControlView) view
                .findViewById(R.id.call_control);

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

        ViewStub mediaControlViewStub = (ViewStub) mStatusOverlay
                .findViewById(R.id.media_control);
        if (mVideo) {
            mediaControlViewStub
                    .setLayoutResource(R.layout.layout_media_control_video);
            View layout = mediaControlViewStub.inflate();// getActivity(), R.layout.layout_media_control_video, null);
            audioButton = (ImageView) layout.findViewById(R.id.audio);
            speakerButton = (ImageView) layout.findViewById(R.id.speaker);

            videoButton = (ImageView) layout.findViewById(R.id.video);
            cameraSwitchButton = (ImageView) layout.findViewById(R.id.camera);

        } else {
            mediaControlViewStub
                    .setLayoutResource(R.layout.layout_media_control_audio);

            View layout = mediaControlViewStub.inflate();// getActivity(), R.layout.layout_media_control_audio, null);
            audioButton = (ImageView) layout.findViewById(R.id.audio);
            speakerButton = (ImageView) layout.findViewById(R.id.speaker);
            recordButton = (ImageView) layout.findViewById(R.id.record);
        }
        initMedia(view);
        if (mCall == null) {

            mCall = OPSessionManager.getInstance().placeCall(userIDs, mAudio,
                    mVideo, mContextId);

        } else {
            if (mCall.getState() == CallStates.CallState_Incoming
                    || mCall.getState() == CallStates.CallState_Ringing) {

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
        String peerName = mCall.getPeerUser().getName();
        getActivity().getActionBar().setTitle(peerName);
        String avatarUri = mCall.getPeerUser().getAvatarUri();
        if (avatarUri != null) {
            Picasso.with(getActivity())
                    .load(avatarUri)
                    .into(mPeerAvatarView);
        }

        return view;
    }

    void setupMediaControl() {
        audioButton
                .setImageResource(mCallStatus.isMuted() ? R.drawable.ic_action_mic_muted
                        : R.drawable.ic_action_mic);
        OPMediaEngine.getInstance().setMuteEnabled(mCallStatus.isMuted());

        audioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCallStatus.setMuted(!mCallStatus.isMuted());
                audioButton.setImageResource(mCallStatus.isMuted() ? R.drawable.ic_action_mic_muted
                        : R.drawable.ic_action_mic);
                OPMediaEngine.getInstance().setMuteEnabled(
                        mCallStatus.isMuted());
            }
        });

        speakerButton
                .setImageResource(mCallStatus.isSpeakerOn() ? R.drawable.ic_action_speaker_on
                        : R.drawable.ic_action_speaker_off);

        OPMediaEngine.getInstance().setLoudspeakerEnabled(
                mCallStatus.isSpeakerOn());
        speakerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCallStatus.setSpeakerOn(!mCallStatus.isSpeakerOn());
                speakerButton.setImageResource(mCallStatus.isSpeakerOn() ? R.drawable.ic_action_speaker_on
                        : R.drawable.ic_action_speaker_off);

                OPMediaEngine.getInstance().setLoudspeakerEnabled(
                        mCallStatus.isSpeakerOn());

            }
        });
        if (mVideo) {
            videoButton
                    .setImageResource(mCallStatus.isCapturing() ? R.drawable.ic_action_video_on
                            : R.drawable.ic_action_video_off);
            videoButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCallStatus.setCapturing(!mCallStatus.isCapturing());
                    videoButton.setImageResource(mCallStatus.isCapturing() ? R.drawable.ic_action_video_on
                            : R.drawable.ic_action_video_off);
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
                        OPMediaEngine.getInstance().setCameraType(
                                CameraTypes.CameraType_Front);
                    } else {
                        OPMediaEngine.getInstance().setCameraType(
                                CameraTypes.CameraType_Back);
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
        }

    }

    void updateCallView(CallStates state) {
        String callStateString = CallUtil.getCallStateStringResId(state);
        if (null != callStateString) {
            getActivity().getActionBar().setSubtitle(callStateString);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCall != null) {
            CallStates state = mCall.getState();
            switch (state) {
            case CallState_Closing:
            case CallState_Closed:
                break;
            default:
                OPNotificationBuilder.showNotificationForCall(mCall);
                stopRingtone();
                break;

            }
        }
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

    }

    public void onDestroy() {
        super.onDestroy();

        if (mVideo) {
            mVideoView.removeAllViews();
            previewLayout.removeAllViews();
            OPMediaEngine.getInstance().setChannelRenderView(null);
        }
        getActivity().unregisterReceiver(receiver);

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
            CallStates state = (CallStates) intent
                    .getSerializableExtra(IntentData.ARG_CALL_STATE);
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
        });
    }

    boolean mVideoPreviewSwitched;

    void initMedia(View view) {
        mCallStatus = OPSessionManager.getInstance().getMediaStateForCall(
                peerUri);

        OPMediaEngine.getInstance().setEcEnabled(true);
        OPMediaEngine.getInstance().setAgcEnabled(true);
        OPMediaEngine.getInstance().setNsEnabled(false);
        OPMediaEngine.getInstance().setMuteEnabled(false);
        OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
        if (mVideo) {
            if (mCallStatus.useFrontCamera())
                OPMediaEngine.getInstance().setCameraType(
                        CameraTypes.CameraType_Front);
            else
                OPMediaEngine.getInstance().setCameraType(
                        CameraTypes.CameraType_Back);
            mLocalSurface = SurfaceViewFactory.getLocalView(getActivity()
                    .getApplicationContext());
            mRemoteSurface = ViERenderer.CreateRenderer(getActivity(), true);
            previewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mVideoPreviewSwitched = !mVideoPreviewSwitched;
                    setupVideoPreview();
                }
            });
            setupVideoPreview();
            // This makes sure the video capture is stopped after call is stopped.
            OPMediaEngine.getInstance().setContinuousVideoCapture(false);
            OPMediaEngine.getInstance().setDefaultVideoOrientation(
                    VideoOrientations.VideoOrientation_Portrait);
            OPMediaEngine.getInstance().setRecordVideoOrientation(
                    VideoOrientations.VideoOrientation_LandscapeRight);
            OPMediaEngine.getInstance().setFaceDetection(false);
            OPMediaEngine.getInstance().setChannelRenderView(mRemoteSurface);
        }
        setupMediaControl();

    }

    private static final double ASPECT_RATIO = 16.0 / 9.0;

    private void setupVideoPreview() {
        RelativeLayout.LayoutParams remotevideoLayoutParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int widthInPixel = getActivity().getResources().getDimensionPixelSize(
                R.dimen.width_local_video);
        int heightInPixel = (int) (widthInPixel * ASPECT_RATIO);// * CameraUtil.getCameraAspectRatio());
        FrameLayout.LayoutParams localvideoLayoutParam = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        // localvideoLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        if (previewLayout.getChildCount() > 0) {
            previewLayout.removeAllViews();
            remoteView.removeAllViews();
        }

        if (mVideoPreviewSwitched) {
            remoteView.addView(mLocalSurface, remotevideoLayoutParam);
            previewLayout.addView(mRemoteSurface, localvideoLayoutParam);
            mRemoteSurface.setZOrderMediaOverlay(true);
            mLocalSurface.setZOrderMediaOverlay(false);
            OPMediaEngine.getInstance().setChannelRenderView(mRemoteSurface);

        } else {
            previewLayout.addView(mLocalSurface, localvideoLayoutParam);
            remoteView.addView(mRemoteSurface, remotevideoLayoutParam);
            OPMediaEngine.getInstance().setChannelRenderView(mRemoteSurface);
            mRemoteSurface.setZOrderMediaOverlay(false);
            mLocalSurface.setZOrderMediaOverlay(true);
        }
    }

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
            getActivity().getActionBar().setSubtitle(
                    mins + ":" + String.format("%02d", secs));
            mCallView.postDelayed(this, 1000);
        }
    };

    private void onCallAnswered() {
        mCallStatus.setAnswerTime(System.currentTimeMillis());
        startShowDuration();
        if (mVideo) {
            mCallView.setVisibility(View.GONE);
            hideOverlay();
        }
        stopRingtone();
    }

    private void onCallClosed() {
        stopRingtone();
        getActivity().finish();
    }

    void playRingtone() {
        if (mRingtone == null) {
            // mRingtone = RingtoneManager.getRingtone(getActivity(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
            mRingtone = SettingsHelper.getRingtone();
            Log.d(TAG, "play ringtone " + mRingtone.getTitle(getActivity()));
        }
        mRingtone.play();
    }

    void stopRingtone() {
        if (mRingtone != null) {
            mRingtone.stop();
        }
        mRingtone = null;
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
