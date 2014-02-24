using System;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class ConversationPartipant
		{
			[PrimaryKey, AutoIncrement, Column("_id")]
			public int Id { get; set; }

			public int ConversationId { get; set; }
		}
	}
}
