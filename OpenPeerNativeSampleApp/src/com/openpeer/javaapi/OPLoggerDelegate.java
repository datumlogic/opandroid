package com.openpeer.javaapi;

public abstract class OPLoggerDelegate {
	//-----------------------------------------------------------------------
	// PURPOSE: Notify that a new subsystem has been created.
	// WARNING: These methods may be called from ANY thread thus these methods
	//          must be thread safe and non-blocking. zsLib::Proxy should *NOT*
	//          be used to create a proxy to this delegate.
	public abstract void onNewSubsystem(
			int subsystemUniqueID,
			String subsystemName
			);

	//-----------------------------------------------------------------------
	// PURPOSE: Notify a new log message has been created.
	// WARNING: These methods may be called from ANY thread thus these methods
	//          must be thread safe and non-blocking. zsLib::Proxy should *NOT*
	//          be used to create a proxy to this delegate.
	public abstract void onLog(
			int subsystemUniqueID,
			String subsystemName,
			OPLogSeverity severity,
			OPLogLevel level,
			String message,
			String function,
			String filePath,
			long lineNumber
			);
}
