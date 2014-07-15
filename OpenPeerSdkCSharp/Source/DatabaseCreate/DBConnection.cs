using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Mono.Data.Sqlite;
using System.Data;
using Android.Database;
using DatabaseCreate;

namespace OpenPeerSdkCSharp.DBConn
{
	public class DBConnection
	{
		public string dbName="charp_opandroid.db3";

		public SqliteConnection GetConnection()
		{
			var dbPath = Path.Combine (Environment.GetFolderPath (Environment.SpecialFolder.Personal), dbName);
			bool Exists = File.Exists (dbPath);
			if (!Exists)
				SqliteConnection.CreateFile (dbPath);

			var conn = new SqliteConnection (String.Format("Data Source={0}",dbPath));

			//Create table...
			if (!Exists) 
			{
				CreateTables.CreateAccountTable (conn);
				CreateTables.CreateAvatarTable (conn);
				CreateTables.CreateCallParticipantsTable (conn);
				CreateTables.CreateCallTable (conn);
				CreateTables.CreateContactsTable (conn);
				CreateTables.CreateConversationStateChangeTable (conn);
				CreateTables.CreateConversationStateTable (conn);
				CreateTables.CreateGroupTable (conn);
				CreateTables.CreateGroupWindowParticipantsTable (conn);
				CreateTables.CreateIdentityContactsTable (conn);
				CreateTables.CreateIdentityTable (conn);
				CreateTables.CreateMessageTable (conn);
				CreateTables.CreateUserIdTable (conn);
				CreateTables.CreateWindowParticipantsTable (conn);
				CreateTables.CreateWindowTable (conn);
				CreateTables.CreateConversationWindowTable (conn);
			}


			return conn;

		}

		#region Singleton pattern

		private static DBConnection instance;

		private DBConnection(){	}

		public static DBConnection SharedDBConnection()
		{
			if (instance == null)
				instance = new DBConnection();
			return instance;
		}

		#endregion
	}
}

