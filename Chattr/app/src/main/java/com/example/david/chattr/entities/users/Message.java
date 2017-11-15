package com.example.david.chattr.entities.users;

import java.util.Date;

/**
 * Created by manu on 17.11.2017.
 */

public class Message <T> {
    // i gave Message a String to test it. I know its wrong so we can delete it later
    protected String senderId;
    protected String recipientID;
    protected Date timeSent;
    protected boolean isQueued;
    protected Date timeReceived;

    protected String text;

    public String getText() {
        return text;
    }

    public Message(String senderId, String recipientID, Date timeSent, boolean isQueued, Date timeReceived, String text) {
        this.senderId = senderId;
        this.recipientID = recipientID;
        this.timeSent = timeSent;
        this.isQueued = isQueued;
        this.timeReceived = timeReceived;

        this.text = text;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public boolean isQueued() {
        return isQueued;
    }

    public void setQueued(boolean queued) {
        isQueued = queued;
    }

    public Date getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(Date timeReceived) {
        this.timeReceived = timeReceived;
    }
}
