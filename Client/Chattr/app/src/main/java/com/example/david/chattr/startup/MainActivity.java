package com.example.david.chattr.startup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.david.chattr.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If no phone number is saved, an activity to sign up is started
        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString("firstName", "default");
        String name = sharedPreferences.getString("name", "default");
        String phoneNumber = sharedPreferences.getString("phoneNumber", "default");

        if (firstName.equals("default") || name.equals("default") || phoneNumber.equals("default")) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        } else {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            homeIntent.putExtra("phone_number", phoneNumber);
            startActivity(homeIntent);
        }
    }
}
