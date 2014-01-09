#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "../../../opios/libs/op/openpeer/core/IStack.h"
#include "../../../opios/libs/op/openpeer/core/ILogger.h"

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
    ILogger::setLogLevel(ILogger::Trace);
    ILogger::installTelnetLogger(59999, 60, true);

	return object;

}

extern "C" JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStackMessageQueue_interceptProcessing
  (JNIEnv *, jobject, jobject)
{
	IStackMessageQueuePtr queue = IStackMessageQueue::singleton();
	queue->interceptProcessing(globalEventManager);

}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    android_jvm = vm;

    return JNI_VERSION_1_6;
}
