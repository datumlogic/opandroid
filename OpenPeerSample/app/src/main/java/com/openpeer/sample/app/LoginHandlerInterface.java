package com.openpeer.sample.app;

import com.openpeer.javaapi.OPIdentity;

/**
 * Created by brucexia on 2014-06-03.
 */
public interface LoginHandlerInterface {

    void onLoadOuterFrameHandle(Object obj);

    void onInnerFrameInitialized(String innerFrameUrl);

    void passMessageToJS(String msg);

    void onNamespaceGrantInnerFrameInitialized(String innerFrameUrl);

    void onAccountStateReady();

    void onDownloadedRolodexContacts(OPIdentity identity);

    void onLookupCompleted();

}
