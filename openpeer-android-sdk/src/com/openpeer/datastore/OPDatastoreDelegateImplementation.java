package com.openpeer.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.openpeer.datastore.DatabaseContracts.AvatarEntry;
import com.openpeer.datastore.DatabaseContracts.ContactEntry;
import com.openpeer.datastore.DatabaseContracts.IdentityContactEntry;
import com.openpeer.datastore.DatabaseContracts.IdentityEntry;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.javaapi.OPRolodexContact.OPAvatar;
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
	public Hashtable<Long, OPIdentityContact> getSelfIdentityContacts() {
		String columns[] = new String[] {
				IdentityEntry.COLUMN_NAME_IDENTITY_ID,
				IdentityEntry.COLUMN_NAME_IDENTITY_CONTACT_ID };
		Cursor cursor = mOpenHelper.getWritableDatabase()
				.query(IdentityEntry.TABLE_NAME, columns, null, null, null,
						null, null);
		if (cursor != null) {
			Hashtable<Long, OPIdentityContact> contacts = new Hashtable<Long, OPIdentityContact>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				contacts.put(cursor.getLong(0),
						getIdentityContact(cursor.getString(1)));
				cursor.moveToNext();
			}
			cursor.close();
			Log.d("test",
					"getSelfIdentityContacts "
							+ Arrays.deepToString(contacts.values().toArray()));
			return contacts;
		}
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
	public List<OPRolodexContact> getContacts(long identityId) {
		List<OPRolodexContact> contacts = new ArrayList<OPRolodexContact>();
		String selection = null;
		if (identityId != 0) {
			selection = ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + "="
					+ identityId;
		}
		Cursor cursor = mOpenHelper.getWritableDatabase().query(
				ContactEntry.TABLE_NAME, null, selection, null, null, null,
				null);
		if (cursor != null) {

			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				contacts.add(contactFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}

		return contacts;
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
	public boolean saveOrUpdateContacts(
			List<? extends OPRolodexContact> contacts, long identityId) {
		for (OPRolodexContact contact : contacts) {
			Log.d("OPDatastoreDelegateImplementation", "saveOrUpdateContacts "
					+ contact + " identityId " + identityId);
			saveOrUpdateContact(contact, identityId);
		}
		return true;
	}

	@Override
	public boolean saveOrUpdateIdentity(OPIdentity identity, long accountId) {
		OPIdentityContact selfContact = identity.getSelfIdentityContact();
		ContentValues values = new ContentValues();
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_ID,
				identity.getStableID());
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_PROVIDER,
				identity.getIdentityProviderDomain());
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_URI,
				identity.getIdentityURI());
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_CONTACT_ID,
				selfContact.getStableID());
		long rowId = mOpenHelper.getWritableDatabase().insertWithOnConflict(
				IdentityEntry.TABLE_NAME, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		saveOrUpdateContact(identity.getSelfIdentityContact(),
				identity.getStableID());

		return rowId != 0;
	}

	@Override
	public boolean saveOrUpdateContact(OPRolodexContact contact, long identityId) {
		ContentValues values = new ContentValues();
		values.put(ContactEntry.COLUMN_NAME_CONTACT_ID, contact.getId());
		if (contact instanceof OPIdentityContact) {
			values.put(ContactEntry.COLUMN_NAME_IDENTITY_CONTACT_ID,
					((OPIdentityContact) contact).getStableID());
		}
		values.put(ContactEntry.COLUMN_NAME_CONTACT_NAME, contact.getName());
		values.put(ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID, identityId);
		values.put(ContactEntry.COLUMN_NAME_IDENTITY_URI,
				contact.getIdentityURI());
		values.put(ContactEntry.COLUMN_NAME_IDENTITY_PROVIDER,
				contact.getIdentityProvider());
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
			icValues.put(IdentityContactEntry.COLUMN_NAME_PEERFILE_PUBLIC, ic
					.getPeerFilePublic().getPeerFileString());

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
				ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID + "=" + id,
				null);
		return false;
	}

	@Override
	public boolean flushContactsForIdentity(long id) {
		String selection = IdentityContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID
				+ "=" + id;
		String cSelection = ContactEntry.COLUMN_NAME_ASSOCIATED_IDENTITY_ID
				+ "=" + id;
		mOpenHelper.getWritableDatabase().delete(
				IdentityContactEntry.TABLE_NAME, selection, null);
		mOpenHelper.getWritableDatabase().delete(ContactEntry.TABLE_NAME,
				cSelection, null);
		return true;
	}

	@Override
	public boolean deleteContact(long id) {
		String rawSql = "delete from " + IdentityContactEntry.TABLE_NAME
				+ " where " + IdentityContactEntry.COLUMN_NAME_STABLE_ID
				+ " in (select " + ContactEntry.COLUMN_NAME_IDENTITY_CONTACT_ID
				+ " from " + ContactEntry.TABLE_NAME + " where "
				+ ContactEntry.COLUMN_NAME_CONTACT_ID + "=" + id + ")";
		mOpenHelper.getWritableDatabase().execSQL(rawSql);
		mOpenHelper.getWritableDatabase().delete(ContactEntry.TABLE_NAME,
				ContactEntry.COLUMN_NAME_CONTACT_ID + "=" + id, null);
		// if (contact instanceof OPIdentityContact) {
		// mOpenHelper.getWritableDatabase()
		// .delete(IdentityContactEntry.TABLE_NAME,
		// IdentityContactEntry.COLUMN_NAME_STABLE_ID
		// + "="
		// + ((OPIdentityContact) contact)
		// .getStableID(), null);
		// }
		return true;
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
		Log.d("test", "datastore contacts version " + version);
		return version;
	}

	@Override
	public List<OPRolodexContact> getOPContacts(String identityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDownloadedContactsVersion(long identityId, String version) {
		ContentValues values = new ContentValues();
		values.put(IdentityEntry.COLUMN_NAME_IDENTITY_CONTACTS_VERSION, version);
		String whereClause = IdentityEntry.COLUMN_NAME_IDENTITY_ID + "="
				+ identityId;
		long rowId = mOpenHelper.getWritableDatabase().update(
				IdentityEntry.TABLE_NAME, values, whereClause, null);
		Log.d("test", "setDownloadedContactsVersion " + rowId);
	}

	@Override
	public OPIdentityContact getIdentityContact(String identityContactId) {
		String iSelection = IdentityContactEntry.COLUMN_NAME_STABLE_ID + "=?";
		String cSelection = ContactEntry.COLUMN_NAME_IDENTITY_CONTACT_ID + "=?";
		String[] selectionParams = new String[] { identityContactId };

		Cursor iCursor = mOpenHelper.getWritableDatabase().query(
				IdentityContactEntry.TABLE_NAME, null, iSelection,
				selectionParams, null, null, null);
		Cursor cCursor = mOpenHelper.getWritableDatabase().query(
				ContactEntry.TABLE_NAME, null, cSelection, selectionParams,
				null, null, null);
		if (cCursor != null && iCursor != null) {
			cCursor.moveToFirst();
			iCursor.moveToFirst();
			return identityContactFromCursor(contactFromCursor(cCursor),
					iCursor);
		}
		return null;

	}

	private OPRolodexContact contactFromCursor(Cursor cursor) {
		int identityUrlIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_IDENTITY_URI);
		int identityProviderIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_IDENTITY_PROVIDER);
		int nameIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_CONTACT_NAME);
		int profileURLIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_URL);
		int vprofileURLIndex = cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_VPROFILE_URL);

		OPRolodexContact contact = new OPRolodexContact(
				cursor.getString(identityUrlIndex),
				cursor.getString(identityProviderIndex),
				cursor.getString(nameIndex), cursor.getString(profileURLIndex),
				cursor.getString(vprofileURLIndex), null);

		contact.setAvatars(this.getAvatars(contact.getId()));
		String stableIdentityId = cursor.getString(cursor
				.getColumnIndex(ContactEntry.COLUMN_NAME_IDENTITY_CONTACT_ID));
		if (stableIdentityId != null) {
			Log.d("test", "retrieving identity contact of id "
					+ stableIdentityId);
			Cursor iCursor = mOpenHelper.getWritableDatabase().query(
					IdentityContactEntry.TABLE_NAME, null,
					IdentityContactEntry.COLUMN_NAME_STABLE_ID + "=?",
					new String[] { stableIdentityId }, null, null, null);
			if (iCursor != null) {
				iCursor.moveToFirst();
				contact = identityContactFromCursor(contact, iCursor);
				iCursor.close();
			}
		}
		return contact;
	}

	private OPIdentityContact identityContactFromCursor(
			OPRolodexContact contact, Cursor iCursor) {
		OPIdentityContact iContact = new OPIdentityContact(contact);
		return iContact
				.setIdentityParams(
						iCursor.getString(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_STABLE_ID)),
						iCursor.getString(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_PEERFILE_PUBLIC)),
						iCursor.getString(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_IDENTITY_PROOF_BUNDLE)),
						iCursor.getInt(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_PRORITY)),
						iCursor.getInt(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_WEIGHT)),
						iCursor.getLong(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_LAST_UPDATE_TIME)),
						iCursor.getLong(iCursor
								.getColumnIndex(IdentityContactEntry.COLUMN_NAME_EXPIRE)));
	}

	private List<OPAvatar> getAvatars(long contactId) {
		Cursor cursor = mOpenHelper.getWritableDatabase().query(
				AvatarEntry.TABLE_NAME, null,
				AvatarEntry.COLUMN_NAME_CONTACT_ID + "=" + contactId, null,
				null, null, null);
		if (cursor != null) {
			List<OPAvatar> avatars = new ArrayList<OPAvatar>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				OPAvatar avatar = new OPAvatar(
						cursor.getString(cursor
								.getColumnIndex(AvatarEntry.COLUMN_NAME_AVATAR_NAME)),
						cursor.getString(cursor
								.getColumnIndex(AvatarEntry.COLUMN_NAME_AVATAR_URL)),
						0, 0);
				avatars.add(avatar);
				cursor.moveToNext();
			}
			cursor.close();
			return avatars;
		}
		return null;
	}
}
