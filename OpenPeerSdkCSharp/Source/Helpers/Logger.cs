using System;

namespace OpenPeerSdk
{
	namespace Helpers
	{
		public class Logger
		{
			public enum Level
			{
				None,
				Basic,
				Detail,
				Debug,
				Trace,
				Insane
			}

			public enum Severity
			{
				Informational,
				Warning,
				Error,
				Fatal
			}

			private static Internal.Logger singleton;

			static Logger ()
			{
				singleton = new Internal.Logger ();
			}

			public static void Basic (string message, params object[] list)
			{
				singleton.Basic (message, list);
			}

			public static void Detail (string message, params object[] list)
			{
				singleton.Detail (message, list);
			}

			public static void Debug (string message, params object[] list)
			{
				singleton.Debug (message, list);
			}

			public static void Trace (string message, params object[] list)
			{
				singleton.Trace (message, list);
			}

			public static void Insane (string message, params object[] list)
			{
				singleton.Insane (message, list);
			}

			public static void Warning (Level level, string message, params object[] list)
			{
				singleton.Log (level, Severity.Warning, message, list);
			}

			public static void Error (Level level, string message, params object[] list)
			{
				singleton.Log (level, Severity.Error, message, list);
			}

			public static void Fatal (Level level, string message, params object[] list)
			{
				singleton.Log (level, Severity.Fatal, message, list);
			}
		}

	}
}
