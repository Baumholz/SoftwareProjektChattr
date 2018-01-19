package com.example.david.chattr.startup;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.messaging.Message;
import com.example.david.chattr.entities.users.UserProfile;
import com.example.david.chattr.messaging.ChatActivity;
import com.example.david.chattr.messaging.MyMqttService;
import com.example.david.chattr.utils.BitmapScaler;
import com.example.david.chattr.utils.ImageSaver;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    // Resultcode for Activity result to differentiate between the gallery and the camera
    private static final int GALLERY= 0;
    private static final int CAMERA = 1;

    // Resultcode for Activity result
    private static final int PROFILEIMAGE = 0;
    private static final int COVERIMAGE = 1;
    // To differentiate betwenn the gallery or the cover image being chosen
    // (Can't do this with the result code because it is needed to differentiate bith images)
    private static boolean isGalleryChosen = false;

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
        setContentView(R.layout.activity_sign_up);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
        }
    }

    public void onSignUpButtonClicked (View view) {
        EditText firstNameSignUp = findViewById(R.id.firstNameSignUp);
        EditText nameSignUp = findViewById(R.id.nameSignUp);
        EditText phoneNumberSignUp = findViewById(R.id.phoneNumberSignUp);
        EditText statusSignUp = findViewById(R.id.statusSignUp);

        String firstName = firstNameSignUp.getText().toString();
        String name = nameSignUp.getText().toString();
        String phoneNumber = phoneNumberSignUp.getText().toString();
        String status = statusSignUp.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName", firstName);
        editor.putString("name", name);
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("status", status);
        editor.apply();

        /*
        * 
        * Creating Signup message for the Server
        * 
        * */
        byte[] byteArrayProfile;
        byte[] byteArrayCover;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmapProfile != null) {
            bitmapProfile.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArrayProfile = stream.toByteArray();
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.default_profile);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            if (bitmap.getHeight() > 800 || bitmap.getWidth() > 800)
                bitmap = BitmapScaler.scaleBitmap(bitmap);

            new ImageSaver(getApplicationContext()).setFileName("profile_image.png").setDirectoryName("images").save(bitmap);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArrayProfile = stream.toByteArray();
        }

        if (bitmapCover != null) {
            bitmapCover.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArrayCover = stream.toByteArray();
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.default_cover);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            if (bitmap.getHeight() > 800 || bitmap.getWidth() > 800)
                bitmap = BitmapScaler.scaleBitmap(bitmap);

            new ImageSaver(getApplicationContext()).setFileName("cover_image.png").setDirectoryName("images").save(bitmap);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArrayCover = stream.toByteArray();
        }

        String userProfile = new UserProfile(phoneNumber,status,firstName,name,byteArrayProfile,byteArrayCover,"false","false").toJson().toString();
        Message signUpMessage = new Message("10", 0, phoneNumber, "-1", userProfile);
        Log.i("signUpMessage", signUpMessage.toString());
        Log.i("userProfile", userProfile);
        mqttService.sendMessage("/all", signUpMessage.toString());

        /*
        *
        * Start Home Activity, which is the 'base' activity of the app
        *
        * */

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //Going back to the  main activity should not be possible
        //super.onBackPressed();
    }

    public void onClickProfileImage(View view) {
        startDialog(PROFILEIMAGE);
    }

    public void onClickCoverImage(View view) {
        startDialog(COVERIMAGE);
    }

    /*
    *
    * Start Dialog to chose between Galery and Camera
    *
    * */
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

    /*
    *
    * Here the picture took with the camera or chosen in the gallery
    * gets saved and set in the Views
    *
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == PROFILEIMAGE) {

                CircleImageView profile_image = (CircleImageView) findViewById(R.id.profile_image_edit);
                TextView profileHintTextView = (TextView) findViewById(R.id.profileHintTextView);

                Uri targetUri = data.getData();

                if (isGalleryChosen) {
                    bitmapProfile = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    if (bitmapProfile.getHeight() > 800 || bitmapProfile.getWidth() > 800)
                        bitmapProfile = BitmapScaler.scaleBitmap(bitmapProfile);
                }
                else {
                    bitmapProfile = (Bitmap) data.getExtras().get("data");
                }

                profile_image.setImageBitmap(bitmapProfile);
                profileHintTextView.setText("");
                profileHintTextView.setBackgroundColor(Color.TRANSPARENT);
                Log.i("Bitmap Size", "Width: " + bitmapProfile.getWidth() + "\nHeight: " + bitmapProfile.getHeight());

                new ImageSaver(getApplicationContext()).setFileName("profile_image.png").setDirectoryName("images").save(bitmapProfile);

            } else if (resultCode == RESULT_OK && requestCode == COVERIMAGE) {

                ImageView cover_image = (ImageView) findViewById(R.id.cover_image_edit);
                TextView coverImageHintTextView = (TextView) findViewById(R.id.coverImageHintTextView);

                Uri targetUri = data.getData();

                if (isGalleryChosen)
                    bitmapCover = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    if (bitmapCover.getHeight() > 800 || bitmapCover.getWidth() > 800)
                        bitmapCover = BitmapScaler.scaleBitmap(bitmapCover);
                else
                    bitmapCover = (Bitmap) data.getExtras().get("data");

                cover_image.setImageBitmap(bitmapCover);
                coverImageHintTextView.setText("");
                coverImageHintTextView.setBackgroundColor(Color.TRANSPARENT);
                Log.i("Bitmap Size", "Width: " + bitmapCover.getWidth() + "\nHeight: " + bitmapCover.getHeight());

                new ImageSaver(getApplicationContext()).setFileName("cover_image.png").setDirectoryName("images").save(bitmapCover);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    *
    * To Send the Signup message with personal information to the server
    *
    * */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // Bound to MyMqttService
            MyMqttService.MyLocalBinder binder = (MyMqttService.MyLocalBinder) iBinder;
            mqttService = binder.getService();

            //Start Service
            Intent startServiceIntent = new Intent(SignUpActivity.this, MyMqttService.class);
            startService(startServiceIntent);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
