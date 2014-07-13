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
	class ChatItem
	{
		public string UserName{ get; set;}
		public string TxtMessage{ get; set;}
		public DateTime Date{ get; set;}
		public int UserId{ get; set;}
		public int Person{ get; set;}
	}
}

