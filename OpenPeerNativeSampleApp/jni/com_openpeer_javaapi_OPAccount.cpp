/*
 * com_openpeer_javaapi_OPAccount.cpp
 *
 *  Created on: Sep 18, 2013
 *      Author: appl
 */

#include "com_openpeer_javaapi_OPAccount.h"
#include "openpeer/core/IAccount.h"
#include "openpeer/core/IIdentity.h"
#include "openpeer/core/IHelper.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/AccountStates;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_toString
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    toDebugString
 * Signature: (Lcom/openpeer/javaapi/OPAccount;Z)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_toDebugString
(JNIEnv *, jclass, jobject, jboolean)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    login
 * Signature: (Lcom/openpeer/javaapi/OPAccountDelegate;Lcom/openpeer/javaapi/OPConversationThreadDelegate;Lcom/openpeer/javaapi/OPCallDelegate;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Lcom/openpeer/javaapi/OPAccount
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPAccount_login
(JNIEnv *env, jclass, jobject, jobject, jobject,
		jstring namespaceGrantOuterFrameURLUponReload,
		jstring grantID,
		jstring lockboxServiceDomain,
		jboolean forceCreateNewLockboxAccount)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	const char *namespaceGrantOuterFrameURLUponReloadStr;
	namespaceGrantOuterFrameURLUponReloadStr = env->GetStringUTFChars(namespaceGrantOuterFrameURLUponReload, NULL);
	if (namespaceGrantOuterFrameURLUponReloadStr == NULL) {
		return object;
	}

	const char *grantIDStr;
	grantIDStr = env->GetStringUTFChars(grantID, NULL);
	if (grantIDStr == NULL) {
		return object;
	}

	const char *lockboxServiceDomainStr;
	lockboxServiceDomainStr = env->GetStringUTFChars(lockboxServiceDomain, NULL);
	if (lockboxServiceDomainStr == NULL) {
		return object;
	}

	accountPtr = IAccount::login(globalEventManager, globalEventManager, globalEventManager,
			namespaceGrantOuterFrameURLUponReloadStr, grantIDStr, lockboxServiceDomainStr, forceCreateNewLockboxAccount);

	if (accountPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPAccount");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);

		}

	}
	return object;

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    relogin
 * Signature: (Lcom/openpeer/javaapi/OPAccountDelegate;Lcom/openpeer/javaapi/OPConversationThreadDelegate;Lcom/openpeer/javaapi/OPCallDelegate;Ljava/lang/String;Ljava/lang/String;)Lcom/openpeer/javaapi/OPAccount;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPAccount_relogin
(JNIEnv *env, jclass, jobject, jobject, jobject,
		jstring namespaceGrantOuterFrameURLUponReload,
		jstring reloginInformation)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	const char *namespaceGrantOuterFrameURLUponReloadStr;
	namespaceGrantOuterFrameURLUponReloadStr = env->GetStringUTFChars(namespaceGrantOuterFrameURLUponReload, NULL);
	if (namespaceGrantOuterFrameURLUponReloadStr == NULL) {
		return object;
	}

	const char *reloginInformationStr;
	reloginInformationStr = env->GetStringUTFChars(reloginInformation, NULL);
	if (reloginInformationStr == NULL) {
		return object;
	}


	ElementPtr reloginElement = IHelper::createElement(reloginInformationStr);
	accountPtr = IAccount::relogin(globalEventManager, globalEventManager, globalEventManager,
			namespaceGrantOuterFrameURLUponReloadStr, reloginElement);

	if (accountPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPAccount");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);

		}

	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getStableID
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getStableID
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getState
 * Signature: (ILjava/lang/String;)Lcom/openpeer/javaapi/AccountStates;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPAccount_getState
(JNIEnv *, jobject, jint, jstring)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getReloginInformation
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getReloginInformation
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getLocationID
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getLocationID
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_shutdown
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getPeerFilePrivate
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getPeerFilePrivate
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getPeerFilePrivateSecret
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_openpeer_javaapi_OPAccount_getPeerFilePrivateSecret
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getAssociatedIdentities
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPAccount_getAssociatedIdentities
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    removeIdentities
 * Signature: (Ljava/util/List;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_removeIdentities
(JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getInnerBrowserWindowFrameURL
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getInnerBrowserWindowFrameURL
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    notifyBrowserWindowVisible
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_notifyBrowserWindowVisible
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    notifyBrowserWindowClosed
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_notifyBrowserWindowClosed
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getNextMessageForInnerBrowerWindowFrame
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getNextMessageForInnerBrowerWindowFrame
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    handleMessageFromInnerBrowserWindowFrame
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_handleMessageFromInnerBrowserWindowFrame
(JNIEnv *, jobject, jstring)
{

}

#ifdef __cplusplus
}
#endif


