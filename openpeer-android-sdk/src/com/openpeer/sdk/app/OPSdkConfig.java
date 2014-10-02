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
    private static final String KEY_APP_ID = "appId";
    private static final String KEY_APP_APPKEY = "appKey";
    private static final String KEY_CHAT_MODE = "application/chatMode";

    private static final long DURATION_ONE_MONTH_IN_MILLIS = 30 * 24 * 60 * 60
            * 1000;

    public static boolean debug = true;
    private Context mContext;
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

    public String getAPPSettingsString() {
        try {
            JSONObject parent = new JSONObject();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(KEY_APP_NAME,
                    mProperties.getProperty(KEY_APP_NAME));
            jsonObject.put(KEY_APP_IMAGE_URL,
                    mProperties.getProperty(KEY_APP_IMAGE_URL));
            jsonObject.put(KEY_APP_APPLICATION_URL,
                    mProperties.getProperty(KEY_APP_APPLICATION_URL));

            Time expires = new Time();
            expires.set(System.currentTimeMillis()
                    + DURATION_ONE_MONTH_IN_MILLIS);
            jsonObject
                    .put("openpeer/calculated/authorizated-application-id",
                            OPStack.createAuthorizedApplicationID(
                                    mProperties.getProperty(KEY_APP_ID),
                                    mProperties.getProperty(KEY_APP_APPKEY),
                                    expires));

            jsonObject.put("openpeer/calculated/user-agent",
                    mProperties.get(KEY_USER_AGENT));
            jsonObject.put("openpeer/calculated/device-id", Secure.getString(
                    mContext.getContentResolver(), Secure.ANDROID_ID));
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
        // try {
        /**
         * getAssets() Return an AssetManager instance for your application's package. AssetManager Provides access to an application's raw
         * asset files;
         */
        instance.mContext = context;
        AssetManager assetManager = context.getAssets();
        /**
         * Open an asset using ACCESS_STREAMING mode. This
         */
        InputStream inputStream;
        try {
            inputStream = assetManager.open(PATH_CONFIG_FILE);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        /**
         * Loads properties from the specified InputStream,
         */
        try {
            mProperties.load(inputStream);
            OPSettings.apply(getAPPSettingsString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public GroupChatMode getGroupChatMode() {
        return GroupChatMode.valueOf(mProperties.getProperty(KEY_CHAT_MODE));
    }

}
