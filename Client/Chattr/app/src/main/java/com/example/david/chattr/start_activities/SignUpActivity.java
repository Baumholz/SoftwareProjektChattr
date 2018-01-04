package com.example.david.chattr.start_activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.chattr.R;

import java.io.FileNotFoundException;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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

    public void profileImageView(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            ImageView profile_image = (ImageView) findViewById(R.id.profileImageSignUp);
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
}
