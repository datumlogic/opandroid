using System;
using System.Diagnostics;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.Views;
using Android.Widget;
using OpenPeerSdk.Helpers;
using HopSampleApp.Services;
using HopSampleApp.Views;
using System.Linq;
using System.Globalization;
using Helpers = OpenPeerSdk.Helpers;
using BitmapType = Android.Graphics.Drawables.BitmapDrawable;


namespace HopSampleApp
{
	namespace Activities
	{
		[LoggerSubsystem("hop_sample_app")]
		class SessionAdapter: BaseAdapter<SessionItem>,ISectionIndexer{
			IList<SessionItem> items;
			Activity context;
			private string[] sections;
			private Java.Lang.Object[] sectionsObject;
			private Dictionary<string,int> alphabetindex;
			public SessionAdapter(Activity context, IList<SessionItem> items)
				: base()
			{
				this.context = context;
				this.items = items;
				this.BuildSectionIndexer();
			}
			private void BuildSectionIndexer()
			{
				this.alphabetindex = new Dictionary<string, int> ();
				for(int i =0;i<items.Count;i++)
				{
					var key = items [i].SesisonUserName[i].ToString();
					if (alphabetindex.ContainsKey (key))
					{
						alphabetindex.Add (key, i);
					}
				}
				this.sections = alphabetindex.Keys.ToArray ();
				this.sectionsObject = new Java.Lang.Object[sections.Length];
				for (int i = 0; i < sections.Length; i++) 
				{
					sectionsObject [i] = new Java.Lang.String (sections [i]);
				}
			}

			public override long GetItemId(int position)
			{
				return position;
			}
			public override SessionItem this[int position]
			{
				get { return items[position]; }
			}

			public override int Count
			{
				get { return items.Count; }
			}
			public override View GetView(int position, View convertView, ViewGroup parent)
			{
				//var item = items[position];
				SocialMediaFeature sm = new SocialMediaFeature ();
				View view = convertView;
				if (view == null) // no view to re-use, create new
					view = context.LayoutInflater.Inflate(Resource.Layout.ListItemSession, null);
				//view.FindViewById<TextView> (Android.Resource.Id.Text1).Text = items[position].SessionMyName;
				view.FindViewById<TextView>(Resource.Id.Name).Text = items[position].SessionMyName;
				view.FindViewById<TextView>(Resource.Id.Username).Text =items[position].SesisonUserName;
				view.FindViewById<TextView> (Resource.Id.SessionDate).Text =sm.Time_stamp(items[position].SessionDate);
				view.FindViewById<ImageView>(Resource.Id.Image).SetImageResource(items[position].UserImg);

				return view;
			}
			public int GetPositionForSection(int section)
			{
				return alphabetindex [sections [section]];
			}
			public int GetSectionForPosition(int position)
			{
				int prevSection = 0;
				for (int i = 0; i < sections.Length; i++) 
				{
					if (GetPositionForSection (i) > position && prevSection < position) 
					{
						prevSection = i;
						break;
					}
				}
				return prevSection;
			}
			public Java.Lang.Object[] GetSections()
			{
				return sectionsObject;
			}
		}
	}
}

