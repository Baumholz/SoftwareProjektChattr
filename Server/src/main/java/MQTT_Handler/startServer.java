package MQTT_Handler;

import Daba.PersonDBImpl;
import Daba.SQLiteJDBC;
import DemoMessageSender.PublishMessage;


import java.util.Calendar;

public class startServer {


    public static void main(String[] args) {

        /*SQLiteJDBC db = new SQLiteJDBC();
        db.openDBConnection();
        db.createTable(); */
        PersonDBImpl pdi = new PersonDBImpl();
        pdi.createPersonTable();
        /*  MQTTSubscribe sub = new MQTTSubscribe();
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
        Message msg = new Message("ServerTestPublisher",currentTimestamp.getNanos(), 015730975250, 015730975250, "Hello World!", "all/testopic");
        PublishMessage pubM = new PublishMessage();
        pubM.sendTestMessage(msg, 2); */

    }
}
