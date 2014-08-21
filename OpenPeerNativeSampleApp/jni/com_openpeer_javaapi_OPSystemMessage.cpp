#include "com_openpeer_javaapi_OPSystemMessage.h"
#include "openpeer/core/ISystemMessage.h"
#include "openpeer/core/IHelper.h"
#include "OpenPeerCoreManager.h"
#include <android/log.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPSystemMessage
 * Method:    createEmptySystemMessage
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPSystemMessage_createEmptySystemMessage
  (JNIEnv *, jclass)
{
	JNIEnv *jni_env = 0;
	jstring ret;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSystemMessage native createEmptySystemMessage called");

	jni_env = getEnv();

	String coreRetStr = String(IHelper::convertToString(ISystemMessage::createEmptySystemMessage()));
	ret = jni_env->NewStringUTF(coreRetStr.c_str());

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPSystemMessage
 * Method:    getMessageType
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPSystemMessage_getMessageType
  (JNIEnv *, jclass)
{
	JNIEnv *jni_env = 0;
	jstring ret;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPSystemMessage native getMessageType called");

	jni_env = getEnv();

	String coreRetStr = String(ISystemMessage::getMessageType());
	ret = jni_env->NewStringUTF(coreRetStr.c_str());

	return ret;
}

#ifdef __cplusplus
}
#endif
