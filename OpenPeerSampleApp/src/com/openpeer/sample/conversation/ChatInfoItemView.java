package com.openpeer.sample.conversation;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
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
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.sample.R;
import com.openpeer.sample.conversation.ConversationActivity;
import com.squareup.picasso.Picasso;

public class ChatInfoItemView extends RelativeLayout {
	private OPRolodexContact mContact;

	private ImageView mImageView;
	private TextView mBadgeView;
	private TextView mTitleView;
	private TextView mLastMessageView;

	public ChatInfoItemView(Context context) {
		this(context, null, 0);
	}

	public ChatInfoItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.item_chat_info, this);
		mImageView = (ImageView) findViewById(R.id.image_view);
		mBadgeView = (TextView) findViewById(R.id.badge_view);

		mTitleView = (TextView) findViewById(R.id.title);
		mTitleView = (TextView) findViewById(R.id.text_message);
	}

	public ChatInfoItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public void updateData(Cursor cursor) {
		mTitleView.setText(cursor.getString(1));
		mLastMessageView.setText(cursor.getString(2));
	}

	public void updateData(OPSession session) {
		// mContact = contact;
		// if (contact.getAvatars() != null && !contact.getAvatars().isEmpty())
		// {
		// Picasso.with(getContext())
		// .load(contact.getAvatars().get(0).getURL())
		// .into(mImageView);
		// }
		mTitleView.setText(formatParticipants(session.getParticipants()));
		OPMessage message = session.getLastMessage();
		if (message != null) {
			mLastMessageView.setText(message.getMessage());
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
