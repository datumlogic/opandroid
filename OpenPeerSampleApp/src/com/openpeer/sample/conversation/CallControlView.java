package com.openpeer.sample.conversation;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openpeer.javaapi.CallClosedReasons;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.sample.OPSessionManager;
import com.openpeer.sample.R;
import com.openpeer.sample.util.DateFormatUtils;
import com.openpeer.sdk.datastore.DatabaseContracts.WindowViewEntry;
import com.squareup.picasso.Picasso;

public class CallControlView extends LinearLayout {
	private OPCall mCall;
	private ImageView mAnswerButton;
	private ImageView mEndButton;
	private View mPaddingView;

	CallActionListener mListener;

	public CallControlView(Context context) {
		this(context, null, 0);
	}

	public CallControlView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public CallControlView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.layout_call_control, this);
		mAnswerButton = (ImageView) findViewById(R.id.answer);
		mEndButton = (ImageView) findViewById(R.id.end);
		mPaddingView = findViewById(R.id.padview);
	}

	public void bindCall(OPCall call) {
		mCall = call;
		mEndButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OPSessionManager.getInstance().hangupCall(mCall, CallClosedReasons.CallClosedReason_User);
				if (mListener != null) {
					mListener.onEndClick();
				}
			}
		});
		if (mCall.getCaller().isSelf()) {
			removeView(mAnswerButton);
			removeView(mPaddingView);
		}
		updateView();
	}

	public void updateView() {
		switch (mCall.getState()) {
		case CallState_Incoming:
			mAnswerButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mCall.answer();
					removeView(mAnswerButton);
					removeView(mPaddingView);
					if (mListener != null) {
						mListener.onAnswerClick();
					}
				}
			});

			break;
		case CallState_Hold:
			break;
		case CallState_Closing:
		case CallState_Closed:
			break;
		case CallState_Open:
			break;
		default:
		}
	}

	public void registerActionListener(CallActionListener listener) {
		mListener = listener;
	}

	public interface CallActionListener {
		public void onAnswerClick();

		public void onEndClick();

	}

}
