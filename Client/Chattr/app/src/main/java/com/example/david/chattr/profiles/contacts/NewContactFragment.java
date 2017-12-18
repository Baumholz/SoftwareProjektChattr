package com.example.david.chattr.profiles.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.david.chattr.R;

/**
 * Created by david on 15.11.17.
 */

public class NewContactFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_contact, container, false);

        // The differennt buttons to chose how to add a new contact
        Button manuallButton = (Button) view.findViewById(R.id.manuallButton);
        Button nfcButton = (Button) view.findViewById(R.id.nfcButton);
        Button qrButton = (Button) view.findViewById(R.id.qrButton);
        Button showQrButton = (Button) view.findViewById(R.id.showQrButton);

        // The button click listeners start new Activitys
        // Add Contact manually
        manuallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditContactActivity.class);
                startActivity(intent);
            }
        });
        // Add Contact with NFC
        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Start NFC reader
            }
        });
        // Add Contact with QR
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Start camera to read qr code
            }
        });
        // Show your own QR-Code
        showQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShowQrActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
