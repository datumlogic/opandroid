package com.openpeer.datastore;

import java.util.ArrayList;
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
import com.openpeer.javaapi.OPRolodexContact.OPAvatar;
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
	static final String WHERE_CLAUSE = "where ";

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
		return mPreferenceStore.getString(PREF_KEY_RELOGIN_INFO, null);
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
	public List<OPRolodexContact> getContacts(long identityId) {

		Cursor cursor = mOpenHelper.getWritableDatabase().query(
				ContactEntry.TABLE_NAME,
				null,
				ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + "="
						+ identityId, null, null, null, null);
		if (cursor != null) {
			List<OPRolodexContact> contacts = new ArrayList<OPRolodexContact>();

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				OPRolodexContact contact = new OPRolodexContact(
						cursor.getString(cursor
								.getColumnIndex(ContactEntry.COLUMN_NAME_CONTACT_NAME)),
						cursor.getString(cursor
								.getColumnIndex(ContactEntry.COLUMN_NAME_URL)));
				Cursor avatarCursor = mOpenHelper.getWritableDatabase().query(
						AvatarEntry.TABLE_NAME,
						null,
						AvatarEntry.COLUMN_NAME_CONTACT_ID + "="
								+ contact.getId(), null, null, null, null);
				if (avatarCursor != null) {
					List<OPAvatar> avatars = new ArrayList<OPAvatar>();
					avatarCursor.moveToFirst();
					while (!avatarCursor.isAfterLast()) {
						OPAvatar avatar = new OPAvatar(
								cursor.getString(cursor
										.getColumnIndex(AvatarEntry.COLUMN_NAME_AVATAR_NAME)),
								cursor.getString(cursor
										.getColumnIndex(AvatarEntry.COLUMN_NAME_AVATAR_URL)),
								0, 0);
						avatars.add(avatar);
					}
					avatarCursor.close();
					contact.setAvatars(avatars);
				}
				contacts.add(contact);
				cursor.moveToNext();
			}
			cursor.close();
			return contacts;
		}

		return null;
	}

	@Override
	public List<OPRolodexContact> getOPContacts(String identityId) {
		String rawQuery = "select * from "
				+ IdentityContactEntry.TABLE_NAME
				+ " ic left join "
				+ ContactEntry.TABLE_NAME
				+ " c on ic.associated_identity_id=c.associated_identity_id where c.associated_identity_id=identityId";
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
	public boolean saveOrUpdateIdentities(List<OPIdentity> identities,
			long accountId) {
		for (OPIdentity identity : identities) {
			saveOrUpdateIdentity(identity, accountId);
		}

		return true;
	}

	@Override
	public boolean saveOrUpdateContacts(List<OPRolodexContact> contacts,
			long identityId) {
		for (OPRolodexContact contact : contacts) {
			saveOrUpdateContact(contact, identityId);
		}
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
		values.put(ContactEntry.COLUMN_NAME_CONTACT_NAME, contact.getName());
		values.put(ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID, identityId);
		values.put(ContactEntry.COLUMN_NAME_IDENTITY_PROVIDER, identityId);
		values.put(ContactEntry.COLUMN_NAME_URL, contact.getProfileURL());
		values.put(ContactEntry.COLUMN_NAME_VPROFILE_URL,
				contact.getVProfileURL());

		long rowId = mOpenHelper.getWritableDatabase().insertWithOnConflict(
				DatabaseContracts.ContactEntry.TABLE_NAME, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		if (rowId != 0 && contact.getAvatars() != null) {
			// insert or update avatar
			for (OPAvatar avatar : contact.getAvatars()) {
				ContentValues avatarValues = new ContentValues();
				avatarValues.put(AvatarEntry.COLUMN_NAME_CONTACT_ID,
						contact.getId());
				avatarValues.put(AvatarEntry.COLUMN_NAME_AVATAR_NAME,
						avatar.getName());
				avatarValues.put(AvatarEntry.COLUMN_NAME_AVATAR_URL,
						avatar.getURL());
				long avatarRowId = mOpenHelper.getWritableDatabase()
						.insertWithOnConflict(AvatarEntry.TABLE_NAME, null,
								avatarValues, SQLiteDatabase.CONFLICT_REPLACE);
			}
		}
		if (contact instanceof OPIdentityContact) {
			OPIdentityContact ic = (OPIdentityContact) contact;
			ContentValues icValues = new ContentValues();

			icValues.put(IdentityContactEntry.COLUMN_NAME_STABLE_ID,
					ic.getStableID());
			icValues.put(
					IdentityContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID,
					identityId);
			icValues.put(IdentityContactEntry.COLUMN_NAME_PEERFILE_PUBLIC,
					ic.getPeerFilePublic().peerFileString);

			icValues.put(
					IdentityContactEntry.COLUMN_NAME_IDENTITY_PROOF_BUNDLE,
					ic.getIdentityProofBundle());
			icValues.put(IdentityContactEntry.COLUMN_NAME_PRORITY,
					ic.getPriority());
			icValues.put(IdentityContactEntry.COLUMN_NAME_LAST_UPDATE_TIME, ic
					.getLastUpdated().toMillis(false));
			icValues.put(IdentityContactEntry.COLUMN_NAME_EXPIRE, ic
					.getExpires().toMillis(false));
			long _rowId = mOpenHelper.getWritableDatabase()
					.insertWithOnConflict(IdentityContactEntry.TABLE_NAME,
							null, icValues, SQLiteDatabase.CONFLICT_REPLACE);
		}

		return rowId != 0;
	}

	@Override
	public boolean deleteIdentity(long id) {

		mOpenHelper.getWritableDatabase().delete(IdentityEntry.TABLE_NAME,
				IdentityEntry.COLUMN_NAME_IDENTITY_ID + "=" + id, null);
		mOpenHelper.getWritableDatabase().delete(
				IdentityContactEntry.TABLE_NAME,
				IdentityContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + "="
						+ id, null);
		mOpenHelper.getWritableDatabase().delete(ContactEntry.TABLE_NAME,
				ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + "=", null);
		return false;
	}

	@Override
	public boolean deleteContact(OPRolodexContact contact) {
		mOpenHelper.getWritableDatabase().delete(ContactEntry.TABLE_NAME,
				ContactEntry.COLUMN_NAME_URL, null);
		if (contact instanceof OPIdentityContact) {
			mOpenHelper.getWritableDatabase().delete(
					IdentityContactEntry.TABLE_NAME,
					WHERE_CLAUSE + IdentityContactEntry.COLUMN_NAME_STABLE_ID,
					null);
		}
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
						IdentityEntry.COLUMN_NAME_IDENTITY_ID + "="
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
