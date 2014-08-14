package com.openpeer.sample;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.sample.contacts.ContactsFragment;
import com.openpeer.sample.conversation.ChatsFragment;
import com.openpeer.sample.conversation.DiscoveryFragment;
import com.openpeer.sample.util.NetworkUtil;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.app.OPHelper;

public class MainActivity extends BaseActivity implements OPHelper.InitListener, ChatsFragment.ChatsViewListener {
    TabsAdapter mTabsAdapter;
    ViewPager mViewPager;
    private static final int TAB_CHATS = 0;
    private static final int TAB_CONTACTS = 1;
    private static final int TAB_FAVORITES = 2;

    private String tabNames[];

    public static void cleanLaunch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabNames = this.getResources().getStringArray(R.array.tabs);
        go();
    }

    void go() {
        OPAccount account = OPDataManager.getInstance().getSharedAccount();
        setupContentView();
        if (account == null || account.getState() == AccountStates.AccountState_Shutdown) {
            if (NetworkUtil.isConnected()) {
                showLoginFragment();
            }

        }
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

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < 3; i++) {
            actionBar.addTab(actionBar.newTab().setText(tabNames[i]).setTabListener(tabListener));
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
                case TAB_FAVORITES:
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_settings:
                SettingsActivity.launch(this);
                break;

            default:
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onInitialized() {
        go();
    }

    @Override
    public void onChatsEmptyViewClick() {
        mViewPager.setCurrentItem(TAB_FAVORITES);
    }
}
