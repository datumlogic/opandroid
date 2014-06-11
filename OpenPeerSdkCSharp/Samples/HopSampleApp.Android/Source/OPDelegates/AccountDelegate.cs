/*
 
 Copyright (c) 2012, Hookflash Inc.
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
	class AccountDelegate
	{
		WebLoginViewController _webLoginViewController;
		public WebLoginViewController WebLoginViewController
		{
			get
			{
				if (_webLoginViewController != null) {
					// _webLoginViewController = new WebLoginViewController(HOPAccount.sharedAccount());
					if (_webLoginViewController !=null) {
						//_webLoginViewController.view.hidden = true;

					}
				}
				return _webLoginViewController;
			}

			set
			{
				_webLoginViewController = value;
			}
		}

		/*

          // Custom getter for webLoginViewController

		//This method handles account state changes from SDK.
		void accountStateChanged(HOPAccount account, HOPAccountStates accountState)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "Account login state: %@", HOPAccount.stringForAccountState(accountState));
			ThreadPool.QueueUserWorkItem( delegate 
				{
					switch (accountState)
					{
					case HOPAccountStates.HOPAccountStatePending:
						break;
					case HOPAccountStates.HOPAccountPendingPeerFilesGeneration:
						break;
					case HOPAccountStates.HOPAccountWaitingForAssociationToIdentity:
						//after creation
						break;
					case HOPAccountStates.HOPAccountWaitingForBrowserWindowToBeLoaded:
						this.webLoginViewController.openLoginUrl(Settings.sharedSettings().getNamespaceGrantServiceURL());
						break;
					case HOPAccountStates.HOPAccountWaitingForBrowserWindowToBeMadeVisible:

						{
							//Add login web view like main view subview
							if (!this.webLoginViewController.view.superview)
							{
								this.webLoginViewController.view.setFrame(OpenPeer.sharedOpenPeer().mainViewController().view.bounds);
								OpenPeer.sharedOpenPeer().mainViewController().showWebLoginView(this.webLoginViewController);
							}

							this.webLoginViewController.view.alpha = 0;
							this.webLoginViewController.view.hidden = false;
							UIView.animateWithDurationAnimations(0.7, delegate()
								{
									this.webLoginViewController.view.alpha = 1;
								});
							//Notify core that login web view is visible now
							account.notifyBrowserWindowVisible();
						}
						break;
					case HOPAccountStates.HOPAccountWaitingForBrowserWindowToClose:

						{
							OpenPeer.sharedOpenPeer().mainViewController().closeWebLoginView(this.webLoginViewController);
							//Notify core that login web view is closed
							account.notifyBrowserWindowClosed();
						}
						break;
					case HOPAccountStates.HOPAccountStateReady:
						LoginManager.sharedLoginManager().onUserLoggedIn();

						#if APNS_ENABLED
						APNSInboxManager.sharedAPNSInboxManager().handleNewMessages();
						#endif

						break;
					case HOPAccountStates.HOPAccountStateShuttingDown:
						break;
					case HOPAccountStates.HOPAccountStateShutdown:

						{
							HOPAccountState accountState = account.getState();
							if (accountState.errorCode && !OpenPeer.sharedOpenPeer().appEnteredForeground())
							{
								OpenPeer.sharedOpenPeer().mainViewController().onAccountLoginError(accountState.errorReason);
								HOPCache.sharedCache().removeCookieWithNamePath(settingsKeySettingsDownloadURL);
							}
							else
							{
								LoginManager.sharedLoginManager().onUserLogOut();
							}

							this.webLoginViewController = null;
						}
						break;
					default :
						break;
					}

				});
		}


		void onAccountAssociatedIdentitiesChanged(HOPAccount account)
		{
			// OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Account associated identities has changed.");
			ThreadPool.QueueUserWorkItem( delegate 
				{
					ArrayList associatedIdentities = account.getAssociatedIdentities();
					foreach (HOPIdentity identity in associatedIdentities)
					{
						LoginManager.sharedLoginManager().attachDelegateForIdentityForceAttach(identity, false);
					}
				});
		}


		void onAccountPendingMessageForInnerBrowserWindowFrame(HOPAccount account)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Account: pending message for inner browser window frame.");
			ThreadPool.QueueUserWorkItem( delegate 
				{
					WebLoginViewController webLoginViewController = this.webLoginViewController();
					if (webLoginViewController)
					{
						String jsMethod =String.Format("sendBundleToJS({0})",account.getNextMessageForInnerBrowerWindowFrame());
						webLoginViewController.passMessageToJS(jsMethod);
					}
				});
		}*/
	}
}

