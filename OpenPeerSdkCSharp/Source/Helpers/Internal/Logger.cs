using System;
using System.Text;

using PubLogger = OpenPeerSdk.Helpers.Logger;
using ActualDebug = System.Diagnostics.Debug;

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

				public void Log(PubLogger.Level level, PubLogger.Severity severity, string message, params object[] list)
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

