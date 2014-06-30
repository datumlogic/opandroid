package com.openpeer.sample.conversation;

import java.util.ArrayList;
import java.util.List;

import org.webrtc.videoengine.ViERenderer;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openpeer.app.OPChatWindow;
import com.openpeer.app.OPDataManager;
import com.openpeer.app.OPSession;
import com.openpeer.app.OPSessionManager;
import com.openpeer.app.OPUser;
import com.openpeer.datastore.DatabaseContracts.ContactsViewEntry;
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
import com.openpeer.sample.R;

public class CallFragment extends BaseFragment {
	TextView mStatusView;
	View mEndButton;
	private OPIdentityContact mPeerContact, mSelfContact;
	private OPSession mSession;

	OPCall mCall;
	private long mWindowId;
	private SurfaceView myLocalSurface;
	private SurfaceView myRemoteSurface;

	public static CallFragment newInstance(long[] peerContactId) {
		CallFragment fragment = new CallFragment();
		Bundle args = new Bundle();
		args.putLongArray(IntentData.ARG_PEER_USER_IDS, peerContactId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_call, null);

		setupView(view);
		mSession.placeCall(new OPCallDelegateImplementation(),
				true, false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();

		long[] userIDs = args.getLongArray(IntentData.ARG_PEER_USER_IDS);
		mWindowId = OPChatWindow.getWindowId(userIDs);
		mSession = OPSessionManager.getInstance().getSessionForUsers(userIDs);
		if (mSession == null) {
			// this is user intiiated session
			List<OPUser> users = new ArrayList<OPUser>();
			for (long userId : userIDs) {
				Cursor cursor = getActivity().getContentResolver().query(ContactsViewEntry.CONTENT_URI, null,
						ContactsViewEntry.COLUMN_NAME_USER_ID + "=" + userId, null, null);
				OPUser user = OPUser.fromDetailCursor(cursor);
				users.add(user);
			}
			mSession = new OPSession(users);
		}

	}

	private View setupView(View view) {
		mStatusView = (TextView) view.findViewById(R.id.text);
		mEndButton = view.findViewById(R.id.end);
		initMedia(false, view);
		return view;
	}

	public class OPCallDelegateImplementation extends OPCallDelegate {

		@Override
		public void onCallStateChanged(OPCall call, CallStates state) {
			Log.d("output", "onCallStateChanged " + state.toString() + " call "
					+ call);
			if (!isDetached()) {
				mStatusView.setText("" + state);
			}
		}
	}

	void initMedia(boolean useFrontCamera, View view) {
		myLocalSurface = ViERenderer.CreateLocalRenderer(getActivity());
		myRemoteSurface = ViERenderer.CreateRenderer(getActivity(), true);
		LinearLayout localViewLinearLayout = (LinearLayout) view.findViewById(R.id.localChatViewLinearLayout);
		LinearLayout remoteViewLinearLayout = (LinearLayout) view.findViewById(R.id.remoteChatViewLinearLayout);
		localViewLinearLayout.addView(myLocalSurface);
		remoteViewLinearLayout.addView(myRemoteSurface);

		if (useFrontCamera)
			OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Front);
		else
			OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Back);
		OPMediaEngine.getInstance().setEcEnabled(true);
		OPMediaEngine.getInstance().setAgcEnabled(true);
		OPMediaEngine.getInstance().setNsEnabled(false);
		OPMediaEngine.getInstance().setMuteEnabled(false);
		OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
		OPMediaEngine.getInstance().setContinuousVideoCapture(true);
		OPMediaEngine.getInstance().setDefaultVideoOrientation(VideoOrientations.VideoOrientation_Portrait);
		OPMediaEngine.getInstance().setRecordVideoOrientation(VideoOrientations.VideoOrientation_LandscapeRight);
		OPMediaEngine.getInstance().setFaceDetection(false);
		OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);

		OPMediaEngine.init(OPApplication.getInstance());
	}

}
