#include "com_openpeer_javaapi_OPIdentity.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"

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
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentity_coreLogin
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

		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPIdentity");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);

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
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    notifyBrowserWindowVisible
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_notifyBrowserWindowVisible
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    notifyBrowserWindowClosed
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_notifyBrowserWindowClosed
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getNextMessageForInnerBrowerWindowFrame
 * Signature: ()Lcom/openpeer/javaapi/OPElement;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentity_getNextMessageForInnerBrowerWindowFrame
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    handleMessageFromInnerBrowserWindowFrame
 * Signature: (Lcom/openpeer/javaapi/OPElement;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_handleMessageFromInnerBrowserWindowFrame
  (JNIEnv *, jobject, jobject)
{

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
