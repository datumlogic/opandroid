using System;
using SQLite;
namespace DatabaseModel.IdentityContactEntry
{
	/// <summary>
	/// Identity contacts.
	/// </summary>
	[Table("identity_contact")]
	public class IdentityContacts
	{
		[PrimaryKey,AutoIncrement]
		public int associated_ientity_id
		{ 
			get;
			set;
		}//Assoxiated identity id

		public int user_id
		{
			get;
			set;
		}//User id

		public int stable_id
		{ 
			get;
			set;
		}//Stable Id

		public int contact_id
		{
			get;
			set;
		}//Contact id

		[MaxLength(1024)]
		public string peerfile_public
		{ 
			get;
			set;
		}//Peer file pubic

		[MaxLength(1024)]
		public string identity_proof_bundle
		{
			get;
			set;
		}//Identity proof bundle

		public int priority
		{
			get;
			set;
		}//Priority

		public int weight
		{
			get;
			set;
		}//weight

		public DateTime last_update_time
		{ 
			get;
			set;
		}//Last update time

		[MaxLength(1024)]
		public string expire
		{
			get;
			set;
		}//Expire
	}
}

