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
package com.openpeer.sample.push;

import java.util.Arrays;

public class PushResult {
    boolean ok;
    String operation_id;
    String push_ids[];

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getOperationId() {
        return operation_id;
    }

    public void setOperationId(String operation_id) {
        this.operation_id = operation_id;
    }

    public String[] getPushIds() {
        return push_ids;
    }

    public void setPushIds(String[] push_ids) {
        this.push_ids = push_ids;
    }

    public String toString() {
        return "ok " + ok + " OperationId " + operation_id + " push_ids " + Arrays.deepToString(push_ids);
    }
}
