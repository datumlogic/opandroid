package com.openpeer.sample.contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.sample.R;
import com.openpeer.sample.conversation.ConversationActivity;
import com.squareup.picasso.Picasso;

public class ContactItemView extends RelativeLayout {
	private OPRolodexContact mContact;

	private ImageView mImageView;
	private TextView mTitleView;

	private View mCallView;
	private View mChatView;
	private View mInviteView;

	public ContactItemView(Context context) {
		this(context, null, 0);
	}

	public ContactItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.item_contact, this);
		mImageView = (ImageView) findViewById(R.id.image_view);
		mTitleView = (TextView) findViewById(R.id.title);
		mCallView = findViewById(R.id.call);
		mChatView = findViewById(R.id.chat);
		mInviteView = findViewById(R.id.invite);
	}

	public ContactItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public void updateData(OPRolodexContact contact) {
		mContact = contact;
		if (contact.getAvatars() != null && !contact.getAvatars().isEmpty()) {
			Picasso.with(getContext())
					.load(contact.getAvatars().get(0).getURL())
					.into(mImageView);
		}
		mTitleView.setText(contact.getName());
		if (contact instanceof OPIdentityContact) {
			mInviteView.setVisibility(View.GONE);
			mCallView.setVisibility(View.VISIBLE);
			mChatView.setVisibility(View.VISIBLE);

			mChatView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d("test", "Chat button tapped");
					ConversationActivity.launchForChat(getContext(),
							((OPIdentityContact) mContact).getStableID());
				}
			});
		} else {
			mInviteView.setVisibility(View.VISIBLE);

			mCallView.setVisibility(View.GONE);
			mChatView.setVisibility(View.GONE);
		}
	}

	public void onClick() {
		Log.d("TODO", "item clicked!");
	}

	public void onLongPress() {

	}

}
