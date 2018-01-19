package com.example.david.chattr.messaging;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.david.chattr.R;
import com.example.david.chattr.utils.BitmapScaler;
import com.example.david.chattr.utils.ImageSaver;
import com.example.david.chattr.utils.MySQLiteHelper;
import com.example.david.chattr.adapters.ChatActivityListViewAdapter;
import com.example.david.chattr.entities.messaging.Message;

import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.messaging.MyMqttService.MyLocalBinder;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements MessageArrivedListener {

    private MyMqttService mqttService;
    private ArrayList<Message> messages;
    private EditText giveInput;
    private String name;
    private String recipientNR;
    private String senderNr;
    private ChatActivityListViewAdapter myChatActivityListViewAdapter;
    private UserProfile temp;
    private Bitmap bitmap;
    private String topic;

    private MySQLiteHelper myDb = new MySQLiteHelper(this);
    private SQLiteDatabase db;

    private ImageView image;

    private boolean isWaitingForImage = false;

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

        recipientNR = (String)getIntent().getSerializableExtra("phoneNumber");

        giveInput = (EditText)findViewById(R.id.chatInput);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView chatListView = (ListView) findViewById(R.id.chat);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        senderNr = sharedPreferences.getString("phoneNumber", "default");

        messages = new ArrayList<Message>();

        //Read out the Database
        db = myDb.getReadableDatabase();
//        String [] projection = {
//
//                BaseColumns._ID,
//                MySQLiteHelper.COL_1,
//                MySQLiteHelper.COL_3,
//                MySQLiteHelper.COL_4,
//                MySQLiteHelper.COL_5
//        };
//        String selection = MySQLiteHelper.COL_4 + " = ?";
//        String [] selectionArgs = {recipientNR};
        Log.e("Number",recipientNR);
        String query ="select * from " + MySQLiteHelper.TABLE + " where " + MySQLiteHelper.COL_3 +
                "='" + recipientNR + "' or " + MySQLiteHelper.COL_4 + "='" + recipientNR + "';";

        Cursor c = db.rawQuery(query, null);
//        Cursor c = db.query(MySQLiteHelper.TABLE, projection, selection, selectionArgs,null,null,null);

        while (c.moveToNext()){
            String temp = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.COL_5));
            //name = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.COL_1));
            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();
            Timestamp currentTimestamp = new Timestamp(now.getTime());
            Message oldMessage = new Message(name,currentTimestamp.getNanos(),"me",recipientNR,temp);
            messages.add(oldMessage);
        }
        ArrayList<UserProfile> recipients = new ArrayList<UserProfile>(myDb.getProfiles());

        for(int i=0; i < recipients.size(); i++){
           if(recipients.get(i).getPhoneNumber().equals(recipientNR)){
                temp = recipients.get(i);
           }
        }

        //finish DB

        image = (ImageView) findViewById(R.id.image);

        myChatActivityListViewAdapter = new ChatActivityListViewAdapter(messages, recipientNR);
        chatListView.setAdapter(myChatActivityListViewAdapter);

        // Set Toolbar Title and Profile Picture
        CircleImageView profilPicture = findViewById(R.id.profilePicutreChat);
        TextView chatActivityTitle = findViewById(R.id.chatActivityTitle);

        chatActivityTitle.setText(temp.getFirstName() + " " + temp.getName());
        if(Arrays.equals(temp.getProfilePicture(), "-1".getBytes())){
            profilPicture.setImageResource(R.drawable.default_profile);
        }else {
            profilPicture.setImageBitmap(BitmapFactory.decodeByteArray(temp.getProfilePicture(), 0, temp.getProfilePicture().length));
        }
        //todo: hier muss das ProfilPicture rein /dB
    }

    @Override
    public void onDestroy()
    {
        unbindService(mConnection);
        super.onDestroy();
    }

    //Here is where the Button Click is handled
    public void onEditTextButtonClicked(View v) {
        String message = giveInput.getText().toString();
        if (!message.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();
            Timestamp currentTimestamp = new Timestamp(now.getTime());
            Message myMessage = new Message("1", currentTimestamp.getNanos(), senderNr, recipientNR, message);
            messages.add(myMessage);
            myChatActivityListViewAdapter.notifyDataSetChanged();
            //Todo: Set the topic according to the person you are chatting with
            mqttService.sendMessage("all/"+recipientNR, myMessage.toString());

            db = myDb.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COL_1,name);
            values.put(MySQLiteHelper.COL_3,senderNr);
            values.put(MySQLiteHelper.COL_4,recipientNR);
            values.put(MySQLiteHelper.COL_5,message);
            long result = db.insert(MySQLiteHelper.TABLE, null, values);
           // boolean isInserted =  myDb.insertData(message);

//            if(result != -1) {
//               Toast.makeText(ChatActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
//            }else{
//               Toast.makeText(ChatActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();
//            }
           // myDb.updateTopic(recipientNR,"teeeest";
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

            recipientNR = (String)getIntent().getSerializableExtra("phoneNumber");

            //Start Service
            Intent startServiceIntent = new Intent(ChatActivity.this, MyMqttService.class);
            startService(startServiceIntent);

            // Step 1: Creates New Chat by sending a hidden message to:
            // topic: “receiver_number” with palyloud: Sender “sender_nr” ChatTopic “rand_nummer_timestamp”
            ArrayList<UserProfile> profiles = new ArrayList<>(myDb.getProfiles());

            for(int i =0; i < profiles.size();i++){
                if(profiles.get(i).getPhoneNumber().equals(recipientNR)){
                    topic = profiles.get(i).getTopic();
                    break;
                }
            }

            if (topic == null || topic.equals("false")) {
                Random random = new Random();
                int tmp = random.nextInt(9999999) + 10000000;
                // Generate new topic
                topic = "all/" + String.valueOf(tmp);
                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();
                Timestamp currentTimestamp = new Timestamp(now.getTime());
                Message message = new Message("12", currentTimestamp.getNanos(), senderNr, recipientNR, topic);
                mqttService.sendMessage("all/" + recipientNR, message.toString());
                myDb.updateTopic(recipientNR,topic);
            }

            //Todo: Save topic in database
            //Subscribe to newly created topic Todo: Real timestamp as topic
//            mqttService.subscribe("all/" + topic);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        // TODO: Save message into database
//
        if(isWaitingForImage) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(message.getPayload(), 0 , message.getPayload().length);
            image.setImageBitmap(bitmap);

        }
    }

    public void onClickSendImage(View view) {
        Bitmap bitmap = new ImageSaver(getApplicationContext()).setFileName("profile_image.png").setDirectoryName("images").load();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        mqttService.sendMessage("all/pub/trainID/camID/", byteArray, 1);
        isWaitingForImage = true;

    }

    public void onClickPhotoButton(View view) {
        startPickerDialog(0);
    }

    // Start Dialog to chose between Galery and Camera
    public void startPickerDialog(final int requestCode) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        pictureDialog.setItems(R.array.gallery_or_camera,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, 0);
                                break;
                            case 1:
                                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 1);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
                TextView profileHintTextView = (TextView) findViewById(R.id.profileHintTextView);
                Uri targetUri = data.getData();


                if (requestCode == 0) {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    if (bitmap.getHeight() > 800 || bitmap.getWidth() > 800)
                        bitmap = BitmapScaler.scaleBitmap(bitmap);
                }
                else if (requestCode == 1)
                    bitmap = (Bitmap) data.getExtras().get("data");

                startAckDialog();

                new ImageSaver(getApplicationContext()).setFileName("temp.png").setDirectoryName("images").save(bitmap);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startAckDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.ack_dialog, null);
        pictureDialog.setView(view);
        if (bitmap != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.dialog_image);
            imageView.setImageBitmap(bitmap);
        }
        pictureDialog.setTitle("Send this picture?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (bitmap != null) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            String image = "";
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] tmp = stream.toByteArray();
                            image = new String(tmp);
                            Calendar calendar = Calendar.getInstance();
                            Date now = calendar.getTime();
                            Timestamp currentTimestamp = new Timestamp(now.getTime());
                            Message message = new Message("2", currentTimestamp.getNanos(), senderNr, recipientNR, image);
                            mqttService.sendMessage("all/", message.toString());
                            Log.i("pictureSend", message.toString());
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        pictureDialog.show();
    }
}
