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
package com.openpeer.sdk.delegates;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @ExcludeFromJavadoc Helper class for cache and setting database.
 */
public class OPCoreDBHelper extends SQLiteOpenHelper {
    private static OPCoreDBHelper instance;
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "opcore.db";
    SQLiteDatabase mDB;

    /**
     * Get writable DB and make it thread safe/optimized
     * 
     * @return
     */
    public SQLiteDatabase getWritableDB() {

        if (mDB == null || !mDB.isOpen()) {
            mDB = instance.getWritableDatabase();
            if (android.os.Build.VERSION.SDK_INT > 15) {
                mDB.enableWriteAheadLogging();
            } else {
                mDB.setLockingEnabled(true);
            }
        }
        return mDB;
    }

    public static OPCoreDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new OPCoreDBHelper(context,
                    DATABASE_NAME,
                    null,
                    DATABASE_VERSION);
        }
        return instance;
    }

    public OPCoreDBHelper(Context context, String name,
            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : CREATE_STATEMENTS) {
            db.execSQL(sql);
        }
        OPSettingsDelegateImpl.loadDefaultSettings(db);
    }

    static final String CREATE_STATEMENTS[] = { OPCacheDelegateImpl.SQL_CREATE_CACHE,
            OPSettingsDelegateImpl.SQL_CREATE_SETTINGS };

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            OPSettingsDelegateImpl.onUpgrade(db,oldVersion,newVersion);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

   
}
