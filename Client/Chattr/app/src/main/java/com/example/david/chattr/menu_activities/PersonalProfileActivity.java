package com.example.david.chattr.menu_activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.chattr.R;

public class PersonalProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Personal Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phoneNumber", "0");
        String firstName = sharedPreferences.getString("firstName", "default");
        String name = sharedPreferences.getString("name", "default");
        String status = sharedPreferences.getString("status", "default");


//        ImageView cover_image = (ImageView) findViewById(R.id.cover_image);
        ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
        TextView user_name = (TextView) findViewById(R.id.user_name);
        TextView phone_number = (TextView) findViewById(R.id.phone_number);
        TextView personal_status = (TextView) findViewById(R.id.personal_status);

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
}
