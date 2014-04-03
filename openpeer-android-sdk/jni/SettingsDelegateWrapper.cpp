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

#include "SettingsDelegateWrapper.h"
#include "globals.h"

String SettingsDelegateWrapper::getString(const char *key) const
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;
	const char *fetchedStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return "";
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "getString", "(Ljava/lang/String;)Ljava/lang/String;");
	object = jni_env->CallStaticObjectMethod(cls, method, keyStr);

	cls = findClass("java/lang/String");
	if(jni_env->IsInstanceOf(object, cls) == JNI_TRUE)
	{
		fetchedStr = jni_env->GetStringUTFChars((jstring)object, NULL);

	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
	return fetchedStr;
}
LONG SettingsDelegateWrapper::getInt(const char *key) const
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;
	long fetchedInt;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return fetchedInt;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "getInt", "(Ljava/lang/String;)J");
	fetchedInt = jni_env->CallStaticLongMethod(cls, method, keyStr);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
	return fetchedInt;
}
ULONG SettingsDelegateWrapper::getUInt(const char *key) const
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;
	long fetchedUInt;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return fetchedUInt;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "getUIntSetting", "(Ljava/lang/String;)J");
	fetchedUInt = jni_env->CallStaticLongMethod(cls, method, keyStr);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
	return fetchedUInt;
}
bool SettingsDelegateWrapper::getBool(const char *key) const
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;
	bool fetchedBool;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return fetchedBool;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "getBool", "(Ljava/lang/String;)Z");
	fetchedBool = jni_env->CallStaticBooleanMethod(cls, method, keyStr);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
	return fetchedBool;
}
float SettingsDelegateWrapper::getFloat(const char *key) const
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;
	float fetchedFloat;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return fetchedFloat;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "getFloat", "(Ljava/lang/String;)F");
	fetchedFloat = jni_env->CallStaticFloatMethod(cls, method, keyStr);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
	return fetchedFloat;
}
double SettingsDelegateWrapper::getDouble(const char *key) const
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;
	double fetchedDouble;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return fetchedDouble;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "getDouble", "(Ljava/lang/String;)D");
	fetchedDouble = jni_env->CallStaticDoubleMethod(cls, method, keyStr);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
	return fetchedDouble;
}

void SettingsDelegateWrapper::setString(
		const char *key,
		const char *value
)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;
	jstring valueStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	keyStr =  jni_env->NewStringUTF(key);
	valueStr =  jni_env->NewStringUTF(value);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "setString", "(Ljava/lang/String;Ljava/lang/String;)V");
	jni_env->CallStaticVoidMethod(cls, method, keyStr, valueStr);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void SettingsDelegateWrapper::setInt(
		const char *key,
		LONG value
)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "setInt", "(Ljava/lang/String;J)V");
	jni_env->CallStaticVoidMethod(cls, method, keyStr, value);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void SettingsDelegateWrapper::setUInt(
		const char *key,
		ULONG value
)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "setUInt", "(Ljava/lang/String;J)V");
	jni_env->CallStaticVoidMethod(cls, method, keyStr, value);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void SettingsDelegateWrapper::setBool(
		const char *key,
		bool value
)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "setBool", "(Ljava/lang/String;Z)V");
	jni_env->CallStaticVoidMethod(cls, method, keyStr, value);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void SettingsDelegateWrapper::setFloat(
		const char *key,
		float value
)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "setFloat", "(Ljava/lang/String;F)V");
	jni_env->CallStaticVoidMethod(cls, method, keyStr, value);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void SettingsDelegateWrapper::setDouble(
		const char *key,
		double value
)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "setDouble", "(Ljava/lang/String;D)V");
	jni_env->CallStaticVoidMethod(cls, method, keyStr, value);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

void SettingsDelegateWrapper::clear(const char *key)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring keyStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	keyStr =  jni_env->NewStringUTF(key);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "clearSettings", "(Ljava/lang/String;)V");
	jni_env->CallStaticVoidMethod(cls, method, keyStr);


	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

SettingsDelegateWrapper::~SettingsDelegateWrapper()
{

}
