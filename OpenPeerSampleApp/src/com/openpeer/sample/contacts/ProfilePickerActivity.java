package com.openpeer.sample.contacts;

import com.openpeer.sample.BaseActivity;
import com.openpeer.sample.BaseFragmentActivity;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class ProfilePickerActivity extends BaseActivity {

	public static final int REQUEST_CODE_ADD_CONTACTS = 10000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_container);
		if (getIntent() != null && getIntent().getDataString() != null
				&& getIntent().getDataString().contains("test")) {
			Log.d("test", "ContactsActivity launching test fragment");
			// this.setContentFragment(ContactsFragment.newTestInstance());
		} else {
			long excludeIds[] = getIntent().getLongArrayExtra(
					IntentData.ARG_PEER_USER_IDS);

			this.setContentFragment(ProfilePickerFragment
					.newInstance(excludeIds));
		}
	}

	public static void launch(Context context) {
		Intent intent = new Intent(context, ProfilePickerActivity.class);
		context.startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
