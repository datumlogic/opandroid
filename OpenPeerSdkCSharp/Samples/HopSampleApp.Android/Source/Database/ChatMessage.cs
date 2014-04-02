using System;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class ChatMessage
		{
			[PrimaryKey, AutoIncrement, Column("_id")]
			public int Id { get; set; }

			[Indexed, Unique, MaxLength(100)]
			public string MessageId { get; set; }
			
			public int ParticipantID { get; set; }

			[Indexed]
			public DateTime When { get; set; }

			public string MimeType { get; set; }

			public string Message { get; set; }
		}
	}
}
