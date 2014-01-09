#include "EventManager.h"
#include "globals.h"

//IStackMessageQueueDelegate implementation
void EventManager::onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread()
	{
		jclass cls;
		jmethodID method;
		jobject object;
		JNIEnv *jni_env = 0;

		jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
		if (attach_result < 0 || jni_env == 0)
		{
		   return;
		}
		cls = jni_env->FindClass("com/openpeer/javaapi/OPStackMessageQueueDelegate");
		method = jni_env->GetMethodID(cls, "onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread", "()V");
		jni_env->CallVoidMethod(cls, method);
	}

//IStackDelegate implementation
void EventManager::onStackShutdown(openpeer::core::IStackAutoCleanupPtr)
{

}

//IMediaEngine implementation
void EventManager::onMediaEngineAudioRouteChanged(openpeer::core::IMediaEngine::OutputAudioRoutes audioRoute)
{

}
void EventManager::onMediaEngineFaceDetected()
{

}
void EventManager::onMediaEngineVideoCaptureRecordStopped()
{

}
