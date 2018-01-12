package com.example.david.chattr.editing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.chattr.R;
import com.example.david.chattr.utils.ImageSaver;

import java.io.FileNotFoundException;

public class EditPersonalProfileActivity extends AppCompatActivity {

    // Resultcode for Activity result
    private static final int PROFILEIMAGE = 0;
    private static final int COVERIMAGE = 1;
    // To differentiate betwenn the gallery or the cover image being chosen
    // (Can't do this with the result code because it is needed to differentiate bith images)
    private static boolean isGalleryChosen = false;

    EditText firstNameEdit;
    EditText nameEdit;
    EditText statusEdit;

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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName", firstName);
        editor.putString("name", name);
        editor.putString("status", status);
        editor.apply();
        finish();
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
