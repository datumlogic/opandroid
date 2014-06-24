
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
using Android.Util;

namespace AndroidSDKTestApp
{
	public class OPAccountDelegateImplementation:OPAccountDelegate
	{
		public override  void OnAccountStateChanged (OPAccount account,AccountStates state)
		{
			if (state == AccountStates.AccountStateWaitingForAssociationToIdentity)
			{
				LoginManager.loadOuterFrame();
			}
			if (state == AccountStates.AccountStateWaitingForBrowserWindowToBeLoaded) {
				LoginManager.startAccountLogin ();
			}
			if (state == AccountStates.AccountStateWaitingForBrowserWindowToBeMadeVisible) {
				LoginManager.mAccount.NotifyBrowserWindowVisible();
			}
			if (state == AccountStates.AccountStateWaitingForBrowserWindowToClose) {
				LoginManager.mAccount.NotifyBrowserWindowClosed();
			}
			if (state == AccountStates.AccountStateReady) {
				Log.Warn ("JNI", "READY !!!!!!!!!!!!");
				IList<OPIdentity> identityList = account.AssociatedIdentities;
				Log.Warn ("JNI", identityList.ToString());

				LoginManager.loadOuterFrame();
			}

		}
		public override void OnAccountAssociatedIdentitiesChanged (OPAccount p0)
		{
			//throw new NotImplementedException ();
		}
		public override  void OnAccountPendingMessageForInnerBrowserWindowFrame (OPAccount p0)
		{
			LoginManager.pendingMessageForNamespaceGrantInnerFrame();
		}
	}
}

