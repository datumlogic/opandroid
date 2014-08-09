#include "LoggerDelegateWrapper.h"
#include "OpenPeerCoreManager.h"
#include "android/log.h"
#include "globals.h"


//ILoggerDelegate implementation
LoggerDelegateWrapper::LoggerDelegateWrapper(jobject delegate)
{
	JNIEnv *jni_env = getEnv();
	javaDelegate = jni_env->NewGlobalRef(delegate);
}

//ILoggerDelegate implementation
void LoggerDelegateWrapper::onNewSubsystem(
		SubsystemID subsystemUniqueID,
		const char *subsystemName
)
{
	jclass cls;
	jmethodID method;
	jobject object;
	jstring subsystemNameString;
	JNIEnv *jni_env = 0;

	subsystemNameString = jni_env->NewStringUTF(subsystemName);

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "onNewSubsystem called");

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
		method = jni_env->GetMethodID(cls, "onNewSubsystem", "(ILjava/lang/String;)V");
		jni_env->CallVoidMethod(javaDelegate, method, (jint) subsystemUniqueID, subsystemNameString);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onNewSubsystem Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	if(attached)
	{
		android_jvm->DetachCurrentThread();
	}
}
void LoggerDelegateWrapper::onLog(
		SubsystemID subsystemUniqueID,
		const char *subsystemName,
		Severity severity,
		Level level,
		const char *message,
		const char *function,
		const char *filePath,
		ULONG lineNumber
)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	jstring subsystemNameString;
	jstring messageString;
	jstring functionString;
	jstring filePathString;

	subsystemNameString = jni_env->NewStringUTF(subsystemName);
	messageString = jni_env->NewStringUTF(message);
	functionString = jni_env->NewStringUTF(function);
	filePathString = jni_env->NewStringUTF(filePath);

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "onLog called");

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
		method = jni_env->GetMethodID(cls, "onLog", "(ILjava/lang/String;Lcom/openpeer/javaapi/OPLogSeverity;Lcom/openpeer/javaapi/OPLogLevel;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V");
		jni_env->CallVoidMethod(javaDelegate, method,
				(jint) subsystemUniqueID,
				subsystemNameString,
				OpenPeerCoreManager::getJavaEnumObject("com/openpeer/javaapi/OPLogSeverity",(jint)severity ),
				OpenPeerCoreManager::getJavaEnumObject("com/openpeer/javaapi/OPLogLevel",(jint)level ),
				messageString,
				functionString,
				filePathString,
				(jlong) lineNumber);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onLog Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	if(attached)
	{
		android_jvm->DetachCurrentThread();
	}
}

LoggerDelegateWrapper::~LoggerDelegateWrapper()
{
	JNIEnv *jni_env = getEnv();
	jni_env->DeleteGlobalRef(javaDelegate);

}
