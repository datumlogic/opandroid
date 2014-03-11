#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    setup
 * Signature: (Lcom/openpeer/javaapi/OPStackDelegate;Lcom/openpeer/javaapi/OPMediaEngineDelegate;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStack_setup
  (JNIEnv *env, jobject, jobject, jobject)
{

	stackPtr = IStack::singleton();
	stackPtr->setup(globalEventManager, globalEventManager);
}

/*
 * Class:     com_openpeer_javaapi_OPStack
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPStack_shutdown
  (JNIEnv *, jobject)
{
	if (stackPtr)
	{
		stackPtr->shutdown();
	}

}

#ifdef __cplusplus
}
#endif
