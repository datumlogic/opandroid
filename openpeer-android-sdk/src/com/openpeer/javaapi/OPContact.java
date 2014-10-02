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
package com.openpeer.javaapi;

public class OPContact {

    private long nativeClassPointer;

    @Override
    public boolean equals(Object o) {
        return o instanceof OPContact
                && getStableID() == ((OPContact) o).getStableID();
    }

    /**
     * @ExcludeFromJavadoc
     * @param contact
     * @param includeCommaPrefix
     * @return
     */
    public static native String toDebugString(OPContact contact,
            boolean includeCommaPrefix);

    public static native OPContact createFromPeerFilePublic(
            OPAccount account,
            String peerFilePublicEl
            );

    /**
     * Construct OPContact instance for the account
     * 
     * @param account
     * @return
     */
    public static native OPContact getForSelf(OPAccount account);

    public native long getStableID();

    /**
     * If the contact is the logged in user self.
     * 
     * @return
     */
    public native boolean isSelf();

    public native String getPeerURI();

    public native String getPeerFilePublic();

    public native OPAccount getAssociatedAccount();

    public native void hintAboutLocation(String contactsLocationID);

    private native void releaseCoreObjects();

    protected void finalize() throws Throwable {

        if (nativeClassPointer != 0)
        {
            releaseCoreObjects();
        }

        super.finalize();
    }
}
