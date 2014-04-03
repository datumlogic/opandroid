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

#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "globals.h"
#include "CacheDelegateWrapper.h"

//ICacheDelegate implementation
zsLib::String CacheDelegateWrapper::fetch(const char *cookieNamePath)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring cookieJavaString;
	const char *fetchedStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return "";
	}

	cookieJavaString =  jni_env->NewStringUTF(cookieNamePath);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "fetch", "(Ljava/lang/String;)V");
	object = jni_env->CallStaticObjectMethod(cls, method, cookieJavaString);

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
void CacheDelegateWrapper::store(const char *cookieNamePath,
		Time expires,
		const char *str)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "store", "(Ljava/lang/String;ILjava/lang/String;)V");
	jni_env->CallStaticVoidMethod(cls, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void CacheDelegateWrapper::clear(const char *cookieNamePath)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "clear", "(Ljava/lang/String;)V");
	jni_env->CallStaticVoidMethod(cls, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

CacheDelegateWrapper::~CacheDelegateWrapper()
{

}
