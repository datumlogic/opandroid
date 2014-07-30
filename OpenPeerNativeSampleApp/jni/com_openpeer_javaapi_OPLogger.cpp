#include "com_openpeer_javaapi_OPLogger.h"
#include "openpeer/core/ILogger.h"
#include "OpenPeerCoreManager.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    installStdOutLogger
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_installStdOutLogger
(JNIEnv *, jclass, jboolean colorizeOutput)
{
	ILogger::installStdOutLogger(colorizeOutput);
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    installFileLogger
 * Signature: (Ljava/lang/String;Z)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_installFileLogger
(JNIEnv *env, jclass, jstring fileName, jboolean colorizeOutput)
{
	const char *fileNameStr;
	fileNameStr = env->GetStringUTFChars(fileName, NULL);
	if (fileNameStr == NULL) {
		return;
	}

	ILogger::installFileLogger(fileNameStr, colorizeOutput);
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    installTelnetLogger
 * Signature: (IJZ)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_installTelnetLogger
(JNIEnv *, jclass, jint port, jlong maxSecondsWaitForSocketToBeAvailable, jboolean colorizeOutput)
{
	ILogger::installTelnetLogger(port, maxSecondsWaitForSocketToBeAvailable, colorizeOutput);
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    installOutgoingTelnetLogger
 * Signature: (Ljava/lang/String;ZLjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_installOutgoingTelnetLogger
(JNIEnv *env, jclass, jstring serverToConnect, jboolean colorizeOutput, jstring stringToSendUponConnection)
{
	const char *serverToConnectStr;
	serverToConnectStr = env->GetStringUTFChars(serverToConnect, NULL);
	if (serverToConnectStr == NULL) {
		return;
	}

	const char *stringToSendUponConnectionStr;
	stringToSendUponConnectionStr = env->GetStringUTFChars(stringToSendUponConnection, NULL);
	if (stringToSendUponConnectionStr == NULL) {
		return;
	}

	ILogger::installOutgoingTelnetLogger(serverToConnectStr, colorizeOutput, stringToSendUponConnectionStr);
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    installDebuggerLogger
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_installDebuggerLogger
(JNIEnv *, jclass)
{
	ILogger::installDebuggerLogger();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    installCustomLogger
 * Signature: (Lcom/openpeer/javaapi/OPLoggerDelegate;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_installCustomLogger
(JNIEnv *, jclass, jobject javaLoggerDelegate)
{
	if (javaLoggerDelegate == NULL)
	{
		return;
	}

	//set java delegate to identity delegate wrapper and init shared pointer for wrappers
	loggerDelegatePtr = LoggerDelegateWrapperPtr(new LoggerDelegateWrapper(javaLoggerDelegate));

	ILogger::installCustomLogger(loggerDelegatePtr);

}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    uninstallStdOutLogger
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_uninstallStdOutLogger
(JNIEnv *, jclass)
{
	ILogger::uninstallStdOutLogger();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    uninstallFileLogger
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_uninstallFileLogger
(JNIEnv *, jclass)
{
	ILogger::uninstallFileLogger();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    uninstallTelnetLogger
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_uninstallTelnetLogger
(JNIEnv *, jclass)
{
	ILogger::uninstallTelnetLogger();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    uninstallOutgoingTelnetLogger
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_uninstallOutgoingTelnetLogger
(JNIEnv *, jclass)
{
	ILogger::uninstallOutgoingTelnetLogger();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    uninstallDebuggerLogger
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_uninstallDebuggerLogger
(JNIEnv *, jclass)
{
	ILogger::uninstallDebuggerLogger();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    isTelnetLoggerListening
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPLogger_isTelnetLoggerListening
(JNIEnv *, jclass)
{
	return ILogger::isTelnetLoggerListening();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    isTelnetLoggerConnected
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPLogger_isTelnetLoggerConnected
(JNIEnv *, jclass)
{
	return ILogger::isTelnetLoggerConnected();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    isOutgoingTelnetLoggerConnected
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPLogger_isOutgoingTelnetLoggerConnected
(JNIEnv *, jclass)
{
	return ILogger::isOutgoingTelnetLoggerConnected();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    getApplicationSubsystemID
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_openpeer_javaapi_OPLogger_getApplicationSubsystemID
(JNIEnv *, jclass)
{
	return (jint) ILogger::getApplicationSubsystemID();
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    getLogLevel
 * Signature: (I)Lcom/openpeer/javaapi/OPLogLevel;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPLogger_getLogLevel
(JNIEnv *, jclass, jint subsystemUniqueId)
{
	return OpenPeerCoreManager::getJavaEnumObject("com/openpeer/javaapi/OPLogLevel", ILogger::getLogLevel(subsystemUniqueId));
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    setLogLevel
 * Signature: (Lcom/openpeer/javaapi/OPLogLevel;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_setLogLevel__Lcom_openpeer_javaapi_OPLogLevel_2
(JNIEnv *, jclass, jobject logLevel)
{
	ILogger::setLogLevel((ILogger::Level)OpenPeerCoreManager::getIntValueFromEnumObject(logLevel,"com/openpeer/javaapi/OPLogLevel"));
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    setLogLevel
 * Signature: (ILcom/openpeer/javaapi/OPLogLevel;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_setLogLevel__ILcom_openpeer_javaapi_OPLogLevel_2
(JNIEnv *, jclass, jint subsystemId, jobject logLevel)
{
	ILogger::setLogLevel(subsystemId, (ILogger::Level)OpenPeerCoreManager::getIntValueFromEnumObject(logLevel,"com/openpeer/javaapi/OPLogLevel"));
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    setLogLevel
 * Signature: (Ljava/lang/String;Lcom/openpeer/javaapi/OPLogLevel;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_setLogLevel__Ljava_lang_String_2Lcom_openpeer_javaapi_OPLogLevel_2
(JNIEnv *env, jclass, jstring subsystemName, jobject logLevel)
{
	String subsystemNameString;
	subsystemNameString = env->GetStringUTFChars(subsystemName, NULL);
	ILogger::setLogLevel(subsystemNameString, (ILogger::Level)OpenPeerCoreManager::getIntValueFromEnumObject(logLevel,"com/openpeer/javaapi/OPLogLevel"));
}

/*
 * Class:     com_openpeer_javaapi_OPLogger
 * Method:    log
 * Signature: (ILcom/openpeer/javaapi/OPLogSeverity;Lcom/openpeer/javaapi/OPLogLevel;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPLogger_log
(JNIEnv *env, jclass,
		jint subsystemUniqueID,
		jobject severity,
		jobject logLevel,
		jstring message,
		jstring function,
		jstring filePath,
		jlong lineNumber)
{

	String messageString = env->GetStringUTFChars(message, NULL);
	String functionString = env->GetStringUTFChars(function, NULL);
	String filePathString = env->GetStringUTFChars(filePath, NULL);

	ILogger::log(subsystemUniqueID,
			(ILogger::Severity)OpenPeerCoreManager::getIntValueFromEnumObject(severity, "com/openpeer/javaapi/OPLogSeverity"),
			(ILogger::Level)OpenPeerCoreManager::getIntValueFromEnumObject(logLevel, "com/openpeer/javaapi/OPLogLevel"),
			messageString,
			functionString,
			filePathString,
			lineNumber);
}

#ifdef __cplusplus
}
#endif
