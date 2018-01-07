package com.example.david.chattr.new_contact;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class NewManuallContactActivity extends AppCompatActivity {

    // Resultcode for Activity result
    private static final int PROFILEIMAGE = 0;
    private static final int COVERIMAGE = 1;
    // To differentiate betwenn the gallery or the cover image being chosen
    // (Can't do this with the result code because it is needed to differentiate bith images)
    private static boolean isGalleryChosen = false;

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

    public void onClickProfileImage(View view) {
        startDialog(PROFILEIMAGE);
    }

    public void onClickCoverImage(View view) {
        startDialog(COVERIMAGE);
    }

    // Start Dialog to chose between Galery and Camera
    public void startDialog(final int requestCode) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        pictureDialog.setItems(R.array.gallery_or_camera,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                isGalleryChosen = true;
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, requestCode);
                                break;
                            case 1:
                                isGalleryChosen = false;
                                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, requestCode);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK && requestCode == PROFILEIMAGE) {
                ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
                TextView profileHintTextView = (TextView) findViewById(R.id.profileHintTextView);
                Uri targetUri = data.getData();

                Bitmap bitmap;
                if (isGalleryChosen)
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                else
                    bitmap = (Bitmap) data.getExtras().get("data");

                profile_image.setImageBitmap(bitmap);
                profileHintTextView.setText("");

            } else if (resultCode == RESULT_OK && requestCode == COVERIMAGE) {
                ImageView cover_image = (ImageView) findViewById(R.id.cover_image);
                TextView coverImageHintTextView = (TextView) findViewById(R.id.coverImageHintTextView);
                Uri targetUri = data.getData();

                Bitmap bitmap;
                if (isGalleryChosen)
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                else
                    bitmap = (Bitmap) data.getExtras().get("data");

                cover_image.setImageBitmap(bitmap);
                coverImageHintTextView.setText("");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
