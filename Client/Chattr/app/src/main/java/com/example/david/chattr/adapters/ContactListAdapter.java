package com.example.david.chattr.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.users.UserProfile;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactListAdapter extends BaseAdapter {

    private ArrayList<UserProfile> contacts ;

    public ContactListAdapter(ArrayList<UserProfile> contacts) {
        this.contacts = contacts;
    }

    public void setContacts(ArrayList<UserProfile> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();
        UserProfile user = contacts.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_list_entry,null,false); //Use our Layout
        }

        CircleImageView profilePicture = view.findViewById(R.id.profilePicture);

        TextView profileName = view.findViewById(R.id.profilName);
        if(Arrays.equals(user.getProfilePicture(), "-1".getBytes())){
                profilePicture.setImageResource(R.drawable.default_profile);
        }else {
            profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(user.getProfilePicture(), 0, user.getProfilePicture().length));
        }
        profileName.setText(user.getFirstName() + " " + user.getName());

        return view;
    }
}