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
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"
#include <android/log.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    singleton
 * Signature: ()Lcom/openpeer/javaapi/OPStack;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPStack_singleton
(JNIEnv *, jclass owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	jni_env = getEnv();
	if(jni_env)
	{
		cls = findClass("com/openpeer/javaapi/OPStack");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		object = jni_env->NewObject(cls, method);

		IStackPtr stack = IStack::singleton();
		stackPair = std::pair<jobject, IStackPtr>(object, stack);
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    setup
 * Signature: (Lcom/openpeer/javaapi/OPStackDelegate;Lcom/openpeer/javaapi/OPMediaEngineDelegate;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStack_setup
(JNIEnv *env, jobject owner, jobject, jobject)
{

	if (stackPair.first == owner)
	{
		stackPair.second->setup(globalEventManager, globalEventManager);
	}
	else
	{
		__android_log_write(ANDROID_LOG_WARN, "com.openpeer.jni", "Core stack is not initialized. It will be auto initialized.");
		IStack::singleton()->setup(globalEventManager, globalEventManager);
		stackPair = std::pair<jobject, IStackPtr>(owner, IStack::singleton());
	}
}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStack_shutdown
(JNIEnv *, jobject owner)
{
	if (stackPair.first == owner)
	{
		stackPair.second->shutdown();
	}
	else
	{
		__android_log_write(ANDROID_LOG_WARN, "com.openpeer.jni", "Core stack is not initialized. It will be auto initialized.");
		IStack::singleton()->shutdown();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    createAuthorizedApplicationID
 * Signature: (Ljava/lang/String;Ljava/lang/String;Landroid/text/format/Time;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPStack_createAuthorizedApplicationID
(JNIEnv *env, jclass, jstring applicationID, jstring applicationIDSharedSecret, jobject expires)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring authorizedApplicationID;

	String applicationIDString;
	applicationIDString = env->GetStringUTFChars(applicationID, NULL);
	if (applicationIDString == NULL) {
		return authorizedApplicationID;
	}

	String applicationIDSharedSecretString;
	applicationIDSharedSecretString = env->GetStringUTFChars(applicationIDSharedSecret, NULL);
	if (applicationIDSharedSecretString == NULL) {
		return authorizedApplicationID;
	}
	jni_env = getEnv();
	cls = findClass("android/text/format/Time");
	jmethodID timeMethodID   = env->GetMethodID(cls, "toMillis", "(Z)J");
	long longValue = (long) env->CallIntMethod(expires, timeMethodID, false);
	Time t = boost::posix_time::from_time_t(longValue/1000) + boost::posix_time::millisec(longValue % 1000);


	authorizedApplicationID =  env->NewStringUTF(IStack::createAuthorizedApplicationID(applicationIDString, applicationIDSharedSecretString, t).c_str());

	return authorizedApplicationID;
}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    getAuthorizedApplicationIDExpiry
 * Signature: (Ljava/lang/String;Landroid/text/format/Time;)Landroid/text/format/Time;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPStack_getAuthorizedApplicationIDExpiry
(JNIEnv *env, jclass, jstring authorizedApplicationID, jobject ignoreThis)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	String authorizedApplicationIDString;
	authorizedApplicationIDString = env->GetStringUTFChars(authorizedApplicationID, NULL);
	if (authorizedApplicationIDString == NULL) {
		return object;
	}

	Time expiryTime = IStack::getAuthorizedApplicationIDExpiry(authorizedApplicationIDString);

	jni_env = getEnv();

	if (jni_env)
	{
		//Convert and set time from C++ to Android; Fetch methods needed to accomplish this
		Time time_t_epoch = boost::posix_time::time_from_string("1970-01-01 00:00:00.000");
		jclass timeCls = findClass("android/text/format/Time");
		jmethodID timeMethodID = jni_env->GetMethodID(timeCls, "<init>", "()V");
		jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(Z)V");

		//calculate and set expiry time
		zsLib::Duration expiryTimeDuration = expiryTime - time_t_epoch;
		object = jni_env->NewObject(timeCls, timeMethodID);
		jni_env->CallVoidMethod(object, timeSetMillisMethodID, expiryTimeDuration.total_milliseconds());
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    isAuthorizedApplicationIDExpiryWindowStillValid
 * Signature: (Ljava/lang/String;Landroid/text/format/Time;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPStack_isAuthorizedApplicationIDExpiryWindowStillValid
(JNIEnv *env, jclass, jstring authorizedApplicationID, jobject ignoreThis)
{
	jclass cls;
	jmethodID method;
	jobject object;
	jboolean ret;
	JNIEnv *jni_env = 0;

	String authorizedApplicationIDString;
	authorizedApplicationIDString = env->GetStringUTFChars(authorizedApplicationID, NULL);
	if (authorizedApplicationIDString == NULL) {
		return ret;
	}

	Duration duration = Seconds(1);

	ret = IStack::isAuthorizedApplicationIDExpiryWindowStillValid(authorizedApplicationIDString, duration);
}

#ifdef __cplusplus
}
#endif
