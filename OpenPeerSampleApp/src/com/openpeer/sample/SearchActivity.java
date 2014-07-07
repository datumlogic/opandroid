package com.openpeer.sample;

import java.util.List;

import com.openpeer.sample.contacts.ContactsFragment;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener,
		SearchView.OnCloseListener, Button.OnClickListener {
	private SearchView mSearchView;
	private Button mOpenButton;
	private Button mCloseButton;
	private TextView mStatusView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.activity_container);
		this.setContentFragment(ContactsFragment.newInstance());

	}

	//    @Override
	//    public boolean onCreateOptionsMenu(Menu menu) {
	//        super.onCreateOptionsMenu(menu);
	//
	//        MenuInflater inflater = getMenuInflater();
	//        inflater.inflate(R.menu.searchview_in_menu, menu);
	//        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	//        setupSearchView();
	//
	//        return true;
	//    }

	private void setupSearchView() {

		mSearchView.setIconifiedByDefault(true);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (searchManager != null) {
			List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

			// Try to use the "applications" global search provider
			SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
			for (SearchableInfo inf : searchables) {
				if (inf.getSuggestAuthority() != null
						&& inf.getSuggestAuthority().startsWith("applications")) {
					info = inf;
				}
			}
			mSearchView.setSearchableInfo(info);
		}

		mSearchView.setOnQueryTextListener(this);
		mSearchView.setOnCloseListener(this);
	}

	//BEGINNING OF INTERFACE IMPLEMENTATION

	public boolean onQueryTextChange(String newText) {
		mStatusView.setText("Query = " + newText);
		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		mStatusView.setText("Query = " + query + " : submitted");
		return false;
	}

	public boolean onClose() {
		mStatusView.setText("Closed!");
		return false;
	}

	public void onClick(View view) {
		if (view == mCloseButton) {
			mSearchView.setIconified(true);
		} else if (view == mOpenButton) {
			mSearchView.setIconified(false);
		}
	}
	//END OF INTERFACE IMPLEMENTATION

}
