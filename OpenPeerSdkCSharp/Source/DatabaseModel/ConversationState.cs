using System;
using SQLite;

namespace DatabaseModel.ConversationStateEntry
{
	/// <summary>
	/// Conversation state.
	/// </summary>
	[Table("conversation_sate")]
	public class ConversationState
	{
		[PrimaryKey,AutoIncrement]
		public int state_change_id
		{ 
			get;
			set;
		}

		[MaxLength(256)]
		public string state_type
		{ 
			get;
			set;
		}
	}
}

