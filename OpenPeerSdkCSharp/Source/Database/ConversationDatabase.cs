using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.Linq;

using SQLite;

using OpenPeerSdk.Helpers;

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
				CreateTable<ConversationPartipant> ();
				CreateTable<ConversationPartipantRecord> ();
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

			public IEnumerable<ConversationPartipantRecord> GetParticipantRecordByPeerUriContactId (string contactId)
			{
				lock (this) {
					IEnumerable<ConversationPartipantRecord> result = this.Query<ConversationPartipantRecord> (@"SELECT * FROM ConversationPartipantRecord WHERE PeerUriContactId = ?", contactId);
					return result;
				}
			}

			// <param name="outContact">optional returned list of existing contacts matching to this peer URI and the value is set to null if no existing contacts were found</param>
			public int GetOrCreateParticipantId (
				string peerUri,
				ContactDatabase contactDb,
				OptionalOut< IEnumerable<Contact> > outContact = null,
				OptionalOut<string> outPreviousName = null,
				OptionalOut<string> outPreviousAvatar = null
			)
			{
				Contract.Requires (!String.IsNullOrEmpty(peerUri));
				Contract.Requires (contactDb != null);

				int resultParticipantId = 0;
				IEnumerable<Contact> resultContact = null;

				try {
					lock (this) {

						#region this needs to change
						#warning needs to do proper split using op stack

						char[] delimiterChars = {'/'};
						string[] split = peerUri.Split (delimiterChars, StringSplitOptions.RemoveEmptyEntries);

						Contract.Assert (split.Length > 2);

						string contactId = split [2];

						Contract.Assert (!String.IsNullOrEmpty (contactId));
						#endregion

						// find any contacts which are associated to this peer URI
						resultContact = contactDb.GetByPeerUriContactId (contactId);

						IEnumerable<ConversationPartipantRecord> existingRecords = GetParticipantRecordByPeerUriContactId (contactId);

						if (!Helper.IsNullOrEmpty(existingRecords)) {
						}
					}
				} finally {
					if (null != outContact) {
						if (!Helper.IsNullOrEmpty(resultContact)) {
							outContact.Result = resultContact;
						} else {
							outContact.Result = null;
						}
					}
				}

				return resultParticipantId;
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
