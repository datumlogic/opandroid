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
	class SessionItem
	{
		public int Id{ get; set;}
		public String SessionTypeName{ get; set;}
		public String Username{ get; set; }
		public DateTime SessionDate{ get; set;}
		public String SessionTime{ get; set;}
		public String SesisonUserName{ get;set;}
		public String SessionMyName{ get; set;}
		public int UserImg{ get; set;}
	}
}

