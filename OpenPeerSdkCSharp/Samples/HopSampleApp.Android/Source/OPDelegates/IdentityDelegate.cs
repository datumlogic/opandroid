/*
Copyright (c) 2014, hookflash Inc.
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FreeBSD Project.
*/
using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using HopSampleApp.Enums;
using System.Threading;
using System.Threading.Tasks;
namespace HopSampleApp
{
	class IdentityDelegate
	{
		protected Mutex mutexVisibleWebView;
		public Dictionary<string,object> LoginWebViewsDictionary {get; set;}
		public HOPIdentity IdentityMutexOwner {get; set;}
		public LoginEventsDelegate LoginDelegate {get; set;}
		//public void removeAllWebViewControllers();

		public IdentityDelegate()
		{
			this.LoginWebViewsDictionary=new Dictionary<string,object>();
			mutexVisibleWebView=new Mutex();
		}

		void RemoveLoginWebViewForIdentity(HOPIdentity identity)
		{
			this.LoginWebViewsDictionary.Remove(identity.getObjectId().ToString());

		}
		//

		public static void OnIdentityRolodexContactsDownloaded(HOPIdentity identity)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity rolodex contacts are downloaded.", identity);
			//Remove activity indicator
			ActivityIndicatorViewController.SharedActivityIndicator().showActivityIndicatorWithTextInView(false, null, null);
			if (Convert.ToBoolean(identity))
			{
				HOPHomeUser homeUser = HOPModelManager.sharedModelManager ().getLastLoggedInHomeUser ();
				HOPAssociatedIdentity associatedIdentity = HOPModelManager.sharedModelManager().getAssociatedIdentityBaseIdentityURIHomeUserStableId(identity.getBaseIdentityURI(), homeUser.StableId);
				bool flushAllRolodexContacts;
				flushAllRolodexContacts = true;
				string downloadedVersion;
				downloadedVersion = string.Empty;

				ArrayList rolodexContacts;
				rolodexContacts = null;
				//Get downloaded rolodex contacts
				bool rolodexContactsObtained = identity.getDownloadedRolodexContactsOutVersionDownloadedOutRolodexContacts (flushAllRolodexContacts, downloadedVersion.ToString(),rolodexContacts);
				//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Identity URI: %@ - Total number of rolodex contacts: %d", identity.GetIdentityURI(), rolodexContacts.Count);
				if (downloadedVersion.Length > 0) associatedIdentity.DownloadedVersion = downloadedVersion;

				//Stop timer that is started when flushAllRolodexContacts is received
				identity.stopTimerForContactsDeletion();
				if (rolodexContactsObtained)
				{
					//Unmark all received contacts, that were earlier set for deletion 
					rolodexContacts.Add(String.Format("{0},{1}",Convert.ToInt32(false), "readyForDeletion"));
					ContactsManager.SharedContactsManager().identityLookupForContactsIdentityServiceDomain(rolodexContacts, identity.getIdentityProviderDomain());
					//Check if there are more contacts marked for deletion
					ArrayList contactsToDelete = HOPModelManager.sharedModelManager().getAllRolodexContactsMarkedForDeletionForHomeUserIdentityURI(identity.getIdentityURI());
					//If there is more contacts for deletion start timer again. If update for marked contacts is not received before timer expire, delete all marked contacts
					if (contactsToDelete.Count > 0) identity.startTimerForContactsDeletion();

					//OpenPeer.SharedOpenPeer().MainViewController().ContactsTableViewController().OnContactsLoaded();
				}
				else if (flushAllRolodexContacts)
				{
					//Get all rolodex contacts that are alredy in the database
					//ArrayList allUserRolodexContacts = HOPModelManager.sharedModelManager().GetRolodexContactsForHomeUserIdentityURIOpenPeerContacts(identity.GetIdentityURI(), false);
					identity.startTimerForContactsDeletion ();
					//allUserRolodexContacts.SetValueForKey(NSNumber.NumberWithBool(true), "readyForDeletion");
					//[[HOPModelManager sharedModelManager] saveContext];
				}

				HOPModelManager.sharedModelManager().saveContext();
				ContactsManager.SharedContactsManager().SetOfIdentitiesWhoseContactsDownloadInProgress.Remove(identity.getIdentityURI());
			}

		}
		//
		public static void OnNewIdentity(HOPIdentity identity)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity: Handling a new identity with the uri:%@", identity, identity.GetIdentityURI());
			//HopSampleApp.LoginManager
			LoginManager.SharedLoginManager().attachDelegateForIdentityForceAttach(identity, true);
		}

		void RemoveAllWebViewControllers()
		{
			this.LoginWebViewsDictionary.Clear();
		}
	}
}

