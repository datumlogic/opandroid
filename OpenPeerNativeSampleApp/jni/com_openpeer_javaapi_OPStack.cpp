#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"

#include "OpenPeerCoreManager.h"
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

		IStackPtr stackPtr = IStack::singleton();
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong stack = (jlong) OpenPeerCoreManager::stackPtr.get();
		jni_env->SetLongField(object, fid, stack);

		__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni",
				"CorePtr raw = %p, ptr as long = %Lu",OpenPeerCoreManager::stackPtr.get(), stack);
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
	jclass cls;
	JNIEnv *jni_env = 0;

	if (OpenPeerCoreManager::stackPtr)
	{
		OpenPeerCoreManager::stackPtr->setup(globalEventManager, globalEventManager);
	}
	else
	{
		__android_log_write(ANDROID_LOG_WARN, "com.openpeer.jni", "Core stack is not initialized. It will be auto initialized.");
		OpenPeerCoreManager::stackPtr = IStack::singleton();
		OpenPeerCoreManager::stackPtr->setup(globalEventManager, globalEventManager);
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPStack");
			jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
			jlong stack = (jlong) OpenPeerCoreManager::stackPtr.get();
			jni_env->SetLongField(owner, fid, stack);
		}

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
	if (OpenPeerCoreManager::stackPtr)
	{
		OpenPeerCoreManager::stackPtr->shutdown();
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
	jlong longValue = env->CallLongMethod(expires, timeMethodID, false);
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
		jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(J)V");

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
