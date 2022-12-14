package com.example.david.chattr.startup;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.david.chattr.adapters.PagerAdapter;
import com.example.david.chattr.R;
import com.example.david.chattr.fragments.ContactListFragment;
import com.example.david.chattr.menu.FaqActivity;
import com.example.david.chattr.menu.PersonalProfileActivity;
import com.example.david.chattr.messaging.MyMqttService;
import com.facebook.stetho.Stetho;


public class HomeActivity extends AppCompatActivity {

    private final static int PERMISSION_READ_STATE = 1;
    MyMqttService mqttService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setUpActivityView();
    }

    public void setUpActivityView()
    {
        setContentView(R.layout.activity_home);
        toastUserNumber();
        setToolbar();
        setUpTabs();
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

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
        }
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
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.faq_entry:
                Intent intentFaq = new Intent(this, FaqActivity.class);
                startActivity(intentFaq);
                return true;
            case R.id.action_profile:
                Intent intentPp = new Intent(this, PersonalProfileActivity.class);
                startActivity(intentPp);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // Bound to MyMqttService
            MyMqttService.MyLocalBinder binder = (MyMqttService.MyLocalBinder) iBinder;
            mqttService = binder.getService();

            Intent startServiceIntent = new Intent(HomeActivity.this, MyMqttService.class);
            startService(startServiceIntent);

            /*
            * Every User subscribes to the topic of his or her own phoneNumber
            * */
            SharedPreferences sharedPreferences = getSharedPreferences("phoneNumber", Context.MODE_PRIVATE);
            String phoneNumber = sharedPreferences.getString("phoneNumber", "0");
//            mqttService.subscribe("all/"+ phoneNumber);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
}
