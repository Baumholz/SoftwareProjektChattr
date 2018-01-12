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

import com.example.david.chattr.ContactActivity;
import com.example.david.chattr.R;
import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.utils.MySQLiteHelper;
import com.example.david.chattr.adapters.ContactListAdapter;

import java.util.ArrayList;

/**
 * Created by david on 15.11.17.
 */
public class ContactListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MySQLiteHelper myDbProfile = new MySQLiteHelper(getContext());
        View view =  inflater.inflate(R.layout.fragment_contactlist, container, false);

        final ArrayList<UserProfile> contacts = new ArrayList<>(myDbProfile.getProfiles());

        final ContactListAdapter myContactListAdapter = new ContactListAdapter(contacts);
        final ListView contactListView = view.findViewById(R.id.contactList);
        contactListView.setAdapter(myContactListAdapter);
        myContactListAdapter.notifyDataSetChanged();

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ContactListFragment.this.getActivity(), ContactActivity.class);
                intent.putExtra("phoneNumber", contacts.get(i).getPhoneNumber());
                intent.putExtra("firstName", contacts.get(i).getFirstName());
                intent.putExtra("name", contacts.get(i).getName());
                intent.putExtra("status", contacts.get(i).getStatus());
                startActivity(intent);
            }
        });

        return view;
    }
}
