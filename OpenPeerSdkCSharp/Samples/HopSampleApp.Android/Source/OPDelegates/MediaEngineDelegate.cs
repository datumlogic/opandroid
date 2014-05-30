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
using System.Threading;
using System.Threading.Tasks;
namespace HopSampleApp
{
	// Singleton Pattern only for translation

	public class HOPMediaEngineOutputAudioRoutes{}
	//Simulation MediaEngine class
	public class HOPMediaEngine
	{
		private static HOPMediaEngine instance;
		private HOPMediaEngine(){ /* code  */ }
		public static HOPMediaEngine SharedInstance(){if (instance == null)	instance = new HOPMediaEngine();return instance;}
		public void PauseVoice(bool value){ /*code*/ }
	}

	//Simulation SessionManager class
	public class SessionManager
	{
		private static SessionManager instance;
		private SessionManager(){ /* code  */ }
		public static SessionManager SharedSessionManager(){if (instance == null)	instance = new SessionManager();return instance;}
		public void OnFaceDetected(){ /*code*/ }
	}

	//end

	class MediaEngineDelegate
	{

		public static void OnMediaEngineAudioRouteChanged(HOPMediaEngineOutputAudioRoutes audioRoute)
		{
			ThreadPool.QueueUserWorkItem( delegate {
			});


			//dispatch_async(dispatch_get_main_queue(), delegate(){});

		}

		public static void OnMediaEngineFaceDetected()
		{
			ThreadPool.QueueUserWorkItem( delegate {
				 SessionManager.SharedSessionManager().OnFaceDetected();

			});

			//dispatch_async(dispatch_get_main_queue(), delegate(){	SessionManager.SharedSessionManager().OnFaceDetected();	});
				
		}

		public static void OnMediaEngineVideoCaptureRecordStopped()
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Video capture record has stopped.");
			ThreadPool.QueueUserWorkItem( delegate {

			});
			//dispatch_async(dispatch_get_main_queue(), delegate(){});

		}

		public static void OnMediaEngineAudioSessionInterruptionBegan()
		{
			HOPMediaEngine.SharedInstance().PauseVoice(true);

		}

		public static void OnMediaEngineAudioSessionInterruptionEnded()
		{
			HOPMediaEngine.SharedInstance().PauseVoice(false);

		}

	}
}

