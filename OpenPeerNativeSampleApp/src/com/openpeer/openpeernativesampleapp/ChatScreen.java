package com.openpeer.openpeernativesampleapp;

import java.util.ArrayList;
import java.util.List;

import org.webrtc.videoengine.ViERenderer;

import com.openpeer.delegates.OPCallDelegateImplementation;
import com.openpeer.delegates.OPConversationThreadDelegateImplementation;
import com.openpeer.javaapi.CameraTypes;
import com.openpeer.javaapi.OPCall;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPContactProfileInfo;
import com.openpeer.javaapi.OPConversationThread;
import com.openpeer.javaapi.OPConversationThreadDelegate;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPMediaEngine;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.javaapi.VideoOrientations;
//import com.openpeer.openpeernativesampleapp.ContactsScreen.DownloadImageTask;
//import com.openpeer.openpeernativesampleapp.ContactsScreen.UserItemAdapter;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class ChatScreen extends Activity implements IChatMessageReceiver{

	private static ChatListAdapter mAdapter;
	private List<OPIdentityContact> mSelfContacts  = new ArrayList<OPIdentityContact> ();
	private List<OPIdentityContact> mContactsToAdd = new ArrayList<OPIdentityContact>();
	private List<OPContact> contacts = new ArrayList<OPContact>();
	private List<OPContactProfileInfo> mContactProfilesToAdd = new ArrayList<OPContactProfileInfo>();
	
	private static SurfaceView myLocalSurface = null;
	private static SurfaceView myRemoteSurface = null;
	static boolean useFrontCamera = false;
	
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
		LoginManager.mChatMessageReceiver = this;

		mAdapter = new ChatScreen.ChatListAdapter(this, R.layout.chat_list_item, LoginManager.mMessages);

	}

	public class ChatListAdapter extends ArrayAdapter<OPMessage> {
		private List<OPMessage> messages;

		public ChatListAdapter(Context context, int textViewResourceId, List<OPMessage> list) {
			super(context, textViewResourceId, list);
			this.messages = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.chat_list_item, null);
			}

			OPMessage msg = messages.get(position);
			if (msg != null) {
				//msg.printInfo();
				//ImageView image = (ImageView) v.findViewById(R.id.contactImageView);
				TextView username = (TextView) v.findViewById(R.id.name);
				TextView message = (TextView) v.findViewById(R.id.message);

				if (username != null) {
					if(msg.getFrom() != null)
					{
						username.setText(msg.getFrom().getPeerURI() + ":");
					}
					else
					{
						username.setText("Me:");
					}
				}

				if(message != null) {
					Log.d("output", "Text is " +msg.getMessage());
					message.setText(msg.getMessage() );
					//message.setGravity(Gravity.RIGHT);
				}
			}
			return v;
		}
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
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			
			myLocalSurface = ViERenderer.CreateLocalRenderer(getActivity());
			myRemoteSurface = ViERenderer.CreateRenderer(getActivity(), true);
			LinearLayout localViewLinearLayout = (LinearLayout) getActivity().findViewById(R.id.localChatViewLinearLayout);
			LinearLayout remoteViewLinearLayout = (LinearLayout) getActivity().findViewById(R.id.remoteChatViewLinearLayout);
			localViewLinearLayout.addView(myLocalSurface);
			remoteViewLinearLayout.addView(myRemoteSurface);
			
			if (useFrontCamera)
				OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Front);
			else
				OPMediaEngine.getInstance().setCameraType(CameraTypes.CameraType_Back);
			OPMediaEngine.getInstance().setEcEnabled(true);
			OPMediaEngine.getInstance().setAgcEnabled(true);
			OPMediaEngine.getInstance().setNsEnabled(false);
			OPMediaEngine.getInstance().setMuteEnabled(false);
			OPMediaEngine.getInstance().setLoudspeakerEnabled(false);
			OPMediaEngine.getInstance().setContinuousVideoCapture(true);
			OPMediaEngine.getInstance().setDefaultVideoOrientation(VideoOrientations.VideoOrientation_Portrait);
			OPMediaEngine.getInstance().setRecordVideoOrientation(VideoOrientations.VideoOrientation_LandscapeRight);
			OPMediaEngine.getInstance().setFaceDetection(false);
			OPMediaEngine.getInstance().setChannelRenderView(myRemoteSurface);
			OPMediaEngine.getInstance().setCaptureRenderView(myLocalSurface);
			
			OPMediaEngine.init(OpenPeerApplication.getAppContext());

			ListView listView = (ListView) getActivity().findViewById(R.id.listViewMessages);
			listView.setAdapter(mAdapter);
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
					OPMessage newMsg = new OPMessage();
					//OPContact sendContact = OPContact.createFromPeerFilePublic(LoginManager.mAccount,

					newMsg.setMessage(text.getText().toString());
					LoginManager.mMessages.add(newMsg);
					mAdapter.notifyDataSetChanged();
					//messageList.add
					text.setText("");

				} 
			});

			Button buttonCall = (Button) rootView.findViewById(R.id.buttonCall);
			buttonCall.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("Place call");
					//builder.setIcon(R.drawable.icon);
					builder.setMessage("Please select which call you want to place");
					builder.setPositiveButton("Video",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//video call
							List<OPContact> contacts = new ArrayList<OPContact>();
							contacts = LoginManager.mConvThread.getContacts();

							Log.d("output", "AFTER ADD contacts size = " + contacts.size());
							OPContact callContact = new OPContact();

							for (OPContact iter : contacts)
							{
								if (iter.isSelf())
								{
									Log.d("output", "Evo jedan je self = " + iter.getPeerURI());
								}
								else
								{
									callContact = iter;
									Log.d("output", "ovaj nije self = " + iter.getPeerURI());
								}
							}
							LoginManager.mCallDelegate = new OPCallDelegateImplementation();

							Log.d("output", "java contact peer uri = "+callContact.getPeerURI());
							//Log.d("output", "java contact peer file = "+callContact.getPeerFilePublic());
							Log.d("output", "stable Id = " + callContact.getStableID());


							LoginManager.mCall = OPCall.placeCall(LoginManager.mConvThread, callContact, true, true);
							LoginManager.mCallbackHandler.registerCallDelegate(LoginManager.mCall, LoginManager.mCallDelegate);

						}
					});

					builder.setNeutralButton("Audio",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//audio call
							List<OPContact> contacts = new ArrayList<OPContact>();
							contacts = LoginManager.mConvThread.getContacts();

							Log.d("output", "AFTER ADD contacts size = " + contacts.size());
							OPContact callContact = new OPContact();

							for (OPContact iter : contacts)
							{
								if (iter.isSelf())
								{
									Log.d("output", "Evo jedan je self = " + iter.getPeerURI());
								}
								else
								{
									callContact = iter;
									Log.d("output", "ovaj nije self = " + iter.getPeerURI());
								}
							}

							Log.d("output", "java contact peer uri = "+callContact.getPeerURI());
							//Log.d("output", "java contact peer file = "+callContact.getPeerFilePublic());
							Log.d("output", "stable Id = " + callContact.getStableID());


							LoginManager.mCall = OPCall.placeCall(LoginManager.mConvThread, callContact, true, false);
							//LoginManager.mCallbackHandler.registerCallDelegate(LoginManager.mCall, LoginManager.mCallDelegate);

						}
					});

					builder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();

						}
					});
					builder.create().show();
				}
			});

			return rootView;
		}
	}

	@Override
	public void onChatMessageReceived() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				//stuff that updates ui
				mAdapter.notifyDataSetChanged();

			}
		});


	}

}
