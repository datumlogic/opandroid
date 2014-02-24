using System;
using System.Collections.Generic;
using System.Linq;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class ConversationDatabase : SQLiteConnection
		{
			private static string DatabaseFilePath (int localUserID) {
				return Common.DatabasePath (localUserID.ToString() + "_conversation.db3");
			}

			public ConversationDatabase (int localUserID) : this(DatabaseFilePath(localUserID))
			{
			}

			protected ConversationDatabase (string path) : base(path, true)
			{
				CreateTable<Conversation> ();
			}

			public int Count {
				get {
					lock (this) {
						return this.ExecuteScalar<int> (@"SELECT COUNT(*) FROM Conversation");
					}
				}
			}

			public IEnumerable<Conversation> GetByIndex (Conversation conversation, int index, int maxRows)
			{
				IEnumerable<Conversation> result = null;

				lock (this) {
					if (0 != maxRows) {
						IEnumerable<Conversation> rows = this.Query<Conversation> (@"SELECT * FROM Conversation ORDER BY DisplayName, IdentityURI, _id ASC OFFSET ? LIMIT ?", index, maxRows);
						result = rows;
					}
				}

				if (null == result) {
					result = new List<Conversation> ();
				}

				return result;
			}

			public Conversation GetById (int id)
			{
				lock (this) {
					IEnumerable<Conversation> result = this.Query<Conversation> (@"SELECT * FROM Conversation WHERE _id = ?", id);
					return result.FirstOrDefault ();
				}
			}

			public Conversation GetByIdentityUri (string identityUri)
			{
				lock (this) {
					IEnumerable<Conversation> result = this.Query<Conversation> (@"SELECT * FROM Conversation WHERE IdentityURI = ?", identityUri);
					return result.FirstOrDefault ();
				}
			}

			public int Update (Conversation item) 
			{
				lock (this) {
					if (item.Id != 0) {
						Update (item);
						return item.Id;
					} else {
						return Insert (item);
					}
				}
			}

			public int Delete(Conversation item) 
			{
				lock (this) {
					return Delete<Conversation> (item.Id);
				}
			}
		}
	}
}
