using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class ContactDatabase : SQLiteConnection
		{
			private static string DatabaseFilePath (int localUserID) {
				var sqliteFilename = localUserID.ToString() + "_contact.db3";

				#if NETFX_CORE
				var path = Path.Combine(Windows.Storage.ApplicationData.Current.LocalFolder.Path, sqliteFilename);
				#else

				#if SILVERLIGHT
				// Windows Phone expects a local path, not absolute
				var path = sqliteFilename;
				#else

				#if __ANDROID__
				// Just use whatever directory SpecialFolder.Personal returns
				string libraryPath = Environment.GetFolderPath(Environment.SpecialFolder.Personal); ;
				#else
				// we need to put in /Library/ on iOS5.1 to meet Apple's iCloud terms
				// (they don't want non-user-generated data in Documents)
				string documentsPath = Environment.GetFolderPath (Environment.SpecialFolder.Personal); // Documents folder
				string libraryPath = Path.Combine (documentsPath, "../Library/"); // Library folder
				#endif
				var path = Path.Combine (libraryPath, sqliteFilename);
				#endif		

				#endif
				return path;	
			}

			public ContactDatabase (int localUserID) : this(DatabaseFilePath(localUserID))
			{
			}

			protected ContactDatabase (string path) : base(path, true)
			{
				CreateTable<Contact> ();
			}

			public int Rows {
				get {
					lock (this) {
						return this.ExecuteScalar<int> (@"SELECT COUNT(*) FROM Contact");
					}
				}
			}

			public IEnumerable<Contact> GetByIndex (Contact contact, int index, int maxRows)
			{
				IEnumerable<Contact> result = null;

				lock (this) {
					if (0 != maxRows) {
						IEnumerable<Contact> rows = this.Query<Contact> (@"SELECT * FROM Contact ORDER BY DisplayName, IdentityURI, _id ASC OFFSET ? LIMIT ?", index, maxRows);
						result = rows;
					}
				}

				if (null == result) {
					result = new List<Contact> ();
				}

				return result;
			}

			public Contact GetById (int id)
			{
				lock (this) {
					IEnumerable<Contact> result = this.Query<Contact> (@"SELECT * FROM Contact WHERE _id = ?", id);
					return result.FirstOrDefault ();
				}
			}

			public Contact GetByIdentityUri (string identityUri)
			{
				lock (this) {
					IEnumerable<Contact> result = this.Query<Contact> (@"SELECT * FROM Contact WHERE IdentityURI = ?", identityUri);
					return result.FirstOrDefault ();
				}
			}

			public int Update (Contact item) 
			{
				lock (this) {
					item.RemovalFlag = false;

					if (item.Id != 0) {
						Update (item);
						return item.Id;
					} else {
						return Insert (item);
					}
				}
			}

			public int Delete(Contact item) 
			{
				lock (this) {
					return Delete<Contact> (item.Id);
				}
			}

			public void BeginRefresh ()
			{
				lock (this) {
					this.Execute (@"UPDATE Contact SET RefreshFlag = ?", true);
				}
			}

			public int EndRefresh ()
			{
				lock (this) {
					return this.Execute (@"DELETE FROM Contact WHERE RefreshFlag = ?", true);
				}
			}
		}
	}
}
