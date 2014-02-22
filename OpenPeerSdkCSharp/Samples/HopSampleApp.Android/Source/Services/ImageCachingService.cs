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

using OpenPeerSdk.Helpers;

using BitmapType = Android.Graphics.Drawables.BitmapDrawable;
using BitmapWeak = OpenPeerSdk.Helpers.WeakReference<Android.Graphics.Drawables.BitmapDrawable>;
using Logger = OpenPeerSdk.Helpers.Logger;


namespace HopSampleApp
{
	namespace Services
	{
		public interface IImageCachingDownloader
		{
			BitmapType FetchNowOrAsyncDownload (string url, int width, int height, Action<BitmapType> callback);

			void RedownloadMissingUponNextFetch ();
			void RedownloadOlderThan (DateTime utcTime);
		}

		public class ImageCachingServiceDownloader : IImageCachingDownloader, IServiceConnection<ImageCachingService>
		{
			ImageCachingService service;
			List<PendingDownload> pendingDownload = new List<PendingDownload> ();

			private bool redownloadMissingUponNextFetch = false;

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

			public BitmapType FetchNowOrAsyncDownload (string url, int maxWidth, int maxHeight, Action<BitmapType> callback)
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

			public void RedownloadMissingUponNextFetch ()
			{
				if (this.service == null) {
					redownloadMissingUponNextFetch = true;
					return;
				}
				this.service.RedownloadMissingUponNextFetch ();
			}

			public void RedownloadOlderThan (DateTime utcTime)
			{
				this.service.RedownloadOlderThan (utcTime);
			}

			BitmapType IImageCachingDownloader.FetchNowOrAsyncDownload (string url, int maxWidth, int maxHeight, Action<BitmapType> callback)
			{
				return this.FetchNowOrAsyncDownload (url, maxWidth, maxHeight, callback);
			}

			void IImageCachingDownloader.RedownloadMissingUponNextFetch ()
			{
				this.RedownloadMissingUponNextFetch ();
			}

			void IImageCachingDownloader.RedownloadOlderThan (DateTime utcTime)
			{
				this.RedownloadOlderThan (utcTime);
			}

			void IServiceConnection<ImageCachingService>.OnServiceConnected ( ImageCachingService service, ComponentName name )
			{
				this.service = service;

				if (redownloadMissingUponNextFetch) {
					this.service.RedownloadMissingUponNextFetch ();
					redownloadMissingUponNextFetch = false;
				}
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
				Logger.Basic ("started");

				return StartCommandResult.Sticky;
			}	

			public override void OnCreate ()
			{
				Logger.Basic ("OnCreate");

				base.OnCreate ();

				lock (this) {
					EnsureCachePathExists ();
					CleanupOlderFiles ();
				}
			}

			public override void OnDestroy ()
			{
				Logger.Basic ("OnDestroy");

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
						Logger.Trace ("Cached file is already present, url={0}", url);
					}

					// create a custom bitmap loader key
					CacheKey key = new CacheKey (url, maxWidth == null ? -1 : (int)maxWidth, maxHeight == null ? -1 : (int)maxHeight);

					if (!cachedBitmaps.TryGetValue (key, out value)) {
						value = new CacheValue (key);

						cachedBitmaps [key] = value;
					} else {
						Logger.Trace ("Cached bitmap key is already present, url={0}", url);

						BitmapType existingBitmap = value.Bitmap;
						if (null != existingBitmap) {
							if (null != outCachedResult) {
								Logger.Trace ("Cached bitmap shortcircuited result, url={0}", url);

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

			public void RedownloadMissingUponNextFetch ()
			{
				Logger.Debug ("Forcing redownload of any missing image cache entries (when next fetched)...");

				lock (this) {
					foreach (KeyValuePair<string, CacheFile> keyValue in cachedFiles) {
						keyValue.Value.RedownloadUponNextFetch ();
					}
				}
			}

			public void RedownloadOlderThan (DateTime utcTime)
			{
				Logger.Debug ("Forcing redownload of images older than specified time, time={0}", utcTime.ToLocalTime ().ToString ());

				lock (this) {
					foreach (KeyValuePair<string, CacheFile> keyValue in cachedFiles) {
						keyValue.Value.RedownloadOlderThan (utcTime);
					}
					foreach (KeyValuePair<CacheKey, CacheValue> keyValue in cachedBitmaps) {
						keyValue.Value.RedownloadOlderThan (utcTime);
					}
				}
			}

			private void EnsureCachePathExists ()
			{
				string path = this.CachePath;
				if (!Directory.Exists (path)) {
					Directory.CreateDirectory (path);
				}
			}

			private void CleanupOlderFiles() {
				string path = this.CachePath;

				DateTime now = DateTime.UtcNow;
				TimeSpan lifeTime = new TimeSpan(5, 0, 0, 0);	// 5 days maximum

				try {
					string [] fileEntries = Directory.GetFiles(path);
					foreach(string fileName in fileEntries) {

						DateTime fileCreationTime = File.GetCreationTimeUtc(fileName);

						try {
							if (fileCreationTime + lifeTime < now) {
								File.Delete (path);
							}
						} catch (Exception e) {
							Logger.Error (Logger.Level.Detail, "Failed to delete a cache file, filename={0}, expection={1}", fileName, e.ToString ());
						}
					}
				} catch (Exception e) {
					Logger.Error (Logger.Level.Basic, "Failed to clean up cache, exception={0}", e.ToString ());
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
				private const long maxBytesToDownload = (1024 * 1024);	// 1 megabyte max
				public string Url { get; set; }
				public bool HasLocalFile { get; set; }
				public bool DownloadFailed { get; set; }
				public bool PermanentFailure { get; set; }

				private static TimeSpan defaultRetryDuration = new TimeSpan (0, 0, 15);
				private static TimeSpan maxRetryDuration = new TimeSpan (1, 0, 0);

				DateTime nextRetryTime;
				TimeSpan nextRetryDuration = defaultRetryDuration;	// start at 15 seconds, thus first retry can only happen after 30 seconds

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

				public void RedownloadUponNextFetch ()
				{
					lock (this) {
						if (this.isDownloading) {
							Logger.Trace ("Cannot redownload while already downloading, url={0}", this.Url);
							return;
						}

						if (!this.DownloadFailed) {
							Logger.Trace ("No need to redownload since download was never attempted or it succeeded, url={0}", this.Url);
							return;
						}

						if (this.notifyDownloadedList.Count > 0) {
							Logger.Trace ("Cannot redownload while pending bitmap caching notifications exist, url={0}", this.Url);
							return;
						}

						Logger.Trace ("Will force redownload next fetch, url={0}", this.Url);
						nextRetryDuration = defaultRetryDuration;
						nextRetryTime = DateTime.UtcNow;
					}
				}

				public void RedownloadOlderThan (DateTime utcTime)
				{
					string localPath = this.LocalFilePath;

					lock (this) {
						try {
							if (this.isDownloading)
								return;

							if (this.notifyDownloadedList.Count > 0) {
								Logger.Trace ("Cannot redownload while pending notifications exist, url={0}", this.Url);
								return;
							}

							if (System.IO.File.Exists (localPath)) {
								DateTime fileTime = System.IO.File.GetCreationTimeUtc (localPath);
								if (fileTime >= utcTime) {
									Logger.Trace ("File is not old enough yet, file={0}, url={1}, created={2}", localPath, this.Url, fileTime.ToLocalTime ().ToString ());
									return;
								}

								Logger.Trace ("Deleting older cached image file, file={0}, url={1}, created={2}", localPath, this.Url, fileTime.ToLocalTime ().ToString ());
								System.IO.File.Delete (localPath);
							}
						} catch (Exception e) {
							Logger.Error (Logger.Level.Detail, "Failed to access cached image file, file={0}, url={1}, exception=", localPath, this.Url, e.ToString ());
						}
					}
				}

				public async Task Download (Action<CacheFile> notifyComplete)
				{
					bool startedDownload = false;
					bool stored = false;

					try
					{
						lock (this) {
							if (this.isDownloading) {
								Logger.Trace ("Already a downloader active, url={0}", this.Url);
								return;
							}

							if (this.IsComplete) {
								if (!this.DownloadFailed) {
									Logger.Trace ("Previous download already complete, url={0}", this.Url);
									goto completed;
								}

								if (this.PermanentFailure) {
									Logger.Warning (Logger.Level.Detail, "Refusing to download this file, url={0}", this.Url);
									goto completed;
								}

								if (this.nextRetryTime <= DateTime.UtcNow) {
									Logger.Trace ("Too soon to retry download, url={0}", this.Url);
									goto completed;
								}

								this.DownloadFailed = false;
								Logger.Debug ("okay to retry download now, url={0}", this.Url);
							}

							startedDownload = this.isDownloading = true;
						}

						Logger.Debug ("Download activating, url={0}", this.Url);

						string localPath = this.LocalFilePath;

						if (System.IO.File.Exists (localPath)) {
							stored = true;
							Logger.Debug ("Previous download already complete, url={0}", this.Url);
							goto completed;
						}

						// download the image...
						using (var webClient = new WebClient ()) {
							var url = new Uri (this.Url);

							try {
								webClient.DownloadProgressChanged += DownloadProgressCallback;
								await webClient.DownloadFileTaskAsync (url, localPath);
								stored = true;
							} catch (TaskCanceledException) {
								Logger.Warning (Logger.Level.Detail, "Download cancelled, url={0}", this.Url);
								goto completed;
							} catch (Exception e) {
								Logger.Error (Logger.Level.Debug, "Download failure: {0}", e.ToString ());
								goto completed;
							}
						}

						Logger.Debug ("Download completed, url=", this.Url);
						goto completed;
					} finally {
						if (startedDownload) {
							lock (this) {
								this.HasLocalFile = stored;
								this.DownloadFailed = !stored;
								if (this.DownloadFailed) {
									this.nextRetryTime = DateTime.UtcNow + nextRetryDuration;
									this.nextRetryDuration += this.nextRetryDuration;
									if (nextRetryDuration > maxRetryDuration) {
										nextRetryDuration = maxRetryDuration;
									}
								} else {
									this.nextRetryTime = DateTime.UtcNow;
									this.nextRetryDuration = CacheFile.defaultRetryDuration;
								}
								this.isDownloading = false;
							}
						}
					}

				completed:

					notifyComplete (this);
				}

				private void DownloadProgressCallback(object sender, DownloadProgressChangedEventArgs e)
				{
					if ((e.TotalBytesToReceive > maxBytesToDownload) ||
						(e.BytesReceived > maxBytesToDownload)) {
						lock (this) {
							Logger.Warning (Logger.Level.Detail, "File download size is too big, refusing to download, total size={0}, downloaded={1}, url={2}", e.TotalBytesToReceive, e.BytesReceived, this.Url);
							this.PermanentFailure = true;
						}

						WebClient webClient = (WebClient)sender;
						webClient.CancelAsync ();
						return;
					}

					Logger.Trace ("Image download progress, percentage={0}, total size={1}, downloaded={2}, url={2}", e.ProgressPercentage, e.TotalBytesToReceive, e.BytesReceived, this.Url);
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

							if (null != result) {
								// check if the java object assocaited was GC
								if (IntPtr.Zero == result.Handle) {
									Logger.Trace ("Associated Java Object was GC'ed, url={0}", Key.Url);

									result = null;
									this.bitmapWeak = null;
								}
							}
						}
						return result;
					}
				}

				public BitmapWeak bitmapWeak;
				Action<BitmapType> callbacks;
				bool loadInProgess;
				bool decodeFailed;
				DateTime bitmapCreated;

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

				public void RedownloadOlderThan (DateTime utcTime)
				{
					lock (this) {
						if (this.loadInProgess)
							return;

						if (bitmapCreated == default(DateTime)) {
							this.bitmapWeak = null;
							return;
						}

						if (bitmapCreated < utcTime) {
							this.bitmapWeak = null;
							return;
						}
					}
				}

				public async Task HandleDownloaded (CacheFile file, Action<CacheValue, BitmapType> callback)
				{
					bool startedLoading = false;
					BitmapType bitmap = null;

					try {
						if (file.DownloadFailed) {
							Logger.Warning (Logger.Level.Debug, "Notifying image download failure, url={0}", file.Url);
							goto completed;
						}

						lock (this) {
							bitmap = this.Bitmap;
						}

						if (null != bitmap) {
							Logger.Trace ("Bitmap already present, url={0}", file.Url);
							goto completed;
						}

						lock (this) {
							if (this.decodeFailed) {
								Logger.Warning (Logger.Level.Trace, "Previous decoding failed, url={0}", file.Url);
							}
							if (loadInProgess) {
								Logger.Trace ("Load already in progress (must wait for it to complete, url={0}", file.Url);
								return;
							}

							startedLoading = this.loadInProgess = true;
						}

						Logger.Debug ("Loading image url from cache, url=" + file.Url);

						string localFile = file.LocalFilePath;

						try {
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

							bitmap = Convert(tempBitmap, bitmap);

						} catch(Exception e) {
							Logger.Warning (Logger.Level.Debug, "Decoding of image failed, url={0}, expection={1}", file.Url, e.ToString ());
						} finally {
							lock (this) {
								if (null != bitmap) {
									this.bitmapWeak = bitmap;
									this.bitmapCreated = DateTime.UtcNow;
								}
							}
						}

						Logger.Debug ("Loading image url from cache completed, url={0}", file.Url);
						goto completed;
					} finally {
						if (startedLoading) {
							lock (this) {
								if (bitmap == null) {
									this.decodeFailed = true;
									this.bitmapCreated = default(DateTime);
								}
								this.loadInProgess = false;
							}
						}
					}

				completed:
					callback (this, bitmap);
				}

				protected static Bitmap Convert(Bitmap input, Bitmap selector)
				{
					return input;
				}

				protected static BitmapDrawable Convert(Bitmap input, BitmapDrawable selector)
				{
					return new BitmapDrawable(input);
				}
			}

		}
	}
}

