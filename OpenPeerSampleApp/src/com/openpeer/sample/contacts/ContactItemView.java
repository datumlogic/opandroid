package com.openpeer.sample.contacts;

import static com.openpeer.datastore.DatabaseContracts.ContactsViewEntry.COLUMN_NAME_AVATAR_URL;
import static com.openpeer.datastore.DatabaseContracts.ContactsViewEntry.COLUMN_NAME_CONTACT_NAME;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openpeer.app.OPDataManager;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.sample.OPSessionManager;
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

	public void updateData(Cursor cursor) {
		// BaseColumns._ID, COLUMN_NAME_CONTACT_NAME, COLUMN_NAME_AVATAR_URL
		mTitleView.setText(cursor.getString(1));
		Picasso.with(getContext())
				.load(cursor.getString(2))
				.into(mImageView);
		final String stableId = cursor.getString(3);
		final long userId = cursor.getLong(4);
		if (!TextUtils.isEmpty(stableId)) {
			mInviteView.setVisibility(View.GONE);
			mCallView.setVisibility(View.VISIBLE);
			mChatView.setVisibility(View.VISIBLE);
			final long[] ids = { userId };

			mChatView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d("test", "Chat button tapped");
					ConversationActivity.launchForChat(getContext(), ids);
				}
			});

			mCallView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d("test", "Call button tapped");

					AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
					builder.setPositiveButton(R.string.audio, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							ConversationActivity.launchForCall(getContext(), ids, true, false);
						}
					}).setNeutralButton(R.string.video, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							ConversationActivity.launchForCall(getContext(), ids, true, true);
						}
					}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
					builder.create().show();
				}
			});
			// show unread messages badge if any
			int numberofUnreadMessages = OPDataManager.getDatastoreDelegate()
					.getNumberofUnreadMessages(stableId);

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
