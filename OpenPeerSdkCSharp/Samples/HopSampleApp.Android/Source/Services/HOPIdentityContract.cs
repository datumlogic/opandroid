
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Timers;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using HopSampleApp.Enums;
namespace HopSampleApp
{
	class HOPIdentityContract
	{
	}
	public  class HOPIdentityState
	{
		public HOPIdentityStates State {get; set;}

		public short LastErrorCode {get; set;}

		public string LastErrorReason {get; set;}

	}
	public class HOPIdentity
	{
		public string IdentityBaseURI {get; set;}

		public string IdentityId {get; set;}

		public Timer DeletionTimer{ get; set;}

		public static object loginWithDelegateIdentityProviderDomainIdentityURIOridentityBaseURIOuterFrameURLUponReload(HOPIdentityDelegate inIdentityDelegate, string identityProviderDomain, string identityURIOridentityBaseURI, string outerFrameURLUponReload)
		{
		}
		//
		public static object loginWithDelegateIdentityProviderDomainIdentityPreauthorizedURIIdentityAccessTokenIdentityAccessSecretIdentityAccessSecretExpires(HOPIdentityDelegate inIdentityDelegate, string identityProviderDomain, string identityURI, string identityAccessToken, string identityAccessSecret, NSDate identityAccessSecretExpires)
		{

		}
		//
		public int getObjectId()
		{
			int a = 0;
			/* logic hire */
			Console.WriteLine ("Empty");
			return a;
		}
		//
		public HOPIdentityState getState()
		{
			Console.WriteLine ("Empty");
			//WORD lastErrorCode;
			string lastErrorCode;

			byte[] lastErrorReason;

			// zsLib::String lastErrorReason;
			HOPIdentityStates state = (HOPIdentityStates)identityPtr.getState(lastErrorCode, lastErrorReason);
			HOPIdentityState ret = new HOPIdentityState();
			ret.State= state;
			ret.LastErrorCode = lastErrorCode;
			ret.LastErrorReason = Encoding.UTF8.GetString(lastErrorCode);// NSString.stringWithCStringEncoding(lastErrorReason, NSUTF8StringEncoding);
			return ret;
		}
		public static bool isDelegateAttached()
		{
			bool ret = false;
			/* logic hire  */
			Console.WriteLine ("Empty");
			return ret;
		}
		public static void attachDelegateRedirectionURL(IdentityDelegate inIdentityDelegate, string redirectionURL)
		{
			/* logic hire   */
			Console.WriteLine ("Empty");
		}
		//
		public static void attachDelegateAndPreauthorizedLoginIdentityAccessTokenIdentityAccessSecretIdentityAccessSecretExpires(HOPIdentityDelegate inIdentityDelegate, string identityAccessToken, string identityAccessSecret, NSDate identityAccessSecretExpires)
		{
			/* logic hire */
			Console.WriteLine ("Empty");
		}
		public string getIdentityURI()
		{
			string ret = null;
			/* logic hire   */
			Console.WriteLine ("Empty");
			return ret;
		}

		public static string getBaseIdentityURI()
		{
			string ret = null;
			/* logic hire   */
			Console.WriteLine ("Empty");
			return ret;
		}

		public static string getIdentityProviderDomain()
		{
			string ret = null;
			/* logic hire */
			Console.WriteLine ("Empty");
    


			return ret;
		}
		public static HOPIdentityContract getSelfIdentityContact()
		{
			HOPIdentityContract ret = null;
			/* logic hire */
			Console.WriteLine ("Empty");
			return ret;
		}

		public static string getInnerBrowserWindowFrameURL()
		{
			string ret = null;
			/* logic hire */
			Console.WriteLine ("Empty");
			return ret;
		}

		public  void notifyBrowserWindowVisible()
		{
			/* logic hire */
			Console.WriteLine ("Empty");
		}

		public  void notifyBrowserWindowClosed()
		{
			/* logic hire */
			Console.WriteLine ("Empty");
		}

		public string getNextMessageForInnerBrowerWindowFrame()
		{
			string ret = null;
			/* logic hire */
			Console.WriteLine ("Empty");
			return ret;
		}

		public static void handleMessageFromInnerBrowserWindowFrame(string message)
		{
			/* logic hire */
			Console.WriteLine ("Empty");
		}

		public void cancel()
		{
			/* logic hire 8 */
			Console.WriteLine ("Empty");
		}

		/*

   + stateToString:(HOPIdentityStates) state

   {

   return [NSString stringWithUTF8String: IIdentity::toString((IIdentity::IdentityStates) state)];

   }

   + (NSString*) stringForIdentityState:(HOPIdentityStates) state

   {

   return [NSString stringWithUTF8String: IIdentity::toString((IIdentity::IdentityStates) state)];

   }

   */
		public static string description()
		{
			string ret = null;
			/* logic hire */
			Console.WriteLine ("Empty");
			return ret;
		}

		public static void startRolodexDownload(string lastDownloadedVersion)
		{
			/* logic hire */
			Console.WriteLine ("Empty");
		}

		public static void refreshRolodexContacts()
		{
			/* logic hire */
			Console.WriteLine ("Empty");
		}

		public  bool getDownloadedRolodexContactsOutVersionDownloadedOutRolodexContacts(ref bool outFlushAllRolodexContacts, ref string outVersionDownloaded, ref ArrayList outRolodexContacts)
		{
			bool ret = false;
			/* logic hire */
			Console.WriteLine ("Empty");
			return ret;
		}

		public void startTimerForContactsDeletion()
		{
			Console.WriteLine ("Empty");
			this.stopTimerForContactsDeletion();
			//this.DeletionTimer = NSTimer.scheduledTimerWithTimeIntervalTargetSelectorUserInfoRepeats(flushContactsDownloadTime, this, @selector (deleteMarkedRolodexContacts), null, false);
		}

		public void stopTimerForContactsDeletion()
		{
			//if (this.deletionTimer.isValid()) this.deletionTimer.invalidate();
			Console.WriteLine ("Empty");
		}

		public static void deleteMarkedRolodexContacts()
		{
			Console.WriteLine ("Empty");
		}

		public void destroyCoreObject()
		{
			/*logic hire */
			Console.WriteLine ("Empty");
		}

		/*

   - (id) initWithIdentityPtr:(IIdentityPtr) inIdentityPtr

   {

   self = [super init];

   if (self)

   {

   

   identityPtr = inIdentityPtr;

   NSString* uri = [NSString stringWithCString:identityPtr->getIdentityURI() encoding:NSUTF8StringEncoding];

   if (uri)

   self.identityBaseURI = [NSString stringWithString:[HOPUtility getBaseIdentityURIFromURI:uri]];

   

   }

   return self;

   }

   */
		/*

		- (id) initWithIdentityPtr:(IIdentityPtr) inIdentityPtr openPeerIdentityDelegate:(boost::shared_ptr<OpenPeerIdentityDelegate>) inOpenPeerIdentityDelegate

		{

			self = [super init];



			if (self)

			{

				identityPtr = inIdentityPtr;

				openPeerIdentityDelegatePtr = inOpenPeerIdentityDelegate;

				NSString* uri = [NSString stringWithCString:identityPtr->getIdentityURI() encoding:NSUTF8StringEncoding];

				if (uri)

					self.identityBaseURI = [NSString stringWithString:[HOPUtility getBaseIdentityURIFromURI:uri]];

			}

			return self;

		}

		*/
		public static void setLocalDelegate(IdentityDelegate inIdentityDelegate)
		{
			//openPeerIdentityDelegatePtr = OpenPeerIdentityDelegate::create(inIdentityDelegate);
		}

		/*

   - (IIdentityPtr) getIdentityPtr

   {

   return identityPtr;

   }*/
		public static String log(string message)
		{
			string result;
			//if (identityPtr) return String("HOPIdentity [") + theString(identityPtr.getID()) + "] " + message.UTF8String();
			//else return String("HOPIdentity: ") + message.UTF8String();
			Console.WriteLine ("Empty");
			return result;
		}




	}

}

