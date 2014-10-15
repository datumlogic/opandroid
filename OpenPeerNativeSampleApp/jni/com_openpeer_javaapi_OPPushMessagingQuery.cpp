#include "com_openpeer_javaapi_OPPushMessagingQuery.h"
#include "OpenPeerCoreManager.h"
#include "openpeer/core/IPushMessaging.h"
#include "openpeer/core/ILogger.h"
#include <android/log.h>


#include "globals.h"

using namespace openpeer::core;
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPPushMessagingQuery
 * Method:    getID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPPushMessagingQuery_getID
(JNIEnv *, jobject owner)
{
	jlong ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushMessagingQuery native getID called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPPushMessagingQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IPushMessagingQueryPtr* coreQueryPtr = (IPushMessagingQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		ret = (jlong) coreQueryPtr->get()->getID();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPPushMessagingQuery native getID core pointer is NULL!!!");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPPushMessagingQuery
 * Method:    cancel
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPPushMessagingQuery_cancel
  (JNIEnv *, jobject owner)
{
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushMessagingQuery native cancel called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPPushMessagingQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IPushMessagingQueryPtr* coreQueryPtr = (IPushMessagingQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		coreQueryPtr->get()->cancel();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPPushMessagingQuery native cancel core pointer is NULL!!!");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPPushMessagingQuery
 * Method:    isComplete
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPPushMessagingQuery_isUploaded
  (JNIEnv *, jobject owner)
{
	jboolean ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushMessagingQuery native isUploaded called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPPushMessagingQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IPushMessagingQueryPtr* coreQueryPtr = (IPushMessagingQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		ret = (jboolean) coreQueryPtr->get()->isUploaded();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPPushMessagingQuery native isUploaded core pointer is NULL!!!");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPPushMessagingQuery
 * Method:    getPushMessage
 * Signature: ()Lcom/openpeer/javaapi/OPPushMessage;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPPushMessagingQuery_getPushMessage
  (JNIEnv *, jobject owner)
{
	IPushMessaging::PushMessagePtr coreMessage;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushMessagingQuery native getPushMessage called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPPushMessagingQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IPushMessagingQueryPtr* coreQueryPtr = (IPushMessagingQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		coreMessage = coreQueryPtr->get()->getPushMessage();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPPushMessagingQuery native getPushMessage core pointer is NULL!!!");
	}

	return OpenPeerCoreManager::pushMessageToJava(*(coreMessage.get()));
}

/*
 * Class:     com_openpeer_javaapi_OPPushMessagingQuery
 * Method:    releaseCoreObjects
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPPushMessagingQuery_releaseCoreObjects
(JNIEnv *, jobject javaObject)
{
	if(javaObject != NULL)
	{
		JNIEnv *jni_env = getEnv();
		jclass queryClass = findClass("com/openpeer/javaapi/OPPushMessagingQuery");
		jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaObject, queryFid);

		delete (IPushMessagingQueryPtr*)pointerValue;
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushMessagingQuery Core object deleted.");

	}
	else
	{
		__android_log_print(ANDROID_LOG_WARN, "com.openpeer.jni", "OPPushMessagingQuery Core object not deleted - already NULL!");
	}
}
#ifdef __cplusplus
}
#endif
