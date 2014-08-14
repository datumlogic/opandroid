/*
 
 Copyright (c) 2014, SMB Phone Inc.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.
 
 */

package com.openpeer.javaapi;

/**
 * @ExcludeFromJavadoc
 *
 */
public abstract class OPSettingsDelegate {

	public abstract String getString(String key);
	public abstract long getInt(String key);
	//TODO: Implement unsigned logic
	//http://jessicarbrown.com/resources/unsignedtojava.html
	public abstract long getUInt(String key);

	public abstract boolean getBool(String key);
	public abstract float getFloat(String key);
	public abstract double getDouble(String key);

	public abstract void setString(
			String key,
			String value
			);
	public abstract void setInt(
			String key,
			long value
			);
	//TODO: Implement unsigned logic
	//http://jessicarbrown.com/resources/unsignedtojava.html
	public abstract void setUInt(
			String key,
			long value
			);
	public abstract void setBool(
			String key,
			boolean value
			);
	public abstract void setFloat(
			String key,
			float value
			);
	public abstract void setDouble(
			String key,
			double value
			);

	public abstract void clear(String key);
}
