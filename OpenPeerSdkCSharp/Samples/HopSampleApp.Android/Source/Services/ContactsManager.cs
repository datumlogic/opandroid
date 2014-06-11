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
	 // Singleton Pattern only for translation
	class ContactsManager
	{
		public ArrayList IdentityLookupsArray {get; set;}

		public ArrayList SetOfIdentitiesWhoseContactsDownloadInProgress {get; set;}

		private static ContactsManager instance;
		private ContactsManager()
		{
			/* code  */
		}
		public static ContactsManager SharedContactsManager()
		{
			if (instance == null)
				instance = new ContactsManager();
			return instance;
		}

		public void loadAddressBookContacts()
		{

		}

		public void loadContacts()
		{

		}

		public void refreshExisitngContacts()
		{

		}

		public void refreshRolodexContacts()
		{

		}

		public void identityLookupForContactsIdentityServiceDomain(ArrayList contacts, string identityServiceDomain)
		{

		}

		public void updateContactsWithDataFromLookup(HOPIdentityLookup identityLookup)
		{

		}

		public bool checkIsContactValid(HOPContact contact)
		{
			return false;
		}

		public ArrayList getBaseURIsForStableId(string stableID)
		{
			return null;
		}

		//string createProfileBundleForCommunicationWithContact(HOPRolodexContact targetContact);

		//HOPRolodexContact getRolodexContactByProfileBundleCoreContact(string profileBundle, HOPContact coreContact);

		public ArrayList getIdentityContactsForHomeUser()
		{
			return null;
		}

		public void removeAllContacts()
		{

		}
	}
}

