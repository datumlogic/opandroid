package com.openpeer.database;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.openpeer.javaapi.OPatabaseDelegate;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPIdentity;

public class OPDatabaseDelegateImplementation implements OPatabaseDelegate {
	private static OPDatabaseDelegateImplementation instance;
	OPDatabaseHelper mOpenHelper;
	private SQLiteDatabase mDatabase;

	private OPDatabaseDelegateImplementation() {

	}
	private OPDatabaseDelegateImplementation(Context context) {
		mOpenHelper = new OPDatabaseHelper(context,
				OPDatabaseHelper.DATABASE_NAME,              // the name of the database)
                null,                // uses the default SQLite cursor
                OPDatabaseHelper.DATABASE_VERSION  );
	}
	public static OPatabaseDelegate getInstance(Context context) {
		if (instance == null) {
			instance = new OPDatabaseDelegateImplementation(context);
		}
		return instance;
	}

	@Override
	public OPAccount getAccount() {
		mDatabase = mOpenHelper.getReadableDatabase();
		
		//TODO: how am i supposed to construct a native account object?
		return null;
	}

	@Override
	public List<OPIdentity> getIdentities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OPContact> getContacts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveOrUpdateAccount(OPAccount account) {
		// TODO Auto-generated method stub
		return false;
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

}
