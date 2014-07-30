#include "StackMessageQueueDelegateWrapper.h"
#include "OpenPeerCoreManager.h"
#include "android/log.h"
#include "globals.h"


//IStackDelegate implementation
StackMessageQueueDelegateWrapper::StackMessageQueueDelegateWrapper(jobject delegate)
{
	JNIEnv *jni_env = getEnv();
	javaDelegate = jni_env->NewGlobalRef(delegate);
}

//IStackMessageQueueDelegate implementation
void StackMessageQueueDelegateWrapper::onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread()
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread called");

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}
	if (javaDelegate != NULL)
	{
		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		jclass callbackClass = findClass(className.c_str());
		method = jni_env->GetMethodID(callbackClass, "onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread", "()V");
		jni_env->CallVoidMethod(javaDelegate, method);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

StackMessageQueueDelegateWrapper::~StackMessageQueueDelegateWrapper()
{
	JNIEnv *jni_env = getEnv();
	jni_env->DeleteGlobalRef(javaDelegate);

}
