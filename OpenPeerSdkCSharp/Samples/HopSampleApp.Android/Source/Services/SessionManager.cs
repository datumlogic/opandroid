using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using HopSampleApp.Enums;
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
		/*
		Session createSessionForContact(HOPRolodexContact contact)
		{
		Console.WriteLine ("Empty");

		}
		*/
		Session createSessionForConversationThread(HOPConversationThread inConversationThread)
		{
			Console.WriteLine ("Empty");
		}
		//
		Session createSessionInitiatedFromSessionForContactPeerURIs(Session inSession, string peerURIs)
		{
			Console.WriteLine ("Empty");
		}
		Session createRemoteSessionForContacts(ArrayList participants)
		{
			Console.WriteLine ("Empty");
		}
		void setValidSessionNewSessionIdOldSessionId(Session inSession, string newSessionId, string oldSessionId)
		{
			Console.WriteLine ("Empty");
		}
		Session proceedWithExistingSessionForContactNewConversationThread(HOPContact contact, HOPConversationThread inConversationThread)
		{
			Console.WriteLine ("Empty");
		}
		//need fix
		/*
		Session getSessionForContact(HOPRolodexContact contact)
		{
			Console.WriteLine ("Empty");
		}*/
		Session getSessionForSessionId(string sessionId)
		{
			Console.WriteLine ("Empty");
		}
		void makeCallForSessionIncludeVideoIsRedial(Session inSession, bool includeVideo, bool isRedial)
		{
			Console.WriteLine ("Empty");
		}
		void answerCallForSession(Session inSession)
		{
			Console.WriteLine ("Empty");
		}
		void endCallForSession(Session inSession)
		{
			Console.WriteLine ("Empty");
		}
		void onCallPreparing(HOPCall call)
		{
		}
		void onCallIncoming(HOPCall call)
		{
			Console.WriteLine ("Empty");
		}
		void onCallRinging(HOPCall call)
		{
			Console.WriteLine ("Empty");
		}
		void onCallOpened(HOPCall call)
		{
		}
		void onCallClosing(HOPCall call)
		{
			Console.WriteLine ("Empty");
		}
		bool setActiveCallSessionCallActive(Session inSession, bool callActive)
		{
			bool value = false;
			Console.WriteLine ("Empty");
			return value;
		}
		void redialCallForSession(Session inSession)
		{
			Console.WriteLine ("Empty");
		}
		void onCallEnded(HOPCall call)
		{
			Console.WriteLine ("Empty");
		}
		void onFaceDetected()
		{
			Console.WriteLine ("Empty");
		}
		void startVideoRecording()
		{
			Console.WriteLine ("Empty");
		}
		void stopVideoRecording()
		{
			Console.WriteLine ("Empty");
		}
		bool isCallInProgress()
		{
			bool value = false;
			Console.WriteLine ("Empty");
			return value;
		}
		void recreateExistingSessions()
		{
			Console.WriteLine ("Empty");
		}
		void stopAnyActiveCall()
		{
			Console.WriteLine ("Empty");
		}

		void clearAllSessions()
		{
			Console.WriteLine ("Empty");
		}

		void setLatestValidConversationThread(HOPConversationThread inConversationThread)
		{
			Console.WriteLine ("Empty");
		}

		int totalNumberOfUnreadMessages()
		{
			int ret = 0;
			Console.WriteLine ("Empty");
			return ret;
		}

	




	}
}

