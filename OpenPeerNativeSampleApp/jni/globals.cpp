#include "globals.h"

JavaVM *android_jvm;
jobject jni_object;

EventManagerPtr globalEventManager = EventManagerPtr(new EventManager());
SettingsDelegateWrapperPtr settingsDelegatePtr = SettingsDelegateWrapperPtr(new SettingsDelegateWrapper());
CacheDelegateWrapperPtr cacheDelegatePtr = CacheDelegateWrapperPtr(new CacheDelegateWrapper());

IAccountPtr accountPtr;
IStackPtr stackPtr;
IStackMessageQueuePtr queuePtr;
//IIdentityPtr identityPtr;
IIdentityLookupPtr identityLookupPtr;
IMediaEnginePtr mediaEnginePtr;
