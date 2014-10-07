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

IPushMessaging::PushInfo OpenPeerCoreManager::pushInfoToCore(jobject javaPushInfo)
{
	IPushMessaging::PushInfo returnObject;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OpenPeerCoreManager pushInfoToCore called");

	jni_env = getEnv();
	if (jni_env)
	{
		//mServiceType
		jclass javaItemClass = findClass("com/openpeer/javaapi/OPPushInfo");

		jmethodID getServiceTypeMethodID = jni_env->GetMethodID( javaItemClass, "getServiceType", "()Ljava/lang/String;" );
		jstring serviceType = jni_env->CallObjectMethod(javaPushInfo, getServiceTypeMethodID);
		returnObject.mServiceType = String(jni_env->GetStringUTFChars(serviceType, NULL));
		jni_env->ReleaseStringUTFChars(serviceType, returnObject.mServiceType);
		jni_env->DeleteLocalRef(serviceType);

		//mValues
		jmethodID getValuesMethodID = jni_env->GetMethodID( javaItemClass, "getValues", "()Lcom/openpeer/javaapi/OPElement;" );
		jclass elementCls = findClass("com/openpeer/javaapi/OPElement");
		jfieldID elementFid = jni_env->GetFieldID(elementCls, "nativeClassPointer", "J");
		jobject elementValuesObject = jni_env->CallObjectMethod(javaPushInfo, getValuesMethodID);
		jlong valuesPointerValue = jni_env->GetLongField(elementValuesObject, elementFid);

		ElementPtr* coreValuesElementPtr = (ElementPtr*)valuesPointerValue;
		returnObject.mValues = *coreValuesElementPtr;

		//mCustom
		jmethodID getCustomMethodID = jni_env->GetMethodID( javaItemClass, "getCustom", "()Lcom/openpeer/javaapi/OPElement;" );
		jobject elementCustomObject = jni_env->CallObjectMethod(javaPushInfo, getCustomMethodID);
		jlong customPointerValue = jni_env->GetLongField(elementCustomObject, elementFid);

		ElementPtr* coreCustomElementPtr = (ElementPtr*)customPointerValue;
		returnObject.mCustom = *coreCustomElementPtr;
	}

	return returnObject;
}

jobject OpenPeerCoreManager::pushInfoToJava(IPushMessaging::PushInfo corePushInfo)
{
	jobject returnObject;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OpenPeerCoreManager pushInfoToJava called");

	jni_env = getEnv();

	if(jni_env)
	{
		//mServiceType
		jclass javaItemClass = findClass("com/openpeer/javaapi/OPPushInfo");
		jmethodID javaItemConstructorMethodID = jni_env->GetMethodID(javaItemClass, "<init>", "()V");
		returnObject = jni_env->NewObject(javaItemClass, javaItemConstructorMethodID);

		jmethodID setServiceTypeMethodID = jni_env->GetMethodID( javaItemClass, "setServiceType", "(Ljava/lang/String;)V" );
		jstring serviceType = jni_env->NewStringUTF(corePushInfo.mServiceType.c_str());
		jni_env->CallVoidMethod(returnObject, setServiceTypeMethodID, serviceType);

		//mValues
		ElementPtr coreEl = corePushInfo.mValues;
		ElementPtr* ptrToElement = new boost::shared_ptr<Element>(coreEl);
		jclass elementCls = findClass("com/openpeer/javaapi/OPElement");
		jmethodID elementMethod = jni_env->GetMethodID(elementCls, "<init>", "()V");
		jobject elementValuesObject = jni_env->NewObject(elementCls, elementMethod);

		jfieldID fid = jni_env->GetFieldID(elementCls, "nativeClassPointer", "J");
		jlong element = (jlong) ptrToElement;
		jni_env->SetLongField(elementValuesObject, fid, element);

		jmethodID setValuesMethodID = jni_env->GetMethodID( javaItemClass, "setValues", "(Lcom/openpeer/javaapi/OPElement;)V" );
		jni_env->CallVoidMethod(returnObject, setValuesMethodID, elementValuesObject);

		//mCustom
		ElementPtr coreCustomEl = corePushInfo.mCustom;
		ElementPtr* ptrToCustomElement = new boost::shared_ptr<Element>(coreCustomEl);
		jobject elementCustomObject = jni_env->NewObject(elementCls, elementMethod);
		jlong elementCustom = (jlong) ptrToCustomElement;
		jni_env->SetLongField(elementCustomObject, fid, elementCustom);

		jmethodID setCustomMethodID = jni_env->GetMethodID( javaItemClass, "setCustom", "(Lcom/openpeer/javaapi/OPElement;)V" );
		jni_env->CallVoidMethod(returnObject, setCustomMethodID, elementCustomObject);

	}

	return returnObject;
}


//push messaging helper methods
IPushMessaging::PushInfoList OpenPeerCoreManager::pushInfoListToCore(jobject javaPushInfoList)
{
	IPushMessaging::PushInfoList returnListObject;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OpenPeerCoreManager pushInfoListToCore called");

	jni_env = getEnv();

	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass arrayListClass = findClass("java/util/ArrayList");
		// Fetch "java.util.List.get(int location)" MethodID
		jmethodID listGetMethodID = jni_env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
		// Fetch "int java.util.List.size()" MethodID
		jmethodID sizeMethodID = jni_env->GetMethodID( arrayListClass, "size", "()I" );

		// Call "int java.util.List.size()" method and get count of items in the list.
		int listItemsCount = (int)jni_env->CallIntMethod( javaPushInfoList, sizeMethodID );

		for( int i=0; i<listItemsCount; ++i )
		{
			// Call "java.util.List.get" method and get Contact object by index.
			jobject pushInfoObject = jni_env->CallObjectMethod( javaPushInfoList, listGetMethodID, i );
			IPushMessaging::PushInfo corePushInfo = pushInfoToCore(pushInfoObject);
			//add core contacts to list for removal
			returnListObject.push_front(corePushInfo);
		}
	}
	return returnListObject;
}
jobject OpenPeerCoreManager::pushInfoListToJava(IPushMessaging::PushInfoList corePushInfoList)
{
	jobject returnListObject;
	JNIEnv *jni_env = 0;

	__android_log_print(ANDROID_LOG_DEBUG, "com.openpeer.jni", "OpenPeerCoreManager pushInfoListToJava called");

	jni_env = getEnv();

	if(jni_env)
	{
		//create return object - java/util/List is interface, ArrayList is implementation
		jclass returnListClass = findClass("java/util/ArrayList");
		jmethodID listConstructorMethodID = jni_env->GetMethodID(returnListClass, "<init>", "()V");
		returnListObject = jni_env->NewObject(returnListClass, listConstructorMethodID);

		//fetch List.add object
		jmethodID listAddMethodID = jni_env->GetMethodID(returnListClass, "add", "(Ljava/lang/Object;)Z");

		//fill java list
		for(IPushMessaging::PushInfoList::iterator coreListIter = corePushInfoList.begin();
				coreListIter != corePushInfoList.end(); coreListIter++)
		{
			jobject javaItemObject = pushInfoToJava(*coreListIter);
			//add to return List
			jboolean success = jni_env->CallBooleanMethod(returnListObject,listAddMethodID , javaItemObject);
			jni_env->DeleteLocalRef(javaItemObject);
		}
	}
	return returnListObject;
}

IPushMessaging::PushStateContactDetailList OpenPeerCoreManager::pushStateContactDetailListToCore(jobject javaPushStateContactDetailList);
jobject OpenPeerCoreManager::pushStateContactDetailListToJava(IPushMessaging::PushStateContactDetailList);

IPushMessaging::PushStateDetailMap OpenPeerCoreManager::pushStateDetailMapToCore(jobject javaPushStateDetailMap);
jobject OpenPeerCoreManager::pushStateDetailMapToJava(IPushMessaging::PushStateDetailMap);

IPushMessaging::PushMessageList OpenPeerCoreManager::pushMessageListToCore(jobject javaPushMessageList);
jobject OpenPeerCoreManager::pushMessageListToJava(IPushMessaging::PushMessageList);
