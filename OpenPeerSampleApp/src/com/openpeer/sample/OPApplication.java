package com.openpeer.sample;

import android.app.Application;

import com.openpeer.app.OPHelper;

public class OPApplication extends Application {
	static {
		try {
			System.loadLibrary("z_shared");
			System.loadLibrary("openpeer");

		} catch (UnsatisfiedLinkError use) {
			use.printStackTrace();
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		OPHelper.getInstance(this).init();
	}

}
