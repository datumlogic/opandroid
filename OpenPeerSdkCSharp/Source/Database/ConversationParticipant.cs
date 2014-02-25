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

			[Indexed]
			public int ConversationId { get; set; }

			[Indexed]
			public int ParticipantId { get; set; }
		}
	}
}
