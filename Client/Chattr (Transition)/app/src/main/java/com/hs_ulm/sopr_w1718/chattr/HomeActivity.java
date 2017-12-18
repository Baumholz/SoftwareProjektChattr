package com.example.david.chattr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.david.chattr.Utils.AppMenuView;
import com.example.david.chattr.Utils.SlidingMenuLayout;
import com.example.david.chattr.mqtt_chat.MyMqttService;
import com.example.david.chattr.profiles.personal.PersonalProfileActivity;


public class HomeActivity extends AppCompatActivity {

    private final static int PERMISSION_READ_STATE = 1;
    MyMqttService mqttService;
    private String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActivityView();
    }

    public void setUpActivityView()
    {
        setContentView(R.layout.activity_home);
        toastUserNumber();
        setToolbar();
        setUpTabs();
    }

    @Override
    public void setContentView(View view) {

        SlidingMenuLayout layout = new SlidingMenuLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));

        layout.addView(new AppMenuView());
        layout.addView(view);

        super.setContentView(layout);
    }

    public void toastUserNumber()
    {
        String phoneNumber = getIntent().getStringExtra("phone_number");
        Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();
    }

    public void setToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setUpTabs()
    {
        ViewPager tabPager = setTabPager();
        setTabLayout(tabPager);
    }

    public ViewPager setTabPager()
    {
        ViewPager viewPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        return viewPager;
    }

    public void setTabLayout(ViewPager pager)
    {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void launchPersonalProfile() {
        Intent intent = new Intent(this, PersonalProfileActivity.class);
        startActivity(intent);
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
