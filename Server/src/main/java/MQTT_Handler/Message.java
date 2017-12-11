package MQTT_Handler;

import org.json.JSONException;
import org.json.JSONObject;

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
public class Message {


    protected String id;
    protected int timestamp;
    protected String senderNr;
    protected boolean recipientNr;
    protected String content;

    @Override
    public String toString() {

        JSONObject message = new JSONObject();
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

    public Message(String id, int timestamp, String senderNr, boolean recipientNr, String content) {

        this.id = id;
        this.timestamp = timestamp;
        this.senderNr = senderNr;
        this.recipientNr = recipientNr;
        this.content = content;
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

    public boolean isRecipientNr() {
        return recipientNr;
    }

    public void setRecipientNr(boolean recipientNr) {
        this.recipientNr = recipientNr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
