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
	class InviteAdapter: BaseAdapter<InviteItemsAcc> {
    List<InviteItemsAcc> items;
    Activity context;
    public InviteAdapter(Activity context, List<InviteItemsAcc> items)
        : base()
    {
        this.context = context;
        this.items = items;
    }
    public override long GetItemId(int position)
    {
        return position;
    }
    public override InviteItemsAcc this[int position]
    {
        get { return items[position]; }
    }
    public override int Count
    {
        get { return items.Count; }
    }
    public override View GetView(int position, View convertView, ViewGroup parent)
    {
        var item = items[position];

        View view = convertView;
        if (view == null) // no view to re-use, create new
            view = context.LayoutInflater.Inflate(Resource.Layout.InviteItem, null);
        view.FindViewById<TextView>(Resource.Id.Name).Text = item.Name +" " + item.Lastname;
        view.FindViewById<TextView>(Resource.Id.Username).Text = item.UserName;
        view.FindViewById<ImageView>(Resource.Id.Image).SetImageResource(item.ImageID);
			if (item.FacebookAccount == true) {
				view.FindViewById<ImageView> (Resource.Id.FacebookIcon).SetImageResource (item.fbImage);
			}
			if (item.LinkedinAccount == true) {
				view.FindViewById<ImageView> (Resource.Id.LinkedinIcon).SetImageResource (item.LinkImage);
			} 
			if (item.TwiterAccount == true) {
				view.FindViewById<ImageView> (Resource.Id.TwiterIcon).SetImageResource (item.twImage);
			}
        return view;
    }
	
	}
}