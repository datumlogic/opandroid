package com.openpeer.sample;

import android.content.Context;
import android.widget.Toast;

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

    public static void showInvalidStateWarning(Context context){
        Toast.makeText(context, R.string.msg_not_logged_in,Toast.LENGTH_LONG).show();

    }

}
