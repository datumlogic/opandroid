#include "com_openpeer_javaapi_OPConversationThreadComposingStatus.h"
#include "openpeer/core/IConversationThreadComposingStatus.h"
#include "openpeer/core/IHelper.h"
#include "OpenPeerCoreManager.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadComposingStatus
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/ComposingStates;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPConversationThreadComposingStatus_toString
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadComposingStatus
 * Method:    toComposingState
 * Signature: (Ljava/lang/String;)Lcom/openpeer/javaapi/ComposingStates;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThreadComposingStatus_toComposingState
(JNIEnv *, jclass, jstring)
{

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadComposingStatus
 * Method:    updateComposingStatus
 * Signature: (Ljava/lang/String;Lcom/openpeer/javaapi/ComposingStates;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPConversationThreadComposingStatus_updateComposingStatus
(JNIEnv *env, jclass, jstring ioContactStatusInThreadEl, jobject composing)
{
	ElementPtr ioContactStatusInThreadElement = ElementPtr();

	const char *ioContactStatusInThreadElStr;
	ioContactStatusInThreadElStr = env->GetStringUTFChars(ioContactStatusInThreadEl, NULL);
	if (ioContactStatusInThreadElStr != NULL) {
		ioContactStatusInThreadElement = IHelper::createElement(ioContactStatusInThreadElStr);
	}

	IConversationThreadComposingStatus::updateComposingStatus(ioContactStatusInThreadElement,
			(IConversationThreadComposingStatus::ComposingStates) OpenPeerCoreManager::getIntValueFromEnumObject(composing, "com/openpeer/javaapi/ComposingStates"));

}

/*
 * Class:     com_openpeer_javaapi_OPConversationThreadComposingStatus
 * Method:    getComposingStatus
 * Signature: (Ljava/lang/String;)Lcom/openpeer/javaapi/ComposingStates;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPConversationThreadComposingStatus_getComposingStatus
(JNIEnv *env, jclass, jstring contactStatusInThreadEl)
{
	jobject status;
	const char *contactStatusInThreadElStr;
	contactStatusInThreadElStr = env->GetStringUTFChars(contactStatusInThreadEl, NULL);
	if (contactStatusInThreadElStr != NULL) {
		return status;
	}
	IConversationThreadComposingStatus::ComposingStates state = IConversationThreadComposingStatus::getComposingStatus(IHelper::createElement(contactStatusInThreadElStr));
	status = OpenPeerCoreManager::getJavaEnumObject("com/openpeer/javaapi/ComposingStates",(jint)state );

	return status;
}

#ifdef __cplusplus
}
#endif
