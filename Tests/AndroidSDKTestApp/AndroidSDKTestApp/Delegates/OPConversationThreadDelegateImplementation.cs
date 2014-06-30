
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
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;
using Android.Util;

namespace AndroidSDKTestApp
{
	public class OPConversationThreadDelegateImplementation:OPConversationThreadDelegate
	{
		public override void OnConversationThreadNew (OPConversationThread p0)
		{
			throw new NotImplementedException ();
		}

		public override void OnConversationThreadContactsChanged (OPConversationThread conversationThread)
		{
			Log.Debug("output", "onConversationThreadContactsChanged");
		}

		public override void OnConversationThreadContactStateChanged (OPConversationThread conversationThread, OPContact contact,ContactStates state)
		{
			Log.Debug("output", "onConversationThreadContactStateChanged  state = " + state.ToString());
		}

		public override void OnConversationThreadMessage (OPConversationThread conversationThread, String messageID)
		{
			Log.Debug("output", "onConversationThreadMessage = " + messageID);

			OPMessage message = conversationThread.GetMessage(messageID);

			Log.Debug("output","Message received from " + message.From);
			Log.Debug("output","Message received type " + message.MessageType);
			Log.Debug("output","Message received time " + message.Time.ToString());
			Log.Debug("output","Message received =  " + message.Message);
			//LoginManager.mMessages.Add(message);
			//LoginManager.mChatMessageReceiver.onChatMessageReceived;
		}

		public override void OnConversationThreadMessageDeliveryStateChanged (OPConversationThread conversationThread, String messageID,MessageDeliveryStates state)
		{
			Log.Debug("output", "onConversationThreadMessageDeliveryStateChanged = " + messageID + "state = " + state.ToString());
		}

		public override void OnConversationThreadPushMessage (OPConversationThread conversationThread, String messageID,OPContact contact)
		{
			Log.Debug ("output","onConversationThreadPushMessage = " + messageID);
		}

	}
}

