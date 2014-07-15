using System;
using SQLite;

namespace DatabaseModel.MessageEntry
{
	[Table("message")]
	public class Message
	{
		private DateTime _time=DateTime.UtcNow;

		[PrimaryKey,AutoIncrement]
		public int message_id
		{ 
			get;
			set;
		}//Message id
		public int window_id
		{
			get;
			set;
		}//Window id

		[MaxLength(256)]
		public string type
		{ 
			get;
			set;
		}//message type

		public int sender_id
		{
			get;
			set;
		}//Sender id

		[MaxLength(1024)]
		public string text
		{
			get;
			set;
		}//Message text

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
		}//Message time

		public Boolean read
		{
			get;
			set;
		}//Message read

		public Boolean delivery_status
		{
			get;
			set;
		}//Mesagedelivery status


	}
}

