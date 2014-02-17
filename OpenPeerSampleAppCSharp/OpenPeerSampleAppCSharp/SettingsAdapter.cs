using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics.Contracts;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Text;

namespace OpenPeerSampleAppCSharp
{
	public class SettingsAdapter : BaseAdapter<object>
	{
		Activity context;

		string [] subsystemFriendlyNames;
		string [] subsystemInternalNames;
		EventHandler<AdapterView.ItemSelectedEventArgs> spinnerItemSelectedEventHandler;// = new EventHandler<AdapterView.ItemSelectedEventArgs> (spinner_ItemSelected);
		EventHandler<Android.Text.TextChangedEventArgs> textChangedEventHandler;
		EventHandler<View.FocusChangeEventArgs> focusChangedEventHandler;

		class SubsystemHolder : Java.Lang.Object
		{
			public string SubsystemFriendlyName { get; set; }
			public string SubsystemName { get; set; }

			public SubsystemHolder(
				string subsystemFriendlyName,
				string subsystemName
			)
			{
				this.SubsystemFriendlyName = subsystemFriendlyName;
				this.SubsystemName = subsystemName;
			}
		}

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

		public SettingsAdapter(Activity context) : base()
		{
			this.context = context;
			this.spinnerItemSelectedEventHandler = new EventHandler<AdapterView.ItemSelectedEventArgs> (spinner_ItemSelected);
			this.textChangedEventHandler = new EventHandler<Android.Text.TextChangedEventArgs> (edittext_TextChanged);
			this.focusChangedEventHandler = new EventHandler<View.FocusChangeEventArgs> (view_FocusChanged);

			subsystemFriendlyNames = context.Resources.GetTextArray (Resource.Array.log_subsystem_friend_names_array);
			subsystemInternalNames = context.Resources.GetTextArray (Resource.Array.log_subsystem_array);

			Contract.Assert (subsystemFriendlyNames.Length == subsystemInternalNames.Length);
			Contract.Assert (subsystemFriendlyNames.Length > 0);
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
			get {
				if (position < (int)Setting.Total) {
					return null;
				}

				return new SubsystemHolder(
					subsystemFriendlyNames[position - (int)Setting.Total],
					subsystemInternalNames[position - (int)Setting.Total]
				);
			}
		}

		public override int Count {
			get { return ((int)Setting.Total) + subsystemFriendlyNames.Length; }
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
					editHolder.EditText.TextChanged += textChangedEventHandler;
					editHolder.EditText.FocusChange += view_FocusChanged;
					break;
				case ListItemType.Spinner:
					view = context.LayoutInflater.Inflate (Resource.Layout.SettingsSpinnerListItem, null);
					holder = spinnerHolder = new SpinnerViewHolder ();
					spinnerHolder.Spinner = view.FindViewById<Spinner> (Resource.Id.valueSpinner);

					var adapter = ArrayAdapter.CreateFromResource (context, Resource.Array.log_levels_array, Android.Resource.Layout.SimpleSpinnerItem);
					adapter.SetDropDownViewResource (Android.Resource.Layout.SimpleSpinnerDropDownItem);
					spinnerHolder.Spinner.Adapter = adapter;
					spinnerHolder.Spinner.ItemSelected += spinnerItemSelectedEventHandler;
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
				SubsystemHolder boxer = (SubsystemHolder)this [position];
				holder.LabelTextView.Text = boxer.SubsystemFriendlyName;
				spinnerHolder.Spinner.Prompt = boxer.SubsystemFriendlyName;
				spinnerHolder.Spinner.Tag = boxer;
				spinnerHolder.Spinner.SetSelection (position % (spinnerHolder.Spinner.Adapter.Count));
				break;
			}

			return view;
		}

		private void spinner_ItemSelected (object sender, AdapterView.ItemSelectedEventArgs e)
		{
			Spinner spinner = (Spinner)sender;

			SubsystemHolder boxer = (SubsystemHolder)spinner.Tag;

			string output = string.Format ("Setting {0} ({1}) to {2}", boxer.SubsystemFriendlyName, boxer.SubsystemName, spinner.GetItemAtPosition (e.Position));
			Console.WriteLine (output);
		}

		private void edittext_TextChanged (object sender, Android.Text.TextChangedEventArgs e)
		{
			EditText editText = (EditText)sender;

			string value = e.Text.ToString();

			string output = string.Format ("TextEdit value as {0}", value);
			Console.WriteLine (output);
		}

		private void view_FocusChanged (object sender, View.FocusChangeEventArgs e)
		{
			EditText editText = (EditText)sender;

			bool hasFocus = e.HasFocus;

			string output = string.Format ("Focus value as {0}", hasFocus.ToString());
			Console.WriteLine (output);
		}
	}
}

