
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
	class HOPHomeUser
	{
		public int LoggedIn {get; set;}

		public string ReloginInfo {get; set;}

		public string StableId {get; set;}

		public string AssociatedIdentities {get; set;}

		void addAssociatedIdentitiesObject(HOPAssociatedIdentity value){}

		void removeAssociatedIdentitiesObject(HOPAssociatedIdentity value){}

		void addAssociatedIdentities(ArrayList values){}

		void removeAssociatedIdentities(ArrayList values){}
	}
}

