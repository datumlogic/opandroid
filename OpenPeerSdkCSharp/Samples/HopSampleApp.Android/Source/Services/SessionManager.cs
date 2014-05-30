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
		public void OnFaceDetected()
		{
			/* code */ 
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
		public void OnCallClosing(object something){}
		public void OnCallEnded(object something){}
		public void OnCallOpened(){}
		public void OnCallRinging(object something){}
		public void OnCallIncoming(object something){}
		public void OnCallPreparing(object something){}
	}
}

