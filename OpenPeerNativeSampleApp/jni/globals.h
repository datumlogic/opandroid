#include <jni.h>
#include "EventManager.h"
#include "AccountDelegateWrapper.h"
#include "CacheDelegateWrapper.h"
#include "SettingsDelegateWrapper.h"
#include "CallDelegateWrapper.h"
#include "ConversationThreadDelegateWrapper.h"

#ifndef _ANDROID_OPENPEER_GLOBALS_H_
#define _ANDROID_OPENPEER_GLOBALS_H_

extern JavaVM *android_jvm;
static JNIEnv* gEnv;
extern jobject jni_object;
static jobject gClassLoader;
static jmethodID gFindClassMethod;

static std::map<jobject, IIdentityPtr> identityMap;
static std::map<jobject, ICallPtr> callMap;
static std::map<jobject, IConversationThreadPtr> conversationThreadMap;
static std::map<jobject, IContactPtr> contactMap;

//single instance objects in pairs
static std::pair<jobject, IStackPtr> stackPair;

static jclass gCallbackClass;
static jobject globalAccount;

extern EventManagerPtr globalEventManager;
//extern IAccountPtr accountPtr;
//extern IStackPtr stackPtr;
extern IStackMessageQueuePtr queuePtr;
//extern IIdentityPtr identityPtr;
//extern IIdentityLookupPtr identityLookupPtr;
extern IMediaEnginePtr mediaEnginePtr;
extern SettingsDelegateWrapperPtr settingsDelegatePtr;
extern AccountDelegateWrapperPtr accountDelegatePtr;
extern CacheDelegateWrapperPtr cacheDelegatePtr;
extern CallDelegateWrapperPtr callDelegatePtr;
extern ConversationThreadDelegateWrapperPtr conversationThreadDelegatePtr;

#endif
