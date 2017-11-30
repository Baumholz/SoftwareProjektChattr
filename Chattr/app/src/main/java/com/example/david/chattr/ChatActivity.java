package com.example.david.chattr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.david.chattr.entities.messaging.Message;
import com.example.david.chattr.mqtt_chat.MyMqttService;

import com.example.david.chattr.mqtt_chat.MyMqttService.MyLocalBinder;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    MyMqttService mqttService;
    ArrayList<Message> messages;
    EditText giveInput;
    String name;
    String phoneNumber;
    ChatActivityListViewAdapter myChatActivityListViewAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to MyMqttService
        Intent intent = new Intent(ChatActivity.this, MyMqttService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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

    //Here is where the Button Click is handled
    public void onEditTextButtonClicked(View v) {
        String message = giveInput.getText().toString();
        if (!message.isEmpty()) {
            Message myMessage = new Message(name, null, null, false, null, message);
            messages.add(myMessage);
            myChatActivityListViewAdapter.notifyDataSetChanged();
            //Todo: Do the topic timestamp thing
            mqttService.sendMessage("pub/trainID/camID/", myMessage.getText());
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

    // This is needed in every Activity that needs the MyMqttService (to be able to call its methods)
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // Bound to MyMqttService
            MyLocalBinder binder = (MyLocalBinder) iBinder;
            mqttService = binder.getService();


            //Start Service
            Intent startServiceIntent = new Intent(ChatActivity.this, MyMqttService.class);
            startService(startServiceIntent);

            // Step 1: Creates New Chat by sending a hidden message to:
            // topic: “receiver_number” with palyloud: Sender “sender_nr” ChatTopic “rand_nummer_timestamp”

            phoneNumber = (String)getIntent().getSerializableExtra("phoneNumber");
            //Todo: Systax for hidden message (JSON?)
            mqttService.sendMessage(phoneNumber, "sender_nr\n" + "pub/trainID/camID/");
            //Subscribe to newly created topic Todo: Real timestamp as topic
            mqttService.subscribe("pub/trainID/camID/");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

}
