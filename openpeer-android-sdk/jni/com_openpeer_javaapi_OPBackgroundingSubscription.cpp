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
 * Class:     com_openpeer_javaapi_OPBackgroundingSubscription
 * Method:    getID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPBackgroundingSubscription_getID
(JNIEnv *, jobject owner)
{
	jlong ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingSubscription native getID called");

	jni_env = getEnv();
	jclass subscriptionClass = findClass("com/openpeer/javaapi/OPBackgroundingSubscription");
	jfieldID subscriptionFid = jni_env->GetFieldID(subscriptionClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, subscriptionFid);

	IBackgroundingSubscriptionPtr* coreSubscriptionPtr = (IBackgroundingSubscriptionPtr*)pointerValue;

	if (coreSubscriptionPtr)
	{
		ret = (jlong) coreSubscriptionPtr->get()->getID();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPBackgroundingSubscription native getID core pointer is NULL!!!");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPBackgroundingSubscription
 * Method:    cancel
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPBackgroundingSubscription_cancel
(JNIEnv *, jobject owner)
{
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingSubscription native cancel called");

	jni_env = getEnv();
	jclass subscriptionClass = findClass("com/openpeer/javaapi/OPBackgroundingSubscription");
	jfieldID subscriptionFid = jni_env->GetFieldID(subscriptionClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, subscriptionFid);

	IBackgroundingSubscriptionPtr* coreSubscriptionPtr = (IBackgroundingSubscriptionPtr*)pointerValue;

	if (coreSubscriptionPtr)
	{
		coreSubscriptionPtr->get()->cancel();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPBackgroundingSubscription native cancel core pointer is NULL!!!");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPBackgroundingSubscription
 * Method:    releaseCoreObjects
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPBackgroundingSubscription_releaseCoreObjects
(JNIEnv *, jobject javaObject)
{
	if(javaObject != NULL)
	{
		JNIEnv *jni_env = getEnv();
		jclass subscriptionClass = findClass("com/openpeer/javaapi/OPBackgroundingSubscription");
		jfieldID subscriptionFid = jni_env->GetFieldID(subscriptionClass, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaObject, subscriptionFid);

		delete (IBackgroundingSubscriptionPtr*)pointerValue;
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingSubscription Core object deleted.");

	}
	else
	{
		__android_log_print(ANDROID_LOG_WARN, "com.openpeer.jni", "OPBackgroundingSubscription Core object not deleted - already NULL!");
	}
}
#ifdef __cplusplus
}
#endif
