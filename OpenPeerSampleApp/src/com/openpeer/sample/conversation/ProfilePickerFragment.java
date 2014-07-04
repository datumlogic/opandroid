package com.openpeer.sample.conversation;

import static com.openpeer.datastore.DatabaseContracts.ContactsViewEntry.COLUMN_NAME_AVATAR_URL;
import static com.openpeer.datastore.DatabaseContracts.ContactsViewEntry.COLUMN_NAME_CONTACT_NAME;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.openpeer.app.OPDataManager;
import com.openpeer.datastore.DatabaseContracts;
import com.openpeer.datastore.DatabaseContracts.ContactsViewEntry;
import com.openpeer.datastore.DatabaseContracts.UserEntry;
import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.R;
import com.openpeer.sample.contacts.ContactItemView;
import com.openpeer.sample.contacts.ContactsFragment;
import com.squareup.picasso.Picasso;

public class ProfilePickerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
		LoaderManager.LoaderCallbacks<Cursor> {

	private SwipeRefreshLayout mRootLayout;
	private ListView mListView;
	private ContactsAdapter mAdapter;
	private boolean mTest;
	List<Long> chosenUserIdsl = new ArrayList<Long>();
	ProfilePickerListener mListener;

	public static android.support.v4.app.Fragment newInstance() {
		return new ContactsFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((ContactItemView) view).onClick();
			}
		});

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
		inflater.inflate(R.menu.menu_contacts, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("test", "ProfilePickerFragment calling callback " + chosenUserIdsl.size());
		if (getTargetFragment() instanceof ProfilePickerListener) {
			((ProfilePickerListener) getTargetFragment()).onDone(chosenUserIdsl);
		}
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
				String avatar = cursor.getString(cursor.getColumnIndex(ContactsViewEntry.COLUMN_NAME_AVATAR_URL));
				String name = cursor.getString(cursor.getColumnIndex(ContactsViewEntry.COLUMN_NAME_CONTACT_NAME));
				Picasso.with(getActivity())
						.load(avatar)
						.into(imageView);
				nameView.setText(name);
				checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						if (arg1) {
							chosenUserIdsl.add(userId);
						} else {
							chosenUserIdsl.remove(userId);
						}
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

	String selection = "stable_id!=?";
	String args[] = { "" };

	// Begin: CursorCallback implementation
	private static final int URL_LOADER = 0;
	static final String LIST_PROJECTION[] = { BaseColumns._ID,
			COLUMN_NAME_CONTACT_NAME,
			COLUMN_NAME_AVATAR_URL,
			ContactsViewEntry.COLUMN_NAME_STABLE_ID,
			ContactsViewEntry.COLUMN_NAME_USER_ID };

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
		switch (loaderID) {
		case URL_LOADER:
			// Returns a new CursorLoader
			return new CursorLoader(
					getActivity(), // Parent activity context
					DatabaseContracts.ContactsViewEntry.CONTENT_URI, // Table to
																		// query
					LIST_PROJECTION, // Projection to return
					selection, // No selection clause
					args, // No selection arguments
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

	public static interface ProfilePickerListener {
		public void onDone(List<Long> userIds);
	}
}
