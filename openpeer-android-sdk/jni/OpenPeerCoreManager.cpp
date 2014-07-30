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

	return returnObj;
}

jint OpenPeerCoreManager::getIntValueFromEnumObject(jobject enumObject, String enumClassName)
{
	JNIEnv *jni_env = getEnv();

	jclass cls = findClass(enumClassName.c_str());
	jmethodID ordinalMethodID = jni_env->GetMethodID(cls, "ordinal", "()I");
	jint intValue = (jint) jni_env->CallIntMethod(enumObject, ordinalMethodID);

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
	ret = env->GetStringUTFChars(strObj, NULL);

	// Print the class name
	printf("\nCalling class is: %s\n", ret.c_str());


	// Release the memory pinned char array
	env->ReleaseStringUTFChars(strObj, ret);
	return ret;
}
