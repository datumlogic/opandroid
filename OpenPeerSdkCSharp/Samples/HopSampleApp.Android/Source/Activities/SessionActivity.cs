using System;
using System.Collections.Generic;
using System.Collections;
using Android.App;
using Android.Content;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.OS;
using Android.Views;
using Android.Runtime;
using System.Linq;
using Android.Widget;
using OpenPeerSdk.Helpers;
using PullToRefresharp.Android.Views;
using HopSampleApp.Services;
using BitmapType = Android.Graphics.Drawables.BitmapDrawable;
using HopSampleApp.Views;
using HopSampleApp.Activities;


namespace HopSampleApp
{
	[LoggerSubsystem("hop_sample_app")]
	[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op")]				
	public class SessionActivity : Activity //ListActivity
	{
		List<SessionItem> UsersSessions = new List<SessionItem>();
		ListView listView;
		//
		SocialMediaFeature sm=new SocialMediaFeature();

		protected override void OnCreate (Bundle bundle)
		{

			base.OnCreate (bundle);
			SetContentView (Resource.Layout.SessionLayout);
			listView = FindViewById<ListView> (Resource.Id.SessionList);
			Button Search = FindViewById<Button> (Resource.Id.SearchButton);
			EditText SearchKeyword = FindViewById<EditText> (Resource.Id.SearchSessionItem);


		  

				//Populate session list with users
			/*
              UsersSessions.Add (new SessionItem
				{
					Id = User Id
					SessionDate = Session Date
					SessionTypeName = Session Type Video Call,Voice Call,Chat etc.
					SessionTime = Modern time stamp ex.usage (sm.Time_stamp(new DateTime(2014,4,20))),
					SesisonUserName = username of user
					SessionMyName = user full name or nickname
				});


			*/
			UsersSessions.Add (new SessionItem
				{
					Id = 0,
					SessionDate = new DateTime(2014,4,20),
					SessionTypeName = "Video Call",
					SessionTime = sm.Time_stamp(new DateTime(2014,4,20)),
					SesisonUserName = "petar-hookflash",
					SessionMyName = "Petar"
				});
			UsersSessions.Add (new SessionItem
				{
					Id = 1,
					SessionDate = new DateTime(2014,5,07),
					SessionTypeName = "Video Call",
					SessionTime = sm.Time_stamp(new DateTime(2014,5,07)),
					SesisonUserName = "sergej-hookflash",
					SessionMyName = "Sergej"
				
				});
			UsersSessions.Add (new SessionItem
				{
					Id = 2,
					SessionDate = new DateTime(2014,5,06),
					SessionTypeName = "Voice Call",
					SessionTime = sm.Time_stamp(new DateTime(2014,5,06)),
					SesisonUserName = "robin-hookflash",
					SessionMyName = "Robin"
				});
			UsersSessions.Add (new SessionItem
				{
					Id=3,
					SessionDate = new DateTime(2014,5,06),
					SessionTypeName = "Voice Call",
					SessionTime = sm.Time_stamp(new DateTime(2014,5,06)),
					SesisonUserName = "marko-hookflash",
					SessionMyName = "Marko"
				});
			UsersSessions.Add (new SessionItem
				{
					Id=4,
					SessionDate = new DateTime(2014,5,06),
					SessionTypeName = "Chat",
					SessionTime = sm.Time_stamp(new DateTime(2014,5,06)),
					SesisonUserName = "adrijano-hookflash",
					SessionMyName = "Adrijano"
				});
			UsersSessions.Add (new SessionItem
				{
					Id=5,
					SessionDate = new DateTime(2014,3,06),
					SessionTypeName = "Chat",
					SessionTime = sm.Time_stamp(new DateTime(2014,3,06)),
					SesisonUserName = "bojan-hookflash",
					SessionMyName = "Bojan"
				});
			UsersSessions.Add (new SessionItem
				{
					Id=6,
					SessionDate = new DateTime(2014,5,06),
					SessionTypeName = "Voice Call",
					SessionTime = sm.Time_stamp(new DateTime(2014,5,06)),
					SesisonUserName = "eric-hookflash",
					SessionMyName = "Eric"
				});
			UsersSessions.Add (new SessionItem
				{
					Id=7,
					SessionDate = new DateTime(2014,5,06),
					SessionTypeName = "Voice Call",
					SessionTime = sm.Time_stamp(new DateTime(2014,5,06)),
					SesisonUserName = "eric-hookflash",
					SessionMyName = "Eric"
				});
			UsersSessions.Add (new SessionItem
				{
					Id=8,
					SessionDate = new DateTime(2014,5,06),
					SessionTypeName = "Voice Call",
					SessionTime = sm.Time_stamp(new DateTime(2014,5,06)),
					SesisonUserName = "df-hookflash",
					SessionMyName = "df"
				});
			//Sorting session by session date and session type.
			var SortingByDateAndType = UsersSessions.Where (s_date => s_date.SessionDate == s_date.SessionDate).OrderBy (s_type => s_type.SessionTypeName == s_type.SessionTypeName).ToList ();
			//Search button
			Search.Click += delegate 
			{
				if(SearchKeyword.Text !=String.Empty)
				{
					//Search session items by SearchKeyword.text string
					listView.Adapter=new SessionAdapter(this,SortingByDateAndType.Where(search=>search.SesisonUserName.Contains(SearchKeyword.Text)).ToList());

				}
				else
				{
					//Populate all session items in list
					listView.Adapter = new SessionAdapter(this,SortingByDateAndType);
				}
			};
			SearchKeyword.TextChanged += delegate
			{
				//if SearchKeyword empty populate all session items
				if(SearchKeyword.Text==String.Empty)
				{
					listView.Adapter = new SessionAdapter(this,SortingByDateAndType);
				}
			};

			//Load session item on screen load
			listView.Adapter = new SessionAdapter(this,UsersSessions);
			//listView.SmoothScrollbarEnabled = true;
			listView.FastScrollAlwaysVisible = true;
			listView.FastScrollEnabled = true;
			//Printing lambda expressions in console to see is selecting good.
			foreach (var item in SortingByDateAndType.ToList()) {
				Console.WriteLine (String.Format ("Result: {0} - {1} - {2}", item.SessionDate, item.SessionTypeName,item.SesisonUserName));
			}

		}

				

	}
}

