package com.example.david.chattr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.chattr.menu.FaqActivity;
import com.example.david.chattr.menu.PersonalProfileActivity;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_profile);

        String firstName = (String)getIntent().getSerializableExtra("firstName");
        String name = (String)getIntent().getSerializableExtra("name");
        String status = (String)getIntent().getSerializableExtra("status");
        String phoneNumber = (String)getIntent().getSerializableExtra("phoneNumber");
        initializeUI(firstName, name, status, phoneNumber);
    }

    public void initializeUI (String firstName, String name, String status, String phoneNumber) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(firstName + " " + name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView userNameTextView = (TextView) findViewById(R.id.username);
        String fullName = firstName + " " + name;
        userNameTextView.setText(fullName);
        TextView phoneNumberTextView = (TextView) findViewById(R.id.phone_number);
        phoneNumberTextView.setText(phoneNumber);
        TextView statusTextView = (TextView) findViewById(R.id.biography);
        statusTextView.setText(status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //for the back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_contact:
                // TODO: Here is where the logic to delete a contact needs to be implemented
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
