#include "EventManager.h"
#include "globals.h"
#include "com_openpeer_javaapi_OPStackMessageQueue.h"

//IStackMessageQueueDelegate implementation
void EventManager::onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread()
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
		method = jni_env->GetMethodID(gCallbackClass, "onJniCallback", "()V");
		jni_env->CallVoidMethod(gCallbackClass, method);

		if (jni_env->ExceptionCheck()) {
			jni_env->ExceptionDescribe();
		}

		android_jvm->DetachCurrentThread();

		IStackMessageQueue::singleton()->notifyProcessMessageFromCustomThread();
	}

//IStackDelegate implementation
void EventManager::onStackShutdown(openpeer::core::IStackAutoCleanupPtr)
{
	int i = 0;
	i++;
}

//IMediaEngine implementation
void EventManager::onMediaEngineAudioRouteChanged(openpeer::core::IMediaEngine::OutputAudioRoutes audioRoute)
{

}
void EventManager::onMediaEngineFaceDetected()
{

}
void EventManager::onMediaEngineVideoCaptureRecordStopped()
{

}

//IAccountDelegate implementation
void EventManager::onAccountStateChanged(IAccountPtr account, IAccount::AccountStates state)
{

	jclass cls;
	jmethodID method;
	jobject object;
	//JNIEnv *jni_env = 0;

	jint attach_result = android_jvm->AttachCurrentThread(&gEnv, NULL);
	if (attach_result < 0 || gEnv == 0)
	{
	   return;
	}

	jclass callbackClass = findClass("com/openpeer/delegates/CallbackHandler");
	method = gEnv->GetStaticMethodID(callbackClass, "onAccountStateChanged", "(I)V");
	gEnv->CallStaticVoidMethod(callbackClass, method, (jint) state);

	if (gEnv->ExceptionCheck()) {
		gEnv->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
	/*
	jint attach_result = android_jvm->AttachCurrentThread(&gEnv, NULL);
	if (attach_result < 0 || gEnv == 0)
	{
	   return;
	}

	// get the callback handler class
	  //javaCallbackHandlerFields.callbackHandlerClass = (jclass) env->NewGlobalRef(env->FindClass(_kClassPathCallbackHandler));


	cls = (jclass)gEnv->NewGlobalRef(gEnv->FindClass("com/openpeer/javaapi/OPAccountDelegate"));

	  if (!cls) {
	    //__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Register: failed to get class for: %s", _kClassPathCallbackHandler);
	    return;
	  }
	jmethodID constr = gEnv->GetMethodID(cls,
		      "<init>","()V");
	jobject delegate = gEnv->NewObject(
			cls,
			constr);


	method = gEnv->GetMethodID(cls, "onAccountStateChanged", "(Lcom/openpeer/javaapi/OPAccount;Lcom/openpeer/javaapi/AccountStates;)V");
	gEnv->CallVoidMethod(delegate, method, NULL, NULL);

	//IStackMessageQueue::singleton()->notifyProcessMessageFromCustomThread();
*/


}
void EventManager::onAccountAssociatedIdentitiesChanged(IAccountPtr account)
{

}
void EventManager::onAccountPendingMessageForInnerBrowserWindowFrame(IAccountPtr account)
{

}

//IConversationThreadDelegate implementation
void EventManager::onConversationThreadNew(IConversationThreadPtr conversationThread)
{

}
void EventManager::onConversationThreadContactsChanged(IConversationThreadPtr conversationThread)
{

}
void EventManager::onConversationThreadContactStateChanged(
	                                                     IConversationThreadPtr conversationThread,
	                                                     IContactPtr contact,
	                                                     IConversationThread::ContactStates state
	                                                     )
{

}
void EventManager::onConversationThreadMessage(
	                                         IConversationThreadPtr conversationThread,
	                                         const char *messageID
	                                        )
{

}
void EventManager::onConversationThreadMessageDeliveryStateChanged(
	                                                             IConversationThreadPtr conversationThread,
	                                                             const char *messageID,
	                                                             IConversationThread::MessageDeliveryStates state
	                                                             )
{

}
void EventManager::onConversationThreadPushMessage(
	                                             IConversationThreadPtr conversationThread,
	                                             const char *messageID,
	                                             IContactPtr contact
	                                             )
{

}

//ICallDelegate implementation
void EventManager::onCallStateChanged(ICallPtr call, ICall::CallStates state)
{

}

//IIdentityDelegate implementation
void EventManager::onIdentityStateChanged(
	                                          IIdentityPtr identity,
	                                          IIdentity::IdentityStates state
	                                          )
{
		jclass cls;
		jmethodID method;
		jobject object;
		//JNIEnv *jni_env = 0;

		jint attach_result = android_jvm->AttachCurrentThread(&gEnv, NULL);
		if (attach_result < 0 || gEnv == 0)
		{
		   return;
		}

		jclass callbackClass = findClass("com/openpeer/delegates/CallbackHandler");
		method = gEnv->GetStaticMethodID(callbackClass, "onIdentityStateChanged", "(I)V");
		gEnv->CallStaticVoidMethod(callbackClass, method, (jint) state);

		if (gEnv->ExceptionCheck()) {
			gEnv->ExceptionDescribe();
		}

		android_jvm->DetachCurrentThread();
}
void EventManager::onIdentityPendingMessageForInnerBrowserWindowFrame(IIdentityPtr identity)
{

}
void EventManager::onIdentityRolodexContactsDownloaded(IIdentityPtr identity)
{

}

EventManager::~EventManager()
{
	int i = 0;
	i++;
}
