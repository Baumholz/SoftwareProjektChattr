package com.example.david.chattr.QrCode;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.david.chattr.R;
import com.example.david.chattr.entities.users.UserProfile;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ShowQrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Personal QR-Code");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView qrImage = (ImageView) findViewById(R.id.qrImageView);

        // Example: {"phoneNumber":"015774738436","status":"","firstName":"David","name":"Hierholz","profilePicture":-1}
        //Todo: Get all the data from the internal Storage
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phoneNumber", "0");
        UserProfile user = new UserProfile(phoneNumber, "", "David","Hierholz", -1);
        String text2Qr = user.toJson().toString();
        Toast.makeText(this, text2Qr, Toast.LENGTH_LONG).show();
        Log.i("Json Content", text2Qr);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,800,800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

    //for the back button
    @Override
    public  boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
