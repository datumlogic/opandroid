#include "CallDelegateWrapper.h"
#include "OpenPeerCoreManager.h"
#include "android/log.h"
#include "globals.h"




//ICallDelegate implementation
CallDelegateWrapper::CallDelegateWrapper(jobject delegate)
{
	JNIEnv *jni_env = getEnv();
	javaDelegate = jni_env->NewGlobalRef(delegate);
}

void CallDelegateWrapper::onCallStateChanged(ICallPtr call, ICall::CallStates state)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "onCallStateChanged state = %d", (jint)state);

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	if (javaDelegate != NULL){
		//String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		//create new OPCall java object
		cls = findClass("com/openpeer/javaapi/OPCall");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		jobject callObject = jni_env->NewObject(cls, method);

		//fill new field with pointer to core pointer
		ICallPtr* ptrToCall = new boost::shared_ptr<ICall>(call);
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jni_env->SetLongField(callObject, fid, (jlong)ptrToCall);

		jclass clas = jni_env->GetObjectClass(javaDelegate);

		// First get the class object
		jmethodID mid = jni_env->GetMethodID(clas, "getClass", "()Ljava/lang/Class;");
		jobject clsObj = jni_env->CallObjectMethod(javaDelegate, mid);

		// Now get the class object's class descriptor
		clas = jni_env->GetObjectClass(clsObj);

		// Find the getName() method on the class object
		mid = jni_env->GetMethodID(clas, "getName", "()Ljava/lang/String;");

		// Call the getName() to get a jstring object back
		jstring strObj = (jstring)jni_env->CallObjectMethod(clsObj, mid);

		// Now get the c string from the java jstring object
		const char* str = jni_env->GetStringUTFChars(strObj, NULL);

		// Print the class name
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "Calling class is: %s", str);

		jclass callbackClass = findClass(str);
		method = jni_env->GetMethodID(callbackClass, "onCallStateChanged", "(Lcom/openpeer/javaapi/OPCall;Lcom/openpeer/javaapi/CallStates;)V");
		jni_env->CallVoidMethod(javaDelegate, method,callObject, OpenPeerCoreManager::getJavaEnumObject("com/openpeer/javaapi/CallStates", (jint) state));
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onCallStateChanged Java delegate is NULL !!!");
	}
	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

CallDelegateWrapper::~CallDelegateWrapper()
{
	JNIEnv *jni_env = getEnv();
	jni_env->DeleteGlobalRef(javaDelegate);

}
