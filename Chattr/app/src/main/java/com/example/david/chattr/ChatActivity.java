package com.example.david.chattr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.example.david.chattr.entities.users.Message;
import com.example.david.chattr.entities.users.UserProfile;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Intent intent = getIntent();

        //only to test it

        UserProfile user1 = new UserProfile("0340442323","none","0","Olaf",R.drawable.hund);
        UserProfile user2 = new UserProfile("0340446364","none","1","Harald",R.drawable.hund2);
        Message m1 = new Message("53423234","23243423",null,false,null,"Hey what's up");
        Message m2 = new Message("23243423","53423234",null,false,null,"Software Priject");

        messages = new ArrayList<Message>();
        messages.add(m1);
        messages.add(m2);

        ChatActivityListViewAdapter myChatActivityListViewAdapter = new ChatActivityListViewAdapter(messages);
        // is not working
        ListView chatListView = (ListView) findViewById(R.id.chat);
        chatListView.setAdapter(myChatActivityListViewAdapter);

        TextView chatPartner = (TextView)findViewById(R.id.chatPartner);
        EditText giveInput = (EditText)findViewById(R.id.chatInput);


        chatPartner.setText(user1.getName());

    }
}
