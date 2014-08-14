package com.openpeer.delegates;

import android.util.Log;

import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogSeverity;
import com.openpeer.javaapi.OPLoggerDelegate;

public class OPLoggerDelegateImplementation extends OPLoggerDelegate {

	@Override
	public void onNewSubsystem(int subsystemUniqueID, String subsystemName) {
		// TODO Auto-generated method stub
		Log.i("output", "onNewSubsystem received in Java");

	}

	@Override
	public void onLog(int subsystemUniqueID, String subsystemName,
			OPLogSeverity severity, OPLogLevel level, String message,
			String function, String filePath, long lineNumber) {
		// TODO Auto-generated method stub
		Log.i("output", "onLog received in Java");

	}

}
