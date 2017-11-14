package com.example.david.chattr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


import com.example.david.chattr.entities.users.UserProfile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<UserProfile> recipients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserProfile user1 = new UserProfile("0340442323","none","0","Olaf",R.drawable.hund);
        UserProfile user2 = new UserProfile("0340446364","none","1","Harald",R.drawable.hund2);

        recipients = new ArrayList<UserProfile>();

        recipients.add(user1);
        recipients.add(user2);

        ChatListAdapter myChatListAdapter = new ChatListAdapter(recipients);
        ListView chatListView = (ListView) findViewById(R.id.chatListView);
        chatListView.setAdapter(myChatListAdapter);
    }
}
