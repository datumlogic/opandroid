using System;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class Conversation
		{
			[PrimaryKey, AutoIncrement, Column("_id")]
			public int Id { get; set; }

			private DateTime created = DateTime.UtcNow;

			public DateTime Created {
				get { return created; }
				set { created = value; }
			}
		}
	}
}
