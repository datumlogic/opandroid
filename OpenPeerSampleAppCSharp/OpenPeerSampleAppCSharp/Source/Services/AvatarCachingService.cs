using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.Graphics;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using System.Diagnostics;
using System.Diagnostics.Contracts;
using System.IO;
using System.Security.Cryptography;

using OpenPeerSampleAppCSharp.Helpers;


using Debug = System.Diagnostics.Debug;
using BitmapWeak = OpenPeerSampleAppCSharp.Helpers.WeakReference<Android.Graphics.Bitmap>;


namespace OpenPeerSampleAppCSharp
{
	namespace Services
	{
		[Service]
		class AvatarCachingService : Service
		{
			public static string CachePath {
				get {
					string localFolder = System.Environment.GetFolderPath (System.Environment.SpecialFolder.LocalApplicationData);
					return System.IO.Path.Combine (localFolder, "avatar_cache");
				}
			}

			public static string CalculateUrlHash (string url)
			{
				return (BitConverter.ToString (SHA256.Create (url).Hash ) ).ToLower ();
			}

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
				return new ServiceBinder<AvatarCachingService> (this);
			}

			void EnsureCachePathExists ()
			{
				string path = AvatarCachingService.CachePath;
				if (!Directory.Exists (path)) {
					Directory.CreateDirectory (path);
				}
			}

			class CacheFile
			{
				public string Url { get; set; }
				public bool HasLocalFile { get; set; }
				public bool DownloadFailed { get; set; }

				public bool IsDownloading {
					get {
						return !this.HasLocalFile && !this.DownloadFailed;
					}
				}

				public string LocalFilePath {
					get {
						Contract.Assert (Url != null);
						return System.IO.Path.Combine (AvatarCachingService.CachePath,  AvatarCachingService.CalculateUrlHash(Url));
					}
				}
			}

			class CacheEntryKey
			{
				public string Url { get; set; }
				public int MaxWidth { get; set; }
				public int MaxHeight { get; set; }

				public override int GetHashCode ()
				{
					int hash = 17;
					hash = hash * 23 + Url.GetHashCode ();
					hash = hash * 23 + MaxWidth.GetHashCode ();
					hash = hash * 23 + MaxHeight.GetHashCode ();
					return hash;
				}

				public override bool Equals(object obj)
				{
					if (obj == null)
						return this == null;

					if (! (obj is CacheEntryKey))
						return false;

					CacheEntryKey item = obj as CacheEntryKey;

					if (!ReferenceEquals (Url, item.Url))
						return false;

					if (MaxWidth != item.MaxWidth)
						return false;

					if (MaxHeight != item.MaxHeight)
						return false;

					return true;
				}
			}

			class CacheEntryValue
			{
				CacheFile CacheFile { get; set; }

				public BitmapWeak Bitmap { get; set; }
			}

		}
	}
}

