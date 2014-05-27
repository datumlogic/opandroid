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
using System.Collections;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using System.Net;
using Android.Net.Http;
using Android.Net;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json.Schema;
using Newtonsoft.Json.Serialization;
using System.Diagnostics.CodeAnalysis;
using System.Diagnostics;
using System.Text.RegularExpressions;
using Android.Hardware;
using Mono;


namespace HopSampleApp
{
	public static class Utility
	{
		//Base 64 Encoding.
		public static string base64Encode(string data)
		{
			try
			{
				byte[] encData_byte = new byte[data.Length];
				encData_byte = System.Text.Encoding.UTF8.GetBytes(data);    
				string encodedData = Convert.ToBase64String(encData_byte);
				return encodedData;
			}
			catch(Exception e)
			{
				throw new Exception("Error in base64Encode" + e.Message);
			}
		}
		//Base 64 Decoding.
		public static string base64Decode(string data)
		{
			try
			{
				System.Text.UTF8Encoding encoder = new System.Text.UTF8Encoding();  
				System.Text.Decoder utf8Decode = encoder.GetDecoder();

				byte[] todecode_byte = Convert.FromBase64String(data);
				int charCount = utf8Decode.GetCharCount(todecode_byte, 0, todecode_byte.Length);    
				char[] decoded_char = new char[charCount];
				utf8Decode.GetChars(todecode_byte, 0, todecode_byte.Length, decoded_char, 0);                   
				string result = new String(decoded_char);
				return result;
			}
			catch(Exception e)
			{
				throw new Exception("Error in base64Decode" + e.Message);
			}
		}
		//Get Device os info.
		public static string GetDeviceOs()
		{
			try
			{
				string deviceOs = String.Format ("{0} {1}",Build.VERSION.Release,System.Environment.OSVersion);
			    return deviceOs;
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("GetDeviceOS Error:{0}",error.Message));

			}

		}

		/*
		public static string GetUserAgentName()
		{
			string developerId = NSBundle.MainBundle().InfoDictionary().ObjectForKey("Hookflash Developer ID");
			string appName = NSBundle.MainBundle().InfoDictionary().ObjectForKey("CFBundleName");
			string appVersion = NSBundle.MainBundle().InfoDictionary().ObjectForKey("CFBundleVersion");
			string appOs = UIDevice.CurrentDevice().SystemName();
			string appVersionOs = UIDevice.CurrentDevice().SystemVersion();
			string deviceModel = UIDevice.CurrentDevice().Model();
			string model = null;
			if (deviceModel.HasPrefix("iPhone") || deviceModel.HasPrefix("iPod")) model = "iPhone";
			else if (deviceModel.HasPrefix("iPad")) model = "iPad";

			string userAgent = NSString.StringWithFormat("%@/%@ (%@ %@;%@) HOPID/1.0 (%@)", appName, appVersion, appOs, appVersionOs, model, developerId);
			return userAgent;
		}
		*/

		//Remove Cookies And Clear Credentials.
		public static void RemoveCookiesAndClearCredentials()
		{
			try
			{
				Android.Webkit.CookieSyncManager.CreateInstance (Android.App.Application.Context);
		        Android.Webkit.CookieManager.Instance.RemoveAllCookie ();
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("RemoveCookiesAndClearCredentials Error:{0}",error.Message));

			}
		}
		public static string GetManufacturer()
		{
			var Manufacturer=Android.OS.Build.Manufacturer;
			return Manufacturer;
		}

		public static string GetPlatform()
		{
			var platformID = Android.OS.Build.VERSION.SdkInt.ToString();
			switch (platformID)
			{
			case "3":
				return "Cupcake (API 3 Android 1.5)";
				break;
			case "4":
				return "Donut (API 4  Android 1.6)";
				break;
			case "5":
				return "Eclair  (API 5 Android 2.0)";
				break;

			case "6":
				return "Eclair (API 6 Android 2.0.1)";
				break;
			case "7":
				return "Eclair (API 7 Android 2.1) ";
				break;
			case "8":
				return"Froyo (API 8  Android 2.2–2.2.3)";
				break;
			case "9":
				return "Gingerbread (API 9  Android 2.3–2.3.2)";
				break;
			case "10":
				return "Gingerbread (API 10 Android 2.3.3–2.3.7)";
				break;
			case "11":
				return "Honeycomb (API 11 Android 3.0)";
				break;
			case "12":
				return "Honeycomb (API 12 Android 3.1)";
				break;
			case "13":
				return "Honeycomb (API 13  Android 3.2 )";
				break;
			case "14":
				return "Ice Cream Sandwich (API 14 Android 4.0–4.0.2 )";
				break;
			case "15":
				return "Ice Cream Sandwich (api 15 Android 4.0.3–4.0.4 )";
				break;
			case "16":
				return "Jelly Bean (API 16 Android 4.1)";
				break;
			case "17":
				return "Jelly Bean (API 17 Android 4.2)";
				break;
			case "18":
				return "Jelly Bean (API 18 Android 4.3)";
				break;
			case "19":
				return "KitKat (API 19 Android 4.4)";
				break;
			default:
				return "Unknown";
				break;
			}
		}
		public static string GetDeviceModelName()
		{
			var device = Android.OS.Build.Model;
			return device;
		}
		//need fix and change for andorid
		public static string GetDeviceFullNameOfModel()
		{

			var device=Android.OS.Build.Model;
			//Samsung Galaxy S4
			if (device == "GT-I9505")return "Samsung Galaxy S4";

			//Samsung Galaxy S5
			if (device == "SM-G900F")return"Samsung Galaxy S5";
			if (device == "SM-G900H")return "Samsung Galaxy S5";
			if (device == "SM-G900R4")return "Samsung Galaxy S5";
			if (device == "SM-G900V")return "Samsung Galaxy S5";
			if (device == "SM-G900RZWAUSC")return "Samsung Galaxy S5";
			if (device == "SM-G900W8")return "Samsung Galaxy S5";

			//Samsung Galaxy Grand
			if (device == "GT-I9080")return "Samsung Galaxy Grand";
			if (device == "GT-I9082")return "Samsung Galaxy Grand";
			if (device == "SM-G7100")return "Samsung Galaxy Grand 2";
			if (device == "SM-G7102")return "Samsung Galaxy Grand 2";
			if (device == "SM-G7105")return "Samsung Galaxy Grand 2";

			//Samsung Galaxy Ace
			if (device == "GT-S7500")return "Samsung Galaxy Ace Plus";
			if (device == "GT-S5830")return "Samsung Galaxy Ace";

			//Samsung Galaxy Mega
			if (device == "GT-I9150")return "Samsung Galaxy Mega";
			if (device == "GT-I9152")return "Samsung Galaxy Mega";
			if (device == "GT-I9200")return "Samsung Galaxy Mega";
			if (device == "GT-I9205")return "Samsung Galaxy Mega";

			//Galaxy Nexus
			if (device == "GT-I9250")return "Galaxy Nexus";

			//Samsung Galaxy Note
			if (device == "GT-N7000")return "Samsung Galaxy Note";
			if (device == "GT-N7000B")return "Samsung Galaxy Note";
			if (device == "GT-N7005")return "Samsung Galaxy Note";
			if (device == "SHV-E160K")return "Samsung Galaxy Note";
			if (device == "SHV-E160L")return "Samsung Galaxy Note";
			if (device == "SHV-E160S")return "Samsung Galaxy Note";
			if (device == "SGH-I717")return "Samsung Galaxy Note";
			if (device == "SC-05D")return "Samsung Galaxy Note";
			if (device == "SGH-T879")return "Samsung Galaxy Note";
			if (device == "GT-I9228")return "Samsung Galaxy Note";
			if (device == "GT-I9220")return "Samsung Galaxy Note";
			if (device == "SCH-I889")return "Samsung Galaxy Note";

			//Samsung Galaxy Note II
			if (device == "GT-N7100")return "Samsung Galaxy Note II";
			if (device == "GT-N7102")return "Samsung Galaxy Note II";
			if (device == "GT-N7105")return "Samsung Galaxy Note II";
			if (device == "GT-N7108")return "Samsung Galaxy Note II";
			if (device == "SCH-i605")return "Samsung Galaxy Note II";
			if (device == "SCH-R950")return "Samsung Galaxy Note II";
			if (device == "SGH-i317")return "Samsung Galaxy Note II";
			if (device == "SGH-i317M")return"Samsung Galaxy Note II";
			if (device == "SGH-T889")return "Samsung Galaxy Note II";
			if (device == "SGH-T889V")return "Samsung Galaxy Note II";
			if (device == "SPH-L900")return "Samsung Galaxy Note II";
			if (device == "SCH-N719")return "Samsung Galaxy Note II";

			//Samsung Galaxy Note 10.1
			if (device =="SM-P600")return "Samsung Galaxy Note 10.1";
			if (device == "SM-P601")return "Samsung Galaxy Note 10.1";
			if (device == "SM-P605")return "Samsung Galaxy Note 10.1";

			//Samsung Galaxy S III
			if (device == "GT-I9300")return"Samsung Galaxy S III";
			if (device == "SGH-I747")return"Samsung Galaxy S III";
			if (device == "SGH-N064")return "Samsung Galaxy S III";
			if (device == "SGH-N035")return "Samsung Galaxy S III";
			if (device == "GT-I9308")return "Samsung Galaxy S III";

			//Nexus 5
			if (device == "LG-D820")return"Nexus 5";
			if (device == "LG-D821")return"Nexus 5";

			//LG Optimus 4X HD P880
			if (device == "LG-P880")return"LG Optimus 4X HD P880";
			if (device == "LG-P880g")return"LG Optimus 4X HD P880g";

			//LG Optimus
			if (device == "SU640") return "LG Optimus LTE";
			if (device == "LU6200") return "LG Optimus LTE";
			if (device == "LG P930") return "LG Optimus LTE";
			if (device == "LG P930") return "LG Nitro HD";
			if (device == "L-01D") return "Optimus LTE L-01D";
			if (device == "LG VS920") return "LG Spectrum";

			//Samsung Galaxy Tab Pro 8.4
			if (device == "SM-T320") return"Samsung Galaxy Tab Pro 8.4";
			if (device == "SM-T321") return "Samsung Galaxy Tab Pro 8.4";
			if (device == "SM-T325") return "Samsung Galaxy Tab Pro 8.4";

			return device;
            
		}


		//Get GUID string.
		public static string GetGUIDstring()
		{
			try
			{
				// Outputs "8c1d1c4b-df68-454c-bf30-953e5701949f"
			    Guid guid = Guid.NewGuid();
			    return guid.ToString();
			}
			catch(Exception error) 
			{
				throw new Exception(String.Format("GetGUIDString Error:{0}",error.Message));
			}

		}
		/*
		//need sdk for fix
		public static string GetCallStateAsString(HOPCallStates callState)
		{
			string res = null;
			switch (callState)
			{
			case HOPCallStateNone :
				break;
			case HOPCallStatePreparing :
				res = NSLocalizedString("preparing", "");
				break;
			case HOPCallStateIncoming :
				res = NSLocalizedString("incoming", "");
				break;
			case HOPCallStatePlaced :
				res = NSLocalizedString("placed", "");
				break;
			case HOPCallStateEarly :
				res = NSLocalizedString("early", "");
				break;
			case HOPCallStateRinging :
				res = NSLocalizedString("ringing", "");
				break;
			case HOPCallStateRingback :
				res = NSLocalizedString("ringback", "");
				break;
			case HOPCallStateOpen :
				res = NSLocalizedString("open", "");
				break;
			case HOPCallStateActive :
				res = NSLocalizedString("active", "");
				break;
			case HOPCallStateInactive :
				res = NSLocalizedString("inactive", "");
				break;
			case HOPCallStateHold :
				res = NSLocalizedString("hold", "");
				break;
			case HOPCallStateClosing :
				res = NSLocalizedString("closing", "");
				break;
			case HOPCallStateClosed :
				res = NSLocalizedString("closed", "");
				break;
			default :
				return null;
			}

			return res;
		}
		*/

		/*
		//need sdk for fix
		public static string GetMessageDeliveryStateAsString(HOPConversationThreadMessageDeliveryStates messageState)
		{
			string res = null;
			switch (messageState)
			{
			case HOPConversationThreadMessageDeliveryStateDiscovering :
				res = NSLocalizedString("discovering", "");
				break;
			case HOPConversationThreadMessageDeliveryStateUserNotAvailable :
				res = NSLocalizedString("user not available", "");
				break;
			case HOPConversationThreadMessageDeliveryStateDelivered :
				res = NSLocalizedString("delivered", "");
				break;
			}

			return res;
		}

		*/

		/*
		//need fix
		public static string HexadecimalStringForData(NSData data)
		{
			const byte dataBuffer = (const unsigned char)data.Bytes();
			if (!dataBuffer) return NSString.TheString();

			uint dataLength = data.Length();
			NSMutableString hexString = NSMutableString.StringWithCapacity((dataLength * 2));
			for (int i = 0; i < dataLength; ++i) hexString.AppendString(NSString.StringWithFormat("%02lx", (unsigned long)dataBuffer[i]));

			return NSString.StringWithString(hexString);
		}
		*/


		//Count number of cameras that current device have. 
		public static int GetNumberOfDeviceCameras()
		{
			try
			{
				return Camera.NumberOfCameras;
			}
			catch(Exception error)
			{
				throw new Exception (String.Format ("GetNumberOfDeviceCameras Error:{0}", error.Message));
			}
		}



		//Check if current device have camera.
		public static bool HasCamera()
		{
			try
			{
				int NumberOfCammera = Camera.NumberOfCameras;
			    if (NumberOfCammera > 0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			catch(Exception error)
			{
				throw new Exception (String.Format ("HasCamera Error:{0}",error.Message));
			}

		}


		//Check if is url valid using regular expression.
		public static bool IsValidURL(string candidate)
		{
			try
			{
				//http|https|ftp|)\://|[a-zA-Z0-9\-\.]+\.[a-zA-Z](:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\-\._\?\,\'/\\\+&amp;%\$#\=~])*[^\.\,\)\(\s]$
				string urlRegEx = @"(http|https)://((\\w)*|([0-9]*)|([-|_])*)+([\\.|/]((\\w)*|([0-9]*)|([-|_])*))+";
			    Regex urlTest = new Regex(urlRegEx, RegexOptions.Compiled | RegexOptions.IgnoreCase);
			    return urlTest.IsMatch (candidate);
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("IsValidURL Error:{0}",error.Message));
			}
		}

		//Check if is Json valid.
		public static bool IsValidJSON(string data)
		{
			bool ret = false;
			if (data.Length > 0)
			{
				try
				{
					JsonSchema schema = JsonSchema.Parse(data);
					JObject json = JObject.Parse(data);
					if (json.IsValid(schema))
					{
						return ret=json.IsValid(schema);
					}else{ return ret; }
				}
				catch (Exception error)
				{ 
					throw new Exception (String.Format ("Parse Error:{0}",error.Message));

				}
			}
			return ret;

		}
		//Get date string from date.
		public static string StringFromDate(DateTime date)
		{
			try
			{
				TimeZone zone = TimeZone.CurrentTimeZone;
			    DateTime timeFormater = zone.ToLocalTime(date);//DateTime.Now
			    var time = timeFormater.ToString("yyyy:MM:dd HH:mm");
			    return time.ToString();
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("StringFromDate Error:{0}",error.Message));
			}
		}
		//get date from string.
		public static DateTime DateFromTimeString(string date)
		{
			try
			{
				TimeZone zone = TimeZone.CurrentTimeZone;
			    DateTime timeFormater = zone.ToLocalTime(Convert.ToDateTime(date));
			    return timeFormater;
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("DateFromTimeString Error:{0}",error.Message));
			}
		}
		public static string Proba()
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);  
			var somePref = prefs.GetString("APPSETTINGS", null);
			return somePref;
		}

		//Check if app setting need to be updated.
		public static bool IsAppUpdated()
		{
			try
			{
				bool ret = false;
				var settings = Application.Context.GetSharedPreferences("android_sample_app", FileCreationMode.Private);  
			    var data1 = settings.GetString("date", null);
				if (JSONParserProperty.date != Convert.ToDateTime(data1))
				{
					ret=true;
					return ret;
				}
				else
				{
					ret=false;
					return ret;
				}
			

			}
			catch(Exception error)
			{
				throw new Exception (String.Format ("IsAppUpdated Error:{0}", error.Message));
			}
		}



	}
}