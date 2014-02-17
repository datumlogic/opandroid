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
	[Activity (Label = "SettingsActivity")]			
	public class SettingsActivity : ListActivity
	{
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.Settings);

			this.ListAdapter = new SettingsAdapter (this);
		}

		protected override void OnListItemClick(ListView l, View v, int position, long id)
		{
			base.OnListItemClick (l, v, position, id);
			Android.Widget.Toast.MakeText(this, "setting " + position.ToString(), Android.Widget.ToastLength.Short).Show();
		}
	}
}

