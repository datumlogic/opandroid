package com.openpeer.datastore;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.model.OPHomeUser;

/**
 * The data being stored in preference: -- Relogin information for account --
 * stableId
 * 
 * The data being stored in database Contacts Conversation history Call history
 * 
 */
public class OPDatastoreDelegateImplementation implements OPDatastoreDelegate {
	private static final String PREF_DATASTORE = "model_data";
	private static final String PREF_KEY_RELOGIN_INFO = "relogin_info";
	private static final String PREF_KEY_HOMEUSER_STABLEID = "homeuser_stable_id";

	private static OPDatastoreDelegateImplementation instance;
	OPDatabaseHelper mOpenHelper;
	private SQLiteDatabase mDatabase;

	private SharedPreferences mPreferenceStore;

	private OPDatastoreDelegateImplementation() {

	}

	private OPDatastoreDelegateImplementation(Context context) {
		mOpenHelper = new OPDatabaseHelper(context,
				OPDatabaseHelper.DATABASE_NAME, // the name of the database)
				null, // uses the default SQLite cursor
				OPDatabaseHelper.DATABASE_VERSION);
		mPreferenceStore = context.getSharedPreferences(PREF_DATASTORE,
				Context.MODE_PRIVATE);
	}

	public static OPDatastoreDelegate getInstance(Context context) {
		if (instance == null) {
			instance = new OPDatastoreDelegateImplementation(context);
		}
		return instance;
	}

	@Override
	public OPAccount getAccount() {
		return null;
	}

	@Override
	public List<OPIdentity> getIdentities() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public OPIdentity getIdentity(){
		return null;
	}

	@Override
	public List<OPRolodexContact> getRolodexContacts(String identityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OPContact> getOPContacts(String identityId) {
		return null;
	}

	@Override
	public boolean saveOrUpdateAccount(OPAccount account) {
		SharedPreferences.Editor editor = mPreferenceStore.edit();
		editor.putString(PREF_KEY_RELOGIN_INFO, account.getReloginInformation());
		editor.putLong(PREF_KEY_HOMEUSER_STABLEID, account.getStableID());
		//TODO: do we need store peerfileprivate and key? what does the client app need it for?
		editor.apply();
		return true;
	}

	@Override
	public boolean saveOrUpdateIdentities(List<OPIdentity> identies) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateContacst(List<OPContact> contacts) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateIdentity(OPIdentity identy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateContact(OPContact contact) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteIdentity(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteContact(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OPHomeUser getHomeUser() {
		String reloginInfo = this.mPreferenceStore.getString(
				PREF_KEY_RELOGIN_INFO, null);
		if (reloginInfo != null) {
			return new OPHomeUser(reloginInfo, mPreferenceStore.getLong(
					PREF_KEY_HOMEUSER_STABLEID, 0L));
		}
		return null;
	}

	@Override
	public boolean saveHomeUser(OPHomeUser user) {
		SharedPreferences.Editor editor = mPreferenceStore.edit();
		editor.putString(PREF_KEY_RELOGIN_INFO, user.getReloginInfo());
		editor.putLong(PREF_KEY_HOMEUSER_STABLEID, user.getStableId());
		return true;
	}

}
