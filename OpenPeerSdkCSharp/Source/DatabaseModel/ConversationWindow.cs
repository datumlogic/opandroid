using System;
using SQLite;

namespace DatabaseMode.ConversationWindowEntry
{
	/// <summary>
	/// Conversation window.
	/// </summary>
	[Table("conversation_window")]
	public class ConversationWindow
	{
		private DateTime _start= DateTime.UtcNow;
		private DateTime _end= DateTime.UtcNow;

		[PrimaryKey,AutoIncrement]
		public int group_id
		{ 
			get;
			set;
		}//Group id

		public int window_id
		{
			get;
			set;
		}//Window id

		public int lrm_id
		{
			get;
			set;
		}//Last read message

		[MaxLength(256)]
		public string is_host
		{
			get;
			set;
		}//Thread host

		public DateTime start_time
		{
			get
			{
				return _start;
			}
			set
			{
				_start = value;
			}
		}//Start time

		public DateTime end_time
		{
			get
			{
				return _end; 
			}
			set
			{
				_end = value;
			}
		}//End time
	}
}

