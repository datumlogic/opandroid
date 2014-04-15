#include "OpenPeerCoreManager.h"
#include "globals.h"
#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include <android/log.h>;

std::vector<IContactPtr> OpenPeerCoreManager::coreContactList;
std::vector<ICallPtr> OpenPeerCoreManager::coreCallList;
std::vector<IConversationThreadPtr> OpenPeerCoreManager::coreConversationThreadList;
std::vector<IIdentityPtr> OpenPeerCoreManager::coreIdentityList;


ICallPtr OpenPeerCoreManager::getCallFromList(jobject javaObject)
{
	JNIEnv *jni_env = getEnv();
	jclass cls = findClass("com/openpeer/javaapi/OPCall");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeAccountPointer", "J");
	jlong pointerValue = jni_env->GetLongField(javaObject, fid);

	ICallPtr returnObj = ICallPtr();

	for (std::vector<ICallPtr>::iterator it = coreCallList.begin(); it != coreCallList.end(); it++)
	{
		if (pointerValue == (jlong) it->get() )
		{
			returnObj = *it;
			break;
		}
	}
	if(!returnObj)
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "Related call not found in call list !!!");
	}

	return returnObj;
}

IContactPtr OpenPeerCoreManager::getContactFromList(jobject javaObject)
{
	JNIEnv *jni_env = getEnv();
	jclass cls = findClass("com/openpeer/javaapi/OPContact");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeAccountPointer", "J");
	jlong pointerValue = jni_env->GetLongField(javaObject, fid);

	IContactPtr returnObj = IContactPtr();

	for (std::vector<IContactPtr>::iterator it = coreContactList.begin(); it != coreContactList.end(); it++)
	{
		if (pointerValue == (jlong) it->get())
		{
			returnObj = *it;
			break;
		}
	}
	if(!returnObj)
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "Related contact not found in contact list !!!");
	}

	return returnObj;
}
IConversationThreadPtr OpenPeerCoreManager::getConversationThreadFromList(jobject javaObject)
{
	JNIEnv *jni_env = getEnv();
	jclass cls = findClass("com/openpeer/javaapi/OPConversationThread");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeAccountPointer", "J");
	jlong pointerValue = jni_env->GetLongField(javaObject, fid);

	IConversationThreadPtr returnObj = IConversationThreadPtr();

	for (std::vector<IConversationThreadPtr>::iterator it = coreConversationThreadList.begin(); it != coreConversationThreadList.end(); it++)
	{
		if (pointerValue == (jlong) it->get())
		{
			returnObj = *it;
			break;
		}
	}
	if(!returnObj)
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "Related conversation thread not found in conversation thread list !!!");
	}

	return returnObj;
}
IIdentityPtr OpenPeerCoreManager::getIdentityFromList(jobject javaObject)
{
	JNIEnv *jni_env = getEnv();
	jclass cls = findClass("com/openpeer/javaapi/OPIdentity");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeAccountPointer", "J");
	jlong pointerValue = jni_env->GetLongField(javaObject, fid);

	IIdentityPtr returnObj = IIdentityPtr();

	for (std::vector<IIdentityPtr>::iterator it = coreIdentityList.begin(); it != coreIdentityList.end(); it++)
	{
		if (pointerValue == (jlong) it->get())
		{
			returnObj = *it;
			break;
		}
	}
	if(!returnObj)
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "Related identity not found in identity list !!!");
	}

	return returnObj;
}

void OpenPeerCoreManager::shutdown()
{

}
