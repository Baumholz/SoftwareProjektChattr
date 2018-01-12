package com.example.david.chattr.profiles.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.david.chattr.R;
import com.example.david.chattr.new_contact.NewManuallContactActivity;
import com.example.david.chattr.new_contact.NfcBeamActivity;
import com.example.david.chattr.new_contact.ShowQrActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by david on 15.11.17.
 */

public class NewContactFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_contact, container, false);

        // The differennt buttons to chose how to add a new contact
        Button manuallButton = view.findViewById(R.id.manuallButton);
        Button nfcButton = view.findViewById(R.id.nfcButton);
        Button qrButton = view.findViewById(R.id.qrButton);
        Button showQrButton = view.findViewById(R.id.showQrButton);

        // The button click listeners start new Activitys
        // Add Contact manually
        manuallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewManuallContactActivity.class);
                startActivity(intent);
            }
        });
        // Add Contact with NFC
        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NfcBeamActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        // Add Contact with QR
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, 0);

                } catch (Exception e) {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);

                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Should work the same for NFC and QR. TODO: Test for NFC
        if ((requestCode == 0 || resultCode == 1) && resultCode == RESULT_OK) {
            String contents = data.getStringExtra("SCAN_RESULT");
            Intent intent = new Intent(getActivity(), NewManuallContactActivity.class);
            intent.putExtra("Contact", contents);
            startActivity(intent);
        }
    }
}
