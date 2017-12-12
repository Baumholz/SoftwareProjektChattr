package MQTT_Handler;

import DemoMessageSender.PublishMessage;
import org.json.JSONException;
import org.json.JSONObject;


public class Message {


    protected String id;
    protected int timestamp;
    protected String senderNr;
    protected String recipientNr;

    public String getRecipientNr() {
        return recipientNr;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    protected String content;
    protected String topic;
    PublishMessage dm;
    JSONObject message;

    @Override
    public String toString() {

        message = new JSONObject();
        try {
            message.put("id" , id);
            message.put("timestamp" , timestamp);
            message.put("senderNr" , senderNr);
            message.put("recipientNr" , recipientNr);
            message.put("content" , content);
            return message.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message(String id, int timestamp, String senderNr, String recipientNr, String content, String topic) {

        this.id = id;
        this.timestamp = timestamp;
        this.senderNr = senderNr;
        this.recipientNr = recipientNr;
        this.content = content;
        this.topic = topic;
    }

    public void sendMessage(){
       dm = new PublishMessage();
       dm.sendTestMessage(this, 2);


    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
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
