#include "com_openpeer_javaapi_OPComposingStatus.h"
#include "openpeer/core/ComposingStatus.h"
#include "openpeer/core/IHelper.h"
#include <android/log.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPComposingStatus
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/ComposingStates;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPComposingStatus_toString
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPComposingStatus
 * Method:    toComposingState
 * Signature: (Ljava/lang/String;)Lcom/openpeer/javaapi/ComposingStates;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPComposingStatus_toComposingState
(JNIEnv *, jclass, jstring)
{

}

/*
 * Class:     com_openpeer_javaapi_OPComposingStatus
 * Method:    extract
 * Signature: (Ljava/lang/String;)Lcom/openpeer/javaapi/OPComposingStatus;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPComposingStatus_extract
(JNIEnv *, jclass, jstring dataEl)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPComposingStatus native extract called");

	jni_env = getEnv();

	const char *dataElStr;
	dataElStr = jni_env->GetStringUTFChars(dataEl, NULL);
	if (dataElStr == NULL) {
		return object;
	}

	ComposingStatusPtr statusPtr = ComposingStatus::extract(IHelper::createElement(dataElStr));

	if (statusPtr)
	{
		cls = findClass("com/openpeer/javaapi/OPComposingStatus");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		object = jni_env->NewObject(cls, method);

		ComposingStatusPtr* ptrToComposingStatus = new boost::shared_ptr<ComposingStatus>(statusPtr);
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong status = (jlong) ptrToComposingStatus;
		jni_env->SetLongField(object, fid, status);
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPComposingStatus native extract core pointer is NULL");
	}

	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPComposingStatus
 * Method:    insert
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPComposingStatus_insert
(JNIEnv *, jobject owner, jstring dataEl)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPComposingStatus native insert called");

	jni_env = getEnv();

	const char *dataElStr;
	dataElStr = jni_env->GetStringUTFChars(dataEl, NULL);
	if (dataElStr == NULL) {
		return;
	}

	cls = findClass("com/openpeer/javaapi/OPComposingStatus");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, fid);

	ComposingStatusPtr* coreComposingStatusPtr = (ComposingStatusPtr*)pointerValue;
	if (coreComposingStatusPtr)
	{
		coreComposingStatusPtr->get()->insert(IHelper::createElement(dataElStr));
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPComposingStatus native insert core pointer is NULL");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPComposingStatus
 * Method:    hasData
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPComposingStatus_hasData
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jboolean ret;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPComposingStatus native insert called");

	jni_env = getEnv();

	cls = findClass("com/openpeer/javaapi/OPComposingStatus");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, fid);

	ComposingStatusPtr* coreComposingStatusPtr = (ComposingStatusPtr*)pointerValue;
	if (coreComposingStatusPtr)
	{
		ret = (jboolean) coreComposingStatusPtr->get()->hasData();
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPComposingStatus native hasData core pointer is NULL");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPComposingStatus
 * Method:    toDebug
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPComposingStatus_toDebug
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring ret;
	String coreRetStr;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPComposingStatus native toDebug called");

	jni_env = getEnv();

	cls = findClass("com/openpeer/javaapi/OPComposingStatus");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, fid);

	ComposingStatusPtr* coreComposingStatusPtr = (ComposingStatusPtr*)pointerValue;
	if (coreComposingStatusPtr)
	{
		coreRetStr = String(IHelper::convertToString(coreComposingStatusPtr->get()->toDebug()));
		ret = jni_env->NewStringUTF(coreRetStr.c_str());
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPComposingStatus native toDebug core pointer is NULL");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPComposingStatus
 * Method:    releaseCoreObjects
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPComposingStatus_releaseCoreObjects
(JNIEnv *, jobject javaObject)
{
	if(javaObject != NULL)
	{
		JNIEnv *jni_env = getEnv();
		jclass cls = findClass("com/openpeer/javaapi/OPComposingStatus");
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaObject, fid);

		delete (ComposingStatusPtr*)pointerValue;
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPComposingStatus releaseCoreObjects Core object deleted.");

	}
	else
	{
		__android_log_print(ANDROID_LOG_WARN, "com.openpeer.jni", "OPComposingStatus releaseCoreObjects Core object not deleted - already NULL!");
	}
}

#ifdef __cplusplus
}
#endif
