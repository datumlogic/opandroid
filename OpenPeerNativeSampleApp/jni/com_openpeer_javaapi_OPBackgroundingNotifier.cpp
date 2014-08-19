#include "com_openpeer_javaapi_OPBackgroundingNotifier.h"
#include "openpeer/core/IBackgrounding.h"
#include "openpeer/core/ILogger.h"
#include <android/log.h>


#include "globals.h"

using namespace openpeer::core;
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPBackgroundingNotifier
 * Method:    getID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPBackgroundingNotifier_getID
(JNIEnv *, jobject owner)
{
	jlong ret;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingNotifier native getID called");

	jni_env = getEnv();
	jclass notifierClass = findClass("com/openpeer/javaapi/OPBackgroundingNotifier");
	jfieldID notifierFid = jni_env->GetFieldID(notifierClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, notifierFid);

	IBackgroundingNotifierPtr* coreNotifierPtr = (IBackgroundingNotifierPtr*)pointerValue;

	if (coreNotifierPtr)
	{
		ret = (jlong) coreNotifierPtr->get()->getID();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPBackgroundingNotifier native getID core pointer is NULL!!!");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPBackgroundingNotifier
 * Method:    ready
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPBackgroundingNotifier_ready
(JNIEnv *, jobject owner)
{
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingNotifier native ready called");

	jni_env = getEnv();
	jclass notifierClass = findClass("com/openpeer/javaapi/OPBackgroundingNotifier");
	jfieldID notifierFid = jni_env->GetFieldID(notifierClass, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, notifierFid);

	IBackgroundingNotifierPtr* coreNotifierPtr = (IBackgroundingNotifierPtr*)pointerValue;

	if (coreNotifierPtr)
	{
		coreNotifierPtr->get()->ready();
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "OPBackgroundingNotifier native ready core pointer is NULL!!!");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPBackgroundingNotifier
 * Method:    releaseCoreObjects
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPBackgroundingNotifier_releaseCoreObjects
(JNIEnv *, jobject javaObject)
{
	if(javaObject != NULL)
	{
		JNIEnv *jni_env = getEnv();
		jclass notifierClass = findClass("com/openpeer/javaapi/OPBackgroundingNotifier");
		jfieldID notifierFid = jni_env->GetFieldID(notifierClass, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaObject, notifierFid);

		delete (IBackgroundingNotifierPtr*)pointerValue;
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPBackgroundingNotifier Core object deleted.");

	}
	else
	{
		__android_log_print(ANDROID_LOG_WARN, "com.openpeer.jni", "OPBackgroundingNotifier Core object not deleted - already NULL!");
	}
}
#ifdef __cplusplus
}
#endif
