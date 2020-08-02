package com.medical.mytestapp.LocalDbManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.medical.mytestapp.model.Data;
import java.util.ArrayList;
import static com.medical.mytestapp.LocalDbManager.DatabaseContract.FeedEntry.COLUMN_SEARCH_CODE;
import static com.medical.mytestapp.LocalDbManager.DatabaseContract.FeedEntry.COLUMN_SEARCH_COMMENT;
import static com.medical.mytestapp.LocalDbManager.DatabaseContract.FeedEntry.COLUMN_SEARCH_ID;
import static com.medical.mytestapp.LocalDbManager.DatabaseContract.FeedEntry.COLUMN_SEARCH_IMAGE;
import static com.medical.mytestapp.LocalDbManager.DatabaseContract.FeedEntry.COLUMN_SEARCH_NAME;
import static com.medical.mytestapp.LocalDbManager.DatabaseContract.FeedEntry.COLUMN_SEARCH_TYPE;
import static com.medical.mytestapp.LocalDbManager.DatabaseContract.FeedEntry.TABLE_SEARCH;


public  class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "Database.db";
    public SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SEARCH);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TABLE_SEARCH);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_CREATE_SEARCH =
            "CREATE TABLE " + TABLE_SEARCH + " (" +
                    COLUMN_SEARCH_TYPE+ " TEXT," +
                    COLUMN_SEARCH_CODE+ " TEXT," +
                    COLUMN_SEARCH_ID+ " TEXT," +
                    COLUMN_SEARCH_COMMENT+ " TEXT," +
                    COLUMN_SEARCH_IMAGE+ " TEXT," +
                    COLUMN_SEARCH_NAME+ " TEXT)";

    private static final String SQL_DELETE_TABLE_SEARCH =
            "DROP TABLE IF EXISTS " + TABLE_SEARCH;

    public void addListItem(ArrayList<Data> listItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < listItem.size(); i++) {
            Log.e("vlaue inserting==", "" + listItem.get(i).getTitle()+listItem.get(i).getId());
            values.put(COLUMN_SEARCH_NAME, listItem.get(i).getTitle());
            values.put(COLUMN_SEARCH_CODE, listItem.get(i).getCover());
            values.put(COLUMN_SEARCH_TYPE,listItem.get(i).getType());
            values.put(COLUMN_SEARCH_IMAGE,listItem.get(i).getLink());
            values.put(COLUMN_SEARCH_ID,listItem.get(i).getId());
            db.insert(TABLE_SEARCH, COLUMN_SEARCH_ID, values);
        }
        db.close(); // Closing database connection
    }

    public boolean update(String id, String comment) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE "+TABLE_SEARCH+" SET "+ COLUMN_SEARCH_COMMENT +" = "+"'"+comment+"' "+ "WHERE "+COLUMN_SEARCH_ID+" = "+"'"+id+"'");
        db.close();
        return true;
    }

    public ArrayList getAlldata() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SEARCH ;
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return array_list;
    }
}
