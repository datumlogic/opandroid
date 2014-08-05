package com.openpeer.sdk.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;

import com.openpeer.javaapi.OPCache;
import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.OPSettings;
import com.openpeer.javaapi.OPStack;
import com.openpeer.javaapi.OPStackMessageQueue;
import com.openpeer.javaapi.VideoOrientations;
import com.openpeer.sdk.datastore.OPDatastoreDelegate;
import com.openpeer.sdk.datastore.OPDatastoreDelegateImplementation;

import com.openpeer.sdk.delegates.OPCacheDelegateImplementation;

/**
 * 
 * 
 *
 */
public class OPHelper {
	private static final String TAG = OPHelper.class.getSimpleName();
	private static OPHelper instance;
	Context mContext;

	public Context getApplicationContext() {
		return mContext;
	}

	public static OPHelper getInstance() {
		if (instance == null) {
			instance = new OPHelper();
		}
		return instance;
	}

	public void toggleOutgoingTelnetLogging(boolean enable, String url) {
		if (enable && !TextUtils.isEmpty(url)) {
			OPLogger.setLogLevel(OPLogLevel.LogLevel_Trace);
			// OPLogger.setLogLevel("openpeer_webrtc", OPLogLevel.LogLevel_Basic);
			// OPLogger.setLogLevel("zsLib_socket", OPLogLevel.LogLevel_Insane);
			String deviceId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
			String instanceId = OPSdkConfig.getInstanceid();
			String telnetLogString = deviceId + "-" + instanceId + "\n";
			Log.d("output", "Bruce Outgoing log string = " + telnetLogString);
			OPLogger.installOutgoingTelnetLogger(url, true, telnetLogString);
		} else {
			// OPLogger.setLogLevel(OPLogLevel.LogLevel_None);
			// OPLogger.setLogLevel("openpeer_webrtc", OPLogLevel.LogLevel_None);
			// OPLogger.setLogLevel("zsLib_socket", OPLogLevel.LogLevel_None);
			OPLogger.uninstallOutgoingTelnetLogger();
		}
	}

	public void toggleFileLogger(boolean enabled, String fileName) {
		// OPLogger.setLogLevel(OPLogLevel.LogLevel_Trace);
		// OPLogger.setLogLevel("openpeer_webrtc", OPLogLevel.LogLevel_None);
		// OPLogger.setLogLevel("zsLib_socket", OPLogLevel.LogLevel_Insane);

		// OPLogger.setLogLevel("openpeer_services_transport_stream",
		// OPLogLevel.LogLevel_None);
		// OPLogger.setLogLevel("openpeer_stack", OPLogLevel.LogLevel_None);
		// OPLogger.installTelnetLogger(59999, 60, true);
		if (enabled && !TextUtils.isEmpty(fileName)) {
			OPLogger.installFileLogger(fileName, true);
		} else {
			OPLogger.uninstallDebuggerLogger();
		}
	}

	public void toggleTelnetLogging(boolean enable, int port) {
		if (enable) {
			
			OPLogger.installTelnetLogger(port, 60, true);
		} else {
			OPLogger.uninstallTelnetLogger();
		}
	}

	private void initMediaEngine() {
		long start = SystemClock.uptimeMillis();
		OPMediaEngine.getInstance().setEcEnabled(true);
		OPMediaEngine.getInstance().setAgcEnabled(true);
		OPMediaEngine.getInstance().setNsEnabled(false);
		OPMediaEngine.getInstance().setMuteEnabled(false);
		OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
		OPMediaEngine.getInstance().setContinuousVideoCapture(true);
		OPMediaEngine.getInstance().setDefaultVideoOrientation(VideoOrientations.VideoOrientation_Portrait);
		OPMediaEngine.getInstance().setRecordVideoOrientation(VideoOrientations.VideoOrientation_LandscapeRight);
		OPMediaEngine.getInstance().setFaceDetection(false);

		Log.d("performance", "initMediaEngine time " + (SystemClock.uptimeMillis() - start));
		// OPMediaEngine.init(mContext);
	}

	public void init1() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				OPMediaEngine.init(mContext);

				OPStackMessageQueue stackMessageQueue = OPStackMessageQueue.singleton();
				// stackMessageQueue = new OPStackMessageQueue();
				// stackMessageQueue.interceptProcessing(null);
				OPStack stack = OPStack.singleton();
				OPSdkConfig.getInstance().init(mContext);

				//
				OPCacheDelegate cacheDelegate = OPCacheDelegateImplementation.getInstance(mContext);
				OPCache.setup(cacheDelegate);

				OPSettings.applyDefaults();

				String httpSettings = createHttpSettings();
				OPSettings.apply(httpSettings);

				String forceDashSettings = createForceDashSetting();
				OPSettings.apply(forceDashSettings);

				OPSettings.apply(OPSdkConfig.getInstance().getAPPSettingsString());

				stack.setup(null, null);

				initialized = true;
				if (initListener != null) {
					initListener.onInitialized();
				}
			}

		});
		thread.run();
	}

	public static boolean initialized = false;
	public InitListener initListener;

	public interface InitListener {
		public void onInitialized();
	}

	public void init(Context context, OPDatastoreDelegate datastoreDelegate) {
		long start = SystemClock.uptimeMillis();

		mContext = context;
		// enableTelnetLogging();
		// initMediaEngine();
		if (datastoreDelegate != null) {
			OPDataManager.getInstance().init(datastoreDelegate);
		} else {
			OPDataManager.getInstance().init(OPDatastoreDelegateImplementation.getInstance().init(mContext));
		}
		// TODO: Add delegate when implement mechanism to post events to the
		// android GUI thread
		init1();

	}

	private String createHttpSettings() {
		try {
			JSONObject parent = new JSONObject();
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("openpeer/stack/bootstrapper-force-well-known-over-insecure-http", "true");
			jsonObject.put("openpeer/stack/bootstrapper-force-well-known-using-post", "true");
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

			jsonObject.put("openpeer/core/authorized-application-id-split-char", "-");
			parent.put("root", jsonObject);
			Log.d("output", parent.toString(2));
			return parent.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void sendBroadcast(Intent intent) {
		mContext.sendBroadcast(intent);
	}

	public static final int MODE_CONTACTS_BASED = 0;
	public static final int MODE_GROUP_BASED = 1;



	private boolean mAppInBackground;

	public boolean isAppInBackground() {
		return mAppInBackground;
	}


	public void onEnteringForeground() {
		mAppInBackground = false;
	}

	public void onEnteringBackground() {
		mAppInBackground = true;
	}

}
