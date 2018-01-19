package com.example.david.chattr.entities.messaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by manu on 17.11.2017.
 */

public class Message {

    protected String id;
    protected int timestamp;
    protected String senderNr;
    protected String recipientNr;
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

    public Message(String id, int timestamp, String senderNr, String recipientNr, String content) {

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

    public String getRecipientNr() {
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
