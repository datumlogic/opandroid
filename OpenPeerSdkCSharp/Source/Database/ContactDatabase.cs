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

			public IEnumerable<Contact> GetByPeerUriContactId (string contactId)
			{
				lock (this) {
					IEnumerable<Contact> result = this.Query<Contact> (@"SELECT * FROM Contact WHERE PeerUriContactId = ?", contactId);
					return result;
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
