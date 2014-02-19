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

namespace OpenPeerSampleAppCSharp
{
	[Activity (Label = "Open Peer Sample App - Chat")]			
	public class ChatActivity : ListActivity
	{
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.Chat);

			ListView view = FindViewById<ListView> (Android.Resource.Id.List);
			view.ItemsCanFocus = true;
		
			this.ListAdapter = new ChatAdapter (this);
		}

		protected override void OnListItemClick(ListView l, View v, int position, long id)
		{
			base.OnListItemClick (l, v, position, id);
			Android.Widget.Toast.MakeText(this, "item clicked " + position.ToString(), Android.Widget.ToastLength.Short).Show();
		}

		protected override void OnStop()
		{
			base.OnStop ();
			Console.WriteLine ("stopping");
		}
	}
}

