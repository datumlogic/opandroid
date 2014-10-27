#include "PushPresenceDatabaseAbstractionDelegateWrapper.h"
#include "OpenPeerCoreManager.h"
#include "android/log.h"
#include "globals.h"


//PushPresenceDatabaseAbstractionDelegateWrapper implementation
PushPresenceDatabaseAbstractionDelegateWrapper::PushPresenceDatabaseAbstractionDelegateWrapper(jobject delegate)
{
	JNIEnv *jni_env = getEnv();
	javaDelegate = jni_env->NewGlobalRef(delegate);
}

//IPushPresenceDatabaseAbstractionDelegate implementation
//TODO Add base delegate method implementation


PushPresenceDatabaseAbstractionDelegateWrapper::~PushPresenceDatabaseAbstractionDelegateWrapper()
{
	JNIEnv *jni_env = getEnv();
	jni_env->DeleteGlobalRef(javaDelegate);

}
