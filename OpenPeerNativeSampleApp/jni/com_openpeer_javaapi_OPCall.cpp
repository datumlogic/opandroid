#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/ICall.h"
#include "openpeer/core/ILogger.h"
#include "OpenPeerCoreManager.h"
#include <android/log.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/CallStates;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPCall_toString__Lcom_openpeer_javaapi_CallStates_2
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    toString
 * Signature: (Lcom/openpeer/javaapi/CallClosedReasons;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPCall_toString__Lcom_openpeer_javaapi_CallClosedReasons_2
(JNIEnv *, jclass, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    toDebugString
 * Signature: (Lcom/openpeer/javaapi/OPCall;Z)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPCall_toDebugString
(JNIEnv *, jclass, jobject, jboolean)
{

}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    placeCall
 * Signature: (Lcom/openpeer/javaapi/OPConversationThread;Lcom/openpeer/javaapi/OPContact;ZZ)Lcom/openpeer/javaapi/OPCall;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_placeCall
(JNIEnv *, jclass, jobject conversationThread, jobject toContact, jboolean includeAudio, jboolean includeVideo)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	ICallPtr callPtr;

	IConversationThreadPtr convThread = (conversationThreadMap.find(conversationThread))->second;
	IContactPtr contact = contactMap.find(toContact)->second;

	if(convThread && contact)
	{

		callPtr = ICall::placeCall(convThread, contact ,includeAudio, includeVideo);
	}

	if(callPtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/OPCall");
			method = jni_env->GetMethodID(cls, "<init>", "()V");
			object = jni_env->NewObject(cls, method);

			jfieldID fid = jni_env->GetFieldID(cls, "nativeClassPointer", "J");
			jlong call = (jlong) callPtr.get();
			jni_env->SetLongField(object, fid, call);

			__android_log_print(ANDROID_LOG_INFO, "com.openpeer.jni",
					"CorePtr raw = %p, ptr as long = %Lu",callPtr.get(), call);

			OpenPeerCoreManager::coreCallList.push_back(callPtr);

		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getStableID
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_openpeer_javaapi_OPCall_getStableID
(JNIEnv *, jobject owner)
{
	jlong ret = 0;
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		ret = coreCall->getID();
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getCallID
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPCall_getCallID
(JNIEnv *env , jobject owner)
{
	jstring ret;
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		ret = env->NewStringUTF(coreCall->getCallID().c_str());
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getConversationThread
 * Signature: ()Lcom/openpeer/javaapi/OPConversationThread;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_getConversationThread
(JNIEnv *, jobject owner)
{
	jobject ret;
	IConversationThreadPtr convThread;
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		convThread = coreCall->getConversationThread();
		for(std::map<jobject, IConversationThreadPtr>::iterator iter = conversationThreadMap.begin(); iter != conversationThreadMap.end(); ++iter)
		{
			if (iter->second == convThread)
			{
				ret = iter->first;
				break;
			}
		}
	}
	return ret;

}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getCaller
 * Signature: ()Lcom/openpeer/javaapi/OPContact;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_getCaller
(JNIEnv *, jobject owner)
{
	jobject ret;
	IContactPtr contact;
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		contact = coreCall->getCaller();
		for(std::map<jobject, IContactPtr>::iterator iter = contactMap.begin(); iter != contactMap.end(); ++iter)
		{
			if (iter->second == contact)
			{
				ret = iter->first;
				break;
			}
		}
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getCallee
 * Signature: ()Lcom/openpeer/javaapi/OPContact;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_getCallee
(JNIEnv *, jobject owner)
{
	jobject ret;
	IContactPtr contact;
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		contact = coreCall->getCallee();
		for(std::map<jobject, IContactPtr>::iterator iter = contactMap.begin(); iter != contactMap.end(); ++iter)
		{
			if (iter->second == contact)
			{
				ret = iter->first;
				break;
			}
		}
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    hasAudio
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPCall_hasAudio
(JNIEnv *, jobject owner)
{
	jboolean ret;
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		ret = coreCall->hasAudio();
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    hasVideo
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_openpeer_javaapi_OPCall_hasVideo
(JNIEnv *, jobject owner)
{
	jboolean ret;
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		ret = coreCall->hasVideo();
	}
	return ret;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getState
 * Signature: ()Lcom/openpeer/javaapi/CallStates;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_getState
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	int state = 0;

	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		state = (int) coreCall->getState();

		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/CallStates");
			method = jni_env->GetMethodID(cls, "<init>", "(I)V");
			object = jni_env->NewObject(cls, method, state);

		}
	}

	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getClosedReason
 * Signature: ()Lcom/openpeer/javaapi/CallClosedReasons;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_getClosedReason
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;
	int state = 0;

	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		state = (int) coreCall->getClosedReason();

		jni_env = getEnv();
		if(jni_env)
		{
			cls = findClass("com/openpeer/javaapi/CallClosedReason");
			method = jni_env->GetMethodID(cls, "<init>", "(I)V");
			object = jni_env->NewObject(cls, method, state);

		}
	}

	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getCreationTime
 * Signature: ()Landroid/text/format/Time;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_getCreationTime
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	Time creationTime;

	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		creationTime = coreCall->getcreationTime();

		jni_env = getEnv();
		if(jni_env)
		{
			//Convert and set time from C++ to Android; Fetch methods needed to accomplish this
			Time time_t_epoch = boost::posix_time::time_from_string("1970-01-01 00:00:00.000");
			jclass timeCls = findClass("android/text/format/Time");
			jmethodID timeMethodID = jni_env->GetMethodID(timeCls, "<init>", "()V");
			jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(Z)V");

			//calculate and set Ring time
			zsLib::Duration creationTimeDuration = creationTime - time_t_epoch;
			object = jni_env->NewObject(timeCls, timeMethodID);
			jni_env->CallVoidMethod(object, timeSetMillisMethodID, creationTimeDuration.total_milliseconds());
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getRingTime
 * Signature: ()Landroid/text/format/Time;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_getRingTime
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	Time ringTime;

	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		ringTime = coreCall->getRingTime();

		jni_env = getEnv();
		if(jni_env)
		{
			//Convert and set time from C++ to Android; Fetch methods needed to accomplish this
			Time time_t_epoch = boost::posix_time::time_from_string("1970-01-01 00:00:00.000");
			jclass timeCls = findClass("android/text/format/Time");
			jmethodID timeMethodID = jni_env->GetMethodID(timeCls, "<init>", "()V");
			jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(Z)V");

			//calculate and set Ring Time
			zsLib::Duration ringTimeDuration = ringTime - time_t_epoch;
			object = jni_env->NewObject(timeCls, timeMethodID);
			jni_env->CallVoidMethod(object, timeSetMillisMethodID, ringTimeDuration.total_milliseconds());
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getAnswerTime
 * Signature: ()Landroid/text/format/Time;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_getAnswerTime
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	Time answerTime;

	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		answerTime = coreCall->getAnswerTime();

		jni_env = getEnv();
		if(jni_env)
		{
			//Convert and set time from C++ to Android; Fetch methods needed to accomplish this
			Time time_t_epoch = boost::posix_time::time_from_string("1970-01-01 00:00:00.000");
			jclass timeCls = findClass("android/text/format/Time");
			jmethodID timeMethodID = jni_env->GetMethodID(timeCls, "<init>", "()V");
			jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(Z)V");

			//calculate and set Answer Time
			zsLib::Duration answerTimeDuration = answerTime - time_t_epoch;
			object = jni_env->NewObject(timeCls, timeMethodID);
			jni_env->CallVoidMethod(object, timeSetMillisMethodID, answerTimeDuration.total_milliseconds());
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    getClosedTime
 * Signature: ()Landroid/text/format/Time;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPCall_getClosedTime
(JNIEnv *, jobject owner)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	Time closedTime;

	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		closedTime = coreCall->getClosedTime();

		jni_env = getEnv();
		if(jni_env)
		{
			//Convert and set time from C++ to Android; Fetch methods needed to accomplish this
			Time time_t_epoch = boost::posix_time::time_from_string("1970-01-01 00:00:00.000");
			jclass timeCls = findClass("android/text/format/Time");
			jmethodID timeMethodID = jni_env->GetMethodID(timeCls, "<init>", "()V");
			jmethodID timeSetMillisMethodID   = jni_env->GetMethodID(timeCls, "set", "(Z)V");

			//calculate and set Answer Time
			zsLib::Duration closedTimeDuration = closedTime - time_t_epoch;
			object = jni_env->NewObject(timeCls, timeMethodID);
			jni_env->CallVoidMethod(object, timeSetMillisMethodID, closedTimeDuration.total_milliseconds());
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    ring
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPCall_ring
(JNIEnv *, jobject owner)
{
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{;
	coreCall->ring();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    answer
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPCall_answer
(JNIEnv *, jobject owner)
{
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		coreCall->answer();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    hold
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPCall_hold
(JNIEnv *, jobject owner, jboolean hold)
{
	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		coreCall->hold(hold);
	}
}

/*
 * Class:     com_openpeer_javaapi_OPCall
 * Method:    hangup
 * Signature: (Lcom/openpeer/javaapi/CallClosedReasons;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPCall_hangup
(JNIEnv *, jobject owner, jobject callClosedReasonObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	ICallPtr coreCall = OpenPeerCoreManager::getCallFromList(owner);
	if (coreCall)
	{
		jni_env = getEnv();

		cls = findClass("com/openpeer/javaapi/CallClosedReasons");
		if(jni_env->IsInstanceOf(callClosedReasonObj, cls) == JNI_TRUE)
		{
			jmethodID callClosedReasonsMID   = jni_env->GetMethodID(cls, "getNumericType", "()I");
			int intValue = (int) jni_env->CallIntMethod(callClosedReasonObj, callClosedReasonsMID);

			ICall::CallClosedReasons reason = (ICall::CallClosedReasons)intValue;
			coreCall->hangup(reason);
			//todo Catch call ended state, and reset and remove call from OpenPeerCoreManager call vector
		}
	}
}

#ifdef __cplusplus
}
#endif
