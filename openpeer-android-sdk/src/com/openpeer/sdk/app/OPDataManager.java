/*******************************************************************************
 *
 *  Copyright (c) 2014 , Hookflash Inc.
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies,
 *  either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
package com.openpeer.sdk.app;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.openpeer.javaapi.AccountStates;
import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPContact;
import com.openpeer.javaapi.OPDownloadedRolodexContacts;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPIdentityContact;
import com.openpeer.javaapi.OPIdentityLookup;
import com.openpeer.javaapi.OPIdentityLookupInfo;
import com.openpeer.javaapi.OPLogLevel;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.sdk.datastore.OPDatastoreDelegate;
import com.openpeer.sdk.delegates.OPIdentityDelegateImpl;
import com.openpeer.sdk.delegates.OPIdentityLookupDelegateImpl;
import com.openpeer.sdk.model.OPUser;

/**
 * Hold reference to objects that cannot be constructed from database, and manages contacts data change.
 * 
 */
public class OPDataManager {
    private static final String TAG = OPDataManager.class.getSimpleName();

    public static String INTENT_CONTACTS_CHANGED = "com.openpeer.contacts_changed";

    private static OPDataManager instance;
    private OPDatastoreDelegate mDatastoreDelegate;

    private OPAccount mAccount;
    private Hashtable<Long, OPIdentity> mIdentities;
    private List<OPIdentityContact> mSelfContacts;
    Hashtable<String, OPIdentityLookup> mIdentityLookups;

    public static OPDatastoreDelegate getDatastoreDelegate() {
        return getInstance().mDatastoreDelegate;
    }

    public void addIdentity(OPIdentity identity) {
        if (mIdentities == null) {
            mIdentities = new Hashtable<Long, OPIdentity>();
        }
        mIdentities.put(identity.getID(), identity);
    }

    public OPIdentity getStoredIdentityById(long id) {
        if (mIdentities == null) {
            return null;
        } else {
            return mIdentities.get(id);
        }
    }

    public String getReloginInfo() {
        return mDatastoreDelegate.getReloginInfo();
    }

    public static OPDataManager getInstance() {
        if (instance == null) {
            instance = new OPDataManager();
        }
        return instance;
    }

    public void init(OPDatastoreDelegate delegate) {
        assert (delegate != null);
        mDatastoreDelegate = delegate;
        // mContacts = new Hashtable<Long, List<OPRolodexContact>>();
        // if (mReloginInfo != null) {
        // // Read idenities contacts and contacts
        // mSelfContacts = mDatastoreDelegate.getSelfIdentityContacts();
        // }
    }

    /**
     * This function should only be called in AccountState_Ready from OPAccountDelegate. This function update the database
     * 
     * @param account
     *            the logged in account
     */
    public void setSharedAccount(OPAccount account) {
        mAccount = account;
    }

    public void saveAccount() {
        mDatastoreDelegate.saveAccount(mAccount);
    }

    public OPAccount getSharedAccount() {
        return mAccount;
    }

    public List<OPIdentityContact> getSelfContacts() {
        List<OPIdentity> identities = mAccount.getAssociatedIdentities();
        mSelfContacts = new ArrayList<OPIdentityContact>();
        for (OPIdentity identity : identities) {
            mSelfContacts.add(identity.getSelfIdentityContact());
        }
        return mSelfContacts;
    }

    public void onDownloadedRolodexContacts(OPIdentity identity) {
        OPDownloadedRolodexContacts downloaded = identity
                .getDownloadedRolodexContacts();
        List<OPRolodexContact> contacts = downloaded.getRolodexContacts();
        if (contacts == null) {
            OPLogger.error(OPLogLevel.LogLevel_Detail,
                    "download rolodex contacts is null for identity "
                            + identity.getIdentityURI());
            return;
        } else if (contacts.isEmpty()) {
            OPLogger.debug(OPLogLevel.LogLevel_Detail,
                    "download rolodex contacts is empty for identity "
                            + identity.getIdentityURI());
            return;
        }
        contacts = mDatastoreDelegate.saveDownloadedRolodexContacts(identity,
                contacts, downloaded.getVersionDownloaded());
        identityLookup(identity, contacts);
    }

    public void identityLookup(OPIdentity identity,
            List<OPRolodexContact> contacts) {

        OPIdentityLookupDelegateImpl mIdentityLookupDelegate = OPIdentityLookupDelegateImpl
                .getInstance(identity);
        List<OPIdentityLookupInfo> inputLookupList = new ArrayList<OPIdentityLookupInfo>();

        for (OPRolodexContact contact : contacts) {
            OPIdentityLookupInfo ilInfo = new OPIdentityLookupInfo();
            ilInfo.initWithRolodexContact(contact);
            inputLookupList.add(ilInfo);
        }

        OPIdentityLookup identityLookup = OPIdentityLookup.create(OPDataManager
                .getInstance().getSharedAccount(), mIdentityLookupDelegate,
                inputLookupList, OPSdkConfig.getInstance()
                        .getIdentityProviderDomain());// "identity-v1-rel-lespaul-i.hcs.io");
        if (identityLookup != null) {
            if (mIdentityLookups == null) {
                mIdentityLookups = new Hashtable<String, OPIdentityLookup>();
            }
            mIdentityLookups.put(identity.getIdentityURI(), identityLookup);
        }
    }

    public void updateIdentityContacts(String identityUri,
            List<OPIdentityContact> iContacts) {

        // Each IdentityContact represents a user. Update user info
        mDatastoreDelegate.saveIdentityContact(iContacts,
                identityUri.hashCode());
    }

    public void refreshContacts() {
        List<OPIdentity> identities = mAccount.getAssociatedIdentities();
        for (OPIdentity identity : identities) {

            identity.refreshRolodexContacts();
        }
    }

    public boolean isAccountReady() {
        return mAccount != null
                && mAccount.getState() == AccountStates.AccountState_Ready;
    }

    public OPUser getUserByPeerUri(String uri) {
        return mDatastoreDelegate.getUserByPeerUri(uri);
    }

    /**
     * @param url
     * @param lookup
     */
    public void onIdentityLookupCompleted(String url, OPIdentityLookup lookup) {
        List<OPIdentityContact> iContacts = lookup.getUpdatedIdentities();
        if (iContacts != null) {
            updateIdentityContacts(url, iContacts);
        }
        if (mIdentityLookups != null) {
            mIdentityLookups.remove(url);
        }
    }

    public OPUser getUserById(long id) {
        return mDatastoreDelegate.getUserById(id);
    }

    public void onSignOut() {
        List<OPIdentity> identities = instance.mAccount
                .getAssociatedIdentities();
        if (mIdentityLookups != null && mIdentityLookups.size() > 0) {
            for (OPIdentityLookup lookup : mIdentityLookups.values()) {
                lookup.cancel();
            }
        }
        for (OPIdentity identity : identities) {
            identity.cancel();
        }

        mAccount.shutdown();
        mDatastoreDelegate.onSignOut();
    }

    /**
     * 
     */
    public void afterSignout() {
        OPDataManager.getDatastoreDelegate().onSignOut();
        OPIdentityDelegateImpl.clearAfterSignout();
    }
}
