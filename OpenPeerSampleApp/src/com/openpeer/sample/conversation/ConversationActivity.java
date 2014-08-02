package com.openpeer.sample.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.sample.BaseActivity;
import com.openpeer.sample.BaseFragmentActivity;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.MainActivity;
import com.openpeer.sample.OPSessionManager;
import com.openpeer.sample.R;
import com.openpeer.sdk.app.OPDataManager;

public class ConversationActivity extends BaseActivity {

    public static Intent getIntentForNotification(Context context,
                                                  OPConversationThread thread, String messageId, OPContact contact) {
        Intent intent = new Intent(context, ConversationActivity.class);
        // set intent data
        return intent;
    }

    public static void launchForChat(Context context, long[] peerContactId) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(IntentData.ARG_CONVERSATION_ACTION,
                IntentData.ACTION_CHAT);
        intent.putExtra(IntentData.ARG_PEER_USER_IDS, peerContactId);
        context.startActivity(intent);
    }

    public static void launchForChat(Context context, long peerContactId) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(IntentData.ARG_CONVERSATION_ACTION,
                IntentData.ACTION_CHAT);
        intent.putExtra(IntentData.ARG_PEER_CONTACT_ID, peerContactId);
        context.startActivity(intent);
    }

    public static void launchForCall(Context context, String peerUri) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(IntentData.ARG_CONVERSATION_ACTION, IntentData.ACTION_CALL);
        intent.putExtra(IntentData.ARG_PEER_URI, peerUri);

        context.startActivity(intent);
    }

    public static void launchForCall(Context context, long[] peerContactId, boolean audio, boolean video) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(IntentData.ARG_CONVERSATION_ACTION,
                IntentData.ACTION_CALL);
        intent.putExtra(IntentData.ARG_PEER_USER_IDS, peerContactId);
        intent.putExtra(IntentData.ARG_AUDIO, audio);
        intent.putExtra(IntentData.ARG_VIDEO, video);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String action = intent
                .getStringExtra(IntentData.ARG_CONVERSATION_ACTION);
        if (action.equals(IntentData.ACTION_CHAT)) {
            getActionBar().setTitle(R.string.label_chat);
            ChatFragment cFragment = ChatFragment.newInstance(intent
                    .getLongArrayExtra(IntentData.ARG_PEER_USER_IDS));
            setContentFragment(cFragment);
        } else if (action.equals(IntentData.ACTION_CALL)) {
            getActionBar().setTitle(R.string.hint_call);
            CallFragment cFragment = CallFragment.newInstance(intent
                            .getLongArrayExtra(IntentData.ARG_PEER_USER_IDS),
                    intent.getStringExtra(IntentData.ARG_PEER_URI),
                    intent.getBooleanExtra(IntentData.ARG_AUDIO, true),
                    intent.getBooleanExtra(IntentData.ARG_VIDEO, false)
            );
            setContentFragment(cFragment);
        }
        if (OPDataManager.getInstance().getSharedAccount() == null || !OPDataManager.getInstance().isAccountReady()) {
//            BaseActivity.showInvalidStateWarning(this);
            showLoginFragment();
            return;
        }
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
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(IntentData.ARG_CONVERSATION_ACTION, IntentData.ACTION_CALL);
        intent.putExtra(IntentData.ARG_PEER_URI, peerUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

}
