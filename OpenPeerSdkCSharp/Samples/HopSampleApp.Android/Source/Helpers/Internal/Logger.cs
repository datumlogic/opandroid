using System;
using System.Text;
using System.Diagnostics;

using PubLogger = OpenPeerSdk.Helpers.Logger;
using ActualDebug = System.Diagnostics.Debug;
using UseLoggerSubsystemAttribute = OpenPeerSdk.Helpers.LoggerSubsystemAttribute;

namespace OpenPeerSdk
{
	namespace Helpers
	{
		namespace Internal
		{
			public class Logger
			{
				public Logger ()
				{
				}

				public void Basic (string message, params object[] list)
				{
					Log (PubLogger.Level.Basic, PubLogger.Severity.Informational, message, list);
				}

				public void Detail (string message, params object[] list)
				{
					Log (PubLogger.Level.Detail, PubLogger.Severity.Informational, message, list);
				}

				public void Debug (string message, params object[] list)
				{
					Log (PubLogger.Level.Debug, PubLogger.Severity.Informational, message, list);
				}

				public void Trace (string message, params object[] list)
				{
					Log (PubLogger.Level.Trace, PubLogger.Severity.Informational, message, list);
				}

				public void Insane (string message, params object[] list)
				{
					Log (PubLogger.Level.Insane, PubLogger.Severity.Informational, message, list);
				}

				public void Warning(PubLogger.Level level, string message, params object[] list)
				{
					Log (level, PubLogger.Severity.Warning, message, list);
				}

				public void Error(PubLogger.Level level, string message, params object[] list)
				{
					Log (level, PubLogger.Severity.Error, message, list);
				}

				public void Fatal(PubLogger.Level level, string message, params object[] list)
				{
					Log (level, PubLogger.Severity.Fatal, message, list);
				}

				public void RegisterCurrentNamespace ()
				{
					StackTrace trace = new StackTrace (2);

					int total = trace.FrameCount;

					string subsystemName = null;

					for (int current = 0; current < total; ++current) {
						StackFrame frame = trace.GetFrame (current);

						var methodBase = frame.GetMethod ();
						var classType = methodBase.ReflectedType;

						string @namespace = classType.Namespace;

						if (null == subsystemName) {
							UseLoggerSubsystemAttribute subsystem = (UseLoggerSubsystemAttribute)Attribute.GetCustomAttribute (classType, typeof(UseLoggerSubsystemAttribute));
							if (null == subsystem) {
								continue;
							}

							subsystemName = subsystem.Name;
						}

						if (String.IsNullOrEmpty (@namespace)) {
							continue;
						}

						RegisterNamespace (@namespace, subsystemName);
						return;
					}

					throw new MissingFieldException ();
				}

				public void RegisterCurrentNamespace (string subsystemName)
				{
					StackTrace trace = new StackTrace (2);

					int total = trace.FrameCount;

					for (int current = 0; current < total; ++current) {
						StackFrame frame = trace.GetFrame (current);

						var methodBase = frame.GetMethod ();
						var classType = methodBase.ReflectedType;

						string @namespace = classType.Namespace;

						if (String.IsNullOrEmpty (@namespace)) {
							continue;
						}

						RegisterNamespace (@namespace, subsystemName);
						return;
					}
				}

				public void RegisterNamespace (string @namespace, string submoduleName)
				{
					Internal.LoggerSubsystemAttribute.RegisterNamespace (@namespace, submoduleName);
				}

				protected void Log(PubLogger.Level level, PubLogger.Severity severity, string message, params object[] list)
				{
					StringBuilder builder = new StringBuilder ();

					builder.Append (level.ToString ());

					if (severity != PubLogger.Severity.Informational) {
						builder.Append (" (");
						builder.Append (severity.ToString ());
						builder.Append ("): ");
					} else {
						builder.Append (": ");
					}

					StackTrace trace = new StackTrace (3);

					if (null != trace) {
						int total = trace.FrameCount;

						StackFrame frame = null;
						UseLoggerSubsystemAttribute subsystem = null;

						for (int current = 0; current < total; ++current) {
							frame = trace.GetFrame (current);

							var methodBase = frame.GetMethod ();
							var classType = methodBase.ReflectedType;

							subsystem = (UseLoggerSubsystemAttribute)Attribute.GetCustomAttribute (classType, typeof(UseLoggerSubsystemAttribute));
							if (null != subsystem) {
								break;
							}
							frame = null;
						}

						if (null == frame) {
							frame = trace.GetFrame (0);
						}

						if (null != frame) {
							var methodBase = frame.GetMethod ();
							var classType = methodBase.ReflectedType;

							string className = classType.Name;
							string @namespace = classType.Namespace;

							if (null == subsystem) {
								subsystem = UseLoggerSubsystemAttribute.GetRegisteredNamespace (@namespace);
							}

							builder.Append ("[");
							if (subsystem != null) {
								builder.Append (subsystem.Name);
								builder.Append ("(");
								builder.Append (subsystem.Id.ToString ());
								builder.Append (")");
							} else {
								builder.Append (@namespace);
							}
							builder.Append ("] ");

							if (className.StartsWith ("<")) {
								builder.Append (classType.FullName);
							} else {
								builder.Append (className);
							}

							builder.Append (".");
							builder.Append (methodBase.Name);
							builder.Append (" ");
						}
					}

					if (list.Length > 0) {
						builder.AppendFormat (message, list);
					} else {
						builder.Append( message);
					}

					ActualDebug.Write (builder.ToString ());
				}
			}
		}
	}
}

