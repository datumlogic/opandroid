using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp.Enums
{

	//#if	//openpeer_ios_sdk_OpenPeerTypes_h

	//#define //openpeer_ios_sdk_OpenPeerTypes_h

	//#pragma mark - HOPCall enums
	public enum HOPCallStates
	{
		HOPCallStateNone, // call has no state yet
		HOPCallStatePreparing, // call is negotiating in the background - do not present this call to a user yet...
		HOPCallStateIncoming, // call is incoming from a remote party
		HOPCallStatePlaced, // call has been placed to the remote party
		HOPCallStateEarly, // call is outgoing to a remote party and is receiving early media (media before being answered)
		HOPCallStateRinging, // call is incoming from a remote party and is ringing
		HOPCallStateRingback, // call is outgoing to a remote party and remote party is ringing
		HOPCallStateOpen, // call is open
		HOPCallStateActive, // call is open, and participant is actively communicating
		HOPCallStateInactive, // call is open, and participant is inactive
		HOPCallStateHold, // call is open but on hold
		HOPCallStateClosing, // call is hanging up
		HOPCallStateClosed, // call has ended
	};
	public enum HOPCallClosedReasons
	{
		HOPCallClosedReasonNone = 0,

		HOPCallClosedReasonUser = 200,

		HOPCallClosedReasonRequestTimeout = 408,
		HOPCallClosedReasonTemporarilyUnavailable = 480,
		HOPCallClosedReasonBusy = 486,
		HOPCallClosedReasonRequestTerminated = 487,
		HOPCallClosedReasonNotAcceptableHere = 488,

		CallClosedReasonServerInternalError = 500,

		CallClosedReasonDecline = 603,
	};

	//#pragma mark - HOPConversationThread enums
	public enum HOPConversationThreadMessageDeliveryStates
	{
		HOPConversationThreadMessageDeliveryStateDiscovering = 0,
		HOPConversationThreadMessageDeliveryStateUserNotAvailable = 1,
		HOPConversationThreadMessageDeliveryStateDelivered = 2,
	};

	public enum HOPConversationThreadContactStates
	{
		HOPConversationThreadContactStateNotApplicable,
		HOPConversationThreadContactStateFinding,
		HOPConversationThreadContactStateConnected,
		HOPConversationThreadContactStateDisconnected
	};
	public enum HOPAccountStates
	{
		HOPAccountStatePending,
		HOPAccountPendingPeerFilesGeneration,
		HOPAccountWaitingForAssociationToIdentity,
		HOPAccountWaitingForBrowserWindowToBeLoaded,
		HOPAccountWaitingForBrowserWindowToBeMadeVisible,
		HOPAccountWaitingForBrowserWindowToClose,
		HOPAccountStateReady,
		HOPAccountStateShuttingDown,
		HOPAccountStateShutdown,
	};


	//#pragma mark - HOPIdentity enums
	public enum HOPIdentityStates
	{
		HOPIdentityStatePending,
		HOPIdentityStatePendingAssociation,
		HOPIdentityStateWaitingAttachmentOfDelegate,
		HOPIdentityStateWaitingForBrowserWindowToBeLoaded,
		HOPIdentityStateWaitingForBrowserWindowToBeMadeVisible,
		HOPIdentityStateWaitingForBrowserWindowToClose,
		HOPIdentityStateReady,
		HOPIdentityStateShutdown
	};



	//#pragma mark - HOPClientLog enums
	public enum HOPLoggerSeverities
	{
		HOPLoggerSeverityInformational,
		HOPLoggerSeverityWarning,
		HOPLoggerSeverityError,
		HOPLoggerSeverityFatal
	}; //Replacing HOPClientLogSeverities

	public enum HOPLoggerLevels
	{
		HOPLoggerLevelNone,
		HOPLoggerLevelBasic,
		HOPLoggerLevelDetail,
		HOPLoggerLevelDebug,
		HOPLoggerLevelTrace,
		HOPLoggerLevelInsane,

		HOPLoggerTotalNumberOfLevels
	} ;//Replacing HOPClientLogSeverities


	//#pragma mark - Client enums
	public enum HOPContactTypes
	{
		HOPContactTypeOpenPeer,
		HOPContactTypeExternal
	};


	//#pragma mark - HOPMediaEngine enums
	public enum HOPMediaEngineCameraTypes
	{
		HOPMediaEngineCameraTypeNone,
		HOPMediaEngineCameraTypeFront,
		HOPMediaEngineCameraTypeBack
	};

	public enum HOPMediaEngineVideoOrientations
	{
		HOPMediaEngineVideoOrientationLandscapeLeft,
		HOPMediaEngineVideoOrientationPortraitUpsideDown,
		HOPMediaEngineVideoOrientationLandscapeRight,
		HOPMediaEngineVideoOrientationPortrait
	};

	public enum HOPMediaEngineOutputAudioRoutes
	{
		HOPMediaEngineOutputAudioRouteHeadphone,
		HOPMediaEngineOutputAudioRouteBuiltInReceiver,
		HOPMediaEngineOutputAudioRouteBuiltInSpeaker
	};
	//#endif
	class HOPTypes
	{


	}
}

