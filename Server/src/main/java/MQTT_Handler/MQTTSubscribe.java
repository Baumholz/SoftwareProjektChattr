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
    PersonDBImpl pdi = new PersonDBImpl();
    MessageDBImpl mdi = new MessageDBImpl();


    public void run() {
        mdi.createMessageTable();
        System.out.println("MessageDatabase initiated");
        pdi.createPersonTable();
        System.out.println("Person initiated");
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
        String messageID ="";
        //ObjectMapper mapper = new ObjectMapper();
        String tmp = new String(message.getPayload());
        //  Message obj = mapper.readValue(tmp, Message.class);
        // System.out.println(obj.toString() + "\n");
        System.out.println("NEW MESSAGE: " + tmp);

        //JSONParser parser = new JSONParser(tmp, Person, true);

        Map<String, String> myMap = new HashMap<String, String>();
        if (count != 0) {
            byte[] mapData = tmp.getBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            myMap = objectMapper.readValue(mapData, HashMap.class);
            messageID = myMap.get("id");
            System.out.println("Map is: " + myMap);
        }

/**
 * If a Profil Arrives --> Save it in the Profil Database.
 */
        if (messageID.equals("10")) {
            Person person = new Person(myMap.get("cellphoneNumber"), myMap.get("status"), myMap.get("sureName"), myMap.get("lastName"), myMap.get("pictureURL"), myMap.get("coverImage"));
            pdi.insert(person);
        }


        /**
         * If a normal message arrives --> Save it in the Message Database.
         */
        if (messageID.equals("1")) {
            Message m1 = new Message(myMap.get("id"), myMap.get("timestampSender"), myMap.get("senderNr"), myMap.get("recipientNr"), myMap.get("content"));
            mdi.insert(m1);
        }
        //  ObjectMapper objectMapper = new ObjectMapper();
        //   objectMapper.        parser.parse();
        // JSONObject arrivedMsgJSON = (JSONObject) parser.parse(tmp);
        count++;
        System.out.println("Count " + count);
    }


    public void deliveryComplete(IMqttDeliveryToken arg0) {
        System.err.println("delivery complete");
    }


}