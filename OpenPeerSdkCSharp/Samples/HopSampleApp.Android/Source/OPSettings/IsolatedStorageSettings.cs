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
using System.IO;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp
{
	/// <summary>
	/// ##########################################################################################
	///                             ISOLATED STORAGE SETTINGS
	/// ##########################################################################################
	/// IsolatedStorageSettings class contains methods:
	/// ------------------------------------------------------------------------------------------
	/// - AddSettings (Adding settings to the well-known app settings)
	/// - AddSettingsToSpecificSettings(Adding settings to your specific settings )
	/// - ContainsKey (Check if well-known settings contains some settings)
	/// - ContainsKey(key,settings_name) (Check if your specific settings contains some settings)
	/// - StringForKey (Return string for key in your well-known app settings )
	/// - StringForKey(key,settings_name) (Return string for key in your specific app settings )
	/// - RemoveSettingByKey (Remove setting in your well-known app settings)
	/// - RemoveSettingByKey(key,setting_name) (Remove setting in your specific app settings)
	/// - RemoveSettings (Remove all setting) ! not implemented !
	/// - SettingsLoadInDictionary (Load all settings in Dictionary )
	/// </summary>

	class IsolatedStorageSettings
	{
		static IsolatedStorageSettings appSettings = null;
		public static IsolatedStorageSettings ApplicationSettings
		{
			get
			{
				if (appSettings == null)
					appSettings = new IsolatedStorageSettings();
				return appSettings;
			}
		}

		public object this[string key]
		{
			get
			{
				// Load
				var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
				return prefs.GetString(key, null);
			}
			set
			{
				AddSettings(key, value);
			}
		}

		#region Adding to SharedPreferences

		public void AddSettings(string key, object value)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			var prefEditor = prefs.Edit();
			prefEditor.PutString(key, Convert.ToString(value));
			prefEditor.Commit();
		}

		public static void AddSettingsToSpecificSettings(string settings_name,string key, object value)
		{
			var prefs = Application.Context.GetSharedPreferences(settings_name, FileCreationMode.Private);
			var prefEditor = prefs.Edit();
			prefEditor.PutString(key, Convert.ToString(value));
			prefEditor.Commit();
			prefEditor.Apply ();
		}
		#endregion

		#region Checking is there a value in SharedPreferences

		public bool ContainsKey(string key)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			return prefs.Contains(key);
		}

		public bool ContainsKey(string key,string settings_name)
		{
			var prefs = Application.Context.GetSharedPreferences(settings_name, FileCreationMode.Private);
			return prefs.Contains(key);
		}
		#endregion

		#region Getting strings from SharedPreferences

		public string StringForKey(string key)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			return prefs.GetString(key,null);
		}

		public static string StringForKey(string key,string settings_name)
		{
			var prefs = Application.Context.GetSharedPreferences (settings_name, FileCreationMode.Private);
			return prefs.GetString (key,null);
		}
		#endregion

		#region Remove

		public void RemoveSettingByKey(string key)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			var edit_prefs = prefs.Edit ();
			edit_prefs.Remove (key);
			edit_prefs.Commit ();

		}

		public void RemoveSettingByKey(string key,string settings_name)
		{
			var prefs = Application.Context.GetSharedPreferences(settings_name, FileCreationMode.Private);
			var edit_prefs = prefs.Edit ();
			edit_prefs.Remove (key);
			edit_prefs.Commit ();

		}
		public void RemoveSettings(string key,string settings_name)
		{
			var prefs = Application.Context.GetSharedPreferences(settings_name, FileCreationMode.Private);
			var edit_prefs = prefs.Edit ();

			edit_prefs.Remove (key);
			edit_prefs.Commit ();

		}
		#endregion

		#region Loading SharedPreferences to Dictionary

		public void SettingsLoadInDictionary(Dictionary<string,object> dic,string settings_name)
		{

			var prefs = Application.Context.GetSharedPreferences(settings_name, FileCreationMode.Private);
			var list = prefs.All;
			foreach (var item in list) 
			{
				dic.Add (item.ToString(),null);
				Console.WriteLine (item.Value);
			}
		}

		#endregion


	}
}

