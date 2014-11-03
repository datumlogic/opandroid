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

package com.openpeer.sdk.datastore;

import android.database.Cursor;
import android.text.TextUtils;

import com.openpeer.javaapi.MessageDeliveryStates;
import com.openpeer.javaapi.OPMessage;
import com.openpeer.sdk.model.MessageEditState;

import static com.openpeer.sdk.datastore.DatabaseContracts.*;

/**
 * Helper function to construct OP data models from database cursor. IMplementation is based on data structure provided by SDK. If
 * application implements their own database these function may need modification.
 * 
 * 
 */
public class OPModelCursorHelper {

    public static OPMessage messageFromCursor(Cursor cursor) {

        OPMessage message = new OPMessage(
                cursor.getLong(cursor
                        .getColumnIndex(MessageEntry.COLUMN_SENDER_ID)),
                cursor.getString(cursor
                        .getColumnIndex(MessageEntry.COLUMN_MESSAGE_TYPE)),
                cursor.getString(cursor
                        .getColumnIndex(MessageEntry.COLUMN_MESSAGE_TEXT)),
                cursor.getLong(cursor
                        .getColumnIndex(MessageEntry.COLUMN_MESSAGE_TIME)),
                cursor.getString(cursor
                        .getColumnIndex(MessageEntry.COLUMN_MESSAGE_ID)),
                MessageEditState.values()[cursor.getInt(cursor
                        .getColumnIndex(MessageEntry.COLUMN_EDIT_STATUS))]);

        String deliveryStateStr = cursor.getString(cursor
                .getColumnIndex(MessageEntry.COLUMN_MESSAGE_DELIVERY_STATUS));
        MessageDeliveryStates deliveryState;

        if (!(TextUtils.isEmpty(deliveryStateStr))) {
            deliveryState = MessageDeliveryStates.valueOf(deliveryStateStr);
            message.setDeliveryStatus(deliveryState);
        }

        return message;
    }
}
