package com.example.david.chattr.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Base64;
import android.util.Log;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.messaging.Message;
import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.fragments.ChatListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manu on 17.12.2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChatDataBase.db";

    //Table Message
    public static final String TABLE = "Messages";
   // public static final String COL_1 = "name";
    public static final String TIMESTAMP = "timestamp";
    public static final String SENDERnr = "senderNr";
    public static final String RECIPIENtnr = "recipientNR";
    public static final String MsgCONTENT = "content";
    // Table Profile
    public static final String TABLE_PROFILE = "userProfile";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String STATUS = "status";
    public static final String FIRST_NAME = "firstName";
    public static final String NAME = "name";
    public static final String PROFILE_PICTURE = "profilePicture";
    public static final String COVER_IMAGE = "coverImage";
    public static final String WRITEABLE = "writeable";
    public static final String TOPIC = "topic";

    //   public static final String SQL_ENTRIES = TABLE+COL_1 + TIMESTAMP + SENDERnr + RECIPIENtnr + MsgCONTENT ;
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            +TIMESTAMP + " TEXT,"
            +SENDERnr +" TEXT,"
            +RECIPIENtnr +" TEXT,"
            +MsgCONTENT +" TEXT)";

    public static final String SQL_CREATE_TABLE_PROFILE = "CREATE TABLE " + TABLE_PROFILE + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY,"
            + FIRST_NAME + " TEXT,"
            + NAME + " TEXT,"
            + PHONE_NUMBER + " TEXT,"
            + PROFILE_PICTURE + " TEXT,"
            + COVER_IMAGE + " TEXT,"
            + WRITEABLE + " TEXT,"
            + TOPIC + " TEXT)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXIST " + TABLE;
    public static final String SQL_DELETE_PROFILE_ENTRIES = "DROP TABLE IF EXIST " + TABLE_PROFILE;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public ArrayList<UserProfile> getProfiles() {
        ArrayList<UserProfile> recipients = new ArrayList<UserProfile>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + MySQLiteHelper.TABLE_PROFILE;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String tempFirstName = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.FIRST_NAME));
            String tempName = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.NAME));
            String tempPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.PHONE_NUMBER));
            byte[] tempProfilePicture = cursor.getBlob(cursor.getColumnIndexOrThrow(MySQLiteHelper.PROFILE_PICTURE));
            byte[] tempCoverImage = cursor.getBlob(cursor.getColumnIndexOrThrow(MySQLiteHelper.COVER_IMAGE));
            String tempWriteable = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.WRITEABLE));
            String tempTopic = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.TOPIC));

            UserProfile user = new UserProfile(tempPhoneNumber, "none", tempFirstName, tempName, tempProfilePicture, tempCoverImage, tempWriteable, tempTopic);
            recipients.add(user);
        }
        return recipients;
    }

    public ArrayList<UserProfile> getProfilesWritable() {
        ArrayList<UserProfile> recipients = new ArrayList<UserProfile>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + MySQLiteHelper.TABLE_PROFILE;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String tempFirstName = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.FIRST_NAME));
            String tempName = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.NAME));
            String tempPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.PHONE_NUMBER));
            byte[] tempProfilePicture = cursor.getBlob(cursor.getColumnIndexOrThrow(MySQLiteHelper.PROFILE_PICTURE));
            byte[] tempCoverImage = cursor.getBlob(cursor.getColumnIndexOrThrow(MySQLiteHelper.COVER_IMAGE));
            String tempWriteable = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.WRITEABLE));
            String tempTopic = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.TOPIC));

            UserProfile user = new UserProfile(tempPhoneNumber, "none", tempFirstName, tempName, tempProfilePicture, tempCoverImage, tempWriteable, tempTopic);
            if (user.getWriteable().equals("true")) {
                recipients.add(user);
            }
        }
        return recipients;
    }

    public void updateTopic(String phoneNumber, String topic) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + MySQLiteHelper.TABLE_PROFILE + " where " + MySQLiteHelper.PHONE_NUMBER + "='" + phoneNumber + "';";
        Cursor c = db.rawQuery(query, null);

        String firstName = "";
        String name = "";
        byte[] profilePicture = null;
        byte[] coverImage = null;
        String writeable = "";

        while (c.moveToNext()) {
            firstName = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.FIRST_NAME));
            name = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.NAME));
            phoneNumber = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.PHONE_NUMBER));
            profilePicture = c.getBlob(c.getColumnIndexOrThrow(MySQLiteHelper.PROFILE_PICTURE));
            coverImage = c.getBlob(c.getColumnIndexOrThrow(MySQLiteHelper.COVER_IMAGE));
            writeable = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.WRITEABLE));
        }
        c.close();
        db.close();

        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.FIRST_NAME, firstName);
        cv.put(MySQLiteHelper.NAME, name);
        cv.put(MySQLiteHelper.WRITEABLE, writeable);
        cv.put(MySQLiteHelper.PHONE_NUMBER, phoneNumber);
        cv.put(MySQLiteHelper.PROFILE_PICTURE, profilePicture);
        cv.put(MySQLiteHelper.COVER_IMAGE, coverImage);
        cv.put(MySQLiteHelper.TOPIC, topic);

        db.update(MySQLiteHelper.TABLE_PROFILE, cv, MySQLiteHelper.PHONE_NUMBER + "=" + phoneNumber, null);

    }
}