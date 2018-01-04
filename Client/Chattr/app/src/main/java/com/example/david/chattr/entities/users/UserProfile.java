package com.example.david.chattr.entities.users;


import android.database.sqlite.SQLiteDatabase;

import com.example.david.chattr.mqtt_chat.MySQLiteHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile extends  Profile{

    private String phoneNumber;
    private String status;

    public UserProfile(String phoneNumber, String status, String firstName, String surName,int profilePicture) {
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.firstName = firstName;
        this.name = surName;
        this.profilePicture = profilePicture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public JSONObject toJson () {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneNumber", phoneNumber);
            jsonObject.put("status", status);
            jsonObject.put("firstName", firstName);
            jsonObject.put("name", name);
            jsonObject.put("profilePicture", profilePicture);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
