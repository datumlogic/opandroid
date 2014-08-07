#include "com_openpeer_javaapi_OPConversationThreadSystemMessage.h"
#include "openpeer/core/IConversationThreadSystemMessage.h"
#include "openpeer/core/IHelper.h"
#include "OpenPeerCoreManager.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadSystemMessage
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/SystemMessageTypes;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPConversationThreadSystemMessage_toString
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadSystemMessage
 * Method:    toSystemMessageType
 * Signature: (Ljava/lang/String;)Lcom/openpeer/javaapi/SystemMessageTypes;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThreadSystemMessage_toSystemMessageType
(JNIEnv *, jclass, jstring)
{

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadSystemMessage
 * Method:    getMessageType
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPConversationThreadSystemMessage_getMessageType
(JNIEnv *env, jclass)
{
	return env->NewStringUTF(IConversationThreadSystemMessage::getMessageType());
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadSystemMessage
 * Method:    createCallMessage
 * Signature: (Lcom/openpeer/javaapi/SystemMessageTypes;Lcom/openpeer/javaapi/OPContact;I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPConversationThreadSystemMessage_createCallMessage
(JNIEnv *, jclass, jobject messageType, jobject contact, jint errorCode)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	//take contacts from core conversation thread
	jni_env = getEnv();
	ElementPtr retElement = ElementPtr();

	cls = findClass("com/openpeer/javaapi/OPContact");
	jfieldID contactfid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong contactPointerValue = jni_env->GetLongField(contact, contactfid);
	IContactPtr* contactPtr = (IContactPtr*)contactPointerValue;

	if(contactPtr)
	{
		retElement = IConversationThreadSystemMessage::createCallMessage(
				(IConversationThreadSystemMessage::SystemMessageTypes) OpenPeerCoreManager::getIntValueFromEnumObject(messageType, "com/openpeer/javaapi/SystemMessageTypes"),
				*contactPtr,
				errorCode);

	}
	return jni_env->NewStringUTF(IHelper::convertToString(retElement));
}

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadSystemMessage
 * Method:    sendMessage
 * Signature: (Lcom/openpeer/javaapi/OPConversationThread;Ljava/lang/String;Ljava/lang/String;Z)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPConversationThreadSystemMessage_sendMessage
(JNIEnv *, jclass, jobject convThread, jstring messageID, jstring systemMessage, jboolean signMessage)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	//take contacts from core conversation thread
	jni_env = getEnv();



	const char *messageIDStr;
	messageIDStr = jni_env->GetStringUTFChars(messageID, NULL);
	if (messageIDStr == NULL) {
		return;
	}

	const char *systemMessageStr;
	systemMessageStr = jni_env->GetStringUTFChars(systemMessage, NULL);
	if (systemMessageStr == NULL) {
		return;
	}

	cls = findClass("com/openpeer/javaapi/OPConversationThread");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(convThread, fid);

	IConversationThreadPtr* coreConversationThreadPtr = (IConversationThreadPtr*)pointerValue;

	if(coreConversationThreadPtr)
	{
		ElementPtr systemMessageElement = IHelper::createElement(systemMessageStr);
		IConversationThreadSystemMessage::sendMessage(*coreConversationThreadPtr,
				messageIDStr, systemMessageElement, signMessage);
	}


}

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadSystemMessage
 * Method:    parseAsSystemMessage
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Lcom/openpeer/javaapi/OPParsedSystemMessage;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThreadSystemMessage_parseAsSystemMessage
(JNIEnv *, jclass, jstring message, jstring messageType)
{
	jclass cls;
	jmethodID method;
	jobject ret = NULL;
	JNIEnv *jni_env = 0;

	jni_env = getEnv();

	const char *messageStr;
	messageStr = jni_env->GetStringUTFChars(message, NULL);
	if (messageStr == NULL) {
		return ret;
	}

	const char *messageTypeStr;
	messageTypeStr = jni_env->GetStringUTFChars(messageType, NULL);
	if (messageTypeStr == NULL) {
		return ret;
	}
	ElementPtr outMessageEl;
	IConversationThreadSystemMessage::SystemMessageTypes coreType = IConversationThreadSystemMessage::parseAsSystemMessage(messageStr, messageTypeStr, outMessageEl);

	cls = findClass("com/openpeer/javaapi/OPParsedSystemMessage");
	method = jni_env->GetMethodID(cls, "<init>", "()V");
	ret = jni_env->NewObject(cls, method);

	jmethodID setTypeMethodID = jni_env->GetMethodID(cls, "setType", "(Lcom/openpeer/javaapi/SystemMessageTypes;)V");
	jmethodID setSystemMessageMethodID = jni_env->GetMethodID(cls, "setSystemMessage", "(Ljava/lang/String;)V");

	jni_env->CallVoidMethod(ret, setTypeMethodID, OpenPeerCoreManager::getJavaEnumObject("com/openpeer/javaapi/SystemMessageTypes", coreType));

	jstring javaMessageType = jni_env->NewStringUTF(IHelper::convertToString(outMessageEl));
	jni_env->CallVoidMethod(ret, setSystemMessageMethodID, javaMessageType);

	return ret;

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadSystemMessage
 * Method:    getCallMessageInfo
 * Signature: (Lcom/openpeer/javaapi/OPConversationThread;Ljava/lang/String;)Lcom/openpeer/javaapi/OPCallMessageInfo;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThreadSystemMessage_getCallMessageInfo
(JNIEnv *, jclass, jobject javaConversationThread, jstring message)
{

	jclass cls;
	jmethodID method;
	jobject ret = NULL;
	JNIEnv *jni_env = 0;

	jni_env = getEnv();

	const char *messageStr;
	messageStr = jni_env->GetStringUTFChars(message, NULL);
	if (messageStr == NULL) {
		return ret;
	}

	jni_env = getEnv();
	cls = findClass("com/openpeer/javaapi/OPConversationThread");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(javaConversationThread, fid);

	IConversationThreadPtr* coreConversationThreadPtr = (IConversationThreadPtr*)pointerValue;
	IContactPtr outContact;
	WORD outErrorCode;

	if (coreConversationThreadPtr)
	{

		ElementPtr systemMessageElement = IHelper::createElement(messageStr);
		IConversationThreadSystemMessage::getCallMessageInfo(*coreConversationThreadPtr,
				systemMessageElement,
				outContact,
				outErrorCode);

		cls = findClass("com/openpeer/javaapi/OPCallMessageInfo");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		ret = jni_env->NewObject(cls, method);

		jmethodID setContactMethodID = jni_env->GetMethodID(cls, "setContact", "(Lcom/openpeer/javaapi/OPContact;)V");
		jmethodID setErrorCodeMethodID = jni_env->GetMethodID(cls, "setErrorCode", "(I)V");

		jni_env->CallVoidMethod(ret, setErrorCodeMethodID, (jint) outErrorCode);

		cls = findClass("com/openpeer/javaapi/OPContact");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		jobject contactObject = jni_env->NewObject(cls, method);

		IContactPtr* ptrToContact = new boost::shared_ptr<IContact>(outContact);
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong contact = (jlong) ptrToContact;
		jni_env->SetLongField(contactObject, fid, contact);

		jni_env->CallVoidMethod(ret, setContactMethodID, contactObject);
	}
	return ret;
}

#ifdef __cplusplus
}
#endif
