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
using System.Collections;
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
using HopSampleApp.Enums;
namespace HopSampleApp
{
	//Only for translation


	public class HOPContact{}
	public class HOPMessage{}

	public class HOPConversationThread 
	{
		public static void StringForContactState(string str){}
		public static string GetMessageForID(string messageid)
		{ 
			return messageid;
		}
		public  string GetThreadId(){ return null;}

		public string GetContacts()
		{
			return null;
		}
	}
	//End
	class ConversationThreadDelegate
	{
		//need fox

		void OnConversationThreadNew(HOPConversationThread conversationThread)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Handling a new conversation thread creation.");
			ThreadPool.QueueUserWorkItem( delegate {
				/*
				if (conversationThread)
					{
					ArrayList participants = conversationThread.GetContacts();
					if (participants.Count > 0)
						{
						HOPContact participant =participants.ObjectAtIndex(0);
							if (!SessionManager.SharedSessionManager().ProceedWithExistingSessionForContactNewConversationThread(participant, conversationThread))
							{
								SessionManager.SharedSessionManager().CreateSessionForConversationThread(conversationThread);
							}

						}

					}
					*/

				});
		}
		//
		void OnConversationThreadContactsChanged(HOPConversationThread conversationThread)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Conversation thread <%@> contact changed.", conversationThread);
			ThreadPool.QueueUserWorkItem( delegate {

			});
		}
		//
		void OnConversationThreadContactStateChangedContactContactState(HOPConversationThread conversationThread, HOPContact contact, HOPConversationThreadContactStates contactState)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Conversation thread <%@> contact <%@> state: %@", conversationThread, contact, HOPConversationThread.StringForContactState(contactState));
			ThreadPool.QueueUserWorkItem( delegate {

			});
		}
		//
		void OnConversationThreadMessageMessageID(HOPConversationThread conversationThread, string messageID)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Handling a new message with id %@ for conversation thread.", messageID);
			ThreadPool.QueueUserWorkItem( delegate {
				SessionManager.SharedSessionManager().setLatestValidConversationThread(conversationThread);
				/*
				HOPMessage message = conversationThread.GetMessageForID(messageID);
				if (message)
				{
					MessageManager.SharedMessageManager().OnMessageReceivedForSessionId(message, conversationThread.GetThreadId());
				}
				*/
			});
		}
		//
		void OnConversationThreadMessageDeliveryStateChangedMessageIDMessageDeliveryStates(HOPConversationThread conversationThread, string messageID, HOPConversationThreadMessageDeliveryStates messageDeliveryStates)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Conversation thread message with id %@ delivery state has changed to: %@", messageID, HOPConversationThread.StringForMessageDeliveryState(messageDeliveryStates));
		}
		//
		void OnConversationThreadPushMessageMessageIDContact(HOPConversationThread conversationThread, string messageID, HOPContact coreContact)
		{
			//need fix
			/*
			//#if APNS_ENABLED
			if (coreContact)
			{
				bool missedCall = false;
                HOPMessage message = conversationThread.GetMessageForID(messageID);
				if (message)
				{
					message.Contact = coreContact;
					HOPRolodexContact contact = HOPModelManager.SharedModelManager().GetRolodexContactsByPeerURI(coreContact.GetPeerURI()).ObjectAtIndex(0);
					if (contact)
					{
						String messageText = null;
						if (MessageManager.SharedMessageManager().GetTypeForSystemMessage(message) == SystemMessage_CheckAvailability)
						{
							messageText = String.Format ("{0} {1}", HOPModelManager.SharedModelManager ().GetLastLoggedInHomeUser ().GetFullName (), "Missed call");
							missedCall = true;
							APNSManager.SharedAPNSManager().SendPushNotificationForContactMessageMissedCall(coreContact, messageText, missedCall);
						}
						else if (!message.Type.IsEqualToString(messageTypeSystem))
						{
							messageText = message.Text;
							APNSManager.SharedAPNSManager().SendRichPushNotificationForMessageMissedCall(message, false);
						}
					}
				}
			}
			*/
			//#endif

		}
	}
}

