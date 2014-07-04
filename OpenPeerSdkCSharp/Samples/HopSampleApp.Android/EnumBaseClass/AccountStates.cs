
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
		public static readonly AccountStates AccountState_Pending = new AccountStates(0, "AccountState_Pending");
		public static readonly AccountStates AccountState_PendingPeerFilesGeneration = new AccountStates(1, "AccountState_PendingPeerFilesGeneration");
		public static readonly AccountStates AccountState_WaitingForAssociationToIdentity= new AccountStates(2,"AccountState_WaitingForAssociationToIdentity");
		public static readonly AccountStates AccountState_WaitingForBrowserWindowToBeLoaded= new AccountStates(3,"AccountState_WaitingForBrowserWindowToBeLoaded");
		public static readonly AccountStates AccountState_WaitingForBrowserWindowToBeMadeVisible= new AccountStates(4,"AccountState_WaitingForBrowserWindowToBeMadeVisible");
		public static readonly AccountStates AccountState_WaitingForBrowserWindowToClose= new AccountStates(5,"AccountState_WaitingForBrowserWindowToClose");
		public static readonly AccountStates AccountState_Ready= new AccountStates(6,"AccountState_Ready");
		public static readonly AccountStates AccountState_ShuttingDown= new AccountStates(7,"AccountState_ShuttingDown");
		public static readonly AccountStates AccountState_Shutdown= new AccountStates(8,"AccountState_Shutdown");


		public AccountStates(int key, string value)
			: base(key, value)
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

