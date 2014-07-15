using System;
using SQLite;

namespace DatabaseModel.CallEntry
{
	/// <summary>
	/// Call.
	/// </summary>
	[Table("call")]
	public class Call
	{
		private DateTime closed=DateTime.UtcNow;
		private DateTime answer=DateTime.UtcNow;

		[PrimaryKey,AutoIncrement]
		public int call_id
		{ 
			get;
			set;
		}//Call id

		public int thread_id
		{ 
			get;
			set;
		}//Thread id

		[MaxLength(256)]
		public string caller
		{ 
			get;
			set;
		}//Caller name

		[MaxLength(256)]
		public string callee
		{ 
			get;
			set;
		}//Callee name

		public DateTime answer_time
		{ 
			get
			{
				return answer;
			}
			set
			{
				answer = value;
			}
		}//Answer time

		public DateTime closed_time
		{ 
			get
			{
				return closed;
			}
			set
			{
				closed = value;
			}
		}//Closed time
	
	}
}

