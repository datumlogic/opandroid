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
package com.openpeer.sample;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.util.Log;

import com.openpeer.javaapi.CallClosedReasons;
import com.openpeer.javaapi.CallStates;
import com.openpeer.javaapi.ComposingStates;
import com.openpeer.javaapi.ContactConnectionStates;
import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPCallDelegate;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sample.conversation.CallActivity;
import com.openpeer.sample.conversation.CallStatus;
import com.openpeer.sample.push.OPPushManager;
import com.openpeer.sample.push.PushResult;
import com.openpeer.sample.push.PushToken;
import com.openpeer.sample.push.UAPushProviderImpl;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPSdkConfig;
import com.openpeer.sdk.model.OPConversation;
import com.openpeer.sdk.model.OPUser;

public class OPSessionManager {
    static final String TAG = OPSessionManager.class.getSimpleName();
    List<OPConversation> mSessions;

    Hashtable<String, OPCall> mCalls;

    private OPConversationThreadDelegate mThreadDelegate;
    private OPCallDelegate mCallDelegate;
    private static OPSessionManager instance;

    public static OPSessionManager getInstance() {
        if (instance == null) {
            instance = new OPSessionManager();
            instance.mSessions = new ArrayList<OPConversation>();
        }
        return instance;
    }

    public OPConversation addSession(OPConversation session) {
        mSessions.add(session);
        return session;
    }

    /**
     * Look up existing session for thread. Uses thread id to look up
     * 
     * @param thread
     * @return
     */
    public OPConversation getSessionOfThread(OPConversationThread thread) {
        for (OPConversation session : mSessions) {
            // TODO: use windowId to search when specified
            if (session.isForThread(thread)) {
                Log.d("test",
                        "found session for thread " + thread.getThreadID()
                                + " sessions " + mSessions.size());
                return session;
            }
        }
        // No existing session for the thread
        OPConversation session = new OPConversation(thread);
        session.save();
        addSession(session);
        return session;
    }

    /**
     * Look up the session "for" users. This call use calculated window id to find the session.
     * 
     * @param users
     * @return
     */
    public OPConversation getSessionForUsers(List<OPUser> users) {
        for (OPConversation session : mSessions) {
            if (session.isForUsers(users)) {
                return session;
            }
        }
        OPConversation session = new OPConversation(users);
        session.save();
        addSession(session);

        return session;
    }

    public OPConversation getSessionOfContext(List<OPUser> users,
            String contextId) {
        for (OPConversation session : mSessions) {
            if (session.getContextId().equals(contextId)) {
                return session;
            }
        }
        OPConversation session = new OPConversation(users, contextId);
        session.save();
        addSession(session);
        return session;
    }

    /**
     * Find existing session "including" the users
     * 
     * @param ids
     *            user ids
     * @return
     */
    private OPConversation getSessionWithUsers(List<OPUser> ids,
            String contextId) {
        switch (OPSdkConfig.getInstance().getGroupChatMode()) {
        case ContactsBased:
            return getSessionForUsers(ids);
        case ContextBased:
            return getSessionOfContext(ids, contextId);
        default:
            return null;
        }
    }

    public OPCall placeCall(long[] userIDs, boolean audio, boolean video,
            String contextId) {
        // long windowId = OPChatWindow.getWindowId(userIDs);

        List<OPUser> users = OPDataManager.getDatastoreDelegate().getUsers(
                userIDs);
        OPConversation session = OPSessionManager.getInstance()
                .getSessionWithUsers(users, contextId);
        if (session == null) {
            // this is user intiiated session
            session = new OPConversation(users);
            addSession(session);
        }

        OPCall call = session.placeCall(users.get(0), audio, video);
        mCalls.put(call.getPeer().getPeerURI(), call);
        return call;
    }

    void init() {
        mCalls = new Hashtable<String, OPCall>();
        mThreadDelegate = new OPConversationThreadDelegate() {

            @Override
            public void onConversationThreadNew(
                    OPConversationThread conversationThread) {
            }

            @Override
            public void onConversationThreadContactsChanged(
                    OPConversationThread conversationThread) {
                getSessionOfThread(conversationThread).onContactsChanged(
                        conversationThread);
            }

            @Override
            public void onConversationThreadContactStatusChanged(
                    OPConversationThread conversationThread, OPContact contact) {
                if (contact.isSelf()) {
                    Log.e(TAG,
                            "weird! onConversationThreadContactStatusChanged for self "
                                    + contact.getPeerURI());
                }
                OPLogger.debug(
                        OPLogLevel.LogLevel_Trace,
                        "onConversationThreadContactStateChanged  "
                                + contact.getPeerURI());
                ComposingStates state = conversationThread
                        .getContactComposingStatus(contact);
                if (state != null) {
                    OPConversation session = getSessionOfThread(conversationThread);
                    session.onContactComposingStateChanged(state, contact);
                }
            }

            @Override
            public void onConversationThreadMessage(
                    OPConversationThread conversationThread, String messageID) {
                OPMessage message = conversationThread
                        .getMessageById(messageID);

                if (message.getFrom().isSelf()) {
                    // Log.e("test", "Weird! received message from myself!" + message.getMessageId() + " messageId " + messageID + " type "
                    // + message.getMessageType());
                    OPLogger.debug(
                            OPLogLevel.LogLevel_Basic,
                            "Weird! received message from myself!"
                                    + message.getMessageId()
                                    + " messageId "
                                    + messageID + " type "
                                    + message.getMessageType());

                    return;
                }
                OPConversation session = getSessionOfThread(conversationThread);
                session.onMessageReceived(conversationThread, message);
                if (BaseActivity.isAppInBackground()) {
                    OPNotificationBuilder.showNotificationForMessage(session,
                            message);
                }
            }

            @Override
            public void onConversationThreadMessageDeliveryStateChanged(
                    OPConversationThread conversationThread, String messageID,
                    MessageDeliveryStates state) {
                OPLogger.debug(OPLogLevel.LogLevel_Detail,
                        "onConversationThreadMessageDeliveryStateChanged  " + messageID + " state "
                                + state);
                OPDataManager.getDatastoreDelegate().updateMessageDeliveryStatus(messageID, state);
            }

            @Override
            public void onConversationThreadPushMessage(
                    OPConversationThread conversationThread,
                    final String messageID,
                    OPContact contact) {
                final OPMessage message = conversationThread
                        .getMessageById(messageID);

                OPPushManager.getInstance().getDeviceToken(
                        contact.getPeerURI(), new Callback<PushToken>() {

                            @Override
                            public void success(PushToken token,
                                    Response response) {
                                OPLogger.debug(OPLogLevel.LogLevel_Detail,
                                        "onConversationThreadPushMessage push message "
                                                + message);
                                new UAPushProviderImpl().pushMessage(message,
                                        token, new Callback<PushResult>() {
                                            @Override
                                            public void success(
                                                    PushResult pushResult,
                                                    Response response) {
                                                OPDataManager
                                                        .getDatastoreDelegate()
                                                        .updateMessageDeliveryStatus(
                                                                messageID,
                                                                MessageDeliveryStates.MessageDeliveryState_Sent);

                                            }

                                            @Override
                                            public void failure(
                                                    RetrofitError error) {

                                                if (error != null) {
                                                    OPLogger.debug(
                                                            OPLogLevel.LogLevel_Basic,
                                                            "eror pushing message "
                                                                    + error.getMessage());
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                if (error != null) {
                                    OPLogger.debug(OPLogLevel.LogLevel_Basic,
                                            "eror retrieving device token "
                                                    + error.getMessage());
                                }
                            }
                        });
            }

            @Override
            public void onConversationThreadContactConnectionStateChanged(
                    OPConversationThread conversationThread, OPContact contact,
                    ContactConnectionStates state) {
                Log.d(TAG, "onConversationThreadContactConnectionStateChanged "
                        + state);
            }
        };
        mCallDelegate = new OPCallDelegate() {

            @Override
            public void onCallStateChanged(OPCall call, CallStates state) {
                OPConversationThread thread = call.getConversationThread();
                getSessionOfThread(thread).onCallStateChanged(call, state);
                Intent intent = new Intent();
                intent.setAction(IntentData.ACTION_CALL_STATE_CHANGE);
                intent.putExtra(IntentData.ARG_CALL_STATE, state);
                intent.putExtra(IntentData.ARG_CALL_ID, call.getCallID());

                OPApplication.getInstance().sendBroadcast(intent);
                switch (state) {
                case CallState_Incoming:
                    OPContact caller = call.getCaller();
                    OPCall currentCall = getOngoingCallForPeer(caller
                            .getPeerURI());
                    if (currentCall != null) {
                        // TODO: auto answer and swap calls
                        Log.d("test", "found existing call.");
                        // return;
                    }

                    OPUser user = getPeerUserForCall(call);
                    Log.d("test", "found user for incoming call " + user);

                    mCalls.put(caller.getPeerURI(), call);
                    CallActivity.launchForIncomingCall(
                            OPApplication.getInstance(), caller.getPeerURI());
                    break;
                case CallState_Closing:
                case CallState_Closed:
                    onCallEnd(call);
                    break;

                }
            }
        };

    }

    public OPConversationThreadDelegate getConversationThreadDelegate() {
        return mThreadDelegate;
    }

    public OPCallDelegate getCallDelegate() {
        return mCallDelegate;
    }

    public OPCall getOngoingCallForPeer(String peerUri) {
        return mCalls.get(peerUri);
    }

    public void onCallEnd(OPCall mCall) {
        String peerUri = mCall.getPeer().getPeerURI();
        mCalls.remove(peerUri);
        mCallStates.remove(peerUri);
        OPNotificationBuilder.cancelNotificationForCall(mCall);
        if (BackgroundingManager.isBackgroundingPending()) {
            BackgroundingManager.onEnteringBackground();
        }
        // save call log
        // OPDataManager.getInstance().saveCallRecord();
    }

    public void hangupCall(OPCall mCall, CallClosedReasons reason) {
        mCall.hangup(reason);
        onCallEnd(mCall);
    }

    public OPUser getPeerUserForCall(OPCall call) {
        OPContact contact = call.getPeer();

        OPUser user = OPDataManager.getDatastoreDelegate().getUser(contact,
                call.getConversationThread()
                        .getIdentityContactList(contact));
        return user;
    }

    Hashtable<String, CallStatus> mCallStates = new Hashtable<String, CallStatus>();

    public CallStatus getMediaStateForCall(String peerUri) {
        CallStatus state = null;
        if (mCallStates == null) {
            mCallStates = new Hashtable<String, CallStatus>();

        } else {
            state = mCallStates.get(peerUri);
        }
        if (state == null) {
            state = new CallStatus();
            mCallStates.put(peerUri, state);
        }
        return state;

    }

    public boolean hasCalls() {
        return mCalls != null && mCalls.size() > 0;
    }

    /**
     * Clean up on signout
     */
    public void onSignOut() {
        mCallStates.clear();
        mCalls.clear();
        mSessions.clear();
    }
}
