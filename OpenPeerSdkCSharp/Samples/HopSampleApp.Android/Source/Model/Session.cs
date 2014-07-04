
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
	class Session
	{
		public ArrayList ParticipantsArray {get; set;}

		//HOPConversationThread _conversationThread;
		//public HOPCall CurrentCall {get; set;}

		public bool IsRedial {get; set;}

		public ArrayList MessageArray {get; set;}

		public ArrayList UnreadMessageArray {get; set;}

		public ArrayList SessionIdsHistory {get; set;}//NSMutableSet

		public ArrayList ArrayMergedConversationThreads {get; set;}

		/*
		public HOPConversationThread ConversationThread
		{ 
			get
			{
				return this._conversationThread;
			}
			set
			{
				if (!this.ArrayMergedConversationThreads.Contains(value))
				{
					if (value !=null) this.ArrayMergedConversationThreads.Add(value);
					else this.ArrayMergedConversationThreads.Clear();

				}

				//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, NSString.stringWithFormat("%@", (value == _conversationThread ? NSString.stringWithFormat("Using same conversation thread %@", value.getThreadId()) : NSString.stringWithFormat("Switched from %@ conversation thread to %@", _conversationThread.getThreadId(), value.getThreadId()))));
				_conversationThread = value; 
			}
		}

		*/
			

		/*
		public Session(HOPRolodexContact inContact, HOPConversationThread inConverationThread)
		{
			this.ParticipantsArray = new ArrayList();
			this.ParticipantsArray.Add(inContact);
			this.MessageArray = new ArrayList();
			this.UnreadMessageArray = new ArrayList();
			this.SessionIdsHistory = new ArrayList ();//NSMutableSet();
			this.ArrayMergedConversationThreads = new ArrayList();
			this.ConversationThread= inConverationThread;
			this.SessionIdsHistory.Add(inConverationThread.GetThreadId());
		}*/
		/*
		public Session(ArrayList inContacts, HOPConversationThread inConverationThread)
		{
			this.ParticipantsArray = new ArrayList();
			if (inContacts !=null)
				this.ParticipantsArray=new ArrayList(inContacts);

			this.MessageArray = new ArrayList();
			this.SessionIdsHistory = new ArrayList();//NSMutableSet();
			this.UnreadMessageArray = new ArrayList();
			this.ConversationThread = inConverationThread;
			this.SessionIdsHistory.Add(inConverationThread.GetThreadId());
		}*/

	}
}

