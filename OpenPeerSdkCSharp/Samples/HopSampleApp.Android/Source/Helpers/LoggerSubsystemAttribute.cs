using System;
using System.Collections.Generic;

namespace OpenPeerSdk
{
	namespace Helpers
	{
		[AttributeUsage(AttributeTargets.All, Inherited = false)]
		public class LoggerSubsystemAttribute : Internal.LoggerSubsystemAttribute
		{
			public int Id { get { return subsystemId; } }
			public string Name { get { return subsystemName; } }

			public LoggerSubsystemAttribute (string name) : base (name)
			{
			}
		}
	}
}
