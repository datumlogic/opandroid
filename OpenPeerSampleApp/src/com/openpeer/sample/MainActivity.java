package com.openpeer.sample;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.openpeer.app.OPDataManager;
import com.openpeer.app.OPHelper;
import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.sample.contacts.ContactsFragment;
import com.openpeer.sample.conversation.ChatsFragment;
import com.openpeer.sample.conversation.DiscoveryFragment;
import com.openpeer.sample.util.NetworkUtil;

public class MainActivity extends BaseActivity {
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;
	private static final int TAB_CHATS = 0;
	private static final int TAB_DISCOVERY = 2;
	private static final int TAB_CONTACTS = 1;
	static final String tabNames[] = { "Chats", "Contacts", "Discovery" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_main);

		OPAccount account = OPDataManager.getInstance().getSharedAccount();
		String reloginInfo = OPDataManager.getInstance().getReloginInfo();
		// Launching app so account hasn't been constructed, and login process
		// hasn't started
		setupContentView();
		if (account == null) {
			// if (reloginInfo == null) {
			doLogin();
			// } else {
			// doRelogin();
			// }
		} else if (account.getState(0, "") == AccountStates.AccountState_Shutdown
				&& reloginInfo != null && NetworkUtil.isConnected()) {
			doRelogin();
		} else {
			// present UI

		}
	}

	void doLogin() {
		showLoginFragment();
	}

	void doRelogin() {
	}

	private void setupContentView() {
		mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mTabsAdapter);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
				mViewPager.setCurrentItem(arg0.getPosition());
			}

			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}
		};

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						getActionBar().setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < 3; i++) {
			actionBar.addTab(actionBar.newTab().setText(tabNames[i])
					.setTabListener(tabListener));
		}
	}

	class TabsAdapter extends FragmentPagerAdapter {

		public TabsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = null;
			switch (arg0) {
			case TAB_CHATS:
				fragment = ChatsFragment.newInstance();
				break;
			case TAB_CONTACTS:
				fragment = ContactsFragment.newInstance();
				break;
			case TAB_DISCOVERY:
				fragment = DiscoveryFragment.newInstance();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		// Get the SearchView and set the searchable configuration
		//		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		//		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		//		// Assumes current activity is the searchable activity
		//		//		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		//		SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
		//		searchView.setSearchableInfo(info);
		//
		//		searchView.setSubmitButtonEnabled(true);
		//		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			showLogDialog();
			break;
		case R.id.menu_settings:
			SettingsActivity.launch(this);
			break;
		default:
		}
		return super.onOptionsItemSelected(item);

	}

	void showLogDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("this will enable all logging neccessary for debugging");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				OPHelper.getInstance().enableAllLogging();
			}

		});
		builder.create().show();
	}
}
