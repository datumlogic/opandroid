package com.openpeer.sample.contacts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.ProviderContracts;
import com.openpeer.sample.R;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.datastore.DatabaseContracts;
import com.openpeer.sdk.datastore.DatabaseContracts.ContactsViewEntry;
import static com.openpeer.sdk.datastore.DatabaseContracts.ContactsViewEntry.*;
import static com.openpeer.sdk.datastore.DatabaseContracts.*;


import com.squareup.picasso.Picasso;

public class ProfilePickerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
		LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

	static final String PARAM_IDS_EXCLUDE = "exclude";
	private SwipeRefreshLayout mRootLayout;
	private ListView mListView;
	private ContactsAdapter mAdapter;
	private boolean mTest;
	List<Long> chosenUserIds = new ArrayList<Long>();
	long[] mIdsExclude;
	ProfilePickerListener mListener;
	MenuItem mDoneMenu;

	public static ProfilePickerFragment newInstance(long idsExclude[]) {
		Bundle bundle = new Bundle();
		bundle.putLongArray(PARAM_IDS_EXCLUDE, idsExclude);
		ProfilePickerFragment fragment = new ProfilePickerFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		mIdsExclude = getArguments().getLongArray(PARAM_IDS_EXCLUDE);
		return setupView(inflater.inflate(R.layout.fragment_profile_picker, null));
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
		mRootLayout = (SwipeRefreshLayout) view;
		mRootLayout.setOnRefreshListener(this);
		mAdapter = new ContactsAdapter(getActivity(), null);
		mListView.setAdapter(mAdapter);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// this.setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_profile_picker, menu);
		mDoneMenu = menu.findItem(R.id.menu_done);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setOnQueryTextListener(this);
		updateDoneText();
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	class ContactsAdapter extends CursorAdapter {

		public ContactsAdapter(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			((ViewHolder) view.getTag()).update(cursor);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup arg2) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_contact_picker, null);
			view.setTag(new ViewHolder(view));
			return view;
		}

		class ViewHolder {
			CheckBox checkbox;
			ImageView imageView;
			TextView nameView;

			ViewHolder(View view) {
				checkbox = (CheckBox) view.findViewById(R.id.checkBox);
				imageView = (ImageView) view.findViewById(R.id.image_view);
				nameView = (TextView) view.findViewById(R.id.title);
			}

			public void update(Cursor cursor) {
				final long userId = cursor.getLong(cursor.getColumnIndex(ContactsViewEntry.COLUMN_NAME_USER_ID));
				String avatar = cursor.getString(cursor.getColumnIndex(DatabaseContracts.COLUMN_NAME_AVATAR_URI));
				String name = cursor.getString(cursor.getColumnIndex(ContactsViewEntry.COLUMN_NAME_CONTACT_NAME));
				Picasso.with(getActivity()).load(avatar).into(imageView);
				nameView.setText(name);
				checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						if (arg1) {
							chosenUserIds.add(userId);
						} else {
							chosenUserIds.remove(userId);
						}
						updateDoneText();
					}

				});
			}
		}

	}

	@Override
	public void onRefresh() {
		OPDataManager.getInstance().refreshContacts();
		mRootLayout.setRefreshing(false);
	}

	// Begin: CursorCallback implementation
	private static final int URL_LOADER = 0;
	static final String LIST_PROJECTION[] = { BaseColumns._ID, COLUMN_NAME_CONTACT_NAME, COLUMN_NAME_AVATAR_URI,
			ContactsViewEntry.COLUMN_NAME_STABLE_ID, ContactsViewEntry.COLUMN_NAME_USER_ID };

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
		switch (loaderID) {
		case URL_LOADER:
			// Returns a new CursorLoader
			StringBuilder builder = new StringBuilder("user_id not in (0,");
			for (int i = 0; i < mIdsExclude.length; i++) {
				if (i == mIdsExclude.length - 1) {
					builder.append(mIdsExclude[i] + ")");
				} else {
					builder.append(mIdsExclude[i] + ",");
				}
			}

			String slectionArgs[] = null;
			if (arg1 != null) {
				String query = arg1.getString("query");
				Log.d("test", "ContactsFragment onCreateLoader query " + query);
				if (!TextUtils.isEmpty(query)) {
					// Note: instr is available from sqlite 3.7.15
					builder.append("and name like ?");
					slectionArgs = new String[] { "%" + query + "%" };
				}

			}

			return new CursorLoader(getActivity(), // Parent activity context
					ProviderContracts.CONTENT_URI_CONTACTS_VIEW,
					// DatabaseContracts.ContactsViewEntry.CONTENT_URI, // Table to
																		// query
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
	private void updateDoneText() {
		mDoneMenu.setTitle(getActivity().getString(R.string.menu_done, "" + chosenUserIds.size()));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_done:

			Intent data = new Intent();
			long[] ids = new long[chosenUserIds.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = chosenUserIds.get(i);
			}
			data.putExtra(IntentData.ARG_PEER_USER_IDS, ids);
			this.getActivity().setResult(Activity.RESULT_OK, data);
			getActivity().finish();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static interface ProfilePickerListener {
		public void onDone(List<Long> userIds);
	}

	// BEGINNING OF INTERFACE IMPLEMENTATION
	static String oldQuery;

	public boolean onQueryTextChange(String newText) {

		if (oldQuery == null && TextUtils.isEmpty(newText)) {
			return true;
		}
		oldQuery = newText;
		Bundle params = new Bundle();
		params.putString("query", newText);
		getLoaderManager().restartLoader(URL_LOADER, params, ProfilePickerFragment.this);

		return true;
	}

	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	// END OF INTERFACE IMPLEMENTATION
}
