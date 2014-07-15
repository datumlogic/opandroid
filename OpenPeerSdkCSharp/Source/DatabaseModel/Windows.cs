using System;
using SQLite;


namespace DatabaseModel.WindowViewEntry
{
	[Table("windows")]
	public class Windows
	{	
		private DateTime lastm=DateTime.UtcNow;

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
		}//Last read message id

		[MaxLength(1024)]
		public string last_message
		{
			get;
			set;
		}//Last message

		public DateTime last_message_time
		{ 
			get
			{ 
				return lastm;
			}
			set
			{
				lastm = value;
			}
		}//Last message time

		public int user_id
		{ 
			get;
			set;
		}//Use id

		[MaxLength(1024)]
		public string name
		{
			get;
			set;
		}//Participant names

		public string avatar_urls
		{
			get;
			set;
		}//Avatars 

		public int unread_count
		{ 
			get;
			set;
		}//Unread count
	}
}

