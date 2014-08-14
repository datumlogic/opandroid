package com.openpeer.sdk.delegates;

import android.util.Log;

import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogSeverity;
import com.openpeer.javaapi.OPLoggerDelegate;

/**
 * @ExcludeFromJavadoc
 */
public class OPLoggerDelegateImpl extends OPLoggerDelegate {
	private static final String TAG = OPLoggerDelegateImpl.class.getSimpleName();

	@Override
	public void onNewSubsystem(int subsystemUniqueID, String subsystemName) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onNewSubsystem received in Java");

	}

	@Override
	public void onLog(int subsystemUniqueID, String subsystemName,
			OPLogSeverity severity, OPLogLevel level, String message,
			String function, String filePath, long lineNumber) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onLog received in Java");

	}

}
