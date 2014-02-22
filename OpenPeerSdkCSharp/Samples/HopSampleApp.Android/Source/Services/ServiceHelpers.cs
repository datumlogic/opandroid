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

using System.Diagnostics.Contracts;

namespace HopSampleApp
{
	namespace Services
	{
		public interface IServiceConnection<ServiceType>
		{
			void OnServiceConnected ( ServiceType service, ComponentName name );
			void OnServiceDisconnected (ComponentName name);
		}

		public class ServiceConnection<ServiceType> : Java.Lang.Object, IServiceConnection, IDisposable where ServiceType : Service
		{
			public bool IsBound { get { return isBound; } }
			public ServiceType Service { get { return service; } }

			protected ServiceType service;
			protected Activity activity;
			protected bool isBound;
			protected IServiceConnection<ServiceType> connection;

			static public ServiceConnection<ServiceType> Bind(Activity activity, Android.Content.Bind bindFlags = Android.Content.Bind.AutoCreate)
			{
				return Bind (activity, null, bindFlags);
			}

			static public ServiceConnection<ServiceType> Bind(Activity activity, IServiceConnection<ServiceType> altConnection, Android.Content.Bind bindFlags = Android.Content.Bind.AutoCreate)
			{
				Intent intent = new Intent (activity, typeof (ServiceType));

				var realConnection = new ServiceConnection<ServiceType>(activity, altConnection);
				activity.BindService (intent, realConnection, bindFlags);
				return realConnection;
			}

			protected ServiceConnection (Activity activity, IServiceConnection<ServiceType> altConnection)
			{
				if (null != altConnection)
					this.connection = altConnection;
				else
					this.connection = activity as IServiceConnection<ServiceType>;
			}

			void IDisposable.Dispose ()
			{
				if (isBound) {
					activity.UnbindService (this);
					isBound = false;
				}
			}

			void IServiceConnection.OnServiceConnected (ComponentName name, IBinder service)
			{
				ServiceBinder<ServiceType> serviceBinder = service as ServiceBinder<ServiceType>;
				if (serviceBinder == null) {
					return;
				}

				this.service = serviceBinder.Service;
				this.isBound = true;
				if (this.connection != null) {
					this.connection.OnServiceConnected (this.service, name);
				}
			}

			void IServiceConnection.OnServiceDisconnected (ComponentName name)
			{
				if (connection != null) {
					this.connection.OnServiceDisconnected (name);
				}
				this.connection = null;
				isBound = false;
			}
		}

		public class ServiceBinder<ServiceType> : Binder
		{
			public ServiceType Service { get {return service;} }

			protected ServiceType service;

			public ServiceBinder (ServiceType service)
			{
				this.service = service;
			}

		}
	}
}

