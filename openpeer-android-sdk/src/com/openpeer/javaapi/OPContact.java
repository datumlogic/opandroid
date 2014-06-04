package com.openpeer.javaapi;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.openpeer.openpeernativesampleapp.LoginManager;


public class OPContact {

	private long nativeClassPointer;
	
	public static native String toDebugString(OPContact contact, boolean includeCommaPrefix);

    public static native OPContact createFromPeerFilePublic(
                                                OPAccount account,
                                                String peerFilePublicEl
                                                );

    public static native OPContact getForSelf(OPAccount account);

    public native long getStableID();

    public native boolean isSelf();
    public native String getPeerURI();
    
    public native String getPeerFilePublic();

    public native OPAccount getAssociatedAccount();

    public native void hintAboutLocation(String contactsLocationID);
//    public String toString(){
//		return super.toString()+ getPeerURI());
//		Log.d("output", contacts.get(0).getPeerFilePublic());
//		Log.d("output", "stable Id = " + contacts.get(0).getStableID());
//		List<OPIdentityContact> identityContactList = new ArrayList<OPIdentityContact>();
//		identityContactList = LoginManager.mConvThread.getIdentityContactList(contacts.get(0));
//		Log.d("output", "contact size = " + identityContactList.size());
//		Log.d("output", identityContactList.get(0).getName());
//		Log.d("output", identityContactList.get(0).getIdentityURI());
//    }
}
