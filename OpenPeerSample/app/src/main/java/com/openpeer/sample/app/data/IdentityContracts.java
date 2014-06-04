package com.openpeer.sample.app.data;

import android.provider.BaseColumns;

/**
 * Created by brucexia on 2014-06-03.
 */
public class IdentityContracts {
    public IdentityContracts() {
    }
    public static abstract class IdentityEntry implements BaseColumns {
        public static final String TABLE_NAME="identity";
        public static final String COLUMN_NAME_IDENTITY_ID="id";
        public static final String COLUMN_NAME_IDENTITY_PROVIDER="provider";

    }
}
