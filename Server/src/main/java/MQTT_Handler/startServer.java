package MQTT_Handler;

import Daba.MessageDBImpl;
import Daba.PersonDBImpl;
import DemoMessageSender.PublishMessage;
import Entities.Message;
import Entities.Person;
import org.json.JSONObject;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class startServer {


    public static void main(String[] args) {

        /**
         * Start to subscribe.
         */
        MQTTSubscribe sub = new MQTTSubscribe();
        sub.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         *wait that our Subscribe Thread is finished and then send TestMessage
         */

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp;
        System.out.println("...\n\n\n\nFuellen der DB mit Testnachrichten...");
        /**
         * Füllen der DB mit Testnachrichten zwischen zwei personen
         */

        for (int i = 0; i < 3; i++) {

            now = calendar.getTime();
            currentTimestamp = new java.sql.Timestamp(now.getTime());
            //    public Message(String id, int timestamp, String senderNr, String recipientNr, String content) {
            Message msg = new Message("1", currentTimestamp.getNanos(), "015730975250", "015730975251", "Hello World! FROM: 015730975250!");
            msg.setTopic("all/testtopic");
            msg.sendMessage("all/testtopic");

            now = calendar.getTime();
            currentTimestamp = new java.sql.Timestamp(now.getTime());
            //    public Message(String id, int timestamp, String senderNr, String recipientNr, String content) {
            Message msg2 = new Message("1", currentTimestamp.getNanos(), "015730975251", "015730975250", "Hello World! FROM: 015730975251!");
            msg2.setTopic("all/testtopic");
            msg2.sendMessage("all/testtopic");
        }

        /**
         * Give the other thread some time to work.
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /*SQLiteJDBC db = new SQLiteJDBC();
        db.openDBConnection();
        db.createTable(); */
        System.out.println("...\n\n\n\nTesten des Profilempfangs...");
        /**
         * Test des Profil empfangs! indem ein Profil verschickt wird
         */

        //public Person(String phoneNumber, String status, String firstName, String name, String profilePicture, String coverImage) {
        Person person = new Person("015730975250", ":)", "Michael", "Kurras", "prfilIMAGEXYZ", "coverIMAGEXYZ");
        person.setId("10");
        PublishMessage pubm = new PublishMessage();
        pubm.run(person.personToJSON().toString(), "all/testtopic", 2);
        Person person2 = new Person("182983928329", ":) I'm the best!", "David", "Hierholz", "prfilIMAGEXYZ", "coverIMAGEXYZ");
        person2.setId("10");
        pubm.sendTestMessage(person2.personToJSON().toString(), "all/testtopic", 2);
        Person person3 = new Person("2274824728427", "HELLO THERE", "Manuel", "Schneckenburger", "prfilIMAGEXYZ", "coverIMAGEXYZ");
        person3.setId("10");
        pubm.sendTestMessage(person3.personToJSON().toString(), "all/testtopic", 2);


        /**
         * Give the other thread some time to work.
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         *  Test des Profil Verschickens.
         */
        System.out.println("...\n\n\n\nTest des Profil verschickens...");
        currentTimestamp = new java.sql.Timestamp(now.getTime());
        Message msg3 = new Message("13", currentTimestamp.getNanos(), "015730975250", "015730975251", "015730975250");
        msg3.setTopic("all/testtopic");
        msg3.sendMessage("all/testtopic");


        /**
         * Give the other thread some time to work.
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * Chat History anfordern
         */
        System.out.println("...\n\n\n\nTest der Chat History Funktion");
        currentTimestamp = new java.sql.Timestamp(now.getTime());
        Message msg5 = new Message("11", currentTimestamp.getNanos(), "015730975250", "015730975251", "XYZ");
        msg5.setTopic("all/testtopic");
        msg5.sendMessage("all/testtopic");


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
        //String id, String timestamp, String senderNr, String recipientNr, String content, String topic
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