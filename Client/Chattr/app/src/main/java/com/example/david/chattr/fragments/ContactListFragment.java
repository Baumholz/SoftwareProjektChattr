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

import com.example.david.chattr.menu.ContactActivity;
import com.example.david.chattr.R;
import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.utils.MySQLiteHelper;
import com.example.david.chattr.adapters.ContactListAdapter;

import java.util.ArrayList;

/**
 * Created by david on 15.11.17.
 */
public class ContactListFragment extends Fragment {

    private ContactListAdapter myContactListAdapter;
    private ArrayList<UserProfile> contacts;
    private MySQLiteHelper myDbProfile;
    private ListView contactListView;

    @Override
    public void onResume() {
        super.onResume();
        contacts = new ArrayList<>(myDbProfile.getProfiles());
        myContactListAdapter.setContacts(contacts);
        setContactListeners();
        myContactListAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myDbProfile = new MySQLiteHelper(getContext());
        View view =  inflater.inflate(R.layout.fragment_contactlist, container, false);

        contacts = new ArrayList<>(myDbProfile.getProfiles());

        myContactListAdapter = new ContactListAdapter(contacts);
        contactListView = view.findViewById(R.id.contactList);
        contactListView.setAdapter(myContactListAdapter);
        myContactListAdapter.notifyDataSetChanged();

        setContactListeners();

        return view;
    }

    private void setContactListeners() {
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                intent.putExtra("phoneNumber", contacts.get(i).getPhoneNumber());
                intent.putExtra("firstName", contacts.get(i).getFirstName());
                intent.putExtra("name", contacts.get(i).getName());
                intent.putExtra("status", contacts.get(i).getStatus());
                intent.putExtra("profilePicture", contacts.get(i).getProfilePicture());
                intent.putExtra("coverImage", contacts.get(i).getCoverImage());
                intent.putExtra("topic", contacts.get(i).getTopic());
                startActivity(intent);
            }
        });
    }
}
