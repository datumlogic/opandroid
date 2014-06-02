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

namespace HopSampleApp
{
	//Singletron class just for translation
	class LoginManager
	{
		public Dictionary<string,object> AssociatingIdentitiesDictionary { get; set; }
		public WebLoginViewController PreloadedWebLoginViewController {get; set;}
		public bool IsLogin { get; set; }
		public bool IsAssociation {get; set;}


		private static LoginManager instance;
		private LoginManager()
		{ 
			this.IsLogin = false;
			this.IsAssociation = false;
			this.AssociatingIdentitiesDictionary = new Dictionary<string,object>();

		}
		public static LoginManager SharedLoginManager()
		{
			if (instance == null)
				instance = new LoginManager();
			return instance;
		}
		//
		public static TValue ObjectForKey<TKey,TValue>(this Dictionary<TKey,TValue> dictionary, TKey key)
		{
			TValue val = default(TValue);
			dictionary.TryGetValue(key,out val);
			return val;
		}
		//
		void login()
		{
			//If peer file doesn't exists, show login view, otherwise start relogin
			if (!HOPModelManager.sharedModelManager().getLastLoggedInHomeUser())
			{
				this.startLogin();
			}
			else
			{
				this.startRelogin();
			}

		}
		//
		void clearIdentities()
		{
			ArrayList associatedIdentities = HOPAccount.sharedAccount().getAssociatedIdentities();
			foreach (HOPIdentity identity in associatedIdentities)
			{
				identity.cancel();
			}
			foreach (HOPIdentity identity in this.AssociatingIdentitiesDictionary)
			{
				identity.cancel();
			}
			this.AssociatingIdentitiesDictionary.Clear ();

		}

		/**
        
           Logout from the current account.
        
           */
		void logout()
		{
			/*
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Logout started");
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Remove cookies");
			//Delete all cookies.
			Utility.RemoveCookiesAndClearCredentials ();
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Remove identity web view controllers");
			OpenPeer.sharedOpenPeer().identityDelegate().removeAllWebViewControllers();//need implementacion
			OpenPeer.sharedOpenPeer().backgroundingDelegate().backgroundingSubscription().cancel();//need implementacion
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Clear all session objects");
			SessionManager.sharedSessionManager().clearAllSessions();
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Clear all identity objects");
			this.clearIdentities();
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Start account shutdown");
			//Call to the SDK in order to shutdown Open Peer engine.
			HOPAccount.sharedAccount().shutdown();
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Handle logout on UI level");
			OpenPeer.sharedOpenPeer().mainViewController().onLogout();//need implemntacion
			HOPHomeUser homeUser = HOPModelManager.sharedModelManager().getLastLoggedInHomeUser();
			homeUser.loggedIn = NSNumber.numberWithBool(false);
			HOPModelManager.sharedModelManager().saveContext();
			this.isLogin = true;
			if (Settings.sharedSettings().isQRSettingsResetEnabled())
			{
				//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Removing QR settings");
				Settings.sharedSettings().removeAppliedQRSettings();
			}

			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Clear session records from the database");
			HOPModelManager.sharedModelManager().clearSessionRecords();
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelInsane, "Release all core objects");
			HOPStack.sharedStack().doLogoutCleanup();
			*/
		}
		//
		/**
        
           Logout from the current account.
        
           */
		void startAccount()
		{
			//HOPAccount.sharedAccount().loginWithAccountDelegateConversationThreadDelegateCallDelegateNamespaceGrantOuterFrameURLUponReloadGrantIDLockboxServiceDomainForceCreateNewLockboxAccount((HOPAccountDelegate)OpenPeer.sharedOpenPeer().accountDelegate(), (HOPConversationThreadDelegate)OpenPeer.sharedOpenPeer().conversationThreadDelegate(), (HOPCallDelegate)OpenPeer.sharedOpenPeer().callDelegate(), Settings.sharedSettings().getOuterFrameURL(), UUIDManager.sharedUUIDManager().getUUID(), Settings.sharedSettings().getIdentityProviderDomain(), false);
		}

		void startLogin()
		{
			this.startLoginUsingIdentityURI(Settings.sharedSettings().getIdentityFederateBaseURI());
			this.IsLogin = true;
		}

		/**
        
           Starts user login for specific identity URI. Activity indicator is displayed and identity login started.
        
           @param identityURI NSString identity uri (e.g. identity://facebook.com/)
        
           */

		public static void startLoginUsingIdentityURI(string identityURI)
		{

			if (!this.AssociatingIdentitiesDictionary.TryGetValue(identityURI))
			{
				//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "Identity login started for uri: %@", identityURI);
				OpenPeer.sharedOpenPeer().mainViewController().onStartLoginWithidentityURI();
				string redirectAfterLoginCompleteURL = NSString.stringWithFormat("%@?reload=true", Settings.sharedSettings().getOuterFrameURL());
				if (!HOPAccount.sharedAccount().isCoreAccountCreated() || HOPAccount.sharedAccount().getState().state == HOPAccountStateShutdown) this.startAccount();

				//For identity login it is required to pass identity delegate, URL that will be requested upon successful login, identity URI and identity provider domain. This is 
				HOPIdentity hopIdentity = HOPIdentity.loginWithDelegateIdentityProviderDomainIdentityURIOridentityBaseURIOuterFrameURLUponReload((HOPIdentityDelegate)OpenPeer.sharedOpenPeer().identityDelegate(), Settings.sharedSettings().getIdentityProviderDomain(), identityURI, redirectAfterLoginCompleteURL);
				if (!hopIdentity)
				{
					OPLog(HOPLoggerSeverityError, HOPLoggerLevelTrace, "Identity login has failed for uri: %@", identityURI);
				}
				else
				{
					this.AssociatingIdentitiesDictionary.setObjectForKey(hopIdentity, identityURI);
				}

			}

		}
		//
		/**
        
           Initiates relogin procedure.
        
           */
		void startRelogin()
		{
			bool reloginStarted = false;
			OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "Relogin started");
			OpenPeer.sharedOpenPeer().mainViewController().onRelogin();
			HOPHomeUser homeUser = HOPModelManager.sharedModelManager().getLastLoggedInHomeUser();
			if (homeUser && homeUser.reloginInfo.length() > 0)
			{
				//To start relogin procedure it is required to pass account, conversation thread and call delegates. Also, private peer file and secret, received on previous login procedure, are required.
				reloginStarted = HOPAccount.sharedAccount().reloginWithAccountDelegateConversationThreadDelegateCallDelegateLockboxOuterFrameURLUponReloadReloginInformation((HOPAccountDelegate)OpenPeer.sharedOpenPeer().accountDelegate(), (HOPConversationThreadDelegate)OpenPeer.sharedOpenPeer().conversationThreadDelegate(), (HOPCallDelegate)OpenPeer.sharedOpenPeer().callDelegate(), Settings.sharedSettings().getOuterFrameURL(), homeUser.reloginInfo);
			}

			if (!reloginStarted) OPLog(HOPLoggerSeverityError, HOPLoggerLevelDebug, "Relogin has failed");

		}

		void preloadLoginWebPage()
		{
			if (!this.preloadedWebLoginViewController)
			{
				this.preloadedWebLoginViewController = new WebLoginViewController();
				if (this.preloadedWebLoginViewController) this.preloadedWebLoginViewController.view.hidden = true;

			}

			this.preloadedWebLoginViewController.openLoginUrl(Settings.sharedSettings().getOuterFrameURL());
		}

		/**
        
           Handles successful identity association. It updates list of associated identities on server side.
        
           @param identity HOPIdentity identity used for login
        
           */
		void onIdentityAssociationFinished(HOPIdentity identity)
		{
			string relogininfo = HOPAccount.sharedAccount().getReloginInformation();
			if (relogininfo.length() > 0)
			{
				//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "Identity association finished - identityURI: %@  - accountStableId: %@", identity.getIdentityURI(), HOPAccount.sharedAccount().getStableID());
				HOPHomeUser homeUser = HOPModelManager.sharedModelManager().getHomeUserByStableID(HOPAccount.sharedAccount().getStableID());
				if (!homeUser)
				{
					homeUser = (HOPHomeUser)HOPModelManager.sharedModelManager().createObjectForEntity("HOPHomeUser");
					homeUser.stableId = HOPAccount.sharedAccount().getStableID();
					homeUser.reloginInfo = HOPAccount.sharedAccount().getReloginInformation();
					homeUser.loggedIn = NSNumber.numberWithBool(true);
				}

				HOPAssociatedIdentity associatedIdentity = HOPModelManager.sharedModelManager().getAssociatedIdentityBaseIdentityURIHomeUserStableId(identity.getBaseIdentityURI(), homeUser.stableId);
				if (!associatedIdentity) associatedIdentity = (HOPAssociatedIdentity)HOPModelManager.sharedModelManager().createObjectForEntity("HOPAssociatedIdentity");

				HOPIdentityContact homeIdentityContact = identity.getSelfIdentityContact();
				associatedIdentity.domain = identity.getIdentityProviderDomain();
				//associatedIdentity.downloadedVersion = @"";
				associatedIdentity.name = identity.getBaseIdentityURI();
				associatedIdentity.baseIdentityURI = identity.getBaseIdentityURI();
				associatedIdentity.homeUserProfile = homeIdentityContact.rolodexContact;
				associatedIdentity.homeUser = homeUser;
				homeIdentityContact.rolodexContact.associatedIdentityForHomeUser = associatedIdentity;
				HOPModelManager.sharedModelManager().saveContext();
				//[self.associatingIdentitiesDictionary removeObjectForKey:[identity getBaseIdentityURI]];
				this.AssociatingIdentitiesDictionary.Clear ();
			}

			this.onUserLoggedIn();
		}
		//
		void attachDelegateForIdentityForceAttach(HOPIdentity identity, bool forceAttach)
		{
			//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "Attach delegate for identity with URI: %@", identity.getIdentityURI());
			if (!identity.isDelegateAttached() || forceAttach)
			{
				//OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "Attaching delegate for identity with URI: %@", identity.getIdentityURI());
				//Create core data record if it is not already in the db    
				this.onIdentityAssociationFinished(identity);
				string redirectAfterLoginCompleteURL = NSString.stringWithFormat("%@?reload=true", Settings.sharedSettings().getOuterFrameURL());
				identity.attachDelegateRedirectionURL((HOPIdentityDelegate)OpenPeer.sharedOpenPeer().identityDelegate(), redirectAfterLoginCompleteURL);
			}

		}
		//
		/**
        
           Handles SDK event after login is successful.
        
           */
        void onUserLoggedIn()
        {
			// OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "onUserLoggedIn");
            //Wait till identity association is not completed
            if (HOPAccount.sharedAccount().getState().state == HOPAccountStateReady && this.associatingIdentitiesDictionary.count() == 0)
            {
				// OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "User is successfully logged in.");
                if (!OpenPeer.sharedOpenPeer().appEnteredForeground())
                {
                    ArrayList associatedIdentites = HOPAccount.sharedAccount().getAssociatedIdentities();
                    foreach (HOPIdentity identity in associatedIdentites)
                    {
                        if (!identity.isDelegateAttached())
                        {
                            string redirectAfterLoginCompleteURL = NSString.stringWithFormat("%@?reload=true", Settings.sharedSettings().getOuterFrameURL());
                            identity.attachDelegateRedirectionURL((HOPIdentityDelegate)OpenPeer.sharedOpenPeer().identityDelegate(), redirectAfterLoginCompleteURL);
                        }

                    }
                    //Check if it is logged in a new user
                    HOPHomeUser previousLoggedInHomeUser = HOPModelManager.sharedModelManager().getLastLoggedInHomeUser();
                    HOPHomeUser homeUser = HOPModelManager.sharedModelManager().getHomeUserByStableID(HOPAccount.sharedAccount().getStableID());
                    if (homeUser)
                    {
                        //If is previous logged in user is different update loggedIn flag
                        if (!homeUser.loggedIn.boolValue())
                        {
                            if (previousLoggedInHomeUser) previousLoggedInHomeUser.loggedIn = false;

                            homeUser.loggedIn = NSNumber.numberWithBool(true);
                            HOPModelManager.sharedModelManager().saveContext();
                        }

                    }

                    //Not yet ready for association
                    /*if ((self.isLogin || self.isAssociation) && ([associatedIdentites count] < 2))
                    
                       {
                    
                       self.isLogin = NO;
                    
                       
                    
                       HOPIdentity* identity = [associatedIdentites objectAtIndex:0];
                    
                       
                    
                       NSString* message = @"Do you want to associate federated account?";
                    
                       
                    
                       if ([[identity getBaseIdentityURI] isEqualToString:identityFacebookBaseURI])
                    
                       message = @"Do you want to associate federated account?";
                    
                       else
                    
                       message = @"Do you want to associate facebook account?";
                    
                       
                    
                       UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Identity association" message:message delegate:self cancelButtonTitle:@"NO" otherButtonTitles:@"YES", nil];
                    
                       
                    
                       [alert show];
                    
                       }
                    
                       else*/

                    {
                        OpenPeer.sharedOpenPeer().mainViewController().onLoginFinished();
                        //Start loading contacts.
                        ContactsManager.sharedContactsManager().loadContacts();
                    }
                }
                else
                {
                    SessionManager.sharedSessionManager().recreateExistingSessions();
                }

                #ifdef APNS_ENABLED
                    APNSInboxManager.sharedAPNSInboxManager().getAllMessages();
                #endif

                //Login finished. Remove activity indicator
                ActivityIndicatorViewController.sharedActivityIndicator().showActivityIndicatorWithTextInView(false, null, null);
            }
            else
            {
				int o = this.AssociatingIdentitiesDictionary.Count();
                if (o > 0)
                {
					// OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "onUserLoggedIn - NOT Ready because of associatingIdentitiesDictionary is not empty: %d", o);
                }
                else
                {
					// OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelDebug, "onUserLoggedIn - NOT Ready because account is not in ready state");
                }

            }

        }

        void onUserLogOut()
        {
			// OPLog(HOPLoggerSeverityInformational, HOPLoggerLevelTrace, "Logout finished");
            OpenPeer.sharedOpenPeer().finishPreSetup();
        }

        /**
        
           Retrieves info if an identity with specified URI is associated or not.
        
           @param inBaseIdentityURI NSString base identity URI
        
           @return YES if associated, otherwise NO
        
           */
        bool isAssociatedIdentity(string inBaseIdentityURI)
        {
            bool ret = false;
            HOPHomeUser homeUser = HOPModelManager.sharedModelManager().getLastLoggedInHomeUser();
            if (homeUser)
            {
                HOPAssociatedIdentity associatedIdentity = HOPModelManager.sharedModelManager().getAssociatedIdentityBaseIdentityURIHomeUserStableId(inBaseIdentityURI, homeUser.stableId);
                if (associatedIdentity) ret = true;

            }

            return ret;
        }

        bool isUserFullyLoggedIn()
        {
            bool ret = false;
            ret = HOPAccount.sharedAccount().getState().state == HOPAccountStateReady;
            if (ret)
            {
                ArrayList identities = HOPAccount.sharedAccount().getAssociatedIdentities();
                foreach (HOPIdentity identity in identities)
                {
                    if (identity.getState().state != HOPIdentityStateReady)
                    {
                        ret = false;
                        break;
                    }

                }
            }

            return ret;
        }

       
        void alertViewClickedButtonAtIndex(UIAlertView alertView, int buttonIndex)
        {
            if (buttonIndex != alertView.cancelButtonIndex)
            {
                ArrayList associatedIdentites = HOPAccount.sharedAccount().getAssociatedIdentities();
                HOPIdentity identity = associatedIdentites.objectAtIndex(0);
                if (identity.getBaseIdentityURI().isEqualToString(identityFacebookBaseURI))
                {
					LoginManager.sharedLoginManager().startLoginUsingIdentityURI(Settings.sharedSettings().getIdentityFederateBaseURI());
                }
                else
                {
                    LoginManager.sharedLoginManager().startLoginUsingIdentityURI(identityFacebookBaseURI);
                }

                this.isAssociation = true;
            }
            else
            {
                OpenPeer.sharedOpenPeer().mainViewController().onLoginFinished();
                //Start loading contacts.
                ContactsManager.sharedContactsManager().loadContacts();
            }

        }

	

	}
}

