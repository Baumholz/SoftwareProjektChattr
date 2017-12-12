package MQTT_Handler;

import DemoMessageSender.PublishMessage;

import java.sql.Timestamp;
import java.util.Calendar;

public class startServer {


    public static void main(String[] args) {

        MQTTSubscribe sub = new MQTTSubscribe();
        sub.subscribe();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        System.out.println (currentTimestamp.toString());
        Message msg = new Message("ServerTestPublisher",currentTimestamp.getNanos(), "015730975250", "015730975250", "Hello World!", "all/testopic");
        PublishMessage pubM = new PublishMessage();
        pubM.sendTestMessage(msg, 2);

    }
}
