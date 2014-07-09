package com.openpeer.sdk.utils;

import com.openpeer.app.OPUser;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;

public class OPModelUtils {
	public static OPContact getPeer(OPCall call) {
		OPContact peer = call.getCaller();
		if (peer.isSelf()) {
			return call.getCallee();
		} else {
			return peer;
		}
	}

	public static OPUser getPeerUser(OPCall call) {
		OPContact contact = getPeer(call);
		return new OPUser(contact, call.getConversationThread().getIdentityContactList(contact));
	}

}
