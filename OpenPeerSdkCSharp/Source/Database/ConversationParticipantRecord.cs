using System;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class ConversationPartipantRecord
		{
			[PrimaryKey, AutoIncrement, Column("_id")]
			public int Id { get; set; }

			[Indexed, MaxLength(1024)]
			public string IdentityUri { get; set; }

			[Indexed, MaxLength(1024)]
			public string PeerUri { get; set; }

			[MaxLength(256)]
			public string StableId { get; set; }

			[Indexed]
			public string ParticipantId { get; set; }

			// only used if identity / contact ID cannot be found in contacts table
			public string LastKnownDisplayName { get; set; }
			public string LastKnownAvatarUrl { get; set; }
		}
	}
}
