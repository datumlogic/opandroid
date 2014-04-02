using System;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class Contact
		{
			[PrimaryKey, AutoIncrement, Column("_id")]
			public int Id { get; set; }

			[Indexed, Unique, MaxLength(1024)]
			public string IdentityUri { get; set; }

			public string Provider { get; set; }

			[MaxLength(256)]
			public string StableId { get; set; }

			[MaxLength(1024)]
			public string PeerUri { get; set; }

			[Indexed, MaxLength(128)]
			public string PeerUriContactId { get; set; }

			public string PeerFile { get; set; }

			public int Priority { get; set; }
			public int Weigth { get; set; }

			public DateTime Updated { get; set; }
			public DateTime Expires { get; set; }

			[MaxLength(256)]
			public string DisplayName { get; set; }

			[MaxLength(1024)]
			public string WebProfileUrl { get; set; }

			[MaxLength(1024)]
			public string ProgrammicUrl { get; set; }

			[MaxLength(1024)]
			public string FeedURL { get; set; }

			[MaxLength(1024)]
			public string AvatarUrl { get; set; }

			public DateTime LastChecked { get; set; }

			public bool RemovalFlag { get; set; }
		}
	}
}
