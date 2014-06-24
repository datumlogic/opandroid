
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


namespace AndroidSDKTestApp
{
	public class OPIdentityDelegateImplementation:OPIdentityDelegate
	{
		public override void OnIdentityStateChanged (OPIdentity account,IdentityStates state)
		{
			if (state == IdentityStates.IdentityStatePendingAssociation) {
				LoginManager.loadOuterFrame ();
			}
			if (state == IdentityStates.IdentityStateWaitingForBrowserWindowToBeMadeVisible)
			{
				LoginManager.mIdentity.NotifyBrowserWindowVisible();
			}
			if (state == IdentityStates.IdentityStateWaitingForBrowserWindowToClose)
			{
				LoginManager.mIdentity.NotifyBrowserWindowClosed();
			}
			if (state == IdentityStates.IdentityStateReady) {
				//LoginManager.mIdentity;
			}
		}



		public override void OnIdentityPendingMessageForInnerBrowserWindowFrame (OPIdentity p0)
		{
			LoginManager.pendingMessageForInnerFrame();
		}
		public override void OnIdentityRolodexContactsDownloaded (OPIdentity identity)
		{
			//LoginManager..onDownloadedRolodexContacts(identity);
			LoginManager.onDownloadedRolodexContacts (identity);
		}

	}
}

