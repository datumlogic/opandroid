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
			private static string conversationTableName = typeof(Conversation).Name;
			private static string conversationPartipantTableName = typeof(ConversationPartipant).Name;
			private static string conversationPartipantRecordTableName = typeof(ConversationPartipantRecord).Name;

			private int lastParticipantId = 0;

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

				ConversationPartipant lastParticipant = GetLastUsedParticipantId ();
				if (null != lastParticipant) {
					lastParticipantId = lastParticipant.ParticipantId;
				}
			}

			public int Count {
				get {
					lock (this) {
						return this.ExecuteScalar<int> (@"SELECT COUNT(*) FROM ?", conversationTableName);
					}
				}
			}

			public ConversationPartipant GetLastUsedParticipantId ()
			{
				lock (this) {
					IEnumerable<ConversationPartipant> result = this.Query<ConversationPartipant> (@"SELECT * FROM ? ORDER BY ParticipantId DESC LIMIT 1", conversationPartipantTableName);
					return result.FirstOrDefault ();
				}
			}

			public IList<ConversationPartipantRecord> GetParticipantRecordByPeerUriContactId (string contactId)
			{
				lock (this) {
					IEnumerable<ConversationPartipantRecord> result = this.Query<ConversationPartipantRecord> (@"SELECT * FROM ? WHERE PeerUriContactId = ? ORDER BY LastUpdated DESC", conversationPartipantRecordTableName, contactId);
					return result.ToList ();
				}
			}

			public IList<ConversationPartipantRecord> GetParticipantRecordByIdentityUri (string identityUri)
			{
				lock (this) {
					IEnumerable<ConversationPartipantRecord> result = this.Query<ConversationPartipantRecord> (@"SELECT * FROM ? WHERE IdentityUri = ? ORDER BY LastUpdated DESC", conversationPartipantRecordTableName, identityUri);
					return result.ToList ();
				}
			}

			// <param name="peerUri">the peer uri of the conversation participant</param>
			// <param name="usingDisplayName">the display name the participant is claiming to be</param>
			// <param name="usingAvatarUrl">the avatar the participant is claiming to be</param>
			// <param name="outContacts">optional returned list of existing contacts matching to this peer URI and the value is set to null if no existing contacts were found</param>
			// <param name="outPreviousName">optionally returned if contacts were not found the this value will be set with the last known display name (if a record was found)</param>
			// <param name="outPreviousAvatarUrl">optionally returned if contacts were not found the this value will be set with the last known avatar url (if a record was found)</param>
			public int GetOrCreateParticipantIdForConversation (
				string peerUri,
				string usingDisplayName,
				string usingAvatarUrl,
				ContactDatabase contactDb,
				OptionalOut< IList<Contact> > outContacts = null,
				OptionalOut<string> outPreviousName = null,
				OptionalOut<string> outPreviousAvatarUrl = null
			)
			{
				Contract.Requires (!String.IsNullOrEmpty(peerUri));
				Contract.Requires (contactDb != null);

				int resultParticipantId = 0;
				IList<Contact> resultContacts = null;

				string previousName = usingDisplayName;
				string previousAvatar = usingAvatarUrl;

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

						// find any contacts which are associated to this peer URI contact id
						resultContacts = contactDb.GetByPeerUriContactId (contactId);

						IList<ConversationPartipantRecord> existingRecordsForContactId = GetParticipantRecordByPeerUriContactId (contactId);

						if (!Helper.IsNullOrEmpty(existingRecordsForContactId)) {

							ConversationPartipantRecord record = existingRecordsForContactId.First ();

							// if we found a contact id that means the user is the same user as there cannot be any two users sharing the same contact id (ever)
							resultParticipantId = record.ParticipantId;

							previousName = record.LastKnownDisplayName;
							previousAvatar = record.LastKnownAvatarUrl;

							Logger.Debug("found existing participant record for contact id, participant id={0}, peer uri={1}", resultParticipantId, peerUri);
						}

						List<Contact> contactsMissingRecords = new List<Contact> ();

						// search all contacts to see if any have existing records (and which records are missing)
						foreach (Contact contact in resultContacts) {
							bool foundRecordForThisIdentity = false;
							IList<ConversationPartipantRecord> records = GetParticipantRecordByIdentityUri(contact.IdentityUri);

							foreach (ConversationPartipantRecord record in records) {
								if ((!String.IsNullOrEmpty(record.StableId)) &&
									(!String.IsNullOrEmpty(contact.StableId))) {
									if (record.StableId != contact.StableId) {
										Logger.Trace ("previous participant record was found for identity uri but stable id doesn't match thus skipping, identity uri={0}, contact stable id={1}, record stable id={2}", contact.IdentityUri, contact.StableId, record.StableId);
										continue;
									}

									foundRecordForThisIdentity = foundRecordForThisIdentity || String.Equals(contact.PeerUri, peerUri);	// make sure it's the same peer URI (they can be different)

									bool updated = false;

									// if the record display name / avatar have changed, update the record
									if (!String.Equals (contact.DisplayName, record.LastKnownDisplayName)) {
										updated = true;
										record.LastKnownDisplayName = contact.DisplayName;
									}
									if (!String.Equals (contact.AvatarUrl, record.LastKnownAvatarUrl)) {
										updated = true;
										record.LastKnownAvatarUrl = contact.AvatarUrl;
									}

									if (updated) {
										record.LastUpdated = DateTime.UtcNow;
										Update (record);
									}

									if (0 == resultParticipantId) {
										resultParticipantId = record.ParticipantId;
										Logger.Debug("found participant record for contact based on identity uri, participant id={0}, peer uri={1}, identity uri={2}", resultParticipantId, peerUri, contact.IdentityUri);
										continue;
									}

									Logger.Debug("found participant record for contact based on identity uri higher priority used, participant id={0}, peer uri={1}, identity uri={2}", resultParticipantId, peerUri, contact.IdentityUri);
								}
							}

							if (!foundRecordForThisIdentity) {
								contactsMissingRecords.Add (contact);
							}
						}

						bool createdNewParticipantId = false;

						// check to see if a participant id was chosen (needs one to be able to add any missing records)
						if (0 == resultParticipantId) {
							createdNewParticipantId = true;

							++lastParticipantId;
							resultParticipantId = lastParticipantId;
						}

						// scan over missing records to add them now
						if (!Helper.IsNullOrEmpty(resultContacts)) {
							foreach (Contact contact in contactsMissingRecords) {
								ConversationPartipantRecord record = new ConversationPartipantRecord ();
								record.IdentityUri = contact.IdentityUri;
								record.PeerUri = peerUri;
								record.PeerUriContactId = contactId;
								record.StableId = contact.StableId;
								record.ParticipantId = resultParticipantId;
								record.LastKnownDisplayName = contact.DisplayName;
								record.LastKnownAvatarUrl = contact.AvatarUrl;

								Insert (record);

								Logger.Trace ("added missing participant record for idenity, participant id={0}, identity={1}, stable id={2}, peer uri={3}", record.ParticipantId, record.IdentityUri, record.StableId, record.PeerUri);
							}
						} else {
							if (createdNewParticipantId) {
								ConversationPartipantRecord record = new ConversationPartipantRecord ();
								record.IdentityUri = "";
								record.PeerUri = peerUri;
								record.PeerUriContactId = contactId;
								record.StableId = "";
								record.ParticipantId = resultParticipantId;
								record.LastKnownDisplayName = usingDisplayName;
								record.LastKnownAvatarUrl = usingAvatarUrl;

								Insert (record);

								Logger.Trace ("added missing participant record for peer uri, participant id={0}, peer uri={1}", record.ParticipantId, record.PeerUri);
							}
						}
					}
				} finally {
					if (null != outContacts) {
						if (!Helper.IsNullOrEmpty (resultContacts)) {
							outContacts.Result = resultContacts;
						} else {
							outContacts.Result = new List<Contact> ();
						}
					}

					if (Helper.IsNullOrEmpty (resultContacts)) {
						if (null != outPreviousName) {
							outPreviousName.Result = previousName;
						}
						if (null != outPreviousAvatarUrl) {
							outPreviousAvatarUrl.Result = previousAvatar;
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

			public int Update (ConversationPartipantRecord item) 
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
