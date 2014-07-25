package com.openpeer.sample.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.sample.BaseActivity;
import com.openpeer.sample.BaseFragmentActivity;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.OPNotificationBuilder;
import com.openpeer.sample.OPSessionManager;
import com.openpeer.sample.R;
import com.openpeer.sdk.app.OPDataManager;

public class CallActivity extends BaseActivity {

	private static final String TAG = CallActivity.class.getSimpleName();

	public static Intent getIntentForNotification(Context context,
			OPConversationThread thread, String messageId, OPContact contact) {
		Intent intent = new Intent(context, CallActivity.class);
		// set intent data
		return intent;
	}

	public static void launchForCall(Context context, String peerUri) {
		Intent intent = new Intent(context, CallActivity.class);
		intent.putExtra(IntentData.ARG_CONVERSATION_ACTION, IntentData.ACTION_CALL);
		intent.putExtra(IntentData.ARG_PEER_URI, peerUri);

		context.startActivity(intent);
	}

	public static void launchForCall(Context context, long[] peerContactId, boolean audio, boolean video) {
		Intent intent = new Intent(context, CallActivity.class);
		intent.putExtra(IntentData.ARG_CONVERSATION_ACTION,
				IntentData.ACTION_CALL);
		intent.putExtra(IntentData.ARG_PEER_USER_IDS, peerContactId);
		intent.putExtra(IntentData.ARG_AUDIO, audio);
		intent.putExtra(IntentData.ARG_VIDEO, video);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if(!OPDataManager.getInstance().isAccountReady()){
            BaseActivity.showInvalidStateWarning(this);
            finish();
            return;
        }
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		setContentView(R.layout.activity_container);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		String action = intent
				.getStringExtra(IntentData.ARG_CONVERSATION_ACTION);

		getActionBar().setTitle(R.string.hint_call);
		CallFragment cFragment = CallFragment.newInstance(intent
				.getLongArrayExtra(IntentData.ARG_PEER_USER_IDS),
				intent.getStringExtra(IntentData.ARG_PEER_URI),
				intent.getBooleanExtra(IntentData.ARG_AUDIO, true),
				intent.getBooleanExtra(IntentData.ARG_VIDEO, false));
		setContentFragment(cFragment);

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");

	}

	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static void launchForIncomingCall(Context context, String peerUri) {
		Intent intent = new Intent(context, CallActivity.class);
		intent.putExtra(IntentData.ARG_CONVERSATION_ACTION, IntentData.ACTION_CALL);
		intent.putExtra(IntentData.ARG_PEER_URI, peerUri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {

	}

}
