package com.openpeer.sample;

import com.openpeer.sdk.app.OPHelper;

public class BaseActivity extends BaseFragmentActivity {

	private static int mStack;

	@Override
	public void onResume() {
		super.onResume();
		mStack++;
		if (mStack == 0) {
			OPHelper.getInstance().onEnteringForeground();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mStack--;
		if (mStack == 0) {
			OPHelper.getInstance().onEnteringBackground();
		}
	}

}
