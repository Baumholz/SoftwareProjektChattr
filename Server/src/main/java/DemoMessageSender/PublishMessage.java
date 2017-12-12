package DemoMessageSender;

import MQTT_Handler.MQTTErrorFileHandler;
import MQTT_Handler.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * Verschickt eine Testnachricht bzw ein Testjsonobjekt um das verschicken einer Naxchricht Serverseitig zu simulieren.
 *
 * @author mkurras
 *
 */
public class PublishMessage {



    /**
     * Das Objekt wird beim Ausführen der Methode übergeben.
     * qos --> message get delivered at most once (0)
     * At least once (1)
     * Exactly once (2)
     * @param tmp
     * @param qos
     * @return
     */
        public boolean sendTestMessage(Message tmp, int qos) {

            //String topic = "/receiver";
            ObjectMapper mapper = new ObjectMapper();

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
                sampleClient.publish(tmp.getTopic(), message);
                System.out.println("Message published");
                sampleClient.disconnect();
                System.out.println("Disconnected" + "\n");
                return true;
            } catch (MqttException me) {
                MQTTErrorFileHandler errFilehandler = new MQTTErrorFileHandler();
                String error = errFilehandler.getErrorCode(me.getReasonCode());
                System.out.println("ERROR: "+error);
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
