using System;
using SQLite;

namespace DatabaseModel.CallParticipantsEntry
{
	/// <summary>
	/// Call participants.
	/// </summary>
	[Table("call_participants")]
	public class CallParticipants
	{
		[PrimaryKey,AutoIncrement]
		public int call_id
		{ 
			get;
			set;
		}

		public int stable_id
		{ 
			get;
			set;
		}
	}
}

