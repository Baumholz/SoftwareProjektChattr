package MQTT_Handler;

import Daba.MessageDBImpl;
import Daba.PersonDBImpl;
import Daba.SQLiteJDBC;
import DemoMessageSender.PublishMessage;
import Entities.Message;
import Entities.Person;
import org.json.JSONObject;


import java.util.Calendar;

public class startServer {


    public static void main(String[] args) {

        //Auf Nachrichten hoeren

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
         Message msg = new Message("ServerTestPublisher",Integer.toString(currentTimestamp.getNanos()),"015730975250", "015730975250", "Hello World!", "all/testopic");
         msg.sendMessage();



        /*SQLiteJDBC db = new SQLiteJDBC();
        db.openDBConnection();
        db.createTable(); */
        /**
         * Test der Person DB
         */

        PersonDBImpl pdi = new PersonDBImpl();
        pdi.createPersonTable();
        //String id, String cellphoneNumber, String status, String sureName, String lastName,String pictureURL
        Person person = new Person("01520975250", ":)","Michael","Kurras", "c/data/pictures/profilepicture");
        pdi.insert(person);
        Person person2 = new Person("182983928329", ":)","David","Hierholz", "c/data/pictures/profilepicture");
        pdi.insert(person2);
        Person person3 = new Person("2274824728427", ":)","Manuel","Schneckenburger", "c/data/pictures/profilepicture");
        pdi.insert(person3);
       Person p2 = pdi.selectById("182983928329");
       System.out.println(p2.toString());


        /**Test der Message DB
         */

        MessageDBImpl mdi = new MessageDBImpl();
        mdi.createMessageTable();
        //String id, String timestampSender, String senderNr, String recipientNr, String content, String topic
        Message m1 = new Message("2788273872382","22323232","015730975250","01573929829","Hallo WELT!");
        mdi.insert(m1);
        Message m2 = new Message("23323","4343","01523275250","01573929829","Hallo WELT2!");
        mdi.insert(m2);
        Message m3 = new Message("2523q2634664","3533","01573434433400","01573929829","Hallo WELT3!");
        mdi.insert(m3);

        JSONObject tmpJsonObj;
        tmpJsonObj = mdi.getAllFromTopic("all/01573929829");
       System.out.println(tmpJsonObj.toString());


    }
}