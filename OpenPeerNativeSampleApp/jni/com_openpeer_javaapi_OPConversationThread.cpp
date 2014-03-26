#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IConversationThread.h"
#include "openpeer/core/IHelper.h"
#include "openpeer/core/ILogger.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/MessageDeliveryStates;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPConversationThread_toString__Lcom_openpeer_javaapi_MessageDeliveryStates_2
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/ContactStates;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPConversationThread_toString__Lcom_openpeer_javaapi_ContactStates_2
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    toDebugString
 * Signature: (Lcom/openpeer/javaapi/OPConversationThread;Z)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPConversationThread_toDebugString
(JNIEnv *, jclass, jobject, jboolean)
{

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    create
 * Signature: (Lcom/openpeer/javaapi/OPAccount;Ljava/lang/String;)Lcom/openpeer/javaapi/OPConversationThread;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThread_create
(JNIEnv *env, jclass, jobject, jstring profileBundle)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	IConversationThreadPtr conversationThreadPtr;

	const char *profileBundleStr;
	profileBundleStr = env->GetStringUTFChars(profileBundle, NULL);
	if (profileBundleStr == NULL) {
		return object;
	}

	if(accountPtr)
	{
		ElementPtr profileBundleElement = IHelper::createElement(profileBundleStr);
		conversationThreadPtr = IConversationThread::create(accountPtr, profileBundleElement);
	}

	if(conversationThreadPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPConversationThread");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);
			conversationThreadMap.insert(std::pair<jobject, IConversationThreadPtr>(object, conversationThreadPtr));

		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getConversationThreads
 * Signature: (Lcom/openpeer/javaapi/OPAccount;)Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThread_getConversationThreads
(JNIEnv *, jclass, jobject)
{
	//TODO fix along with list methods
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getConversationThreadByID
 * Signature: (Lcom/openpeer/javaapi/OPAccount;Ljava/lang/String;)Lcom/openpeer/javaapi/OPConversationThread;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThread_getConversationThreadByID
(JNIEnv *env, jclass, jobject, jstring threadID)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	IConversationThreadPtr conversationThreadPtr;

	const char *threadIDStr;
	threadIDStr = env->GetStringUTFChars(threadID, NULL);
	if (threadIDStr == NULL) {
		return object;
	}

	if(accountPtr)
	{
		conversationThreadPtr = IConversationThread::getConversationThreadByID(accountPtr, threadIDStr);
	}

	if(conversationThreadPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPConversationThread");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);
			conversationThreadMap.insert(std::pair<jobject, IConversationThreadPtr>(object, conversationThreadPtr));

		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getStableID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPConversationThread_getStableID
(JNIEnv *, jobject owner)
{
	jlong ret = 0;
	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (it!= conversationThreadMap.end())
	{
		ret = it->second->getID();
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getThreadID
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPConversationThread_getThreadID
(JNIEnv *env, jobject owner)
{
	jstring ret;
	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (it!= conversationThreadMap.end())
	{
		ret = env->NewStringUTF(it->second->getThreadID().c_str());
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    amIHost
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPConversationThread_amIHost
(JNIEnv *, jobject owner)
{
	jboolean ret;
	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (it!= conversationThreadMap.end())
	{
		ret = it->second->amIHost();
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getAssociatedAccount
 * Signature: ()Lcom/openpeer/javaapi/OPAccount;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThread_getAssociatedAccount
(JNIEnv *, jobject)
{
	return globalAccount;
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getContacts
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThread_getContacts
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject returnListObject;
	JNIEnv *jni_env = 0;

	//Core Contact list
	ContactListPtr coreContacts;


	//take contacts from core conversation thread
	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (it!= conversationThreadMap.end())
	{
		coreContacts = it->second->getContacts();
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

		//fill/update map
		for(ContactList::iterator coreListIter = coreContacts->begin();
				coreListIter != coreContacts->end(); coreListIter++)
		{
			//fetch List item object / OPContact
			jclass contactClass = findClass("com/openpeer/javaapi/OPContact");
			jmethodID contactConstructorMethodID = jni_env->GetMethodID(contactClass, "<init>", "()V");
			jobject contactObject = jni_env->NewObject(contactClass, contactConstructorMethodID);

			//add to map for future calls
			//identityMap.insert(std::pair<jobject, IIdentityPtr>(identityObject, *coreListIter));
			//identityMap[identityObject] = *coreListIter;

			//add to return List
			jboolean success = jni_env->CallBooleanMethod(returnListObject,listAddMethodID , contactObject);

		}
	}
	return returnListObject;
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getProfileBundle
 * Signature: (Lcom/openpeer/javaapi/OPContact;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPConversationThread_getProfileBundle
(JNIEnv *env, jobject owner, jobject contact)
{
	jstring ret;
	std::map<jobject, IContactPtr>::iterator contactIterator = contactMap.find(contact);
	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (contactIterator!= contactMap.end() && it != conversationThreadMap.end())
	{
		ElementPtr profileBundleElement = it->second->getProfileBundle(contactIterator->second);
		ret = env->NewStringUTF(IHelper::convertToString(profileBundleElement).c_str());
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getContactState
 * Signature: (Lcom/openpeer/javaapi/OPContact;)Lcom/openpeer/javaapi/ContactStates;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThread_getContactState
(JNIEnv *, jobject owner, jobject contact)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	int state = 0;

	std::map<jobject, IContactPtr>::iterator contactIterator = contactMap.find(contact);
	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (contactIterator!= contactMap.end() && it != conversationThreadMap.end())
	{
		state = (int) it->second->getContactState(contactIterator->second);
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/ContactStates");
			method = jni_env->GetMethodID(cls, "<init>", "(I)V");
			object = jni_env->NewObject(cls, method, state);

		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    addContacts
 * Signature: (Ljava/util/List;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPConversationThread_addContacts
(JNIEnv *, jobject owner, jobject contactProfileInfos)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	//Core contact profile list
	ContactProfileInfoList coreContactProfilesToAdd;

	//fetch JNI env
	jni_env = getEnv();
	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass arrayListClass = findClass("java/util/ArrayList");
		if(jni_env->IsInstanceOf(contactProfileInfos, arrayListClass) != JNI_TRUE)
		{
			return;
		}
		// Fetch "java.util.List.get(int location)" MethodID
		jmethodID listGetMethodID = jni_env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
		// Fetch "int java.util.List.size()" MethodID
		jmethodID sizeMethodID = jni_env->GetMethodID( arrayListClass, "size", "()I" );

		// Call "int java.util.List.size()" method and get count of items in the list.
		int listItemsCount = (int)jni_env->CallIntMethod( contactProfileInfos, sizeMethodID );

		for( int i=0; i<listItemsCount; ++i )
		{
			// Call "java.util.List.get" method and get Contact object by index.
			jobject contactProfileInfoObject = jni_env->CallObjectMethod( contactProfileInfos, listGetMethodID, i - 1 );
			if( contactProfileInfoObject != NULL )
			{
				//Fetch OPContactProfileInfo class
				jclass contactProfileInfoClass = findClass("com/openpeer/javaapi/OPContactProfileInfo");
				//Fetch getContact method from OPContactProfileInfo class
				jmethodID getContactMethodID = jni_env->GetMethodID( contactProfileInfoClass, "getContact", "()Lcom/openpeer/javaapi/OPContact;" );

				// Call "getContact method to fetch contact from Contact profile info
				jobject contactObject = jni_env->CallObjectMethod( contactProfileInfoClass, getContactMethodID );

				IContactPtr contact = contactMap.find(contactObject)->second;

				ContactProfileInfo contactProfileInfo;
				contactProfileInfo.mContact = contact;
				//todo add profile bundle to contact profile info

				//contactProfileInfo.mProfileBundleEl = contact->
				//add core contacts to list for removal
				coreContactProfilesToAdd.push_front(contactProfileInfo);

			}
		}
	}
	//remove contacts from conversation thread
	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (it != conversationThreadMap.end())
	{
		it->second->addContacts(coreContactProfilesToAdd);
	}
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    removeContacts
 * Signature: (Ljava/util/List;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPConversationThread_removeContacts
(JNIEnv *, jobject owner, jobject contactsToRemove)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	//Core contact list
	ContactList coreContactsToRemove;

	//fetch JNI env
	jni_env = getEnv();
	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass arrayListClass = findClass("java/util/ArrayList");
		if(jni_env->IsInstanceOf(contactsToRemove, arrayListClass) != JNI_TRUE)
		{
			return;
		}
		// Fetch "java.util.List.get(int location)" MethodID
		jmethodID listGetMethodID = jni_env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
		// Fetch "int java.util.List.size()" MethodID
		jmethodID sizeMethodID = jni_env->GetMethodID( arrayListClass, "size", "()I" );

		// Call "int java.util.List.size()" method and get count of items in the list.
		int listItemsCount = (int)jni_env->CallIntMethod( contactsToRemove, sizeMethodID );

		for( int i=0; i<listItemsCount; ++i )
		{
			// Call "java.util.List.get" method and get Contact object by index.
			jobject contactObject = jni_env->CallObjectMethod( contactsToRemove, listGetMethodID, i - 1 );
			if( contactObject != NULL )
			{
				IContactPtr contact = contactMap.find(contactObject)->second;
				//add core contacts to list for removal
				coreContactsToRemove.push_front(contact);
				//remove contact entry from jni contact map
				contactMap.erase(contactObject);
			}
		}
	}
	//remove contacts from conversation thread
	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (it != conversationThreadMap.end())
	{
		it->second->removeContacts(coreContactsToRemove);
	}
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    sendMessage
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPConversationThread_sendMessage
(JNIEnv *env, jobject owner, jstring messageID, jstring messageType, jstring message)
{
	const char *messageIDStr;
	messageIDStr = env->GetStringUTFChars(messageID, NULL);
	if (messageIDStr == NULL) {
		return;
	}

	const char *messageTypeStr;
	messageTypeStr = env->GetStringUTFChars(messageType, NULL);
	if (messageTypeStr == NULL) {
		return;
	}

	const char *messageStr;
	messageStr = env->GetStringUTFChars(message, NULL);
	if (messageStr == NULL) {
		return;
	}

	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (it!= conversationThreadMap.end())
	{
		it->second->sendMessage(messageIDStr, messageTypeStr, messageStr);
	}
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getMessage
 * Signature: (Ljava/lang/String;Lcom/openpeer/javaapi/OPContact;Ljava/lang/String;Ljava/lang/String;Landroid/text/format/Time;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPConversationThread_getMessage
(JNIEnv *, jobject, jstring, jobject, jstring, jstring, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThread
 * Method:    getMessageDeliveryState
 * Signature: (Ljava/lang/String;Lcom/openpeer/javaapi/MessageDeliveryStates;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPConversationThread_getMessageDeliveryState
(JNIEnv *, jobject, jstring, jobject)
{

}

#ifdef __cplusplus
}
#endif
