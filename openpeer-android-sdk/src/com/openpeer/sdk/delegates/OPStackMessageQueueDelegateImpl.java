package com.openpeer.sdk.delegates;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.openpeer.javaapi.OPStackMessageQueue;
import com.openpeer.javaapi.OPStackMessageQueueDelegate;

public class OPStackMessageQueueDelegateImpl extends OPStackMessageQueueDelegate {
	private static OPStackMessageQueueDelegateImpl instance;

	public static OPStackMessageQueueDelegateImpl getInstance() {
		if (instance == null) {
			instance = new OPStackMessageQueueDelegateImpl();
		}
		return instance;
	}

	// Defines a Handler object that's attached to the UI thread
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		/*
		 * handleMessage() defines the operations to perform when the Handler receives a new Message to process.
		 */
		@Override
		public void handleMessage(Message inputMessage) {
			// Gets the image task from the incoming Message object.
			// Log.d("output", "handleMessage");
			OPStackMessageQueue.singleton().notifyProcessMessageFromCustomThread();
		}
	};

	@Override
	public void onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread() {
		// TODO Auto-generated method stub
		Message msg = mHandler.obtainMessage();
		msg.sendToTarget();

	}

}
