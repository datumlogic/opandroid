using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Mono.Data.Sqlite;
using System.Data;
using Android.Database;

namespace DatabaseCreate
{
	public class CreateTables
	{
		/// <summary>
		/// The name of the db.
		/// </summary>
		public static string dbName="charp_opandroid.db3";

		private static SqliteConnection GetConnection()
		{
			var dbPath = Path.Combine (Environment.GetFolderPath (Environment.SpecialFolder.Personal), dbName);
			bool Exists = File.Exists (dbPath);
			if (!Exists)
				SqliteConnection.CreateFile (dbPath);

			var conn = new SqliteConnection (String.Format("Data Source={0}",dbPath));

			//Create table...
			if (!Exists)
				CreateAccountTable (conn);

			return conn;
			
		}

		//Create Account
		public static void CreateAccountTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Avatar
		public static void CreateAvatarTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Call
		public static void CreateCallTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Call Participants
		public static void CreateCallParticipantsTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Contacts
		public static void CreateContactsTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create ConversationState
		public static void CreateConversationStateTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Conversation State Change
		public static void CreateConversationStateChangeTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Conversation Window
		public static void CreateWindowTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Group
		public static void CreateGroupTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Group Window Participants
		public static void CreateGroupWindowParticipantsTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Identity
		public static void CreateIdentityTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create IdentityContacts
		public static void CreateIdentityContactsTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Message
		public static void CreateMessageTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Userid
		public static void CreateUserIdTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Window Participants
		public static void CreateWindowParticipantsTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}

		//Create Windows
		public static void CreateWindowsTable(SqliteConnection Connection)
		{
			var sqlclause = "";
			Connection.Open ();

			using (var command = Connection.CreateCommand ())
			{
				command.CommandText = sqlclause;
				command.ExecuteNonQuery ();
			}

			Connection.Close ();
		}


	}
}

