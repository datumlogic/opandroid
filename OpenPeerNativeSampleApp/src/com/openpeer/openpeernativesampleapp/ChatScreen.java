package com.openpeer.openpeernativesampleapp;

import java.util.ArrayList;
import java.util.List;

import com.openpeer.delegates.OPConversationThreadDelegateImplementation;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPContactProfileInfo;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentityContact;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.os.Build;

public class ChatScreen extends Activity {

	private List<OPIdentityContact> mSelfContacts  = new ArrayList<OPIdentityContact> ();
	private List<OPIdentityContact> mContactsToAdd = new ArrayList<OPIdentityContact>();
	private List<OPContact> contacts = new ArrayList<OPContact>();
	private List<OPContactProfileInfo> mContactProfilesToAdd = new ArrayList<OPContactProfileInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_screen);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}


		OPIdentityContact self = LoginManager.mIdentity.getSelfIdentityContact();
		mSelfContacts.add(self);

		
		LoginManager.mConvThread = OPConversationThread.create(LoginManager.mAccount, mSelfContacts);

		Intent intent = getIntent();
		String peerFile = intent.getExtras().getString("peerFile");
		String identityURI = intent.getExtras().getString("identityURI");

		mContactsToAdd.clear();
		for (OPIdentityContact contact : LoginManager.mIdentityContacts)
		{
			if (contact.getIdentityURI().equals(identityURI))
			{
				mContactsToAdd.add(contact);
			}
		}

		//
		OPContactProfileInfo info = new OPContactProfileInfo();
		OPContact newContact = OPContact.createFromPeerFilePublic(LoginManager.mAccount, peerFile);

		info.setIdentityContacts(mContactsToAdd);
		info.setContact(newContact);

		mContactProfilesToAdd.add(info);

		LoginManager.mConvThread.addContacts(mContactProfilesToAdd);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_screen, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_chat_screen,
					container, false);

			Button button = (Button) rootView.findViewById(R.id.buttonSend);
			button.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// do something
					EditText text = (EditText) getActivity().findViewById(R.id.editTextMessage);
					LoginManager.mConvThread.sendMessage(java.util.UUID.randomUUID().toString(),
							"text/x-application-hookflash-message-text",
							text.getText().toString(),
							false);
					
					ListView messageList = (ListView) getActivity().findViewById(R.id.listViewMessages);
					//messageList.add
					text.setText("");
					

				} 
			});


			return rootView;
		}
	}

}
