package com.openpeer.datastore;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.model.OPHomeUser;
import static com.openpeer.datastore.DatabaseContracts.*;

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
		mDatabase = mOpenHelper.getWritableDatabase();

		mPreferenceStore = context.getSharedPreferences(PREF_DATASTORE,
				Context.MODE_PRIVATE);
	}

	public static OPDatastoreDelegateImplementation getInstance() {
		if (instance == null) {
			instance = new OPDatastoreDelegateImplementation();
		}
		return instance;
	}

	public OPDatastoreDelegateImplementation init(Context context) {
		mOpenHelper = new OPDatabaseHelper(context,
				OPDatabaseHelper.DATABASE_NAME, // the name of the database)
				null, // uses the default SQLite cursor
				OPDatabaseHelper.DATABASE_VERSION);
		mDatabase = mOpenHelper.getWritableDatabase();

		mPreferenceStore = context.getSharedPreferences(PREF_DATASTORE,
				Context.MODE_PRIVATE);
		return this;// fluent API

	}

	@Override
	public String getReloginInfo() {
		return null;
	}

	@Override
	public List<OPIdentity> getIdentities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OPIdentity getIdentity() {
		return null;
	}

	@Override
	public List<OPRolodexContact> getContacts(String identityId) {

		return null;
	}

	@Override
	public List<OPRolodexContact> getOPContacts(String identityId) {
		return null;
	}

	@Override
	public boolean saveOrUpdateAccount(OPAccount account) {
		SharedPreferences.Editor editor = mPreferenceStore.edit();
		editor.putString(PREF_KEY_RELOGIN_INFO, account.getReloginInformation());
		editor.putLong(PREF_KEY_HOMEUSER_STABLEID, account.getStableID());
		// TODO: do we need store peerfileprivate and key? what does the client
		// app need it for?
		editor.apply();
		return true;
	}

	@Override
	public boolean saveOrUpdateIdentities(List<OPIdentity> identies,
			long accountId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateContacts(List<OPRolodexContact> contacts,
			long identityId) {

		return true;
	}

	@Override
	public boolean saveOrUpdateIdentity(OPIdentity identity, long accountId) {
		ContentValues values = new ContentValues();
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_ID,
				identity.getStableID());
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_PROVIDER,
				identity.getIdentityProviderDomain());
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_URI,
				identity.getIdentityURI());
		long rowId = mOpenHelper.getWritableDatabase().insertWithOnConflict(
				IdentityEntry.TABLE_NAME, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);

		return rowId != 0;
	}

	@Override
	public boolean saveOrUpdateContact(OPRolodexContact contact, long identityId) {
		ContentValues values = new ContentValues();
		if (contact instanceof OPIdentityContact) {
			values.put(ContactEntry.COLUMN_NAME_CONTACT_ID,
					((OPIdentityContact) contact).getStableID());
		}

		long rowId = mOpenHelper.getWritableDatabase().insertWithOnConflict(
				DatabaseContracts.ContactEntry.TABLE_NAME, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		if (rowId != 0) {
			// insert or update avatar
		}

		return rowId != 0;
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

	public String getDownloadedContactsVersion(long identityId) {
		String version = null;
		Cursor cursor = mOpenHelper
				.getWritableDatabase()
				.query(IdentityEntry.TABLE_NAME,
						new String[] { IdentityEntry.COLUMN_NAME_IDENTITY_CONTACTS_VERSION },
						"where " + IdentityEntry.COLUMN_NAME_IDENTITY_ID + "="
								+ identityId, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			version = cursor
					.getString(cursor
							.getColumnIndex(IdentityEntry.COLUMN_NAME_IDENTITY_CONTACTS_VERSION));
		}
		return version;

	}
}
