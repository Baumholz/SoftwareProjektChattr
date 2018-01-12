package com.example.david.chattr.messaging;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
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
import org.json.JSONObject;

import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * Created by david on 22.11.17.
 */

public class MyMqttService extends Service implements MqttCallback{

    private final MyLocalBinder myBinder = new MyLocalBinder();

    private final static String Broker = "tcp://7ofie4f20pn09gmt.myfritz.net";
//    private final static String Broker = "tcp://broker.hivemq.com:1883";
    private final static int Quos = 2;

    MqttAndroidClient client;

    //    private String topic;
    private String clientId;
//    private Context context;

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
    public int onStartCommand(Intent intent, int flags, int startId) {

//        topic = intent.getStringExtra("topic");

        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phoneNumber", "default");
        if (phoneNumber.equals("default")) {
            clientId = intent.getStringExtra("clientId");
        } else {
            clientId = phoneNumber;
        }

//        if (clientId != null) {
//            clientId = intent.getStringExtra("clientId");
//        } else {
//            clientId = "SampleMessageReceiver";
//        }

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
                Toast.makeText(MyMqttService.this, "Connected with Broker", Toast.LENGTH_SHORT).show();
                client.setCallback(MyMqttService.this);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                // Something went wrong e.g. connection timeout or firewall problems
                Log.d(TAG, "onFailure");
                Toast.makeText(MyMqttService.this, "Not connected", Toast.LENGTH_SHORT).show();
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

    public void subscribe(final String topic) {
        try {
            IMqttToken token = client.subscribe(topic, Quos);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "\nSubscribed successfully on topic: " + topic + "\n");
//                    Toast.makeText(MyMqttService.this, "Subscribed successfully on topic: " + topic, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "\nFailed to subscribe on topic: " + topic + "\n");
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
        //Todo: Find out why messages do not arrive
        String mMessage = message.toString();
        Log.d(TAG, "\nMessage arrived on topic: " + topic + "\n" + message + "\n");
//        Toast.makeText(MyMqttService.this, "Message arrived: " + message, Toast.LENGTH_SHORT).show();

        myBinder.messageArrived(topic, message);

        //TODO: Make notification with real content
        MessageNotifier notifier = new MessageNotifier(this);
        notifier.showOrUpdateNotification(mMessage);
//        Toast.makeText(this, "Arived: "+ mMessage, Toast.LENGTH_SHORT).show();

        JSONObject json = new JSONObject(mMessage);
        String senderNr = json.getString("senderNr");
        String recipientNR = json.getString("recipientNr");
        String content = json.getString("content");
//        String id = json.getString("id");

        MySQLiteHelper myDb = new MySQLiteHelper(this);
        SQLiteDatabase db = myDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_3,senderNr);
        values.put(MySQLiteHelper.COL_4,recipientNR);
        values.put(MySQLiteHelper.COL_5,content);
        long result = db.insert(MySQLiteHelper.TABLE, null, values);

        if(result != -1) {
           Toast.makeText(this, "Data Inserted", Toast.LENGTH_LONG).show();
        }else{
           Toast.makeText(this, "Data not Inserted", Toast.LENGTH_SHORT).show();
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
}
