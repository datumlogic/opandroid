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
			globalAccount = object;

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
			globalAccount = object;

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
(JNIEnv *, jobject)
{
	jlong pid = 0;

	if (accountPtr)
	{
		pid = accountPtr->getID();
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

	if (accountPtr)
	{
		state = (int) accountPtr->getState(&outErrorCode, &outErrorReason);

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


	if (accountPtr)
	{
		reloginInfoElement = accountPtr->getReloginInformation();

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


	if (accountPtr)
	{

		locationID =  env->NewStringUTF(accountPtr->getLocationID().c_str());
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


	if (accountPtr)
	{
		peerFilePrivateElement = accountPtr->getReloginInformation();

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

	if (accountPtr)
	{
		SecureByteBlockPtr sec = accountPtr->getPeerFilePrivateSecret();
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
	jobject object;
	JNIEnv *jni_env = 0;

	//Core identity list
	IdentityListPtr coreIdentities;


	//take associated identities from core
	if (accountPtr)
	{
		coreIdentities = accountPtr->getAssociatedIdentities();
	}

	//fetch JNI env
	jni_env = getEnv();
	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass returnListClass = findClass("java/util/ArrayList");
		jmethodID listConstructorMethodID = jni_env->GetMethodID(returnListClass, "<init>", "()V");
		jobject returnListObject = jni_env->NewObject(returnListClass, listConstructorMethodID);


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
		return returnListObject;
	}


	//	if (accountPtr)
	//	{
	//		coreIdentities = accountPtr->getAssociatedIdentities();
	//
	//		for (IdentityList::iterator coreListIter = coreIdentities->begin(); coreListIter != coreIdentities->end();coreListIter++)
	//		{
	//			//todo fill core list to java list
	//			std::map<jobject, IIdentityPtr>::iterator it = identityMap.find(owner);
	//			if (it!= identityMap.end())
	//			{
	//				for(std::map<jobject, IIdentityPtr>::iterator iter = identityMap.begin(); iter != identityMap.end(); ++iter)
	//				{
	//					if (iter->second == coreListIter)
	//					{
	//						ret = iter->first;
	//						break;
	//					}
	//				}
	//
	//			}
	//			contactMap.insert(std::pair<jobject, IContactPtr>(object, contactPtr));
	//		}
	//	}

}

/*
 * Class:     com_openpeer_javaapi_OPAccount
 * Method:    removeIdentities
 * Signature: (Ljava/util/List;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPAccount_removeIdentities
(JNIEnv *, jobject, jobject)
{
	//	jclass cls;
	//	jmethodID method;
	//	jobject object;
	//	JNIEnv *jni_env = 0;
	//
	//	IdentityList identityList;
	//	if(identityPtr)
	//	{
	//		identityPtr->getDownloadedRolodexContacts(&identityList);
	//
	//	}
	//	jni_env = getEnv();
	//	if(jni_env)
	//	{
	//		cls = findClass("com/openpeer/javaapi/OPIdentityContact");
	//		method = jni_env->GetMethodID(cls, "<init>", "()V");
	//		object = jni_env->NewObject(cls, method);
	//	}
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


	if (accountPtr)
	{
		innerBrowserWindowFrameURLString = accountPtr->getInnerBrowserWindowFrameURL();

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
	if (accountPtr)
	{
		accountPtr->notifyBrowserWindowVisible();
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
	if (accountPtr)
	{
		accountPtr->notifyBrowserWindowClosed();
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


	if (accountPtr)
	{
		nextMessageForInnerBrowerWindowFrameElement = accountPtr->getNextMessageForInnerBrowerWindowFrame();

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

	if (accountPtr)
	{
		accountPtr->handleMessageFromInnerBrowserWindowFrame(IHelper::createElement(unparsedMessageString));
	}
}

#ifdef __cplusplus
}
#endif


