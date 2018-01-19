package Entities;

import DemoMessageSender.PublishMessage;
import org.json.JSONException;
import org.json.JSONObject;


public class Message {


    private String id;
    private String timestamp;
    private String senderNr;
    private String recipientNr;
    private String content;
    private String topic = null;
    PublishMessage dm;
    JSONObject message;

    public String getRecipientNr() {
        return recipientNr;
    }

    /**
     * @return
     */
    public String getTopic() {
            return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public void wrapMessage() {

        message = new JSONObject();
        try {
            message.put("id" , id);
            message.put("timestamp" , timestamp);
            message.put("senderNr" , senderNr);
            message.put("recipientNr" , recipientNr);
            message.put("content" , content);
            message.put("topic", topic);
            System.out.println(message.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Message(String id, String timestamp, String senderNr, String recipientNr, String content) {

        this.id = id;
        this.timestamp = timestamp;
        this.senderNr = senderNr;
        this.recipientNr = recipientNr;
        this.content = content;
    }
    public Message (){

    }


    public void sendMessage(String topic) {
        dm = new PublishMessage();
        wrapMessage();
        dm.run(message.toString(), topic,2);
      //  dm.sendTestMessage(message.toString(), getTopic(),2);


    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String gettimestamp() {
        return timestamp;
    }

    public void settimestamp(String timestamp) {
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
