package com.openpeer.sample.conversation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.sample.OPSessionManager;
import com.openpeer.sample.R;

public class CallInfoView extends LinearLayout {
	private OPRolodexContact mContact;

	private ImageView mImageView;
	private TextView mTitleView;
	private TextView mTimeView;

	CallStatus mState;

	private OPCall mCall;
	OPCallDelegate mDelegate;

	public CallInfoView(Context context) {
		this(context, null, 0);
	}

	public CallInfoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.layout_call_info, this);
		mImageView = (ImageView) findViewById(R.id.image_view);

		mTitleView = (TextView) findViewById(R.id.title);
		mTimeView = (TextView) findViewById(R.id.duration);
	}

	public CallInfoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public void bindCall(OPCall call) {
		mCall = call;
		mState = OPSessionManager.getInstance().getMediaStateForCall(call.getPeer().getPeerURI());
		mDelegate = new OPCallDelegate() {

			@Override
			public void onCallStateChanged(OPCall call, final CallStates state) {
				if (!call.getCallID().equals(mCall.getCallID())) {
					return;
				}

				switch (state) {
				case CallState_Closing:
				case CallState_Closed:
					post(new Runnable() {
						public void run() {
							setVisibility(View.GONE);
						}
					});
					CallbackHandler.getInstance().unregisterCallDelegate(call, this);
					break;
				default:
					break;
				}

			}

		};
		CallbackHandler.getInstance().registerCallDelegate(mCall, mDelegate);
		this.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				CallActivity.launchForCall(getContext(), mCall.getPeer().getPeerURI());
			}
		});
		startShowDuration();
	}

	public void unbind() {
		CallbackHandler.getInstance().unregisterCallDelegate(mCall, mDelegate);
		mCall = null;
	}

	private void startShowDuration() {
		mTimeView.postDelayed(timerThread, 1000);
	}

	private Runnable timerThread = new Runnable() {

		public void run() {
			if (!isShown()) {
				return;
			}

			long timeInMilliseconds = mState.getDuration();

			int secs = (int) (timeInMilliseconds / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			mTimeView.setText(mins + ":" + String.format("%02d", secs));
			mTimeView.postDelayed(this, 1000);
		}
	};

}
