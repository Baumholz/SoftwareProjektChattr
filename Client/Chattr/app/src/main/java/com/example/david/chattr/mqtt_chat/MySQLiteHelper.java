package com.example.david.chattr.mqtt_chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by manu on 17.12.2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {



    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChatDataBase.db";

    public static final String TABLE = "Messages";
    public static final String COL_1 = "name";
    public static final String COL_2 = "timestamp";
    public static final String COL_3 = "senderNr";
    public static final String COL_4 = "recipientNR";
    public static final String COL_5 = "content";

    //   public static final String SQL_ENTRIES = TABLE+COL_1 + COL_2 + COL_3 + COL_4 + COL_5 ;
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE + " ("
            + BaseColumns._ID+" INTEGER PRIMARY KEY,"
            + COL_1 + " TEXT,"
            + COL_5 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXIST " + TABLE;

    public MySQLiteHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /*public boolean insertData(String content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_5,content);
       long result = db.insert(TABLE,null,values);
       if(result == -1){
           return false;
       }else
       return true;
    }
    */
}
