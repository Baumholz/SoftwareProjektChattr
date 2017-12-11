package MQTT_Handler;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Verschickt eine Testnachricht bzw ein Testobjekt für die Darstellung eines
 * neuen Markers in der Railmapweb. Json als Protokoll und MQTT als Verpackung.
 *
 * @author mkurras
 *
 */
public class MQTTPublish {

    /*
     * Das Objekt wird beim Ausführen der Methode übergeben.
     */
    public boolean sendTestMessage(Message tmp) {

        String topic = "/receiver";
        ObjectMapper mapper = new ObjectMapper();

        int qos = 2; // quality of service
        String broker = "tcp://iluhotcopvh4gnmu.myfritz.net:1883"; //91.250.113.207
        String clientId = "ServerPublish";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            String msg = mapper.writeValueAsString(tmp);
            System.out.println("Publishing message: " + msg);

            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(qos); // Sets the quality of service for this message
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected" + "\n");
            return true;
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        } catch (JsonProcessingException e) {
            System.out.println("Json Exception happend");
            e.printStackTrace();
        }
        return false;
    }

}