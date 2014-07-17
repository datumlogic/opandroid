package com.openpeer.sdk.utils;

import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.sdk.model.OPUser;

public class OPModelUtils {

	public static long getWindowId(long userIds[]) {
		Arrays.sort(userIds);
		String arr[] = new String[userIds.length];
		for (int i = 0; i < userIds.length; i++) {
			arr[i] = "" + userIds[i];
		}
		long code = Arrays.deepHashCode(arr);
		Log.d("test", " hash code " + code + " array " + Arrays.deepToString(arr));
		return code;
	}

	public static long getWindowId(List<OPUser> users) {
		long userIds[] = new long[users.size()];
		for (int i = 0; i < userIds.length; i++) {
			OPUser user = users.get(i);
			userIds[i] = user.getUserId();
		}
		// TODO Auto-generated method stub
		return getWindowId(userIds);
	}

}
