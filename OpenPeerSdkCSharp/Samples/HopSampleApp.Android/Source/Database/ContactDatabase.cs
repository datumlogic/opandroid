using System;
using System.Collections.Generic;
using System.Linq;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class ContactDatabase : SQLiteConnection
		{
			private static string tableName = typeof(Contact).Name;

			private static string DatabaseFilePath (int localUserID) {
				return Common.DatabasePath (localUserID.ToString() + "_contact.db3");
			}

			public ContactDatabase (int localUserID) : this(DatabaseFilePath(localUserID))
			{
			}

			protected ContactDatabase (string path) : base(path, true)
			{
				CreateTable<Contact> ();
			}

			public int Count {
				get {
					lock (this) {
						return this.ExecuteScalar<int> (@"SELECT COUNT(*) FROM Contact");
					}
				}
			}

			public IList<Contact> GetByIndex (Contact contact, int index, int maxRows)
			{
				lock (this) {
					if (0 != maxRows) {
						IEnumerable<Contact> rows = this.Query<Contact> (@"SELECT * FROM ? ORDER BY DisplayName, IdentityURI, _id ASC OFFSET ? LIMIT ?", tableName, index, maxRows);
						return rows.ToList ();
					}
				}

				return new List<Contact> ();
			}

			public Contact GetById (int id)
			{
				lock (this) {
					IEnumerable<Contact> result = this.Query<Contact> (@"SELECT * FROM ? WHERE _id = ?", tableName, id);
					return result.FirstOrDefault ();
				}
			}

			public Contact GetByIdentityUri (string identityUri)
			{
				lock (this) {
					IEnumerable<Contact> result = this.Query<Contact> (@"SELECT * FROM ? WHERE IdentityURI = ?", tableName, identityUri);
					return result.FirstOrDefault ();
				}
			}

			public IList<Contact> GetByPeerUriContactId (string contactId)
			{
				lock (this) {
					IEnumerable<Contact> result = this.Query<Contact> (@"SELECT * FROM ? WHERE PeerUriContactId = ?", tableName, contactId);
					return result.ToList ();
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
					this.Execute (@"UPDATE ? SET RefreshFlag = ?", tableName, true);
				}
			}

			public int EndRefresh ()
			{
				lock (this) {
					return this.Execute (@"DELETE FROM ? WHERE RefreshFlag = ?", tableName, true);
				}
			}
		}
	}
}
