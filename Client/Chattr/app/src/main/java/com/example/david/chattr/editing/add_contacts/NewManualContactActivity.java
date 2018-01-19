package com.example.david.chattr.editing.add_contacts;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.chattr.R;

import com.example.david.chattr.entities.messaging.Message;
import com.example.david.chattr.messaging.PublishMessage;
import com.example.david.chattr.utils.BitmapScaler;
import com.example.david.chattr.utils.MySQLiteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewManualContactActivity extends AppCompatActivity {

    // Resultcode for Activity result
    private static final int PROFILEIMAGE = 0;
    private static final int COVERIMAGE = 1;
    // To differentiate betwenn the gallery or the cover image being chosen
    // (Can't do this with the result code because it is needed to differentiate bith images)
    private static boolean isGalleryChosen = false;

    private MySQLiteHelper myDbProfile = new MySQLiteHelper(this);
    private SQLiteDatabase dbProfile;
    public String sqlPath;

    private Bitmap bitmapProfileImage;
    private Bitmap bitmapCoverImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_manuall_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("New Contact");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        EditText firstNameEdit = findViewById(R.id.firstNameEdit);
        EditText nameEdit = findViewById(R.id.nameEdit);
        EditText phoneNumberEdit = findViewById(R.id.phoneNumberEdit);

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
                CircleImageView profile_image = (CircleImageView) findViewById(R.id.profile_image_edit);
                TextView profileHintTextView = (TextView) findViewById(R.id.profileHintTextView);
                Uri targetUri = data.getData();

                if (isGalleryChosen) {
                    bitmapProfileImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    if (bitmapProfileImage.getHeight() > 800 || bitmapProfileImage.getWidth() > 800)
                        bitmapProfileImage = BitmapScaler.scaleBitmap(bitmapProfileImage);
                }
                else
                    bitmapProfileImage = (Bitmap) data.getExtras().get("data");

                profile_image.setImageBitmap(bitmapProfileImage);
                profileHintTextView.setBackgroundColor(Color.TRANSPARENT);
                profileHintTextView.setText("");

            } else if (resultCode == RESULT_OK && requestCode == COVERIMAGE) {
                ImageView cover_image = (ImageView) findViewById(R.id.cover_image_edit);
                TextView coverImageHintTextView = (TextView) findViewById(R.id.coverImageHintTextView);
                Uri targetUri = data.getData();

                if (isGalleryChosen) {
                    bitmapCoverImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    if (bitmapCoverImage.getHeight() > 800 || bitmapCoverImage.getWidth() > 800)
                        bitmapCoverImage = BitmapScaler.scaleBitmap(bitmapCoverImage);
                }
                else
                    bitmapCoverImage = (Bitmap) data.getExtras().get("data");

                cover_image.setImageBitmap(bitmapCoverImage);
                coverImageHintTextView.setBackgroundColor(Color.TRANSPARENT);
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

    public void onSaveButtonClicked(View view) {

        EditText firstNameEdit = (EditText) findViewById(R.id.firstNameEdit);
        EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        EditText phoneNumberEdit = (EditText) findViewById(R.id.phoneNumberEdit);

        String firstNameDB = firstNameEdit.getText().toString();
        String nameDB = nameEdit.getText().toString();
        String phoneNumberDB = phoneNumberEdit.getText().toString();
        //(String id, int timestamp, String senderNr, String recipientNr, String content)

        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        String myPhoneNumber = sharedPreferences.getString("phoneNumber", "default");
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        Message msg = new Message("13",timeStamp,myPhoneNumber, "",phoneNumberEdit.getText().toString());
        PublishMessage pubM = new PublishMessage();
        pubM.start();
        pubM.run(msg.toString(),"all/server", 2, myPhoneNumber);

        try {
            if (alreadyInserted(phoneNumberDB)) {
                Toast.makeText(NewManualContactActivity.this, "Phone Number already exists!", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e){}

        if( !firstNameDB.isEmpty() && !nameDB.isEmpty()&& !phoneNumberDB.isEmpty()){

            dbProfile = myDbProfile.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.FIRST_NAME,firstNameDB);
            values.put(MySQLiteHelper.NAME,nameDB);
            values.put(MySQLiteHelper.PHONE_NUMBER,phoneNumberDB);

            if(bitmapProfileImage != null){
              byte[] tempByteArray = getBytes(bitmapProfileImage);                                                                 
              values.put(MySQLiteHelper.PROFILE_PICTURE,tempByteArray);
            }else{
                byte[] tempByteArray = "-1".getBytes();
                values.put(MySQLiteHelper.PROFILE_PICTURE,tempByteArray);
            }

            if(bitmapProfileImage != null){
                byte[] tempByteArray = getBytes(bitmapCoverImage);
                values.put(MySQLiteHelper.COVER_IMAGE,tempByteArray);
            }else{
                byte[] tempByteArray = "-1".getBytes();
                values.put(MySQLiteHelper.PROFILE_PICTURE,tempByteArray);
            }

            values.put(MySQLiteHelper.WRITEABLE,"false");
            values.put(MySQLiteHelper.WRITEABLE,"false");

            long result = dbProfile.insert(MySQLiteHelper.TABLE_PROFILE, null, values);

            setSqlPath();

            if(result != -1) {
                Log.i("NewContact", "Data inserted");
            }else{
                Log.i("NewContact", "Data not inserted");
            }

        }else{
            Toast.makeText(NewManualContactActivity.this, "No Input!", Toast.LENGTH_SHORT).show();
            return;
        }
        dbProfile.close();
        finish();
    }

    public boolean alreadyInserted (String phoneNumber){

        SQLiteDatabase db = myDbProfile.getReadableDatabase();

        String query = "select * from " + MySQLiteHelper.TABLE_PROFILE;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String tempPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.PHONE_NUMBER));
            if(tempPhoneNumber.equals(phoneNumber)){
                return true;
            }
        }
        return false;
    }

    public String getSqlPath(){
        return dbProfile.getPath();
    }

    public void setSqlPath(){
        sqlPath = dbProfile.getPath();
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
