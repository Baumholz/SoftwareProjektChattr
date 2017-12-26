package com.example.david.chattr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;


public class NewManuallContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_manuall_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Contact");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        EditText firstNameEdit = (EditText) findViewById(R.id.firstNameEdit);
        EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        EditText phoneNumberEdit = (EditText) findViewById(R.id.phoneNumberEdit);

        if (getIntent().hasExtra("Contact")) {
            String contact = (String)getIntent().getSerializableExtra("Contact");
            try {
                JSONObject json = new JSONObject(contact);
                String phoneNumber = json.getString("phoneNumber");
                String status = json.getString("status");
                String firstName = json.getString("firstName");
                String name = json.getString("name");
                String profilePicure = json.getString("profilePicture");

                firstNameEdit.setText(firstName);
                nameEdit.setText(name);
                phoneNumberEdit.setText(phoneNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    //for the back button
    @Override
    public  boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    public void onSaveButtonClicked(View view) {
        //Todo: Functionality to Save Contact
    }
}
