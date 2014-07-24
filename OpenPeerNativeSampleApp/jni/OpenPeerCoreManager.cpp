#include "OpenPeerCoreManager.h"
#include "globals.h"
//#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include <android/log.h>;

//IAccountPtr OpenPeerCoreManager::accountPtr = IAccountPtr();
//IStackPtr OpenPeerCoreManager::stackPtr = IStackPtr();
IStackMessageQueuePtr OpenPeerCoreManager::queuePtr = IStackMessageQueuePtr();
//IIdentityLookupPtr OpenPeerCoreManager::identityLookupPtr = IIdentityLookupPtr();
//IMediaEnginePtr OpenPeerCoreManager::mediaEnginePtr = IMediaEnginePtr();
ISettingsPtr OpenPeerCoreManager::settingsPtr = ISettingsPtr();
ICachePtr OpenPeerCoreManager::cachePtr = ICachePtr();

//std::vector<IContactPtr> OpenPeerCoreManager::coreContactList;
//std::vector<ICallPtr> OpenPeerCoreManager::coreCallList;
//std::vector<IConversationThreadPtr> OpenPeerCoreManager::coreConversationThreadList;
//std::vector<IIdentityPtr> OpenPeerCoreManager::coreIdentityList;


//ICallPtr OpenPeerCoreManager::getCallFromList(jobject javaObject)
//{
//	JNIEnv *jni_env = getEnv();
//	jclass cls = findClass("com/openpeer/javaapi/OPCall");
//	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
//	jlong pointerValue = jni_env->GetLongField(javaObject, fid);
//
//	ICallPtr returnObj = ICallPtr();
//
//	for (std::vector<ICallPtr>::iterator it = coreCallList.begin(); it != coreCallList.end(); it++)
//	{
//		if (pointerValue == (jlong) it->get() )
//		{
//			returnObj = *it;
//			break;
//		}
//	}
//	if(!returnObj)
//	{
//		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "Related call not found in call list !!!");
//	}
//
//	return returnObj;
//}
//
//IContactPtr OpenPeerCoreManager::getContactFromList(jobject javaObject)
//{
//	JNIEnv *jni_env = getEnv();
//	jclass cls = findClass("com/openpeer/javaapi/OPContact");
//	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
//	jlong pointerValue = jni_env->GetLongField(javaObject, fid);
//
//	IContactPtr returnObj = IContactPtr();
//
//	for (std::vector<IContactPtr>::iterator it = coreContactList.begin(); it != coreContactList.end(); it++)
//	{
//		if (pointerValue == (jlong) it->get())
//		{
//			returnObj = *it;
//			break;
//		}
//	}
//	if(!returnObj)
//	{
//		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "Related contact not found in contact list !!!");
//	}
//
//	return returnObj;
//}
//IConversationThreadPtr OpenPeerCoreManager::getConversationThreadFromList(jobject javaObject)
//{
//	JNIEnv *jni_env = getEnv();
//	jclass cls = findClass("com/openpeer/javaapi/OPConversationThread");
//	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
//	jlong pointerValue = jni_env->GetLongField(javaObject, fid);
//
//	IConversationThreadPtr returnObj = IConversationThreadPtr();
//
//	for (std::vector<IConversationThreadPtr>::iterator it = coreConversationThreadList.begin(); it != coreConversationThreadList.end(); it++)
//	{
//		if (pointerValue == (jlong) it->get())
//		{
//			returnObj = *it;
//			break;
//		}
//	}
//	if(!returnObj)
//	{
//		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "Related conversation thread not found in conversation thread list !!!");
//	}
//
//	return returnObj;
//}
//IIdentityPtr OpenPeerCoreManager::getIdentityFromList(jobject javaObject)
//{
//	JNIEnv *jni_env = getEnv();
//	jclass cls = findClass("com/openpeer/javaapi/OPIdentity");
//	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
//	jlong pointerValue = jni_env->GetLongField(javaObject, fid);
//
//	IIdentityPtr returnObj = IIdentityPtr();
//
//	for (std::vector<IIdentityPtr>::iterator it = coreIdentityList.begin(); it != coreIdentityList.end(); it++)
//	{
//		if (pointerValue == (jlong) it->get())
//		{
//			returnObj = *it;
//			break;
//		}
//	}
//	if(!returnObj)
//	{
//		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "Related identity not found in identity list = %p!!!",javaObject);
//	}
//
//	return returnObj;
//}

jobject OpenPeerCoreManager::getJavaEnumObject(String enumClassName, jint index)
{
	JNIEnv *jni_env = getEnv();

	jclass cls = findClass(enumClassName.c_str());
	jmethodID valuesMethodID = jni_env->GetStaticMethodID(cls, "values", ("()[L" + enumClassName  + ";").c_str());
	jobjectArray valuesArray = (jobjectArray)jni_env->CallStaticObjectMethod(cls, valuesMethodID);
	jobject returnObj = jni_env->GetObjectArrayElement(valuesArray, index);

	return returnObj;
}

jint OpenPeerCoreManager::getIntValueFromEnumObject(jobject enumObject, String enumClassName)
{
	JNIEnv *jni_env = getEnv();

	jclass cls = findClass(enumClassName.c_str());
	jmethodID ordinalMethodID = jni_env->GetMethodID(cls, "ordinal", "()I");
	jint intValue = (jint) jni_env->CallIntMethod(enumObject, ordinalMethodID);

	return intValue;
}

String OpenPeerCoreManager::getObjectClassName (jobject delegate)
{
	String ret;
	JNIEnv *env = getEnv();

	jclass cls = env->GetObjectClass(delegate);

	// First get the class object
	jmethodID mid = env->GetMethodID(cls, "getClass", "()Ljava/lang/Class;");
	jobject clsObj = env->CallObjectMethod(delegate, mid);

	// Now get the class object's class descriptor
	cls = env->GetObjectClass(clsObj);

	// Find the getName() method on the class object
	mid = env->GetMethodID(cls, "getName", "()Ljava/lang/String;");

	// Call the getName() to get a jstring object back
	jstring strObj = (jstring)env->CallObjectMethod(clsObj, mid);

	// Now get the c string from the java jstring object
	ret = env->GetStringUTFChars(strObj, NULL);

	// Print the class name
	printf("\nCalling class is: %s\n", ret.c_str());


	// Release the memory pinned char array
	env->ReleaseStringUTFChars(strObj, ret);
	return ret;
}

void OpenPeerCoreManager::shutdown()
{

}
