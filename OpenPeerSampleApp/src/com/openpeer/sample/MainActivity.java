package com.openpeer.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.openpeer.sample.contacts.ContactsActivity;

public class MainActivity extends BaseFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginFragment loginFragment = LoginFragment.newInstance();
		this.setContentFragment(loginFragment);
		// if (OPDatastoreDelegateImplementation.getInstance().getReloginInfo()
		// == null) {
		// Log.d("test", "logging in");
		// LoginFragment loginFragment = LoginFragment.newInstance();
		// this.setContentFragment(loginFragment);
		// LoginManager loginManager = new LoginManager(loginFragment, null,
		// null);
		// loginManager.login();
		// } else {
		// Log.d("test", "re-logging in");
		// LoginFragment loginFragment = LoginFragment.newInstance();
		// this.setContentFragment(loginFragment);
		// LoginManager loginManager = new LoginManager(loginFragment, null,
		// null);
		// loginManager.relogin(OPDatastoreDelegateImplementation
		// .getInstance().getReloginInfo());
		// }
	}

	public static void launch(Context context) {
		Intent intent = new Intent(context, ContactsActivity.class);
		context.startActivity(intent);
	}

}
