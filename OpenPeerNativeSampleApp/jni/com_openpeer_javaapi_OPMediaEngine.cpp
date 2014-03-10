#include "com_openpeer_javaapi_OPMediaEngine.h"
#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"
#include "openpeer/core/internal/core_MediaEngine.h"
#include "openpeer/core/test/TestMediaEngine.h"
#include <android/log.h>
#include <voe_base.h>
#include <vie_base.h>

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    init
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_init
  (JNIEnv *, jclass, jobject context)
{
	JNIEnv *jni_env = 0;

	jni_env = getEnv();
	if(jni_env)
	{
		webrtc::VoiceEngine::SetAndroidObjects(android_jvm, jni_env, context);
		webrtc::VideoEngine::SetAndroidObjects(android_jvm, context);
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    singleton
 * Signature: ()Lcom/openpeer/javaapi/OPMediaEngine;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_singleton
  (JNIEnv *, jclass)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	jni_env = getEnv();
	if(jni_env)
	{
		cls = findClass("com/openpeer/javaapi/OPMediaEngine");
		method = jni_env->GetMethodID(cls, "<init>", "()V");
		object = jni_env->NewObject(cls, method);

	    openpeer::core::test::TestMediaEngineFactoryPtr overrideFactory(new openpeer::core::test::TestMediaEngineFactory);

	    openpeer::core::internal::Factory::override(overrideFactory);

		mediaEnginePtr = IMediaEngine::singleton();

	}
	return object;

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setDefaultVideoOrientation
 * Signature: (Lcom/openpeer/javaapi/VideoOrientations;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setDefaultVideoOrientation
  (JNIEnv *, jobject, jobject videoOrientationObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("com/openpeer/javaapi/VideoOrientations");
		if(jni_env->IsInstanceOf(videoOrientationObj, cls) == JNI_TRUE)
		{
			jmethodID videoOrientationMID   = jni_env->GetMethodID(cls, "getNumericType", "()I");
			int intValue = (int) jni_env->CallIntMethod(videoOrientationObj, videoOrientationMID);

			IMediaEngine::VideoOrientations orientation = (IMediaEngine::VideoOrientations)intValue;
			mediaEnginePtr->setDefaultVideoOrientation(orientation);
		}
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getDefaultVideoOrientation
 * Signature: ()Lcom/openpeer/javaapi/VideoOrientations;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getDefaultVideoOrientation
  (JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			IMediaEngine::VideoOrientations value = mediaEnginePtr->getDefaultVideoOrientation();
			cls = findClass("com/openpeer/javaapi/VideoOrientations");
			method = jni_env->GetMethodID(cls, "<init>", "(I)V");
			object = jni_env->NewObject(cls, method, (int)value);
		}
	}
	return object;

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setRecordVideoOrientation
 * Signature: (Lcom/openpeer/javaapi/VideoOrientations;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setRecordVideoOrientation
  (JNIEnv *, jobject, jobject videoOrientationObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("com/openpeer/javaapi/VideoOrientations");
		if(jni_env->IsInstanceOf(videoOrientationObj, cls) == JNI_TRUE)
		{
			jmethodID videoOrientationMID   = jni_env->GetMethodID(cls, "getNumericType", "()I");
			int intValue = (int) jni_env->CallIntMethod(videoOrientationObj, videoOrientationMID);

			IMediaEngine::VideoOrientations orientation = (IMediaEngine::VideoOrientations)intValue;
			mediaEnginePtr->setRecordVideoOrientation(orientation);
		}
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getRecordVideoOrientation
 * Signature: ()Lcom/openpeer/javaapi/VideoOrientations;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getRecordVideoOrientation
  (JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			IMediaEngine::VideoOrientations value = mediaEnginePtr->getRecordVideoOrientation();
			cls = findClass("com/openpeer/javaapi/VideoOrientations");
			method = jni_env->GetMethodID(cls, "<init>", "(I)V");
			object = jni_env->NewObject(cls, method, (int)value);
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setVideoOrientation
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setVideoOrientation
  (JNIEnv *, jobject)
{

	if (mediaEnginePtr)
	{
		mediaEnginePtr->setVideoOrientation();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setCaptureRenderView
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setCaptureRenderView
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setChannelRenderView
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setChannelRenderView
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setEcEnabled
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setEcEnabled
  (JNIEnv *, jobject, jobject booleanObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("java/lang/Boolean");
		if(jni_env->IsInstanceOf(booleanObj, cls) == JNI_TRUE)
		{
			jmethodID booleanValueMID   = jni_env->GetMethodID(cls, "booleanValue", "()Z");
			bool booleanValue = (bool) jni_env->CallBooleanMethod(booleanObj, booleanValueMID);
			mediaEnginePtr->setEcEnabled(booleanValue);
		}
	}

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setAgcEnabled
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setAgcEnabled
  (JNIEnv *, jobject, jobject booleanObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("java/lang/Boolean");
		if(jni_env->IsInstanceOf(booleanObj, cls) == JNI_TRUE)
		{
			jmethodID booleanValueMID   = jni_env->GetMethodID(cls, "booleanValue", "()Z");
			bool booleanValue = (bool) jni_env->CallBooleanMethod(booleanObj, booleanValueMID);
			mediaEnginePtr->setAgcEnabled(booleanValue);
		}
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setNsEnabled
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setNsEnabled
  (JNIEnv *, jobject, jobject booleanObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("java/lang/Boolean");
		if(jni_env->IsInstanceOf(booleanObj, cls) == JNI_TRUE)
		{
			jmethodID booleanValueMID   = jni_env->GetMethodID(cls, "booleanValue", "()Z");
			bool booleanValue = (bool) jni_env->CallBooleanMethod(booleanObj, booleanValueMID);
			mediaEnginePtr->setNsEnabled(booleanValue);
		}
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setVoiceRecordFile
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setVoiceRecordFile
  (JNIEnv *, jobject, jstring)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getVoiceRecordFile
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getVoiceRecordFile
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setMuteEnabled
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setMuteEnabled
  (JNIEnv *, jobject, jobject booleanObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("java/lang/Boolean");
		if(jni_env->IsInstanceOf(booleanObj, cls) == JNI_TRUE)
		{
			jmethodID booleanValueMID   = jni_env->GetMethodID(cls, "booleanValue", "()Z");
			bool booleanValue = (bool) jni_env->CallBooleanMethod(booleanObj, booleanValueMID);
			mediaEnginePtr->setMuteEnabled(booleanValue);
		}
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getMuteEnabled
 * Signature: ()Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getMuteEnabled
  (JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			bool value = mediaEnginePtr->getMuteEnabled();
			cls = findClass("java/lang/Boolean");
			method = jni_env->GetMethodID(cls, "<init>", "(Z)V");
			object = jni_env->NewObject(cls, method, value);
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setLoudspeakerEnabled
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setLoudspeakerEnabled
  (JNIEnv *, jobject, jobject booleanObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("java/lang/Boolean");
		if(jni_env->IsInstanceOf(booleanObj, cls) == JNI_TRUE)
		{
			jmethodID booleanValueMID   = jni_env->GetMethodID(cls, "booleanValue", "()Z");
			bool booleanValue = (bool) jni_env->CallBooleanMethod(booleanObj, booleanValueMID);
			mediaEnginePtr->setLoudspeakerEnabled(booleanValue);
		}
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getLoudspeakerEnabled
 * Signature: ()Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getLoudspeakerEnabled
  (JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			bool value = mediaEnginePtr->getLoudspeakerEnabled();
			cls = findClass("java/lang/Boolean");
			method = jni_env->GetMethodID(cls, "<init>", "(Z)V");
			object = jni_env->NewObject(cls, method, value);
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getOutputAudioRoute
 * Signature: ()Lcom/openpeer/javaapi/OutputAudioRoutes;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getOutputAudioRoute
  (JNIEnv *, jobject)
{

	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			IMediaEngine::OutputAudioRoutes value = mediaEnginePtr->getOutputAudioRoute();
			cls = findClass("com/openpeer/javaapi/OutputAudioRoutes");
			method = jni_env->GetMethodID(cls, "<init>", "(I)V");
			object = jni_env->NewObject(cls, method, (int)value);
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setContinuousVideoCapture
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setContinuousVideoCapture
  (JNIEnv *, jobject, jobject booleanObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("java/lang/Boolean");
		if(jni_env->IsInstanceOf(booleanObj, cls) == JNI_TRUE)
		{
			jmethodID booleanValueMID   = jni_env->GetMethodID(cls, "booleanValue", "()Z");
			bool booleanValue = (bool) jni_env->CallBooleanMethod(booleanObj, booleanValueMID);
			mediaEnginePtr->setContinuousVideoCapture(booleanValue);
		}
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getContinuousVideoCapture
 * Signature: ()Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getContinuousVideoCapture
  (JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			bool value = mediaEnginePtr->getContinuousVideoCapture();
			cls = findClass("java/lang/Boolean");
			method = jni_env->GetMethodID(cls, "<init>", "(Z)V");
			object = jni_env->NewObject(cls, method, value);
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setFaceDetection
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setFaceDetection
  (JNIEnv *, jobject, jobject booleanObj)
{
	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("java/lang/Boolean");
		if(jni_env->IsInstanceOf(booleanObj, cls) == JNI_TRUE)
		{
			jmethodID booleanValueMID   = jni_env->GetMethodID(cls, "booleanValue", "()Z");
			bool booleanValue = (bool) jni_env->CallBooleanMethod(booleanObj, booleanValueMID);
			mediaEnginePtr->setFaceDetection(booleanValue);
		}
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getFaceDetection
 * Signature: ()Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getFaceDetection
  (JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			bool value = mediaEnginePtr->getFaceDetection();
			cls = findClass("java/lang/Boolean");
			method = jni_env->GetMethodID(cls, "<init>", "(Z)V");
			object = jni_env->NewObject(cls, method, value);
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getCameraType
 * Signature: ()Lcom/openpeer/javaapi/CameraTypes;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getCameraType
  (JNIEnv *, jobject)
{
	jclass cls;
	jmethodID method;
	jobject object;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();
		if(jni_env)
		{
			IMediaEngine::CameraTypes value = mediaEnginePtr->getCameraType();
			cls = findClass("com/openpeer/javaapi/CameraTypes");
			method = jni_env->GetMethodID(cls, "<init>", "(I)V");
			object = jni_env->NewObject(cls, method, (int)value);
		}
	}
	return object;
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setCameraType
 * Signature: (Lcom/openpeer/javaapi/CameraTypes;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setCameraType
  (JNIEnv *, jobject, jobject cameraTypeObj)
{

	jclass cls;
	JNIEnv *jni_env = 0;

	if (mediaEnginePtr)
	{
		jni_env = getEnv();

		cls = findClass("com/openpeer/javaapi/CameraTypes");
		if(jni_env->IsInstanceOf(cameraTypeObj, cls) == JNI_TRUE)
		{
			jmethodID cameraTypeMID   = jni_env->GetMethodID(cls, "getNumericType", "()I");
			int intValue = (int) jni_env->CallIntMethod(cameraTypeObj, cameraTypeMID);

			IMediaEngine::CameraTypes cameraType = (IMediaEngine::CameraTypes)intValue;
			mediaEnginePtr->setCameraType(cameraType);
		}
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    startVideoCapture
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_startVideoCapture
  (JNIEnv *, jobject)
{
    __android_log_print(ANDROID_LOG_DEBUG, "WEBRTC",
                        "Java_com_openpeer_javaapi_OPMediaEngine_startVideoCapture");

	if (mediaEnginePtr)
	{
		mediaEnginePtr->startVideoCapture();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    stopVideoCapture
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_stopVideoCapture
  (JNIEnv *, jobject)
{
	if (mediaEnginePtr)
	{
		mediaEnginePtr->stopVideoCapture();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    startRecordVideoCapture
 * Signature: (Ljava/lang/String;Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_startRecordVideoCapture
  (JNIEnv *, jobject, jstring, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    stopRecordVideoCapture
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_stopRecordVideoCapture
  (JNIEnv *, jobject)
{
	if (mediaEnginePtr)
	{
		mediaEnginePtr->stopRecordVideoCapture();
	}
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getVideoTransportStatistics
 * Signature: (Lcom/openpeer/javaapi/OPRtpRtcpStatistics;)I
 */
JNIEXPORT jint JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getVideoTransportStatistics
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getVoiceTransportStatistics
 * Signature: (Lcom/openpeer/javaapi/OPRtpRtcpStatistics;)I
 */
JNIEXPORT jint JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getVoiceTransportStatistics
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_test_OPTestMediaEngine
 * Method:    startVoice
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_test_OPTestMediaEngine_startVoice
  (JNIEnv *, jobject)
{
    __android_log_print(ANDROID_LOG_DEBUG, "WEBRTC",
                        "Java_com_openpeer_javaapi_test_OPTestMediaEngine_startVoice");

	if (mediaEnginePtr)
	{
	    openpeer::core::internal::IMediaEngineForCallTransport::singleton()->startVoice();
	}
}

/*
 * Class:     com_openpeer_javaapi_test_OPTestMediaEngine
 * Method:    stopVoice
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_test_OPTestMediaEngine_stopVoice
  (JNIEnv *, jobject)
{
    __android_log_print(ANDROID_LOG_DEBUG, "WEBRTC",
                        "Java_com_openpeer_javaapi_test_OPTestMediaEngine_stopVoice");

	if (mediaEnginePtr)
	{
	    openpeer::core::internal::IMediaEngineForCallTransport::singleton()->stopVoice();
	}
}

/*
 * Class:     com_openpeer_javaapi_test_OPTestMediaEngine
 * Method:    startVideoChannel
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_test_OPTestMediaEngine_startVideoChannel
  (JNIEnv *, jobject)
{
    __android_log_print(ANDROID_LOG_DEBUG, "WEBRTC",
                        "Java_com_openpeer_javaapi_test_OPTestMediaEngine_startVideoChannel");

	if (mediaEnginePtr)
	{
	    openpeer::core::internal::IMediaEngineForCallTransport::singleton()->startVideoChannel();
	}
}

/*
 * Class:     com_openpeer_javaapi_test_OPTestMediaEngine
 * Method:    stopVideoChannel
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_test_OPTestMediaEngine_stopVideoChannel
  (JNIEnv *, jobject)
{
    __android_log_print(ANDROID_LOG_DEBUG, "WEBRTC",
                        "Java_com_openpeer_javaapi_test_OPTestMediaEngine_stopVideoChannel");

	if (mediaEnginePtr)
	{
	    openpeer::core::internal::IMediaEngineForCallTransport::singleton()->stopVideoChannel();
	}
}

/*
 * Class:     com_openpeer_javaapi_test_OPTestMediaEngine
 * Method:    setReceiverAddress
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_test_OPTestMediaEngine_setReceiverAddress
  (JNIEnv *, jobject, jstring receiver_address)
{
    __android_log_print(ANDROID_LOG_DEBUG, "WEBRTC",
                        "Java_com_openpeer_javaapi_test_OPTestMediaEngine_setReceiverAddress");

	if (mediaEnginePtr)
	{
		JNIEnv *jni_env = getEnv();
	    const char* receiver_address_utf8;
	    receiver_address_utf8 = jni_env->GetStringUTFChars(receiver_address, NULL);
	    if (receiver_address_utf8 == NULL) {
	    	return;
	    }

	    openpeer::core::test::TestMediaEnginePtr testMediaEngine =
	        boost::dynamic_pointer_cast<openpeer::core::test::TestMediaEngine>(mediaEnginePtr);
	    testMediaEngine->setReceiverAddress(receiver_address_utf8);

	    jni_env->ReleaseStringUTFChars(receiver_address, receiver_address_utf8);
	}

}

/*
 * Class:     com_openpeer_javaapi_test_OPTestMediaEngine
 * Method:    getReceiverAddress
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_openpeer_javaapi_test_OPTestMediaEngine_getReceiverAddress
  (JNIEnv *, jobject)
{
    __android_log_print(ANDROID_LOG_DEBUG, "WEBRTC",
                        "Java_com_openpeer_javaapi_test_OPTestMediaEngine_setReceiverAddress");

    jstring receiver_address;

	if (mediaEnginePtr)
	{
		JNIEnv *jni_env = getEnv();
		const char* receiver_address_utf8;

	    openpeer::core::test::TestMediaEnginePtr testMediaEngine =
	        boost::dynamic_pointer_cast<openpeer::core::test::TestMediaEngine>(mediaEnginePtr);
	    receiver_address_utf8 = testMediaEngine->getReceiverAddress().c_str();

	    receiver_address = jni_env->NewStringUTF((const char*)receiver_address_utf8);
	}

	return receiver_address;
}

#ifdef __cplusplus
}
#endif
