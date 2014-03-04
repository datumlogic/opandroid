#include "openpeer/core/ISettings.h"
#include "openpeer/core/ILogger.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setup
 * Signature: (Lcom/openpeer/javaapi/OPSettingsDelegate;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setup
  (JNIEnv *, jclass, jobject)
{
	ISettings::setup(settingsDelegatePtr);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    apply
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPSettings_apply
  (JNIEnv *env, jclass, jstring jsonSettings)
{
	const char *jsonSettingsStr;
	jsonSettingsStr = env->GetStringUTFChars(jsonSettings, NULL);
	if (jsonSettingsStr == NULL) {
		return false;
	}

	return ISettings::apply(jsonSettingsStr);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    applyDefaults
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_applyDefaults
  (JNIEnv *, jclass)
{
	ISettings::applyDefaults();
}

#ifdef __cplusplus
}
#endif
