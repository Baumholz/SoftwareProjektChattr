package com.example.david.chattr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.david.chattr.entities.users.UserProfile;

import java.util.ArrayList;

/**
 * Created by david on 15.11.17.
 */

public class ChatListFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatlist, container, false);

        UserProfile user1 = new UserProfile("0340442323","none","0","Olaf",R.drawable.hund);
        UserProfile user2 = new UserProfile("0340446364","none","1","Harald",R.drawable.hund2);

        ArrayList<UserProfile> recipients = new ArrayList<UserProfile>();

        recipients.add(user1);
        recipients.add(user2);

        ChatListAdapter myChatListAdapter = new ChatListAdapter(recipients);
        ListView chatListView = (ListView) view.findViewById(R.id.chatListView);
        chatListView.setAdapter(myChatListAdapter);

        return view;
    }
}