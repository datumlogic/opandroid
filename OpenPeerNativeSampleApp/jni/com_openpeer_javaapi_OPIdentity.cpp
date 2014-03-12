#include "com_openpeer_javaapi_OPIdentity.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"
#include "openpeer/core/IHelper.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/IdentityStates;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPIdentity_toString
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    toDebugString
 * Signature: (Lcom/openpeer/javaapi/OPIdentity;Ljava/lang/Boolean;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPIdentity_toDebugString
(JNIEnv *, jclass, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    login
 * Signature: (Lcom/openpeer/javaapi/OPAccount;Lcom/openpeer/javaapi/OPIdentityDelegate;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/openpeer/javaapi/OPIdentity;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentity_login
(JNIEnv *env, jclass, jobject, jobject, jstring identityProviderDomain, jstring identityURI_or_identityBaseURI, jstring outerFrameURLUponReload)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	const char *identityProviderDomainStr;
	identityProviderDomainStr = env->GetStringUTFChars(identityProviderDomain, NULL);
	if (identityProviderDomainStr == NULL) {
		return object;
	}

	const char *identityURIStr;
	identityURIStr = env->GetStringUTFChars(identityURI_or_identityBaseURI, NULL);
	if (identityURIStr == NULL) {
		return object;
	}

	const char *outerFrameURLUponReloadStr;
	outerFrameURLUponReloadStr = env->GetStringUTFChars(outerFrameURLUponReload, NULL);
	if (outerFrameURLUponReloadStr == NULL) {
		return object;
	}

	identityPtr = IIdentity::login(accountPtr, globalEventManager, (char const *)identityProviderDomainStr,
			(char const *)identityURIStr, (char const *)outerFrameURLUponReloadStr);

	if(identityPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPIdentity");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);

		}
	}
	return object;

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    loginWithIdentityPreauthorized
 * Signature: (Lcom/openpeer/javaapi/OPAccount;Lcom/openpeer/javaapi/OPIdentityDelegate;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/text/format/Time;)Lcom/openpeer/javaapi/OPIdentity;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentity_loginWithIdentityPreauthorized
(JNIEnv *env, jclass, jobject, jobject,
		jstring identityProviderDomain,
		jstring identityURI,
		jstring identityAccessToken,
		jstring identityAccessSecret,
		jobject identityAccessSecretExpires)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	const char *identityProviderDomainStr;
	identityProviderDomainStr = env->GetStringUTFChars(identityProviderDomain, NULL);
	if (identityProviderDomainStr == NULL) {
		return object;
	}

	const char *identityURIStr;
	identityURIStr = env->GetStringUTFChars(identityURI, NULL);
	if (identityURIStr == NULL) {
		return object;
	}

	const char *identityAccessTokenStr;
	identityAccessTokenStr = env->GetStringUTFChars(identityAccessToken, NULL);
	if (identityAccessTokenStr == NULL) {
		return object;
	}

	const char *identityAccessSecretStr;
	identityAccessSecretStr = env->GetStringUTFChars(identityAccessSecret, NULL);
	if (identityAccessSecretStr == NULL) {
		return object;
	}

	//todo fix time
	Time t;
	jni_env = getEnv();

	cls = findClass("android/text/format/Time");
	if(jni_env->IsInstanceOf(identityAccessSecretExpires, cls) == JNI_TRUE)
	{
		jmethodID timeMethodID   = jni_env->GetMethodID(cls, "toMillis", "(B)J");
		long longValue = (long) jni_env->CallIntMethod(identityAccessSecretExpires, timeMethodID, false);
		t = boost::posix_time::from_time_t(longValue/1000) + boost::posix_time::millisec(longValue % 1000);
	}

	identityPtr = IIdentity::loginWithIdentityPreauthorized(accountPtr,
			globalEventManager,
			(char const *)identityProviderDomainStr,
			(char const *)identityURIStr,
			(char const *)identityAccessTokenStr,
			(char const *)identityAccessSecretStr,
			t);

	if(identityPtr)
	{
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPIdentity");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);

		}
	}
	return object;

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getState
 * Signature: (ILjava/lang/String;)Lcom/openpeer/javaapi/IdentityStates;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentity_getState
(JNIEnv *, jobject, jint, jstring)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPIdentity_getID
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    isDelegateAttached
 * Signature: ()Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentity_isDelegateAttached
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    attachDelegate
 * Signature: (Lcom/openpeer/javaapi/OPIdentityDelegate;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_attachDelegate
(JNIEnv *, jobject, jobject, jstring)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getIdentityURI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPIdentity_getIdentityURI
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getIdentityProviderDomain
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPIdentity_getIdentityProviderDomain
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getSignedIdentityBundle
 * Signature: ()Lcom/openpeer/javaapi/OPElement;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentity_getSignedIdentityBundle
(JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getInnerBrowserWindowFrameURL
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPIdentity_getInnerBrowserWindowFrameURL
(JNIEnv *env, jobject)
{
	String innerBrowserWindowFrameURLString;
	jstring innerBrowserWindowFrameURL;


	if (identityPtr)
	{
		innerBrowserWindowFrameURLString = identityPtr->getInnerBrowserWindowFrameURL();

		innerBrowserWindowFrameURL =  env->NewStringUTF(innerBrowserWindowFrameURLString.c_str());
	}

	return innerBrowserWindowFrameURL;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    notifyBrowserWindowVisible
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_notifyBrowserWindowVisible
(JNIEnv *, jobject)
{
	if (identityPtr)
	{
		identityPtr->notifyBrowserWindowVisible();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    notifyBrowserWindowClosed
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_notifyBrowserWindowClosed
(JNIEnv *, jobject)
{
	if (identityPtr)
	{
		identityPtr->notifyBrowserWindowClosed();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getNextMessageForInnerBrowerWindowFrame
 * Signature: ()Lcom/openpeer/javaapi/OPElement;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPIdentity_getNextMessageForInnerBrowerWindowFrame
(JNIEnv *env, jobject)
{
	ElementPtr nextMessageForInnerBrowerWindowFrameElement;
	jstring nextMessageForInnerBrowerWindowFrame;


	if (identityPtr)
	{
		nextMessageForInnerBrowerWindowFrameElement = identityPtr->getNextMessageForInnerBrowerWindowFrame();

		nextMessageForInnerBrowerWindowFrame =  env->NewStringUTF(IHelper::convertToString(nextMessageForInnerBrowerWindowFrameElement).c_str());
	}

	return nextMessageForInnerBrowerWindowFrame;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    handleMessageFromInnerBrowserWindowFrame
 * Signature: (Lcom/openpeer/javaapi/OPElement;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_handleMessageFromInnerBrowserWindowFrame
(JNIEnv *env, jobject, jstring unparsedMessage)
{
	String unparsedMessageString;
	unparsedMessageString = env->GetStringUTFChars(unparsedMessage, NULL);
	if (unparsedMessageString == NULL) {
		return;
	}

	if (identityPtr)
	{
		identityPtr->handleMessageFromInnerBrowserWindowFrame(IHelper::createElement(unparsedMessageString));
	}
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    cancel
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_cancel
(JNIEnv *, jobject)
{

}

#ifdef __cplusplus
}
#endif
