package MQTT_Handler;

import Daba.MessageDBImpl;
import Daba.PersonDBImpl;
import DemoMessageSender.PublishMessage;
import Entities.Message;
import Entities.Person;
import org.json.JSONObject;

public class incomingMessageHandler {

    PersonDBImpl pdi = new PersonDBImpl();
    MessageDBImpl mdi = new MessageDBImpl();
    String messageID = "";


    public incomingMessageHandler() {

        mdi.createMessageTable();
        System.out.println("MessageDatabase initiated");
        pdi.createPersonTable();
        System.out.println("PersonDatabase initiated");

    }


    public void handleMessage(JSONObject jsn, String topic) {

        messageID = jsn.getString("id");

        if (messageID.equals("1")) {
            normalMessage(jsn, topic);

        }
        if (messageID.equals("2")) {
        System.out.println("Picture has been send between(Sender): "+ jsn.getString("senderNr") + " and (Receiver): " + jsn.getString("recipientNr"));
        normalMessage(jsn, topic);
        }
        if (messageID.equals("10")) {
            saveProfil(jsn);
        }
        if (messageID.equals("11")) {
            System.out.println("Collecting Chat History.. from: "+ jsn.getString("senderNr") + " and (Receiver): " + jsn.getString("recipientNr"));
            sendChatHistory(jsn);

        }
        if (messageID.equals("12")) {

            System.out.println("Establishing new Chat Activity between(Sender): " + jsn.getString("senderNr") + " and (Receiver) " + jsn.getString("recipientNr") + " under the new topic: " + jsn.getString("content"));

        }
        if (messageID.equals("13")) {
            sendProfil(jsn);
        }

    }


    /**
     * Methode welche alle normale Nachrichten in die Datenbank einträgt und damit "behandelt".
     * AKTUELL werden auch Bilder als normale Nachrichten eingetragen!!!!!
     * @param jsn
     * @param topic
     */
    private void normalMessage(JSONObject jsn, String topic) {

        Message m1 = new Message(jsn.getString("id"), jsn.getString("timestamp"), jsn.getString("senderNr"), jsn.getString("recipientNr"), jsn.getString("content"));
        m1.setTopic(topic);
        mdi.insert(m1);
        System.out.println("Message inserted to database!");


    }

    private void saveProfil(JSONObject jsn) {

        Person person = new Person(jsn.getString("phoneNumber"), jsn.getString("status"), jsn.getString("firstName"), jsn.getString("name"), jsn.getString("profilePicture"), jsn.getString("coverImage"));
        pdi.insert(person);
        System.out.println("New Profil inserted to database!");
    }

    private void sendProfil(JSONObject jsn) {
        //Message m1 = new Message(jsn.getString("id"), jsn.getInt("timestamp"), jsn.getString("senderNr"), jsn.getString("recipientNr"), jsn.getString("content"));
        String phoneNumber = jsn.getString("content"); //Profil welches angefordert wird.
        String receiverNumber =  jsn.getString("senderNr"); //Topic an die die Person geschickt wird.
        Person person = pdi.selectById(phoneNumber);
        person.setId("15"); //Send Profil ID
        System.out.println("Selected Person from database: " +person.toString());
        PublishMessage send = new PublishMessage();
        send.run(person.personToJSON().toString(), receiverNumber, 2);
    }

    private void sendChatHistory(JSONObject jsn){
        mdi.sendChactHistoryBetweenSenderAndReceiver(jsn.getString("senderNr"), jsn.getString("recipientNr"));
    }


}
