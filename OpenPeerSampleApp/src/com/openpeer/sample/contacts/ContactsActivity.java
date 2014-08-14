package com.openpeer.sample.contacts;

import com.openpeer.sample.BaseFragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ContactsActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (getIntent() != null && getIntent().getDataString() != null
				&& getIntent().getDataString().contains("test")) {
			Log.d("test", "ContactsActivity launching test fragment");
			this.setContentFragment(ContactsFragment.newTestInstance());
		} else {
			this.setContentFragment(ContactsFragment.newInstance());
		}
	}

	public static void launch(Context context) {
		Intent intent = new Intent(context, ContactsActivity.class);
		context.startActivity(intent);
	}

}
