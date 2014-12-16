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
package com.openpeer.sdk.model;

/**
 *
 */
public class OPConversationEvent {
    String conversationId;
    private EventTypes event;
    private String mDescription;
    private long mId;
    private long mTime;
    long cbcId;

    public long getTime() {
        return mTime;
    }

    public long getCbcId() {
        return cbcId;
    }

    public void setCbcId(long cbcId) {
        this.cbcId = cbcId;
    }

    /**
     * @param conversation
     *            TODO
     * @param event
     * @param description
     *            TODO
     */
    public OPConversationEvent(OPConversation conversation, EventTypes event, String description) {
        this(conversation.getConversationId(), event, description, conversation.getCurrentWindowId(),System.currentTimeMillis());
    }

    public OPConversationEvent(String conversationId,
                               EventTypes event,
                               String description,
                               long cbcId,
            long time) {
        super();
        this.conversationId = conversationId;
        this.event = event;
        this.mDescription = description;
        this.cbcId=cbcId;
        this.mTime = time;

    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversation(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public enum EventTypes {
        NewConversation,
        ContactsAdded,
        ContactsRemoved,
        TopicChange
    }

    public EventTypes getEvent() {
        return event;
    }

    public void setEvent(EventTypes event) {
        this.event = event;
    }

    public String getContent() {
        return mDescription;
    }

    public void setContent(String content) {
        this.mDescription = content;
    }

    /**
     * @return
     */
    public long getId() {
        // TODO Auto-generated method stub
        return mId;
    }

}
