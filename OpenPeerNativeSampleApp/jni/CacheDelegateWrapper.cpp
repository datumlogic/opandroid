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
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "fetch", "(Ljava/lang/String;)V");
	object = jni_env->CallStaticObjectMethod(cls, method, cookieJavaString);

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

	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "store", "(Ljava/lang/String;ILjava/lang/String;)V");
	jni_env->CallStaticVoidMethod(cls, method);

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

	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "clear", "(Ljava/lang/String;)V");
	jni_env->CallStaticVoidMethod(cls, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

CacheDelegateWrapper::~CacheDelegateWrapper()
{

}
