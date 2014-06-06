
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
				Add(key, value);
			}
		}

		public void Add(string key, object value)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			var prefEditor = prefs.Edit();
			prefEditor.PutString(key, Convert.ToString(value));
			prefEditor.Commit();
		}

		public bool ContainsKey(string key)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			return prefs.Contains(key);
		}
		public string StringForKey(string key)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			return prefs.GetString (key);
		}
		public void RemoveSettingByKey(string key)
		{
			var prefs = Application.Context.GetSharedPreferences("androidsampleapp", FileCreationMode.Private);
			var edit_prefs = prefs.Edit ();
			edit_prefs.Remove (key);
			edit_prefs.Commit ();

		}
		public void Save()
		{

		}
	}
}

