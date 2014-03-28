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
 * Signature: (Lcom/openpeer/javaapi/OPAccount;Ljava/util/List;)Lcom/openpeer/javaapi/OPConversationThread;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThread_create
(JNIEnv *, jclass, jobject, jobject identityContacts)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	IConversationThreadPtr conversationThreadPtr;

	//Core contact profile list
	IdentityContactList coreIdentityContacts;

	//check if account is existing
	if(accountPtr)
	{
		return object;
	}

	//fetch JNI env
	jni_env = getEnv();
	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass arrayListClass = findClass("java/util/ArrayList");
		if(jni_env->IsInstanceOf(identityContacts, arrayListClass) != JNI_TRUE)
		{
			return object;
		}
		// Fetch "java.util.List.get(int location)" MethodID
		jmethodID listGetMethodID = jni_env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
		// Fetch "int java.util.List.size()" MethodID
		jmethodID sizeMethodID = jni_env->GetMethodID( arrayListClass, "size", "()I" );

		// Call "int java.util.List.size()" method and get count of items in the list.
		int listItemsCount = (int)jni_env->CallIntMethod( identityContacts, sizeMethodID );


		//Fetch OPIdentityContact class
		jclass identityContactClass = findClass("com/openpeer/javaapi/OPIdentityContact");

		//FETCH METHODS TO GET INFO FROM JAVA
		//Fetch getStableID method from OPIdentityContact class
		jmethodID getStableIDMethodID = jni_env->GetMethodID( identityContactClass, "getStableID", "()Ljava/lang/String;" );
		//Fetch getPeerFilePublic method from OPIdentityContact class
		jmethodID getPeerFilePublicMethodID = jni_env->GetMethodID( identityContactClass, "getPeerFilePublic", "()Lcom/openpeer/javaapi/OPPeerFilePublic;" );
		//Fetch getIdentityProofBundle method from OPIdentityContact class
		jmethodID getIdentityProofBundleMethodID = jni_env->GetMethodID( identityContactClass, "getIdentityProofBundle", "()Ljava/lang/String;" );
		//Fetch getPriority method from OPIdentityContact class
		jmethodID getPriorityMethodID = jni_env->GetMethodID( identityContactClass, "getPriority", "()I" );
		//Fetch getWeight method from OPIdentityContact class
		jmethodID getWeightMethodID = jni_env->GetMethodID( identityContactClass, "getWeight", "()I" );
		//Fetch getLastUpdated method from OPIdentityContact class
		jmethodID getLastUpdatedMethodID = jni_env->GetMethodID( identityContactClass, "getLastUpdated", "()Landroid/text/format/Time;" );
		//Fetch getExpires method from OPIdentityContact class
		jmethodID getExpiresMethodID = jni_env->GetMethodID( identityContactClass, "getExpires", "()Landroid/text/format/Time;" );


		for( int i=0; i<listItemsCount; ++i )
		{
			// Call "java.util.List.get" method and get IdentityContact object by index.
			jobject identityContactObject = jni_env->CallObjectMethod( identityContacts, listGetMethodID, i - 1 );

			if( identityContactObject != NULL )
			{
				//CALL METHODS TO FETCH INFO FROM JAVA
				// Call getStableID method to fetch stable ID from OPIdentityContact
				jstring stableID = (jstring)jni_env->CallObjectMethod( identityContactObject, getStableIDMethodID );

				// Call getPeerFilePublic method to fetch peer file public from OPIdentityContact
				jobject peerFilePublic = jni_env->CallObjectMethod( identityContactObject, getPeerFilePublicMethodID );

				// Call getIdentityProofBundle method to fetch identity proof bundle from OPIdentityContact
				jstring identityProofBundle = (jstring)jni_env->CallObjectMethod( identityContactObject, getIdentityProofBundleMethodID );

				// Call getPriority method to fetch priority from OPIdentityContact
				jint priority = jni_env->CallIntMethod( identityContactObject, getPriorityMethodID );

				// Call getWeight method to fetch priority from OPIdentityContact
				jint weight = jni_env->CallIntMethod( identityContactObject, getWeightMethodID );

				// Call getLastUpdated method to fetch last updated from OPIdentityContact
				jobject lastUpdated = jni_env->CallObjectMethod( identityContactObject, getLastUpdatedMethodID );

				// Call getExpires method to fetch expires from OPIdentityContact
				jobject expires = jni_env->CallObjectMethod( identityContactObject, getExpiresMethodID );

				//FILL IN CORE IDENTITY CONTACT STRUCTURE WITH DATA FROM JAVA
				IdentityContact coreIdentityContact;
				//Add stableID to IdentityContact structure
				coreIdentityContact.mStableID = jni_env->GetStringUTFChars(stableID, NULL);

				//Add peerFilePublic to IdentityContact structure
				//TODO will not implement now

				//Add identityProofBundle to IdentityContact structure
				String identityProofBundleString = jni_env->GetStringUTFChars(identityProofBundle, NULL);
				coreIdentityContact.mIdentityProofBundleEl = IHelper::createElement(identityProofBundleString);

				//Add priority to IdentityContact structure
				coreIdentityContact.mPriority = priority;

				//Add weight to IdentityContact structure
				coreIdentityContact.mWeight = weight;

				//Add last updated to IdentityContact structure
				jclass timeCls = findClass("android/text/format/Time");
				jmethodID timeMethodID   = jni_env->GetMethodID(timeCls, "toMillis", "(Z)J");
				long longValue = (long) jni_env->CallIntMethod(lastUpdated, timeMethodID, false);
				Time t = boost::posix_time::from_time_t(longValue/1000) + boost::posix_time::millisec(longValue % 1000);
				coreIdentityContact.mLastUpdated = t;

				//Add expires to IdentityContact structure
				//jclass timeCls = findClass("android/text/format/Time");
				//jmethodID timeMethodID   = jni_env->GetMethodID(timeCls, "toMillis", "(Z)J");
				longValue = (long) jni_env->CallIntMethod(expires, timeMethodID, false);
				t = boost::posix_time::from_time_t(longValue/1000) + boost::posix_time::millisec(longValue % 1000);
				coreIdentityContact.mExpires = t;

				//add core identity contacts to list
				coreIdentityContacts.push_front(coreIdentityContact);

			}
		}
	}

	conversationThreadPtr = IConversationThread::create(accountPtr, coreIdentityContacts);

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
	jclass cls;
	jmethodID method;
	jobject returnListObject;
	JNIEnv *jni_env = 0;

	//Core identity list
	ConversationThreadListPtr coreConversationThreads;


	//take associated identities from core
	if(accountPtr)
	{
		coreConversationThreads = IConversationThread::getConversationThreads(accountPtr);
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
		for(ConversationThreadList::iterator coreListIter = coreConversationThreads->begin();
				coreListIter != coreConversationThreads->end(); coreListIter++)
		{
			//fetch List item object / OPConversationThread
			jclass conversationThreadClass = findClass("com/openpeer/javaapi/OPConversationThread");
			jmethodID conversationThreadConstructorMethodID = jni_env->GetMethodID(conversationThreadClass, "<init>", "()V");
			jobject conversationThreadObject = jni_env->NewObject(conversationThreadClass, conversationThreadConstructorMethodID);

			//add to map for future calls
			conversationThreadMap.insert(std::pair<jobject, IConversationThreadPtr>(conversationThreadObject, *coreListIter));

			//add to return List
			jboolean success = jni_env->CallBooleanMethod(returnListObject,listAddMethodID , conversationThreadObject);

		}

	}
	return returnListObject;
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
 * Method:    getIdentityContactList
 * Signature: (Lcom/openpeer/javaapi/OPContact;)Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThread_getIdentityContactList
(JNIEnv *, jobject owner, jobject contact)
{
	jclass cls;
	jmethodID method;
	jobject returnListObject;
	jobject object;
	JNIEnv *jni_env = 0;

	IContactPtr contactPtr = contactMap.find(contact)->second;
	IdentityContact coreContact;
	IdentityContactListPtr coreContactList;

	//take contacts from core conversation thread
	std::map<jobject, IConversationThreadPtr>::iterator it = conversationThreadMap.find(owner);
	if (it!= conversationThreadMap.end())
	{
		coreContactList = it->second->getIdentityContactList(contactPtr);

	}
	jni_env = getEnv();
	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass returnListClass = findClass("java/util/ArrayList");
		jmethodID listConstructorMethodID = jni_env->GetMethodID(returnListClass, "<init>", "()V");
		returnListObject = jni_env->NewObject(returnListClass, listConstructorMethodID);


		//fetch List.add object
		jmethodID listAddMethodID = jni_env->GetMethodID(returnListClass, "add", "(Ljava/lang/Object;)Z");

		for(IdentityContactList::iterator iter = coreContactList->begin(); iter != coreContactList->end(); iter ++)
		{
			coreContact = *iter;

			cls = findClass("com/openpeer/javaapi/OPIdentityContact");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);

			//set Stable ID to OPIdentityContact
			method = jni_env->GetMethodID(cls, "setStableID", "(Ljava/lang/String;)V");
			jstring stableID =  jni_env->NewStringUTF(coreContact.mStableID.c_str());
			jni_env->CallVoidMethod(object, method, stableID);

			//set Public Peer File to OPIdentityContact
			//TODO export peer file public to ElementPtr and then convert to String
			jclass peerFileCls = findClass("com/openpeer/javaapi/OPPeerFilePublic");
			jmethodID peerFileMethodID = jni_env->GetMethodID(peerFileCls, "<init>", "()V");
			jobject peerFileObject = jni_env->NewObject(peerFileCls, peerFileMethodID);
			method = jni_env->GetMethodID(cls, "setPeerFilePublic", "(Lcom/openpeer/javaapi/OPPeerFilePublic)V");
			jni_env->CallVoidMethod(object, method, peerFileObject);

			//set IdentityProofBundle to OPIdentityContact
			method = jni_env->GetMethodID(cls, "setIdentityProofBundleEl", "(Ljava/lang/String;)V");
			jstring identityProofBundle =  jni_env->NewStringUTF(IHelper::convertToString(coreContact.mIdentityProofBundleEl).c_str());
			jni_env->CallVoidMethod(object, method, identityProofBundle);

			//set Priority to OPIdentityContact
			method = jni_env->GetMethodID(cls, "setPriority", "(I)V");
			jni_env->CallVoidMethod(object, method, (int)coreContact.mPriority);

			//set Weight to OPIdentityContact
			method = jni_env->GetMethodID(cls, "setWeight", "(I)V");
			jni_env->CallVoidMethod(object, method, (int)coreContact.mWeight);

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
			jni_env->CallVoidMethod(object, method, timeLastUpdatedObject);

			//calculate and set Expires
			zsLib::Duration expires = coreContact.mExpires - time_t_epoch;
			jobject timeExpiresObject = jni_env->NewObject(peerFileCls, peerFileMethodID);
			jni_env->CallVoidMethod(timeExpiresObject, timeSetMillisMethodID, expires.total_milliseconds());
			//Time has been converted, now call OPIdentityContact setter
			method = jni_env->GetMethodID(cls, "setLastUpdated", "(Landroid/text/format/Time;)V");
			jni_env->CallVoidMethod(object, method, timeExpiresObject);

			//add to return List
			jboolean success = jni_env->CallBooleanMethod(returnListObject,listAddMethodID , object);

		}

	}
	return returnListObject;
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
		it->second->sendMessage(messageIDStr, messageTypeStr, messageStr, true);
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
