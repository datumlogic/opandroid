
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
	class HOPAssociatedIdentity
	{
		public string BaseIdentityURI {get; set;}

		public string Domain {get; set;}

		public string DownloadedVersion {get; set;}

		public string Name {get; set;}

		public HOPHomeUser HomeUser {get; set;}

		//public HOPRolodexContact HomeUserProfile {get; set;}

		public Dictionary<int,object> RolodexContacts {get; set;}
	}
}

