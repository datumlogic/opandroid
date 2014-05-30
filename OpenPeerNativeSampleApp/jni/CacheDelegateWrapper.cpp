#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "globals.h"
#include "CacheDelegateWrapper.h"
#include <android/log.h>

//ICacheDelegate implementation
zsLib::String CacheDelegateWrapper::fetch(const char *cookieNamePath)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring cookieJavaString;
	const char *fetchedStr;

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

	cookieJavaString =  jni_env->NewStringUTF(cookieNamePath);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "fetch", "(Ljava/lang/String;)Ljava/lang/String;");
	object = jni_env->CallStaticObjectMethod(cls, method, cookieJavaString);

	cls = findClass("java/lang/String");
	if(jni_env->IsInstanceOf(object, cls) == JNI_TRUE)
	{
		fetchedStr = jni_env->GetStringUTFChars((jstring)object, NULL);

	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	if (attached)
	{
		android_jvm->DetachCurrentThread();
	}
	return fetchedStr;
}
void CacheDelegateWrapper::store(const char *cookieNamePath,
		Time expires,
		const char *str)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring cookieJavaString;
	jstring storeStr;

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

	cookieJavaString =  jni_env->NewStringUTF(cookieNamePath);
	storeStr =  jni_env->NewStringUTF(str);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "store", "(Ljava/lang/String;ILjava/lang/String;)V");
	jni_env->CallStaticVoidMethod(cls, method, cookieJavaString, 123456789, storeStr);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	if(attached)
	{
		android_jvm->DetachCurrentThread();
	}
}
void CacheDelegateWrapper::clear(const char *cookieNamePath)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring cookieJavaString;

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

	cookieJavaString = jni_env->NewStringUTF(cookieNamePath);
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "clear", "(Ljava/lang/String;)V");
	jni_env->CallStaticVoidMethod(cls, method, cookieJavaString);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	if(attached)
	{
		android_jvm->DetachCurrentThread();
	}
}

CacheDelegateWrapper::~CacheDelegateWrapper()
{

}
