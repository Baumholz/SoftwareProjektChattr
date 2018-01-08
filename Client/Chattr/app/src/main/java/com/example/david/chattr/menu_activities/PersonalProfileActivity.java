package com.example.david.chattr.menu_activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.chattr.R;

public class PersonalProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String phoneNumber;
    String firstName;
    String name;
    String status;

    ImageView cover_image;
    ImageView profile_image;
    TextView user_name;
    TextView phone_number;
    TextView personal_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Personal Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Button edit = (Button) findViewById(R.id.send_message);
        edit.setText("Edit");
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalProfileActivity.this, EditPersonalProfileActivity.class);
                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "0");
        firstName = sharedPreferences.getString("firstName", "default");
        name = sharedPreferences.getString("name", "default");
        status = sharedPreferences.getString("status", "default");

        cover_image = (ImageView) findViewById(R.id.cover_image);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        user_name = (TextView) findViewById(R.id.user_name);
        phone_number = (TextView) findViewById(R.id.phone_number);
        personal_status = (TextView) findViewById(R.id.personal_status);

        user_name.setText(firstName + " " + name);
        phone_number.setText(phoneNumber);
        personal_status.setText(status);
    }

    //for the back button
    @Override
    public  boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "0");
        firstName = sharedPreferences.getString("firstName", "default");
        name = sharedPreferences.getString("name", "default");
        status = sharedPreferences.getString("status", "default");

        //      ImageView cover_image = (ImageView) findViewById(R.id.cover_image);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        user_name = (TextView) findViewById(R.id.user_name);
        phone_number = (TextView) findViewById(R.id.phone_number);
        personal_status = (TextView) findViewById(R.id.personal_status);

        user_name.setText(firstName + " " + name);
        phone_number.setText(phoneNumber);
        personal_status.setText(status);
    }
}
