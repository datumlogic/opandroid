/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
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
        intent.putExtra(IntentData.ARG_CONVERSATION_ACTION,
                IntentData.ACTION_CALL);
        intent.putExtra(IntentData.ARG_PEER_URI, peerUri);

        context.startActivity(intent);
    }

    public static void launchForCall(Context context, long[] peerContactId,
            String contextId, boolean audio, boolean video) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(IntentData.ARG_CONVERSATION_ACTION,
                IntentData.ACTION_CALL);
        intent.putExtra(IntentData.ARG_PEER_USER_IDS, peerContactId);
        intent.putExtra(IntentData.ARG_CONTEXT_ID, contextId);
        intent.putExtra(IntentData.ARG_AUDIO, audio);
        intent.putExtra(IntentData.ARG_VIDEO, video);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!OPDataManager.getInstance().isAccountReady()) {
            BaseActivity.showInvalidStateWarning(this);
            finish();
            return;
        }
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setContentView(R.layout.activity_container);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        getActionBar().setTitle(R.string.hint_call);
        CallFragment cFragment = CallFragment.newInstance(intent
                .getLongArrayExtra(IntentData.ARG_PEER_USER_IDS),
                intent.getStringExtra(IntentData.ARG_PEER_URI),
                intent.getStringExtra(IntentData.ARG_CONTEXT_ID),
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
        intent.putExtra(IntentData.ARG_CONVERSATION_ACTION,
                IntentData.ACTION_CALL);
        intent.putExtra(IntentData.ARG_PEER_URI, peerUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

}
