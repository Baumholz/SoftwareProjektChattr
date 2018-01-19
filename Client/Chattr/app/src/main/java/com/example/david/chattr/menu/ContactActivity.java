package com.example.david.chattr.menu;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.david.chattr.R;
import com.example.david.chattr.messaging.ChatActivity;
import com.example.david.chattr.startup.HomeActivity;
import com.example.david.chattr.utils.MySQLiteHelper;

public class ContactActivity extends AppCompatActivity {

    MySQLiteHelper myDbProfile = new MySQLiteHelper(this);

    Toolbar toolbar;
    TextView userNameTextView;
    TextView phoneNumberTextView;
    TextView bioTextView;

    Button newMessageButton;

    String username;
    String phoneNumber;
    String bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        initializeUI();
    }

    public void initializeUI() {
        setUpUsername();
        setUpPhoneNumber();
        setUpBio();
        setUpToolbar();

        newMessageButton = findViewById(R.id.new_message);
        newMessageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onNewMessageClicked();
            }
        });

        findViewById(R.id.edit_profile_bar).setVisibility(View.GONE);
    }

    private void setUpUsername() {
        String firstName = (String) getIntent().getSerializableExtra("firstName");
        String name = (String) getIntent().getSerializableExtra("name");

        username = firstName + " " + name;
        userNameTextView = findViewById(R.id.username);
        userNameTextView.setText(username);
    }

    private void setUpPhoneNumber() {
        phoneNumber = (String) getIntent().getSerializableExtra("phoneNumber");

        phoneNumberTextView = findViewById(R.id.phone_number);
        phoneNumberTextView.setText(phoneNumber);
    }

    private void setUpBio() {
        bio = (String) getIntent().getSerializableExtra("status");

        bioTextView = findViewById(R.id.biography);
        bioTextView.setText(bio);
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(username);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete_contact:
                SQLiteDatabase db = myDbProfile.getWritableDatabase();
                String phoneNumber = (String) getIntent().getSerializableExtra("phoneNumber");
                db.delete(MySQLiteHelper.TABLE_PROFILE, MySQLiteHelper.PHONE_NUMBER + "=" + phoneNumber, null);

                Intent intent = new Intent(ContactActivity.this, HomeActivity.class);
                startActivity(intent);

                return true;

            case R.id.action_send_message:
                onNewMessageClicked();
                return true;

            case R.id.action_edit_contact:
                onNewMessageClicked();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onNewMessageClicked() {

        String phoneNumber = (String) getIntent().getSerializableExtra("phoneNumber");
        String firstName = (String) getIntent().getSerializableExtra("firstName");
        String name = (String) getIntent().getSerializableExtra("name");
        Bundle extras = getIntent().getExtras();
        byte[] profilePicture = extras.getByteArray("profilePicture");
        byte[] coverImage = extras.getByteArray("coverImage");
        String topic = (String) getIntent().getSerializableExtra("topic");

        SQLiteDatabase db = myDbProfile.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.FIRST_NAME, firstName);
        cv.put(MySQLiteHelper.NAME, name);
        cv.put(MySQLiteHelper.WRITEABLE, "true");
        cv.put(MySQLiteHelper.PHONE_NUMBER, phoneNumber);
        cv.put(MySQLiteHelper.PROFILE_PICTURE, profilePicture);
        cv.put(MySQLiteHelper.COVER_IMAGE, coverImage);
        cv.put(MySQLiteHelper.TOPIC, topic);
        db.update(MySQLiteHelper.TABLE_PROFILE, cv, MySQLiteHelper.PHONE_NUMBER + "=" + phoneNumber, null);

        Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }
}
