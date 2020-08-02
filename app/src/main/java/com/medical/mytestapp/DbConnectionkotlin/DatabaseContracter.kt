package com.medical.mytestapp.LocalDbManager

import android.provider.BaseColumns

class DatabaseContracter{
    object FeedEntry : BaseColumns {
        const val TABLE_SEARCH = "SEARCH_RECORDS"
        const val COLUMN_SEARCH_NAME = "title"
        const val COLUMN_SEARCH_ID = "id"
        const val COLUMN_SEARCH_CODE = "cover"
        const val COLUMN_SEARCH_IMAGE = "link"
        const val COLUMN_SEARCH_TYPE = "type"
        const val COLUMN_SEARCH_COMMENT = "comment"
    }
}
