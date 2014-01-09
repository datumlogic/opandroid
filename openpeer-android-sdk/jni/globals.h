#include <jni.h>
#include "EventManager.h"

#ifndef _ANDROID_OPENPEER_GLOBALS_H_
#define _ANDROID_OPENPEER_GLOBALS_H_

extern JavaVM *android_jvm;
extern jobject jni_object;

extern EventManagerPtr globalEventManager;

#endif
