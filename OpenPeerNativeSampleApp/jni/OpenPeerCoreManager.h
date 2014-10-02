//#include "openpeer/core/IStack.h"
//#include "openpeer/core/IAccount.h"
//#include "openpeer/core/IIdentity.h"
//#include "openpeer/core/IIdentityLookup.h"
//#include "openpeer/core/IConversationThread.h"
//#include "openpeer/core/ICall.h"
//#include "openpeer/core/IMediaEngine.h"
//#include "openpeer/core/ILogger.h"
#include <vector>
#include "globals.h"
//#define NULL ((void*) 0)

#ifndef _ANDROID_OPENPEER_CORE_MANAGER_H_
#define _ANDROID_OPENPEER_CORE_MANAGER_H_

using namespace openpeer::core;

class OpenPeerCoreManager {
public:

	static jobject getJavaEnumObject(String enumClassName, jint index);
	static jint getIntValueFromEnumObject(jobject enumObject, String enumClassName);

	static String getObjectClassName (jobject delegate);

	static void fillJavaTokenFromCoreObject(jobject javaToken, IIdentity::Token coreToken);

public:
	static IStackMessageQueuePtr queuePtr;
	static ISettingsPtr settingsPtr;
	static ICachePtr cachePtr;

};

#endif //_ANDROID_OPENPEER_CORE_MANAGER_H_
