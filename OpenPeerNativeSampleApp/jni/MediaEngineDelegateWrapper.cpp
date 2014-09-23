#include "MediaEngineDelegateWrapper.h"
#include "OpenPeerCoreManager.h"
#include "android/log.h"
#include "globals.h"


//IMediaEngineDelegate implementation
MediaEngineDelegateWrapper::MediaEngineDelegateWrapper(jobject delegate)
{
	JNIEnv *jni_env = getEnv();
	javaDelegate = jni_env->NewGlobalRef(delegate);
}

void MediaEngineDelegateWrapper::onMediaEngineAudioRouteChanged(openpeer::core::IMediaEngine::OutputAudioRoutes audioRoute)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onMediaEngineAudioRouteChanged");

	bool attached = false;
	switch (android_jvm->GetEnv((void**)&jni_env, JNI_VERSION_1_6))
	{
	case JNI_OK:
		break;
	case JNI_EDETACHED:
		if (android_jvm->AttachCurrentThread(&jni_env, NULL)!=0)
		{
			throw std::runtime_error("Could not attach current thread");
		}
		attached = true;
		break;
	case JNI_EVERSION:
		throw std::runtime_error("Invalid java version");
	}

	if (javaDelegate != NULL)
	{
		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		cls = findClass(className.c_str());
		method = jni_env->GetMethodID(cls, "onMediaEngineAudioRouteChanged", "(I)V");
		jni_env->CallVoidMethod(javaDelegate, method, (jint) audioRoute);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onMediaEngineAudioRouteChanged Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	if(attached)
	{
		android_jvm->DetachCurrentThread();
	}

}
void MediaEngineDelegateWrapper::onMediaEngineAudioSessionInterruptionBegan()
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onMediaEngineAudioSessionInterruptionBegan");

	bool attached = false;
	switch (android_jvm->GetEnv((void**)&jni_env, JNI_VERSION_1_6))
	{
	case JNI_OK:
		break;
	case JNI_EDETACHED:
		if (android_jvm->AttachCurrentThread(&jni_env, NULL)!=0)
		{
			throw std::runtime_error("Could not attach current thread");
		}
		attached = true;
		break;
	case JNI_EVERSION:
		throw std::runtime_error("Invalid java version");
	}
	if (javaDelegate != NULL)
	{
		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		cls = findClass(className.c_str());
		method = jni_env->GetMethodID(cls, "onMediaEngineAudioSessionInterruptionBegan", "()V");
		jni_env->CallVoidMethod(javaDelegate, method);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onMediaEngineAudioSessionInterruptionBegan Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	if(attached)
	{
		android_jvm->DetachCurrentThread();
	}
}
void MediaEngineDelegateWrapper::onMediaEngineAudioSessionInterruptionEnded()
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onMediaEngineAudioSessionInterruptionEnded");

	bool attached = false;
	switch (android_jvm->GetEnv((void**)&jni_env, JNI_VERSION_1_6))
	{
	case JNI_OK:
		break;
	case JNI_EDETACHED:
		if (android_jvm->AttachCurrentThread(&jni_env, NULL)!=0)
		{
			throw std::runtime_error("Could not attach current thread");
		}
		attached = true;
		break;
	case JNI_EVERSION:
		throw std::runtime_error("Invalid java version");
	}

	if (javaDelegate != NULL)
	{
		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		cls = findClass(className.c_str());
		method = jni_env->GetMethodID(cls, "onMediaEngineAudioSessionInterruptionEnded", "()V");
		jni_env->CallVoidMethod(javaDelegate, method);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onMediaEngineAudioSessionInterruptionEnded Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	if(attached)
	{
		android_jvm->DetachCurrentThread();
	}
}

void MediaEngineDelegateWrapper::onMediaEngineFaceDetected()
{

	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onMediaEngineFaceDetected");

	bool attached = false;
	switch (android_jvm->GetEnv((void**)&jni_env, JNI_VERSION_1_6))
	{
	case JNI_OK:
		break;
	case JNI_EDETACHED:
		if (android_jvm->AttachCurrentThread(&jni_env, NULL)!=0)
		{
			throw std::runtime_error("Could not attach current thread");
		}
		attached = true;
		break;
	case JNI_EVERSION:
		throw std::runtime_error("Invalid java version");
	}

	if (javaDelegate != NULL)
	{
		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		cls = findClass(className.c_str());
		method = jni_env->GetMethodID(cls, "onMediaEngineFaceDetected", "()V");
		jni_env->CallVoidMethod(javaDelegate, method);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onMediaEngineFaceDetected Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	if(attached)
	{
		android_jvm->DetachCurrentThread();
	}
}
void MediaEngineDelegateWrapper::onMediaEngineVideoCaptureRecordStopped()
{

	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onMediaEngineVideoCaptureRecordStopped");

	bool attached = false;
	switch (android_jvm->GetEnv((void**)&jni_env, JNI_VERSION_1_6))
	{
	case JNI_OK:
		break;
	case JNI_EDETACHED:
		if (android_jvm->AttachCurrentThread(&jni_env, NULL)!=0)
		{
			throw std::runtime_error("Could not attach current thread");
		}
		attached = true;
		break;
	case JNI_EVERSION:
		throw std::runtime_error("Invalid java version");
	}
	if (javaDelegate != NULL)
	{
		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		cls = findClass(className.c_str());
		method = jni_env->GetMethodID(cls, "onMediaEngineVideoCaptureRecordStopped", "()V");
		jni_env->CallVoidMethod(javaDelegate, method);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onMediaEngineVideoCaptureRecordStopped Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}
	if(attached)
	{
		android_jvm->DetachCurrentThread();
	}
}


MediaEngineDelegateWrapper::~MediaEngineDelegateWrapper()
{
	JNIEnv *jni_env = getEnv();
	jni_env->DeleteGlobalRef(javaDelegate);

}
