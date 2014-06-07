package com.openpeer.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.delegates.OPCacheDelegateImplementation;
import com.openpeer.javaapi.OPCache;
import com.openpeer.javaapi.OPCacheDelegate;
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
//	static {
//		try {
//			System.loadLibrary("z_shared");
//			System.loadLibrary("openpeer");
//
//		} catch (UnsatisfiedLinkError use) {
//			use.printStackTrace();
//		}
//	}
	private OPHelper(Context context) {
		mContext = context;
	}

	public static OPHelper getInstance(Context context) {
		if (instance == null) {
			instance = new OPHelper(context);
		}
		return instance;
	}

	public void enableTelnetLogging(){
		OPLogger.installTelnetLogger(59999, 60, true);
	}
	public void init() {
		OPMediaEngine.init(mContext);
		//TODO: Add delegate when implement mechanism to post events to the android GUI thread
		OPStackMessageQueue stackMessageQueue = OPStackMessageQueue.singleton(); 
//		stackMessageQueue = new OPStackMessageQueue();
//		stackMessageQueue.interceptProcessing(null);
		OPStack stack = OPStack.singleton();
		if(OPSdkConfig.debug){
			enableTelnetLogging();
		}
		//		
		OPCacheDelegate cacheDelegate =  OPCacheDelegateImplementation.getInstance(mContext);
		CallbackHandler.getInstance().registerCacheDelegate(cacheDelegate);
		OPCache.setup(cacheDelegate);
		
		OPSettings.applyDefaults();
		
		String httpSettings = createHttpSettings();
		OPSettings.apply(httpSettings);
		
		String forceDashSettings = createForceDashSetting();
		OPSettings.apply(forceDashSettings);
		
		OPSettings.apply(OPSdkConfig.getInstance(mContext).getAPPSettingsString());
				
		stack.setup(null, null);
	}
	
	private String createHttpSettings()
	{
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
	
	private String createForceDashSetting()
	{
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
	
}
