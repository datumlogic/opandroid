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
	public class SettingsAdapter : BaseAdapter<object>
	{
		Activity context;

		enum Setting
		{
			StandardOutLoggerSwitch,
			OutgoingTelnetLoggerSwitch,
			OutgoingTelnetLoggerServer,
			TelnetLoggerSwitch,
			TelnetLoggerPort,

			Total
		}

		enum ListItemType
		{
			Switch,
			TextEdit,
			Spinner,

			Total
		}

		class ViewHolder : Java.Lang.Object
		{
			public TextView LabelTextView { get; set; }
		}

		class SwitchViewHolder : ViewHolder
		{
			public ToggleButton ToggleButton { get; set; }
		}

		class TextEditViewHolder : ViewHolder
		{
			public EditText EditText { get; set; }
		}

		class SpinnerViewHolder : ViewHolder
		{
			public Spinner Spinner { get; set; }
		}

		public SettingsAdapter(Activity context) : base() {
			this.context = context;
		}

		public override long GetItemId(int position)
		{
			return position;
		}

		public override int GetItemViewType (int position)
		{
			switch ((Setting)position) {
			case Setting.StandardOutLoggerSwitch:		return (int)ListItemType.Switch;
			case Setting.OutgoingTelnetLoggerSwitch:	return (int)ListItemType.Switch;
			case Setting.OutgoingTelnetLoggerServer:	return (int)ListItemType.TextEdit;
			case Setting.TelnetLoggerSwitch:			return (int)ListItemType.Switch;
			case Setting.TelnetLoggerPort:				return (int)ListItemType.TextEdit;
			default:									break;					
			}
			return (int)ListItemType.Spinner;
		}

		public override object this[int position] {  
			get { return null; }
		}

		public override int Count {
			get { return 24; }
		}

		public override int ViewTypeCount{
			get { return (int)ListItemType.Total; }
		}

		public override View GetView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView; // re-use an existing view, if one is available

			ViewHolder holder = null;
			SwitchViewHolder switchHolder = null;
			TextEditViewHolder editHolder = null;
			SpinnerViewHolder spinnerHolder = null;

			ListItemType @type = (ListItemType)GetItemViewType (position);

			if (view == null) { // otherwise create a new one
				switch (@type) {
				case ListItemType.Switch:
					view = context.LayoutInflater.Inflate (Resource.Layout.SettingsToggleListItem, null);
					holder = switchHolder = new SwitchViewHolder ();
					switchHolder.ToggleButton = view.FindViewById<ToggleButton> (Resource.Id.toggleButton);
					break;
				case ListItemType.TextEdit:
					view = context.LayoutInflater.Inflate (Resource.Layout.SettingsTextEditListItem, null);
					holder = editHolder = new TextEditViewHolder ();
					editHolder.EditText = view.FindViewById<EditText> (Resource.Id.editText);
					break;
				case ListItemType.Spinner:
					view = context.LayoutInflater.Inflate (Resource.Layout.SettingsSpinnerListItem, null);
					holder = spinnerHolder = new SpinnerViewHolder ();
					spinnerHolder.Spinner = view.FindViewById<Spinner> (Resource.Id.valueSpinner);

					var adapter = ArrayAdapter.CreateFromResource (context, Resource.Array.log_levels_array, Android.Resource.Layout.SimpleSpinnerItem);
					adapter.SetDropDownViewResource (Android.Resource.Layout.SimpleSpinnerDropDownItem);
					spinnerHolder.Spinner.Adapter = adapter;
					spinnerHolder.Spinner.ItemSelected += new EventHandler<AdapterView.ItemSelectedEventArgs> (spinner_ItemSelected);
					break;
				default:
					throw new NotImplementedException();
				}

				holder.LabelTextView = view.FindViewById<TextView> (Resource.Id.labelTextView);

				view.Tag = holder;
			} else {
				holder = (ViewHolder)view.Tag;
				switch (@type) {
				case ListItemType.Switch:
					holder = switchHolder = (SwitchViewHolder)view.Tag;
					break;
				case ListItemType.TextEdit:
					holder = editHolder = (TextEditViewHolder)view.Tag;
					break;
				case ListItemType.Spinner:
					holder = spinnerHolder = (SpinnerViewHolder)view.Tag;
					break;
				default:
					throw new NotImplementedException();
				}
			}

			object source = this [position];

			switch ((Setting)position) {
			case Setting.StandardOutLoggerSwitch:
				holder.LabelTextView.Text = context.Resources.GetString(Resource.String.standard_out_logger);
				break;
			case Setting.OutgoingTelnetLoggerSwitch:
				holder.LabelTextView.Text = context.Resources.GetString(Resource.String.outgoing_telnet_logger);
				break;
			case Setting.OutgoingTelnetLoggerServer:
				holder.LabelTextView.Text = context.Resources.GetString(Resource.String.outgoing_telnet_logger_server_prompt);
				editHolder.EditText.Text = "logger.foo.com:5900";
				break;
			case Setting.TelnetLoggerSwitch:
				holder.LabelTextView.Text = context.Resources.GetString(Resource.String.telnet_logger);
				break;
			case Setting.TelnetLoggerPort:
				holder.LabelTextView.Text = context.Resources.GetString(Resource.String.telnet_logger_port_prompt);
				editHolder.EditText.Text = "59999";
				break;
			default:
				holder.LabelTextView.Text = "Log Level Module " + position.ToString ();
				break;
			}

			return view;
		}

		private void spinner_ItemSelected (object sender, AdapterView.ItemSelectedEventArgs e)
		{
			Spinner spinner = (Spinner)sender;

			string toast = string.Format ("The value is {0}", spinner.GetItemAtPosition (e.Position));
			Toast.MakeText (context, toast, ToastLength.Long).Show ();
		}
	}
}

