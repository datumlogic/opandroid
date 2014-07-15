using System;
using SQLite;

namespace DatabaseModel.GroupParticipantEntry
{
	/// <summary>
	/// Group window participants.
	/// </summary>
	[Table("group_window_participants")]
	public class GroupWindowParticipants
	{
		[PrimaryKey,AutoIncrement]
		public int group_id
		{ 
			get;
			set;
		}//Group id

		public int identity_id
		{ 
			get;
			set;
		}//Identity id

		[MaxLength(256)]
		public string name
		{ 
			get;
			set;
		}//Identity name
	}
}

