#include "com_openpeer_javaapi_OPBackgroundingQuery.h"
#include "openpeer/core/IBackgrounding.h"
#include "openpeer/core/ILogger.h"
#include <android/log.h>


#include "globals.h"

using namespace openpeer::core;
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPBackgroundingQuery
 * Method:    getID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPBackgroundingQuery_getID
(JNIEnv *, jobject owner)
{
	jlong ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingQuery native getID called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPBackgroundingQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IBackgroundingQueryPtr* coreQueryPtr = (IBackgroundingQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		ret = (jlong) coreQueryPtr->get()->getID();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPBackgroundingQuery native getID core pointer is NULL!!!");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPBackgroundingQuery
 * Method:    isReady
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPBackgroundingQuery_isReady
(JNIEnv *, jobject owner)
{
	jboolean ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingQuery native isReady called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPBackgroundingQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IBackgroundingQueryPtr* coreQueryPtr = (IBackgroundingQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		ret = (jboolean) coreQueryPtr->get()->isReady();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPBackgroundingQuery native isReady core pointer is NULL!!!");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPBackgroundingQuery
 * Method:    totalBackgroundingSubscribersStillPending
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPBackgroundingQuery_totalBackgroundingSubscribersStillPending
  (JNIEnv *, jobject owner)
{
	jlong ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingQuery native totalBackgroundingSubscribersStillPending called");

	jni_env = getEnv();
	jclass queryClass = findClass("com/openpeer/javaapi/OPBackgroundingQuery");
	jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, queryFid);

	IBackgroundingQueryPtr* coreQueryPtr = (IBackgroundingQueryPtr*)pointerValue;

	if (coreQueryPtr)
	{
		ret = (jlong) coreQueryPtr->get()->totalBackgroundingSubscribersStillPending();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPBackgroundingQuery native totalBackgroundingSubscribersStillPending core pointer is NULL!!!");
	}
}


/*
 * Class:     com_openpeer_javaapi_OPBackgroundingQuery
 * Method:    releaseCoreObjects
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPBackgroundingQuery_releaseCoreObjects
(JNIEnv *, jobject javaObject)
{
	if(javaObject != NULL)
	{
		JNIEnv *jni_env = getEnv();
		jclass queryClass = findClass("com/openpeer/javaapi/OPBackgroundingQuery");
		jfieldID queryFid = jni_env->GetFieldID(queryClass, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaObject, queryFid);

		delete (IBackgroundingQueryPtr*)pointerValue;
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingQuery Core object deleted.");

	}
	else
	{
		__android_log_print(ANDROID_LOG_WARN, "com.openpeer.jni", "OPBackgroundingQuery Core object not deleted - already NULL!");
	}
}
#ifdef __cplusplus
}
#endif
