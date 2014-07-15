using System;
using SQLite;

namespace DatabaseModel.IdentityEntry
{
	/// <summary>
	/// Identity.
	/// </summary>
	[Table("identity")]
	public class Identity
	{
		[PrimaryKey,AutoIncrement]
		public int stable_id
		{ 
			get;
			set;
		}

		[MaxLength(256)]
		public string provider
		{ 
			get;
			set;
		}

		[ MaxLength(1024)]
		public string uri
		{
			get; 
			set;
		}

		//The "selfContact" of the identity
		public int contact_id
		{ 
			get;
			set;
		}
		[MaxLength(1024)]
		public string contacts_version
		{
			get;
			set;
		}
	}
}

