#include "com_openpeer_javaapi_OPMediaEngine.h"
#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/ILogger.h"

#include "globals.h"

using namespace openpeer::core;

#ifdef __cplusplus
extern "C" {
#endif

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
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getDefaultVideoOrientation
 * Signature: ()Lcom/openpeer/javaapi/VideoOrientations;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getDefaultVideoOrientation
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setRecordVideoOrientation
 * Signature: (Lcom/openpeer/javaapi/VideoOrientations;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setRecordVideoOrientation
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getRecordVideoOrientation
 * Signature: ()Lcom/openpeer/javaapi/VideoOrientations;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getRecordVideoOrientation
  (JNIEnv *, jobject)
{

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
  (JNIEnv *, jobject, jobject)
{
	IMediaEngine::singleton()->setEcEnabled(true);
}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setAgcEnabled
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setAgcEnabled
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setNsEnabled
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setNsEnabled
  (JNIEnv *, jobject, jobject)
{

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
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getMuteEnabled
 * Signature: ()Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getMuteEnabled
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setLoudspeakerEnabled
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setLoudspeakerEnabled
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getLoudspeakerEnabled
 * Signature: ()Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getLoudspeakerEnabled
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getOutputAudioRoute
 * Signature: ()Lcom/openpeer/javaapi/OutputAudioRoutes;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getOutputAudioRoute
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setContinuousVideoCapture
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setContinuousVideoCapture
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getContinuousVideoCapture
 * Signature: ()Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getContinuousVideoCapture
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setFaceDetection
 * Signature: (Ljava/lang/Boolean;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setFaceDetection
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getFaceDetection
 * Signature: ()Ljava/lang/Boolean;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getFaceDetection
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    getCameraType
 * Signature: ()Lcom/openpeer/javaapi/CameraTypes;
 */
JNIEXPORT jobject JNICALL Java_com_openpeer_javaapi_OPMediaEngine_getCameraType
  (JNIEnv *, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    setCameraType
 * Signature: (Lcom/openpeer/javaapi/CameraTypes;)V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_setCameraType
  (JNIEnv *, jobject, jobject)
{

}

/*
 * Class:     com_openpeer_javaapi_OPMediaEngine
 * Method:    startVideoCapture
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_openpeer_javaapi_OPMediaEngine_startVideoCapture
  (JNIEnv *, jobject)
{

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

#ifdef __cplusplus
}
#endif
