package com.openpeer.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import com.openpeer.javaapi.OPStack;

import android.content.Context;
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
	// TODO add configuration items in
	private static final String PATH_CONFIG_FILE = "openpeersdk.properties";
	private static final String PREFIX_APP_COMMON_SETTING = "openpeer/common/";
	private static final String PREFIX_APP_CALCULATED_SETTING = "openpeer/calculated/";

	private static final String KEY_APP_NAME = "application-name";
	private static final String KEY_APP_IMAGE_URL = "application-image-url";
	private static final String KEY_APP_APPLICATION_URL = "application-url";

	private static final String KEY_OUTER_FRAME_URL = "outerFrameURL";
	private static final String KEY_IDENTITY_PROVIDE_DOMAIN = "identityProviderDomain";
	private static final String KEY_IDENTITY_BASE_URI = "identityFederateBaseURI";
	private static final String KEY_NAMESPACE_GRANT_SERVICE_URL = "namespaceGrantServiceURL";

	private static final long DURATION_ONE_MONTH_IN_MILLIS = 30 * 24 * 60 * 60
			* 1000;

	public static boolean debug = true;
	private Context mContext;
	private Properties mProperties = new Properties();
	private static OPSdkConfig instance;

	private OPSdkConfig() {

	}

	public static OPSdkConfig getInstance(Context context) {
		if (instance == null) {
			instance = new OPSdkConfig();
			instance.mContext = context;
			instance.init(context);
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

	public String getAPPSettingsString() {
		try {
			JSONObject parent = new JSONObject();
			JSONObject jsonObject = new JSONObject();

			Log.d("login", "getAPPSettingsString mProperties" + mProperties);
			Log.d("login",
					"getAPPSettingsString " + mProperties.propertyNames());

			jsonObject.put(PREFIX_APP_COMMON_SETTING + KEY_APP_NAME,
					mProperties.getProperty(KEY_APP_NAME));
			jsonObject.put(PREFIX_APP_COMMON_SETTING + KEY_APP_IMAGE_URL,
					mProperties.getProperty(KEY_APP_IMAGE_URL));
			jsonObject.put(PREFIX_APP_COMMON_SETTING + KEY_APP_APPLICATION_URL,
					mProperties.getProperty(KEY_APP_APPLICATION_URL));

			Time expires = new Time();
			expires.set(System.currentTimeMillis()
					+ DURATION_ONE_MONTH_IN_MILLIS);
			// expires.set(30, 6, 2014);
			// rel-dev2
			// jsonObject.put("openpeer/calculated/authorizated-application-id",
			// OPStack.createAuthorizedApplicationID("com.openpeer.nativeApp",
			// "8f1ff375433b6e11026cb806a32ae4d04a59d7b1", expires ));
			// lespaul
			jsonObject
					.put("openpeer/calculated/authorizated-application-id",
							OPStack.createAuthorizedApplicationID(
									"com.openpeer.nativeApp",
									"14b2c9df6713df465d97d0736863c42964faa678",
									expires));

			jsonObject.put("openpeer/calculated/user-agent",
					"OpenPeerNativeSampleApp");
			jsonObject.put("openpeer/calculated/device-id", Secure.getString(
					mContext.getContentResolver(), Secure.ANDROID_ID));
			jsonObject.put("openpeer/calculated/os",
					android.os.Build.VERSION.RELEASE);
			jsonObject
					.put("openpeer/calculated/system", android.os.Build.MODEL);
			parent.put("root", jsonObject);
			Log.d("output", parent.toString(2));
			return parent.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}

	}

	public void init(Context context) {
		// try {
		/**
		 * getAssets() Return an AssetManager instance for your application's
		 * package. AssetManager Provides access to an application's raw asset
		 * files;
		 */
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

}
