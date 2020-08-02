package com.medical.mytestapp.LocalDbManager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.medical.mytestapp.LocalDbManager.DatabaseContracter.FeedEntry.COLUMN_SEARCH_CODE
import com.medical.mytestapp.LocalDbManager.DatabaseContracter.FeedEntry.COLUMN_SEARCH_COMMENT
import com.medical.mytestapp.LocalDbManager.DatabaseContracter.FeedEntry.COLUMN_SEARCH_ID
import com.medical.mytestapp.LocalDbManager.DatabaseContracter.FeedEntry.COLUMN_SEARCH_IMAGE
import com.medical.mytestapp.LocalDbManager.DatabaseContracter.FeedEntry.COLUMN_SEARCH_NAME
import com.medical.mytestapp.LocalDbManager.DatabaseContracter.FeedEntry.COLUMN_SEARCH_TYPE
import com.medical.mytestapp.LocalDbManager.DatabaseContracter.FeedEntry.TABLE_SEARCH
import com.medical.mytestapp.model.Data
import java.util.*

class DatabaseHelperManager(context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    var db: SQLiteDatabase? = null

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_SEARCH)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int
    ) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TABLE_SEARCH)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int
    ) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addListItem(listItem: ArrayList<Data>) {
        val db = this.writableDatabase
        val values = ContentValues()
        for (i in listItem.indices) {
            Log.e("vlaue inserting==", "" + listItem[i].title + listItem[i].id)
            values.put(COLUMN_SEARCH_NAME, listItem[i].title)
            values.put(COLUMN_SEARCH_CODE, listItem[i].cover)
            values.put(COLUMN_SEARCH_TYPE, listItem[i].type)
            values.put(COLUMN_SEARCH_IMAGE, listItem[i].link)
            values.put(COLUMN_SEARCH_ID, listItem[i].id)
            db.insert(TABLE_SEARCH, COLUMN_SEARCH_ID, values)
        }
        db.close() // Closing database connection
    }

//    fun update(id: String, comment: String): Boolean {
//        val db = this.readableDatabase
//        db.execSQL("UPDATE " + TABLE_SEARCH + " SET " +COLUMN_SEARCH_COMMENT + " = " + "'" + comment + "' " + "WHERE " + DatabaseContract.FeedEntry.COLUMN_SEARCH_ID + " = " + "'" + id + "'")
//        db.close()
//        return true
//    }

//    fun read(image_id: String): Boolean {
//        val db = this.readableDatabase
//        val query =
//            "SELECT " + DatabaseContract.FeedEntry.COLUMN_SEARCH_COMMENT + " FROM " + DatabaseContract.FeedEntry.TABLE_SEARCH + " WHERE " + DatabaseContract.FeedEntry.COLUMN_SEARCH_ID + "='" + image_id + "' "
//        db.execSQL(query)
//        db.close()
//        return true
//    }





    val getAllSearchData: List<Data>
    get()
    {
        val db = this.readableDatabase
        val array_list = ArrayList<Data>()
        val query = "SELECT * FROM " +TABLE_SEARCH
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val dt = Data()
                dt.title = cursor.getString(cursor.getColumnIndex(COLUMN_SEARCH_NAME))
                dt.id = cursor.getString(cursor.getColumnIndex(COLUMN_SEARCH_ID))
                    dt.link = cursor.getString(cursor.getColumnIndex(COLUMN_SEARCH_IMAGE))
         //       Log.i("the","data"+dt.title)
                array_list.add(dt)
            } while (cursor.moveToNext())
            db.close()
        }
     return array_list
    }


    fun getSearch(searchName: String): List<Data> {
//        val query_search = "SELECT * FROM SEARCH_RECORDS WHERE title LIKE '%Val_%'"
        val query_search = "SELECT id,comment,title,link FROM SEARCH_RECORDS WHERE title LIKE '%"+ searchName+"_%'"
        Log.i("the","query"+query_search)
        val db = readableDatabase
        val cursor = db.rawQuery(query_search, null)
        val search_list = ArrayList<Data>()
        if (cursor.moveToFirst()) {
            run {
                do {
                    val dt = Data()
                    dt.title = cursor.getString(cursor.getColumnIndex(COLUMN_SEARCH_NAME))
                    dt.id = cursor.getString(cursor.getColumnIndex(COLUMN_SEARCH_ID))
                   dt.link = cursor.getString(cursor.getColumnIndex(COLUMN_SEARCH_IMAGE))
                    search_list.add(dt)
                    Log.i("the","data"+dt)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
        }
        return search_list
    }



    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 4
        const val DATABASE_NAME = "ImageDatabase.db"
        private const val SQL_CREATE_SEARCH =
            "CREATE TABLE " + TABLE_SEARCH + " (" +
                    COLUMN_SEARCH_TYPE + " TEXT," +
                    COLUMN_SEARCH_CODE + " TEXT," +
                    COLUMN_SEARCH_ID + " TEXT," +
                    COLUMN_SEARCH_COMMENT + " TEXT," +
                    COLUMN_SEARCH_IMAGE + " TEXT," +
                    COLUMN_SEARCH_NAME + " TEXT)"

        private const val SQL_DELETE_TABLE_SEARCH =
            "DROP TABLE IF EXISTS " + TABLE_SEARCH
    }


}