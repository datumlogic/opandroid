
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Util;
using Com.Openpeer.Delegates;
using Com.Openpeer.Javaapi;

namespace HopSampleApp
{
	public interface DatastoreInterfaces
	{
		String getReloginInfo();

		//CSOPHomeUser getHomeUser();

		//Boolean saveHomeUser(CSOPHomeUser user);

		//List<OPIdentity> getIdentities();

		//OPIdentity getIdentity();

		//List<OPRolodexContact> getOPContacts(String identityId);

		//List<OPRolodexContact> getContacts(long identityId);

		Boolean saveOrUpdateAccount(OPAccount account);

		//Boolean saveOrUpdateIdentities(List<OPIdentity> identies,long accountId);

		//Boolean saveOrUpdateContacts(List<OPRolodexContact> contacts, long identityId);

		//Boolean saveOrUpdateIdentity(OPIdentity identy, long accountId);

		//Boolean saveOrUpdateContact(OPRolodexContact contact, long identityId);

		//Boolean deleteIdentity(long id);

		//Boolean deleteContact(long id);

		//String getDownloadedContactsVersion(long identityId);

		//void setDownloadedContactsVersion(long identityId, String version);

		//Boolean flushContactsForIdentity(long id);

		//List<OPIdentityContact> getSelfIdentityContacts();

		//OPIdentityContact getIdentityContact(String identityContactId);

		//List<OPMessage> getMessagesWithContact(long contactId, int max,String lastMessageId);

		//Boolean saveMessage(OPMessage message, long sessionId, String threadId);

		//Boolean saveSession(OPSession session);

		//int getNumberofUnreadMessages(String contactId);

		//List<OPMessage> getMessagesWithSession(long sessionId, int max,String lastMessageId);

		//List<OPSession> getRecentSessions();

		//void saveWindow(long windowId, List<OPUser> userList);

		//void saveOrUpdateUsers(List<OPIdentityContact> iContacts, long stableID);

		//OPUser saveUser(OPUser user);

		//List<OPUser> getUsers(long[] ids);

		//List<OPAvatar> getAvatars(long contactId);

		//void markMessagesRead(long mCurrentWindowId);
	}
}

