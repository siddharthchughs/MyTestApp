package com.medical.mytestapp.LocalDbManager;

import android.provider.BaseColumns;

public final class DatabaseContract {

    private DatabaseContract() {}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_SEARCH = "SEARCH_RECORDS";
        public static final String COLUMN_SEARCH_NAME = "title";
        public static final String COLUMN_SEARCH_ID = "id";
        public static final String COLUMN_SEARCH_CODE = "cover";
        public static final String COLUMN_SEARCH_IMAGE = "link";
        public static final String COLUMN_SEARCH_TYPE = "type";
        public static final String COLUMN_SEARCH_COMMENT = "comment";
    }

}