package MQTT_Handler;

import Daba.MessageDBImpl;
import Daba.PersonDBImpl;
import DemoMessageSender.PublishMessage;
import Entities.Message;
import Entities.Person;
import org.json.JSONObject;


import java.util.Calendar;

public class startServer {


    public static void main(String[] args) {

        /**
         * Start to subscribe.
         */
        MQTTSubscribe sub = new MQTTSubscribe();
        sub.run();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         *wait that our Subscribe Thread is finished and then send TestMessage
         */
        /*
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        Message msg = new Message("ServerTestPublisher", Integer.toString(currentTimestamp.getNanos()), "015730975250", "015730975250", "Hello World!", "all/testtopic");
        msg.sendMessage();
/*


        /*SQLiteJDBC db = new SQLiteJDBC();
        db.openDBConnection();
        db.createTable(); */
        /**
         * Test der Person DB
         */
        /*
        PersonDBImpl pdi = new PersonDBImpl();
        pdi.createPersonTable();
        //String id, String cellphoneNumber, String status, String sureName, String lastName,String pictureURL
        Person person = new Person("01520975250", ":)", "Michael", "Kurras", "c/data/pictures/profilepicture");
        pdi.insert(person);
        Person person2 = new Person("182983928329", ":)", "David", "Hierholz", "c/data/pictures/profilepicture");
        pdi.insert(person2);
        Person person3 = new Person("2274824728427", ":)", "Manuel", "Schneckenburger", "c/data/pictures/profilepicture");
        pdi.insert(person3);
        Person p2 = pdi.selectById("182983928329");
        System.out.println(p2.toString());
/*

        /**
         *  TestProfil Verschicken, um Eingang in DB zu testen.
         */

        //Person(String cellphoneNumber, String status, String sureName, String lastName, String pictureURL)
        /*int i = 0;
        while(i < 2000) {

            Person person4 = new Person(Integer.toString(i), "meinStatus!", "Manuella", "Stratz", "c/data/pictures/profilepicture", "c/data/pictures/profilepicture");
            JSONObject profilJson = person4.personToJSON();
            PublishMessage send = new PublishMessage();
            send.run(profilJson.toString(), "all/newProfilToXYZ", 2);


            Message m2 = new Message("1", "4343", "01523275250", "01573929829", "Hallo WELT2!");
            m2.sendMessage();

            i++;
        } */
        /**Test der Message DB
         */
/*
        MessageDBImpl mdi = new MessageDBImpl();
        mdi.createMessageTable();
        //String id, String timestampSender, String senderNr, String recipientNr, String content, String topic
        Message m1 = new Message("2788273872382", "22323232", "015730975250", "01573929829", "Hallo WELT!");
        mdi.insert(m1);
        Message m2 = new Message("23323", "4343", "01523275250", "01573929829", "Hallo WELT2!");
        mdi.insert(m2);
        Message m3 = new Message("2523q2634664", "3533", "01573434433400", "01573929829", "Hallo WELT3!");
        mdi.insert(m3);
        */
/**
 *  Test Chat History extraction
 */
/*
        JSONObject tmpJsonObj;
        tmpJsonObj = mdi.getAllFromTopic("all/01573929829");
        System.out.println("Chat History example: " + tmpJsonObj.toString());
*/

    }
}