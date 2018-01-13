package MQTT_Handler;

import Daba.PersonDBImpl;
import Daba.SQLiteJDBC;
import DemoMessageSender.PublishMessage;
import Entities.Message;
import Entities.Person;


import java.util.Calendar;

public class startServer {


    public static void main(String[] args) {

        //Auf Nachrichten hoeren
        MQTTSubscribe sub = new MQTTSubscribe();
        System.out.println("status");
        sub.subscribe();
        System.out.println("nachSub");

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




    }
}
