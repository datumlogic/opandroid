package com.openpeer.sample.contacts;

import android.util.Log;

public class ContactsManager {

	private static ContactsManager instance;

	public static ContactsManager getInstance() {
		if (instance == null) {
			instance = new ContactsManager();
		}
		return instance;
	}

	public void refreshContacts() {
		Log.d("TODO", "refreshing contacts");
	}

}
