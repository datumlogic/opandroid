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
#include "globals.h"

JavaVM *android_jvm;
jobject jni_object;

SettingsDelegateWrapperPtr settingsDelegatePtr;
CacheDelegateWrapperPtr cacheDelegatePtr;
CallDelegateWrapperPtr callDelegatePtr;
ConversationThreadDelegateWrapperPtr conversationThreadDelegatePtr;
LoggerDelegateWrapperPtr loggerDelegatePtr;
MediaEngineDelegateWrapperPtr mediaEngineDelegatePtr;

IMediaEnginePtr mediaEnginePtr;

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    android_jvm = vm;

    int getEnvStat = android_jvm->GetEnv((void **)&gEnv, JNI_VERSION_1_6);
	if (getEnvStat == JNI_EDETACHED) {
		std::cout << "GetEnv: not attached" << std::endl;
		if (android_jvm->AttachCurrentThread(&gEnv, NULL) != 0) {
			std::cout << "Failed to attach" << std::endl;
		}
	} else if (getEnvStat == JNI_OK) {
		//
	} else if (getEnvStat == JNI_EVERSION) {
		std::cout << "GetEnv: version not supported" << std::endl;
	}

	jclass randomClass = gEnv->FindClass("com/openpeer/javaapi/OPAccount");
	jclass classClass = gEnv->FindClass("java/lang/Class");
	jclass classLoaderClass = gEnv->FindClass("java/lang/ClassLoader");
	jmethodID getClassLoaderMethod = gEnv->GetMethodID(classClass, "getClassLoader",
											 "()Ljava/lang/ClassLoader;");

	jobject tmpClassLoader = gEnv->CallObjectMethod(randomClass, getClassLoaderMethod);
	gClassLoader = (jclass)gEnv->NewGlobalRef(tmpClassLoader);

	gFindClassMethod = gEnv->GetMethodID(classLoaderClass, "findClass",
									"(Ljava/lang/String;)Ljava/lang/Class;");

    return JNI_VERSION_1_6;
}

JNIEnv* getEnv() {
    JNIEnv *env;
    int status = android_jvm->GetEnv((void**)&env, JNI_VERSION_1_6);
    if(status < 0) {
        status = android_jvm->AttachCurrentThread(&env, NULL);
        if(status < 0) {
            return NULL;
        }
    }
    return env;
}

jclass findClass(const char* name) {
    return static_cast<jclass>(getEnv()->CallObjectMethod(gClassLoader, gFindClassMethod, getEnv()->NewStringUTF(name)));
}
