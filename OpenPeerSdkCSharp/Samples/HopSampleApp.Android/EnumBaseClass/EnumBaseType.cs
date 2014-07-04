﻿
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
using System.Collections.ObjectModel;

namespace HopSampleApp
{
	public abstract class EnumBaseType<T> where T : EnumBaseType<T>
	{
		protected static List<T> enumValues = new List<T>();

		public readonly int Key;
		public readonly string Value;

		public EnumBaseType(int key, string value)
		{
			Key = key;
			Value = value;
			enumValues.Add((T)this);
		}

		protected static ReadOnlyCollection<T> GetBaseValues()
		{
			return enumValues.AsReadOnly();
		}

		protected static T GetBaseByKey(int key)
		{
			foreach (T t in enumValues)
			{
				if(t.Key == key) return t;
			}
			return null;
		}

		public override string ToString()
		{
			return Value;
		}
	}
}

