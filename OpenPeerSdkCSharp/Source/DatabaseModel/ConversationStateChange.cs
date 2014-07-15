using System;
using SQLite;

namespace DatabaseModel.ConversationStateChangeEntry
{
	/// <summary>
	/// Conversation state change.
	/// </summary>
	[Table("conversation_state_change")]
	public class ConversationStateChange
	{
		private DateTime _time=DateTime.UtcNow;

		[PrimaryKey,AutoIncrement]
		public int thread_id
		{ 
			get;
			set;
		}

		public DateTime time
		{ 
			get
			{
				return _time;
			}
			set
			{
				_time = value;
			}
		}

	}
}

