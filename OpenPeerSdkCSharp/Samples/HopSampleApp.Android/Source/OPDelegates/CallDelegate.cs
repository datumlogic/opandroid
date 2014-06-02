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
using System.Threading.Tasks;
using System.Threading;
using HopSampleApp.Services;
using HopSampleApp.Enums;

namespace HopSampleApp
{
	//only for translation
	public class HOPCall{}



	//End
	class CallDelegate
	{

		void OnCallStateChangedCallState(HOPCall call, HOPCallStates callState)
		{
			/*
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "Call state: %@", Utility.GetCallStateAsString(call.GetState()));
			//SessionManager.SharedSessionManager().SetLatestValidConversationThread(call.GetConversationThread());
			//String sessionId = call.GetConversationThread().GetThreadId();
			ThreadPool.QueueUserWorkItem( delegate {
				//SessionViewController_iPhone sessionViewController = OpenPeer.SharedOpenPeer().MainViewController().SessionViewControllersDictionary().ObjectForKey(sessionId);
				//sessionViewController.UpdateCallState();
				switch (callState)
				{
				case HOPCallStates.HOPCallStatePreparing :
						//Receives both parties, caller and callee.
						SessionManager.SharedSessionManager().OnCallPreparing(call);
						break;
				case HOPCallStates.HOPCallStateIncoming :
						//Receives just callee
						SessionManager.SharedSessionManager().OnCallIncoming(call);
						break;
				case HOPCallStates.HOPCallStatePlaced :
						//Receives just calller
						break;
				case HOPCallStates.HOPCallStateEarly :
						//Currently is not in use
						break;
				case HOPCallStates.HOPCallStateRinging :
						//Receives just callee side. Now should play ringing sound
						SessionManager.SharedSessionManager().OnCallRinging(call);
						//[[SoundManager sharedSoundsManager] playRingingSound];
						break;
				case HOPCallStates.HOPCallStateRingback :
						//Receives just caller side
					//SoundManager.SharedSoundsManager().PlayCallingSound();
						break;
				case HOPCallStates.HOPCallStateOpen :
						//Receives both parties. Call is established
						SoundManager.SharedSoundsManager().StopCallingSound();
						SoundManager.SharedSoundsManager().StopRingingSound();
						SessionManager.SharedSessionManager().OnCallOpened(call);
					//sessionViewController.StartTimer();
						break;
				case HOPCallStates.HOPCallStateActive :
						//Currently not in use
						break;
				case HOPCallStates.HOPCallStateInactive :
						//Currently not in use
						break;
				case HOPCallStates.HOPCallStateHold :
						//Receives both parties
						break;
				  case HOPCallStates.HOPCallStateClosing :
						//Receives both parties
					if (OpenPeer.sharedOpenPeer().AppEnteredBackground()) OpenPeer.sharedOpenPeer().PrepareAppForBackground();

						SessionManager.SharedSessionManager().OnCallClosing(call);
						SoundManager.SharedSoundsManager().StopCallingSound();
						SoundManager.SharedSoundsManager().StopRingingSound();
					//sessionViewController.StopTimer();
						break;
				case HOPCallStates.HOPCallStateClosed :
						//Receives both parties
						SessionManager.SharedSessionManager().OnCallEnded(call);
						break;
				case HOPCallStates.HOPCallStateNone :
					default :
						break;
					}

				});
			*/
		}

	}
}

