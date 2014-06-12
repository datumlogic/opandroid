package com.openpeer.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.openpeer.datastore.OPDatastoreDelegate;
import com.openpeer.datastore.OPDatastoreDelegateImplementation;
import com.openpeer.delegates.CallbackHandler;
import com.openpeer.delegates.OPCacheDelegateImplementation;
import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPCache;
import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPSettings;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackMessageQueue;

/**
 * 
 * 
 *
 */
public class OPHelper {
	private static final String TAG = OPHelper.class.getSimpleName();
	private static OPHelper instance;
	Context mContext;

	// static {
	// try {
	// System.loadLibrary("z_shared");
	// System.loadLibrary("openpeer");
	//
	// } catch (UnsatisfiedLinkError use) {
	// use.printStackTrace();
	// }
	// }

	public static OPHelper getInstance() {
		if (instance == null) {
			instance = new OPHelper();
		}
		return instance;
	}

	public void enableTelnetLogging() {
		OPLogger.setLogLevel(OPLogLevel.LogLevel_Trace);
		OPLogger.setLogLevel("openpeer_webrtc", OPLogLevel.LogLevel_Basic);
		OPLogger.setLogLevel("zsLib_socket", OPLogLevel.LogLevel_Insane);

		OPLogger.setLogLevel("openpeer_services_transport_stream",
				OPLogLevel.LogLevel_Insane);
		OPLogger.setLogLevel("openpeer_stack", OPLogLevel.LogLevel_Insane);
		OPLogger.installTelnetLogger(59999, 60, true);
		OPLogger.installFileLogger("/storage/emulated/0/HFLog1.txt", true);
	}

	public void init(Context context, OPDatastoreDelegate datastoreDelegate) {
		mContext = context;
		OPMediaEngine.init(mContext);
		if (datastoreDelegate != null) {
			OPDataManager.getInstance().init(datastoreDelegate);
		} else {
			OPDataManager.getInstance().init(
					OPDatastoreDelegateImplementation.getInstance().init(
							mContext));
		}
		// TODO: Add delegate when implement mechanism to post events to the
		// android GUI thread
		OPStackMessageQueue stackMessageQueue = OPStackMessageQueue.singleton();
		// stackMessageQueue = new OPStackMessageQueue();
		// stackMessageQueue.interceptProcessing(null);
		OPStack stack = OPStack.singleton();
		OPSdkConfig.getInstance().init(mContext);
		if (OPSdkConfig.debug) {
			enableTelnetLogging();
		}
		//
		OPCacheDelegate cacheDelegate = OPCacheDelegateImplementation
				.getInstance(mContext);
		CallbackHandler.getInstance().registerCacheDelegate(cacheDelegate);
		OPCache.setup(cacheDelegate);

		OPSettings.applyDefaults();

		String httpSettings = createHttpSettings();
		OPSettings.apply(httpSettings);

		String forceDashSettings = createForceDashSetting();
		OPSettings.apply(forceDashSettings);

		OPSettings.apply(OPSdkConfig.getInstance().getAPPSettingsString());

		stack.setup(null, null);

	}

	private String createHttpSettings() {
		try {
			JSONObject parent = new JSONObject();
			JSONObject jsonObject = new JSONObject();

			jsonObject
					.put("openpeer/stack/bootstrapper-force-well-known-over-insecure-http",
							"true");
			jsonObject.put(
					"openpeer/stack/bootstrapper-force-well-known-using-post",
					"true");
			parent.put("root", jsonObject);
			Log.d("output", parent.toString(2));
			return parent.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	private String createForceDashSetting() {
		try {
			JSONObject parent = new JSONObject();
			JSONObject jsonObject = new JSONObject();

			jsonObject.put(
					"openpeer/core/authorized-application-id-split-char", "-");
			parent.put("root", jsonObject);
			Log.d("output", parent.toString(2));
			return parent.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

}
