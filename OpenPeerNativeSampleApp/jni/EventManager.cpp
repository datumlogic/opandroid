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
	cls = jni_env->FindClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();

	IStackMessageQueue::singleton()->notifyProcessMessageFromCustomThread();
}

//IStackDelegate implementation
void EventManager::onStackShutdown(openpeer::core::IStackAutoCleanupPtr)
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
	cls = jni_env->FindClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onStackShutdown", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

	stackPair.second.reset();
	//delete stackPair;

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

//IMediaEngine implementation
void EventManager::onMediaEngineAudioRouteChanged(openpeer::core::IMediaEngine::OutputAudioRoutes audioRoute)
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
	method = jni_env->GetMethodID(gCallbackClass, "onMediaEngineAudioRouteChanged", "(I)V");
	jni_env->CallVoidMethod(gCallbackClass, method, (jint) audioRoute);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();

}
void EventManager::onMediaEngineAudioSessionInterruptionBegan()
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
	method = jni_env->GetMethodID(gCallbackClass, "onMediaEngineAudioSessionInterruptionBegan", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onMediaEngineAudioSessionInterruptionEnded()
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
	method = jni_env->GetMethodID(gCallbackClass, "onMediaEngineAudioSessionInterruptionEnded", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

void EventManager::onMediaEngineFaceDetected()
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
	method = jni_env->GetMethodID(gCallbackClass, "onMediaEngineFaceDetected", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onMediaEngineVideoCaptureRecordStopped()
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
	method = jni_env->GetMethodID(gCallbackClass, "onMediaEngineVideoCaptureRecordStopped", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
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

}
void EventManager::onAccountAssociatedIdentitiesChanged(IAccountPtr account)
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
	method = jni_env->GetStaticMethodID(cls, "onAccountAssociatedIdentitiesChanged", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onAccountPendingMessageForInnerBrowserWindowFrame(IAccountPtr account)
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
	method = jni_env->GetStaticMethodID(cls, "onAccountPendingMessageForInnerBrowserWindowFrame", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

//IConversationThreadDelegate implementation
void EventManager::onConversationThreadNew(IConversationThreadPtr conversationThread)
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
	method = jni_env->GetMethodID(gCallbackClass, "onConversationThreadNew", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onConversationThreadContactsChanged(IConversationThreadPtr conversationThread)
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
	method = jni_env->GetMethodID(gCallbackClass, "onConversationThreadContactsChanged", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onConversationThreadContactStateChanged(
		IConversationThreadPtr conversationThread,
		IContactPtr contact,
		IConversationThread::ContactStates state
)
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
	method = jni_env->GetMethodID(gCallbackClass, "onConversationThreadContactStateChanged", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onConversationThreadMessage(
		IConversationThreadPtr conversationThread,
		const char *messageID
)
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
	method = jni_env->GetMethodID(gCallbackClass, "onConversationThreadMessage", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onConversationThreadMessageDeliveryStateChanged(
		IConversationThreadPtr conversationThread,
		const char *messageID,
		IConversationThread::MessageDeliveryStates state
)
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
	method = jni_env->GetMethodID(gCallbackClass, "onConversationThreadMessageDeliveryStateChanged", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onConversationThreadPushMessage(
		IConversationThreadPtr conversationThread,
		const char *messageID,
		IContactPtr contact
)
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
	method = jni_env->GetMethodID(gCallbackClass, "onConversationThreadPushMessage", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

//ICallDelegate implementation
void EventManager::onCallStateChanged(ICallPtr call, ICall::CallStates state)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	jint attach_result = android_jvm->AttachCurrentThread(&gEnv, NULL);
	if (attach_result < 0 || gEnv == 0)
	{
		return;
	}
	jclass callbackClass = findClass("com/openpeer/delegates/CallbackHandler");
	method = gEnv->GetStaticMethodID(callbackClass, "onCallStateChanged", "(I)V");
	gEnv->CallStaticVoidMethod(callbackClass, method, (jint) state);

	if (gEnv->ExceptionCheck()) {
		gEnv->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
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
	method = jni_env->GetStaticMethodID(cls, "onIdentityPendingMessageForInnerBrowserWindowFrame", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onIdentityRolodexContactsDownloaded(IIdentityPtr identity)
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
	method = jni_env->GetMethodID(gCallbackClass, "onIdentityRolodexContactsDownloaded", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}


//IIdentityLookupDelegate implementation
void EventManager::onIdentityLookupCompleted(
		IIdentityLookupPtr identity
)
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
	cls = jni_env->FindClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onIdentityLookupCompleted", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

//ILoggerDelegate implementation
void EventManager::onNewSubsystem(
		SubsystemID subsystemUniqueID,
		const char *subsystemName
)
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
	method = jni_env->GetMethodID(gCallbackClass, "onNewSubsystem", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void EventManager::onLog(
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

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}
	//cls = jni_env->FindClass("com/openpeer/delegates/OPStackMessageQueueDelegate");
	method = jni_env->GetMethodID(gCallbackClass, "onLog", "()V");
	jni_env->CallVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

EventManager::~EventManager()
{
	int i = 0;
	i++;
}
