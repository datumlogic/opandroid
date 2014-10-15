#include "PushMessagingDatabaseAbstractionDelegateWrapper.h"
#include "OpenPeerCoreManager.h"
#include "android/log.h"
#include "globals.h"


//PushMessagingDatabaseAbstractionDelegateWrapper implementation
PushMessagingDatabaseAbstractionDelegateWrapper::PushMessagingDatabaseAbstractionDelegateWrapper(jobject delegate)
{
	JNIEnv *jni_env = getEnv();
	javaDelegate = jni_env->NewGlobalRef(delegate);
}

//IPushMessagingDatabaseAbstractionDelegate implementation
//TODO Add base delegate method implementation


PushMessagingDatabaseAbstractionDelegateWrapper::~PushMessagingDatabaseAbstractionDelegateWrapper()
{
	JNIEnv *jni_env = getEnv();
	jni_env->DeleteGlobalRef(javaDelegate);

}
