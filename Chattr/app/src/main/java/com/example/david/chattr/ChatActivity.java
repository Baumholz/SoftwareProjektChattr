package com.example.david.chattr;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.david.chattr.entities.users.Message;
import com.example.david.chattr.entities.users.UserProfile;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ArrayList<Message> messages
            ;
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
        Button sendButton = (Button) findViewById(R.id.sendButton);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intentToAdapter = new Intent(ChatActivity.this,ChatActivityListViewAdapter.class);
        intentToAdapter.putExtra("me",name);

        Message m1 = new Message("Harald","Olaf",null,false,null,"Hey what's up");
        Message m2 = new Message("Olaf","Harald",null,false,null,"Software Priject");

        messages = new ArrayList<Message>();
        messages.add(m1);
        messages.add(m2);

        myChatActivityListViewAdapter = new ChatActivityListViewAdapter(messages);

        chatListView.setAdapter(myChatActivityListViewAdapter);


        int pic = (int)getIntent().getSerializableExtra("picture");
        profilPicture.setImageResource(pic);

//        sendButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.send));

//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = giveInput.getText().toString();
//                if (message.isEmpty() == false) {
//                    Message myMessage = new Message(name, null, null, false, null, message);
//                    messages.add(myMessage);
//                }
//                giveInput.setText("");
//            }
//        });
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
