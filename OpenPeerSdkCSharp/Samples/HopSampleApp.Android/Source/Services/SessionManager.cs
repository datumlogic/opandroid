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

namespace HopSampleApp
{
	// Singleton Pattern only for translation
	public class SessionManager
	{
		public Session SessionWithActiveCall {get; set;}

		private static SessionManager instance;
		private SessionManager()
		{ 
			/* code  */
		}
		public static SessionManager SharedSessionManager()
		{
			if (instance == null)
				instance = new SessionManager();
			return instance;
		}
		public static void onFaceDetected()
		{
			//code
		}
		public void SetLatestValidConversationThread(object something /*need fix */)
		{
			/* code */
		}
		public void CreateSessionForConversationThread(object something/*need fix */)
		{
			/* code */
		}
		public object ProceedWithExistingSessionForContactNewConversationThread(object something1,object something2)
		{
			return null;
			/* code */
		}
		/**
        
           Handle case when call is in closing state.
        
           @param call HOPCall Ending call
        
           */
		void onCallClosing(HOPCall call)
		{
			String sessionId = call.getConversationThread().getThreadId();
			Session session = SessionManager.sharedSessionManager().sessionsDictionary().objectForKey(sessionId);
			HOPMediaEngine.sharedInstance().stopVideoCapture();
			session.currentCall().hangup(HOPCallClosedReasonNone);
			//Set flag that there is no active call
			this.setActiveCallSessionCallActive(session, false);
		}

		public void OnCallEnded(object something){}
		public void OnCallOpened(object something){}

		void onCallRinging(HOPCall call)
		{
			String sessionId = call.getConversationThread().getThreadId();
			if (sessionId.length() > 0)
			{
				Session session = SessionManager.SharedSessionManager.sessionsDictionary().objectForKey(sessionId);
				if (session)
				{
					OpenPeer.sharedOpenPeer().mainViewController().showIncominCallForSession(session);
					SoundManager.sharedSoundsManager().playRingingSound();
				}

			}

		}
		public void OnCallIncoming(object something){}
		public void OnCallPreparing(object something){}


	}
}

