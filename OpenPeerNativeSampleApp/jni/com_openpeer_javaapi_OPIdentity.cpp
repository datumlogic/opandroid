#include "com_openpeer_javaapi_OPIdentity.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"
#include "openpeer/core/IHelper.h"
#include <android/log.h>

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
		jmethodID timeMethodID   = jni_env->GetMethodID(cls, "toMillis", "(Z)J");
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
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	int state = 0;
	unsigned short int outErrorCode;
	String outErrorReason;

	if (identityPtr)
	{
		state = (int) identityPtr->getState(&outErrorCode, &outErrorReason);

		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/IdentityStates");
			method = jni_env->GetMethodID(cls, "<init>", "(I)V");
			object = jni_env->NewObject(cls, method, state);

		}
	}


	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getStableID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPIdentity_getStableID
(JNIEnv *, jobject)
{
	jlong pid = 0;

	if (identityPtr)
	{
		pid = identityPtr->getID();
	}

	return pid;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    isDelegateAttached
 * Signature: ()Z;
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPIdentity_isDelegateAttached
(JNIEnv *, jobject)
{
	return identityPtr->isDelegateAttached();
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    attachDelegate
 * Signature: (Lcom/openpeer/javaapi/OPIdentityDelegate;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_attachDelegate
(JNIEnv *env, jobject, jobject, jstring outerFrameURLUponReload)
{

	const char *outerFrameURLUponReloadStr;
	outerFrameURLUponReloadStr = env->GetStringUTFChars(outerFrameURLUponReload, NULL);
	if (outerFrameURLUponReloadStr == NULL) {
		return;
	}

	if(identityPtr)
	{
		identityPtr->attachDelegate(globalEventManager, outerFrameURLUponReloadStr);
	}
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    attachDelegateAndPreauthorizedLogin
 * Signature: (Lcom/openpeer/javaapi/OPIdentityDelegate;Ljava/lang/String;Ljava/lang/String;Landroid/text/format/Time;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_attachDelegateAndPreauthorizedLogin
(JNIEnv *env, jobject, jobject,
		jstring identityAccessToken,
		jstring identityAccessSecret,
		jobject identityAccessSecretExpires)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	const char *identityAccessTokenStr;
	identityAccessTokenStr = env->GetStringUTFChars(identityAccessToken, NULL);
	if (identityAccessTokenStr == NULL) {
		return;
	}

	const char *identityAccessSecretStr;
	identityAccessSecretStr = env->GetStringUTFChars(identityAccessSecret, NULL);
	if (identityAccessSecretStr == NULL) {
		return;
	}

	//todo fix time
	Time t;
	jni_env = getEnv();

	cls = findClass("android/text/format/Time");
	if(jni_env->IsInstanceOf(identityAccessSecretExpires, cls) == JNI_TRUE)
	{
		jmethodID timeMethodID   = jni_env->GetMethodID(cls, "toMillis", "(Z)J");
		long longValue = (long) jni_env->CallIntMethod(identityAccessSecretExpires, timeMethodID, false);
		t = boost::posix_time::from_time_t(longValue/1000) + boost::posix_time::millisec(longValue % 1000);
	}

	if(identityPtr)
	{
		identityPtr->attachDelegateAndPreauthorizedLogin(
				globalEventManager,
				(char const *)identityAccessTokenStr,
				(char const *)identityAccessSecretStr,
				t);
	}
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getIdentityURI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPIdentity_getIdentityURI
(JNIEnv *env, jobject)
{
	jstring identityURI;
	if (identityPtr)
	{

		identityURI =  env->NewStringUTF(identityPtr->getIdentityURI().c_str());
	}

	return identityURI;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getIdentityProviderDomain
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPIdentity_getIdentityProviderDomain
(JNIEnv *env, jobject)
{
	jstring identityProviderDomain;
	if (identityPtr)
	{

		identityProviderDomain =  env->NewStringUTF(identityPtr->getIdentityProviderDomain().c_str());
	}

	return identityProviderDomain;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getSelfIdentityContact
 * Signature: ()Lcom/openpeer/javaapi/OPContact;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentity_getSelfIdentityContact
(JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	IdentityContact coreContact;
	if(identityPtr)
	{
		identityPtr->getSelfIdentityContact(coreContact);

	}
	jni_env = getEnv();
	if(jni_env)
	{
		cls = findClass("com/openpeer/javaapi/OPIdentityContact");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		object = jni_env->NewObject(cls, method);

		//set Stable ID to OPIdentityContact
		method = jni_env->GetMethodID(cls, "setStableID", "(Ljava/lang/String;)V");
		jstring stableID =  jni_env->NewStringUTF(coreContact.mStableID.c_str());
		jni_env->CallVoidMethod(cls, method, stableID);

		//set Public Peer File to OPIdentityContact
		//TODO export peer file public to ElementPtr and then convert to String
		jclass peerFileCls = findClass("com/openpeer/javaapi/OPPeerFilePublic");
		jmethodID peerFileMethodID = jni_env->GetMethodID(peerFileCls, "<init>", "()V");
		jobject peerFileObject = jni_env->NewObject(peerFileCls, peerFileMethodID);
		method = jni_env->GetMethodID(cls, "setPeerFilePublic", "(Lcom/openpeer/javaapi/OPPeerFilePublic)V");
		jni_env->CallVoidMethod(cls, method, peerFileObject);

		//set IdentityProofBundle to OPIdentityContact
		method = jni_env->GetMethodID(cls, "setIdentityProofBundleEl", "(Ljava/lang/String;)V");
		jstring identityProofBundle =  jni_env->NewStringUTF(IHelper::convertToString(coreContact.mIdentityProofBundleEl).c_str());
		jni_env->CallVoidMethod(cls, method, identityProofBundle);

		//set Priority to OPIdentityContact
		method = jni_env->GetMethodID(cls, "setPriority", "(I)V");
		jni_env->CallVoidMethod(cls, method, (int)coreContact.mPriority);

		//set Weight to OPIdentityContact
		method = jni_env->GetMethodID(cls, "setWeight", "(I)V");
		jni_env->CallVoidMethod(cls, method, (int)coreContact.mWeight);

		//Convert and set time from C++ to Android; Fetch methods needed to accomplish this
		Time time_t_epoch = boost::posix_time::time_from_string("1970-01-01 00:00:00.000");
		jclass timeCls = findClass("android/text/format/Time");
		jmethodID timeMethodID = jni_env->GetMethodID(timeCls, "<init>", "()V");
		jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(Z)V");

		//calculate and set Last Updated
		zsLib::Duration lastUpdated = coreContact.mLastUpdated - time_t_epoch;
		jobject timeLastUpdatedObject = jni_env->NewObject(timeCls, timeMethodID);
		jni_env->CallVoidMethod(timeLastUpdatedObject, timeSetMillisMethodID, lastUpdated.total_milliseconds());
		//Time has been converted, now call OPIdentityContact setter
		method = jni_env->GetMethodID(cls, "setLastUpdated", "(Landroid/text/format/Time;)V");
		jni_env->CallVoidMethod(cls, method, timeLastUpdatedObject);

		//calculate and set Expires
		zsLib::Duration expires = coreContact.mExpires - time_t_epoch;
		jobject timeExpiresObject = jni_env->NewObject(peerFileCls, peerFileMethodID);
		jni_env->CallVoidMethod(timeExpiresObject, timeSetMillisMethodID, expires.total_milliseconds());
		//Time has been converted, now call OPIdentityContact setter
		method = jni_env->GetMethodID(cls, "setLastUpdated", "(Landroid/text/format/Time;)V");
		jni_env->CallVoidMethod(cls, method, timeExpiresObject);

	}
	return object;
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
 * Method:    startRolodexDownload
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_startRolodexDownload
(JNIEnv *env, jobject, jstring inLastDownloadedVersion)
{
	if (identityPtr)
	{
		String inLastDownloadedVersionString;
		inLastDownloadedVersionString = env->GetStringUTFChars(inLastDownloadedVersion, NULL);
		if (inLastDownloadedVersionString == NULL) {

			identityPtr->startRolodexDownload();

		}
		else
		{

			identityPtr->startRolodexDownload(inLastDownloadedVersionString);
		}
	}
	else
	{
		__android_log_write(ANDROID_LOG_ERROR, "com.openpeer.jni", "IdentityPtr is NULL");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    refreshRolodexContacts
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentity_refreshRolodexContacts
(JNIEnv *, jobject)
{
	if (identityPtr)
	{
		identityPtr->refreshRolodexContacts();
	}
	else
	{
		__android_log_write(ANDROID_LOG_ERROR, "com.openpeer.jni", "IdentityPtr is NULL");
	}

}

/*
 * Class:     com_openpeer_javaapi_OPIdentity
 * Method:    getDownloadedRolodexContacts
 * Signature: (ZLjava/lang/String;Ljava/util/List;)Z;
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPIdentity_getDownloadedRolodexContacts
(JNIEnv *, jobject, jboolean, jstring, jobject)
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
	if (identityPtr)
	{
		identityPtr->cancel();
	}
}

#ifdef __cplusplus
}
#endif
