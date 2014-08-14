package com.openpeer.sample.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.R;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.datastore.DatabaseContracts.ContactsViewEntry;
import com.openpeer.sdk.datastore.OPContentProvider;

import static com.openpeer.sdk.datastore.DatabaseContracts.COLUMN_NAME_AVATAR_URI;
import static com.openpeer.sdk.datastore.DatabaseContracts.ContactsViewEntry.COLUMN_NAME_CONTACT_NAME;


public class ContactsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>,
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	private SwipeRefreshLayout mRootLayout;
	private ListView mListView;
	private ContactsAdapter mAdapter;
	private boolean mTest;
	private DataChangeReceiver mReceiver;

	public static android.support.v4.app.Fragment newInstance() {
		return new ContactsFragment();
	}

	public static ContactsFragment newTestInstance() {
		ContactsFragment fragment = new ContactsFragment();
		fragment.mTest = true;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mReceiver = new DataChangeReceiver();
		return setupView(inflater.inflate(R.layout.fragment_contacts, null));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		getLoaderManager().initLoader(URL_LOADER, null, this);

	}

	private View setupView(View view) {
		mListView = (ListView) view.findViewById(R.id.listview);
		View emptyView = view.findViewById(R.id.empty_view);
		mListView.setEmptyView(emptyView);
		mRootLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_view);
		mRootLayout.setOnRefreshListener(this);
		mAdapter = new ContactsAdapter(getActivity(), null);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				((ContactItemView) view).onClick();
			}
		});

		setupContent();

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_contacts, menu);
		// SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setOnQueryTextListener(this);
		// Assumes current activity is the searchable activity
		// searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		// SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
		// searchView.setSearchableInfo(info);

		// searchView.setSubmitButtonEnabled(true);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(mReceiver);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().registerReceiver(mReceiver, new IntentFilter(OPDataManager.INTENT_CONTACTS_CHANGED));

	}

	static class ContactsAdapter extends CursorAdapter {

		public ContactsAdapter(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			((ContactItemView) view).updateData(cursor);
		}

		@Override
		public View newView(Context view, Cursor cursor, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return new ContactItemView(arg2.getContext());
		}

	}

	@Override
	public void onRefresh() {
		OPDataManager.getInstance().refreshContacts();
		mRootLayout.setRefreshing(false);
	}

	static String oldQuery;

	void setupContent() {
		// mAdapter.mContacts = OPDataManager.getDatastoreDelegate()
		// .getContacts(0);
		//
		// mAdapter.notifyDataSetChanged();
	}

	class DataChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// Log.d("test", "received broadcast " + arg1.getAction());
			// setupContent();
		}
	}

	// Begin: CursorCallback implementation
	private static final int URL_LOADER = 0;
	static final String LIST_PROJECTION[] = { BaseColumns._ID, COLUMN_NAME_CONTACT_NAME, COLUMN_NAME_AVATAR_URI,
			ContactsViewEntry.COLUMN_NAME_STABLE_ID, ContactsViewEntry.COLUMN_NAME_USER_ID };

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
        StringBuilder builder = new StringBuilder("not (" + ContactsViewEntry.COLUMN_NAME_USER_ID + "=0 and " + ContactsViewEntry.COLUMN_NAME_STABLE_ID + " not null)");
        String slectionArgs[] = null;
		if (arg1 != null) {
			String query = arg1.getString("query");
			Log.d("test", "ContactsFragment onCreateLoader query " + query);
			if (!TextUtils.isEmpty(query)) {
				// Note: instr is available from sqlite 3.7.15
				builder.append(" and name like ?");
				slectionArgs = new String[] { "%" + query + "%" };
			}

		}
		switch (loaderID) {
		case URL_LOADER:
			// Returns a new CursorLoader
			return new CursorLoader(getActivity(), // Parent activity context
					OPContentProvider.getContentUri(ContactsViewEntry.URI_PATH_INFO),

					LIST_PROJECTION, // Projection to return
					builder.toString(), // No selection clause
					slectionArgs, // No selection arguments
					null // Default sort order
			);
		default:
			// An invalid id was passed in
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.changeCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.changeCursor(null);

	}

	// End: CursorCallback implementation

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.search:
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// BEGINNING OF INTERFACE IMPLEMENTATION

	public boolean onQueryTextChange(String newText) {

		if (oldQuery == null && TextUtils.isEmpty(newText)) {
			return true;
		}
		oldQuery = newText;
		Bundle params = new Bundle();
		params.putString("query", newText);
		getLoaderManager().restartLoader(URL_LOADER, params, ContactsFragment.this);

		return true;
	}

	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	public boolean onClose() {
		return false;
	}

	// END OF INTERFACE IMPLEMENTATION

}
