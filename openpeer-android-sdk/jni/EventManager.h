#include "com_openpeer_javaapi_OPStackMessageQueue.h"
#include "openpeer/core/IStack.h"
#include "openpeer/core/IAccount.h"
#include "openpeer/core/IIdentity.h"
#include "openpeer/core/IIdentityLookup.h"
#include "openpeer/core/IConversationThread.h"
//#include "openpeer/core/ICall.h"
#include "openpeer/core/IMediaEngine.h"
#include "openpeer/core/ILogger.h"
//#define NULL ((void*) 0)

#ifndef _ANDROID_OPENPEER_EVENT_MANAGER_H_
#define _ANDROID_OPENPEER_EVENT_MANAGER_H_

using namespace openpeer::core;

class EventManager : public IStackMessageQueueDelegate,
public IStackDelegate,
public IMediaEngineDelegate,
public IAccountDelegate,
public IIdentityDelegate,
public IConversationThreadDelegate,
//public ICallDelegate,
public IIdentityLookupDelegate,
public ILoggerDelegate
{
public:
	//IStackMessageQueueDelegate implementation
	virtual void onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread();

	//IStackDelegate implementation
	virtual void onStackShutdown(openpeer::core::IStackAutoCleanupPtr);

	//IMediaEngineDelegate implementation
	virtual void onMediaEngineAudioRouteChanged(openpeer::core::IMediaEngine::OutputAudioRoutes audioRoute);
	virtual void onMediaEngineAudioSessionInterruptionBegan();
	virtual void onMediaEngineAudioSessionInterruptionEnded();
	virtual void onMediaEngineFaceDetected();
	virtual void onMediaEngineVideoCaptureRecordStopped();

	//IAccountDelegate implementation
	virtual void onAccountStateChanged(IAccountPtr account, IAccount::AccountStates state);
	virtual void onAccountAssociatedIdentitiesChanged(IAccountPtr account);
	virtual void onAccountPendingMessageForInnerBrowserWindowFrame(IAccountPtr account);

	//IConversationThreadDelegate implementation
	virtual void onConversationThreadNew(IConversationThreadPtr conversationThread);
	virtual void onConversationThreadContactsChanged(IConversationThreadPtr conversationThread);
	virtual void onConversationThreadContactStateChanged(
			IConversationThreadPtr conversationThread,
			IContactPtr contact,
			IConversationThread::ContactStates state
	);
	virtual void onConversationThreadMessage(
			IConversationThreadPtr conversationThread,
			const char *messageID
	);
	virtual void onConversationThreadMessageDeliveryStateChanged(
			IConversationThreadPtr conversationThread,
			const char *messageID,
			IConversationThread::MessageDeliveryStates state
	);
	virtual void onConversationThreadPushMessage(
			IConversationThreadPtr conversationThread,
			const char *messageID,
			IContactPtr contact
	);

//	//ICallDelegate implementation
//	virtual void onCallStateChanged(ICallPtr call, ICall::CallStates state);

	//IIdentityDelegate implementation
	virtual void onIdentityStateChanged(
			IIdentityPtr identity,
			IIdentity::IdentityStates state
	);
	virtual void onIdentityPendingMessageForInnerBrowserWindowFrame(IIdentityPtr identity);
	virtual void onIdentityRolodexContactsDownloaded(IIdentityPtr identity);

	//IIdentityLookupDelegate implementation
	virtual void onIdentityLookupCompleted(
			IIdentityLookupPtr identity
	);

	//ILoggerDelegate implementation
	virtual void onNewSubsystem(
			SubsystemID subsystemUniqueID,
			const char *subsystemName
	);
	virtual void onLog(
			SubsystemID subsystemUniqueID,
			const char *subsystemName,
			Severity severity,
			Level level,
			const char *message,
			const char *function,
			const char *filePath,
			ULONG lineNumber
	);


	virtual ~EventManager();
};

typedef boost::shared_ptr<EventManager> EventManagerPtr;


#endif
