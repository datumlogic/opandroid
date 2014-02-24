using System;
using System.Collections.Generic;
using System.Linq;
using SQLite;

namespace OpenPeerSdk
{
	namespace Database
	{
		public class ChatMessageDatabase : SQLiteConnection
		{
			private static string DatabaseFilePath (int localUserID, int dbId) {
				return Common.DatabasePath (localUserID.ToString () + "_" + dbId.ToString () + "_chat.db3");
			}

			public ChatMessageDatabase (int localUserID, int chatMessageDatabaseId) : this(DatabaseFilePath(localUserID, chatMessageDatabaseId))
			{
			}

			protected ChatMessageDatabase (string path) : base(path, true)
			{
				CreateTable<ChatMessage> ();
			}

			public int Count {
				get {
					lock (this) {
						return this.ExecuteScalar<int> (@"SELECT COUNT(*) FROM ChatMessage");
					}
				}
			}

			public IEnumerable<ChatMessage> GetLatestEntries (int limit, bool orderAsc = true) 
			{
				lock (this) {
					IEnumerable<ChatMessage> result = this.Query<ChatMessage> ("@SELECT * FROM ChatMessage ORDER BY When, _id DESC LIMIT ?", limit);
					if (orderAsc)
						return result.Reverse ();

					return result;
				}
			}

			public IEnumerable<ChatMessage> GetRelativeEntries (ChatMessage message, int entriesBefore, int entriesAfter)
			{
				IEnumerable<ChatMessage> result = null;

				lock (this) {
					if (0 != entriesBefore) {
						IEnumerable<ChatMessage> beforeResult = this.Query<ChatMessage> (@"SELECT * FROM ChatMessage ORDER BY When, _id DESC LIMIT ? WHERE When <= ? AND _id != ?", entriesBefore, message.When.Ticks, message.Id);

						result = beforeResult.Reverse ();
					}

					if (0 != entriesAfter) {
						IEnumerable<ChatMessage> afterResult = this.Query<ChatMessage> (@"SELECT * FROM ChatMessage ORDER BY When, _id ASC LIMIT ? WHERE When >= ? AND _id != ?", entriesAfter, message.When.Ticks, message.Id);
						if (null != result) {
							result = result.Concat(afterResult);
						} else {
							result = afterResult;
						}
					}
				}

				if (null == result) {
					result = new List<ChatMessage> ();
				}

				return result;
			}

			public ChatMessage GetById (int id)
			{
				lock (this) {
					IEnumerable<ChatMessage> result = this.Query<ChatMessage> (@"SELECT * FROM ChatMessage WHERE _id = ?", id);
					return result.FirstOrDefault ();
				}
			}

			public ChatMessage GetByMessageId (string messageId)
			{
				lock (this) {
					IEnumerable<ChatMessage> result = this.Query<ChatMessage> (@"SELECT * FROM ChatMessage WHERE MessageId = ?", messageId);
					return result.FirstOrDefault ();
				}
			}

			public int Update (ChatMessage item) 
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

			public int Delete(ChatMessage item) 
			{
				lock (this) {
					return Delete<ChatMessage> (item.Id);
				}
			}
		}
	}
}
