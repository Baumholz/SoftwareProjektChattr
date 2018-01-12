package com.example.david.chattr.messaging;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by david on 29.12.17.
 */

public interface MessageArrivedListener {

    public void messageArrived(String topic, MqttMessage message);

}
