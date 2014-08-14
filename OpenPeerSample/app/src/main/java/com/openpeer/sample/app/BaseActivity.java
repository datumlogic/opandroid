package com.openpeer.sample.app;

/**
 * Created by brucexia on 2014-06-03.
 */

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Perform injection so that when this call returns all dependencies will be available for use.
        ((OpenPeerApplication) getApplication()).inject(this);
    }
}
