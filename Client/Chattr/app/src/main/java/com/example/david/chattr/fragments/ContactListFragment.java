package com.example.david.chattr.fragments;

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
                //TODO: Set Intent to Profile View
            }
        });

        return view;
    }
}
