#include "EventManager.h"
#include "globals.h"
#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include <android/log.h>
#include "OpenPeerCoreManager.h"

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
	cls = findClass("com/openpeer/delegates/CallbackHandler");
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
	cls = findClass("com/openpeer/delegates/CallbackHandler");
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

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","EventManager::onMediaEngineAudioRouteChanged");

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onMediaEngineAudioRouteChanged", "(I)V");
	jni_env->CallStaticVoidMethod(cls, method, (jint) audioRoute);

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

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","EventManager::onMediaEngineAudioSessionInterruptionBegan");

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onMediaEngineAudioSessionInterruptionBegan", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

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

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","EventManager::onMediaEngineAudioSessionInterruptionEnded");

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onMediaEngineAudioSessionInterruptionEnded", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

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
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onMediaEngineFaceDetected", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

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
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onMediaEngineVideoCaptureRecordStopped", "()V");
	jni_env->CallStaticVoidMethod(cls, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

//IAccountDelegate implementation
//void EventManager::onAccountStateChanged(IAccountPtr account, IAccount::AccountStates state)
//{
//
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	//JNIEnv *jni_env = 0;
//
//	jint attach_result = android_jvm->AttachCurrentThread(&gEnv, NULL);
//	if (attach_result < 0 || gEnv == 0)
//	{
//		return;
//	}
//
//	jclass callbackClass = findClass("com/openpeer/delegates/CallbackHandler");
//	method = gEnv->GetStaticMethodID(callbackClass, "onAccountStateChanged", "(I)V");
//	gEnv->CallStaticVoidMethod(callbackClass, method, (jint) state);
//
//	if (gEnv->ExceptionCheck()) {
//		gEnv->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//
//}
//void EventManager::onAccountAssociatedIdentitiesChanged(IAccountPtr account)
//{
//
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	JNIEnv *jni_env = 0;
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//	cls = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(cls, "onAccountAssociatedIdentitiesChanged", "()V");
//	jni_env->CallStaticVoidMethod(cls, method);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}
//void EventManager::onAccountPendingMessageForInnerBrowserWindowFrame(IAccountPtr account)
//{
//
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	JNIEnv *jni_env = 0;
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//	cls = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(cls, "onAccountPendingMessageForInnerBrowserWindowFrame", "()V");
//	jni_env->CallStaticVoidMethod(cls, method);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}

////IConversationThreadDelegate implementation
//void EventManager::onConversationThreadNew(IConversationThreadPtr conversationThreadPtr)
//{
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	JNIEnv *jni_env = 0;
//	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","EventManager::onConversationThreadNew");
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//
//	cls = findClass("com/openpeer/javaapi/OPConversationThread");
//	method = jni_env->GetMethodID(cls, "<init>", "()V");
//	object = jni_env->NewObject(cls, method);
//	OpenPeerCoreManager::coreConversationThreadList.push_back(conversationThreadPtr);
//	//conversationThreadMap.insert(std::pair<jobject, IConversationThreadPtr>(object, conversationThread));
//
//	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
//	jlong convThread = (jlong) conversationThreadPtr.get();
//	jni_env->SetLongField(object, fid, convThread);
//
//	cls = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(cls, "onConversationThreadNew", "(Lcom/openpeer/javaapi/OPConversationThread;)V");
//	jni_env->CallStaticVoidMethod(cls, method, object);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}
//void EventManager::onConversationThreadContactsChanged(IConversationThreadPtr conversationThread)
//{
//
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	JNIEnv *jni_env = 0;
//	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","EventManager::onConversationThreadContactsChanged");
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//
//	cls = findClass("com/openpeer/javaapi/OPConversationThread");
//	method = jni_env->GetMethodID(cls, "<init>", "()V");
//	object = jni_env->NewObject(cls, method);
//
//	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
//	jlong convThread = (jlong) conversationThread.get();
//	jni_env->SetLongField(object, fid, convThread);
//
//	jclass callbackClass = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(callbackClass, "onConversationThreadContactsChanged", "(Lcom/openpeer/javaapi/OPConversationThread;)V");
//	jni_env->CallStaticVoidMethod(callbackClass, method, object);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}
//void EventManager::onConversationThreadContactStateChanged(
//		IConversationThreadPtr conversationThread,
//		IContactPtr contactPtr,
//		IConversationThread::ContactStates state
//)
//{
//
//	jclass cls;
//	jmethodID method;
//	jobject convThreadObject;
//	jobject contactObject;
//	JNIEnv *jni_env = 0;
//	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","EventManager::onConversationThreadContactStateChanged");
//
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//
//	jclass Ctcls = findClass("com/openpeer/javaapi/OPConversationThread");
//	jmethodID ctmethod = jni_env->GetMethodID(Ctcls, "<init>", "()V");
//	convThreadObject = jni_env->NewObject(Ctcls, ctmethod);
//
//	jfieldID ctfid = jni_env->GetFieldID(Ctcls, "nativeClassPointer", "J");
//	jlong convThread = (jlong) conversationThread.get();
//	jni_env->SetLongField(convThreadObject, ctfid, convThread);
//
//
//	jclass contactCls = findClass("com/openpeer/javaapi/OPContact");
//	jmethodID contactConstructor = jni_env->GetMethodID(contactCls, "<init>", "()V");
//	contactObject = jni_env->NewObject(contactCls, contactConstructor);
//
//	jfieldID fid = jni_env->GetFieldID(contactCls, "nativeClassPointer", "J");
//	jlong contact = (jlong) contactPtr.get();
//	jni_env->SetLongField(contactObject, fid, contact);
//
//
//	jclass callbackClass = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(callbackClass, "onConversationThreadContactStateChanged", "(Lcom/openpeer/javaapi/OPConversationThread;Lcom/openpeer/javaapi/OPContact;I)V");
//	jni_env->CallStaticVoidMethod(callbackClass, method, convThreadObject, contactObject, (jint) state);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}
//void EventManager::onConversationThreadMessage(
//		IConversationThreadPtr conversationThread,
//		const char *messageID
//)
//{
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	JNIEnv *jni_env = 0;
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//
//	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","EventManager::onConversationThreadMessage");
//
//	jclass Ctcls = findClass("com/openpeer/javaapi/OPConversationThread");
//	jmethodID ctmethod = jni_env->GetMethodID(Ctcls, "<init>", "()V");
//	jobject convThreadObject = jni_env->NewObject(Ctcls, ctmethod);
//
//	jfieldID ctfid = jni_env->GetFieldID(Ctcls, "nativeClassPointer", "J");
//	jlong convThread = (jlong) conversationThread.get();
//	jni_env->SetLongField(convThreadObject, ctfid, convThread);
//
//
//	jstring messageIDStr = jni_env->NewStringUTF(messageID);
//
//	cls = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(cls, "onConversationThreadMessage", "(Lcom/openpeer/javaapi/OPConversationThread;Ljava/lang/String;)V");
//	jni_env->CallStaticVoidMethod(cls, method, convThreadObject, messageIDStr);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}
//void EventManager::onConversationThreadMessageDeliveryStateChanged(
//		IConversationThreadPtr conversationThread,
//		const char *messageID,
//		IConversationThread::MessageDeliveryStates state
//)
//{
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	JNIEnv *jni_env = 0;
//
//	__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onConversationThreadMessageDeliveryStateChanged state = %d", (jint)state);
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//
//	cls = findClass("com/openpeer/javaapi/OPConversationThread");
//	method = jni_env->GetMethodID(cls, "<init>", "()V");
//	object = jni_env->NewObject(cls, method);
//
//	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
//	jlong convThread = (jlong) conversationThread.get();
//	jni_env->SetLongField(object, fid, convThread);
//
//	jstring messageIDStr = jni_env->NewStringUTF(messageID);
//
//	cls = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(cls, "onConversationThreadMessageDeliveryStateChanged", "(Lcom/openpeer/javaapi/OPConversationThread;Ljava/lang/String;I)V");
//	jni_env->CallStaticVoidMethod(cls, method, object, messageIDStr, (jint)state);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}
//void EventManager::onConversationThreadPushMessage(
//		IConversationThreadPtr conversationThread,
//		const char *messageID,
//		IContactPtr contactPtr
//)
//{
//	jclass cls;
//	jmethodID method;
//	jobject convThreadObject;
//	jobject contactObject;
//	JNIEnv *jni_env = 0;
//
//	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","EventManager::onConversationThreadPushMessage");
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//
//	jclass Ctcls = findClass("com/openpeer/javaapi/OPConversationThread");
//	jmethodID ctmethod = jni_env->GetMethodID(Ctcls, "<init>", "()V");
//	convThreadObject = jni_env->NewObject(Ctcls, ctmethod);
//
//	jfieldID ctfid = jni_env->GetFieldID(Ctcls, "nativeClassPointer", "J");
//	jlong convThread = (jlong) conversationThread.get();
//	jni_env->SetLongField(convThreadObject, ctfid, convThread);
//
//
//	jclass contactCls = findClass("com/openpeer/javaapi/OPContact");
//	jmethodID contactConstructor = jni_env->GetMethodID(contactCls, "<init>", "()V");
//	contactObject = jni_env->NewObject(contactCls, contactConstructor);
//
//	jfieldID fid = jni_env->GetFieldID(contactCls, "nativeClassPointer", "J");
//	jlong contact = (jlong) contactPtr.get();
//	jni_env->SetLongField(contactObject, fid, contact);
//
//	jstring messageIDStr = jni_env->NewStringUTF(messageID);
//
//	cls = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(cls, "onConversationThreadPushMessage", "(Lcom/openpeer/javaapi/OPConversationThread;Ljava/lang/String;Lcom/openpeer/javaapi/OPContact;)V");
//	jni_env->CallStaticVoidMethod(cls, method, convThreadObject, messageIDStr, contactObject);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}

////ICallDelegate implementation
//void EventManager::onCallStateChanged(ICallPtr call, ICall::CallStates state)
//{
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	JNIEnv *jni_env = 0;
//	__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onCallStateChanged state = %d", (jint)state);
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//	jclass callbackClass = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(callbackClass, "onCallStateChanged", "(I)V");
//	jni_env->CallStaticVoidMethod(callbackClass, method, (jint) state);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}

//IIdentityDelegate implementation
//void EventManager::onIdentityStateChanged(
//		IIdentityPtr identity,
//		IIdentity::IdentityStates state
//)
//{
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	//JNIEnv *jni_env = 0;
//
//	jint attach_result = android_jvm->AttachCurrentThread(&gEnv, NULL);
//	if (attach_result < 0 || gEnv == 0)
//	{
//		return;
//	}
//
//	jclass callbackClass = findClass("com/openpeer/delegates/CallbackHandler");
//	method = gEnv->GetStaticMethodID(callbackClass, "onIdentityStateChanged", "(I)V");
//	gEnv->CallStaticVoidMethod(callbackClass, method, (jint) state);
//
//	if (gEnv->ExceptionCheck()) {
//		gEnv->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}
//void EventManager::onIdentityPendingMessageForInnerBrowserWindowFrame(IIdentityPtr identity)
//{
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	JNIEnv *jni_env = 0;
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//	cls = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(cls, "onIdentityPendingMessageForInnerBrowserWindowFrame", "()V");
//	jni_env->CallStaticVoidMethod(cls, method);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}
//void EventManager::onIdentityRolodexContactsDownloaded(IIdentityPtr identity)
//{
//	jclass cls;
//	jmethodID method;
//	jobject object;
//	JNIEnv *jni_env = 0;
//
//	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
//	if (attach_result < 0 || jni_env == 0)
//	{
//		return;
//	}
//	cls = findClass("com/openpeer/delegates/CallbackHandler");
//	method = jni_env->GetStaticMethodID(cls, "onIdentityRolodexContactsDownloaded", "()V");
//	jni_env->CallStaticVoidMethod(cls, method);
//
//	if (jni_env->ExceptionCheck()) {
//		jni_env->ExceptionDescribe();
//	}
//
//	android_jvm->DetachCurrentThread();
//}


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
	cls = findClass("com/openpeer/delegates/CallbackHandler");
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
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onNewSubsystem", "()V");
	jni_env->CallVoidMethod(cls, method);

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
	cls = findClass("com/openpeer/delegates/CallbackHandler");
	method = jni_env->GetStaticMethodID(cls, "onLog", "()V");
	jni_env->CallStaticVoidMethod(gCallbackClass, method);

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}

EventManager::~EventManager()
{
}
