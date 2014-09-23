/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.sample;

import java.util.List;

import com.openpeer.sample.contacts.ContactsFragment;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
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

	private void doMySearch(String query) {
		Toast.makeText(this, "Search recieved " + query, Toast.LENGTH_LONG);
	}

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
