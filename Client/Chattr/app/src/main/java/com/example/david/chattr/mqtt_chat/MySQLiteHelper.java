package com.example.david.chattr.mqtt_chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.homeactivity_fragments.ChatListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manu on 17.12.2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    // protected class ContactTablerHandler()
//    {
//      void readDB()
//    }

    // private ContactTableHandler contactHandler = new ContactTableHandler();
    // void readContacts()
    // {
    //  contactHandler.readDB();
    // }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChatDataBase.db";

    //Table Message
    public static final String TABLE = "Messages";
    public static final String COL_1 = "name";
    public static final String COL_2 = "timestamp";
    public static final String COL_3 = "senderNr";
    public static final String COL_4 = "recipientNR";
    public static final String COL_5 = "content";
    // Table Profile
    public static final String TABLE_PROFILE = "userProfile";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String STATUS = "status";
    public static final String FIRST_NAME = "firstName";
    public static final String NAME = "name";
    public static final String PROFILE_PICTURE = "profilePicture";

    //   public static final String SQL_ENTRIES = TABLE+COL_1 + COL_2 + COL_3 + COL_4 + COL_5 ;
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE + " ("
            + BaseColumns._ID+" INTEGER PRIMARY KEY,"
            + COL_1 + " TEXT,"
            + COL_5 + " TEXT)";

    public static final String SQL_CREATE_TABLE_PROFILE = "CREATE TABLE " + TABLE_PROFILE + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + FIRST_NAME  + " TEXT,"
            + NAME + " TEXT,"
            + PHONE_NUMBER + " TEXT)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXIST " + TABLE;
    public static final String SQL_DELETE_PROFILE_ENTRIES = "DROP TABLE IF EXIST " + TABLE_PROFILE;

    public MySQLiteHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_TABLE_PROFILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_PROFILE_ENTRIES);
        onCreate(db);
    }

    public ArrayList<UserProfile> getProfiles(){
        ArrayList<UserProfile> recipients = new ArrayList<UserProfile>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(MySQLiteHelper.TABLE_PROFILE, null);

        while (!c.isAfterLast()) {
            String tempFirstName = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.FIRST_NAME));
            String tempName = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.FIRST_NAME));
            String tempPhoneNumber = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.FIRST_NAME));

            UserProfile user = new UserProfile(tempPhoneNumber, "none", tempFirstName, tempName, R.drawable.hund);
            recipients.add(user);
        }
        return recipients;
    }

}
