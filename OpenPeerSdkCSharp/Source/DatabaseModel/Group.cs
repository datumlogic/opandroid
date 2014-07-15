using System;
using SQLite;

namespace DatabaseModel.GroupEntry
{
	/// <summary>
	/// Group.
	/// </summary>
	[Table("group")]
	public class Group
	{
		[PrimaryKey,AutoIncrement]
		public int group_id
		{
			get;
			set;
		}//Group id

		[MaxLength(1024)]
		public string group_name
		{
			get;
			set;
		}//Group name

	
	}
}

