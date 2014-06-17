package com.openpeer.openpeernativesampleapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPRolodexContact;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class ContactsScreen extends Activity {

	private static UserItemAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts_screen);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
		OPDownloadedRolodexContacts rolodexContacts = LoginManager.mIdentity.getDownloadedRolodexContacts();
		mAdapter = new ContactsScreen.UserItemAdapter(this, R.layout.contact_list_item, rolodexContacts.getRolodexContacts());
	}
	public class UserItemAdapter extends ArrayAdapter<OPRolodexContact> {
		private ArrayList<OPRolodexContact> users;

		public UserItemAdapter(Context context, int textViewResourceId, ArrayList<OPRolodexContact> list) {
			super(context, textViewResourceId, list);
			this.users = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.contact_list_item, null);
			}

			OPRolodexContact user = users.get(position);
			if (user != null) {
				user.printInfo();
				ImageView image = (ImageView) v.findViewById(R.id.contactImageView);
				TextView username = (TextView) v.findViewById(R.id.contactName);
				TextView email = (TextView) v.findViewById(R.id.contactIdentityURI);

				if (image != null) {
					Log.w("output", "URL = " + user.getAvatars().get(0).getURL());
					new DownloadImageTask(image).execute(user.getAvatars().get(0).getURL());
				}

				if (username != null) {
					username.setText(user.getName());
				}

				if(email != null) {
					email.setText("Email: " + user.getIdentityURI() );
				}
			}
			return v;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
		  bmImage.setImageBitmap(result);
		} 
	}




	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_contacts_screen,
					container, false);
			return rootView;
		}
		@Override
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);

			ListView listView = (ListView) getActivity().findViewById(R.id.mContactsListView);
			listView.setAdapter(mAdapter);

		}

	}

}
