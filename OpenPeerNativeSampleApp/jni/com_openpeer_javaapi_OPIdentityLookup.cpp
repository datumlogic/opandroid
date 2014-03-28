#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IIdentityLookup.h"
#include "openpeer/core/ILogger.h"
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
				jstring identityURI = (jstring)jni_env->CallObjectMethod( identityLookupInfoClass, getIdentityURIMethodID );

				// Call "getIdentityURI method to fetch contact from Identity lookup info
				jobject lastUpdated = jni_env->CallObjectMethod( identityLookupInfoClass, getLastUpdatedMethodID );

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

	identityLookupPtr = IIdentityLookup::create(accountPtr,
			globalEventManager,
			identityLookupInfosForCore,
			identityServiceDomainStr);

	if(identityLookupPtr)
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
(JNIEnv *, jobject);

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    isComplete
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_isComplete
(JNIEnv *, jobject);

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    wasSuccessful
 * Signature: (ILjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_wasSuccessful
(JNIEnv *, jobject, jint, jstring);

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    cancel
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_cancel
(JNIEnv *, jobject);

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    getUpdatedIdentities
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_getUpdatedIdentities
(JNIEnv *, jobject);

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    getUnchangedIdentities
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_getUnchangedIdentities
(JNIEnv *, jobject);

/*
 * Class:     com_openpeer_javaapi_OPIdentityLookup
 * Method:    getInvalidIdentities
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPIdentityLookup_getInvalidIdentities
(JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
