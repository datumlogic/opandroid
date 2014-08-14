package com.openpeer.sdk.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import com.openpeer.javaapi.OPStack;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.provider.Settings.Secure;
import android.text.format.Time;
import android.util.Log;

/**
 * 
 * Global SDK configurations. TODO: put them in a file like
 * openpeersdk.properties
 * 
 * All configurable items should have a default value and the value will be
 * overwritten if the configuration file exists
 */
public class OPSdkConfig {

	private static final String PREF_NAME = "sdk.pref";

	// TODO add configuration items in
	private static final String PATH_CONFIG_FILE = "openpeersdk.properties";
	private static final String KEY_GRANT_ID = "grantId";

	private static final String PREFIX_APP_COMMON_SETTING = "openpeer/common/";
	private static final String PREFIX_APP_CALCULATED_SETTING = "openpeer/calculated/";

	private static final String KEY_APP_NAME = "application-name";
	private static final String KEY_APP_IMAGE_URL = "application-image-url";
	private static final String KEY_APP_APPLICATION_URL = "application-url";

	private static final String KEY_OUTER_FRAME_URL = "outerFrameURL";
	private static final String KEY_IDENTITY_PROVIDE_DOMAIN = "identityProviderDomain";
	private static final String KEY_IDENTITY_BASE_URI = "identityFederateBaseURI";
	private static final String KEY_NAMESPACE_GRANT_SERVICE_URL = "namespaceGrantServiceURL";
	private static final String KEY_LOCKBOX_SERVICE_DOMAIN = "lockBoxServiceDomain";
	private static final String KEY_APP_ID = "appId";
	private static final String KEY_APP_APPKEY = "appKey";
	private static final Object KEY_USER_AGENT = "user-agent";

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
	public String getAppId(){
		return mProperties.getProperty(KEY_APP_ID);
	}

	public static OPSdkConfig getInstance() {
		if (instance == null) {
			instance = new OPSdkConfig();
		}
		return instance;
	}

	public String getOuterFrameUrl() {
		return mProperties.getProperty(KEY_OUTER_FRAME_URL);
	}

	public String getIdentityProviderDomain() {
		return mProperties.getProperty(KEY_IDENTITY_PROVIDE_DOMAIN);
	}

	public String getIdentityBaseUri() {
		return mProperties.getProperty(KEY_IDENTITY_BASE_URI);
	}

	public String getNamespaceGrantServiceUrl() {
		return mProperties.getProperty(KEY_NAMESPACE_GRANT_SERVICE_URL);
	}

	public String getLockboxServiceDomain() {
		return mProperties.getProperty(KEY_LOCKBOX_SERVICE_DOMAIN);
	}

	public String getGrantId() {
		String id = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE).getString(KEY_GRANT_ID, null);
		if (id == null) {
			id = java.util.UUID.randomUUID().toString();
			saveGrantId(id);
		}
		return id;
	}

	public String getAPPSettingsString() {
		try {
			JSONObject parent = new JSONObject();
			JSONObject jsonObject = new JSONObject();

			jsonObject.put(PREFIX_APP_COMMON_SETTING + KEY_APP_NAME,
					mProperties.getProperty(KEY_APP_NAME));
			jsonObject.put(PREFIX_APP_COMMON_SETTING + KEY_APP_IMAGE_URL,
					mProperties.getProperty(KEY_APP_IMAGE_URL));
			jsonObject.put(PREFIX_APP_COMMON_SETTING + KEY_APP_APPLICATION_URL,
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		/**
		 * Loads properties from the specified InputStream,
		 */
		try {
			mProperties.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void saveGrantId(String grantId) {
		SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(KEY_GRANT_ID, grantId).apply();

	}

}
