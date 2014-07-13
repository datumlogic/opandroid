
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
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;

namespace HopSampleApp
{
	[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op")]		
	public class AndroidLookContactActivity : Activity
	{
		List<AndroidContactItem> ContactsItems = new List<AndroidContactItem>();
		ListView listView;
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			SetContentView(Resource.Layout.AndroidLookContactLayout);
			listView = FindViewById<ListView>(Resource.Id.conList);	
			Button Search = FindViewById<Button> (Resource.Id.SearchContactsButton);
			EditText SearchKeyword = FindViewById<EditText> (Resource.Id.SearchContactItem);
			// Create your application here

			//LoginManager.mConvThreadDelegate = new CSOPConversationThreadDelegate();
			//LoginManager.mCallbackHandler.RegisterConversationThreadDelegate(LoginManager.mConvThreadDelegate);

			foreach (OPRolodexContact contact in LoginManager.mRolodexContact)
			{		
				contact.PrintInfo();
				ContactsItems.Add (new AndroidContactItem() 
					{
						UserName=contact.Name,
						IdentityUri=contact.IdentityURI,
						avatar=contact.Avatars.ElementAt(0).URL
										   
					});

			}
			var ConSearch = ContactsItems.Where (s_user =>s_user.UserName==s_user.UserName).ToList ();

			//Search button
			Search.Click += delegate 
			{
				if(SearchKeyword.Text !=String.Empty)
				{
					//Search session items by SearchKeyword.text string
					listView.Adapter=new AndroidLookContactAdapter(this,ConSearch.Where(txt=>txt.UserName.Contains(SearchKeyword.Text)).ToList());

				}
				else
				{
					//Populate all session items in list
					listView.Adapter = new AndroidLookContactAdapter(this,ConSearch);
				}
			};

			SearchKeyword.TextChanged += delegate
			{
				//if SearchKeyword empty populate all session items
				if(SearchKeyword.Text==String.Empty)
				{
					listView.Adapter = new AndroidLookContactAdapter(this,ConSearch.Where(txt=>txt.UserName.Contains(SearchKeyword.Text)).ToList());
				}
			};
			listView.Adapter = new AndroidLookContactAdapter(this, ContactsItems);
		}
	}
}

