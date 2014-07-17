#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IContact.h"
#include "openpeer/core/IHelper.h"
#include "openpeer/core/ILogger.h"
#include "OpenPeerCoreManager.h"

#include <android/log.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPContact
 * Method:    toDebugString
 * Signature: (Lcom/openpeer/javaapi/OPContact;Z)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPContact_toDebugString
(JNIEnv *, jclass, jobject, jboolean)
{

}

/*
 * Class:     com_openpeer_javaapi_OPContact
 * Method:    createFromPeerFilePublic
 * Signature: (Lcom/openpeer/javaapi/OPAccount;Ljava/lang/String;)Lcom/openpeer/javaapi/OPContact;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPContact_createFromPeerFilePublic
(JNIEnv *env, jclass, jobject account, jstring peerFilePublic)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	IContactPtr contactPtr;

	const char *peerFilePublicStr;
	peerFilePublicStr = env->GetStringUTFChars(peerFilePublic, NULL);
	if (peerFilePublicStr == NULL) {
		return object;
	}

	if(OpenPeerCoreManager::accountPtr)
	{
		ElementPtr peerFileElement = IHelper::createElement(peerFilePublicStr);
		contactPtr = IContact::createFromPeerFilePublic(OpenPeerCoreManager::accountPtr, IHelper::createPeerFilePublic(peerFileElement));
	}

	if(contactPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPContact");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);
			//contactMap.insert(std::pair<jobject, IContactPtr>(object, contactPtr));

			IContactPtr* ptrToContact = new boost::shared_ptr<IContact>(contactPtr);
			jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
			jlong contact = (jlong) ptrToContact;
			jni_env->SetLongField(object, fid, contact);

			__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni",
					"CorePtr raw = %p, ptr as long = %Lu",contactPtr.get(), contact);

			//OpenPeerCoreManager::coreContactList.push_back(contactPtr);

		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPContact
 * Method:    getForSelf
 * Signature: (Lcom/openpeer/javaapi/OPAccount;)Lcom/openpeer/javaapi/OPContact;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPContact_getForSelf
(JNIEnv *, jclass, jobject)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	IContactPtr contactPtr;

	if(OpenPeerCoreManager::accountPtr)
	{
		contactPtr = IContact::getForSelf(OpenPeerCoreManager::accountPtr);
	}

	if(contactPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPContact");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);

			IContactPtr* ptrToContact = new boost::shared_ptr<IContact>(contactPtr);
			jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
			jlong contact = (jlong) ptrToContact;
			jni_env->SetLongField(object, fid, contact);

			__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni",
					"CorePtr raw = %p, ptr as long = %Lu",contactPtr.get(), contact);

			//OpenPeerCoreManager::coreContactList.push_back(contactPtr);

		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPContact
 * Method:    getStableID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPContact_getStableID
(JNIEnv *, jobject owner)
{
	jlong ret = 0;
	JNIEnv * jni_env = 0;
	jni_env = getEnv();
	jclass contactClass = findClass("com/openpeer/javaapi/OPContact");
	jfieldID contactfid = jni_env->GetFieldID(contactClass, "nativeClassPointer", "J");
	jlong contactPointerValue = jni_env->GetLongField(owner, contactfid);

	IContactPtr* contactPtr = (IContactPtr*)contactPointerValue;
	if (contactPtr)
	{
		ret = contactPtr->get()->getID();
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPContact
 * Method:    isSelf
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPContact_isSelf
(JNIEnv *, jobject owner)
{
	jboolean ret;
	JNIEnv * jni_env = 0;
	jni_env = getEnv();
	jclass contactClass = findClass("com/openpeer/javaapi/OPContact");
	jfieldID contactfid = jni_env->GetFieldID(contactClass, "nativeClassPointer", "J");
	jlong contactPointerValue = jni_env->GetLongField(owner, contactfid);

	IContactPtr* contactPtr = (IContactPtr*)contactPointerValue;
	if (contactPtr)
	{
		ret = contactPtr->get()->isSelf();
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPContact
 * Method:    getPeerURI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPContact_getPeerURI
(JNIEnv *, jobject owner)
{
	jstring ret;
	JNIEnv * jni_env = 0;
	jni_env = getEnv();
	jclass contactClass = findClass("com/openpeer/javaapi/OPContact");
	jfieldID contactfid = jni_env->GetFieldID(contactClass, "nativeClassPointer", "J");
	jlong contactPointerValue = jni_env->GetLongField(owner, contactfid);

	IContactPtr* contactPtr = (IContactPtr*)contactPointerValue;
	if (contactPtr)
	{
		ret = jni_env->NewStringUTF(contactPtr->get()->getPeerURI().c_str());
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPContact
 * Method:    getPeerFilePublic
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPContact_getPeerFilePublic
(JNIEnv *env, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	jstring ret;
	JNIEnv * jni_env = 0;

	jni_env = getEnv();
	jclass contactClass = findClass("com/openpeer/javaapi/OPContact");
	jfieldID contactfid = jni_env->GetFieldID(contactClass, "nativeClassPointer", "J");
	jlong contactPointerValue = jni_env->GetLongField(owner, contactfid);

	IContactPtr* contactPtr = (IContactPtr*)contactPointerValue;
	if (contactPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			ElementPtr peerFilePublic = IHelper::convertToElement(contactPtr->get()->getPeerFilePublic());
			ret = env->NewStringUTF(IHelper::convertToString(peerFilePublic).c_str());

			//TODO export peer file public to ElementPtr and then convert to String
			jclass peerFileCls = findClass("com/openpeer/javaapi/OPPeerFilePublic");
			jmethodID peerFileMethodID = jni_env->GetMethodID(peerFileCls, "<init>", "()V");
			jobject peerFileObject = jni_env->NewObject(peerFileCls, peerFileMethodID);
		}
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPContact
 * Method:    getAssociatedAccount
 * Signature: ()Lcom/openpeer/javaapi/OPAccount;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPContact_getAssociatedAccount
(JNIEnv *, jobject)
{
	//TODO This should not be native call. Current limitation is single account at the time, so it should return OPaccount object
	return (jobject) OpenPeerCoreManager::accountPtr.get();
}

/*
 * Class:     com_openpeer_javaapi_OPContact
 * Method:    hintAboutLocation
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPContact_hintAboutLocation
(JNIEnv *, jobject owner, jstring locationId)
{
	JNIEnv * jni_env = 0;

	jni_env = getEnv();
	const char *locationIdStr;
	locationIdStr = jni_env->GetStringUTFChars(locationId, NULL);
	if (locationIdStr == NULL) {
		return;
	}

	jclass contactClass = findClass("com/openpeer/javaapi/OPContact");
	jfieldID contactfid = jni_env->GetFieldID(contactClass, "nativeClassPointer", "J");
	jlong contactPointerValue = jni_env->GetLongField(owner, contactfid);

	IContactPtr* contactPtr = (IContactPtr*)contactPointerValue;
	if (contactPtr)
	{
		contactPtr->get()->hintAboutLocation(locationIdStr);
	}
}

#ifdef __cplusplus
}
#endif
