package com.example.david.chattr.editing;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.messaging.Message;
import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.messaging.MyMqttService;
import com.example.david.chattr.startup.SignUpActivity;
import com.example.david.chattr.utils.BitmapScaler;
import com.example.david.chattr.utils.ImageSaver;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class EditPersonalProfileActivity extends AppCompatActivity {

    // Resultcode for Activity result
    private static final int PROFILEIMAGE = 0;
    private static final int COVERIMAGE = 1;
    // To differentiate betwenn the gallery or the cover image being chosen
    // (Can't do this with the result code because it is needed to differentiate bith images)
    private static boolean isGalleryChosen = false;

    private EditText firstNameEdit;
    EditText nameEdit;
    EditText statusEdit;

    private MyMqttService mqttService;
    private Bitmap bitmapProfile = null;
    private Bitmap bitmapCover = null;

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to MyMqttService
        Intent intent = new Intent(this, MyMqttService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit your Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firstNameEdit = (EditText) findViewById(R.id.firstNameEdit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        statusEdit = (EditText) findViewById(R.id.statusEdit);

        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString("firstName", "default");
        String name = sharedPreferences.getString("name", "default");
        String status = sharedPreferences.getString("status", "default");

        firstNameEdit.setHint(firstName);
        nameEdit.setHint(name);
        statusEdit.setHint(status);
    }

    public void onSaveButtonClicked(View view) {

        String firstName = firstNameEdit.getText().toString().equals("") ? firstNameEdit.getHint().toString() : firstNameEdit.getText().toString();
        String name = nameEdit.getText().toString().equals("") ? nameEdit.getHint().toString() : nameEdit.getText().toString();
        String status = statusEdit.getText().toString().equals("") ? statusEdit.getHint().toString() : statusEdit.getText().toString();;

        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phoneNumber", "-1");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName", firstName);
        editor.putString("name", name);
        editor.putString("status", status);
        editor.apply();
        finish();

        /*
        *
        * Creating message with new profile information for the Server
        *
        * */
        byte[] byteArrayProfile = null;
        byte[] byteArrayCover = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmapProfile != null) {
            bitmapProfile.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArrayProfile = stream.toByteArray();
        }
        if (bitmapCover != null) {
            bitmapCover.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArrayCover = stream.toByteArray();
        }

        String userProfile = new UserProfile(phoneNumber,status,firstName,name,byteArrayProfile,byteArrayCover,"","-1").toJson().toString();
        Message signUpMessage = new Message("10", 0, phoneNumber, "-1", userProfile);
        Log.i("newProfileMessage", signUpMessage.toString());
        mqttService.sendMessage("/all", signUpMessage.toString());
    }

    //for the back button
    @Override
    public  boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
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

                if (isGalleryChosen) {
                    bitmapProfile = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    if (bitmapProfile.getHeight() > 800 || bitmapProfile.getWidth() > 800)
                        bitmapProfile = BitmapScaler.scaleBitmap(bitmapProfile);
                }
                else
                    bitmapProfile = (Bitmap) data.getExtras().get("data");

                profile_image.setImageBitmap(bitmapProfile);
                profileHintTextView.setText("");
                profileHintTextView.setBackgroundColor(Color.TRANSPARENT);

                new ImageSaver(getApplicationContext()).setFileName("profile_image.png").setDirectoryName("images").save(bitmapProfile);

            } else if (resultCode == RESULT_OK && requestCode == COVERIMAGE) {
                ImageView cover_image = (ImageView) findViewById(R.id.cover_image);
                TextView coverImageHintTextView = (TextView) findViewById(R.id.coverImageHintTextView);
                Uri targetUri = data.getData();

                if (isGalleryChosen) {
                    bitmapCover = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    if (bitmapCover.getHeight() > 800 || bitmapCover.getWidth() > 800)
                        bitmapCover = BitmapScaler.scaleBitmap(bitmapCover);
                }
                else
                    bitmapCover = (Bitmap) data.getExtras().get("data");

                cover_image.setImageBitmap(bitmapCover);
                coverImageHintTextView.setText("");
                coverImageHintTextView.setBackgroundColor(Color.TRANSPARENT);

                new ImageSaver(getApplicationContext()).setFileName("cover_image.png").setDirectoryName("images").save(bitmapCover);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    *
    * To Send message to the server if information has been changed
    *
    * */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // Bound to MyMqttService
            MyMqttService.MyLocalBinder binder = (MyMqttService.MyLocalBinder) iBinder;
            mqttService = binder.getService();

            //Start Service
            Intent startServiceIntent = new Intent(EditPersonalProfileActivity.this, MyMqttService.class);
            startService(startServiceIntent);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
