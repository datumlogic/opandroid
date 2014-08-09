package com.openpeer.delegates;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.openpeer.javaapi.OPStackMessageQueueDelegate;
import com.openpeer.openpeernativesampleapp.LoginManager;

public class OPStackMessageQueueDelegateImplementation extends OPStackMessageQueueDelegate{

	// Defines a Handler object that's attached to the UI thread
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		/*
         * handleMessage() defines the operations to perform when
         * the Handler receives a new Message to process.
         */
        @Override
        public void handleMessage(Message inputMessage) {
            // Gets the image task from the incoming Message object.
        	//Log.d("output", "handleMessage");
        	LoginManager.stackMessageQueue.notifyProcessMessageFromCustomThread();
        	
        }
    };
	@Override
	public void onStackMessageQueueWakeUpCustomThreadAndProcessOnCustomThread() {
		// TODO Auto-generated method stub
		Message msg = mHandler.obtainMessage();
		msg.sendToTarget();

	}

}
