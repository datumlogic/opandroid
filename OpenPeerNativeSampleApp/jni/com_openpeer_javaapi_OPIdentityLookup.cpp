#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IIdentityLookup.h"
#include "openpeer/core/ILogger.h"
#include "openpeer/core/IHelper.h"
#include "OpenPeerCoreManager.h"
#include <android/log.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    toDebugString
 * Signature: (Lcom/openpeer/javaapi/OPIdentityLookup;Z)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_toDebugString
(JNIEnv *, jclass, jobject, jboolean)
{

}

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    create
 * Signature: (Lcom/openpeer/javaapi/OPAccount;Lcom/openpeer/javaapi/OPIdentityLookupDelegate;Ljava/util/List;Ljava/lang/String;)Lcom/openpeer/javaapi/OPIdentityLookup;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_create
(JNIEnv *env, jclass, jobject, jobject, jobject identityLookupInfos, jstring identityServiceDomain)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	const char *identityServiceDomainStr;
	identityServiceDomainStr = env->GetStringUTFChars(identityServiceDomain, NULL);
	if (identityServiceDomainStr == NULL) {
		return object;
	}

	//Core contact profile list
	IIdentityLookup::IdentityLookupInfoList identityLookupInfosForCore;

	//fetch JNI env
	jni_env = getEnv();
	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass arrayListClass = findClass("java/util/ArrayList");
		if(jni_env->IsInstanceOf(identityLookupInfos, arrayListClass) != JNI_TRUE)
		{
			return object;
		}
		// Fetch "java.util.List.get(int location)" MethodID
		jmethodID listGetMethodID = jni_env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
		// Fetch "int java.util.List.size()" MethodID
		jmethodID sizeMethodID = jni_env->GetMethodID( arrayListClass, "size", "()I" );

		// Call "int java.util.List.size()" method and get count of items in the list.
		int listItemsCount = (int)jni_env->CallIntMethod( identityLookupInfos, sizeMethodID );

		for( int i=0; i<listItemsCount; ++i )
		{
			// Call "java.util.List.get" method and get Contact object by index.
			jobject identityLookupInfoObject = jni_env->CallObjectMethod( identityLookupInfos, listGetMethodID, i - 1 );
			if( identityLookupInfoObject != NULL )
			{
				//Fetch OPIdentityLookupInfo class
				jclass identityLookupInfoClass = findClass("com/openpeer/javaapi/OPIdentityLookupInfo");
				//Fetch getIdentityURI method from OPIdentityLookupInfo class
				jmethodID getIdentityURIMethodID = jni_env->GetMethodID( identityLookupInfoClass, "getIdentityURI", "()Ljava/lang/String;" );
				//Fetch getLastUpdated method from OPIdentityLookupInfo class
				jmethodID getLastUpdatedMethodID = jni_env->GetMethodID( identityLookupInfoClass, "getLastUpdated", "()Landroid/text/format/Time;" );

				// Call "getIdentityURI method to fetch contact from Identity lookup info
				jstring identityURI = (jstring)jni_env->CallObjectMethod( identityLookupInfoObject, getIdentityURIMethodID );

				// Call "getIdentityURI method to fetch contact from Identity lookup info
				jobject lastUpdated = jni_env->CallObjectMethod( identityLookupInfoObject, getLastUpdatedMethodID );

				//Add identity URI to IdentityLookupInfo structure
				IIdentityLookup::IdentityLookupInfo identityLookupInfo;
				identityLookupInfo.mIdentityURI = env->GetStringUTFChars(identityURI, NULL);

				//Add last updated to IdentityLookupInfo structure
				jclass timeCls = findClass("android/text/format/Time");
				jmethodID timeMethodID   = env->GetMethodID(timeCls, "toMillis", "(Z)J");
				long longValue = (long) env->CallIntMethod(lastUpdated, timeMethodID, false);
				Time t = boost::posix_time::from_time_t(longValue/1000) + boost::posix_time::millisec(longValue % 1000);
				identityLookupInfo.mLastUpdated = t;

				//add core contacts to list for removal
				identityLookupInfosForCore.push_front(identityLookupInfo);

			}
		}
	}

	OpenPeerCoreManager::identityLookupPtr = IIdentityLookup::create(OpenPeerCoreManager::accountPtr,
			globalEventManager,
			identityLookupInfosForCore,
			identityServiceDomainStr);

	if(OpenPeerCoreManager::identityLookupPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPIdentityLookup");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    getStableID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_getStableID
(JNIEnv *, jobject)
{
	jlong pid = 0;

	if (OpenPeerCoreManager::identityLookupPtr)
	{
		pid = OpenPeerCoreManager::identityLookupPtr->getID();
	}

	return pid;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    isComplete
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_isComplete
(JNIEnv *, jobject)
{
	jboolean ret = 0;

	if (OpenPeerCoreManager::identityLookupPtr)
	{
		ret = OpenPeerCoreManager::identityLookupPtr->isComplete();
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    wasSuccessful
 * Signature: (ILjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_wasSuccessful
(JNIEnv *env, jobject, jint outErrorCode, jstring outErrorReason)
{
	jboolean ret = 0;
	String outReasonCore;
	WORD ourCodeCore;

	if (OpenPeerCoreManager::identityLookupPtr)
	{
		ret = OpenPeerCoreManager::identityLookupPtr->wasSuccessful(&ourCodeCore, &outReasonCore);
		outErrorCode = ourCodeCore;
		outErrorReason = env->NewStringUTF(outReasonCore);
	}

	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    cancel
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_cancel
(JNIEnv *, jobject)
{
	if (OpenPeerCoreManager::identityLookupPtr)
	{
		OpenPeerCoreManager::identityLookupPtr->cancel();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    getUpdatedIdentities
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_getUpdatedIdentities
(JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject returnListObject;
	jobject object;
	JNIEnv *jni_env = 0;

	IdentityContact coreContact;
	IdentityContactListPtr coreContactList;
	if(OpenPeerCoreManager::identityLookupPtr)
	{
		coreContactList = OpenPeerCoreManager::identityLookupPtr->getUpdatedIdentities();

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
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    getUnchangedIdentities
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_getUnchangedIdentities
(JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject returnListObject;
	JNIEnv *jni_env = 0;

	//Core Identity lookup info list
	IIdentityLookup::IdentityLookupInfoListPtr coreIdentityLookupInfoList;


	//take list from core identity lookup
	if (OpenPeerCoreManager::identityLookupPtr)
	{
		coreIdentityLookupInfoList = OpenPeerCoreManager::identityLookupPtr->getUnchangedIdentities();
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
		for(IIdentityLookup::IdentityLookupInfoList::iterator coreListIter = coreIdentityLookupInfoList->begin();
				coreListIter != coreIdentityLookupInfoList->end(); coreListIter++)
		{
			//fetch List item object / OPIdentityLookupInfo
			jclass identityLookupInfoClass = findClass("com/openpeer/javaapi/OPIdentityLookupInfo");
			jmethodID identityLookupInfoConstructorMethodID = jni_env->GetMethodID(identityLookupInfoClass, "<init>", "()V");
			jobject identityLookupInfoObject = jni_env->NewObject(identityLookupInfoClass, identityLookupInfoConstructorMethodID);

			//Fetch setIdentityURI method from OPIdentityLookupInfo class
			jmethodID setIdentityURIMethodID = jni_env->GetMethodID( identityLookupInfoClass, "setIdentityURI", "(Ljava/lang/String;)V" );
			//Fetch setLastUpdated method from OPIdentityLookupInfo class
			jmethodID setLastUpdatedMethodID = jni_env->GetMethodID( identityLookupInfoClass, "setLastUpdated", "(Landroid/text/format/Time;)V" );

			// Call "setIdentityURI method to fetch contact from Identity lookup info
			jstring identityURI = jni_env->NewStringUTF(coreListIter->mIdentityURI.c_str());
			jni_env->CallObjectMethod( identityLookupInfoObject, setIdentityURIMethodID, identityURI );

			// Call "setLastUpdated method to fetch contact from Identity lookup info
			//Convert and set time from C++ to Android; Fetch methods needed to accomplish this
			Time time_t_epoch = boost::posix_time::time_from_string("1970-01-01 00:00:00.000");
			jclass timeCls = findClass("android/text/format/Time");
			jmethodID timeMethodID = jni_env->GetMethodID(timeCls, "<init>", "()V");
			jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(Z)V");
			//calculate and set Ring time
			zsLib::Duration creationTimeDuration = coreListIter->mLastUpdated - time_t_epoch;
			jobject lastUpdatedObject = jni_env->NewObject(timeCls, timeMethodID);
			jni_env->CallVoidMethod(lastUpdatedObject, timeSetMillisMethodID, creationTimeDuration.total_milliseconds());
			jni_env->CallObjectMethod( identityLookupInfoObject, setLastUpdatedMethodID, lastUpdatedObject);

			//add to return List
			jboolean success = jni_env->CallBooleanMethod(returnListObject,listAddMethodID , identityLookupInfoObject);

		}
	}
	return returnListObject;
}

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    getInvalidIdentities
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_getInvalidIdentities
(JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject returnListObject;
	JNIEnv *jni_env = 0;

	//Core Identity lookup info list
	IIdentityLookup::IdentityLookupInfoListPtr coreIdentityLookupInfoList;


	//take list from core identity lookup
	if (OpenPeerCoreManager::identityLookupPtr)
	{
		coreIdentityLookupInfoList = OpenPeerCoreManager::identityLookupPtr->getInvalidIdentities();
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
		for(IIdentityLookup::IdentityLookupInfoList::iterator coreListIter = coreIdentityLookupInfoList->begin();
				coreListIter != coreIdentityLookupInfoList->end(); coreListIter++)
		{
			//fetch List item object / OPIdentityLookupInfo
			jclass identityLookupInfoClass = findClass("com/openpeer/javaapi/OPIdentityLookupInfo");
			jmethodID identityLookupInfoConstructorMethodID = jni_env->GetMethodID(identityLookupInfoClass, "<init>", "()V");
			jobject identityLookupInfoObject = jni_env->NewObject(identityLookupInfoClass, identityLookupInfoConstructorMethodID);

			//Fetch setIdentityURI method from OPIdentityLookupInfo class
			jmethodID setIdentityURIMethodID = jni_env->GetMethodID( identityLookupInfoClass, "setIdentityURI", "(Ljava/lang/String;)V" );
			//Fetch setLastUpdated method from OPIdentityLookupInfo class
			jmethodID setLastUpdatedMethodID = jni_env->GetMethodID( identityLookupInfoClass, "setLastUpdated", "(Landroid/text/format/Time;)V" );

			// Call "setIdentityURI method to fetch contact from Identity lookup info
			jstring identityURI = jni_env->NewStringUTF(coreListIter->mIdentityURI.c_str());
			jni_env->CallObjectMethod( identityLookupInfoObject, setIdentityURIMethodID, identityURI );

			// Call "setLastUpdated method to fetch contact from Identity lookup info
			//Convert and set time from C++ to Android; Fetch methods needed to accomplish this
			Time time_t_epoch = boost::posix_time::time_from_string("1970-01-01 00:00:00.000");
			jclass timeCls = findClass("android/text/format/Time");
			jmethodID timeMethodID = jni_env->GetMethodID(timeCls, "<init>", "()V");
			jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(Z)V");
			//calculate and set Ring time
			zsLib::Duration creationTimeDuration = coreListIter->mLastUpdated - time_t_epoch;
			jobject lastUpdatedObject = jni_env->NewObject(timeCls, timeMethodID);
			jni_env->CallVoidMethod(lastUpdatedObject, timeSetMillisMethodID, creationTimeDuration.total_milliseconds());
			jni_env->CallObjectMethod( identityLookupInfoObject, setLastUpdatedMethodID, lastUpdatedObject);

			//add to return List
			jboolean success = jni_env->CallBooleanMethod(returnListObject,listAddMethodID , identityLookupInfoObject);

		}
	}
	return returnListObject;
}

#ifdef __cplusplus
}
#endif
