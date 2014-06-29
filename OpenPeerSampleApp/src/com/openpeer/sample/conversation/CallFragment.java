package com.openpeer.sample.conversation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openpeer.app.OPDataManager;
import com.openpeer.app.OPSessionManager;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.R;

public class CallFragment extends BaseFragment {
	TextView mStatusView;
	View mEndButton;
	private OPIdentityContact mPeerContact, mSelfContact;
	OPCall mCall;

	public static CallFragment newInstance(String peerContactId) {
		CallFragment fragment = new CallFragment();
		Bundle args = new Bundle();
		args.putString(IntentData.ARG_PEER_CONTACT_ID, peerContactId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_call, null);

		setupView(view);
//		OPSessionManager
//				.getInstance()
//				.getSessionForContact(mPeerContact)
//				.placeCall(mPeerContact, new OPCallDelegateImplementation(),
//						true, false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		mPeerContact = OPDataManager.getDatastoreDelegate().getIdentityContact(
				args.getString(IntentData.ARG_PEER_CONTACT_ID));

	}

	private View setupView(View view) {
		mStatusView = (TextView) view.findViewById(R.id.text);
		mEndButton = view.findViewById(R.id.end);
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

}
