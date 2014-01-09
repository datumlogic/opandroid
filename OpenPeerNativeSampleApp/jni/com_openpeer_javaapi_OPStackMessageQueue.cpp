#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"


#include "globals.h"
//#define NULL ((void*) 0)


using namespace openpeer::core;

extern "C" JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPStackMessageQueue_singleton
  (JNIEnv *env, jclass)
{
	jint i;
	jobject object;
	jmethodID singleton;
	jclass cls;

	//IStackMessageQueuePtr queue = IStackMessageQueue::singleton();

	//cls = env->FindClass("com/openpeer/javaapi/OPStackMessageQueue");

	//what should put as the second parameter? Is my try correct, according to what
	//you can find in .java file? I used this documentation: http://download.oracle.com/javase/6/docs/technotes/guides/jni/spec/functions.html#wp16027

	//singleton = env->GetStaticMethodID(cls, "singleton", "()Lcom/openpeer/javaapi/OPStackMessageQueue;");
	//http://download.oracle.com/javase/6/docs/technotes/guides/jni/spec/functions.html#wp16660
	//Again, is the last parameter ok?

	//object = env->CallStaticObjectMethod(cls, singleton);
	//I want to assign "5" and "6" to point.x and point.y respectfully.

	return object;

}

extern "C" JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStackMessageQueue_interceptProcessing
  (JNIEnv *, jobject, jobject)
{

	IStackMessageQueuePtr queue = IStackMessageQueue::singleton();
	if (globalEventManager) {
		queue->interceptProcessing(globalEventManager);
	}

}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    android_jvm = vm;
    //JNIEnv* env;// = getEnv();

    int getEnvStat = android_jvm->GetEnv((void **)&gEnv, JNI_VERSION_1_6);
	if (getEnvStat == JNI_EDETACHED) {
		std::cout << "GetEnv: not attached" << std::endl;
		if (android_jvm->AttachCurrentThread(&gEnv, NULL) != 0) {
			std::cout << "Failed to attach" << std::endl;
		}
	} else if (getEnvStat == JNI_OK) {
		//
	} else if (getEnvStat == JNI_EVERSION) {
		std::cout << "GetEnv: version not supported" << std::endl;
	}

	jclass randomClass = gEnv->FindClass("com/openpeer/delegates/CallbackHandler");
	jclass classClass = gEnv->FindClass("java/lang/Class");
	jclass classLoaderClass = gEnv->FindClass("java/lang/ClassLoader");
	jmethodID getClassLoaderMethod = gEnv->GetMethodID(classClass, "getClassLoader",
											 "()Ljava/lang/ClassLoader;");

	jobject tmpClassLoader = gEnv->CallObjectMethod(randomClass, getClassLoaderMethod);
	gClassLoader = (jclass)gEnv->NewGlobalRef(tmpClassLoader);

	gFindClassMethod = gEnv->GetMethodID(classLoaderClass, "findClass",
									"(Ljava/lang/String;)Ljava/lang/Class;");



    //jclass tmp = gEnv->FindClass("com/openpeer/delegates/CallbackHandler");
    //gCallbackClass = (jclass)gEnv->NewGlobalRef(tmp);

    return JNI_VERSION_1_6;
}

JNIEnv* getEnv() {
    JNIEnv *env;
    int status = android_jvm->GetEnv((void**)&env, JNI_VERSION_1_6);
    if(status < 0) {
        status = android_jvm->AttachCurrentThread(&env, NULL);
        if(status < 0) {
            return NULL;
        }
    }
    return env;
}

jclass findClass(const char* name) {
    return static_cast<jclass>(getEnv()->CallObjectMethod(gClassLoader, gFindClassMethod, getEnv()->NewStringUTF(name)));
}

