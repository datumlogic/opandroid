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
#include "OpenPeerCoreManager.h"

#include <android/log.h>;

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

	OpenPeerCoreManager::accountPtr = IAccount::login(globalEventManager, globalEventManager, globalEventManager,
			namespaceGrantOuterFrameURLUponReloadStr, grantIDStr, lockboxServiceDomainStr, forceCreateNewLockboxAccount);

	if (OpenPeerCoreManager::accountPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPAccount");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);
			globalAccount = object;

			jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
			jlong acc = (jlong) OpenPeerCoreManager::accountPtr.get();
			jni_env->SetLongField(object, fid, acc);

			__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni",
					"CorePtr raw = %p, ptr as long = %Lu",OpenPeerCoreManager::accountPtr.get(), acc);

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
	OpenPeerCoreManager::accountPtr = IAccount::relogin(globalEventManager, globalEventManager, globalEventManager,
			namespaceGrantOuterFrameURLUponReloadStr, reloginElement);

	if (OpenPeerCoreManager::accountPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPAccount");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);
			globalAccount = object;

			jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
			jlong acc = (jlong) OpenPeerCoreManager::accountPtr.get();
			jni_env->SetLongField(object, fid, acc);

			__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni",
					"CorePtr raw = %p, ptr as long = %Lu",OpenPeerCoreManager::accountPtr.get(), acc);

		}

	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getStableID
 * Signature: ()J;
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPAccount_getStableID
(JNIEnv *env, jobject obj)
{
	jlong pid = 0;

	if (OpenPeerCoreManager::accountPtr)
	{
		jclass cls = findClass("com/openpeer/javaapi/OPAccount");
		jfieldID fid = env->GetFieldID(cls, "nativeClassPointer", "J");


		jlong acc = env->GetLongField(obj, fid);

		if (acc == (jlong)OpenPeerCoreManager::accountPtr.get())
		{
			pid = OpenPeerCoreManager::accountPtr->getID();
		}
		__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni",
				"CorePtr raw = %p, java ptr as long = %Lu",OpenPeerCoreManager::accountPtr.get(), acc);
	}

	return pid;

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getState
 * Signature: (ILjava/lang/String;)Lcom/openpeer/javaapi/AccountStates;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPAccount_getState
(JNIEnv *, jobject, jint, jstring)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	int state = 0;
	unsigned short int outErrorCode;
	String outErrorReason;

	if (OpenPeerCoreManager::accountPtr)
	{
		state = (int) OpenPeerCoreManager::accountPtr->getState(&outErrorCode, &outErrorReason);

		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/AccountStates");
			method = jni_env->GetMethodID(cls, "<init>", "(I)V");
			object = jni_env->NewObject(cls, method, state);

		}
	}


	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getReloginInformation
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getReloginInformation
(JNIEnv *env , jobject)
{
	ElementPtr reloginInfoElement;
	jstring reloginInfo;


	if (OpenPeerCoreManager::accountPtr)
	{
		reloginInfoElement = OpenPeerCoreManager::accountPtr->getReloginInformation();

		reloginInfo =  env->NewStringUTF(IHelper::convertToString(reloginInfoElement).c_str());
	}

	return reloginInfo;
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getLocationID
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getLocationID
(JNIEnv *env , jobject)
{
	jstring locationID;


	if (OpenPeerCoreManager::accountPtr)
	{

		locationID =  env->NewStringUTF(OpenPeerCoreManager::accountPtr->getLocationID().c_str());
	}

	return locationID;
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_shutdown
(JNIEnv *, jobject)
{
	if (OpenPeerCoreManager::accountPtr)
	{
		OpenPeerCoreManager::accountPtr->shutdown();
		//todo reset OpenPeerCoreManager::accountPtr when shutdown state is received
	}

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getPeerFilePrivate
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getPeerFilePrivate
(JNIEnv *env, jobject)
{
	ElementPtr peerFilePrivateElement;
	jstring peerFilePrivate;


	if (OpenPeerCoreManager::accountPtr)
	{
		peerFilePrivateElement = OpenPeerCoreManager::accountPtr->getReloginInformation();

		peerFilePrivate =  env->NewStringUTF(IHelper::convertToString(peerFilePrivateElement).c_str());
	}

	return peerFilePrivate;
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getPeerFilePrivateSecret
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_openpeer_javaapi_OPAccount_getPeerFilePrivateSecret
(JNIEnv *env, jobject)
{
	jbyte* bufferPtr;
	jbyteArray returnArr;

	if (OpenPeerCoreManager::accountPtr)
	{
		SecureByteBlockPtr sec = OpenPeerCoreManager::accountPtr->getPeerFilePrivateSecret();
		returnArr = env->NewByteArray(sec->SizeInBytes());
		env->SetByteArrayRegion(returnArr, (int)0, (int)sec->SizeInBytes(), (const signed char *)sec->data());

		//bufferPtr = env->GetByteArrayElements(sec->BytePtr(), 0);
	}

	return returnArr;

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getAssociatedIdentities
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPAccount_getAssociatedIdentities
(JNIEnv *env, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject returnListObject;
	JNIEnv *jni_env = 0;

	//Core identity list
	IdentityListPtr coreIdentities;


	//take associated identities from core
	if (OpenPeerCoreManager::accountPtr)
	{
		coreIdentities = OpenPeerCoreManager::accountPtr->getAssociatedIdentities();
	}

	//fetch JNI env
	jni_env = getEnv();
	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass returnListClass = findClass("java/util/ArrayList");
		jmethodID listConstructorMethodID = jni_env->GetMethodID(returnListClass, "<init>", "()V");
		returnListObject = jni_env->NewObject(returnListClass, listConstructorMethodID);


		//fetch List.add object
		jmethodID listAddMethodID = jni_env->GetMethodID(returnListClass, "add", "(Ljava/lang/Object;)Z");

		if(identityMap.size() < 1 || identityMap.size() != coreIdentities->size())
		{
			//fill/update map
			for(IdentityList::iterator coreListIter = coreIdentities->begin();
					coreListIter != coreIdentities->end(); coreListIter++)
			{
				//fetch List item object / OPIdentity
				jclass identityClass = findClass("com/openpeer/javaapi/OPIdentity");
				jmethodID identityConstructorMethodID = jni_env->GetMethodID(identityClass, "<init>", "()V");
				jobject identityObject = jni_env->NewObject(identityClass, identityConstructorMethodID);

				//add to map for future calls
				identityMap.insert(std::pair<jobject, IIdentityPtr>(identityObject, *coreListIter));
				//identityMap[identityObject] = *coreListIter;

				//add to return List
				jboolean success = jni_env->CallBooleanMethod(returnListObject,listAddMethodID , identityObject);

			}
		}
		else
		{
			//return known identities from map
			for (std::map<jobject, IIdentityPtr>::iterator it = identityMap.begin();
					it != identityMap.end(); it++)
			{
				jni_env->CallBooleanMethod(returnListObject,listAddMethodID , it->first);

			}
		}
	}
	return returnListObject;
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    removeIdentities
 * Signature: (Ljava/util/List;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_removeIdentities
(JNIEnv *, jobject owner, jobject identitiesToRemove)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	//Core identity list
	IdentityList coreIdentitiesToRemove;

	//fetch JNI env
	jni_env = getEnv();
	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass arrayListClass = findClass("java/util/ArrayList");
		if(jni_env->IsInstanceOf(identitiesToRemove, arrayListClass) != JNI_TRUE)
		{
			return;
		}
		// Fetch "java.util.List.get(int location)" MethodID
		jmethodID listGetMethodID = jni_env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
		// Fetch "int java.util.List.size()" MethodID
		jmethodID sizeMethodID = jni_env->GetMethodID( arrayListClass, "size", "()I" );

		// Call "int java.util.List.size()" method and get count of items in the list.
		int listItemsCount = (int)jni_env->CallIntMethod( identitiesToRemove, sizeMethodID );

		for( int i=0; i<listItemsCount; ++i )
		{
			// Call "java.util.List.get" method and get IdentParams object by index.
			jobject identityObject = jni_env->CallObjectMethod( identitiesToRemove, listGetMethodID, i - 1 );
			if( identityObject != NULL )
			{
				IIdentityPtr identity = identityMap.find(identityObject)->second;
				//add core identities to list for removal
				coreIdentitiesToRemove.push_front(identity);
				//remove identity entry from jni identity map
				identityMap.erase(identityObject);
			}
		}
	}
	//remove associated identities from core
	if (OpenPeerCoreManager::accountPtr)
	{
		OpenPeerCoreManager::accountPtr->removeIdentities(coreIdentitiesToRemove);
	}
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getInnerBrowserWindowFrameURL
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getInnerBrowserWindowFrameURL
(JNIEnv *env, jobject)
{
	String innerBrowserWindowFrameURLString;
	jstring innerBrowserWindowFrameURL;


	if (OpenPeerCoreManager::accountPtr)
	{
		innerBrowserWindowFrameURLString = OpenPeerCoreManager::accountPtr->getInnerBrowserWindowFrameURL();

		innerBrowserWindowFrameURL =  env->NewStringUTF(innerBrowserWindowFrameURLString.c_str());
	}

	return innerBrowserWindowFrameURL;
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    notifyBrowserWindowVisible
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_notifyBrowserWindowVisible
(JNIEnv *, jobject)
{
	if (OpenPeerCoreManager::accountPtr)
	{
		OpenPeerCoreManager::accountPtr->notifyBrowserWindowVisible();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    notifyBrowserWindowClosed
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_notifyBrowserWindowClosed
(JNIEnv *, jobject)
{
	if (OpenPeerCoreManager::accountPtr)
	{
		OpenPeerCoreManager::accountPtr->notifyBrowserWindowClosed();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    getNextMessageForInnerBrowerWindowFrame
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPAccount_getNextMessageForInnerBrowerWindowFrame
(JNIEnv *env, jobject)
{
	ElementPtr nextMessageForInnerBrowerWindowFrameElement;
	jstring nextMessageForInnerBrowerWindowFrame;


	if (OpenPeerCoreManager::accountPtr)
	{
		nextMessageForInnerBrowerWindowFrameElement = OpenPeerCoreManager::accountPtr->getNextMessageForInnerBrowerWindowFrame();

		nextMessageForInnerBrowerWindowFrame =  env->NewStringUTF(IHelper::convertToString(nextMessageForInnerBrowerWindowFrameElement).c_str());
	}

	return nextMessageForInnerBrowerWindowFrame;
}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    handleMessageFromInnerBrowserWindowFrame
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_handleMessageFromInnerBrowserWindowFrame
(JNIEnv *env, jobject, jstring unparsedMessage)
{
	String unparsedMessageString;
	unparsedMessageString = env->GetStringUTFChars(unparsedMessage, NULL);
	if (unparsedMessageString == NULL) {
		return;
	}

	if (OpenPeerCoreManager::accountPtr)
	{
		OpenPeerCoreManager::accountPtr->handleMessageFromInnerBrowserWindowFrame(IHelper::createElement(unparsedMessageString));
	}
}

#ifdef __cplusplus
}
#endif


