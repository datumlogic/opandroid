/*
Copyright (c) 2014, hookflash Inc.
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FreeBSD Project.
*/
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
	};//Replacing HOPClientLogSeverities

	public enum HOPContactTypes
	{
		HOPContactTypeOpenPeer,
		HOPContactTypeExternal
	};

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

	class HOPTypes { }
}

