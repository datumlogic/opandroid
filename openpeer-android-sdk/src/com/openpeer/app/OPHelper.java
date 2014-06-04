package com.openpeer.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.provider.Settings.Secure;
import android.text.format.Time;
import android.util.Log;

import com.openpeer.delegates.CallbackHandler;
import com.openpeer.delegates.OPCacheDelegateImplementation;
import com.openpeer.javaapi.OPCache;
import com.openpeer.javaapi.OPCacheDelegate;
import com.openpeer.javaapi.OPLogger;
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

	private OPHelper(Context context) {
		mContext = context;
	}

	public OPHelper getInstance(Context context) {
		if (instance == null) {
			instance = new OPHelper(context);
		}
		return instance;
	}

	public void enableTelnetLogging(){
		OPLogger.installTelnetLogger(59999, 60, true);
	}
	public void init() {
		OPSdkConfig.init(mContext);
		//TODO: Add delegate when implement mechanism to post events to the android GUI thread
		OPStackMessageQueue stackMessageQueue = OPStackMessageQueue.singleton(); 
		stackMessageQueue = new OPStackMessageQueue();
		stackMessageQueue.interceptProcessing(null);
		OPStack stack = OPStack.singleton();
		
		//		
		OPCacheDelegate cacheDelegate =  OPCacheDelegateImplementation.getInstance(mContext);
		CallbackHandler.getInstance().registerCacheDelegate(cacheDelegate);
		OPCache.setup(cacheDelegate);
		
		OPSettings.applyDefaults();
		
		String httpSettings = createHttpSettings();
		OPSettings.apply(httpSettings);
		
		String forceDashSettings = createForceDashSetting();
		OPSettings.apply(forceDashSettings);
		
		String appSettings = createFakeApplicationSettings();
		OPSettings.apply(appSettings);
		
		//TODO: After interception is done, we can call setup
		
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
	
	private String createFakeApplicationSettings()
	{
		//TODO: Read configuration from file
	
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

            jsonObject.put("openpeer/common/application-name", "OpenPeer Native Sample App");
            jsonObject.put("openpeer/common/application-image-url", "http://fake-image-url");
            jsonObject.put("openpeer/common/application-url", "http://fake-application-url");
            
            Time expires = new Time ();
            expires.set(30, 5, 2014);
            //rel-dev2
			//jsonObject.put("openpeer/calculated/authorizated-application-id", OPStack.createAuthorizedApplicationID("com.openpeer.nativeApp", "8f1ff375433b6e11026cb806a32ae4d04a59d7b1", expires ));
			//lespaul
            jsonObject.put("openpeer/calculated/authorizated-application-id", OPStack.createAuthorizedApplicationID("com.openpeer.nativeApp", "14b2c9df6713df465d97d0736863c42964faa678", expires ));
			
            jsonObject.put("openpeer/calculated/user-agent", "OpenPeerNativeSampleApp");
            jsonObject.put("openpeer/calculated/device-id", Secure.getString(mContext.getContentResolver(),
                    Secure.ANDROID_ID));
            jsonObject.put("openpeer/calculated/os", android.os.Build.VERSION.RELEASE);
            jsonObject.put("openpeer/calculated/system", android.os.Build.MODEL);
            parent.put("root", jsonObject);
            Log.d("output", parent.toString(2));
            return parent.toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
	}
}
