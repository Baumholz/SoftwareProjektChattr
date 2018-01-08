package com.example.david.chattr.homeactivity_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.mqtt_chat.ChatActivity;
import com.example.david.chattr.mqtt_chat.MySQLiteHelper;
import com.example.david.chattr.new_contact.NewManuallContactActivity;

import java.util.ArrayList;

/**
 * Created by david on 15.11.17.
 */

public class ChatListFragment extends Fragment {
    private MySQLiteHelper myDbProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myDbProfile = new MySQLiteHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_chatlist, container, false);

        //TODO: I commented the argument out because of the error: no such table: userProfile --> MySQLiteHelper line 82
        final ArrayList<UserProfile> recipients = new ArrayList<UserProfile>(/*myDbProfile.getProfiles()*/);

        UserProfile user1 = new UserProfile("0340442323", "none", "0", "Olaf", R.drawable.hund);
        UserProfile user2 = new UserProfile("0340446364", "none", "1", "Harald", R.drawable.hund2);

        // Read DB
        recipients.add(user1);
        recipients.add(user2);

        Log.e("Number",recipients.get(1).getPhoneNumber());

        final ChatListAdapter myChatListAdapter = new ChatListAdapter(recipients);
        final ListView chatListView = (ListView) view.findViewById(R.id.chatListView);
        chatListView.setAdapter(myChatListAdapter);

        //David i changed something her added an intent to the chatActivity #Manu
        //ClickListener for the listView /Main view
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = chatListView.getSelectedItemPosition();
                //this.getActivity needed cause of Fragments
                Intent intent = new Intent(ChatListFragment.this.getActivity(), ChatActivity.class);
                //intent.putExtra("position",pos);
                intent.putExtra("picture", recipients.get(i).getProfilePicture());
                intent.putExtra("name", recipients.get(i).getName());
                intent.putExtra("phoneNumber", recipients.get(i).getPhoneNumber());
                startActivity(intent);
            }
        });

        return view;
    }
}
