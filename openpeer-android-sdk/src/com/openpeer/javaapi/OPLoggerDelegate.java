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
package com.openpeer.javaapi;

/**
 * @ExcludeFromJavadoc
 */
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
