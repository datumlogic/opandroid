using System;
using SQLite;
namespace DatabaseModel.AccountEntry
{
	/// <summary>
	/// Account.
	/// </summary>
	[Table("account")]
	public class Account
	{
		[MaxLength(1024)]
		public string relogin_info
		{
			get;
			set;
		}//Relogin info
	}
}

