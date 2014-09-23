#include <jni.h>
#include "AccountDelegateWrapper.h"
#include "CacheDelegateWrapper.h"
#include "SettingsDelegateWrapper.h"
#include "CallDelegateWrapper.h"
#include "ConversationThreadDelegateWrapper.h"
#include "IdentityDelegateWrapper.h"
#include "IdentityLookupDelegateWrapper.h"
#include "StackDelegateWrapper.h"
#include "StackMessageQueueDelegateWrapper.h"
#include "LoggerDelegateWrapper.h"
#include "MediaEngineDelegateWrapper.h"
#include "BackgroundingDelegateWrapper.h"
#include "BackgroundingCompletionDelegateWrapper.h"

#ifndef _ANDROID_OPENPEER_GLOBALS_H_
#define _ANDROID_OPENPEER_GLOBALS_H_

extern JavaVM *android_jvm;
static JNIEnv* gEnv;
extern jobject jni_object;
static jobject gClassLoader;
static jmethodID gFindClassMethod;

static jclass gCallbackClass;
static jobject globalAccount;


extern IMediaEnginePtr mediaEnginePtr;
extern SettingsDelegateWrapperPtr settingsDelegatePtr;
extern CacheDelegateWrapperPtr cacheDelegatePtr;
extern CallDelegateWrapperPtr callDelegatePtr;
extern ConversationThreadDelegateWrapperPtr conversationThreadDelegatePtr;
extern LoggerDelegateWrapperPtr loggerDelegatePtr;
extern MediaEngineDelegateWrapperPtr mediaEngineDelegatePtr;
extern BackgroundingDelegateWrapperPtr backgroundingDelegatePtr;
extern BackgroundingCompletionDelegateWrapperPtr backgroundingCompletionDelegatePtr;

jclass findClass(const char* name);

JNIEnv* getEnv();

#endif
