#include "openpeer/core/ISettings.h"
#include "openpeer/core/ILogger.h"
#include <android/log.h>;

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
 * Method:    setString
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setString
(JNIEnv *env , jclass, jstring key, jstring value)
{
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

	const char *valueStr;
	valueStr = env->GetStringUTFChars(value, NULL);
	if (valueStr == NULL) {
		return;
	}

	ISettings::setString(keyStr, valueStr);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setInt
 * Signature: (Ljava/lang/String;J)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setInt
(JNIEnv *env, jclass, jstring key, jlong value)
{
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

	ISettings::setInt(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setUInt
 * Signature: (Ljava/lang/String;J)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setUInt
(JNIEnv *env, jclass, jstring key, jlong value)
{
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

	ISettings::setUInt(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setBool
 * Signature: (Ljava/lang/String;Z)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setBool
(JNIEnv *env, jclass, jstring key, jboolean value)
{
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

	ISettings::setBool(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setFloat
 * Signature: (Ljava/lang/String;F)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setFloat
(JNIEnv *env, jclass, jstring key, jfloat value)
{
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

	ISettings::setFloat(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    setDouble
 * Signature: (Ljava/lang/String;D)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_setDouble
(JNIEnv *env, jclass, jstring key, jdouble value)
{
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

	ISettings::setDouble(keyStr, value);
}

/*
 * Class:     com_openpeer_javaapi_OPSettings
 * Method:    clear
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_clear
(JNIEnv *env, jclass, jstring key)
{
	const char *keyStr;
	keyStr = env->GetStringUTFChars(key, NULL);
	if (keyStr == NULL) {
		return;
	}

	ISettings::clear(keyStr);
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

JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPSettings_redirectLog(JNIEnv* env, jclass cls)
{
    int pipes[2];
    pipe(pipes);
    dup2(pipes[1], STDERR_FILENO);
    close(pipes[1]);
    FILE *inputFile = fdopen(pipes[0], "r");
    char readBuffer[256];
    while (1) {
        fgets(readBuffer, sizeof(readBuffer), inputFile);
        __android_log_write(2, "stderr", readBuffer);
    }
}

#ifdef __cplusplus
}
#endif
