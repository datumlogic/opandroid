#include "globals.h"

JavaVM *android_jvm;
jobject jni_object;

EventManagerPtr globalEventManager = EventManagerPtr(new EventManager());

IAccountPtr accountPtr;
IStackPtr stackPtr;
IStackMessageQueuePtr queuePtr;
IIdentityPtr identityPtr;
