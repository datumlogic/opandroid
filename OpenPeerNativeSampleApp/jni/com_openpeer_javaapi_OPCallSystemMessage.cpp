#include "com_openpeer_javaapi_OPCallSystemMessage.h"
#include "openpeer/core/ISystemMessage.h"
#include "openpeer/core/IHelper.h"
#include <android/log.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPCallSystemMessage
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/CallSystemMessageTypes;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPCallSystemMessage_toString
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPCallSystemMessage
 * Method:    toCallSystemMessageType
 * Signature: (Ljava/lang/String;)Lcom/openpeer/javaapi/CallSystemMessageTypes;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCallSystemMessage_toCallSystemMessageType
(JNIEnv *, jclass, jstring)
{

}

/*
 * Class:     com_openpeer_javaapi_OPCallSystemMessage
 * Method:    extract
 * Signature: (Ljava/lang/String;Lcom/openpeer/javaapi/OPAccount;)Lcom/openpeer/javaapi/OPCallSystemMessage;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCallSystemMessage_extract
(JNIEnv *, jclass, jstring dataEl, jobject javaAccount)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	CallSystemMessagePtr systemMessagePtr;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPCallSystemMessage native extract called");

	jni_env = getEnv();

	const char *dataElStr;
	dataElStr = jni_env->GetStringUTFChars(dataEl, NULL);
	if (dataElStr == NULL) {
		return object;
	}
	if (javaAccount != NULL)
	{
		jclass accountClass = findClass("com/openpeer/javaapi/OPAccount");
		jfieldID accountFid = jni_env->GetFieldID(accountClass, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaAccount, accountFid);

		IAccountPtr* coreAccountPtr = (IAccountPtr*)pointerValue;

		if(coreAccountPtr)
		{
			systemMessagePtr = CallSystemMessage::extract(IHelper::createElement(dataElStr), *coreAccountPtr);
		}
	}



	if (systemMessagePtr)
	{
		cls = findClass("com/openpeer/javaapi/OPCallSystemMessage");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		object = jni_env->NewObject(cls, method);

		CallSystemMessagePtr* ptrToCallSystemMessage = new boost::shared_ptr<CallSystemMessage>(systemMessagePtr);
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong systemMessage = (jlong) ptrToCallSystemMessage;
		jni_env->SetLongField(object, fid, systemMessage);
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPCallSystemMessage native extract core pointer is NULL");
	}

	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPCallSystemMessage
 * Method:    insert
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPCallSystemMessage_insert
(JNIEnv *, jobject owner, jstring dataEl)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPCallSystemMessage native insert called");

	jni_env = getEnv();

	const char *dataElStr;
	dataElStr = jni_env->GetStringUTFChars(dataEl, NULL);
	if (dataElStr == NULL) {
		return;
	}

	cls = findClass("com/openpeer/javaapi/OPCallSystemMessage");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, fid);

	CallSystemMessagePtr* coreCallSystemMessagePtr = (CallSystemMessagePtr*)pointerValue;
	if (coreCallSystemMessagePtr)
	{
		coreCallSystemMessagePtr->get()->insert(IHelper::createElement(dataElStr));
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPCallSystemMessage native insert core pointer is NULL");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPCallSystemMessage
 * Method:    hasData
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPCallSystemMessage_hasData
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jboolean ret;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPCallSystemMessage native insert called");

	jni_env = getEnv();

	cls = findClass("com/openpeer/javaapi/OPCallSystemMessage");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, fid);

	CallSystemMessagePtr* coreCallSystemMessagePtr = (CallSystemMessagePtr*)pointerValue;
	if (coreCallSystemMessagePtr)
	{
		ret = (jboolean) coreCallSystemMessagePtr->get()->hasData();
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPCallSystemMessage native hasData core pointer is NULL");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPCallSystemMessage
 * Method:    toDebug
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPCallSystemMessage_toDebug
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring ret;
	String coreRetStr;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPCallSystemMessage native toDebug called");

	jni_env = getEnv();

	cls = findClass("com/openpeer/javaapi/OPCallSystemMessage");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, fid);

	CallSystemMessagePtr* coreCallSystemMessagePtr = (CallSystemMessagePtr*)pointerValue;
	if (coreCallSystemMessagePtr)
	{
		coreRetStr = String(IHelper::convertToString(coreCallSystemMessagePtr->get()->toDebug()));
		ret = jni_env->NewStringUTF(coreRetStr.c_str());
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPCallSystemMessage native toDebug core pointer is NULL");
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPCallSystemMessage
 * Method:    releaseCoreObjects
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPCallSystemMessage_releaseCoreObjects
(JNIEnv *, jobject javaObject)
{
	if(javaObject != NULL)
	{
		JNIEnv *jni_env = getEnv();
		jclass cls = findClass("com/openpeer/javaapi/OPCallSystemMessage");
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaObject, fid);

		delete (CallSystemMessagePtr*)pointerValue;
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPCallSystemMessage releaseCoreObjects Core object deleted.");

	}
	else
	{
		__android_log_print(ANDROID_LOG_WARN, "com.openpeer.jni", "OPCallSystemMessage releaseCoreObjects Core object not deleted - already NULL!");
	}
}

#ifdef __cplusplus
}
#endif
