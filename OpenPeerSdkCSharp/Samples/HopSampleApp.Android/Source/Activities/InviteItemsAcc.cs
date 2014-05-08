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
	class InviteItemsAcc
	{
		public string Name {get; set;}
		public string Lastname{get;set;}
		public Boolean FacebookAccount{ get; set;}
		public int fbImage{ get; set;}
		public Boolean TwiterAccount{ get; set;}
		public int twImage{ get; set;}
		public Boolean LinkedinAccount { get; set;}
		public int LinkImage{ get; set;}
		public int ImageID{ get; set;}
		public string UserName{ get;set;}
	}
}

