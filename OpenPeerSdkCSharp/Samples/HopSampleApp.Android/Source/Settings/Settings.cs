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
using System.Collections;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using HopSampleApp;
using HopSampleApp.Enums;
using Newtonsoft.Json;

namespace HopSampleApp
{

	#region Enums

	public enum LoggerTypes
	{
		LOGGER_STD_OUT,
		LOGGER_TELNET,
		LOGGER_OUTGOING_TELNET
	};
	public enum TelnetLoggerOptions 
	{
		TELNET_ENABLE,
		TELNET_SERVER_OR_PORT,
		TELNET_COLOR,
		TOTAL_TELNET_OPTIONS
	};
	public enum Modules 
	{
		MODULE_APPLICATION,
		MODEULE_SDK,
		MODULE_MEDIA,
		MODULE_WEBRTC,
		MODULE_CORE,
		MODULE_STACK_MESSAGE,
		MODULE_STACK,
		MODULE_SERVICES,
		MODULE_SERVICES_WIRE,
		MODULE_SERVICES_ICE,
		MODULE_SERVICES_TURN,
		MODULE_SERVICES_RUDP,
		MODULE_SERVICES_HTTP,
		MODULE_SERVICES_MLS,
		MODULE_SERVICES_TCP,
		MODULE_SERVICES_TRANSPORT,
		MODULE_ZSLIB,
		MODULE_ZSLIB_SOCKET,
		MODULE_JAVASCRIPT,
		TOTAL_MODULES
	};

	#endregion

	class Settings
	{
		#region Properties

		public bool IsMediaAECOn {get; set;}

		public bool IsMediaAGCOn {get; set;}

		public bool IsMediaNSOn {get; set;}

		public bool IsRemoteSessionActivationModeOn {get; set;}

		public bool IsFaceDetectionModeOn {get; set;}

		public bool IsRedialModeOn {get; set;}

		public bool EnabledStdLogger {get; set;}

		public Dictionary<string,string> AppModulesLoggerLevel {get; set;}

		public Dictionary<string,object> TelnetLoggerSettings {get; set;}

		public Dictionary<string,object> OutgoingTelnetLoggerSettings {get; set;}

		#endregion

		#region Singleton Pattern

		private static Settings instance;
		private Settings()
		{
			//need to make IsolatedStorageSettings for android

			if (IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.settingsKeyMediaAEC) !=false)
				this.IsMediaAECOn =IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.settingsKeyMediaAEC);

			if (IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.settingsKeyMediaAGC) !=false)
				this.IsMediaAGCOn = IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.settingsKeyMediaAGC);

			if (IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.settingsKeyMediaNS) !=false)
				this.IsMediaNSOn = IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.settingsKeyMediaNS);

			if (IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.archiveRemoteSessionActivationMode) !=false)
				this.IsRemoteSessionActivationModeOn = IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.archiveRemoteSessionActivationMode);

			if (IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.archiveFaceDetectionMode) !=false)
				this.IsFaceDetectionModeOn = IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.archiveFaceDetectionMode);

			if (IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.archiveRedialMode) !=false)
				this.IsRedialModeOn = IsolatedStorageSettings.ApplicationSettings.ContainsKey(AppConsts.archiveRedialMode);

			if (IsolatedStorageSettings.ApplicationSettings.ContainsKey (AppConsts.settingsKeyStdOutLogger) !=false)
				this.EnabledStdLogger = IsolatedStorageSettings.ApplicationSettings.ContainsKey (AppConsts.settingsKeyStdOutLogger);
					

			//this.AppModulesLoggerLevel = NSUserDefaults.standardUserDefaults().objectForKey(archiveModulesLogLevels).mutableCopy();
			if (this.AppModulesLoggerLevel !=null) 
				this.AppModulesLoggerLevel = new Dictionary<string,string>();

		}

		public static Settings SharedSettings()
		{
			if (instance == null)
				instance = new Settings();
			return instance;
		}

		#endregion

		#region Methods
		//
		public void enableMediaAEC(bool enable)
		{
			this.IsMediaAECOn = enable;
			HOPSettings.sharedSettings().storeSettingsObjectKey(Convert.ToBoolean(this.IsMediaAECOn),AppConsts.settingsKeyMediaAEC);
		}

		public void enableMediaAGC(bool enable)
		{
			this.IsMediaAGCOn = enable;
			HOPSettings.sharedSettings().storeSettingsObjectKey(Convert.ToBoolean(this.IsMediaAGCOn),AppConsts.settingsKeyMediaAGC);
		}

		public void enableMediaNS(bool enable)
		{
			this.IsMediaNSOn = enable;
			HOPSettings.sharedSettings().storeSettingsObjectKey(Convert.ToBoolean(this.IsMediaNSOn), AppConsts.settingsKeyMediaNS);
		}

		public void enableRemoteSessionActivationMode(bool enable)
		{
			this.IsRemoteSessionActivationModeOn = enable;
			HOPSettings.sharedSettings().storeSettingsObjectKey(Convert.ToBoolean(this.IsRemoteSessionActivationModeOn), AppConsts.archiveRemoteSessionActivationMode);
		}

		public void enableFaceDetectionMode(bool enable)
		{
			this.IsFaceDetectionModeOn = enable;
			HOPSettings.sharedSettings().storeSettingsObjectKey(Convert.ToBoolean(this.IsFaceDetectionModeOn), AppConsts.archiveFaceDetectionMode);
		}

		public void enableRedialMode(bool enable)
		{
			this.IsRedialModeOn = enable;
			HOPSettings.sharedSettings().storeSettingsObjectKey(Convert.ToBoolean(this.IsRedialModeOn), AppConsts.archiveRedialMode);
		}
		//
		public string getArchiveKeyForLoggerType(LoggerTypes type)
		{
			string key = null;
			switch (type)
			{
			case LoggerTypes.LOGGER_STD_OUT:
				key =AppConsts.settingsKeyStdOutLogger;
				break;
			case LoggerTypes.LOGGER_TELNET:
				key = AppConsts.settingsKeyTelnetLogger;
				break;
			case LoggerTypes.LOGGER_OUTGOING_TELNET :
				key =AppConsts.settingsKeyOutgoingTelnetLogger;
				break;
			default :
				break;
			}

			return key;
		}

		public void enableLogger(bool enable, LoggerTypes type)
		{
			string key = this.getArchiveKeyForLoggerType(type);
			if (key.Length > 0)
			{
				key = new StringBuilder (key).Append(AppConsts.archiveEnabled).ToString();// key.stringByAppendingString(archiveEnabled);
				HOPSettings.sharedSettings().storeSettingsObjectKey(Convert.ToBoolean(enable), key);


			}

		}

		public bool isLoggerEnabled(LoggerTypes type)
		{
			bool ret = false;
			string key = this.getArchiveKeyForLoggerType(type);
			if (key.Length > 0)
			{
				key = new StringBuilder(key).Append(AppConsts.archiveEnabled).ToString();
				ret = IsolatedStorageSettings.ApplicationSettings.ContainsKey (key);
			}

			return ret;
		}
		public void setServerOrPortLogger(string server, LoggerTypes type)
		{
			string key = this.getArchiveKeyForLoggerType(type);
			if (key.Length > 0)
			{
				//key = new StringBuilder (key).Append (AppConsts.archiveServer).ToString ();
				HOPSettings.sharedSettings().storeSettingsObjectKey(server, key);
			}

		}
		public string getServerPortForLogger(LoggerTypes type)
		{
			string ret = null;
			string key = this.getArchiveKeyForLoggerType(type);
			if (key.Length > 0)
			{
				key = new StringBuilder (key).Append (AppConsts.archiveServer).ToString ();
				ret = IsolatedStorageSettings.ApplicationSettings.StringForKey(key).ToString();
			}

			return ret;
		}

		public void setColorizedOutputLogger(bool colorized, LoggerTypes type)
		{
			string key = this.getArchiveKeyForLoggerType(type);
			if (key.Length > 0)
			{
				key = new StringBuilder (key).Append (AppConsts.archiveColorized).ToString ();
				HOPSettings.sharedSettings().storeSettingsObjectKey(Convert.ToBoolean(colorized), key);
			}

		}
		public bool isColorizedOutputForLogger(LoggerTypes type)
		{
			bool ret = false;
			string key = this.getArchiveKeyForLoggerType(type);
			if (key.Length > 0)
			{
				key = new StringBuilder (key).Append (AppConsts.archiveColorized).ToString ();
				ret = IsolatedStorageSettings.ApplicationSettings.ContainsKey (key);
			}

			return ret;
		}


		public HOPLoggerLevels getLoggerLevelForAppModule(Modules module)
		{
			HOPLoggerLevels ret = HOPLoggerLevels.HOPLoggerLevelNone;
			string archiveString = this.getArchiveStringForModule(module);
			if (archiveString.Length > 0) ret = this.getLoggerLevelForAppModuleKey(archiveString);

			return ret;
		}


		public HOPLoggerLevels getLoggerLevelForAppModuleKey (string moduleKey)
		{
			HOPLoggerLevels ret =HOPLoggerLevels.HOPLoggerLevelNone;

			int retNumber =Convert.ToInt32(this.AppModulesLoggerLevel.FirstOrDefault(x=>x.Value.Contains(moduleKey)).Key);
			if (retNumber !=0) ret = (HOPLoggerLevels)retNumber;

			return ret;
		}


		public void setLoggerLevelForAppModule(HOPLoggerLevels level, Modules module)
		{
			string archiveString = this.getArchiveStringForModule(module);
			this.AppModulesLoggerLevel.Add(getStringForLogLevel(level),archiveString);
			this.saveModuleLogLevels();
		}

		public void saveModuleLogLevels()
		{
			HOPSettings.sharedSettings().storeSettingsObjectKey(this.AppModulesLoggerLevel,AppConsts.archiveModulesLogLevels);

		}

		public string getStringForModule(Modules module)
		{
			string ret = null;
			switch (module)
			{
			case Modules.MODULE_APPLICATION :
				ret = "Application";
				break;
			case Modules.MODEULE_SDK :
				ret = "SDK (Android)";//SDK (iOS)
				break;
			case Modules.MODULE_MEDIA :
				ret = "SDK (media)";
				break;
			case Modules.MODULE_WEBRTC :
				ret = "SDK (webRTC)";
				break;
			case Modules.MODULE_CORE :
				ret = "SDK (core)";
				break;
			case Modules.MODULE_STACK_MESSAGE :
				ret = "SDK (messages)";
				break;
			case Modules.MODULE_STACK :
				ret = "SDK (stack)";
				break;
			case Modules.MODULE_SERVICES :
				ret = "SDK (services)";
				break;
			case Modules.MODULE_SERVICES_WIRE :
				ret = "SDK (services packets)";
				break;
			case Modules.MODULE_SERVICES_ICE :
				ret = "SDK (STUN/ICE)";
				break;
			case Modules.MODULE_SERVICES_TURN :
				ret = "SDK (TURN)";
				break;
			case Modules.MODULE_SERVICES_RUDP :
				ret = "SDK (RUDP)";
				break;
			case Modules.MODULE_SERVICES_HTTP :
				ret = "SDK (HTTP)";
				break;
			case Modules.MODULE_SERVICES_MLS :
				ret = "SDK (MLS)";
				break;
			case Modules.MODULE_SERVICES_TCP :
				ret = "SDK (TCP Messaging)";
				break;
			case Modules.MODULE_SERVICES_TRANSPORT :
				ret = "SDK (Transport Stream)";
				break;
			case Modules.MODULE_ZSLIB :
				ret = "SDK (zsLib)";
				break;
			case Modules.MODULE_ZSLIB_SOCKET :
				ret = "SDK (zsLib sockets)";
				break;
			case Modules.MODULE_JAVASCRIPT :
				ret = "JavaScript";
				break;
			default :
				break;
			}

			return ret;
		}

		public string getArchiveStringForModule(Modules module)
		{
			string ret = null;
			switch (module)
			{
			case Modules.MODULE_APPLICATION :
				ret = AppConsts.moduleApplication;
				break;
			case Modules.MODEULE_SDK :
				ret = AppConsts.moduleSDK;
				break;
			case Modules.MODULE_MEDIA :
				ret = AppConsts.moduleMedia;
				break;
			case Modules.MODULE_WEBRTC :
				ret = AppConsts.moduleWebRTC;
				break;
			case Modules.MODULE_CORE :
				ret = AppConsts.moduleCore;
				break;
			case Modules.MODULE_STACK_MESSAGE :
				ret = AppConsts.moduleStackMessage;
				break;
			case Modules.MODULE_STACK :
				ret = AppConsts.moduleStack;
				break;
			case Modules.MODULE_SERVICES :
				ret = AppConsts.moduleServices;
				break;
			case Modules.MODULE_SERVICES_WIRE :
				ret = AppConsts.moduleServicesWire;
				break;
			case Modules.MODULE_SERVICES_ICE :
				ret = AppConsts.moduleServicesIce;
				break;
			case Modules.MODULE_SERVICES_TURN :
				ret = AppConsts.moduleServicesTurn;
				break;
			case Modules.MODULE_SERVICES_RUDP :
				ret = AppConsts.moduleServicesRudp;
				break;
			case Modules.MODULE_SERVICES_HTTP :
				ret = AppConsts.moduleServicesHttp;
				break;
			case Modules.MODULE_SERVICES_MLS :
				ret = AppConsts.moduleServicesMls;
				break;
			case Modules.MODULE_SERVICES_TCP :
				ret = AppConsts.moduleServicesTcp;
				break;
			case Modules.MODULE_SERVICES_TRANSPORT :
				ret = AppConsts.moduleServicesTransport;
				break;
			case Modules.MODULE_ZSLIB :
				ret = AppConsts.moduleZsLib;
				break;
			case Modules.MODULE_ZSLIB_SOCKET :
				ret = AppConsts.moduleZsLibSocket;
				break;
			case Modules.MODULE_JAVASCRIPT :
				ret = AppConsts.moduleJavaScript;
				break;
			default :
				break;
			}

			return ret;
		}

		public string getStringForLogLevel(HOPLoggerLevels level)
		{
			switch (level)
			{
			case HOPLoggerLevels.HOPLoggerLevelNone :
				return "NONE";
			case HOPLoggerLevels.HOPLoggerLevelBasic :
				return "BASIC";
			case HOPLoggerLevels.HOPLoggerLevelDetail :
				return "DETAIL";
			case HOPLoggerLevels.HOPLoggerLevelDebug :
				return "DEBUG";
			case HOPLoggerLevels.HOPLoggerLevelTrace :
				return "TRACE";
			case HOPLoggerLevels.HOPLoggerLevelInsane :
				return "INSANE";
			default :
				break;
			}

			return null;
		}

		public void saveDefaultsLoggerSettings()
		{
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_APPLICATION);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_SERVICES);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelDebug, Modules.MODULE_SERVICES_WIRE);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_SERVICES_ICE);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_SERVICES_TURN);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelDebug, Modules.MODULE_SERVICES_RUDP);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelDebug, Modules.MODULE_SERVICES_HTTP);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_SERVICES_MLS);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_SERVICES_TCP);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelDebug, Modules.MODULE_SERVICES_TRANSPORT);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_CORE);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_STACK_MESSAGE);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_STACK);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_ZSLIB);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelDebug, Modules.MODULE_ZSLIB_SOCKET);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODEULE_SDK);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelDetail, Modules.MODULE_WEBRTC);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelDetail, Modules.MODULE_MEDIA);
			this.setLoggerLevelForAppModule(HOPLoggerLevels.HOPLoggerLevelTrace, Modules.MODULE_JAVASCRIPT);
			this.setColorizedOutputLogger(true, LoggerTypes.LOGGER_STD_OUT);
			this.setColorizedOutputLogger(true, LoggerTypes.LOGGER_TELNET);
			this.setColorizedOutputLogger(true, LoggerTypes.LOGGER_OUTGOING_TELNET);
			this.enableLogger(true, LoggerTypes.LOGGER_STD_OUT);
			this.enableLogger(true, LoggerTypes.LOGGER_TELNET);
			this.enableLogger(true, LoggerTypes.LOGGER_OUTGOING_TELNET);
		}
		//
		public bool isQRSettingsResetEnabled()
		{
			return IsolatedStorageSettings.ApplicationSettings.ContainsKey (AppConsts.settingsKeyRemoveSettingsAppliedByQRCode);

		}
		public void enableQRSettingsReset(bool enable)
		{
			HOPSettings.sharedSettings().storeSettingsObjectKey(Convert.ToBoolean(enable),AppConsts.settingsKeyRemoveSettingsAppliedByQRCode);
		}

		public string getOuterFrameURL()
		{
			return IsolatedStorageSettings.ApplicationSettings.StringForKey (AppConsts.settingsKeyOuterFrameURL);

		}
		public string getNamespaceGrantServiceURL()
		{
			return IsolatedStorageSettings.ApplicationSettings.StringForKey (AppConsts.settingsKeyGrantServiceURL);

		}
		public string getIdentityProviderDomain()
		{
			return IsolatedStorageSettings.ApplicationSettings.StringForKey (AppConsts.settingsKeyIdentityProviderDomain);

		}
		public string getIdentityFederateBaseURI()
		{
			return IsolatedStorageSettings.ApplicationSettings.StringForKey (AppConsts.settingsKeyIdentityFederateBaseURI);

		}

		public string getLockBoxServiceDomain()
		{
			return IsolatedStorageSettings.ApplicationSettings.StringForKey (AppConsts.settingsKeyLockBoxServiceDomain);

		}
		public string getDefaultOutgoingTelnetServer()
		{
			return IsolatedStorageSettings.ApplicationSettings.StringForKey (AppConsts.settingsKeyOutgoingTelnetLogger);

		}

		public bool isAppDataSet()
		{
			//need fix
			bool ret = true;
			ret &= IsolatedStorageSettings.ApplicationSettings.ContainsKey (HOPSettings.sharedSettings ().getCoreKeyForAppKey (AppConsts.settingsKeyAppId));
			ret &= IsolatedStorageSettings.ApplicationSettings.ContainsKey(HOPSettings.sharedSettings().getCoreKeyForAppKey(AppConsts.settingsKeyAppIdSharedSecret));
			ret &= IsolatedStorageSettings.ApplicationSettings.ContainsKey(HOPSettings.sharedSettings().getCoreKeyForAppKey(AppConsts.settingsKeyAppName));
			ret &= IsolatedStorageSettings.ApplicationSettings.ContainsKey(HOPSettings.sharedSettings().getCoreKeyForAppKey(AppConsts.settingsKeyAppURL));
			ret &= IsolatedStorageSettings.ApplicationSettings.ContainsKey(HOPSettings.sharedSettings().getCoreKeyForAppKey(AppConsts.settingsKeyAppImageURL));
			#if APNS_ENABLED
			ret &= IsolatedStorageSettings.ApplicationSettings.ContainsKey(HOPSettings.sharedSettings().getCoreKeyForAppKey(AppConsts.settingsKeyUrbanAirShipAPIPushURL));
			#endif
			return ret;
		}

		public bool isLoginSettingsSet()
		{
			bool ret = true;
			ret &= this.getOuterFrameURL().Length != 0;
			ret &= this.getIdentityFederateBaseURI().Length != 0;
			ret &= this.getIdentityProviderDomain().Length != 0;
			ret &= this.getNamespaceGrantServiceURL().Length != 0;
			ret &= this.getLockBoxServiceDomain().Length != 0;
			ret &= this.getDefaultOutgoingTelnetServer().Length != 0;
			return ret;
		}
		public bool isAppSettingsSetForPath(string path)
		{
			bool ret = true;
			Dictionary<string,object> customerSpecificDict = new Dictionary<string, object> ();//.Contains (path);
			if (customerSpecificDict.Count > 0)
			{
				//string appID = customerSpecificDict.GetObjectData(AppConsts.settingsKeyAppId);
				//string appSecret = customerSpecificDict.GetObjectData(AppConsts.settingsKeyAppIdSharedSecret);
				//string appName = customerSpecificDict.GetObjectData(AppConsts.settingsKeyAppName);
				//if (appID.Length == 0 || appSecret.Length == 0 || appName.Length == 0) ret = false;
			}
			return ret;
		}

		public ArrayList getMissingAppSettings()
		{
			//need fix
			ArrayList ret = new ArrayList();
			if (IsolatedStorageSettings.ApplicationSettings.StringForKey(AppConsts.settingsKeyAppId).Length == 0)
				ret.Add(AppConsts.settingsKeyAppId);
			if (IsolatedStorageSettings.ApplicationSettings.StringForKey(AppConsts.settingsKeyAppIdSharedSecret).Length== 0)
				ret.Add(AppConsts.settingsKeyAppIdSharedSecret);
			return ret;
		}

		/* for later
		NSMutableDictionary dictionaryForJSONString(string jsonString)
		{
			NSMutableDictionary ret = null;
			SBJsonParser parser = new SBJsonParser();
			NSDictionary inputDictionary = parser.objectWithString(jsonString);
			if (inputDictionary.count() > 0 && inputDictionary.objectForKey("root") != null)
			{
				ret = NSMutableDictionary.dictionaryWithDictionary(inputDictionary.objectForKey("root"));
				if (ret) this.createUserAgentFromDictionary(ret);

			}

			return ret;
		}

		string createUserAgentFromDictionary(NSMutableDictionary inDictionary)
		{
			string ret = null;
			if (inDictionary.count() > 0)
			{
				string temp = inDictionary.objectForKey(settingsKeyUserAgent);
				if (temp.length() > 0)
				{
					ret = "";
					ArrayList partsOfString = temp.componentsSeparatedByString("$");
					foreach (string str in partsOfString)
					{
						string toAppend = "";
						if (str.compare(userAgentVariableAppName) == NSOrderedSame)
						{
							toAppend = NSBundle.mainBundle().infoDictionary().objectForKey("CFBundleName");
						}
						else if (str.compare(userAgentVariableAppVersion) == NSOrderedSame)
						{
							toAppend = NSBundle.mainBundle().infoDictionary().objectForKey("CFBundleVersion");
						}
						else if (str.compare(userAgentVariableSystemOS) == NSOrderedSame)
						{
							toAppend = UIDevice.currentDevice().systemName();
						}
						else if (str.compare(userAgentVariableVersionOS) == NSOrderedSame)
						{
							toAppend = UIDevice.currentDevice().systemVersion();
						}
						else if (str.compare(userAgentVariableDeviceModel) == NSOrderedSame)
						{
							toAppend = UIDevice.currentDevice().model();
							if (toAppend.hasPrefix("iPhone") || toAppend.hasPrefix("iPod")) toAppend = "iPhone";
							else if (toAppend.hasPrefix("iPad")) toAppend = "iPad";
						}
						else if (str.compare(userAgentVariableDeveloperID) == NSOrderedSame)
						{
							toAppend = NSBundle.mainBundle().infoDictionary().objectForKey("Hookflash Developer ID");
						}
						else
						{
							toAppend = str;
						}
						ret = ret.stringByAppendingString(toAppend);
					}
					if (ret.length() > 0) inDictionary.setObjectForKey(ret, HOPSettings.sharedSettings().getCoreKeyForAppKey(settingsKeyUserAgent));
					inDictionary.removeObjectForKey(settingsKeyUserAgent);
				}
			}
			return ret;
		}

		NSMutableDictionary dictionaryWithRemovedAllInvalidEntriesForPath(string path)
		{
			NSMutableDictionary plistDictionary = NSMutableDictionary.dictionaryWithContentsOfFile (path);
			if (plistDictionary.count () > 0) {
				foreach (string key in plistDictionary.allKeys()) {
					object value = plistDictionary.objectForKey (key);
					if (value.isKindOfClass (typeof(string))) {
						if (((string)value).rangeOfString ("<--").location != NSNotFound) {
							plistDictionary.removeObjectForKey (key);
						}
					}
				}
			}
			return plistDictionary;
		}*/
		public void updateDeviceInfo()
		{
			string deviceId = HOPUtility.hashString(Utility.GetGUIDstring());
			if (deviceId.Length > 0) HOPSettings.sharedSettings().storeCalculatedSettingObjectKey(deviceId, "openpeer/calculated/device-id");
			string str = Utility.GetDeviceOs();
			if (str.Length > 0) HOPSettings.sharedSettings().storeCalculatedSettingObjectKey(str, "openpeer/calculated/os");
			string system = Utility.GetPlatform();
			if (system.Length > 0) HOPSettings.sharedSettings().storeCalculatedSettingObjectKey(system, "openpeer/calculated/system");
			//string userAgent = Utility.getUserAgentName();
			//if (userAgent.length() > 0) HOPSettings.sharedSettings().storeCalculatedSettingObjectKey(userAgent, HOPSettings.sharedSettings().getCoreKeyForAppKey(settingsKeyUserAgent));
		}
		public void snapshotCurrentSettings()
		{
			//Dictionary<string,object> currentSettings = HOPSettings.sharedSettings ().getCurrentSettingsDictionary ();
			//if (currentSettings.Count > 0)
			//NSUserDefaults.standardUserDefaults ().setObjectForKey (currentSettings, settingsKeySettingsSnapshot);
		}
		/*
		void storeQRSettings(NSDictionary inDictionary)
		{
			NSUserDefaults.standardUserDefaults().setObjectForKey(inDictionary, settingsKeyAppliedQRSettings);
		}*/
		/*
		void removeAppliedQRSettings()
		{
			//Load applied QR settings
			NSDictionary appliedQRSettingsDictionary = NSUserDefaults.standardUserDefaults().objectForKey(settingsKeyAppliedQRSettings);
			//Load initial settings
			NSDictionary preQRSettingsSnapshotDictionary = NSUserDefaults.standardUserDefaults().objectForKey(settingsKeySettingsSnapshot);
			foreach (string key in appliedQRSettingsDictionary)
			{
				//Get core key for existing application
				string coreKey = HOPSettings.sharedSettings().getCoreKeyForAppKey(key);
				object qrValue = appliedQRSettingsDictionary.objectForKey(key);
				object currentValue = NSUserDefaults.standardUserDefaults().objectForKey(coreKey);
				object preValue = preQRSettingsSnapshotDictionary.objectForKey(coreKey);
				bool isEqual = false;
				//Check if qr settings is changed since it is applied
				if ((typeof(qrValue)).isSubclassOfClass(typeof(NSNumber))) isEqual = qrValue.compare(currentValue) == NSOrderedSame;
				else if ((typeof(qrValue)).isSubclassOfClass(typeof(string))) isEqual = qrValue.isEqualToString(currentValue);
				else isEqual = qrValue == currentValue;
				//If qr settings is not changed remove it or replace with initial value
				if (isEqual)
				{
					if (preValue == null) NSUserDefaults.standardUserDefaults().removeObjectForKey(coreKey);
					else NSUserDefaults.standardUserDefaults().setObjectForKey(preValue, coreKey);
				}
			}
		}*/

		//
		#endregion
	}
}

