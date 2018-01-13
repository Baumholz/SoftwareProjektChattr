package Entities;

import DemoMessageSender.PublishMessage;
import org.json.JSONException;
import org.json.JSONObject;


public class Message {


    private String id;
    private String timestampSender;
    private String senderNr;
    private String recipientNr;
    private String content;
    private String topic = null;
    PublishMessage dm;
    JSONObject message;

    public String getRecipientNr() {
        return recipientNr;
    }

    public String getTopic() {

        topic = "all/"+recipientNr;
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public void wrapMessage() {

        message = new JSONObject();
        try {
            message.put("id" , id);
            message.put("timestampSender" , timestampSender);
            message.put("senderNr" , senderNr);
            message.put("recipientNr" , recipientNr);
            message.put("content" , content);
            message.put("topic", topic);
            System.out.println(message.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Message(String id, String timestampSender, String senderNr, String recipientNr, String content, String topic) {

        this.id = id;
        this.timestampSender = timestampSender;
        this.senderNr = senderNr;
        this.recipientNr = recipientNr;
        this.content = content;
        this.topic = topic;
    }

    public Message(String id, String timestampSender, String senderNr, String recipientNr, String content) {

        this.id = id;
        this.timestampSender = timestampSender;
        this.senderNr = senderNr;
        this.recipientNr = recipientNr;
        this.content = content;
    }
    public Message (){

    }

    public void sendMessage() {
        dm = new PublishMessage();
        wrapMessage();
        dm.sendTestMessage(message.toString(), getTopic(),2);


    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestampSender() {
        return timestampSender;
    }

    public void settimestampSender(String timestampSender) {
        this.timestampSender = timestampSender;
    }

    public String getSenderNr() {
        return senderNr;
    }

    public void setSenderNr(String senderNr) {
        this.senderNr = senderNr;
    }

    public String isRecipientNr() {
        return recipientNr;
    }

    public void setRecipientNr(String recipientNr) {
        this.recipientNr = recipientNr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
