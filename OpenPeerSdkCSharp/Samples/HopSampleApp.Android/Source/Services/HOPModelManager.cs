
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp
{
	class HOPModelManager
	{
		private static HOPModelManager instance;
		private HOPModelManager()
		{

		}
		public static HOPModelManager sharedModelManager()
		{
			if (instance == null)
				instance = new HOPModelManager();
			return instance;
		}

		//
		void setDataPathBackupData(string path, bool inBackupData){}
		void setCachePath(string path){}
		//NSManagedObjectContext managedObjectContext(){}
		//NSManagedObjectContext backgroundManagedObjectContext(){}
		//NSManagedObjectModel managedObjectModel(){}
		//NSPersistentStoreCoordinator persistentStoreCoordinator(){}
		//NSURL applicationDocumentsDirectory(){}
		public void saveContext(){}
		//void deleteObject(NSManagedObject managedObjectToDelete){);}

		//NSManagedObject createObjectForEntity(string entityName){}


		public ArrayList getResultsForEntityWithPredicateStringOrderDescriptors(string entityName, string predicateString, ArrayList orderDescriptors)
		{
			ArrayList ret = null;
			return ret;
		}

		/*
		HOPRolodexContact getRolodexContactByIdentityURI(string identityURI)
		{
			//HOPRolodexContact ret = null;
			ArrayList results = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPRolodexContact", String.Format("(identityURI MATCHES '{0}')", identityURI), null);
			if (results.Count > 0)
			{
				ret = results[0];
			}

			return ret;
		}*/

		public ArrayList getRolodexContactsByPeerURI(string peerURI)
		{
			ArrayList ret = null;
			if (peerURI.Length > 0)
			{
				//NSSortDescriptor sortDescriptor = new NSSortDescriptor("identityContact.priority", false);
				//ret = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPRolodexContact", NSString.stringWithFormat("identityContact.peerFile.peerURI MATCHES '%@'", peerURI), @[sortDescriptor]);
			}

			return ret;
		}

		public ArrayList getAllRolodexContactForHomeUserIdentityURI(string homeUserIdentityURI)
		{
			ArrayList ret = null;
			ArrayList results = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPAssociatedIdentity", String.Format("(homeUserProfile.identityURI MATCHES '{0}')", homeUserIdentityURI), null);
			if (results.Count > 0)
			{
				//HOPAssociatedIdentity associatedIdentity = results.IndexOf(0);
				//ret = associatedIdentity.RolodexContacts.All();
			}



			return ret;
		}

		public ArrayList getRolodexContactsForHomeUserIdentityURIOpenPeerContacts(string homeUserIdentityURI, bool openPeerContacts)
		{
			ArrayList ret = null;
			string stringFormat = null;
			if (openPeerContacts)
			{
				stringFormat = String.Format("(identityContact != nil || identityContact.@count > 0 && associatedIdentity.homeUserProfile.identityURI MATCHES '{0}')", homeUserIdentityURI);
			}
			else
			{
				stringFormat = String.Format("(identityContact == nil || identityContact.@count == 0 && associatedIdentity.homeUserProfile.identityURI MATCHES '{0}')", homeUserIdentityURI);
			}

			ret = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPRolodexContact", stringFormat, null);
			return ret;
		}

		public HOPIdentityContract getIdentityContactByStableIDIdentityURI(string stableID, string identityURI)
		{
			HOPIdentityContract ret = null;
			ArrayList results = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPIdentityContact", String.Format("(stableID MATCHES '{0}' AND rolodexContact.identityURI MATCHES '{1}')", stableID, identityURI), null);
			if (results.Count > 0)
			{
				//ret = results[0];
			}

			return ret;
		}

		public ArrayList getIdentityContactsByStableID(string stableID)
		{
			ArrayList ret = null;
			if (stableID.Length > 0)
			{
				//        NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"identityContact.priority" ascending:NO];
				//        ret = [self getResultsForEntity:@"HOPIdentityContact" withPredicateString:[NSString stringWithFormat:@"stableID MATCHES '%@'",stableID] orderDescriptors:@[sortDescriptor]];
				ret = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPIdentityContact", String.Format("stableID MATCHES '{0}'", stableID), null);
			}

			return ret;
		}

		/*
		HOPPublicPeerFile getPublicPeerFileForPeerURI(string peerURI)
		{
			HOPPublicPeerFile ret = null;
			ArrayList results = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPPublicPeerFile", String.Format("(peerURI MATCHES '{0}')", peerURI), null);
			if (results.Count > 0)
			{
				ret = results[0];
			}

			return ret;
		}*/

		public HOPAssociatedIdentity getAssociatedIdentityByDomainIdentityNameHomeUserIdentityURI(string identityProviderDomain, string identityName, string homeUserIdentityURI)
		{
			HOPAssociatedIdentity ret = null;
			ArrayList results = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPAssociatedIdentity", String.Format("(domain MATCHES '{0}' AND baseIdentityURI MATCHES '{1}' AND homeUserProfile.identityURI MATCHES '{2}')", identityProviderDomain, identityName, homeUserIdentityURI), null);
			if (results.Count > 0)
			{
				//ret = results[0];
			}

			return ret;
		}

		public HOPAssociatedIdentity getAssociatedIdentityBaseIdentityURIHomeUserStableId(string baseIdentityURI, string homeUserStableId)
		{
			HOPAssociatedIdentity ret = null;
			if (homeUserStableId.Length > 0)
			{
				ArrayList results = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPAssociatedIdentity", String.Format("(baseIdentityURI MATCHES '{0}' AND homeUser.stableId MATCHES '{1}')", baseIdentityURI, homeUserStableId), null);
				if (results.Count > 0)
				{
					//ret = results[0];
				}

			}

			return ret;
		}

		public ArrayList getAllIdentitiesInfoForHomeUserIdentityURI(string identityURI)
		{
			ArrayList ret = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPAssociatedIdentity", String.Format("(homeUserProfile.identityURI MATCHES '{0}')", identityURI), null);
			return ret;
		}

		//HOPAvatar getAvatarByURL(string url){return null;}

		public HOPHomeUser getLastLoggedInHomeUser()
		{
			HOPHomeUser ret = null;
			ArrayList results = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPHomeUser", "(loggedIn == YES)", null);
			if (results.Count > 0)
			{
				//ret = results[0];
			}

			return ret;
		}

		HOPHomeUser getHomeUserByStableID(string stableID)
		{
			HOPHomeUser ret = null;
			ArrayList results = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPHomeUser",String.Format("(stableId MATCHES '{0}')", stableID), null);
			if (results.Count > 0)
			{
				//ret = results[0];
			}

			return ret;
		}

		public void deleteAllMarkedRolodexContactsForHomeUserIdentityURI(string homeUserIdentityURI)
		{

		}

		public ArrayList getAllRolodexContactsMarkedForDeletionForHomeUserIdentityURI(string homeUserIdentityURI)
		{
			ArrayList ret = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPRolodexContact", String.Format("(readyForDeletion == YES AND associatedIdentity.homeUserProfile.identityURI MATCHES '{0}')", homeUserIdentityURI), null);
			return ret;
		}

		public ArrayList getRolodexContactsForRefreshByHomeUserIdentityURILastRefreshTime(string homeUserIdentityURI, DateTime lastRefreshTime)
		{
			ArrayList ret = this.getResultsForEntityWithPredicateStringOrderDescriptors("HOPRolodexContact", String.Format("(associatedIdentity.homeUserProfile.identityURI MATCHES '{0}' AND (ANY associatedIdentity.rolodexContacts.identityContact == nil OR ANY associatedIdentity.rolodexContacts.identityContact.lastUpdated < {1})", homeUserIdentityURI, lastRefreshTime), null);
			return ret;
		}

		public ArrayList getAPNSDataForPeerURI(string peerURI)
		{
			return null;
		}

		public void setAPNSDataPeerURI(string deviceToken, string peerURI)
		{
		
		}

		//NSManagedObject createObjectInBackgroundForEntity(string entityName){return null;}

		void saveBackgroundContext()
		{

		}

		public ArrayList getResultsInBackgroundForEntityWithPredicateStringOrderDescriptors(string entityName, string predicateString, ArrayList orderDescriptors)
		{
			return null;
		}

		//HOPCacheData getCacheDataForPathWithExpireCheck(string path, bool expireCheck){return null;	}

		public void setCookieWithPathExpires(string data, string path, DateTime expires)
		{


		}

		public string getCookieWithPath(string path)
		{
			return null;
		}

		public void removeExpiredCookies()
		{

		}

		public void removeCookieForPath(string path)
		{

		}

		String log(string message)
		{
			return String.Format("HOPModelManager:{0} ", message.ToString());
		}

		//HOPSessionRecord getSessionRecordByID(string sessionID){return null;}

		//HOPSessionRecord getMessageRecordByID(string messageID){return null;}

		//HOPSessionRecord addSessionTypeDateNameParticipants(string sessionID, string type, DateTime date, string name, ArrayList participants){return null;}

		//HOPMessageRecord addMessageTypeDateSessionRolodexContactMessageId(string messageText, string type, DateTime date, string sessionRecordId, HOPRolodexContact rolodexContact, string messageId){return null;}

		void clearSessionRecords()
		{
			this.saveContext();
		}
	}
}

