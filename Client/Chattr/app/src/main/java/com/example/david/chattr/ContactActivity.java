package com.example.david.chattr;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.fragments.ChatListFragment;
import com.example.david.chattr.fragments.ContactListFragment;
import com.example.david.chattr.menu.FaqActivity;
import com.example.david.chattr.menu.PersonalProfileActivity;
import com.example.david.chattr.messaging.ChatActivity;
import com.example.david.chattr.startup.HomeActivity;
import com.example.david.chattr.utils.MySQLiteHelper;

public class ContactActivity extends AppCompatActivity {

    MySQLiteHelper myDbProfile = new MySQLiteHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        findViewById(R.id.edit_profile_bar).setVisibility(View.GONE);

        String firstName = (String)getIntent().getSerializableExtra("firstName");
        String name = (String)getIntent().getSerializableExtra("name");
        String status = (String)getIntent().getSerializableExtra("status");
        String phoneNumber = (String)getIntent().getSerializableExtra("phoneNumber");
        initializeUI(firstName, name, status, phoneNumber);
    }

    public void initializeUI (String firstName, String name, String status, String phoneNumber) {

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(firstName + " " + name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView userNameTextView = findViewById(R.id.username);
        String fullName = firstName + " " + name;
        userNameTextView.setText(fullName);
        TextView phoneNumberTextView = findViewById(R.id.phone_number);
        phoneNumberTextView.setText(phoneNumber);
        TextView statusTextView = findViewById(R.id.biography);
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
                //Sollte fertig sein
                SQLiteDatabase db =  myDbProfile.getWritableDatabase();
                String phoneNumber = (String)getIntent().getSerializableExtra("phoneNumber");
                db.delete(MySQLiteHelper.TABLE_PROFILE, MySQLiteHelper.PHONE_NUMBER + "=" + phoneNumber,null );

                Intent intent = new Intent(ContactActivity.this, HomeActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onNewMessageClicked(View v) {

        String phoneNumber = (String)getIntent().getSerializableExtra("phoneNumber");
        String firstName = (String)getIntent().getSerializableExtra("firstName");
        String name = (String)getIntent().getSerializableExtra("name");
        Bundle extras = getIntent().getExtras();
        byte[] profilePicture = extras.getByteArray("profilePicture");
        byte[] coverImage = extras.getByteArray("coverImage");
        String topic = (String)getIntent().getSerializableExtra("topic");

        SQLiteDatabase db =  myDbProfile.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.FIRST_NAME,firstName);
        cv.put(MySQLiteHelper.NAME,name);
        cv.put(MySQLiteHelper.WRITEABLE,"true");
        cv.put(MySQLiteHelper.PHONE_NUMBER,phoneNumber);
        cv.put(MySQLiteHelper.PROFILE_PICTURE,profilePicture);
        cv.put(MySQLiteHelper.COVER_IMAGE,coverImage);
        cv.put(MySQLiteHelper.TOPIC,topic);
        db.update(MySQLiteHelper.TABLE_PROFILE, cv, MySQLiteHelper.PHONE_NUMBER+"=" +phoneNumber, null);

        Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }
}
