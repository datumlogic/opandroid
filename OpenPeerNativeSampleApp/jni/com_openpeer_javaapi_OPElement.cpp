#include <openpeer/core/types.h>
#include "openpeer/core/IHelper.h"
#include <android/log.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPElement
 * Method:    convertToString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPElement_convertToString
(JNIEnv *, jobject owner)
{
	jstring ret = 0;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPElement native convertToString called");

	jni_env = getEnv();
	jclass cls = findClass("com/openpeer/javaapi/OPElement");
	jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
	jlong pointerValue = jni_env->GetLongField(owner, fid);

	ElementPtr* coreElementPtr = (ElementPtr*)pointerValue;
	if (coreElementPtr)
	{
		ret = jni_env->NewStringUTF(IHelper::convertToString(*coreElementPtr).c_str());
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPElement native convertToString core pointer is NULL!!!");
	}
}

/*
 * Class:     com_openpeer_javaapi_OPElement
 * Method:    releaseCoreObjects
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPElement_releaseCoreObjects
(JNIEnv *, jobject javaObject)
{
	if(javaObject != NULL)
	{
		JNIEnv *jni_env = getEnv();
		jclass cls = findClass("com/openpeer/javaapi/OPElement");
		jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
		jlong pointerValue = jni_env->GetLongField(javaObject, fid);

		delete (ElementPtr*)pointerValue;
		__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OPElement Core object deleted.");
	}
	else
	{
		__android_log_print(ANDROID_LOG_WARN, "com.openpeer.jni", "OPElement Core object not deleted - already NULL!");
	}
}

#ifdef __cplusplus
}
#endif
