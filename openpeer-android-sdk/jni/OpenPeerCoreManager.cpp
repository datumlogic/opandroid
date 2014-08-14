/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
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
