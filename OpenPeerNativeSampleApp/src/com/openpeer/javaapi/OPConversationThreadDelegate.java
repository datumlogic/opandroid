package com.openpeer.javaapi;


public abstract class OPConversationThreadDelegate {

	public abstract void onConversationThreadNew(OPConversationThread conversationThread);

	public abstract void onConversationThreadContactsChanged(OPConversationThread conversationThread);
	public abstract void onConversationThreadContactStateChanged(
														 OPConversationThread conversationThread,
                                                         OPContact contact,
                                                         ContactStates state
                                                         );

	public abstract void onConversationThreadMessage(
			OPConversationThread conversationThread,
                                             String messageID
                                             );

	public abstract void onConversationThreadMessageDeliveryStateChanged(
																 OPConversationThread conversationThread,
																 String messageID,
                                                                 MessageDeliveryStates state
                                                                 );

	public abstract void onConversationThreadPushMessage(
												 OPConversationThread conversationThread,
												 String messageID,
                                                 OPContact contact
                                                 );
}
