using System;
using SQLite;

namespace OpenPeerSampleAppCSharp
{
	namespace Database
	{
		public class ChatMessage {
			[PrimaryKey, AutoIncrement, Column("_id")]
			public int Id { get; set; }

			[Unique, MaxLength(100)]
			public string MessageId { get; set; }
			
			public int ParticipantID { get; set; }

			public DateTime When { get; set; }

			public string MimeType { get; set; }

			public string Message { get; set; }
		}
	}
}
