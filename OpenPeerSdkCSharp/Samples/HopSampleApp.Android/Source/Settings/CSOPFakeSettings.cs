
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.Text.Format;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Util;
using Org.Json;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;
namespace HopSampleApp
{
	public class CSOPFakeSettings
	{
		public static String createForceDashSetting()
		{
			try {
				JSONObject parent = new JSONObject();
				JSONObject jsonObject = new JSONObject();

				jsonObject.Put("openpeer/core/authorized-application-id-split-char", "-");
				parent.Put("root", jsonObject);
				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			} catch (JSONException e) {
				e.PrintStackTrace();
				return "";
			}
		}

		public static String createFakeApplicationSettings()
		{
			//		{
			//			"root": {
			//				"outerFrameURL": "http://jsouter-v1-beta-1-i.hcs.io/identity.html?view=choose&federated=false",
			//				"identityProviderDomain": "identity-v1-beta-1-i.hcs.io",
			//				"identityFederateBaseURI": "identity://identity-v1-beta-1-i.hcs.io/",
			//				"namespaceGrantServiceURL": "http://jsouter-v1-beta-1-i.hcs.io/grant.html",
			//				"lockBoxServiceDomain": "lockbox-v1-beta-1-i.hcs.io",
			//				"defaultOutgoingTelnetServer": "tcp-logger-v1-beta-1-i.hcs.io:8055"
			//			}
			//		}

			try {
				JSONObject parent = new JSONObject();
				JSONObject jsonObject = new JSONObject();

				jsonObject.Put("openpeer/common/application-name", "OpenPeer Native Sample App");
				jsonObject.Put("openpeer/common/application-image-url", "http://fake-image-url");
				jsonObject.Put("openpeer/common/application-url", "http://fake-application-url");

				Time expires = new Time ();
				expires.Set(30, 5, 2014);
				//rel-dev2
				//jsonObject.put("openpeer/calculated/authorizated-application-id", OPStack.createAuthorizedApplicationID("com.openpeer.nativeApp", "8f1ff375433b6e11026cb806a32ae4d04a59d7b1", expires ));
				//lespaul
				jsonObject.Put("openpeer/calculated/authorizated-application-id", OPStack.CreateAuthorizedApplicationID("com.openpeer.nativeApp", "14b2c9df6713df465d97d0736863c42964faa678", expires ));

				jsonObject.Put("openpeer/calculated/user-agent", "OpenPeerNativeSampleApp");
				jsonObject.Put("openpeer/calculated/device-id", Utility.GetDeviceID(LoginManager.mContext));
				jsonObject.Put("openpeer/calculated/os",Utility.GetAndroidVersionRelease());
				jsonObject.Put("openpeer/calculated/system",Utility.GetDeviceModelName());
				jsonObject.Put("openpeer/calculated/instance-id", Utility.GetGUIDInstanceID());
				parent.Put("root", jsonObject);
				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			} catch (JSONException e) {
				e.PrintStackTrace();
				return "";
			}

		}

		public static String createHttpSettings()
		{
			try {
				JSONObject parent = new JSONObject();
				JSONObject jsonObject = new JSONObject();

				jsonObject.Put("openpeer/stack/bootstrapper-force-well-known-over-insecure-http", "true");
				jsonObject.Put("openpeer/stack/bootstrapper-force-well-known-using-post", "true");
				parent.Put("root", jsonObject);

				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			} catch (JSONException e) {
				e.PrintStackTrace();
				return "";
			}
		}


	}
}

