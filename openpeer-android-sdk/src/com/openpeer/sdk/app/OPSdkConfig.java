/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.sdk.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.provider.Settings.Secure;
import android.text.format.Time;

import com.openpeer.javaapi.OPSettings;
import com.openpeer.javaapi.OPStack;
import com.openpeer.sdk.model.GroupChatMode;
import com.openpeer.sdk.utils.AssetUtils;

/**
 * 
 * Global SDK configurations. TODO: put them in a file like openpeersdk.properties
 * 
 * All configurable items should have a default value and the value will be overwritten if the configuration file exists
 */
public class OPSdkConfig {

    private static final String PREF_NAME = "sdk.pref";

    // TODO add configuration items in
    private static final String PATH_CONFIG_FILE = "openpeersdk.properties";
    private static final String KEY_GRANT_ID = "openpeer/calculated/grantId";

    private static final String PREFIX_APP_COMMON_SETTING = "openpeer/common/";
    private static final String PREFIX_APP_CALCULATED_SETTING = "openpeer/calculated/";

    private static final String KEY_APP_NAME = PREFIX_APP_COMMON_SETTING
            + "application-name";
    private static final String KEY_APP_IMAGE_URL = PREFIX_APP_COMMON_SETTING
            + "application-image-url";
    private static final String KEY_APP_APPLICATION_URL = PREFIX_APP_COMMON_SETTING
            + "application-url";
    private static final Object KEY_USER_AGENT = PREFIX_APP_COMMON_SETTING
            + "user-agent";

    private static final String KEY_OUTER_FRAME_URL = "outerFrameURL";
    private static final String KEY_IDENTITY_PROVIDE_DOMAIN = "identityProviderDomain";
    private static final String KEY_IDENTITY_BASE_URI = "identityFederateBaseURI";
    private static final String KEY_NAMESPACE_GRANT_SERVICE_URL = "namespaceGrantServiceURL";
    private static final String KEY_LOCKBOX_SERVICE_DOMAIN = "lockBoxServiceDomain";
    private static final String KEY_APP_ID = "id";
    private static final String KEY_APP_APPKEY = "sharedSecret";
    private static final String KEY_CHAT_MODE = "application/chatMode";

    private static final long DURATION_ONE_MONTH_IN_MILLIS = 30 * 24 * 60 * 60
            * 1000;

    public static boolean debug = true;
    private Properties mProperties = new Properties();
    private static OPSdkConfig instance;
    private static final String instanceId = java.util.UUID.randomUUID()
            .toString();

    public static String getInstanceid() {
        return instanceId;
    }

    private OPSdkConfig() {

    }

    public String getAppId() {
        return OPHelper.getSettingsDelegate().getString(KEY_APP_ID);
    }

    public static OPSdkConfig getInstance() {
        if (instance == null) {
            instance = new OPSdkConfig();
        }
        return instance;
    }

    public String getOuterFrameUrl() {
        return OPHelper.getSettingsDelegate().getString(KEY_OUTER_FRAME_URL);
    }

    public String getIdentityProviderDomain() {
        return OPHelper.getSettingsDelegate().getString(
                KEY_IDENTITY_PROVIDE_DOMAIN);
    }

    public String getIdentityBaseUri() {
        return OPHelper.getSettingsDelegate().getString(KEY_IDENTITY_BASE_URI);
    }

    public String getNamespaceGrantServiceUrl() {
        return OPHelper.getSettingsDelegate().getString(
                KEY_NAMESPACE_GRANT_SERVICE_URL);
    }

    public String getLockboxServiceDomain() {
        return OPHelper.getSettingsDelegate().getString(
                KEY_LOCKBOX_SERVICE_DOMAIN);
    }

    public String getGrantId() {
        String id = OPHelper.getSettingsDelegate().getString(KEY_GRANT_ID);// mContext.getSharedPreferences(PREF_NAME,
        // Context.MODE_PRIVATE).getString(KEY_GRANT_ID, null);
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
            OPHelper.getSettingsDelegate().setString(KEY_GRANT_ID, id);
        }
        return id;
    }

    public static String getAPPSettingsString(Context context) {
        try {
            JSONObject parent = new JSONObject();
            JSONObject jsonObject = new JSONObject();

            Time expires = new Time();
            expires.set(System.currentTimeMillis()
                    + DURATION_ONE_MONTH_IN_MILLIS);
            jsonObject
                    .put("openpeer/calculated/authorizated-application-id",
                            OPStack.createAuthorizedApplicationID(
                                    OPHelper.getSettingsDelegate().getString(
                                            KEY_APP_ID),
                                    OPHelper.getSettingsDelegate().getString(
                                            KEY_APP_APPKEY),
                                    expires));

            jsonObject.put("openpeer/calculated/device-id", Secure.getString(
                    context.getContentResolver(), Secure.ANDROID_ID));
            jsonObject.put("openpeer/calculated/os",
                    android.os.Build.VERSION.RELEASE);
            jsonObject
                    .put("openpeer/calculated/system", android.os.Build.MODEL);
            jsonObject.put("openpeer/calculated/instance-id", instanceId);
            parent.put("root", jsonObject);
            return parent.toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void init(Context context) {
        try {
            byte[] bytes = AssetUtils.readAsset(context,
                    "app_settings_bojan.json");
            String str = new String(bytes, "UTF-8");
            OPSettings.apply(str);

            OPSettings.apply(getAPPSettingsString(context));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

    public GroupChatMode getGroupChatMode() {
        return GroupChatMode.valueOf(OPHelper.getSettingsDelegate().getString(
                KEY_CHAT_MODE));
    }

}
