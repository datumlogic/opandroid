/**
 * Copyright (c) 2014, SMB Phone Inc. / Hookflash Inc.
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p/>
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */
package com.openpeer.datastore;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Application;

import com.openpeer.javaapi.OPAccount;
import com.openpeer.javaapi.OPIdentity;
import com.openpeer.javaapi.OPLogger;
import com.openpeer.javaapi.OPRolodexContact;
import com.openpeer.javaapi.OPRolodexContact.OPAvatar;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.datastore.OPDatastoreDelegateImpl;
import com.openpeer.sdk.model.GroupChatMode;
import com.openpeer.sdk.model.OPConversation;
import com.openpeer.sdk.model.OPUser;

/**
 *
 */
@RunWith(RobolectricTestRunner.class)
public class DatastoreDelegateImplTest {

    OPDatastoreDelegateImpl delegate;

    @Before
    public void setup() {
        Application application = Robolectric.application;
        OPUser user = new OPUser();
        user.setUserId(1);
        delegate = OPDatastoreDelegateImpl.getInstance();
        delegate.init(application);
        delegate.setupForTest();
        OPLogger.setIsUnitTest(true);
        OPDataManager.getInstance().init(delegate);
    }
    @After
    public void teardown(){
        
    }

    @Test
    public void testSaveAccount() {
        OPAccount account = Mockito.mock(OPAccount.class);
        Mockito.when(account.getPeerUri()).thenReturn("peer://hcs.io/1234567890");
        Mockito.when(account.getReloginInformation()).thenReturn(
                "{\"reloginInfo\":\"peer://hcs.io/1234567890\"}");
        Mockito.when(account.getPeerUri()).thenReturn("peer://hcs.io/1234567890");
        // delegate.saveAccount(account);

    }
    @Test
    public void testSaveDownloadedContacts() {
        OPIdentity identity = Mockito.mock(OPIdentity.class);
        Mockito.when(identity.getIdentityURI()).thenReturn("identity://facebook.com/0987654321");
        List<OPRolodexContact> contacts = new ArrayList<>();
        OPRolodexContact contact1 = Mockito.mock(OPRolodexContact.class);
        Mockito.when(contact1.getIdentityURI()).thenReturn("identity://facebook.com/1234567890");
        Mockito.when(contact1.getDisposition()).thenReturn(
                OPRolodexContact.Dispositions.Disposition_Update);
        Mockito.when(contact1.getName()).thenReturn("Test Name1");
        Mockito.when(contact1.getIdentityProvider()).thenReturn("facebook.com");
        Mockito.when(contact1.getAvatars()).thenReturn(new ArrayList<OPRolodexContact.OPAvatar>());
        contacts.add(contact1);
        
        OPRolodexContact contact2 = Mockito.mock(OPRolodexContact.class);
        Mockito.when(contact2.getIdentityURI()).thenReturn("identity://facebook.com/1234567891");
        Mockito.when(contact2.getDisposition()).thenReturn(
                OPRolodexContact.Dispositions.Disposition_Update);
        Mockito.when(contact2.getName()).thenReturn("Test Name2");
        Mockito.when(contact2.getIdentityProvider()).thenReturn("facebook.com");
        Mockito.when(contact2.getAvatars()).thenReturn(new ArrayList<OPRolodexContact.OPAvatar>());
        contacts.add(contact2);
    
        List<OPRolodexContact> results =delegate.saveDownloadedRolodexContacts(identity, contacts, "abcdefg");
        Assert.assertEquals(contact1, results.get(0));
        Assert.assertEquals(contact2, results.get(1));
        
    }
    @Test
    public void testSaveConversation() {
        List<OPUser> users = new ArrayList<OPUser>();
        OPUser user1 = Mockito.mock(OPUser.class);
        user1.setUserId(2);
        Mockito.when(user1.getPeerUri()).thenReturn("peer://hcs.io/12345678901");
        OPUser user2 = Mockito.mock(OPUser.class);
        user2.setUserId(3);
        Mockito.when(user2.getPeerUri()).thenReturn("peer://hcs.io/12345678902");
        users.add(user1);
        users.add(user2);
        OPConversation conversation = new OPConversation(users, "123", GroupChatMode.ContactsBased);
        long id = delegate.saveConversation(conversation);
        Assert.assertEquals(id, 1);
    }

   
}
