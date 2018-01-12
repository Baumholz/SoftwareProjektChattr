package com.example.david.chattr.fragments;

import android.content.Intent;
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
import com.example.david.chattr.adapters.ChatListAdapter;
import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.messaging.ChatActivity;
import com.example.david.chattr.utils.MySQLiteHelper;

import java.util.ArrayList;

/**
 * Created by david on 15.11.17.
 */

public class ChatListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MySQLiteHelper myDbProfile = new MySQLiteHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_chatlist, container, false);

        final ArrayList<UserProfile> recipients = new ArrayList<>(myDbProfile.getProfiles());

        final ChatListAdapter myChatListAdapter = new ChatListAdapter(recipients);
        final ListView chatListView = view.findViewById(R.id.chatListView);
        chatListView.setAdapter(myChatListAdapter);

        //David i changed something her added an intent to the chatActivity #Manu
        //ClickListener for the listView /Main view
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChatListFragment.this.getActivity(), ChatActivity.class);
                intent.putExtra("phoneNumber", recipients.get(i).getPhoneNumber());
                startActivity(intent);
            }
        });

        return view;
    }
}
