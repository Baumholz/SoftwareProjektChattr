package com.example.david.chattr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.david.chattr.homeactivity_fragments.PagerAdapter;
import com.example.david.chattr.mqtt_chat.MyMqttService;


public class HomeActivity extends AppCompatActivity {

    private final static int PERMISSION_READ_STATE = 1;
    MyMqttService mqttService;
    private String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //if no phone number is save, an activity to sign up is started thirst
        SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phoneNumber", "default");
        if (phoneNumber.equals("default")) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }

        Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Habdles the tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to MyMqttService
        Intent intent = new Intent(this, MyMqttService.class);
        intent.putExtra("clientId", clientId);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        //Going back to the  main activity should not be possible
        //super.onBackPressed();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // Bound to MyMqttService
            MyMqttService.MyLocalBinder binder = (MyMqttService.MyLocalBinder) iBinder;
            mqttService = binder.getService();

            Intent startServiceIntent = new Intent(HomeActivity.this, MyMqttService.class);
            startService(startServiceIntent);

//            mqttService.sendMessage("/pub/trainID/camID/", "message");
            //Subscribe to newly created topic Todo: Real timestamp as topic
//            mqttService.subscribe("/pub/trainID/camID/");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
