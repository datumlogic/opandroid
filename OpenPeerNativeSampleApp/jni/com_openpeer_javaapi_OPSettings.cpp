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
(JNIEnv *, jclass, jobject)
{
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
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

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
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

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
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

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
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

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
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

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
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

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
	const char *jsonSettingsStr;
	jsonSettingsStr = env->GetStringUTFChars(jsonSettings, NULL);
	if (jsonSettingsStr == NULL) {
		return false;
	}

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
	ISettings::applyDefaults();
}

#ifdef __cplusplus
}
#endif
