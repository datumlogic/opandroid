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

		//public HopSampleApp.Session SessionWithActiveCall {get; set;}

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
			return null;
		}
		//
		Session createSessionInitiatedFromSessionForContactPeerURIs(Session inSession, string peerURIs)
		{
			Console.WriteLine ("Empty");
			return null;
		}
		Session createRemoteSessionForContacts(ArrayList participants)
		{
			Console.WriteLine ("Empty");
			return null;
		}
		void setValidSessionNewSessionIdOldSessionId(Session inSession, string newSessionId, string oldSessionId)
		{
			Console.WriteLine ("Empty");
		}
		Session proceedWithExistingSessionForContactNewConversationThread(HOPContact contact, HOPConversationThread inConversationThread)
		{
			Console.WriteLine ("Empty");
			return null;
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
			return null;
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
		/*
		public void onCallPreparing(HOPCall call)
		{
		}

		public void onCallIncoming(HOPCall call)
		{
			Console.WriteLine ("Empty");
		}

		public void onCallRinging(HOPCall call)
		{
			Console.WriteLine ("Empty");
		}

		public void onCallOpened(HOPCall call)
		{
		}

		public void onCallClosing(HOPCall call)
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

		public void onCallEnded(HOPCall call)
		{
			Console.WriteLine ("Empty");
		}
		*/
		public void onFaceDetected()
		{
			Console.WriteLine ("Empty");
		}

		public void startVideoRecording()
		{
			Console.WriteLine ("Empty");
		}

		public void stopVideoRecording()
		{
			Console.WriteLine ("Empty");
		}

		public bool isCallInProgress()
		{
			bool value = false;
			Console.WriteLine ("Empty");
			return value;
		}

		public void recreateExistingSessions()
		{
			Console.WriteLine ("Empty");
		}

		public void stopAnyActiveCall()
		{
			Console.WriteLine ("Empty");
		}

		public void clearAllSessions()
		{
			Console.WriteLine ("Empty");
		}

		public void setLatestValidConversationThread(HOPConversationThread inConversationThread)
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

