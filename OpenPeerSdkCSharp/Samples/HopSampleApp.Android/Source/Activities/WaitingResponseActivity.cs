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
using Android.Graphics.Drawables;
namespace HopSampleApp
{
	[Activity (Theme = "@style/Theme.Splash",MainLauncher = false,NoHistory = true,Icon="@drawable/op",ScreenOrientation = Android.Content.PM.ScreenOrientation.Portrait)]			
	public class WaitingResponseActivity : Activity
	{
		AnimationDrawable _LoadingDrawable;
		AnimationDrawable _SkyDrawable;
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			// Create your application here
			SetContentView (Resource.Layout.WaitingResponse);

			//Loading Animacion
			_SkyDrawable=(Android.Graphics.Drawables.AnimationDrawable)Resources.GetDrawable(Resource.Drawable.animacionskybg);
			_LoadingDrawable = (Android.Graphics.Drawables.AnimationDrawable)Resources.GetDrawable(Resource.Drawable.animacionshapes);

			ImageView LoadingImage = FindViewById<ImageView>(Resource.Id.LoadingImage);
			LoadingImage.SetImageDrawable((Android.Graphics.Drawables.Drawable) _LoadingDrawable);
			_LoadingDrawable.Start ();

			RelativeLayout LayoutBg = FindViewById<RelativeLayout> (Resource.Id.LayoutBg);
			LayoutBg.SetBackgroundDrawable ((Android.Graphics.Drawables.Drawable) _SkyDrawable);
			_SkyDrawable.Start ();

		}
	}
}

