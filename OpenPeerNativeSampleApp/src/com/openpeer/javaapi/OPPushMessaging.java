package com.openpeer.javaapi;

import java.util.List;
import java.util.Map;

import android.text.format.Time;
import android.util.Log;

public class OPPushMessaging {
	private long nativeClassPointer;

	private long nativeDelegatePointer;

	//-----------------------------------------------------------------------
	// PURPOSE: create a connection to the push messaging service
	public static native OPPushMessaging create(
			OPPushMessagingDelegate delegate,
			OPPushMessagingDatabaseAbstractionDelegate databaseDelegate,
			OPAccount account
			);

	//-----------------------------------------------------------------------
	// PURPOSE: get the push messaging object instance ID
	public native long getID();

	//-----------------------------------------------------------------------
	// PURPOSE: get the current state of the push messaging service
	public native PushMessagingStates getState(
			int outErrorCode,
			String outErrorReason
			);

	//-----------------------------------------------------------------------
	// PURPOSE: shutdown the connection to the push messaging service
	public native void shutdown();

	//-----------------------------------------------------------------------
	// PURPOSE: register or unregister for push messages
	public native OPPushMessagingRegisterQuery registerDevice(
			OPPushMessagingRegisterQueryDelegate inDelegate,
			String inDeviceToken,        // a token used for pushing to this particular service
			Time inExpires,                   // how long should the subscription for push messaging last; pass in Time() to remove a previous subscription
			String inMappedType,         // for APNS maps to "loc-key"
			boolean inUnreadBadge,               // true causes total unread messages to be displayed in badge
			String inSound,              // what sound to play upon receiving a message. For APNS, maps to "sound" field
			String inAction,             // for APNS, maps to "action-loc-key"
			String inLaunchImage,        // for APNS, maps to "launch-image"
			int inPriority,          // for APNS, maps to push priority
			List<String> inValueNames // list of values requested from each push from the push server (in order they should be delivered); empty = all values
			);

	//-----------------------------------------------------------------------
	// PURPOSE: send a push message to a contact list
	public native OPPushMessagingQuery push(
			OPPushMessagingQueryDelegate delegate,
			List<OPContact> toContactList,
			OPPushMessage message
			);

	//-----------------------------------------------------------------------
	// PURPOSE: cause a check to refresh data contained within the server
	public native void recheckNow();

	//-----------------------------------------------------------------------
	// PURPOSE: get delta list of messages that have changed since last
	//          fetch of messages
	// RETURNS: true if call was successful otherwise false
	// NOTES:   If false is returned the current list of messages must be
	//          flushed and all messages must be downloaded again (i.e.
	//          a version conflict was detected). Pass in NULL
	//          for "inLastVersionDownloaded" if false is returned. If false
	//          is still returning then the messages cannot be fetched.
	//
	//          The resultant list can be empty even if the method
	//          returns true. This could mean all the downloaded messages
	//          were filtererd out because they were not compatible push
	//          messages.
	public native List<OPPushMessage> getMessagesUpdates(
			String inLastVersionDownloaded,  // pass in NULL if no previous version known
			String outUpdatedToVersion          // updated to this version (if same as passed in then no change available)
			);

	//-----------------------------------------------------------------------
	// PURPOSE: extract a list of name / value pairs contained within
	//          a push info structure
	// RETURNS: a pointer to the name value map
	public static native Map<String, String> getValues(OPPushInfo pushInfo);

	//-----------------------------------------------------------------------
	// PURPOSE: create a JSON blob compatible with the PushInfo.mValues
	//          based on a collection of name / value pairs.
	// RETURNS: a pointer to the values blob or null ElementPtr() if no
	//          values were found.
	public static native OPElement createValues(Map<String, String> values);

	//-----------------------------------------------------------------------
	// PURPOSE: mark an individual message as having been read
	public native void markPushMessageRead(String messageID);

	//-----------------------------------------------------------------------
	// PURPOSE: delete an individual message
	public native void deletePushMessage(String messageID);

	private native void releaseCoreObjects(); 

	protected void finalize() throws Throwable {

		if (nativeClassPointer != 0 || nativeDelegatePointer != 0)
		{
			Log.d("output", "Cleaning push messaging core objects");
			releaseCoreObjects();
		}

		super.finalize();
	}
}
