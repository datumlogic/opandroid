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

#include "openpeer/core/ISettings.h"
#include "openpeer/core/ILogger.h"
#include <android/log.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setup
 * Signature: (Lcom/openpeer/javaapi/OPSettingsDelegate;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setup
(JNIEnv *, jclass, jobject javaSettingsDelegate)
{
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native setup called");
	settingsDelegatePtr = SettingsDelegateWrapperPtr(new SettingsDelegateWrapper(javaSettingsDelegate));
	ISettings::setup(settingsDelegatePtr);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setString
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setString
(JNIEnv *env , jclass, jstring key, jstring value)
{
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native setString called");

	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

	const char *valueStr;
	valueStr = env->GetStringUTFChars(value, NULL);
	if (valueStr == NULL) {
		return;
	}
	__android_log_print(ANDROID_LOG_VERBOSE, "com.openpeer.jni", "OPSettings native setString key = %s, value = %s", keyStr, valueStr);
	ISettings::setString(keyStr, valueStr);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setInt
 * Signature: (Ljava/lang/String;J)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setInt
(JNIEnv *env, jclass, jstring key, jlong value)
{

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native setInt called");

	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}
	__android_log_print(ANDROID_LOG_VERBOSE, "com.openpeer.jni", "OPSettings native setInt key = %s, value = %d", keyStr, value);
	ISettings::setInt(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setUInt
 * Signature: (Ljava/lang/String;J)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setUInt
(JNIEnv *env, jclass, jstring key, jlong value)
{
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native setUInt called");

	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}
	__android_log_print(ANDROID_LOG_VERBOSE, "com.openpeer.jni", "OPSettings native setUInt key = %s, value = %u", keyStr, value);
	ISettings::setUInt(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setBool
 * Signature: (Ljava/lang/String;Z)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setBool
(JNIEnv *env, jclass, jstring key, jboolean value)
{
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native setBool called");

	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}
	__android_log_print(ANDROID_LOG_VERBOSE, "com.openpeer.jni", "OPSettings native setBool key = %s, value = %d", keyStr, value);
	ISettings::setBool(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setFloat
 * Signature: (Ljava/lang/String;F)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setFloat
(JNIEnv *env, jclass, jstring key, jfloat value)
{
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native setFloat called");

	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}
	__android_log_print(ANDROID_LOG_VERBOSE, "com.openpeer.jni", "OPSettings native setFloat key = %s, value = %f", keyStr, value);
	ISettings::setFloat(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setDouble
 * Signature: (Ljava/lang/String;D)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setDouble
(JNIEnv *env, jclass, jstring key, jdouble value)
{
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native setDouble called");

	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}
	__android_log_print(ANDROID_LOG_VERBOSE, "com.openpeer.jni", "OPSettings native setDouble key = %s, value = %g", keyStr, value);
	ISettings::setDouble(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    clear
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_clear
(JNIEnv *env, jclass, jstring key)
{
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native clear called");

	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}
	__android_log_print(ANDROID_LOG_VERBOSE, "com.openpeer.jni", "OPSettings native clear key = %s", keyStr);
	ISettings::clear(keyStr);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    apply
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPSettings_apply
(JNIEnv *env, jclass, jstring jsonSettings)
{
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native apply called");

	const char *jsonSettingsStr;
	jsonSettingsStr = env->GetStringUTFChars(jsonSettings, NULL);
	if (jsonSettingsStr == NULL) {
		return false;
	}

	__android_log_print(ANDROID_LOG_VERBOSE, "com.openpeer.jni", "OPSettings native apply jsonSettings = %s", jsonSettingsStr);

	return ISettings::apply(jsonSettingsStr);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    applyDefaults
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_applyDefaults
(JNIEnv *, jclass)
{
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSettings native applyDefaults called");
	ISettings::applyDefaults();
}

#ifdef __cplusplus
}
#endif
