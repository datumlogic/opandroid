using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Mono.Data.Sqlite;
using System.Data;
using Android.Database;
using OpenPeerSdkCSharp.DBConn;

namespace OpenPeerSdkCSharp
{
	public class CreateViews
	{

		public static void CreatSomeView()
		{
			var sqlclause = "CREATE VIEW......";
			using (var connection = DBConnection.SharedDBConnection().GetConnection())
			{
				connection.Open ();
				using (var command = connection.CreateCommand ())
				{
					command.CommandText = sqlclause;
					command.ExecuteNonQuery ();
				}
				connection.Close ();
			}
		}
	}
}

