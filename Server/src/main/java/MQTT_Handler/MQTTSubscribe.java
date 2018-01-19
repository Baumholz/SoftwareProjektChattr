package MQTT_Handler;


import Daba.MessageDBImpl;
import Daba.PersonDBImpl;
import Entities.Message;
import Entities.Person;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

/**
 * Startet einen Testlauf zum verschicken und gleichzeitigen Empfangen eines
 * test Objektes. Was diese Klasse noch könnte: Direkt eine neue Instanz von
 * einem Marker erstellen und diesen in die Datenbank eintragen. Aktuell
 * erstellt diese Klasse selbst ein entsprechendes Objekt, welches dann
 * versendet wird über die Publish Klasse und dann hier wieder empfangen wird.
 *
 * @author mkurras
 */

public class MQTTSubscribe extends Thread implements MqttCallback {

    int count = 0;

    incomingMessageHandler msgHandler = new incomingMessageHandler();


    public void run() {
        subscribe();
    }


    public void subscribe() {

        // Topic auf das gehört wird!
        String topic = "all/#";
        int qos = 2;
        String broker = "tcp://7ofie4f20pn09gmt.myfritz.net:1883"; // Hostadresse, für Testzwecke
        String clientId = "ServerMessageReceiver";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.setCallback(new MQTTSubscribe());
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            Thread.sleep(1000);
            sampleClient.subscribe(topic, qos);
            System.out.println("Subscribed" + "\n");
        } catch (Exception me) {
            if (me instanceof MqttException) {

                MQTTErrorFileHandler errFilehandler = new MQTTErrorFileHandler();
                String error = errFilehandler.getErrorCode(((MqttException) me).getReasonCode());
                System.out.println("ERROR: " + error);
            }
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void connectionLost(Throwable arg0) {
        System.out.println(arg0.getMessage());
        arg0.printStackTrace();
        System.err.println("connection lost");

    }

    /**
     * hier kommt die Datenbank anbindung, aktuell
     * nur Demonstration des message Empfangs
     */
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        String tmp = new String(message.getPayload());
        System.out.println("NEW MESSAGE: " + tmp + " Under TOPIC: " + topic);
        JSONObject jsn = new JSONObject(tmp);
        msgHandler.handleMessage(jsn, topic);

/*
        //JSONParser parser = new JSONParser(tmp, Person, true);

        Map<String, String> myMap = new HashMap<String, String>();
        if (count != 0) {
            byte[] mapData = tmp.getBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            myMap = objectMapper.readValue(mapData, HashMap.class);
            messageID = myMap.get("id");
            System.out.println("Map is: " + myMap);
        }
*/
/**
 * If a Profil Arrives --> Save it in the Profil Database.
 */
/*
        if (messageID.equals("10")) {
            Person person = new Person(myMap.get("cellphoneNumber"), myMap.get("status"), myMap.get("sureName"), myMap.get("lastName"), myMap.get("pictureURL"), myMap.get("coverImage"));
            pdi.insert(person);
        }
*/

        /**
         * If a normal message arrives --> Save it in the Message Database.
         */
      /*  if (messageID.equals("1")) {
            Message m1 = new Message(myMap.get("id"), myMap.get("timestamp"), myMap.get("senderNr"), myMap.get("recipientNr"), myMap.get("content"));
            mdi.insert(m1);
        }
        //  ObjectMapper objectMapper = new ObjectMapper();
        //   objectMapper.        parser.parse();
        // JSONObject arrivedMsgJSON = (JSONObject) parser.parse(tmp);
        count++;
        System.out.println("Count " + count);
    */

        /**
         * BYTE ARRAY: {"id":"1","timestamp":2004,"senderNr":"","recipientNr":"22222","content":"h Hg hj"}
         NEW MESSAGE: {"id":"1","timestamp":2004,"senderNr":"","recipientNr":"22222","content":"h Hg hj"} Under TOPIC: all/pub/trainID/camID/
         BYTE ARRAY: {"id":"12","timestamp":-1,"senderNr":"015774738436","recipientNr":"466464","content":"11483420"}
         NEW MESSAGE: {"id":"12","timestamp":-1,"senderNr":"015774738436","recipientNr":"466464","content":"11483420"} Under TOPIC: all/466464
         BYTE ARRAY: {"id":"12","timestamp":-1,"senderNr":"015774738436","recipientNr":"645454","content":"10931843"}
         NEW MESSAGE: {"id":"12","timestamp":-1,"senderNr":"015774738436","recipientNr":"645454","content":"10931843"} Under TOPIC: all/645454
         BYTE ARRAY: {"id":"12","timestamp":-1,"senderNr":"7","recipientNr":"723","content":"10844484"}
         NEW MESSAGE: {"id":"12","timestamp":-1,"senderNr":"7","recipientNr":"723","content":"10844484"} Under TOPIC: all/723
         BYTE ARRAY: {"id":"12","timestamp":-1,"senderNr":"","recipientNr":"22222","content":"13804482"}
         NEW MESSAGE: {"id":"12","timestamp":-1,"senderNr":"","recipientNr":"22222","content":"13804482"} Under TOPIC: all/22222

         */
    }


    public void deliveryComplete(IMqttDeliveryToken arg0) {
        System.err.println("delivery complete");
    }


}