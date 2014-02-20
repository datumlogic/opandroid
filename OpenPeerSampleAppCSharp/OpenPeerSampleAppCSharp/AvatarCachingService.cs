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
using System.Diagnostics;

using System.Diagnostics.Contracts;

using Debug = System.Diagnostics.Debug;


namespace OpenPeerSampleAppCSharp
{
	namespace Services
	{
		[Service]
		class AvatarCachingService : Service
		{
			public override StartCommandResult OnStartCommand (Android.Content.Intent intent, StartCommandFlags flags, int startId)
			{
				Debug.Write ("started");

				return StartCommandResult.Sticky;
			}	

			public override void OnCreate ()
			{
				Debug.Write ("OnCreate");

				base.OnCreate ();
			}

			public override void OnDestroy ()
			{
				Debug.Write ("OnDestroy");

				base.OnDestroy ();
			}

			public override IBinder OnBind (Intent Intent)
			{
				var binder = new ServiceBinder<AvatarCachingService> (this);
				return binder;
			}
		}
	}
}

