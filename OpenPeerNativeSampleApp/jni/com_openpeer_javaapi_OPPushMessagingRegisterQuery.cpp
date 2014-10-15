#include "com_openpeer_javaapi_OPPushMessagingRegisterQuery.h"
#include "openpeer/core/IPushMessaging.h"
#include "openpeer/core/ILogger.h"
#include <android/log.h>


#include "globals.h"

using namespace openpeer::core;
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPPushMessagingRegisterQuery
 * Method:    getID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPPushMessagingRegisterQuery_getID
(JNIEnv *, jobject owner)
{
	jlong ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushMessagingRegisterQuery native getID called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPPushMessagingRegisterQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IPushMessagingRegisterQueryPtr* coreQueryPtr = (IPushMessagingRegisterQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		ret = (jlong) coreQueryPtr->get()->getID();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPPushMessagingRegisterQuery native getID core pointer is NULL!!!");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPPushMessagingRegisterQuery
 * Method:    isComplete
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPPushMessagingRegisterQuery_isComplete
  (JNIEnv *, jobject owner)
{
	jboolean ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushMessagingRegisterQuery native isComplete called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPPushMessagingRegisterQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IPushMessagingRegisterQueryPtr* coreQueryPtr = (IPushMessagingRegisterQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		WORD errorCode = 0;
		String errorReason;

		ret = (jboolean) coreQueryPtr->get()->isComplete(&errorCode, &errorReason);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPPushMessagingRegisterQuery native isComplete core pointer is NULL!!!");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPPushMessagingRegisterQuery
 * Method:    releaseCoreObjects
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPPushMessagingRegisterQuery_releaseCoreObjects
(JNIEnv *, jobject javaObject)
{
	if(javaObject != NULL)
	{
		JNIEnv *jni_env = getEnv();
		jclass queryClass = findClass("com/openpeer/javaapi/OPPushMessagingRegisterQuery");
		jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaObject, queryFid);

		delete (IPushMessagingRegisterQueryPtr*)pointerValue;
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushMessagingRegisterQuery Core object deleted.");

	}
	else
	{
		__android_log_print(ANDROID_LOG_WARN, "com.openpeer.jni", "OPPushMessagingRegisterQuery Core object not deleted - already NULL!");
	}
}
#ifdef __cplusplus
}
#endif
