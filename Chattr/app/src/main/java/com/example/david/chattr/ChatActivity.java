package com.example.david.chattr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.david.chattr.entities.messaging.Message;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ArrayList<Message> messages;
    EditText giveInput;
    String name;
    ChatActivityListViewAdapter myChatActivityListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        name = (String)getIntent().getSerializableExtra("name");

        giveInput = (EditText)findViewById(R.id.chatInput);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        ImageView profilPicture = (ImageView) findViewById(R.id.profilePicutreChat);
        ListView chatListView = (ListView) findViewById(R.id.chat);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intentToAdapter = new Intent(ChatActivity.this,ChatActivityListViewAdapter.class);
        intentToAdapter.putExtra("me",name);

        messages = new ArrayList<Message>();

        myChatActivityListViewAdapter = new ChatActivityListViewAdapter(messages);
        chatListView.setAdapter(myChatActivityListViewAdapter);


        int pic = (int)getIntent().getSerializableExtra("picture");
        profilPicture.setImageResource(pic);
    }

    public void onEditTextButtonClicked(View v) {
        String message = giveInput.getText().toString();
        if (message.isEmpty() == false) {
            Message myMessage = new Message(name, null, null, false, null, message);
            messages.add(myMessage);
            myChatActivityListViewAdapter.notifyDataSetChanged();
        }
        giveInput.setText("");
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
