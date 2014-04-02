using System;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class ConversationPartipantRecord
		{
			public DateTime lastUpdated = DateTime.UtcNow;

			[PrimaryKey, AutoIncrement, Column("_id")]
			public int Id { get; set; }

			[Indexed, MaxLength(1024)]
			public string IdentityUri { get; set; }

			[MaxLength(1024)]
			public string PeerUri { get; set; }

			[Indexed, MaxLength(128)]
			public string PeerUriContactId { get; set; }

			[MaxLength(256)]
			public string StableId { get; set; }

			[Indexed]
			public int ParticipantId { get; set; }

			[Indexed]
			public DateTime LastUpdated { get { return lastUpdated; } set { lastUpdated = value; } }

			// only used if identity / contact ID cannot be found in contacts table
			public string LastKnownDisplayName { get; set; }
			public string LastKnownAvatarUrl { get; set; }
		}
	}
}
