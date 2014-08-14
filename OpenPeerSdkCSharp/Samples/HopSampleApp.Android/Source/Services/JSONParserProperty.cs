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
	class JSONParserProperty
	{
		/*  Propertyes for JsonParser class  */
		public string outerFrameURL { get; set;}
		public string identityProviderDomain { get; set;}
		public string identityFederateBaseURI { get; set;}
		public string namespaceGrantServiceURL { get; set;}
		public string lockBoxServiceDomain { get; set;}
		public string archiveOutgoingTelnetLoggerServer { get; set;}
		public string defaultOutgoingTelnetServer{ get; set; }
		public string openpeer_stack_bootstrapper_force_well_known_over_insecure_http { get; set;}
		public string openpeer_stack_bootstrapper_force_well_known_using_post { get; set;}
		public static bool GestureOption{ get; set;}
	}
}

