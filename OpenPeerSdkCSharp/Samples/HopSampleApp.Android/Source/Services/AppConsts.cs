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
using System.IO;


namespace HopSampleApp
{

	class AppConsts
	{

		public const string identityFacebookBaseURI = @"identity://facebook.com/";
		/*
		public string _identityFacebookBaseURI
		{
			get { return identityFacebookBaseURI; }
			set { identityFacebookBaseURI = value; }
		}

        */
		//Property list keys
		public const string settingsKeyAppliedQRSettings = @"appliedQRSettings";
		public const string settingsKeySettingsSnapshot = @"settingsSnapshot";
		public const string settingsKeyDefaultSettingsSnapshot = @"defaultSettingsSnapshot";
		public const string settingsKeyOverwriteExistingSettings = @"applicationOverwriteSettings";
		public const string settingsKeyUserAgent = @"userAgent";

		public const string settingsKeyMediaAEC = @"archiveMediaAEC";
		public const string settingsKeyMediaAGC = @"archiveMediaAGC";
		public const string settingsKeyMediaNS = @"archiveMediaNS";

		//UserAgent Variables
		public const string userAgentVariableAppName = @"appName";
		public const string userAgentVariableAppVersion = @"appVersion";
		public const string userAgentVariableSystemOS = @"systemOs";
		public const string userAgentVariableVersionOS = @"versionOs";
		public const string userAgentVariableDeviceModel = @"deviceModel";
		public const string userAgentVariableDeveloperID = @"developerID";

		public const string settingsKeyAppId = @"applicationId";
		public const string settingsKeyAppIdSharedSecret = @"applicationIdSharedSecret";
		public const string settingsKeyAppName = @"applicationName";
		public const string settingsKeyAppImageURL = @"applicationImageURL";
		public const string settingsKeyAppURL = @"applicationURL";
		public const string settingsKeyAPNS = @"APNS-UrbanAirShip";
		public const string settingsKeyTelnetLogger = @"localTelnetLoggerPort";
		public const string settingsKeyOutgoingTelnetLogger = @"defaultOutgoingTelnetServer";
		public const string settingsKeyStdOutLogger = @"archiveStdOutLogger";
		public const string settingsKeyRemoveSettingsAppliedByQRCode = @"applicationRemoveSettingsAppliedByQRCode";
		public const string settingsKeyOuterFrameURL = @"outerFrameURL";
		public const string settingsKeyGrantServiceURL = @"namespaceGrantServiceURL";
		public const string settingsKeyIdentityProviderDomain = @"identityProviderDomain";
		public const string settingsKeyIdentityFederateBaseURI = @"identityFederateBaseURI";
		public const string settingsKeyLockBoxServiceDomain = @"lockBoxServiceDomain";
		//public const string settingsKeyOutgoingTelnetLoggerServer = @"defaultOutgoingTelnetServer";
		public const string settingsKeySettingsDownloadURL = @"applicationSettingsDownloadURL";
		public const string settingsKeySettingsDownloadExpiryTime = @"applicationSettingsDownloadExpiryTime";
		public const string settingsKeyQRScannerShownAtStart = @"applicationQRScannerShownAtStart";
		public const string settingsKeySplashScreenAllowsQRScannerGesture = @"applicationSplashScreenAllowsQRScannerGesture";
		public const string settingsKeySettingsVersion = @"applicationSettingsVersion";
		public const string settingsKeyBackgroundingPhaseRichPush = @"applicationSettingsBackgroundingPhaseRichPush";

		public const string archiveEnabled = @"enabled";
		public const string archiveServer = @"Server";
		public const string archiveColorized = @"colorized";

        #if APNS_ENABLED
		public const string settingsKeyUrbanAirShipMasterAppSecretDev = @"masterAppSecretDev";
		public const string settingsKeyUrbanAirShipMasterAppSecret = @"masterAppSecret";
		public const string settingsKeyUrbanAirShipDevelopmentAppKey = @"developmentAppKey";
		public const string settingsKeyUrbanAirShipDevelopmentAppSecret = @"developmentAppSecret";
		public const string settingsKeyUrbanAirShipProductionAppKey = @"productionAppKey";
		public const string settingsKeyUrbanAirShipProductionAppSecret = @"productionAppSecret";
		public const string settingsKeyUrbanAirShipAPIPushURL = @"apiPushURL";
		public const string settingsKeyAPNSTimeOut = @"applicationTimeBetweenTwoPushes";
		#endif



        //Contact Profile xml tags
		public const string profileXmlTagProfile = @"profile";
		public const string profileXmlTagName = @"name";
		public const string profileXmlTagIdentities = @"identities";
		public const string profileXmlTagIdentityBundle = @"identityBundle";
		public const string profileXmlTagIdentity = @"identity";
		public const string profileXmlTagSignature = @"signature";
		public const string profileXmlTagAvatar = @"avatar";
		public const string profileXmlTagContactID = @"contactID";
		public const string profileXmlTagPublicPeerFile = @"publicPeerFile";
		public const string profileXmlTagSocialId = @"socialId";
		public const string profileXmlAttributeId = @"id";
		public const string profileXmlTagUserID = @"userID";

        //Message types
		public const string messageTypeText = @"text/x-application-hookflash-message-text";
		public const string messageTypeSystem = @"text/x-application-hookflash-message-system";

		public const string TagEvent = @"event";
		public const string TagId = @"id";
		public const string TagText = @"text";

		public const string systemMessageRequest = @"?";

		public const string notificationRemoteSessionModeChanged = @"notificationRemoteSessionModeChanged";

		public const string defaultTelnetPort = @"59999";



		public const string archiveRemoteSessionActivationMode = @"archiveRemoteSessionActivationMode";
		public const string archiveFaceDetectionMode = @"archiveFaceDetectionMode";
		public const string archiveRedialMode = @"archiveRedialMode";

		public const string archiveModulesLogLevels = @"archiveModulesLogLevels";

		public const string moduleApplication = @"openpeer_application";
		public const string moduleSDK = @"openpeer_sdk";
		public const string moduleMedia = @"openpeer_media";
		public const string moduleWebRTC = @"openpeer_webrtc";
		public const string moduleCore = @"openpeer_core";
		public const string moduleStackMessage = @"openpeer_stack_message";
		public const string moduleStack = @"openpeer_stack";
		public const string moduleServices = @"openpeer_services";
		public const string moduleServicesWire = @"openpeer_services_wire";
		public const string moduleServicesIce = @"openpeer_services_ice";
		public const string moduleServicesTurn = @"openpeer_services_turn";
		public const string moduleServicesRudp = @"openpeer_services_rudp";
		public const string moduleServicesHttp = @"openpeer_services_http";
		public const string moduleServicesMls = @"openpeer_services_mls";
		public const string moduleServicesTcp = @"openpeer_services_tcp_messaging";
		public const string moduleServicesTransport = @"openpeer_services_transport_stream";
		public const string moduleZsLib = @"zsLib";
		public const string moduleZsLibSocket = @"zsLib_socket";
		public const string moduleJavaScript = @"openpeer_javascript";
	


	}
}

