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
	public class ContactAdapter : BaseAdapter<object>
	{
		Activity context;

		public ContactAdapter(Activity context) : base() {
			this.context = context;
		}

		public override long GetItemId(int position)
		{
			return position;
		}

		public override object this[int position] {  
			get { return null; }
		}

		public override int Count {
			get { return 24; }
		}

		public override View GetView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView; // re-use an existing view, if one is available

			if (view == null) { // otherwise create a new one
				view = context.LayoutInflater.Inflate (Resource.Layout.ContactListItem, null);

				TextView badgeTextView = view.FindViewById<TextView> (Resource.Id.badgeAnchorTextView);

				BadgeView badgeView = new BadgeView(context, badgeTextView);
				badgeView.BadgePosition = BadgeView.Position.TopLeft;
				view.Tag = badgeView;
			}

			object source = this [position];

			BadgeView badge = (BadgeView)view.Tag;

			view.FindViewById<TextView> (Resource.Id.nameTextView).Text = "My Name " + position.ToString();
			view.FindViewById<TextView> (Resource.Id.usernameTextView).Text = "Username" + position.ToString();

			badge.Text = position.ToString();
			badge.Show ();

			return view;
		}
	}
}

