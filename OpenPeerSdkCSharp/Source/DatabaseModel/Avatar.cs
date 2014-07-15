using System;
using SQLite;

namespace DatabaseModel.AvatarEntry
{
	/// <summary>
	/// Avatar.
	/// </summary>
	[Table("avatar")]
	public class Avatar
	{
		[PrimaryKey,AutoIncrement]
		public int contact_id
		{
			get;
			set;
		}//Contact id

		[MaxLength(256)]
		public string name
		{ 
			get;
			set;
		}//Avatar name

		[MaxLength(1024)]
		public string url
		{ 
			get;
			set;
		}//Avatar url
	}
}

