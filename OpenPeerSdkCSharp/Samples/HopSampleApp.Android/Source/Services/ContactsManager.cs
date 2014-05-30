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

namespace HopSampleApp
{   
	 // Singleton Pattern only for translation
	class ContactsManager
	{
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
		public void UpdateContactsWithDataFromLookup (object somevalue)
		{
			/*code*/ 
		}
	}
}

