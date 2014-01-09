#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "../../../opios/libs/op/openpeer/core/IStack.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    setup
 * Signature: (Lcom/openpeer/javaapi/OPStackDelegate;Lcom/openpeer/javaapi/OPMediaEngineDelegate;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStack_setup
  (JNIEnv *env, jobject, jobject, jobject, jstring appID, jstring appName, jstring appImageURL, jstring appURL, jstring userAgent, jstring deviceID, jstring os, jstring system)
{
	const char *appIDStr;
	appIDStr = env->GetStringUTFChars(appID, NULL);
	if (appIDStr == NULL) {
		return;
	}

	const char *appNameStr;
	appNameStr = env->GetStringUTFChars(appName, NULL);
	if (appNameStr == NULL) {
		return;
	}

	const char *appImageURLStr;
	appImageURLStr = env->GetStringUTFChars(appImageURL, NULL);
	if (appImageURLStr == NULL) {
		return;
	}

	const char *appURLStr;
	appURLStr = env->GetStringUTFChars(appURL, NULL);
	if (appURLStr == NULL) {
		return;
	}

	const char *userAgentStr;
	userAgentStr = env->GetStringUTFChars(userAgent, NULL);
	if (userAgentStr == NULL) {
		return;
	}

	const char *deviceIDStr;
	deviceIDStr = env->GetStringUTFChars(deviceID, NULL);
	if (deviceIDStr == NULL) {
		return;
	}

	const char *osStr;
	osStr = env->GetStringUTFChars(os, NULL);
	if (osStr == NULL) {
		return;
	}

	const char *systemStr;
	systemStr = env->GetStringUTFChars(system, NULL);
	if (systemStr == NULL) {
		return;
	}

	IStackPtr stack = IStack::singleton();
	stack->setup(globalEventManager, globalEventManager, (char const *)appIDStr,
			(char const *)appNameStr, (char const *)appImageURLStr, (char const *)appURLStr, (char const *)userAgentStr,
			(char const *)deviceIDStr, (char const *)osStr, (char const *)systemStr);
}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStack_shutdown
  (JNIEnv *, jobject)
{
	IStackPtr stack = IStack::singleton();
	stack->shutdown();
}

#ifdef __cplusplus
}
#endif
