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
namespace HopSampleApp
{
	public static class Utility
	{
		/*
		const char[] _base64EncodingTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		const short[] _base64DecodingTable = {
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -1, -1,
			-2, -1, -1, -2, -2, -2, -2, -2, -2, -2, -2,
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -1, 
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 62, 
			-2, -2, -2, 63, 52, 53, 54, 55, 56, 57, 58, 
			59, 60, 61, -2, -2, -2, -2, -2, -2, -2, 0, 1,
			2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 
			-2, -2, -2, -2, -2, -2, 26, 27, 28, 29, 30, 
			31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 
			42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -2, 
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2
		};
		//Need fix a litle
		public static string Base64StringFromDataLength(NSData data, int length)
		{
			uint ixtext, lentext;
			int ctremaining;
			byte input[3], output[4];
			short i, charsonline = 0, ctcopy;
			const byte raw;
			NSMutableString result;
			lentext = data.Length();
			if (lentext < 1) return "";

			result = NSMutableString.StringWithCapacity(lentext);
			raw = data.Bytes();
			ixtext = 0;
			while (true)
			{
				ctremaining = lentext - ixtext;
				if (ctremaining <= 0) break;

				for (i = 0; i < 3; i++)
				{
					uint ix = ixtext + i;
					if (ix < lentext) input[i] = raw[ix];
					else input[i] = 0;

				}

				output[0] = (input[0] & 0xFC) >> 2;
				output[1] = ((input[0] & 0x03) << 4) | ((input[1] & 0xF0) >> 4);
				output[2] = ((input[1] & 0x0F) << 2) | ((input[2] & 0xC0) >> 6);
				output[3] = input[2] & 0x3F;
				ctcopy = 4;
				switch (ctremaining)
				{
				case 1 :
					ctcopy = 2;
					break;
				case 2 :
					ctcopy = 3;
					break;
				}

				for (i = 0; i < ctcopy; i++) result.AppendString(NSString.StringWithFormat("%c", _base64EncodingTable[output[i]]));

				for (i = ctcopy; i < 4; i++) result.AppendString("=");

				ixtext += 3;
				charsonline += 4;
				if ((length > 0) && (charsonline >= length)) charsonline = 0;

			}

			return result;
		}


		public static string DecodeBase64(string data64based)
		{
			const char objPointer = data64based.CStringUsingEncoding(NSASCIIStringEncoding);
			if (objPointer == null) return null;

			size_t intLength = strlen(objPointer);
			int intCurrent;
			int i = 0, j = 0, k;
			byte objResult;
			objResult = calloc(intLength, sizeof (unsigned char));
			while (((intCurrent = objPointer++) != '\0') && (intLength-- > 0))
			{
				if (intCurrent == '=')
				{
					if (objPointer != '=' && ((i % 4) == 1))
					{
						free (objResult);
						return null;
					}

					continue;
				}

				intCurrent = _base64DecodingTable[intCurrent];
				if (intCurrent == -1)
				{
					continue;
				}
				else if (intCurrent == -2)
				{
					free (objResult);
					return null;
				}

				switch (i % 4)
				{
				case 0 :
					objResult[j] = intCurrent << 2;
					break;
				case 1 :
					objResult[j++] |= intCurrent >> 4;
					objResult[j] = (intCurrent & 0x0f) << 4;
					break;
				case 2 :
					objResult[j++] |= intCurrent >> 2;
					objResult[j] = (intCurrent & 0x03) << 6;
					break;
				case 3 :
					objResult[j++] |= intCurrent;
					break;
				}

				i++;
			}

			k = j;
			if (intCurrent == '=')
			{
				switch (i % 4)
				{
				case 1 :
					free(objResult);
					return null;
				case 2 :
					k++;
				case 3 :
					objResult[k] = 0;
				}

			}

			NSData retData = new NSData(objResult, j, true);
			string retString = new string(retData, NSUTF8StringEncoding);
			return retString;
		}
		*/
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
		//done
		static string GetDeviceOs()
		{

			string deviceOs = String.Format ("{0} {1}",Build.VERSION.Release,System.Environment.OSVersion);
			return deviceOs;

			//string deviceOs = NSString.StringWithFormat("%@ %@,", UIDevice.CurrentDevice().SystemName(), UIDevice.CurrentDevice().SystemVersion());
			//return deviceOs;


		}

		//
		static string GetUserAgentName()
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
		//done
		static void RemoveCookiesAndClearCredentials()
		{
			Android.Webkit.CookieSyncManager.CreateInstance (Android.App.Application.Context);
			Android.Webkit.CookieManager.Instance.RemoveAllCookie ();

			/*
			NSHTTPCookieStorage cookieStorage = NSHTTPCookieStorage.SharedHTTPCookieStorage();
			foreach (NSHTTPCookie each in cookieStorage.Cookies())
			{
				cookieStorage.DeleteCookie(each);
			}
			*/

		}

		//
		static string GetGUIDstring()
		{
			// Outputs "8c1d1c4b-df68-454c-bf30-953e5701949f"
			Guid guid = Guid.NewGuid();
			return guid.ToString();
			/*
			CFUUIDRef guid = CFUUIDCreate(null);
			string strGuid = (string)CFBridgingRelease(CFUUIDCreateString(null, guid));
			CFRelease (guid);
			return strGuid;
			*/
		}
		//need fix
		static string GetCallStateAsString(HOPCallStates callState)
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

		//
		static string GetMessageDeliveryStateAsString(HOPConversationThreadMessageDeliveryStates messageState)
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
		//
		static string GetFunctionNameForRequest(string requestString)
		{
			string ret = "";
			if (requestString.HasPrefix("https://datapass.hookflash.me/?method=")) ret = requestString.SubstringFromIndex("https://datapass.hookflash.me/?method=".Length());
			else if (requestString.HasPrefix("http://datapass.hookflash.me/?method=")) ret = requestString.SubstringFromIndex("http://datapass.hookflash.me/?method=".Length());

			ArrayList components = ret.ComponentsSeparatedByString(";");
			if (components.Count > 0) ret = components[0];

			return ret;
		}

		//
		static string GetParametersNameForRequest(string requestString)
		{
			string ret = "";
			ArrayList components = requestString.ComponentsSeparatedByString(";");
			if (components.Count == 2)
			{
				string theParams = (string)components[1];
				if (theParams.HasPrefix("data="))
				{
					ret = theParams.SubstringFromIndex("data=".Length());
				}

			}

			return ret;
		}
		//
		static UIBarButtonItem CreateNavigationBackButtonForTarget(object target)
		{
			UIButton button = UIButton.ButtonWithType(UIButtonTypeCustom);
			button.SetImageForState(UIImage.ImageNamed("iPhone_back_button.png"), UIControlStateNormal);
			button.AddTargetActionForControlEvents(target, @selector (popViewControllerAnimated:), UIControlEventTouchUpInside);
			button.Frame = CGRectMake(0.0, 0.0, 40.0, 40.0);
			UIBarButtonItem backButon = new UIBarButtonItem(button);
			return backButon;
		}

		//need fix
		static string FormatedMessageTimeStampForDate(NSDate inDate)
		{
			NSDateFormatter df = new NSDateFormatter();
			NSDateComponents massageDayOfDate = NSCalendar.CurrentCalendar().ComponentsFromDate(NSEraCalendarUnit | NSYearCalendarUnit | NSMonthCalendarUnit | NSDayCalendarUnit, inDate);
			NSDateComponents today = NSCalendar.CurrentCalendar().ComponentsFromDate(NSEraCalendarUnit | NSYearCalendarUnit | NSMonthCalendarUnit | NSDayCalendarUnit, NSDate.Date());
			if (today.Day() == massageDayOfDate.Day() && today.Month() == massageDayOfDate.Month() && today.Year() == massageDayOfDate.Year() && today.Era() == massageDayOfDate.Era())
			{
				df.SetDateFormat("hh:mm aa");
			}
			else
			{
				df.SetDateFormat("MM/dd/yyyy hh:mm aa");
			}

			return df.StringFromDate(inDate);
		}

		//need fix
		static string HexadecimalStringForData(NSData data)
		{
			const byte dataBuffer = (const unsigned char)data.Bytes();
			if (!dataBuffer) return NSString.TheString();

			uint dataLength = data.Length();
			NSMutableString hexString = NSMutableString.StringWithCapacity((dataLength * 2));
			for (int i = 0; i < dataLength; ++i) hexString.AppendString(NSString.StringWithFormat("%02lx", (unsigned long)dataBuffer[i]));

			return NSString.StringWithString(hexString);
		}

		//need fix
		static int GetNumberOfDeviceCameras()
		{
			return AVCaptureDevice.DevicesWithMediaType(AVMediaTypeVideo).Count();
		}
		//need fix
		static bool HasCamera()
		{
			return AVCaptureDevice.DevicesWithMediaType(AVMediaTypeVideo).Count() > 0;
		}
		//done
		static bool IsValidURL(string candidate)
		{
			//http|https|ftp|)\://|[a-zA-Z0-9\-\.]+\.[a-zA-Z](:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\-\._\?\,\'/\\\+&amp;%\$#\=~])*[^\.\,\)\(\s]$
			string urlRegEx = @"(http|https)://((\\w)*|([0-9]*)|([-|_])*)+([\\.|/]((\\w)*|([0-9]*)|([-|_])*))+";
			Regex urlTest = new Regex(urlRegEx, RegexOptions.Compiled | RegexOptions.IgnoreCase);
			return urlTest.IsMatch (candidate);
			/*
			string urlRegEx = "(http|https)://((\\w)*|([0-9]*)|([-|_])*)+([\\.|/]((\\w)*|([0-9]*)|([-|_])*))+";
			NSPredicate urlTest = NSPredicate.PredicateWithFormat("SELF MATCHES %@", urlRegEx);
			return urlTest.EvaluateWithObject(candidate);
			*/
		}

		//done
		static bool IsValidJSON(string data)
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
					System.Diagnostics.Debug.WriteLine(String.Format("Parse Error:{0}",error.Message));
					return ret;
				}
			}
			return ret;

			/*
			bool ret = false;
			if (json.Length() > 0)
			{
				NSData data = json.DataUsingEncoding(NSUTF8StringEncoding);
				object jsonObj = NSJSONSerialization.JSONObjectWithDataOptionsError(data, kNilOptions, null);
				ret = jsonObj != null;
			}

			return ret;
			*/
		}
		//done
		static string StringFromDate(DateTime date)
		{
			TimeZone zone = TimeZone.CurrentTimeZone;
			DateTime timeFormater = zone.ToLocalTime(date);//DateTime.Now
			var time = timeFormater.ToString("yyyy:MM:dd HH:mm");
			return time.ToString();
			/*
			NSDateFormatter timeFormatter = new NSDateFormatter();
			timeFormatter.SetTimeZone(NSTimeZone.SystemTimeZone());
			timeFormatter.SetDateFormat("yyyy:MM:dd HH:mm");
			string ret = timeFormatter.StringFromDate(date);
			return ret;
			*/
		}
		//done
		static DateTime DateFromTimeString(string date)
		{
			TimeZone zone = TimeZone.CurrentTimeZone;
			DateTime timeFormater = zone.ToLocalTime(Convert.ToDateTime(date));
			return timeFormater;
			/*
			DateTime timeFormatter = new DateTime();
			timeFormatter.ToString("yyyy:MM:dd HH:mm");
			timeFormatter.NSTimeZone.SystemTimeZone());
			NSDate date = timeFormatter.DateFromString(timeStr);
			return date;
			*/
		}
		//need fix
		static bool IsAppUpdated()
		{
			bool ret = true;
			NSDate appPreviousModificationDate = NSUserDefaults.StandardUserDefaults().ObjectForKey("appUpdateDate");
			string exePath = NSBundle.MainBundle().ExecutablePath();
			NSDictionary exeAttrs = NSFileManager.DefaultManager().AttributesOfItemAtPathError(exePath, null);
			NSDate lastModificationDate = exeAttrs.ObjectForKey("NSFileModificationDate");
			if (lastModificationDate.IsEqualToDate(appPreviousModificationDate)) ret = false;
			else NSUserDefaults.StandardUserDefaults().SetObjectForKey(lastModificationDate, "appUpdateDate");

			return ret;
		}
		//
	}
}