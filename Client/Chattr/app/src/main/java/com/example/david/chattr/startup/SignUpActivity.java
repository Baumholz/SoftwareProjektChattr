package com.example.david.chattr.startup;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.chattr.R;
import com.example.david.chattr.utils.ImageSaver;

import java.io.FileNotFoundException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
        }
    }

    public void onSignUpButtonClicked (View view) {
        EditText firstNameSignUp = (EditText) findViewById(R.id.firstNameSignUp);
        EditText nameSignUp = (EditText) findViewById(R.id.nameSignUp);
        EditText phoneNumberSignUp = (EditText) findViewById(R.id.phoneNumberSignUp);
        EditText statusSignUp = (EditText) findViewById(R.id.statusSignUp);

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

    // Start Dialog to chose between Gallery and Camera
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
                CircleImageView profile_image = (CircleImageView) findViewById(R.id.profile_image);
                TextView profileHintTextView = (TextView) findViewById(R.id.profileHintTextView);
                Uri targetUri = data.getData();

                Bitmap bitmap;
                if (isGalleryChosen)
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                else
                    bitmap = (Bitmap) data.getExtras().get("data");

                profile_image.setImageBitmap(bitmap);
                profileHintTextView.setText("");

                new ImageSaver(getApplicationContext()).setFileName("profile_image.png").setDirectoryName("images").save(bitmap);

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

                new ImageSaver(getApplicationContext()).setFileName("cover_image.png").setDirectoryName("images").save(bitmap);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
