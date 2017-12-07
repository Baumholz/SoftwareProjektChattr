package com.example.david.chattr.mainactivity_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.david.chattr.ChatActivity;
import com.example.david.chattr.MainActivity;
import com.example.david.chattr.NewContactActivity;
import com.example.david.chattr.R;
import com.example.david.chattr.entities.users.UserProfile;

import java.util.ArrayList;

/**
 * Created by david on 15.11.17.
 */

public class NewContactFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_contact, container, false);

        Button manuallButton = (Button) view.findViewById(R.id.manuallButton);

        manuallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewContactActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
