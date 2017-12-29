package com.example.david.chattr.mqtt_chat;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.david.chattr.R;
import com.example.david.chattr.entities.messaging.Message;

import com.example.david.chattr.mqtt_chat.MyMqttService.MyLocalBinder;
import com.example.david.chattr.mqtt_chat.MySQLiteHelper;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements MessageArrivedListener {

    MyMqttService mqttService;
    ArrayList<Message> messages;
    EditText giveInput;
    String name;
    String phoneNumber;
    ChatActivityListViewAdapter myChatActivityListViewAdapter;

    MySQLiteHelper myDb = new MySQLiteHelper(this);
    SQLiteDatabase db;

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
        ListView chatListView = (ListView) findViewById(R.id.chat);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        messages = new ArrayList<Message>();

        db = myDb.getReadableDatabase();
        String [] projection = {

                BaseColumns._ID,
                MySQLiteHelper.COL_1,
                MySQLiteHelper.COL_5
        };
        String selection = MySQLiteHelper.COL_1 + " = ?";
        String [] selectionArgs = {name};
        Cursor c = db.query(MySQLiteHelper.TABLE, projection, selection, selectionArgs,null,null,null);

        while (c.moveToNext()){
            String temp = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.COL_5));
            Message oldMessage = new Message(name,2004,"me",false,temp);
            messages.add(oldMessage);
        }

        myChatActivityListViewAdapter = new ChatActivityListViewAdapter(messages);
        chatListView.setAdapter(myChatActivityListViewAdapter);

        // Set Toolbar Title and Profile Picture
        CircleImageView profilPicture = (CircleImageView) findViewById(R.id.profilePicutreChat);
        TextView chatActivityTitle = (TextView) findViewById(R.id.chatActivityTitle);
        chatActivityTitle.setText(name);
        int pic = (int)getIntent().getSerializableExtra("picture");
        profilPicture.setImageResource(pic);
    }

    //Here is where the Button Click is handled
    public void onEditTextButtonClicked(View v) {
        String message = giveInput.getText().toString();
        if (!message.isEmpty()) {
            Message myMessage = new Message(name, 2004, "me", false, message);
            messages.add(myMessage);
            myChatActivityListViewAdapter.notifyDataSetChanged();
            //Todo: Do the topic timestamp thing
            mqttService.sendMessage("all/pub/trainID/camID/", myMessage.getContent());

            db = myDb.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COL_1,name);
            values.put(MySQLiteHelper.COL_5,message);
            long result = db.insert(MySQLiteHelper.TABLE, null, values);
           // boolean isInserted =  myDb.insertData(message);

            if(result != -1) {
               Toast.makeText(ChatActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
            }else{
               Toast.makeText(ChatActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();
            }

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
            binder.setMessageArrivedListener(ChatActivity.this);


            //Start Service
            Intent startServiceIntent = new Intent(ChatActivity.this, MyMqttService.class);
            startService(startServiceIntent);

            // Step 1: Creates New Chat by sending a hidden message to:
            // topic: “receiver_number” with palyloud: Sender “sender_nr” ChatTopic “rand_nummer_timestamp”

            phoneNumber = (String)getIntent().getSerializableExtra("phoneNumber");
            //Todo: Systax for hidden message (JSON?)
            mqttService.sendMessage("all/pub/trainID/camID/", "message");
            //Subscribe to newly created topic Todo: Real timestamp as topic
            mqttService.subscribe("all/pub/trainID/camID/");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        // TODO: Save message into database
    }
}
