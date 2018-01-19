package com.example.david.chattr.messaging;

/**
 * Created by Micha on 19.01.2018.
 */

        import android.content.Context;
        import android.content.SharedPreferences;

        import org.eclipse.paho.client.mqttv3.MqttClient;
        import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
        import org.eclipse.paho.client.mqttv3.MqttException;
        import org.eclipse.paho.client.mqttv3.MqttMessage;
        import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * @author mkurras
 */
public class PublishMessage extends Thread{


    public void run(String message, String topic, int qos, String clientID) {
        sendTestMessage(message, topic, qos, clientID);

    }


    /**
     * Das Objekt wird beim Ausführen der Methode übergeben.
     * qos --> message get delivered at most once (0)
     * At least once (1)
     * Exactly once (2)
     * @param tmp
     * @param qos
     * @return
     */
    public boolean sendTestMessage(String tmp, String topic, int qos, String clientID) {


        String broker = "tcp://7ofie4f20pn09gmt.myfritz.net:1883"; //91.250.113.207
        String clientId = clientID;

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            //  String msg = mapper.writeValueAsString(tmp);
            String msg = tmp;
            System.out.println("Publishing message: " + msg);

            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setRetained(true);
            message.setQos(qos); // Sets the quality of service for this message
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected" + "\n");
            return true;
        } catch (MqttException me) {
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
        return false;
    }



}

