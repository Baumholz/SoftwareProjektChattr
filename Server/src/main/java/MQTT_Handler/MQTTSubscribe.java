package MQTT_Handler;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Startet einen Testlauf zum verschicken und gleichzeitigen Empfangen eines
 * test Objektes. Was diese Klasse noch könnte: Direkt eine neue Instanz von
 * einem Marker erstellen und diesen in die Datenbank eintragen. Aktuell
 * erstellt diese Klasse selbst ein entsprechendes Objekt, welches dann
 * versendet wird über die Publish Klasse und dann hier wieder empfangen wird.
 *
 * @author mkurras
 *
 */

public class MQTTSubscribe implements MqttCallback {


    public void subscribe() {

        // Topic auf das gehört wird!
        String topic = "#";
        int qos = 2;
        String broker = "tcp://iluhotcopvh4gnmu.myfritz.net:1883"; // Hostadresse, für Testzwecke
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
                System.out.println("ERROR: "+error);
            }
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void connectionLost(Throwable arg0) {
        System.err.println("connection lost");

    }

    /**
     * hier könnte eine automatische sortierung nach topics geschehen, aktuell
     * nur Demonstration des message empfangs
     */
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String tmp = new String(message.getPayload());
        Message obj = mapper.readValue(tmp, Message.class);

        System.out.println(obj.toString() + "\n");

    }


    public void deliveryComplete(IMqttDeliveryToken arg0) {
        System.err.println("delivery complete");
    }



}