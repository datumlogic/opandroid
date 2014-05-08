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
	[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op")]			
	public class InviteActivity : Activity
	{
		List<InviteItemsAcc> UsersInviteItems = new List<InviteItemsAcc>();
		ListView listView;
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			SetContentView(Resource.Layout.InviteLayout);
			listView = FindViewById<ListView>(Resource.Id.InviteList);	
			// Create your application here
			UsersInviteItems.Add(
				new InviteItemsAcc() 
			                     {
				Name="Marko",
				Lastname="Marusic",
				UserName="markisa",
				ImageID=Resource.Drawable.person,
				FacebookAccount=true,
				fbImage=Resource.Drawable.facebook,
				TwiterAccount=false,
				twImage=Resource.Drawable.twiter,
				LinkedinAccount=true ,
				LinkImage=Resource.Drawable.linkedin
			});
			UsersInviteItems.Add(
				new InviteItemsAcc() 
				{
				Name="Janko",
				Lastname="Jankovic",
				UserName="janko87",
				ImageID=Resource.Drawable.remote,
				FacebookAccount=true,
				fbImage=Resource.Drawable.facebook,
				TwiterAccount=true,
				twImage=Resource.Drawable.twiter,
				LinkedinAccount=true,
				LinkImage=Resource.Drawable.linkedin
			});
			UsersInviteItems.Add(
				new InviteItemsAcc() 
				{
				Name="Stevan",
				ImageID=Resource.Drawable.robin,
				Lastname="Stevanovic",
				UserName="steva-stev89",
				FacebookAccount=false,
				fbImage=Resource.Drawable.facebook,
				TwiterAccount=false,
				twImage=Resource.Drawable.twiter,
				LinkedinAccount=true,
				LinkImage=Resource.Drawable.linkedin
			    
			});

			listView.Adapter = new InviteAdapter(this, UsersInviteItems);
}
	}}
