/*
Copyright (c) 2014, hookflash Inc.
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FreeBSD Project.
*/
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.Util;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Org.Json;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;

namespace HopSampleApp
{
	public class CSOPHelper
	{
		CallbackHandler mCallbackHandler;
		#region toggle Outgoing Telnet Logging
		public void toggleOutgoingTelnetLogging(Context context,Boolean enable)
		{
			if (enable) 
			{
				OPLogger.SetLogLevel(OPLogLevel.LogLevelTrace);
				OPLogger.SetLogLevel("openpeer_webrtc", OPLogLevel.LogLevelBasic);
				OPLogger.SetLogLevel("zsLib_socket", OPLogLevel.LogLevelInsane);
				String deviceId = Android.Provider.Settings.Secure.GetString (context.ContentResolver, Android.Provider.Settings.Secure.AndroidId);
				String instanceId = CSUtility.GetGUIDInstanceID();
				String telnetLogString = deviceId + "-" + instanceId + "\n";
				Log.Debug("output", "Outgoing log string = " + telnetLogString);
				OPLogger.InstallOutgoingTelnetLogger("logs.opp.me:8115", true, telnetLogString);
			}
			else
			{
				OPLogger.SetLogLevel(OPLogLevel.LogLevelNone);
				OPLogger.SetLogLevel("openpeer_webrtc", OPLogLevel.LogLevelNone);
				OPLogger.SetLogLevel("zsLib_socket", OPLogLevel.LogLevelNone);
				OPLogger.UninstallOutgoingTelnetLogger();
			}
		}
		#endregion

		#region enable Telnet Logging
		public void enableTelnetLogging()
		{
			OPLogger.SetLogLevel(OPLogLevel.LogLevelTrace);
			OPLogger.SetLogLevel("openpeer_webrtc", OPLogLevel.LogLevelNone);
			OPLogger.SetLogLevel("zsLib_socket", OPLogLevel.LogLevelInsane);

			OPLogger.SetLogLevel("openpeer_services_transport_stream",OPLogLevel.LogLevelNone);
			OPLogger.SetLogLevel("openpeer_stack", OPLogLevel.LogLevelNone);
			OPLogger.InstallTelnetLogger(59999, 60, true);
			OPLogger.InstallFileLogger("/storage/emulated/0/HFLog1.txt", true);
		}
		#endregion

		#region Enable Default Telnet Logging
		public void EnableDefaultTelnetLogging(Context context ,Boolean enable)
		{
			if (enable)
			{
				Log.Debug ("Default Telnet","");
				OPLogger.SetLogLevel (OPLogLevel.LogLevelTrace);
				OPLogger.SetLogLevel ("openpeer_webrtc", OPLogLevel.LogLevelBasic);
				OPLogger.SetLogLevel ("zsLib_socket", OPLogLevel.LogLevelInsane);
				String telnetLogString = Android.Provider.Settings.Secure.GetString (context.ContentResolver, Android.Provider.Settings.Secure.AndroidId);
				Log.Debug ("output", "Outgoing log string = " + telnetLogString);
				OPLogger.InstallTelnetLogger (59999, 60, true);
				OPLogger.InstallFileLogger ("/storage/emulated/0/HFLog.txt", true);

			}
		}
		#endregion

		#region Initialize Settings
		public void InitializeSettings(Context context,CallbackHandler callbackhandler,CSOPDatastoreDelegate datastoreDelegate)
		{
			CSSettings.SharedCSSettings ().InitializeAssets (context);
			mCallbackHandler = callbackhandler;
			EnableDefaultTelnetLogging (context,true);
			if (datastoreDelegate != null)
			{
				CSOPDataManager.SharedCSOPDataManager().init(datastoreDelegate);
			} 
			else
			{
				CSOPDataManager.SharedCSOPDataManager().init(CSOPDatastoreDelegate.SharedCSOPDatastoreDelegate().init(context));
			}
			OPStackMessageQueue StackMessageQueue = OPStackMessageQueue.Singleton();
			OPStack Stack = OPStack.Singleton();

			OPCacheDelegate mCacheDelegate = new CSOPCacheDelegate();

			mCallbackHandler.RegisterCacheDelegate(mCacheDelegate);
			OPCache.Setup(mCacheDelegate);


			OPSettings.ApplyDefaults();

			String httpSettings =CSSettings.SharedCSSettings().createHttpSettings();
			OPSettings.Apply(httpSettings);

			String forceDashSettings =CSSettings.SharedCSSettings().createForceDashSetting();
			OPSettings.Apply(forceDashSettings);

			String appSettings =CSSettings.SharedCSSettings().getAPPSettingsString();
			OPSettings.Apply(appSettings);

			Stack.Setup(null,null);

		}

		#endregion

		#region Singleton pattern

		private static CSOPHelper instance;

		private CSOPHelper(){	}

		public static CSOPHelper SharedCSOPHelper()
		{
			if (instance == null)
				instance = new CSOPHelper();
			return instance;
		}

		#endregion
	}
}

