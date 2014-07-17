package com.openpeer.sample;

import com.openpeer.sdk.app.OPHelper;

public class BaseActivity extends BaseFragmentActivity {

	private static int mStack = 0;

	@Override
	public void onResume() {
		super.onResume();
		if (mStack == 0) {
			OPHelper.getInstance().onEnteringForeground();
			OPApplication.getInstance().onEnteringForeground();
		}
		mStack++;
	}

	@Override
	public void onPause() {
		super.onPause();
		mStack--;
		if (mStack == 0) {
			OPHelper.getInstance().onEnteringBackground();
			OPApplication.getInstance().onEnteringBackground();
		}
	}

}
