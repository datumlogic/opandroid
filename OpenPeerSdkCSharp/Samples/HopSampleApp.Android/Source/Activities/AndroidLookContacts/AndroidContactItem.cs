
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.Graphics;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp
{
	class AndroidContactItem
	{
		public string UserName {get; set;}
		public string IdentityUri{ get; set;}
		public string IdentityProvider{ get; set; }
		public string avatar{ get; set;}
		public Bitmap Image{ get; set;}
		public  Boolean FacebookAccount{ get; set;}
		public  int fbImage{ get; set;}
		public Boolean TwiterAccount{ get; set;}
		public int twImage{ get; set;}
		public Boolean LinkedinAccount { get; set;}
		public int LinkImage{ get; set;}
		public int ImageID{ get; set;}

		#region identity checks

		public string Identity(string identity)
		{
			if (identity.Contains("facebook"))
			{
				FacebookAccount = true;
				fbImage = Resource.Drawable.facebook;
				return identity;
			}
			else if (identity.Contains ("linkedin"))
			{
				LinkedinAccount = true;
				LinkImage = Resource.Drawable.linkedin;
				return identity;
			}
			return identity;
		}

		#endregion
	}
}

