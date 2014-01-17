/*

 Copyright (c) 2013, SMB Phone Inc. / Hookflash Inc.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.
 
 */

package com.openpeer.javaapi;

import java.util.List;


public class OPAccount {

	public static native String toString(AccountStates state);

	public static native String toDebugString(OPAccount account, Boolean includeCommaPrefix);

    public static native void login(
                             OPAccountDelegate delegate,
                             OPConversationThreadDelegate conversationThreadDelegate,
                             OPCallDelegate callDelegate,
                             String namespaceGrantOuterFrameURLUponReload,
                             String grantID,
                             String lockboxServiceDomain,
                             Boolean forceCreateNewLockboxAccount
                             );

    public static native OPAccount relogin(
    						   OPAccountDelegate delegate,
    						   OPConversationThreadDelegate conversationThreadDelegate,
    						   OPCallDelegate callDelegate,
    						   String namespaceGrantOuterFrameURLUponReload,
                               OPElement reloginInformation
                               );

    public native String getStableID();

    public native AccountStates getState(
                                   int outErrorCode,
                                   String outErrorReason
                                   );

    public native OPElement getReloginInformation();   // NOTE: will return ElementPtr() is relogin information is not available yet
    
    public native String getLocationID();

    public native void shutdown();

    public native String getPeerFilePrivate();
    public native byte[] getPeerFilePrivateSecret();

    public native List<OPIdentity> getAssociatedIdentities();
    public native void removeIdentities (List<OPIdentity> identitiesToRemove);

    public native String getInnerBrowserWindowFrameURL();

    public native void notifyBrowserWindowVisible();
    
    public native void notifyBrowserWindowClosed();

    public native String getNextMessageForInnerBrowerWindowFrame();
    public native void handleMessageFromInnerBrowserWindowFrame(String unparsedMessage);
 
}
