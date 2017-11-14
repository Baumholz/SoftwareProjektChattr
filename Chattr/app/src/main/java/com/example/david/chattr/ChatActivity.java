package com.example.david.chattr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.example.david.chattr.entities.users.UserProfile;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ArrayList<UserProfile> recipients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

    }
}
