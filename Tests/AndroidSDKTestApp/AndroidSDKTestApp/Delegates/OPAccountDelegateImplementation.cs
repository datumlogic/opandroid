
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
			switch (state.NumericType) 
			{
			case 2://WaitingForAssociationToIdentity
				LoginManager.loadOuterFrame();
				break;
			case 3://WaitingForBrowserWindowToBeLoaded
				LoginManager.startAccountLogin ();
				break;
			case 4://WaitingForBrowserWindowToBeMadeVisible
				LoginManager.mAccount.NotifyBrowserWindowVisible();
				break;
			case 5://WaitingForBrowserWindowToClose
				LoginManager.mAccount.NotifyBrowserWindowClosed();
				break;
			case 6://AccountStateReady
				Log.Warn ("JNI", "READY !!!!!!!!!!!!");
				IList<OPIdentity> identityList = account.AssociatedIdentities;
				Log.Warn ("JNI", identityList.ToString());

				LoginManager.loadOuterFrame();
				break;
			

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

