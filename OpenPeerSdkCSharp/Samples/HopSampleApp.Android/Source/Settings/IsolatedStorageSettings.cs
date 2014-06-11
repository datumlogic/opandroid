using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
//using System.Windows;
using Android.OS;
using System.IO;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp
{
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

		public void AddSettings(string key, object value)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			var prefEditor = prefs.Edit();
			prefEditor.PutString(key, Convert.ToString(value));
			prefEditor.Commit();
		}

		public void AddSettingsToSpecificSettings(string settings_name,string key, object value)
		{
			var prefs = Application.Context.GetSharedPreferences(settings_name, FileCreationMode.Private);
			var prefEditor = prefs.Edit();
			prefEditor.PutString(key, Convert.ToString(value));
			prefEditor.Commit();
		}
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

		public string StringForKey(string key)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			return prefs.GetString(key,null);
		}

		public string StringForKey(string key,string settings_name)
		{
			var prefs = Application.Context.GetSharedPreferences (settings_name, FileCreationMode.Private);
			return prefs.GetString (key,null);
		}
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



	}
}

