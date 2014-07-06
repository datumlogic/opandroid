/*
Copyright (c) 2014, hookflash Inc.
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FreeBSD Project.
*/
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
	/// <summary>
	/// ############################################################################
	///                           JSON PARSER PROPERTY
	/// ############################################################################
	/// These are propertyes for JSON PARSER CLASS
	/// </summary>
	class JSONParserProperty
	{
		/*  Propertyes for JsonParser class  */
		public static DateTime date{ get; set; }
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

