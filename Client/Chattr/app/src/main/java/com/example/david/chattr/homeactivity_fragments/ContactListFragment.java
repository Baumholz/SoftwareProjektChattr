package com.example.david.chattr.homeactivity_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.users.UserProfile;

import java.util.ArrayList;

/**
 * Created by david on 15.11.17.
 */

public class ContactListFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_contactlist, container, false);

        UserProfile user1 = new UserProfile("0340442323","none","0","Olaf",R.drawable.hund);
        UserProfile user2 = new UserProfile("0340446364","none","1","Harald",R.drawable.hund2);

        final ArrayList<UserProfile> contacts = new ArrayList<UserProfile>();

        contacts.add(user1);
        contacts.add(user2);

        final ChatListAdapter myContactListAdapter = new ChatListAdapter(contacts);
        final ListView contactListView = (ListView) view.findViewById(R.id.contactList);
        contactListView.setAdapter(myContactListAdapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: Set Intent to Profile View
            }
        });
        
        return view;
    }
}
