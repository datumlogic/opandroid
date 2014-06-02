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
		WebLoginViewController GetLoginWebViewForIdentityCreate(HOPIdentity identity, bool create)
		{
			WebLoginViewController ret = null;
			OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity - Get login web view for identity objectId:%d", identity, identity.GetObjectId().IntValue());
			ret = this.LoginWebViewsDictionary.ObjectForKey(identity.GetObjectId());
			if (create && !ret)
			{
				//ret = [[LoginManager sharedLoginManager] preloadedWebLoginViewController];
				//if (!ret)

				{
					ret = new WebLoginViewController(identity);
					OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity - Created web view: %p \nidentity uri: %@ \nidentity object id:%d", identity, ret, identity.GetIdentityURI(), identity.GetObjectId().IntValue());
				}
				ret.View.Hidden = true;
				ret.CoreObject = identity;
				this.LoginWebViewsDictionary.SetObjectForKey(ret, identity.GetObjectId());
				//[[LoginManager sharedLoginManager] setPreloadedWebLoginViewController:nil];
			}
			else
			{
				if (ret)
				{
					OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity - Retrieved exisitng web view:%p for identity objectId:%d", identity, ret, identity.GetObjectId().IntValue());
				}
				else OPLog(HOPLoggerSeverityWarning, HOPLoggerLevelTrace, "<%p> Identity - getLoginWebViewForIdentity - NO VALID WEB VIEW:%p - %d", identity, ret, identity.GetObjectId().IntValue());

			}

			return ret;
		}
		//
		void RemoveLoginWebViewForIdentity(HOPIdentity identity)
		{
			this.LoginWebViewsDictionary.RemoveObjectForKey(identity.GetObjectId());
		}
		//
		void IdentityStateChanged(HOPIdentity identity, HOPIdentityStates state)
		{
			OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity login state has changed to: %@ - identityURI: %@", identity, HOPIdentity.StringForIdentityState(state), identity.GetIdentityURI());
			//Prevent to have two web views visible at the time
			if (state == HOPIdentityStateWaitingForBrowserWindowToBeMadeVisible)
			{
				OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity tries to obtain web view visibility mutex. identityURI: %@ identityObjectId: %d", identity, identity.GetIdentityURI(), identity.GetObjectId().IntegerValue());
				lock(mutexVisibleWebView);
				this.IdentityMutexOwner = identity;
				OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity owns web view visibility mutex. identityURI: %@ identityObjectId: %d", identity, identity.GetIdentityURI(), identity.GetObjectId().IntegerValue());
			}
			ThreadPool.QueueUserWorkItem( delegate 
				{
					WebLoginViewController webLoginViewController = null;
					switch (state)
					{
					case HOPIdentityStates.HOPIdentityStatePending :
						break;
					case HOPIdentityStates.HOPIdentityStatePendingAssociation :
						break;
					case HOPIdentityStates.HOPIdentityStateWaitingAttachmentOfDelegate :
						{
							LoginManager.SharedLoginManager().AttachDelegateForIdentityForceAttach(identity, false);
						}
						break;
					case HOPIdentityStates.HOPIdentityStateWaitingForBrowserWindowToBeLoaded :
						{
							webLoginViewController = this.GetLoginWebViewForIdentityCreate(identity, true);
							if (LoginManager.SharedLoginManager().IsLogin() || LoginManager.SharedLoginManager().IsAssociation())
							{
								this.LoginDelegate.OnOpeningLoginPage();
							}
							if (LoginManager.SharedLoginManager().PreloadedWebLoginViewController() != webLoginViewController)
							{
								//Open identity login web page
								webLoginViewController.OpenLoginUrl(Settings.SharedSettings().GetOuterFrameURL());
							}
						}
						break;
					case HOPIdentityStates.HOPIdentityStateWaitingForBrowserWindowToBeMadeVisible :
						{
							webLoginViewController = this.GetLoginWebViewForIdentityCreate(identity, false);
							this.LoginDelegate.OnLoginWebViewVisible(webLoginViewController);
							//Notify core that identity login web view is visible now
							identity.NotifyBrowserWindowVisible();
						}
						break;
					case HOPIdentityStates.HOPIdentityStateWaitingForBrowserWindowToClose :
						{
							webLoginViewController = this.GetLoginWebViewForIdentityCreate(identity, false);
							this.LoginDelegate.OnIdentityLoginWebViewCloseForIdentityURI(webLoginViewController, identity.GetIdentityURI());
							//Notify core that identity login web view is closed
							identity.NotifyBrowserWindowClosed();
							if (this.IdentityMutexOwner.GetObjectId().IntValue() == identity.GetObjectId().IntValue())
							{
								this.IdentityMutexOwner = null;
								OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity releases web view visibility mutex. identityURI: %@", identity, identity.GetIdentityURI());
								pthread_mutex_unlock(mutexVisibleWebView);
							}
							this.RemoveLoginWebViewForIdentity(identity);
						}
						break;
					case HOPIdentityStates.HOPIdentityStateReady :
						this.LoginDelegate.OnIdentityLoginFinished();
						#if APNS_ENABLED
						APNSInboxManager.SharedAPNSInboxManager().HandleNewMessages();
						#endif
						if (LoginManager.SharedLoginManager().IsLogin() || LoginManager.SharedLoginManager().IsAssociation()) LoginManager.SharedLoginManager().OnIdentityAssociationFinished(identity);
						break;
					case HOPIdentityStates.HOPIdentityStateShutdown :
						{
							HOPIdentityStates identityState = identity.GetState();
							if (identityState.LastErrorCode) this.LoginDelegate.OnIdentityLoginError(identityState.LastErrorReason);
							identity.DestroyCoreObject();
							this.LoginDelegate.OnIdentityLoginShutdown();
						}
						break;
					default :
						break;
					}
				});
		}
		//
		void OnIdentityPendingMessageForInnerBrowserWindowFrame(HOPIdentity identity)
		{
			OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity: pending message for inner browser window frame.", identity);
			ThreadPool.QueueUserWorkItem( delegate 
				{
					//Get login web view for specified identity
					WebLoginViewController webLoginViewController = this.GetLoginWebViewForIdentityCreate(identity, false);
					if (webLoginViewController)
					{
						string jsMethod =String.Format("sendBundleToJS({0})",identity.GetNextMessageForInnerBrowerWindowFrame()) ;
						//Pass JSON message to java script
						webLoginViewController.PassMessageToJS(jsMethod);
					}

				});
		}
		//
		void OnIdentityRolodexContactsDownloaded(HOPIdentity identity)
		{
			OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity rolodex contacts are downloaded.", identity);
			//Remove activity indicator
			ActivityIndicatorViewController.SharedActivityIndicator().ShowActivityIndicatorWithTextInView(false, null, null);
			if (identity)
			{
				HOPHomeUser homeUser = HOPModelManager.SharedModelManager().GetLastLoggedInHomeUser();
				HOPAssociatedIdentity associatedIdentity = HOPModelManager.SharedModelManager().GetAssociatedIdentityBaseIdentityURIHomeUserStableId(identity.GetBaseIdentityURI(), homeUser.StableId);
				bool flushAllRolodexContacts;
				string downloadedVersion;
				ArrayList rolodexContacts;
				//Get downloaded rolodex contacts
				bool rolodexContactsObtained = identity.GetDownloadedRolodexContactsOutVersionDownloadedOutRolodexContacts(flushAllRolodexContacts, downloadedVersion, rolodexContacts);
				OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Identity URI: %@ - Total number of rolodex contacts: %d", identity.GetIdentityURI(), rolodexContacts.Count);
				if (downloadedVersion.Length() > 0) associatedIdentity.DownloadedVersion = downloadedVersion;

				//Stop timer that is started when flushAllRolodexContacts is received
				identity.StopTimerForContactsDeletion();
				if (rolodexContactsObtained)
				{
					//Unmark all received contacts, that were earlier set for deletion 
					rolodexContacts.SetValueForKey(NSNumber.NumberWithBool(false), "readyForDeletion");
					ContactsManager.SharedContactsManager().IdentityLookupForContactsIdentityServiceDomain(rolodexContacts, identity.GetIdentityProviderDomain());
					//Check if there are more contacts marked for deletion
					ArrayList contactsToDelete = HOPModelManager.SharedModelManager().GetAllRolodexContactsMarkedForDeletionForHomeUserIdentityURI(identity.GetIdentityURI());
					//If there is more contacts for deletion start timer again. If update for marked contacts is not received before timer expire, delete all marked contacts
					if (contactsToDelete.Count > 0) identity.StartTimerForContactsDeletion();

					OpenPeer.SharedOpenPeer().MainViewController().ContactsTableViewController().OnContactsLoaded();
				}
				else if (flushAllRolodexContacts)
				{
					//Get all rolodex contacts that are alredy in the database
					ArrayList allUserRolodexContacts = HOPModelManager.SharedModelManager().GetRolodexContactsForHomeUserIdentityURIOpenPeerContacts(identity.GetIdentityURI(), false);
					identity.StartTimerForContactsDeletion();
					allUserRolodexContacts.SetValueForKey(NSNumber.NumberWithBool(true), "readyForDeletion");
					//[[HOPModelManager sharedModelManager] saveContext];
				}

				HOPModelManager.SharedModelManager().SaveContext();
				ContactsManager.SharedContactsManager().SetOfIdentitiesWhoseContactsDownloadInProgress().RemoveObject(identity.GetIdentityURI());
			}

		}
		//
		void OnNewIdentity(HOPIdentity identity)
		{
			OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "<%p> Identity: Handling a new identity with the uri:%@", identity, identity.GetIdentityURI());
			LoginManager.SharedLoginManager().AttachDelegateForIdentityForceAttach(identity, true);
		}

		void RemoveAllWebViewControllers()
		{
			this.LoginWebViewsDictionary.RemoveAllObjects();
		}
	}
}

