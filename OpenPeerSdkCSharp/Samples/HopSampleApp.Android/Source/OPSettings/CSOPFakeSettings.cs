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
		#region createForceDashSetting
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

		#endregion

		#region createFakeApplicationSettings

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
				jsonObject.Put("openpeer/calculated/device-id", CSUtility.GetDeviceID(LoginManager.mContext));
				jsonObject.Put("openpeer/calculated/os",CSUtility.GetAndroidVersionRelease());
				jsonObject.Put("openpeer/calculated/system",CSUtility.GetDeviceModelName());
				jsonObject.Put("openpeer/calculated/instance-id", CSUtility.GetGUIDInstanceID());
				parent.Put("root", jsonObject);
				Log.Debug("output", parent.ToString(2));
				return parent.ToString(2);
			} catch (JSONException e) {
				e.PrintStackTrace();
				return "";
			}

		}

		#endregion

		#region createHttpSettings

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

		#endregion

	}
}

