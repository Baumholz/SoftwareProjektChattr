package com.example.david.chattr;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.example.david.chattr.entities.users.Message;
import com.example.david.chattr.entities.users.UserProfile;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ArrayList<Message> messages
            ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String name = (String)getIntent().getSerializableExtra("name");
        //only to test it
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
      //  getSupportActionBar().setIcon(R.drawable.hund);


        UserProfile user1 = new UserProfile("0340442323","none","0","Olaf",R.drawable.hund);
        UserProfile user2 = new UserProfile("0340446364","none","1","Harald",R.drawable.hund2);
        Message m1 = new Message("53423234","23243423",null,false,null,"Hey what's up");
        Message m2 = new Message("23243423","53423234",null,false,null,"Software Priject");

        messages = new ArrayList<Message>();
        messages.add(m1);
        messages.add(m2);

        ChatActivityListViewAdapter myChatActivityListViewAdapter = new ChatActivityListViewAdapter(messages);


        ImageView profilPicture = (ImageView) findViewById(R.id.profilePicutreChat);
        ListView chatListView = (ListView) findViewById(R.id.chat);
        chatListView.setAdapter(myChatActivityListViewAdapter);

        EditText giveInput = (EditText)findViewById(R.id.chatInput);

        int pic = (int)getIntent().getSerializableExtra("picture");


        profilPicture.setImageResource(pic);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_activity_menu, menu);
        return true;
    }



    //for the back button
    @Override
    public  boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

}
