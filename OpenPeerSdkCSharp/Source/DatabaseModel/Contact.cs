using System;
using SQLite;
namespace DatabaseModel.ContactEntry
{
	/// <summary>
	/// Contact.
	/// </summary>
	[Table("contact")]
	public class Contact
	{
		[PrimaryKey, AutoIncrement]
		public int contact_id
		{ 
			get;
			set;
		}

		public int associated_ientity_id
		{ 
			get;
			set; 
		}

		[Indexed, Unique, MaxLength(1024)]
		public string identity_uri
		{ 
			get;
			set;
		}

		[MaxLength(1024)]
		public string identity_provider
		{ 
			get;
			set;
		}

		[MaxLength(256)]
		public string name
		{ 
			get;
			set;
		}

		[MaxLength(1024)]
		public string profile_url
		{ 
			get;
			set;
		}

		[MaxLength(1024)]
		public string vprofile_url
		{ 
			get;
			set;
		}
		//Identity
	}
}

