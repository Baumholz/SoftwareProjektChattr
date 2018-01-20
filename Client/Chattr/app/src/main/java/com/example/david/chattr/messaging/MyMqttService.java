package com.example.david.chattr.messaging;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.david.chattr.utils.MySQLiteHelper;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by david on 22.11.17.
 */

public class MyMqttService extends Service implements MqttCallback{

    private final MyLocalBinder myBinder = new MyLocalBinder();

    private final static String Broker = "tcp://7ofie4f20pn09gmt.myfritz.net";
    private final static String TAG = "MyMqttService";
    private final static String TOPICALL = "all/";
//    private final static String Broker = "tcp://broker.hivemq.com:1883";
    private final static int Quos = 2;

    private MqttAndroidClient client;

    //    private String topic;
    private String clientId;
    private String phoneNumber;
    private String topic;
    private boolean isSubscribed;

    // Returns this instance of MyMqttService, so that other clients/Activities can call its methods
    public class MyLocalBinder extends Binder {

        private MessageArrivedListener listener;

        public MyMqttService getService() {
            return MyMqttService.this;
        }

        public void setMessageArrivedListener(MessageArrivedListener listener) {
            this.listener = listener;
        }

        public void messageArrived(String topic, MqttMessage message) {
            if (listener != null) {
                listener.messageArrived(topic, message);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "default");
        if (phoneNumber.equals("default")) {
            clientId = intent.getStringExtra("clientId");
        } else {
            clientId = phoneNumber;
        }
        try {
            connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return Service.START_STICKY;
    }

    public void connect() throws MqttException {

        client = new MqttAndroidClient(this.getApplicationContext(), Broker, clientId);

        IMqttToken token = client.connect();
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // We are connected
                Log.d(TAG, "\nonSuccess\n");
//                Toast.makeText(MyMqttService.this, "Connected with Broker", Toast.LENGTH_SHORT).show();
                client.setCallback(MyMqttService.this);
                if (!isSubscribed && !phoneNumber.equals("default")) {
                    subscribe();
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                // Something went wrong e.g. connection timeout or firewall problems
                Log.d(TAG, "onFailure");
//                Toast.makeText(MyMqttService.this, "Not connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendMessage(String topic, String message) {
        try {
            MqttMessage mMessage = new MqttMessage(message.getBytes());
            mMessage.setRetained(true);
            client.publish(topic, mMessage);
            Log.d(TAG, "\nMessage send\n");
//            Toast.makeText(MyMqttService.this, "Message send on topic: " + topic + "\n" + message, Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String topic, byte[] message, int a) {
        try {
            MqttMessage mMessage = new MqttMessage(message);
            mMessage.setRetained(true);
            client.publish(topic, mMessage);
            Log.d(TAG, "\nMessage send\n");
//            Toast.makeText(MyMqttService.this, "Message send on topic: " + topic + "\n" + message, Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe() {
        topic = TOPICALL + phoneNumber;
        try {
            IMqttToken token = client.subscribe(topic, Quos);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "\nSubscribed successfully on topic: " + topic + "\n");
                    isSubscribed = true;
//                    Toast.makeText(MyMqttService.this, "Subscribed successfully on topic: " + topic, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "\nFailed to subscribe on topic: " + topic + "\n");
                    isSubscribed = false;
//                    Toast.makeText(MyMqttService.this, "Failed to subscribe on topic: " + topic, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String mMessage = message.toString();
        Log.d(TAG, "\nMessage arrived on topic: " + topic + "\n" + message + "\n");
//        Toast.makeText(MyMqttService.this, "Message arrived: " + message, Toast.LENGTH_SHORT).show();


        JSONObject json = new JSONObject(mMessage);
        String id = json.getString("id");
        String content = json.getString("content");

        /**
         *
         * Different messages are send from the server to the chattr app.
         * They are differentiated by the id.
         * id == 1: Text messages
         * id == 2: Pictures
         * id == 15: Profile and Cover Picture of Contact
         *
         * **/
        switch (id) {
            case "1":
                makeNotification(content);
                saveMessagetoDB(mMessage);
                break;
            case "2":
                makeNotification("New Picture :)");
                saveMessagetoDB(mMessage);
                break;
            case "15":
                //Todo: Get the Profile/Cover Picture out of the message and save it the according contact in the DB
                break;
            default:
                break;
        }
        myBinder.messageArrived(topic, message);
    }

    private void makeNotification(String content) {
        MessageNotifier notifier = new MessageNotifier(this);
        notifier.showOrUpdateNotification(content);
    }

    private void saveMessagetoDB(String mMessage) throws JSONException {
        JSONObject json = new JSONObject(mMessage);
        String senderNr = json.getString("senderNr");
        String recipientNR = json.getString("recipientNr");
        String content = json.getString("content");
        String timestamp = json.getString("timestamp");

        String tmstmp = ""; //Tmp Timestamp aus db fuer vergleich.


        MySQLiteHelper myDb = new MySQLiteHelper(this);
        SQLiteDatabase dbr;
        dbr = myDb.getReadableDatabase();
        String query = "select * from " + MySQLiteHelper.TABLE + " where " + MySQLiteHelper.SENDERnr +
                "='" + senderNr + "' AND " + MySQLiteHelper.RECIPIENtnr + "='" + recipientNR + "' AND " + MySQLiteHelper.MsgCONTENT +
        "='" + content + "' AND " + MySQLiteHelper.TIMESTAMP + "='" + timestamp + "';";

        Cursor c = dbr.rawQuery(query, null);
//        Cursor c = db.query(MySQLiteHelper.TABLE, projection, selection, selectionArgs,null,null,null);
        while (c.moveToNext()) {
             tmstmp = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.TIMESTAMP));
        }

        if (tmstmp == ""){

            SQLiteDatabase db = myDb.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.SENDERnr,senderNr);
            values.put(MySQLiteHelper.RECIPIENtnr,recipientNR);
            values.put(MySQLiteHelper.MsgCONTENT,content);
            values.put(MySQLiteHelper.TIMESTAMP,timestamp);
            long result = db.insert(MySQLiteHelper.TABLE, null, values);
            if(result != -1) {
//           Toast.makeText(this, "Data Inserted", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Data Inserted");
            }else{
//           Toast.makeText(this, "Data not Inserted", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Data not Inserted");
            }


        }








    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (client != null) {
            try {
                client.unsubscribe(topic);
                Log.d(TAG, "\nUnsubscribed on topic: " + topic + "\n");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        isSubscribed = false;
    }
}
