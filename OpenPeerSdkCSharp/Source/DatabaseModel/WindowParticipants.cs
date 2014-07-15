using System;
using SQLite;

namespace DatabaseModel.WindowParticipantEntry
{
	/// <summary>
	/// Window participants.
	/// </summary>
	[Table("window_participants")]
	public class WindowParticipants
	{
		public int window_id
		{ 
			get;
			set;
		}//Window id

		public int user_id
		{
			get;
			set;
		}//User id

		[MaxLength(256)]
		public string name
		{
			get;
			set;
		}//User name

		[MaxLength(1024)]
		public string avatar
		{ 
			get;
			set;
		}//User avata

	}
}

