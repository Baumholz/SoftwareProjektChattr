package com.example.david.chattr.mqtt_chat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static android.content.ContentValues.TAG;

/**
 * Created by david on 22.11.17.
 */

public class MyMqttService extends Service implements MqttCallback{

    private final IBinder myBinder = new MyLocalBinder();

//    private final static String Broker = "tcp://iluhotcopvh4gnmu.myfritz.net:1883";
    private final static String Broker = "tcp://broker.hivemq.com:1883";
    private final static int Quos = 2;

    MqttAndroidClient client;

//    private String topic;
    private String clientID;
//    private Context context;

    public MyMqttService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        topic = intent.getStringExtra("topic");
        if (clientID != null) {
            clientID = intent.getStringExtra("clientID");
        } else {
            clientID = "SampleMessageReceiver";
        }

        try {
            connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return Service.START_STICKY;
    }

    private void connect() throws MqttException {

        client = new MqttAndroidClient(this.getApplicationContext(), Broker, clientID);

        IMqttToken token = client.connect();
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // We are connected
                Log.d(TAG, "onSuccess");
                Toast.makeText(MyMqttService.this, "Connected", Toast.LENGTH_LONG).show();
                client.setCallback(MyMqttService.this);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                // Something went wrong e.g. connection timeout or firewall problems
                Log.d(TAG, "onFailure");
                Toast.makeText(MyMqttService.this, "Not connected", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendMessage(String topic, String message) {
        try {
            MqttMessage mMessage = new MqttMessage(message.getBytes());
            mMessage.setRetained(true);
            client.publish(topic, mMessage);
            Log.d(TAG, "Message send");
            Toast.makeText(MyMqttService.this, "Message send", Toast.LENGTH_LONG).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            IMqttToken token = client.subscribe(topic, Quos);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Subscribed successfully");
                    Toast.makeText(MyMqttService.this, "Subscribed successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failed to subscribe");
                    Toast.makeText(MyMqttService.this, "Failed to subscribe", Toast.LENGTH_LONG).show();
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
        String mMessage = message.getPayload().toString();
        Log.d(TAG, "Message arrived");
        Toast.makeText(MyMqttService.this, "Message arrived", Toast.LENGTH_LONG).show();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }




    public class MyLocalBinder extends Binder {
        MyMqttService getService() {
            return MyMqttService.this;
        }
    }
}
