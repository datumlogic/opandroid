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
using Org.Json;
using Android.Util;
using Android.Text.Format;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;

namespace HopSampleApp
{
	/// <summary>
	/// ###############################################################################
	///                                CSSETTINGS
	/// ###############################################################################
	/// CSSettings class contains methods:
	/// -------------------------------------------------------------------------------
	/// - getOuterFrameURL
	/// - getIdentityProviderDomain
	/// - getIdentityBaseUri
	/// - getNamespaceGrantServiceUrl
	/// - getGrantID
	/// - getApplicationName
	/// - getApplicationImageUrl
	/// - getApplicationUserAgent
	/// - getApplicationAuthDomain
	/// - getApplicationAuthID
	/// - getAPPSettingsString
	/// - createHttpSettings
	/// - createForceDashSetting
	/// -------------------------------------------------------------------------------
	/// Initialize methods for Assets
	/// -------------------------------------------------------------------------------
	/// - init(context)
	/// -------------------------------------------------------------------------------
	/// <remarks>
	/// This class is designed for loading all application settings and frames.
	/// This class also ensures that the all application propertyes bee 100% loaded,
	/// if the reading of assets file fail class will use alternative method of loading
	/// properties which is defined in returning string in order to prevent falling 
	/// of application.
	/// -------------------------------------------------------------------------------
	/// ADDITION:Class has detailed description of method work process so 
	/// you'll know what's going on when the method is called.
	/// </remarks>
	/// </summary>
	public class CSSettings
	{
		#region 
		public static Boolean debug = true;
		string Outer;
		string ProviderDomain;
		string BaseUri;
		string GrantServiceUrl;
		string IdGrant;
		string AppName;
		string AppNameUrl;
		string AppUrl;
		string UserAgent;
		string AuthAppDomain;
		string AuthAppID;
		#endregion
		//Properties
		Java.Util.Properties mProperties=new Java.Util.Properties();

		#region Initialize Assets
		public void  InitializeAssets(Context context)
		{
			Log.Debug ("TEST ASSETS","ASSETS SETTINGS ARE LOADED");

			System.IO.Stream inputStream;
			try
			{
				Log.Debug("TEST ASSETS","OPENPEER ASSETS ARE LOADED");
				inputStream=context.Assets.Open(CSAppConsts.path_config_file);

			}
			catch
			{
				Log.Error ("Error","Error open");
				return;
			}

			try
			{
				Log.Debug("TEST ASSETS","OPENPEER SAMPLE APP PROPERTIES ARE LOADED FROM ASSETS");
				mProperties.Load(inputStream);
			} 
			catch
			{

				Log.Error ("Error","Error Load");
			}

		}
		#endregion

		#region Outer Frame URL
		public String getOuterFrameURL()
		{
			try
			{
				Outer=mProperties.GetProperty (CSAppConsts.settingsKeyOuterFrameURL);
				if(Outer != null)
				{
					Log.Debug ("[TEST ASSETS] OUTER FRAME URL ARE LOADED FROM ASSETS",Outer);
					return Outer;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","OUTER FRAME URL ARE LOADED MANUAL");
					return "http://jsouter-v1-rel-lespaul-i.hcs.io/identity.html?view=choose?reload=true";
				}
			}
			catch(Exception Error)
			{
				Log.Error ("OUTER FRAME URL ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);

			}
		}
		#endregion

		#region Identity Provider Domain
		public String getIdentityProviderDomain()
		{
			try
			{
				//TODO:
				ProviderDomain=mProperties.GetProperty(CSAppConsts.settingsKeyIdentityProviderDomain);
				if(ProviderDomain != null)
				{
					Log.Debug ("[TEST ASSETS] IDENTITY PROVIDER DOMAIN ARE LOADED FROM ASSETS",ProviderDomain);
					return ProviderDomain;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","IDENTITY PROVIDER DOMAIN ARE LOADED MANUAL");
					return "identity-v1-rel-lespaul-i.hcs.io";
				}
			}
			catch(Exception Error)
			{
				Log.Error("IDENTITY PROVIDER DOMAIN ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}

		}
		#endregion

		#region Identity Base Uri
		public String getIdentityBaseUri()
		{
			try
			{
				BaseUri= mProperties.GetProperty(CSAppConsts.settingsKeyIdentityFederateBaseURI);
				if(BaseUri != null)
				{
					Log.Debug ("[TEST ASSETS] IDENTITY BASE URI ARE LOADED FROM ASSETS",BaseUri);
					return BaseUri;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","IDENTITY BASE URI ARE LOADED MANUAL");
					return "identity://identity-v1-rel-lespaul-i.hcs.io/";

				}

			}
			catch(Exception Error)
			{
				Log.Error ("IDENTITY BASE URI ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}
		}
		#endregion

		#region Namespace Grant Service Url
		public String getNamespaceGrantServiceUrl()
		{
			try
			{
				GrantServiceUrl=mProperties.GetProperty(CSAppConsts.settingsKeyGrantServiceURL);
				if(GrantServiceUrl != null)
				{
					Log.Debug ("[TEST ASSETS] NAMESPACE GRANT SERVICE URL ARE LOADED FROM ASSETS ",GrantServiceUrl);
					return GrantServiceUrl;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","NAMESPACE GRANT SERVICE URL ARE LOADED MANUAL");
					return "http://jsouter-v1-rel-lespaul-i.hcs.io/grant.html";
				}
			}
			catch(Exception Error)
			{
				Log.Error ("NAMESPACE GRANT SERVICE URL ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}

		}
		#endregion

		#region Grant id
		public String getGrantID()
		{
			try
			{
				IdGrant=mProperties.GetProperty(CSAppConsts.settingsKeyGrantId);
				if(IdGrant != null)
				{
					Log.Debug ("[TEST ASSETS] GRANT ID ARE LOADED FROM ASSETS ",IdGrant);
					return IdGrant;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","GRANT ID ARE LOADED MANUAL");
					return "bojanGrantID";
				}

			}
			catch(Exception Error)
			{
				Log.Error ("GRANT ID ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}
		}
		#endregion

		#region Application Name
		public String getApplicationName()
		{
			try
			{
				AppName=mProperties.GetProperty(CSAppConsts.settingsKeyAppName);
				if(AppName != null)
				{
					Log.Debug ("[TEST ASSETS] APPLICATION NAME LOADED FROM ASSETS ",AppName);
					return AppName;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","APPLICATION NAME ARE LOADED MANUAL");
					return "OpenPeer CSHARP Sample App";
				}
			}
			catch(Exception Error)
			{
				Log.Error ("APPLICATION NAME ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}
		
		}
		#endregion

		#region Application Image url
		public String getApplicationImageUrl()
		{
			try
			{
				AppNameUrl=mProperties.GetProperty(CSAppConsts.settingsKeyAppImageURL);
				if(AppNameUrl != null)
				{
					Log.Debug ("[TEST ASSETS] APPLICATION NAME URL LOADED FROM ASSETS ",AppNameUrl);
					return AppNameUrl;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","APPLICATION NAME URL ARE LOADED MANUAL");
					return "http://fake-image-url";
				}
			}
			catch(Exception Error)
			{
				Log.Error ("APPLICATION NAME URL ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}
		}

		#endregion

		#region Application Url
		public String getApplicationUrl()
		{
			try
			{
				AppUrl=mProperties.GetProperty(CSAppConsts.settingsKeyAppURL);
				if(AppUrl != null)
				{
					Log.Debug ("[TEST ASSETS] APPLICATION URL LOADED FROM ASSETS ",AppUrl);
					return AppUrl;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","APPLICATION URL ARE LOADED MANUAL");
					return "http://fake-application-url";
				}
			}
			catch(Exception Error)
			{
				Log.Error ("APPLICATION URL ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}
		}

		#endregion

		#region Application User Agent
		public String getApplicationUserAgent()
		{
			try
			{
				UserAgent=mProperties.GetProperty(CSAppConsts.settingsKeyAppUserAgent);
				if(UserAgent != null)
				{
					Log.Debug ("[TEST ASSETS] APPLICATION USER AGENT LOADED FROM ASSETS ",UserAgent);
					return UserAgent;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","APPLICATION USER AGENT ARE LOADED MANUAL");
					return "OpenPeerNativeSampleApp";
				}
			}
			catch(Exception Error)
			{
				Log.Error ("APPLICATION USER AGENT ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}
		}
		#endregion

		#region Authorizated application domain
		public String getApplicationAuthDomain()
		{
			try
			{
				AuthAppDomain=mProperties.GetProperty(CSAppConsts.settingsKeyAuthAppDomain);
				if(AuthAppDomain != null)
				{
					Log.Debug ("[TEST ASSETS] APPLICATION  AUTHORIZATED DOMAIN LOADED FROM ASSETS ",AuthAppDomain);
					return AuthAppDomain;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","APPLICATION  AUTHORIZATED DOMAIN ARE LOADED MANUAL");
					return "com.openpeer.nativeApp";
				}
			}
			catch(Exception Error)
			{
				Log.Error ("APPLICATION AUTHORIZATED DOMAIN ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}
		}

		#endregion

		#region Authorizated application id
		public String getApplicationAuthID()
		{
			try
			{
				AuthAppID=mProperties.GetProperty(CSAppConsts.settingsKeyAuthAppId);
				if(AuthAppID != null)
				{
					Log.Debug ("[TEST ASSETS] APPLICATION  AUTHORIZATED ID LOADED FROM ASSETS ",AuthAppID);
					return AuthAppID;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","APPLICATION  AUTHORIZATED ID ARE LOADED MANUAL");
					return "14b2c9df6713df465d97d0736863c42964faa678";
				}
			}
			catch(Exception Error)
			{
				Log.Error ("APPLICATION AUTHORIZATED ID ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}
		}

		#endregion

		#region Application Settings
		public String getAPPSettingsString()
		{
			try
			{
				JSONObject parent=new JSONObject();
				JSONObject jsonObject=new JSONObject();
				Log.Debug("Login","getAPPSettingsString mProperties" + mProperties);
				Log.Debug("Login","getAPPSettingsString " + mProperties.PropertyNames());
				jsonObject.Put("openpeer/common/application-name",getApplicationName());
				jsonObject.Put("openpeer/common/application-image-url",getApplicationImageUrl());
				jsonObject.Put("openpeer/common/application-url",getApplicationUrl());
				Time expires = new Time();
				expires.Set(CSUtility.currentTimeMillis() +CSAppConsts.duration_one_month_in_millis);
				//expires.Set(30, 8, 2014);
				jsonObject.Put("openpeer/calculated/authorizated-application-id",OPStack.CreateAuthorizedApplicationID(getApplicationAuthDomain(),getApplicationAuthID(),expires));

				jsonObject.Put("openpeer/calculated/user-agent",getApplicationUserAgent());
				jsonObject.Put("openpeer/calculated/device-id",CSUtility.GetDeviceID(LoginManager.mContext));//Android.Provider.Settings.Secure.GetString (mContext.ContentResolver, Android.Provider.Settings.Secure.AndroidId));
				jsonObject.Put("openpeer/calculated/os",CSUtility.GetAndroidVersionRelease());
				jsonObject.Put("openpeer/calculated/system", CSUtility.GetDeviceModelName());
				jsonObject.Put("openpeer/calculated/instance-id", CSUtility.GetGUIDInstanceID());
				parent.Put("root", jsonObject);
				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			}
			catch
			{
				Log.Error ("Error JSON","error");
				return"";
			}
		}
		#endregion

		#region Http Settings
		public String createHttpSettings()
		{
			try {
				JSONObject parent = new JSONObject();
				JSONObject jsonObject = new JSONObject();

				jsonObject.Put("openpeer/stack/bootstrapper-force-well-known-over-insecure-http","true");
				jsonObject.Put("openpeer/stack/bootstrapper-force-well-known-using-post","true");
				parent.Put("root", jsonObject);
				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			}
			catch (JSONException e) 
			{
				e.PrintStackTrace();
				return "";
			}
		}
		#endregion

		#region Force Dash Setting
		public String createForceDashSetting()
		{
			try
			{
				JSONObject parent = new JSONObject();
				JSONObject jsonObject = new JSONObject();

				jsonObject.Put("openpeer/core/authorized-application-id-split-char", "-");
				parent.Put("root", jsonObject);
				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			} 
			catch (JSONException e)
			{
				e.PrintStackTrace();
				return "";
			}
		}
		#endregion

		#region Singleton pattern
		private static CSSettings instance;

		private CSSettings(){	}

		public static CSSettings SharedCSSettings()
		{
			if (instance == null)
				instance = new CSSettings();
			return instance;
		}

		#endregion

	}
}

