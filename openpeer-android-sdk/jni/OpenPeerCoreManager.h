#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/IAccount.h"
#include "openpeer/core/IIdentity.h"
#include "openpeer/core/IIdentityLookup.h"
#include "openpeer/core/IConversationThread.h"
#include "openpeer/core/ICall.h"
#include "openpeer/core/IMediaEngine.h"
#include "openpeer/core/ILogger.h"
#include <vector>
//#define NULL ((void*) 0)

#ifndef _ANDROID_OPENPEER_CORE_MANAGER_H_
#define _ANDROID_OPENPEER_CORE_MANAGER_H_

using namespace openpeer::core;

class OpenPeerCoreManager {
public:
	//returns proper shared pointer based on raw ptr saved in Java
	static ICallPtr getCallFromList(jobject javaObject);
	static IContactPtr getContactFromList(jobject javaObject);
	static IConversationThreadPtr getConversationThreadFromList(jobject javaObject);
	static IIdentityPtr getIdentityFromList(jobject javaObject);

	static jobject getJavaEnumObject(String enumClassName, jint index);
	static jint getIntValueFromEnumObject(jobject enumObject, String enumClassName);

	static String getObjectClassName (jobject delegate);

	static void shutdown();


public:
	static IAccountPtr accountPtr;
	static IStackPtr stackPtr;
	static IStackMessageQueuePtr queuePtr;
	static IIdentityLookupPtr identityLookupPtr;
	static IMediaEnginePtr mediaEnginePtr;
	static ISettingsPtr settingsPtr;
	static ICachePtr cachePtr;

	//Lists of objects
	static std::vector<IContactPtr> coreContactList;
	static std::vector<ICallPtr> coreCallList;
	static std::vector<IConversationThreadPtr> coreConversationThreadList;
	static std::vector<IIdentityPtr> coreIdentityList;

};

#endif //_ANDROID_OPENPEER_CORE_MANAGER_H_
