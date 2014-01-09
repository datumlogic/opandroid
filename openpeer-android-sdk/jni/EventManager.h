#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "../../../opios/libs/op/openpeer/core/IStack.h"
#include "../../../opios/libs/op/openpeer/core/IMediaEngine.h"
//#define NULL ((void*) 0)

#ifndef _ANDROID_OPENPEER_EVENT_MANAGER_H_
#define _ANDROID_OPENPEER_EVENT_MANAGER_H_

using namespace openpeer::core;

class EventManager : public IStackMessageQueueDelegate,
					 public IStackDelegate,
					 public IMediaEngineDelegate
{
	//IStackMessageQueueDelegate implementation
	virtual void onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread();

	//IStackDelegate implementation
	virtual void onStackShutdown(openpeer::core::IStackAutoCleanupPtr);

	//IMediaEngine implementation
	virtual void onMediaEngineAudioRouteChanged(openpeer::core::IMediaEngine::OutputAudioRoutes audioRoute);
	virtual void onMediaEngineFaceDetected();
	virtual void onMediaEngineVideoCaptureRecordStopped();
};

typedef boost::shared_ptr<EventManager> EventManagerPtr;


#endif
