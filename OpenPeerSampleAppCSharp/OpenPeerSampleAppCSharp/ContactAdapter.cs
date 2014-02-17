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

		class ViewHolder : Java.Lang.Object
		{
			public BadgeView BadgeView { get; set; }
			public TextView NameTextView { get; set; }
			public TextView UsernameTextView { get; set; }
		}

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

			ViewHolder holder;

			if (view == null) { // otherwise create a new one
				view = context.LayoutInflater.Inflate (Resource.Layout.ContactListItem, null);

				TextView badgeTextView = view.FindViewById<TextView> (Resource.Id.badgeAnchorTextView);

				BadgeView badgeView = new BadgeView (context, badgeTextView);
				badgeView.BadgePosition = BadgeView.Position.TopLeft;

				holder = new ViewHolder ();
				holder.BadgeView = badgeView;
				holder.NameTextView = view.FindViewById<TextView> (Resource.Id.nameTextView);
				holder.UsernameTextView = view.FindViewById<TextView> (Resource.Id.usernameTextView);
				view.Tag = holder;
			} else {
				holder = (ViewHolder)view.Tag;
			}

			object source = this [position];

			holder.NameTextView.Text = "My Name " + position.ToString();
			holder.UsernameTextView.Text = "Username" + position.ToString();

			holder.BadgeView.Text = position.ToString();
			holder.BadgeView.Show ();

			return view;
		}
	}
}

