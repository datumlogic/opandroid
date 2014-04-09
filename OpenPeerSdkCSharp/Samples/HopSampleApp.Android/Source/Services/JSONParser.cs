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
//using Newtonsoft.Json.Serialization;
using System.IO;
//using Newtonsoft.Json.Linq;//https://github.com/ayoung/Newtonsoft.Json  commit a77ac5499636ec00c31533eb06df7d3b5336c077

namespace HopSampleApp
{
	class JSONParser
	{
		public Dictionary<string, string> fieldValuePair { get; set; }

		/* Logic for reading string direct from QR Code where image created with JSON file  */
		public static void ParseData(string data)
		{
			/*
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
				Console.WriteLine(String.Format("Settings:{0},{1},{3},{4},{5},{6},{7}",jsondata.outerFrameURL,jsondata.identityProviderDomain,jsondata.identityFederateBaseURI,jsondata.namespaceGrantServiceURL,jsondata.lockBoxServiceDomain,jsondata.archiveOutgoingTelnetLoggerServer,jsondata.openpeer_stack_bootstrapper_force_well_known_over_insecure_http,jsondata.openpeer_stack_bootstrapper_force_well_known_using_post,jsondata.defaultOutgoingTelnetServer));
			}
			catch(Exception Error)
			{
				Console.WriteLine (String.Format("QRCodeError:{0}",Error.Message));//Error Message to determine where the error
			}
			*/
		}
		/* Logic for reading json string from QR Code where image created with http link witch contains path to JSON File  */
		public static void ParseData(string url,string paramether)
		{
			//Optimal
		}

		public static void ParseData(string http_protocol,string domain,string paramether)
		{
			//Optimal
		}
		public static void ParseDataCompleteURL(string url_complete)
		{
			/*
			try
			{
			var request = HttpWebRequest.Create (url_complete);
			request.ContentType = "application/json";
			request.Method = "GET";
			using (HttpWebResponse response = request.GetResponse() as HttpWebResponse)
			{
				if (response.StatusCode != HttpStatusCode.OK)
					Console.Out.WriteLine("Error fetching data. Server returned status code: {0}", response.StatusCode);
				using (StreamReader reader = new StreamReader(response.GetResponseStream()))
				{
					var content = reader.ReadToEnd();
					if(string.IsNullOrWhiteSpace(content)) {
						Console.Out.WriteLine("Response contained empty body...");
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
						Console.WriteLine(String.Format("HTTP Settings:{0},{1},{3},{4},{5},{6},{7}",jsondata.outerFrameURL,jsondata.identityProviderDomain,jsondata.identityFederateBaseURI,jsondata.namespaceGrantServiceURL,jsondata.lockBoxServiceDomain,jsondata.archiveOutgoingTelnetLoggerServer,jsondata.openpeer_stack_bootstrapper_force_well_known_over_insecure_http,jsondata.openpeer_stack_bootstrapper_force_well_known_using_post,jsondata.defaultOutgoingTelnetServer));

					}

					//Assert.NotNull(content);
				}
			}
			}
			catch(Exception Error)
			{
				Console.WriteLine("You need internet Connection for this option");
			}
			*/
		}
	}



}

