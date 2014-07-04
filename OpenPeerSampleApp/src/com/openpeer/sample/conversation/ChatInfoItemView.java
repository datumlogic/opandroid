package com.openpeer.sample.conversation;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openpeer.app.OPDataManager;
import com.openpeer.app.OPSession;
import com.openpeer.datastore.DatabaseContracts.WindowViewEntry;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.sample.R;
import com.openpeer.sample.conversation.ConversationActivity;
import com.openpeer.sample.util.DateFormatUtils;
import com.squareup.picasso.Picasso;

public class ChatInfoItemView extends RelativeLayout {
	private OPRolodexContact mContact;

	private ImageView mImageView;
	private TextView mBadgeView;
	private TextView mTitleView;
	private TextView mLastMessageView;
	private TextView mTimeView;

	public ChatInfoItemView(Context context) {
		this(context, null, 0);
	}

	public ChatInfoItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.item_chat_info, this);
		mImageView = (ImageView) findViewById(R.id.image_view);
		mBadgeView = (TextView) findViewById(R.id.badge_view);

		mTitleView = (TextView) findViewById(R.id.title);
		mLastMessageView = (TextView) findViewById(R.id.text_message);
		mTimeView = (TextView) findViewById(R.id.time_view);
	}

	public ChatInfoItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public void updateData(Cursor cursor) {
		mTitleView.setText(cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_PARTICIPANT_NAMES)));
		String msg = cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_LAST_MESSAGE));
		Long time = cursor.getLong(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_LAST_MESSAGE_TIME));
		final String idListString = cursor.getString(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_USER_ID));
		int unreadCount = cursor.getInt(cursor.getColumnIndex(WindowViewEntry.COLUMN_NAME_UNREAD_COUNT));
		Log.d("test", "ChatInfo user_id " + idListString);
		if (idListString != null) {
			String idStrings[] = idListString.split(",");
			final long IDs[] = new long[idStrings.length];
			for (int i = 0; i < IDs.length; i++) {
				IDs[i] = Long.parseLong(idStrings[i]);
			}
			this.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ConversationActivity.launchForChat(getContext(), IDs);
				}
			});
		}
		if (msg != null) {
			mLastMessageView.setText(msg);
			mTimeView.setText(DateFormatUtils.getSameDayTime(time));
		}
		if (unreadCount > 0) {
			mBadgeView.setVisibility(View.VISIBLE);
			mBadgeView.setText(""+unreadCount);
		} else {
			mBadgeView.setVisibility(View.GONE);

		}

	}

	public void onClick() {
		Toast.makeText(getContext(), "TODO: onclick show chat fragment",
				Toast.LENGTH_LONG);
	}

	public void onLongPress() {
		Toast.makeText(getContext(), "TODO: on longpress show something",
				Toast.LENGTH_LONG);
	}

	private String formatParticipants(List<OPIdentityContact> peers) {
		return "Bruce Xia,Bojan";
	}

}
