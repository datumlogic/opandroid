#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    setup
 * Signature: (Lcom/openpeer/javaapi/OPStackDelegate;Lcom/openpeer/javaapi/OPMediaEngineDelegate;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStack_setup
(JNIEnv *env, jobject, jobject, jobject)
{

	stackPtr = IStack::singleton();
	stackPtr->setup(globalEventManager, globalEventManager);
}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStack_shutdown
(JNIEnv *, jobject)
{
	if (stackPtr)
	{
		stackPtr->shutdown();
	}

}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    createAuthorizedApplicationID
 * Signature: (Ljava/lang/String;Ljava/lang/String;Landroid/text/format/Time;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPStack_createAuthorizedApplicationID
(JNIEnv *env, jclass, jstring applicationID, jstring applicationIDSharedSecret, jobject expires)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring authorizedApplicationID;

	String applicationIDString;
	applicationIDString = env->GetStringUTFChars(applicationID, NULL);
	if (applicationIDString == NULL) {
		return authorizedApplicationID;
	}

	String applicationIDSharedSecretString;
	applicationIDSharedSecretString = env->GetStringUTFChars(applicationIDSharedSecret, NULL);
	if (applicationIDSharedSecretString == NULL) {
		return authorizedApplicationID;
	}
	jni_env = getEnv();
	cls = findClass("android/text/format/Time");
	jmethodID timeMethodID   = env->GetMethodID(cls, "toMillis", "(Z)J");
	long longValue = (long) env->CallIntMethod(expires, timeMethodID, false);
	Time t = boost::posix_time::from_time_t(longValue/1000) + boost::posix_time::millisec(longValue % 1000);


	authorizedApplicationID =  env->NewStringUTF(IStack::createAuthorizedApplicationID(applicationIDString, applicationIDSharedSecretString, t).c_str());

	return authorizedApplicationID;
}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    getAuthorizedApplicationIDExpiry
 * Signature: (Ljava/lang/String;Landroid/text/format/Time;)Landroid/text/format/Time;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPStack_getAuthorizedApplicationIDExpiry
(JNIEnv *, jclass, jstring, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    isAuthorizedApplicationIDExpiryWindowStillValid
 * Signature: (Ljava/lang/String;Landroid/text/format/Time;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPStack_isAuthorizedApplicationIDExpiryWindowStillValid
(JNIEnv *, jclass, jstring, jobject)
{

}

#ifdef __cplusplus
}
#endif
