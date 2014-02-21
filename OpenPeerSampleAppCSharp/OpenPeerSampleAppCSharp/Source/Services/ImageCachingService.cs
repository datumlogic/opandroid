using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Diagnostics.Contracts;
using System.IO;
using System.Linq;
using System.Net;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

using Android.App;
using Android.Content;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

using OpenPeerSampleAppCSharp.Helpers;

using BitmapType = Android.Graphics.Drawables.BitmapDrawable;
using BitmapWeak = OpenPeerSampleAppCSharp.Helpers.WeakReference<Android.Graphics.Drawables.BitmapDrawable>;
using Debug = System.Diagnostics.Debug;


namespace OpenPeerSampleAppCSharp
{
	namespace Services
	{
		public interface IImageCachingDownloader
		{
			BitmapType FetchNowOrAsyncDownload (string url, int width, int height, Action<BitmapType> callback);
		}

		public class ImageCachingServiceDownloader : IImageCachingDownloader, IServiceConnection<ImageCachingService>
		{
			ImageCachingService service;
			List<PendingDownload> pendingDownload = new List<PendingDownload> ();

			struct PendingDownload
			{
				public string Url;
				public int MaxWidth;
				public int MaxHeight;
				public Action<BitmapType> Callback;

				public PendingDownload(string url, int maxWidth, int maxHeight, Action<BitmapType> callback) {
					this.Url = url;
					this.MaxWidth = maxWidth;
					this.MaxHeight = maxHeight;
					this.Callback = callback;
				}
			}

			public ImageCachingServiceDownloader ()
			{
			}

			BitmapType IImageCachingDownloader.FetchNowOrAsyncDownload (string url, int maxWidth, int maxHeight, Action<BitmapType> callback)
			{
				if (null != this.service) {
					ImageCachingService.OutBitmap instantResult = new ImageCachingService.OutBitmap ();
					this.service.DownloadImageAsync (url, maxWidth, maxHeight, callback, instantResult);

					return instantResult.Bitmap;
				}

				pendingDownload.Add( new PendingDownload (url, maxWidth, maxHeight, callback));
				DownloadNow ();

				return null;
			}

			void IServiceConnection<ImageCachingService>.OnServiceConnected ( ImageCachingService service, ComponentName name )
			{
				this.service = service;

				DownloadNow ();
			}

			void IServiceConnection<ImageCachingService>.OnServiceDisconnected (ComponentName name)
			{
				this.service = null;
			}

			protected void DownloadNow ()
			{
				if (this.service == null)
					return;

				foreach (PendingDownload pending in pendingDownload) {
					ImageCachingService.OutBitmap instantResult = new ImageCachingService.OutBitmap ();
					this.service.DownloadImageAsync (pending.Url, pending.MaxWidth, pending.MaxHeight, pending.Callback, instantResult);

					if (null != instantResult.Bitmap) {
						pending.Callback (instantResult.Bitmap);
					}
				}
			}

		}

		[Service]
		public class ImageCachingService : Service
		{
			public class OutBitmap
			{
				public BitmapType Bitmap { get; set; }
			}

			Handler handler = new Handler (Looper.MainLooper);

			Dictionary<string, CacheFile> cachedFiles = new Dictionary<string, CacheFile> ();
			Dictionary<CacheKey, CacheValue> cachedBitmaps = new Dictionary<CacheKey, CacheValue> ();

			public string CachePath {
				get {
					string cacheFolder = this.CacheDir.AbsolutePath;
					//					string localFolder = System.Environment.GetFolderPath (System.Environment.SpecialFolder.LocalApplicationData);
					return System.IO.Path.Combine (cacheFolder, "image_cache");
				}
			}

			public static string CalculateUrlHash (string url)
			{
				string result;

				using (SHA256 hasher = SHA256Managed.Create ()) {
					Encoding encoder = Encoding.UTF8;
					byte[] output = hasher.ComputeHash(encoder.GetBytes(url));

					result = BitConverter.ToString (output).Replace("-", string.Empty).ToLower ();
				}

				return result;
			}

			public override StartCommandResult OnStartCommand (Android.Content.Intent intent, StartCommandFlags flags, int startId)
			{
				Debug.WriteLine ("started");

				return StartCommandResult.Sticky;
			}	

			public override void OnCreate ()
			{
				Debug.WriteLine ("OnCreate");

				base.OnCreate ();

				lock (this) {
					EnsureCachePathExists ();
				}
			}

			public override void OnDestroy ()
			{
				Debug.WriteLine ("OnDestroy");

				base.OnDestroy ();
			}

			public override IBinder OnBind (Intent Intent)
			{
				return new ServiceBinder<ImageCachingService> (this);
			}

			public async void DownloadImageAsync (string url, int? maxWidth, int? maxHeight, Action<BitmapType> callbackOnUiThread, OutBitmap outCachedResult = null)
			{
				Contract.Assert (!String.IsNullOrEmpty (url));

				CacheFile file;
				CacheValue value;

				lock (this) {
					if (!cachedFiles.TryGetValue (url, out file)) {
						file = new CacheFile (url, this.CachePath);

						cachedFiles [url] = file;
					} else {
						Debug.WriteLine ("Cached file is already present, url=" + url);
					}

					// create a custom bitmap loader key
					CacheKey key = new CacheKey (url, maxWidth == null ? -1 : (int)maxWidth, maxHeight == null ? -1 : (int)maxHeight);

					if (!cachedBitmaps.TryGetValue (key, out value)) {
						value = new CacheValue (key);

						cachedBitmaps [key] = value;
					} else {
						Debug.WriteLine ("Cached bitmap key is already present, url=" + url);

						BitmapType existingBitmap = value.Bitmap;
						if (null != existingBitmap) {
							if (null != outCachedResult) {
								Debug.WriteLine ("Cached bitmap shortcircuited result, url=" + url);

								outCachedResult.Bitmap = existingBitmap;
								return;
							}
						}
					}

					file.AddToNotifyList (value);
					value.NotifyOnceReady (callbackOnUiThread);
				}

				await file.Download (HandleDownloaded);
			}

			private void EnsureCachePathExists ()
			{
				string path = this.CachePath;
				if (!Directory.Exists (path)) {
					Directory.CreateDirectory (path);
				}
			}

			private async void HandleDownloaded (CacheFile file)
			{
				List<CacheValue> pending = file.GetAllPendingNotifies ();

				var tasks = new List<Task> ();
				foreach (CacheValue value in pending) {
					tasks.Add( value.HandleDownloaded (file, this.HandleFileLoaded) );
				}
				await Task.WhenAll (tasks);
			}

			private void HandleFileLoaded (CacheValue value, BitmapType bitmap)
			{
				var pending = value.GetPendingCallbacks ();

				if (null != pending) {
					handler.Post (() => {
						// notify from GUI thread
						pending (bitmap);
					});
				}
			}

			class CacheFile
			{
				public string Url { get; set; }
				public bool HasLocalFile { get; set; }
				public bool DownloadFailed { get; set; }

				private string cachePath;
				private List<CacheValue> notifyDownloadedList = new List<CacheValue> ();

				bool isDownloading;

				public bool IsComplete
				{
					get {
						return this.HasLocalFile || this.DownloadFailed;
					}
				}

				public string LocalFilePath
				{
					get {
						Contract.Assert (Url != null);
						return System.IO.Path.Combine (cachePath,  ImageCachingService.CalculateUrlHash(Url));
					}
				}

				public CacheFile (string url, string cachePath) { this.Url = url; this.cachePath = cachePath;}

				public void AddToNotifyList (CacheValue value)
				{
					lock (this) {
						notifyDownloadedList.Add (value);
					}
				}

				public List<CacheValue> GetAllPendingNotifies()
				{
					List<CacheValue> result;
					lock (this) {
						result = notifyDownloadedList;
						notifyDownloadedList = new List<CacheValue> ();	// future notifications can append new entries to a fresh blank "pending" list
					}
					return result;
				}

				public async Task Download (Action<CacheFile> notifyComplete)
				{
					bool startedDownload = false;
					bool stored = false;

					try
					{
						lock (this) {
							if (this.isDownloading) {
								Debug.WriteLine ("Already a downloader active, url=" + this.Url);
								return;
							}

							if (this.IsComplete) {
								Debug.WriteLine ("Previous download already complete, url=" + this.Url);
								goto completed;
							}
								
							startedDownload = this.isDownloading = true;
						}

						Debug.WriteLine ("Download activating, url=" + this.Url);

						string localPath = this.LocalFilePath;

						if (System.IO.File.Exists (localPath)) {
							stored = true;
							Debug.WriteLine ("Previous download already complete, url=" + this.Url);
							goto completed;
						}

						// download the image...
						using (var webClient = new WebClient ()) {
							var url = new Uri (this.Url);

							try {
								await webClient.DownloadFileTaskAsync (url, localPath);
								stored = true;
							} catch (TaskCanceledException) {
								Debug.WriteLine ("Download cancelled, url=" + this.Url);
								goto completed;
							} catch (Exception e) {
								Debug.WriteLine ("Download failure: " + e.ToString ());
								goto completed;
							}
						}

						Debug.WriteLine ("Download completed, url=" + this.Url);
						goto completed;
					} finally {
						if (startedDownload) {
							lock (this) {
								this.HasLocalFile = stored;
								this.DownloadFailed = !stored;
								this.isDownloading = false;
							}
						}
					}

				completed:

					notifyComplete (this);
				}
			}

			class CacheKey
			{
				public string Url { get; set; }
				public int MaxWidth { get; set; }
				public int MaxHeight { get; set; }

				public CacheKey (string url, int maxWidth, int maxHeight)
				{
					this.Url = url;
					this.MaxWidth = (maxWidth < 0 ? 0 : maxWidth);
					this.MaxHeight = (maxHeight < 0 ? 0 : maxHeight);
				}

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

					if (! (obj is CacheKey))
						return false;

					CacheKey item = obj as CacheKey;

					if (!ReferenceEquals (Url, item.Url))
						return false;

					if (MaxWidth != item.MaxWidth)
						return false;

					if (MaxHeight != item.MaxHeight)
						return false;

					return true;
				}
			}

			class CacheValue
			{
				public CacheKey Key { get; set; }

				public BitmapType Bitmap {
					get {
						BitmapType result;
						lock (this) {
							result = this.bitmapWeak;
						}
						return result;
					}
				}

				public BitmapWeak bitmapWeak;
				Action<BitmapType> callbacks;
				bool loadInProgess;
				bool decodeFailed;

				public CacheValue (CacheKey key)
				{
					this.Key = key;
				}

				public void NotifyOnceReady (Action<BitmapType> callback)
				{
					lock (this) {
						callbacks += callback;
					}
				}

				public Action<BitmapType> GetPendingCallbacks ()
				{
					Action<BitmapType> result;
					lock (this) {
						result = callbacks;
						callbacks = null;
					}

					return result;
				}

				public async Task HandleDownloaded (CacheFile file, Action<CacheValue, BitmapType> callback)
				{
					bool startedLoading = false;
					BitmapType bitmap = null;

					try {
						if (file.DownloadFailed) {
							Debug.WriteLine ("Notifying image url downloaded, url=" + file.Url);
							goto completed;
						}

						lock (this) {
							bitmap = (BitmapType)this.bitmapWeak;
						}

						if (null != bitmap) {
							Debug.WriteLine ("Bitmap already present, url=" + file.Url);
							goto completed;
						}

						lock (this) {
							if (this.decodeFailed) {
								Debug.WriteLine ("Previous decoding failed, url=" + file.Url);
							}
							if (loadInProgess) {
								Debug.WriteLine ("Load already in progress (must wait for it to complete, url=" + file.Url);
								return;
							}

							startedLoading = this.loadInProgess = true;
						}

						Debug.WriteLine ("Loading image url from cache, url=" + file.Url);

						string localFile = file.LocalFilePath;

						BitmapFactory.Options options = new BitmapFactory.Options ();
						options.InJustDecodeBounds = true;
						await BitmapFactory.DecodeFileAsync (localFile, options);

						int scale = 1;

						if (this.Key.MaxHeight > 0) {
							scale = options.OutHeight / Key.MaxHeight;
						}
						if (this.Key.MaxHeight > 0) {
							int temp = options.OutWidth / Key.MaxWidth;
							scale = (temp > scale ? temp : scale);
						}

						options.InSampleSize = scale;
						options.InJustDecodeBounds = false;

						Bitmap tempBitmap = await BitmapFactory.DecodeFileAsync (localFile, options);

						if ((typeof (BitmapType) == typeof(BitmapDrawable)) ||
							(typeof (BitmapType) == typeof(Drawable))) {
							object temp = new BitmapDrawable(tempBitmap);
							bitmap = (BitmapDrawable)temp;
						} else {
							object temp = tempBitmap;
							bitmap = (BitmapType)temp;
						}

						lock (this) {
							this.bitmapWeak = bitmap;
						}

						Debug.WriteLine ("Loading image url from cache completed, url=" + file.Url);
						goto completed;
					} finally {
						if (startedLoading) {
							lock (this) {
								if (bitmap == null) {
									this.decodeFailed = true;
								}
								this.loadInProgess = false;
							}
						}
					}

				completed:
					callback (this, bitmap);
				}
			}

		}
	}
}

