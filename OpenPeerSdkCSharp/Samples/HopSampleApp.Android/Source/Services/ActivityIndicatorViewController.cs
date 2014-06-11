
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.Appwidget;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp
{
	class ActivityIndicatorViewController
	{

		//public UIActivityIndicatorView ActivityIndicator {get; set;}


		public EditText ActivityLabel {get; set;}

		private static ActivityIndicatorViewController instance;
		private ActivityIndicatorViewController()
		{

		}
		public static ActivityIndicatorViewController SharedActivityIndicator()
		{
			if (instance == null)
				instance = new ActivityIndicatorViewController();
			return instance;
		}

		void viewDidLoad()
		{

		}

		void didReceiveMemoryWarning()
		{

		}

		public void showActivityIndicatorWithTextInView(bool show, string text, View inView)
		{

			if (show)
			{

			}

		}

	}
}

