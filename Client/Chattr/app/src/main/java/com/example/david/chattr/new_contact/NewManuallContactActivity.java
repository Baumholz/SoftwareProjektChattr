package com.example.david.chattr.new_contact;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.mqtt_chat.MySQLiteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class NewManuallContactActivity extends AppCompatActivity {

    private MySQLiteHelper myDbProfile = new MySQLiteHelper(this);
    private SQLiteDatabase dbProfile;
    public String sqlPath;
    int z = -5;

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

    public void profileImageView(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
            TextView profileHintTextView = (TextView) findViewById(R.id.profileHintTextView);
            Uri targetUri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                profile_image.setImageBitmap(bitmap);
                profileHintTextView.setText("");
            } catch (FileNotFoundException e) {
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

    //Todo: Functionality to Save Contact
    public void onSaveButtonClicked(View view) {

        EditText firstNameEdit = (EditText) findViewById(R.id.firstNameEdit);
        EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        EditText phoneNumberEdit = (EditText) findViewById(R.id.phoneNumberEdit);

        String firstNameDB = firstNameEdit.getText().toString();
        String nameDB = nameEdit.getText().toString();
        String phoneNumberDB = phoneNumberEdit.getText().toString();

        if( !firstNameDB.isEmpty() && !nameDB.isEmpty()&& !phoneNumberDB.isEmpty()){

            dbProfile = myDbProfile.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.FIRST_NAME,firstNameDB);
            values.put(MySQLiteHelper.NAME,nameDB);
            values.put(MySQLiteHelper.PHONE_NUMBER,phoneNumberDB);
            long result = dbProfile.insert(MySQLiteHelper.TABLE_PROFILE, null, values);

            setSqlPath();

            if(result != -1) {
                Toast.makeText(NewManuallContactActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(NewManuallContactActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(NewManuallContactActivity.this, "No Input!", Toast.LENGTH_SHORT).show();
            return;
        }
        dbProfile.close();

        if(z > 0) {
            ArrayList<UserProfile> temp = new ArrayList<UserProfile>(readDB());
        }
        z++;
    }
    public ArrayList <UserProfile> readDB(){

        dbProfile = myDbProfile.getReadableDatabase();

        ArrayList<UserProfile> recipients = new ArrayList<UserProfile>();

        Cursor c = dbProfile.rawQuery(MySQLiteHelper.TABLE_PROFILE, null);


        while (!c.isAfterLast()) {
            String tempFirstName = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.FIRST_NAME));
            String tempName = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.FIRST_NAME));
            String tempPhoneNumber = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.FIRST_NAME));

            UserProfile user = new UserProfile(tempPhoneNumber, "none", tempFirstName, tempName, R.drawable.hund);
            recipients.add(user);
        }
    return recipients;
    }
    public String getSqlPath(){
        return dbProfile.getPath();
    }



    public void setSqlPath(){
        sqlPath = dbProfile.getPath();
    }
}
