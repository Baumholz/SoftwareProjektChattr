package com.example.david.chattr.menu_activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.chattr.R;

import java.io.FileNotFoundException;

public class EditPersonalProfileActivity extends AppCompatActivity {

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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    public void onClickCoverImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == 0) {
                ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
                TextView profileHintTextView = (TextView) findViewById(R.id.profileHintTextView);
                Uri targetUri = data.getData();

                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                profile_image.setImageBitmap(bitmap);
                profileHintTextView.setText("");

            } else if (resultCode == RESULT_OK && requestCode == 1) {
                ImageView cover_image = (ImageView) findViewById(R.id.cover_image);
                TextView coverImageHintTextView = (TextView) findViewById(R.id.coverImageHintTextView);
                Uri targetUri = data.getData();

                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                cover_image.setImageBitmap(bitmap);
                coverImageHintTextView.setText("");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
