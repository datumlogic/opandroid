using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp
{
	class Loger
	{
		//Need fix
		public static void SetLogLevels()
		{
			/*
			HOPLogger.SetLogLevelbyNameLevel(moduleApplication, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleApplication));
			HOPLogger.SetLogLevelbyNameLevel(moduleServices, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleServices));
			HOPLogger.SetLogLevelbyNameLevel(moduleServicesWire, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleServicesWire));
			HOPLogger.SetLogLevelbyNameLevel(moduleServicesIce, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleServicesIce));
			HOPLogger.SetLogLevelbyNameLevel(moduleServicesTurn, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleServicesTurn));
			HOPLogger.SetLogLevelbyNameLevel(moduleServicesRudp, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleServicesRudp));
			HOPLogger.SetLogLevelbyNameLevel(moduleServicesHttp, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleServicesHttp));
			HOPLogger.SetLogLevelbyNameLevel(moduleServicesMls, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleServicesMls));
			HOPLogger.SetLogLevelbyNameLevel(moduleServicesTcp, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleServicesTcp));
			HOPLogger.SetLogLevelbyNameLevel(moduleServicesTransport, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleServicesTransport));
			HOPLogger.SetLogLevelbyNameLevel(moduleCore, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleCore));
			HOPLogger.SetLogLevelbyNameLevel(moduleStackMessage, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleStackMessage));
			HOPLogger.SetLogLevelbyNameLevel(moduleStack, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleStack));
			HOPLogger.SetLogLevelbyNameLevel(moduleWebRTC, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleWebRTC));
			HOPLogger.SetLogLevelbyNameLevel(moduleZsLib, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleZsLib));
			HOPLogger.SetLogLevelbyNameLevel(moduleZsLibSocket, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleZsLibSocket));
			HOPLogger.SetLogLevelbyNameLevel(moduleSDK, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleSDK));
			HOPLogger.SetLogLevelbyNameLevel(moduleMedia, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleMedia));
			HOPLogger.SetLogLevelbyNameLevel(moduleJavaScript, Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleJavaScript));
			applicationLogerLevel = Settings.SharedSettings().GetLoggerLevelForAppModuleKey(moduleApplication);
			*/
		}
		//Need fix
		public static void StartStdLogger(bool start)
		{
			/*
			if (start)
			{
				HOPLogger.InstallStdOutLogger(false);
			}
			else HOPLogger.UninstallStdOutLogger();
			*/

		}
		//Need fix
		public static void StartTelnetLogger(bool start)
		{
			/*
			if (start)
			{
				string port = Settings.SharedSettings().GetServerPortForLogger(LOGGER_TELNET);
				bool colorized = Settings.SharedSettings().IsColorizedOutputForLogger(LOGGER_TELNET);
				if (port.Length() > 0) HOPLogger.InstallTelnetLoggerMaxSecondsWaitForSocketToBeAvailableColorizeOutput(port.IntValue(), 60, colorized);

			}
			else
			{
				HOPLogger.UninstallTelnetLogger();
			}
			*/

		}
		//Need fix
		public static void StartOutgoingTelnetLogger(bool start)
		{
			/*
			if (start)
			{
				string server = Settings.SharedSettings().GetServerPortForLogger(LOGGER_OUTGOING_TELNET);
				bool colorized = Settings.SharedSettings().IsColorizedOutputForLogger(LOGGER_OUTGOING_TELNET);
				if (server.Length() > 0) HOPLogger.InstallOutgoingTelnetLoggerColorizeOutputStringToSendUponConnection(server, colorized, HOPSettings.SharedSettings().GetAuthorizedApplicationId());

			}
			else
			{
				HOPLogger.UninstallOutgoingTelnetLogger();
			}
			*/

		}
		//Need fix
		public static void StartAllSelectedLoggers()
		{
			/*
			this.SetLogLevels();
			this.StartLogger(Settings.SharedSettings().IsLoggerEnabled(LOGGER_STD_OUT), LOGGER_STD_OUT);
			this.StartLogger(Settings.SharedSettings().IsLoggerEnabled(LOGGER_TELNET), LOGGER_TELNET);
			this.StartLogger(Settings.SharedSettings().IsLoggerEnabled(LOGGER_OUTGOING_TELNET), LOGGER_OUTGOING_TELNET);
			*/
		}
		/*
        //Need fix
		public static void StartLogger(bool start, LoggerTypes type)
		{

			switch (type)
			{
			case LOGGER_STD_OUT :
				this.StartStdLogger(start);
				break;
			case LOGGER_TELNET :
				this.StartTelnetLogger(start);
				break;
			case LOGGER_OUTGOING_TELNET :
				this.StartOutgoingTelnetLogger(start);
				break;
			default :
				break;
			}

			isLoggerStarted = Settings.SharedSettings().IsLoggerEnabled(LOGGER_STD_OUT) || Settings.SharedSettings().IsLoggerEnabled(LOGGER_TELNET) || Settings.SharedSettings().IsLoggerEnabled(LOGGER_OUTGOING_TELNET);

		}
		*/
		//Need fix
		public static void StartTelnetLoggerOnStartUp()
		{
			/*
			Settings.SharedSettings().SaveDefaultsLoggerSettings();
			Logger.StartAllSelectedLoggers();
			UIAlertView alert = new UIAlertView("OpenPeer", "Logger is started! Almost all log levels are set to trace. If you want to change that, you can do that from the settings.", null, "Ok", null);
			alert.Show();
			*/
		}
	}
}

