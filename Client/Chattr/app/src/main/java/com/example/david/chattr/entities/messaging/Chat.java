package com.example.david.chattr.entities.messaging;

import com.example.david.chattr.entities.users.UserProfile;

import java.util.ArrayList;

/**
 * Created by manu on 17.11.2017.
 */

public class Chat {

    ArrayList<Message> messageList;
    ArrayList<UserProfile> recipients;

    public Chat(ArrayList<Message> messageList, ArrayList<UserProfile> recipients) {
        this.messageList = messageList;
        this.recipients = recipients;
    }
}
