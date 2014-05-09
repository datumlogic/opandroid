using System;
using System.Collections.Generic;

namespace OpenPeerSdk
{
	namespace Helpers
	{
		namespace Internal
		{
			public class LoggerSubsystemAttribute : System.Attribute
			{
				private static int lastSubsystemId = 0;
				private static Dictionary<string, int> subsystems = new Dictionary<string, int> ();
				private static Dictionary<string, Helpers.LoggerSubsystemAttribute> namespaces = new Dictionary<string, Helpers.LoggerSubsystemAttribute> ();

				protected int subsystemId;
				protected string subsystemName;

				public LoggerSubsystemAttribute (string name) : base ()
				{
					this.subsystemName = name;

					lock (subsystems) {
						int value = 0;
						if (subsystems.TryGetValue (name, out value)) {
							this.subsystemId = value;
							return;
						}
						#warning should register subsystem and it should be tied to zsLib::createPUID()


						this.subsystemId = lastSubsystemId;
						subsystems [name] = lastSubsystemId;
						++lastSubsystemId;
					}
				}

				public static void RegisterNamespace (string @namespace, string submoduleName)
				{
					lock (namespaces) {
						Helpers.LoggerSubsystemAttribute existingAttribute = null;
						if (namespaces.TryGetValue (@namespace, out existingAttribute)) {
							// nothing to do, already registered
							return;
						}

						Helpers.LoggerSubsystemAttribute subsytem = new Helpers.LoggerSubsystemAttribute (submoduleName);
						namespaces [@namespace] = subsytem;
					}
				}

				public static Helpers.LoggerSubsystemAttribute GetRegisteredNamespace (string @namespace)
				{
					lock (namespaces) {
						Helpers.LoggerSubsystemAttribute existingAttribute = null;
						if (namespaces.TryGetValue (@namespace, out existingAttribute)) {
							return existingAttribute;
						}
					}
					return null;
				}
			}
		}
	}
}
