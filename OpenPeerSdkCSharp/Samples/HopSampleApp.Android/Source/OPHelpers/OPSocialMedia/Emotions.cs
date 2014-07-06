using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using Android.Text;
using Android.Text.Style;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Java.Lang;

namespace HopSampleApp
{
	#region Emotions class
	public static class Emotions
	{
		private static Dictionary<string,int>Smiles;

		#region Emotions constructor
		//Emotions constructor
		static Emotions()
		{
			Smiles=new Dictionary<string, int>();
			Smiles.Add(":-)",Resource.Drawable.shappy);
			Smiles.Add (":)",Resource.Drawable.ssad);
			Smiles.Add ("hookflash", Resource.Drawable.hookflash);
			Smiles.Add ("ios phone", Resource.Drawable.iosphone);
			Smiles.Add ("android", Resource.Drawable.andorid);
		}

		#endregion

		#region Smiles add

		public static bool SmilesAdd(Context context, ISpannable ispan)
		{
			var hasChanges = false;
			foreach (var item in Smiles)
			{
				var smiley = item.Key;
				var smileyImage = item.Value;
				var indices = ispan.ToString ().IndexesOf(smiley);
				foreach (var index in indices )
				{
				
					var set = true;
					foreach (ImageSpan imgspan in ispan.GetSpans(index, index + smiley.Length, Java.Lang.Class.FromType(typeof(ImageSpan))))
					{
						if (ispan.GetSpanStart(imgspan) >= index && ispan.GetSpanEnd(imgspan) <= index + smiley.Length)
							ispan.RemoveSpan(imgspan);
						else
						{
							set = false;
							break;
						}
					}
					if (set)
					{
						hasChanges = true;
						ispan.SetSpan(new ImageSpan(context, smileyImage), index, index + smiley.Length, SpanTypes.ExclusiveExclusive );
					}
				}
			}
			return hasChanges;
		}

		#endregion

		#region Get smiledText

		public static ISpannable GetSmiledText(Context context, ICharSequence text)
		{
			var spannable = SpannableFactory.Instance.NewSpannable(text);
			SmilesAdd(context, spannable);
			return spannable;
		}

		#endregion

		#region Add Smiley

		public static void AddSmiley(string textSmiley, int smileyResource)
		{
			Smiles.Add(textSmiley, smileyResource);
		}

		#endregion
	}

	#endregion

	#region StringExtensions class

	public static class StringExtensions
	{
		#region IndexesOf

		public static IEnumerable<int> IndexesOf(this string haystack, string needle)
		{
			var lastIndex = 0;
			while (true)
			{
				var index = haystack.IndexOf(needle, lastIndex);
				if (index == -1)
				{
					yield break;
				}
				yield return index;
				lastIndex = index + needle.Length;
			}
		}

		#endregion
	}

	#endregion
}

