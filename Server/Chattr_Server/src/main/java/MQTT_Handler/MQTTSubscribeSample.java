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

public class MQTTSubscribeSample implements MqttCallback {

    public static void main(String[] args) {
       /* JsonNewObjMsg tmp = new JsonNewObjMsg("64627", 2, 50.61897, 13.02953, "SignalHp1(wh).PNG", "DB8814Hp1.jpg",
                "Hauptsignal",
                "Signal Hp 1, " + "<br>Bedeutung: Fahrt.<br>" + "Formsignal: Tageszeichen  "
                        + "<br>Ein Signalflügel – bei zweiflügligen Signalen<br>"
                        + "der obere Flügel – zeigt schräg nach rechts aufwärts.",
                "8856", "2016-06-13", "18:37" ,"Am 07.01.2008 aufgestellt"); */

        // Topic auf das gehört wird!
        String topic = "#";
        int qos = 2;
        String broker = "tcp://iluhotcopvh4gnmu.myfritz.net:1883"; // Hostadresse, für Testzwecke
        // "tcp://91.250.113.207:1883"
        // nur lokal!
        String clientId = "SampleMessageReceiver";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.setCallback(new MQTTSubscribeSample());
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            Thread.sleep(1000);
            sampleClient.subscribe(topic, qos);
            System.out.println("Subscribed" + "\n");
        } catch (Exception me) {
            if (me instanceof MqttException) {
                System.out.println("reason " + ((MqttException) me).getReasonCode());
            }
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
        for (int i = 0; i < 3; i++) {
            MQTTPublishSample test1 = new MQTTPublishSample();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            test1.sendTestMessage(tmp);

        }
    }

    public void connectionLost(Throwable arg0) {
        System.err.println("connection lost");

    }

    public void deliveryComplete(IMqttDeliveryToken arg0) {
        System.err.println("delivery complete");
    }

    /**
     * hier könnte eine automatische sortierung nach topics geschehen, aktuell
     * nur Demonstration des message empfangs
     */
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String tmp = new String(message.getPayload());

        JsonNewObjMsg obj = mapper.readValue(tmp, JsonNewObjMsg.class);

        System.out.println(obj.toString() + "\n");

    }

}