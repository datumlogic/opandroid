#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "globals.h"
#include "CacheDelegateWrapper.h"


//ICacheDelegate implementation
zsLib::String CacheDelegateWrapper::fetch(const char *cookieNamePath)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring cookieJavaString;
	const char *fetchedStr;

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return "";
	}

	cookieJavaString =  jni_env->NewStringUTF(cookieNamePath);
	method = jni_env->GetMethodID(gCallbackClass, "fetch", "(Ljava/lang/String;)Ljava/lang/String");
	object = jni_env->CallObjectMethod(gCallbackClass, method, cookieJavaString);

	cls = findClass("java/lang/String");
	if(jni_env->IsInstanceOf(object, cls) == JNI_TRUE)
	{
		fetchedStr = jni_env->GetStringUTFChars((jstring)object, NULL);
//		if (cookieNamePathStr == NULL) {
//			return cookieNamePathStr;
//		}

	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();

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

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}
	//cls = jni_env->FindClass("com/openpeer/delegates/OPStackMessageQueueDelegate");
	method = jni_env->GetMethodID(gCallbackClass, "store", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void CacheDelegateWrapper::clear(const char *cookieNamePath)
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
	//cls = jni_env->FindClass("com/openpeer/delegates/OPStackMessageQueueDelegate");
	method = jni_env->GetMethodID(gCallbackClass, "clear", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

CacheDelegateWrapper::~CacheDelegateWrapper()
{

}
