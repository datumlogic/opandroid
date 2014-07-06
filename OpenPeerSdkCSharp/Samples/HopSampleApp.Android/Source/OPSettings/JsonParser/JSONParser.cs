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
using System.Net;
using Newtonsoft.Json.Serialization;
using System.IO;
using Android.Util;
using Newtonsoft.Json.Linq;//https://github.com/ayoung/Newtonsoft.Json  commit a77ac5499636ec00c31533eb06df7d3b5336c077

namespace HopSampleApp
{
	/// <summary>
	/// ###############################################################################
	///                                 JSON PARSER
	/// ###############################################################################
	/// JSONParser class contains methods:
	/// -------------------------------------------------------------------------------
	/// - ParseData (Logic for reading string direct from QR Code where image created 
	/// with JSON file)
	/// - ParseDataCompleteURL (Logic for reading string direct from QR Code where
	///  image created with JSON file from the internet.)
	/// - ParseData(string url,string paramether) ! Optimal and not implemented
	/// - ParseData(string http_protocol,string domain,string paramether)
	/// !Optimal and not implemented
	/// </summary>
	class JSONParser
	{
		public Dictionary<string, string> fieldValuePair { get; set; }

		#region Logic for reading string direct from QR Code where image created with JSON file

		#region ParseData(data)
		public static void ParseData(string data)
		{

			try{
			var jObj = JObject.Parse(data);
			var jsondata = jObj.Children ().Cast<JProperty> ().Select (c => new JSONParserProperty
				{

					outerFrameURL = (string)c.Value ["outerFrameURL"],
					identityProviderDomain=(string)c.Value["identityProviderDomain"],
					identityFederateBaseURI=(string)c.Value["identityFederateBaseURI"],
					namespaceGrantServiceURL=(string)c.Value["namespaceGrantServiceURL"],
					lockBoxServiceDomain=(string)c.Value["lockBoxServiceDomain"],
					defaultOutgoingTelnetServer = (string)c.Value["defaultOutgoingTelnetServer"],
					archiveOutgoingTelnetLoggerServer=(string)c.Value["archiveOutgoingTelnetLoggerServer"],
					openpeer_stack_bootstrapper_force_well_known_over_insecure_http=(string)c.Value["openpeer/stack/bootstrapper-force-well-known-over-insecure-http"],
					openpeer_stack_bootstrapper_force_well_known_using_post=(string)c.Value["openpeer/stack/bootstrapper-force-well-known-using-post"]//

				} ).FirstOrDefault();//Mapping childred elements from root element of json file.
				Log.Debug("output",String.Format("Settings:{0},{1},{3},{4},{5},{6},{7}",jsondata.outerFrameURL,jsondata.identityProviderDomain,jsondata.identityFederateBaseURI,jsondata.namespaceGrantServiceURL,jsondata.lockBoxServiceDomain,jsondata.archiveOutgoingTelnetLoggerServer,jsondata.openpeer_stack_bootstrapper_force_well_known_over_insecure_http,jsondata.openpeer_stack_bootstrapper_force_well_known_using_post,jsondata.defaultOutgoingTelnetServer));
			}
			catch(Exception Error)
			{
				Log.Error ("Error",String.Format("QRCodeError:{0}",Error.Message));//Error Message to determine where the error
			}

		}

		#endregion

		#endregion

		#region Logic for reading json string from QR Code where image created with http link witch contains path to JSON File  */

		#region ParseData(string url,string paramether) !Optimal not implemented

		public static void ParseData(string url,string paramether)
		{
			//Optimal
		}

		#endregion

		#region ParseData(string http_protocol,string domain,string paramether) !Optimal not implemented

		public static void ParseData(string http_protocol,string domain,string paramether)
		{
			//Optimal
		}

		#endregion

		#region  ParseDataCompleteURL(string url_complete)

		public static void ParseDataCompleteURL(string url_complete)
		{
			try
			{
			var request = HttpWebRequest.Create (url_complete);
			request.ContentType = "application/json";
			request.Method = "GET";
			using (HttpWebResponse response = request.GetResponse() as HttpWebResponse)
			{
				if (response.StatusCode != HttpStatusCode.OK)
						Log.Debug("output","Error fetching data. Server returned status code: {0}", response.StatusCode);
				using (StreamReader reader = new StreamReader(response.GetResponseStream()))
				{
					var content = reader.ReadToEnd();
					if(string.IsNullOrWhiteSpace(content)) {
							Log.Debug("output","Response contained empty body...");
					}
					else {
						var jObj = JObject.Parse(content);
						var jsondata = jObj.Children ().Cast<JProperty> ().Select (c => new JSONParserProperty
							{

								outerFrameURL = (string)c.Value ["outerFrameURL"],
								identityProviderDomain=(string)c.Value["identityProviderDomain"],
								identityFederateBaseURI=(string)c.Value["identityFederateBaseURI"],
								namespaceGrantServiceURL=(string)c.Value["namespaceGrantServiceURL"],
								lockBoxServiceDomain=(string)c.Value["lockBoxServiceDomain"],
								defaultOutgoingTelnetServer = (string)c.Value["defaultOutgoingTelnetServer"],
								archiveOutgoingTelnetLoggerServer=(string)c.Value["archiveOutgoingTelnetLoggerServer"],
								openpeer_stack_bootstrapper_force_well_known_over_insecure_http=(string)c.Value["openpeer/stack/bootstrapper-force-well-known-over-insecure-http"],
								openpeer_stack_bootstrapper_force_well_known_using_post=(string)c.Value["openpeer/stack/bootstrapper-force-well-known-using-post"]//

							} ).FirstOrDefault();//Mapping childred elements from root element of json file.
							Log.Debug("output",String.Format("HTTP Settings:{0},{1},{3},{4},{5},{6},{7}",jsondata.outerFrameURL,jsondata.identityProviderDomain,jsondata.identityFederateBaseURI,jsondata.namespaceGrantServiceURL,jsondata.lockBoxServiceDomain,jsondata.archiveOutgoingTelnetLoggerServer,jsondata.openpeer_stack_bootstrapper_force_well_known_over_insecure_http,jsondata.openpeer_stack_bootstrapper_force_well_known_using_post,jsondata.defaultOutgoingTelnetServer));

					}

					//Assert.NotNull(content);
				}
			}
			}
			catch(Exception Error)
			{
				Log.Error("Error",String.Format("You need internet Connection for this option:{0}",Error.Message));
			}
		}

		#endregion

		#endregion
	}



}

