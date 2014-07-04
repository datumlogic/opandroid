
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;
namespace HopSampleApp
{
	public class AccountStates:EnumBaseType<AccountStates>
	{
		public enum ssssss{}
		public static readonly AccountStates AccountState_PendingOP  = new AccountStates(0, "AccountState_Pending ");
		public static readonly AccountStates  AccountState_PendingPeerFilesGeneration= new AccountStates(1, " AccountState_PendingPeerFilesGeneration");

		public AccountStates(int key, string value) : base(key, value)
		{
		}

		public static IReadOnlyCollection<AccountStates> GetValues()
		{
			return GetBaseValues();
		}

		public static AccountStates GetByKey(int key)
		{
			return GetBaseByKey(key);
		}

	}
}

