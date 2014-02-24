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

			[Indexed, Unique, MaxLength(256)]
			public string IdentityURI { get; set; }

			public string Provider { get; set; }

			[MaxLength(256)]
			public string StableID { get; set; }

			[MaxLength(1024)]
			public string PeerURI { get; set; }

			public string PeerFile { get; set; }

			public int Priority { get; set; }
			public int Weigth { get; set; }

			public DateTime Updated { get; set; }
			public DateTime Expires { get; set; }

			[MaxLength(256)]
			public string DisplayName { get; set; }

			[MaxLength(1024)]
			public string WebProfileURL { get; set; }

			[MaxLength(1024)]
			public string ProgrammicURL { get; set; }

			[MaxLength(1024)]
			public string FeedURL { get; set; }

			[MaxLength(1024)]
			public string AvatarURL { get; set; }

			public DateTime LastChecked { get; set; }

			public bool RemovalFlag { get; set; }
		}
	}
}
