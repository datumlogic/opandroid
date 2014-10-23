#include "com_openpeer_javaapi_OPPushPresenceRegisterQuery.h"
#include "openpeer/core/IPushPresence.h"
#include "openpeer/core/ILogger.h"
#include <android/log.h>


#include "globals.h"

using namespace openpeer::core;
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPPushPresenceRegisterQuery
 * Method:    getID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPPushPresenceRegisterQuery_getID
(JNIEnv *, jobject owner)
{
	jlong ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushPresenceRegisterQuery native getID called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPPushPresenceRegisterQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IPushPresenceRegisterQueryPtr* coreQueryPtr = (IPushPresenceRegisterQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		ret = (jlong) coreQueryPtr->get()->getID();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPPushPresenceRegisterQuery native getID core pointer is NULL!!!");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPPushPresenceRegisterQuery
 * Method:    isComplete
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPPushPresenceRegisterQuery_isComplete
  (JNIEnv *, jobject owner)
{
	jboolean ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushPresenceRegisterQuery native isComplete called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPPushPresenceRegisterQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IPushPresenceRegisterQueryPtr* coreQueryPtr = (IPushPresenceRegisterQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		WORD errorCode = 0;
		String errorReason;

		ret = (jboolean) coreQueryPtr->get()->isComplete(&errorCode, &errorReason);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPPushPresenceRegisterQuery native isComplete core pointer is NULL!!!");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPPushPresenceRegisterQuery
 * Method:    releaseCoreObjects
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPPushPresenceRegisterQuery_releaseCoreObjects
(JNIEnv *, jobject javaObject)
{
	if(javaObject != NULL)
	{
		JNIEnv *jni_env = getEnv();
		jclass queryClass = findClass("com/openpeer/javaapi/OPPushPresenceRegisterQuery");
		jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaObject, queryFid);

		delete (IPushPresenceRegisterQueryPtr*)pointerValue;
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPPushPresenceRegisterQuery Core object deleted.");

	}
	else
	{
		__android_log_print(ANDROID_LOG_WARN, "com.openpeer.jni", "OPPushPresenceRegisterQuery Core object not deleted - already NULL!");
	}
}
#ifdef __cplusplus
}
#endif
