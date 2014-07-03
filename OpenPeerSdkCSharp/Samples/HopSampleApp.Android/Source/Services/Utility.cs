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
using HopSampleApp.Enums;
using Mono;


namespace HopSampleApp
{

	public static class Utility
	{

		#region Base 64 Encription (Encoding/Decoding)

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
		#endregion

		#region Device Informacion

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

		//Get device manufacturer
		public static string GetManufacturer()
		{
			try
			{
				var Manufacturer=Android.OS.Build.Manufacturer;
			    return Manufacturer;
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("GetManufacturer Error:{0}",error.Message));
			}
		}

		//Get device platform.
		public static string GetPlatform()
		{
			try{
				int platformID = (int)Android.OS.Build.VERSION.SdkInt;
				switch (platformID)
				{
				case 3:
					return "Cupcake (API 3 Android 1.5)".ToString();
				case 4:
					return "Donut (API 4  Android 1.6)".ToString();
				case 5:
					return "Eclair  (API 5 Android 2.0)".ToString();
				case 6:
					return "Eclair (API 6 Android 2.0.1)".ToString();
				case 7:
					return "Eclair (API 7 Android 2.1)".ToString();
				case 8:
					return"Froyo (API 8  Android 2.2–2.2.3)".ToString();
				case 9:
					return "Gingerbread (API 9  Android 2.3–2.3.2)".ToString();
				case 10:
					return "Gingerbread (API 10 Android 2.3.3–2.3.7)".ToString();
				case 11:
					return "Honeycomb (API 11 Android 3.0)".ToString();
				case 12:
					return "Honeycomb (API 12 Android 3.1)".ToString();
				case 13:
					return "Honeycomb (API 13  Android 3.2)".ToString();
				case 14:
					return "Ice Cream Sandwich (API 14 Android 4.0–4.0.2)".ToString();
				case 15:
					return "Ice Cream Sandwich (api 15 Android 4.0.3–4.0.4)".ToString();
				case 16:
					return "Jelly Bean (API 16 Android 4.1)".ToString();
				case 17:
					return "Jelly Bean (API 17 Android 4.2)".ToString();
				case 18:
					return "Jelly Bean (API 18 Android 4.3)".ToString();
				case 19:
					return "KitKat (API 19 Android 4.4)".ToString();
				default:
					return "Unknown Platform".ToString();
				}
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("GetPlatform Error:{0}",error.Message));
			}
		}

		//Get Device id
		public static string GetDeviceID(Context context)
		{
			try
			{
				return  Android.Provider.Settings.Secure.GetString (context.ContentResolver, Android.Provider.Settings.Secure.AndroidId).ToString ();
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("GetDeviceID Error:{0}",error.Message));
			}
		}

		//Get Device model name
		public static string GetDeviceModelName()
		{
			try
			{
				var device = Android.OS.Build.Model;
				return device;
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("GetDeviceModelName Error:{0}",error.Message));
			}
		}

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


		//Get device model full name
		public static string GetDeviceFullNameOfModel()
		{
			try
			{
				var device=Android.OS.Build.Model;

				Dictionary<string,string> DeviceList=new Dictionary<string, string>(){
					{"SM-G900F","Samsung Galaxy S5"},{"SM-G900H","Samsung Galaxy S5"},{"SM-G900R4","Samsung Galaxy S5"},
					{"SM-G900V","Samsung Galaxy S5"},{"SM-G900RZWAUSC","Samsung Galaxy S5"},{"SM-G900W8","Samsung Galaxy S5"},
					{"GT-I9080","Samsung Galaxy Grand"},{"GT-I9082","Samsung Galaxy Grand"},{"SM-G7100","Samsung Galaxy Grand 2"},
					{"SM-G7102","Samsung Galaxy Grand 2"},{"SM-G7105","Samsung Galaxy Grand 2"},{"GT-S7500","Samsung Galaxy Ace Plus"},
					{"GT-S5830","Samsung Galaxy Ace"},{"GT-I9505","Samsung Galaxy S4"},{"GT-I9150","Samsung Galaxy Mega"},
					{"GT-I9152","Samsung Galaxy Mega"},{"GT-I9200","Samsung Galaxy Mega"},{"GT-I9205","Samsung Galaxy Mega"},
					{"GT-N7000","Samsung Galaxy Note"},{"GT-N7000B","Samsung Galaxy Note"},{"GT-N7005","Samsung Galaxy Note"},
					{"SHV-E160K","Samsung Galaxy Note"},{"SHV-E160L","Samsung Galaxy Note"},{"SHV-E160S","Samsung Galaxy Note"},
					{"SGH-I717","Samsung Galaxy Note"},{"SC-05D","Samsung Galaxy Note"},{"SGH-T879","Samsung Galaxy Note"},
					{"GT-I9228","Samsung Galaxy Note"},{"GT-I9220","Samsung Galaxy Note"},{"SCH-I889","Samsung Galaxy Note"},
					{"GT-N7100","Samsung Galaxy Note II"},{"GT-N7102","Samsung Galaxy Note II"},{"GT-N7105","Samsung Galaxy Note II"},
					{"GT-N7108","Samsung Galaxy Note II"},{"SCH-i605","Samsung Galaxy Note II"},{"SCH-R950","Samsung Galaxy Note II"},
					{"SGH-i317","Samsung Galaxy Note II"},{"SGH-i317M","Samsung Galaxy Note II"},{"SGH-T889","Samsung Galaxy Note II"},
					{"SGH-T889V","Samsung Galaxy Note II"},{"SPH-L900","Samsung Galaxy Note II"},{"SCH-N719","Samsung Galaxy Note II"},
					{"SM-P600","Samsung Galaxy Note 10.1"},{"SM-P601","Samsung Galaxy Note 10.1"},{"SM-P605","Samsung Galaxy Note 10.1"},
					{"GT-I9300","Samsung Galaxy S III"},{"SGH-I747","Samsung Galaxy S III"},{"SGH-N064","Samsung Galaxy S III"},
					{"SGH-N035","Samsung Galaxy S III"},{"GT-I9308","Samsung Galaxy S III"},{"LG-D820","Nexus 5"},
					{"LG-D821","Nexus 5"},{"LG-P880","LG Optimus 4X HD P880"},{"LG-P880g","LG Optimus 4X HD P880g"},
					{"SU640","LG Optimus LTE"},{"LU6200","LG Optimus LTE"},{"LG-P930","LG Nitro HD"},
					{"L-01D","Optimus LTE L-01D"},{"LG VS920","LG Spectrum"},{"SM-T320","Samsung Galaxy Tab Pro 8.4"},
					{"SM-T321","Samsung Galaxy Tab Pro 8.4"},{"SM-T325","Samsung Galaxy Tab Pro 8.4"}
				};

				if(DeviceList.ContainsKey(device))
					return DeviceList[device];
				else
					return "Unknown device";
			}
			catch(Exception error)
			{

				throw new Exception (String.Format("GetDeviceFullNameOfModel Error:{0}",error.Message));
			}



		}
		#endregion
			

		#region Methods for Various checks

		public static string GetAndroidVersionRelease()
		{
			try
			{
				return Android.OS.Build.VERSION.Release.ToString();
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("GetAndroidVersionRelease Error:{0}",error.Message));
			}
		}

		//Get Java UUID
		public static string GetInstanceIDJavaUUID()
		{
			try
			{
				return Java.Util.UUID.RandomUUID().ToString().Replace("-","");
			}
			catch(Exception error)
			{
				throw new Exception(String.Format("GetInstanceIDJavaUUID Error:{0}",error.Message));
			}
		}

		//Get GUID string.
		public static string GetGUIDInstanceID()
		{
			try
			{
				// Outputs "8c1d1c4b-df68-454c-bf30-953e5701949f"
			    Guid guid = Guid.NewGuid();
				return guid.ToString().Replace("-","");
			}
			catch(Exception error) 
			{
				throw new Exception(String.Format("GetGUIDString Error:{0}",error.Message));
			}

		}

		//Get Call states as string
		public static string GetCallStateAsString(HOPCallStates callState)
		{
			string res = null;
			switch (callState)
			{
			case HOPCallStates.HOPCallStateNone :
				break;
			case HOPCallStates.HOPCallStatePreparing :
				res = "preparing";
				break;
			case HOPCallStates.HOPCallStateIncoming :
				res = "incoming";
				break;
			case HOPCallStates.HOPCallStatePlaced :
				res = "placed";
				break;
			case HOPCallStates.HOPCallStateEarly :
				res = "early";
				break;
			case HOPCallStates.HOPCallStateRinging :
				res = "ringing";
				break;
			case HOPCallStates.HOPCallStateRingback :
				res = "ringback";
				break;
			case HOPCallStates.HOPCallStateOpen :
				res = "open";
				break;
			case HOPCallStates.HOPCallStateActive :
				res = "active";
				break;
			case HOPCallStates.HOPCallStateInactive :
				res = "inactive";
				break;
			case HOPCallStates.HOPCallStateHold :
				res = "hold";
				break;
			case HOPCallStates.HOPCallStateClosing :
				res = "closing";
				break;
			case HOPCallStates.HOPCallStateClosed :
				res = "closed";
				break;
			default :
				return null;
			}

			return res;
		}

		//Get Message Delivery state as string
		public static string GetMessageDeliveryStateAsString(HOPConversationThreadMessageDeliveryStates messageState)
		{
			string res = null;
			switch (messageState)
			{
			case HOPConversationThreadMessageDeliveryStates.HOPConversationThreadMessageDeliveryStateDiscovering:
				res = "discovering";
				break;
			case HOPConversationThreadMessageDeliveryStates.HOPConversationThreadMessageDeliveryStateUserNotAvailable:
				res = "user not available";
				break;
			case HOPConversationThreadMessageDeliveryStates.HOPConversationThreadMessageDeliveryStateDelivered:
				res = "delivered";
				break;
			default:
				break;
			}

			return res;
		}



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
					}
					else
					{ 
						return ret;
					}
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
		#endregion


	}
}