#include "ConversationThreadDelegateWrapper.h"
#include "OpenPeerCoreManager.h"
#include "android/log.h"
#include "globals.h"


//IConversationThreadDelegate implementation
ConversationThreadDelegateWrapper::ConversationThreadDelegateWrapper(jobject delegate)
{
	JNIEnv *jni_env = getEnv();
	javaDelegate = jni_env->NewGlobalRef(delegate);
}

//IConversationThreadDelegate implementation
void ConversationThreadDelegateWrapper::onConversationThreadNew(IConversationThreadPtr conversationThread)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onConversationThreadNew called");

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	if (javaDelegate != NULL){
		cls = findClass("com/openpeer/javaapi/OPConversationThread");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		object = jni_env->NewObject(cls, method);

		IConversationThreadPtr* ptrToConversationThread = new boost::shared_ptr<IConversationThread>(conversationThread);
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong convThread = (jlong) ptrToConversationThread;
		jni_env->SetLongField(object, fid, convThread);

		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		jclass callbackClass = findClass(className.c_str());
		method = jni_env->GetMethodID(callbackClass, "onConversationThreadNew", "(Lcom/openpeer/javaapi/OPConversationThread;)V");
		jni_env->CallVoidMethod(javaDelegate, method, object);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onConversationThreadNew Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void ConversationThreadDelegateWrapper::onConversationThreadContactsChanged(IConversationThreadPtr conversationThread)
{

	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onConversationThreadContactsChanged called");

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}

	if (javaDelegate != NULL){
		cls = findClass("com/openpeer/javaapi/OPConversationThread");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		object = jni_env->NewObject(cls, method);

		IConversationThreadPtr* ptrToConversationThread = new boost::shared_ptr<IConversationThread>(conversationThread);
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong convThread = (jlong) ptrToConversationThread;
		jni_env->SetLongField(object, fid, convThread);

		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		jclass callbackClass = findClass(className.c_str());
		method = jni_env->GetMethodID(callbackClass, "onConversationThreadContactsChanged", "(Lcom/openpeer/javaapi/OPConversationThread;)V");
		jni_env->CallVoidMethod(javaDelegate, method, object);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onConversationThreadContactsChanged Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void ConversationThreadDelegateWrapper::onConversationThreadContactStateChanged(
		IConversationThreadPtr conversationThread,
		IContactPtr contact,
		IConversationThread::ContactStates state
)
{

	jclass cls;
	jmethodID method;
	jobject convThreadObject;
	jobject contactObject;
	JNIEnv *jni_env = 0;
	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onConversationThreadContactStateChanged called");


	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}
	if (javaDelegate != NULL){
		jclass conversationThreadClass = findClass("com/openpeer/javaapi/OPConversationThread");
		jmethodID ctmethod = jni_env->GetMethodID(conversationThreadClass, "<init>", "()V");
		convThreadObject = jni_env->NewObject(conversationThreadClass, ctmethod);

		IConversationThreadPtr* ptrToConversationThread = new boost::shared_ptr<IConversationThread>(conversationThread);
		jfieldID ctfid = jni_env->GetFieldID(conversationThreadClass, "nativeClassPointer", "J");
		jlong convThread = (jlong) ptrToConversationThread;
		jni_env->SetLongField(convThreadObject, ctfid, convThread);


		jclass contactClass = findClass("com/openpeer/javaapi/OPContact");
		jmethodID contactConstructor = jni_env->GetMethodID(contactClass, "<init>", "()V");
		contactObject = jni_env->NewObject(contactClass, contactConstructor);

		IContactPtr* ptrToContact = new boost::shared_ptr<IContact>(contact);
		jfieldID fid = jni_env->GetFieldID(contactClass, "nativeClassPointer", "J");
		jlong ptrforJava = (jlong) ptrToContact;
		jni_env->SetLongField(contactObject, fid, ptrforJava);

		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		jclass callbackClass = findClass(className.c_str());

		method = jni_env->GetMethodID(callbackClass, "onConversationThreadContactStateChanged", "(Lcom/openpeer/javaapi/OPConversationThread;Lcom/openpeer/javaapi/OPContact;I)V");
		jni_env->CallVoidMethod(javaDelegate, method, convThreadObject, contactObject, (jint) state);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onConversationThreadContactStateChanged Java delegate is NULL !!!");
	}
	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void ConversationThreadDelegateWrapper::onConversationThreadMessage(
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

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onConversationThreadMessage called");

	if (javaDelegate != NULL){
		jclass Ctcls = findClass("com/openpeer/javaapi/OPConversationThread");
		jmethodID ctmethod = jni_env->GetMethodID(Ctcls, "<init>", "()V");
		jobject convThreadObject = jni_env->NewObject(Ctcls, ctmethod);

		IConversationThreadPtr* ptrToConversationThread = new boost::shared_ptr<IConversationThread>(conversationThread);
		jfieldID ctfid = jni_env->GetFieldID(Ctcls, "nativeClassPointer", "J");
		jlong convThread = (jlong) ptrToConversationThread;
		jni_env->SetLongField(convThreadObject, ctfid, convThread);

		jstring messageIDStr = jni_env->NewStringUTF(messageID);

		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		jclass callbackClass = findClass(className.c_str());
		method = jni_env->GetMethodID(callbackClass, "onConversationThreadMessage", "(Lcom/openpeer/javaapi/OPConversationThread;Ljava/lang/String;)V");
		jni_env->CallVoidMethod(javaDelegate, method, convThreadObject, messageIDStr);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onConversationThreadMessage Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void ConversationThreadDelegateWrapper::onConversationThreadMessageDeliveryStateChanged(
		IConversationThreadPtr conversationThread,
		const char *messageID,
		IConversationThread::MessageDeliveryStates state
)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni", "onConversationThreadMessageDeliveryStateChanged state = %d", (jint)state);

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}
	if (javaDelegate != NULL){
		cls = findClass("com/openpeer/javaapi/OPConversationThread");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		object = jni_env->NewObject(cls, method);

		IConversationThreadPtr* ptrToConversationThread = new boost::shared_ptr<IConversationThread>(conversationThread);
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong convThread = (jlong) ptrToConversationThread;
		jni_env->SetLongField(object, fid, convThread);

		jstring messageIDStr = jni_env->NewStringUTF(messageID);

		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		jclass callbackClass = findClass(className.c_str());
		jobject messageDeliveryState = OpenPeerCoreManager::getJavaEnumObject("com/openpeer/javaapi/MessageDeliveryStates", state);

		method = jni_env->GetMethodID(callbackClass, "onConversationThreadMessageDeliveryStateChanged", "(Lcom/openpeer/javaapi/OPConversationThread;Ljava/lang/String;Lcom/openpeer/javaapi/MessageDeliveryStates;)V");
		jni_env->CallVoidMethod(javaDelegate, method, object, messageIDStr, messageDeliveryState);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onConversationThreadMessageDeliveryStateChanged Java delegate is NULL !!!");
	}
	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}
void ConversationThreadDelegateWrapper::onConversationThreadPushMessage(
		IConversationThreadPtr conversationThread,
		const char *messageID,
		IContactPtr contactPtr
)
{
	jclass cls;
	jmethodID method;
	jobject convThreadObject;
	jobject contactObject;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni","onConversationThreadPushMessage called");

	jint attach_result = android_jvm->AttachCurrentThread(&jni_env, NULL);
	if (attach_result < 0 || jni_env == 0)
	{
		return;
	}
	if (javaDelegate != NULL){
		jclass Ctcls = findClass("com/openpeer/javaapi/OPConversationThread");
		jmethodID ctmethod = jni_env->GetMethodID(Ctcls, "<init>", "()V");
		convThreadObject = jni_env->NewObject(Ctcls, ctmethod);

		IConversationThreadPtr* ptrToConversationThread = new boost::shared_ptr<IConversationThread>(conversationThread);
		jfieldID ctfid = jni_env->GetFieldID(Ctcls, "nativeClassPointer", "J");
		jlong convThread = (jlong) ptrToConversationThread;
		jni_env->SetLongField(convThreadObject, ctfid, convThread);


		jclass contactCls = findClass("com/openpeer/javaapi/OPContact");
		jmethodID contactConstructor = jni_env->GetMethodID(contactCls, "<init>", "()V");
		contactObject = jni_env->NewObject(contactCls, contactConstructor);

		IContactPtr* ptrToContact = new boost::shared_ptr<IContact>(contactPtr);
		jfieldID fid = jni_env->GetFieldID(contactCls, "nativeClassPointer", "J");
		jlong contact = (jlong) ptrToContact;
		jni_env->SetLongField(contactObject, fid, contact);

		jstring messageIDStr = jni_env->NewStringUTF(messageID);

		//get delegate implementation class name in order to get method
		String className = OpenPeerCoreManager::getObjectClassName(javaDelegate);

		jclass callbackClass = findClass(className.c_str());
		method = jni_env->GetMethodID(callbackClass, "onConversationThreadPushMessage", "(Lcom/openpeer/javaapi/OPConversationThread;Ljava/lang/String;Lcom/openpeer/javaapi/OPContact;)V");
		jni_env->CallVoidMethod(javaDelegate, method, convThreadObject, messageIDStr, contactObject);
	}
	else
	{
		__android_log_print(ANDROID_LOG_ERROR, "com.openpeer.jni", "onConversationThreadMessageDeliveryStateChanged Java delegate is NULL !!!");
	}

	if (jni_env->ExceptionCheck()) {
		jni_env->ExceptionDescribe();
	}

	android_jvm->DetachCurrentThread();
}





ConversationThreadDelegateWrapper::~ConversationThreadDelegateWrapper()
{
	JNIEnv *jni_env = getEnv();
	jni_env->DeleteGlobalRef(javaDelegate);

}
