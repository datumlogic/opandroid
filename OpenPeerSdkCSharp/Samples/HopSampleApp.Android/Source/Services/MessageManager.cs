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
	class MessageManager
	{
		private static MessageManager instance;
		private MessageManager()
		{ 
			/* code  */
		}
		public static MessageManager SharedMessageManager()
		{
			if (instance == null)
				instance = new MessageManager();
			return instance;
		}
		public void GetTypeForSystemMessage(object something /* need fix */)
		{
			/* code  */
		}
		public void OnMessageReceivedForSessionId(object something,object something1)
		{
			/* code */
		}
	}
}

