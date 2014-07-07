package com.openpeer.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

public class BaseFragmentActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_container);
	}

	public Fragment setContentFragment(Fragment fragment) {
		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragment).commit();
		return fragment;
	}

	public void switchFragment(Fragment fragment) {
		Log.d("BaseFragmentActivity", "switching fragment " + fragment
				+ " for activity " + this);
		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

	}

	public void showLoginFragment() {
		findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, LoginFragment.newInstance())
				.commit();

	}

	public void hideLoginFragment() {
		findViewById(R.id.fragment_container).setVisibility(View.GONE);
	}
}
