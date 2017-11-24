package com.example.david.chattr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.david.chattr.entities.users.UserProfile;

import java.util.ArrayList;


public class ChatListAdapter extends BaseAdapter {

    private ArrayList<UserProfile> recipients ;

    public ChatListAdapter(ArrayList<UserProfile> recipients) {
        this.recipients = recipients;
    }

    @Override
    public int getCount() {
        return recipients.size();
    }

    @Override
    public Object getItem(int i) {
        return recipients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();
        UserProfile user = recipients.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_list_entry,null,false); //Use our Layout
        }

        ImageView profilePicture = view.findViewById(R.id.profilePicture);

        TextView profileName = view.findViewById(R.id.profilName);


        profilePicture.setImageResource(user.getProfilePicture());
        profileName.setText(user.getName());

        return view;
    }
}
