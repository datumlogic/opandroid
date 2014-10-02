#include "OpenPeerCoreManager.h"
#include "globals.h"
#include <android/log.h>;

IStackMessageQueuePtr OpenPeerCoreManager::queuePtr = IStackMessageQueuePtr();
ISettingsPtr OpenPeerCoreManager::settingsPtr = ISettingsPtr();
ICachePtr OpenPeerCoreManager::cachePtr = ICachePtr();


jobject OpenPeerCoreManager::getJavaEnumObject(String enumClassName, jint index)
{
	JNIEnv *jni_env = getEnv();

	jclass cls = findClass(enumClassName.c_str());
	jmethodID valuesMethodID = jni_env->GetStaticMethodID(cls, "values", ("()[L" + enumClassName  + ";").c_str());
	jobjectArray valuesArray = (jobjectArray)jni_env->CallStaticObjectMethod(cls, valuesMethodID);
	jobject returnObj = jni_env->GetObjectArrayElement(valuesArray, index);
	jni_env->DeleteLocalRef(valuesArray);
	jni_env->DeleteLocalRef(cls);

	return returnObj;
}

jint OpenPeerCoreManager::getIntValueFromEnumObject(jobject enumObject, String enumClassName)
{
	JNIEnv *jni_env = getEnv();

	jclass cls = findClass(enumClassName.c_str());
	jmethodID ordinalMethodID = jni_env->GetMethodID(cls, "ordinal", "()I");
	jint intValue = (jint) jni_env->CallIntMethod(enumObject, ordinalMethodID);
	jni_env->DeleteLocalRef(cls);

	return intValue;
}

String OpenPeerCoreManager::getObjectClassName (jobject delegate)
{
	String ret;
	JNIEnv *env = getEnv();

	jclass cls = env->GetObjectClass(delegate);

	// First get the class object
	jmethodID mid = env->GetMethodID(cls, "getClass", "()Ljava/lang/Class;");
	jobject clsObj = env->CallObjectMethod(delegate, mid);

	// Now get the class object's class descriptor
	cls = env->GetObjectClass(clsObj);

	// Find the getName() method on the class object
	mid = env->GetMethodID(cls, "getName", "()Ljava/lang/String;");

	// Call the getName() to get a jstring object back
	jstring strObj = (jstring)env->CallObjectMethod(clsObj, mid);

	// Now get the c string from the java jstring object
	ret = String(env->GetStringUTFChars(strObj, NULL));

	env->DeleteLocalRef(clsObj);
	env->DeleteLocalRef(cls);
	// Release the memory pinned char array
	env->ReleaseStringUTFChars(strObj, ret);
	env->DeleteLocalRef(strObj);
	return ret;
}

void OpenPeerCoreManager::fillJavaTokenFromCoreObject(jobject javaToken, IIdentity::Token coreToken)
{
	JNIEnv *jni_env = 0;
	jni_env = getEnv();

	jclass cls = findClass("com/openpeer/javaapi/OPToken");

	jfieldID fid = jni_env->GetFieldID(cls, "mID", "Ljava/lang/String;");
	jstring id = jni_env->NewStringUTF(coreToken.mID.c_str());
	jni_env->SetObjectField(javaToken, fid, id);

	jfieldID fSecret = jni_env->GetFieldID(cls, "mSecret", "Ljava/lang/String;");
	jstring secret = jni_env->NewStringUTF(coreToken.mSecret.c_str());
	jni_env->SetObjectField(javaToken, fSecret, secret);

	jfieldID fSecretEncrypted = jni_env->GetFieldID(cls, "mSecretEncrypted", "Ljava/lang/String;");
	jstring secretEncrypted = jni_env->NewStringUTF(coreToken.mSecretEncrypted.c_str());
	jni_env->SetObjectField(javaToken, fSecretEncrypted, secretEncrypted);

	//TIME
	jfieldID fExpires = jni_env->GetFieldID(cls, "mExpires", "Landroid/text/format/Time;");
	//Convert and set time from C++ to Android; Fetch methods needed to accomplish this
	Time time_t_epoch = boost::posix_time::time_from_string("1970-01-01 00:00:00.000");
	jclass timeCls = findClass("android/text/format/Time");
	jmethodID timeMethodID = jni_env->GetMethodID(timeCls, "<init>", "()V");
	jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(J)V");
	//calculate and set Ring Time
	zsLib::Duration ringTimeDuration = coreToken.mExpires - time_t_epoch;
	jobject object = jni_env->NewObject(timeCls, timeMethodID);
	jni_env->CallVoidMethod(object, timeSetMillisMethodID, ringTimeDuration.total_milliseconds());
	jni_env->SetObjectField(javaToken, fExpires, object);

	jfieldID fProof = jni_env->GetFieldID(cls, "mProof", "Ljava/lang/String;");
	jstring proof = jni_env->NewStringUTF(coreToken.mProof.c_str());
	jni_env->SetObjectField(javaToken, fProof, proof);

	jfieldID fNonce = jni_env->GetFieldID(cls, "mNonce", "Ljava/lang/String;");
	jstring nonce = jni_env->NewStringUTF(coreToken.mNonce.c_str());
	jni_env->SetObjectField(javaToken, fNonce, nonce);

	jfieldID fResource = jni_env->GetFieldID(cls, "mResource", "Ljava/lang/String;");
	jstring resource = jni_env->NewStringUTF(coreToken.mResource.c_str());
	jni_env->SetObjectField(javaToken, fResource, resource);

}
