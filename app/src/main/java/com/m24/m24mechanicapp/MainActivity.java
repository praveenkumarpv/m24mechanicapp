package com.m24.m24mechanicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ImageView logo;
    ViewPager widget;
    TabLayout tabHost;
    LinearLayout formlayout;
    SharedPreferences systemfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        formlayout = findViewById(R.id.form);
        logo = findViewById(R.id.logo);
        Glide.with(MainActivity.this).load(R.drawable.m24logo).into(logo);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                systemfile = getSharedPreferences("preference",Context.MODE_PRIVATE);
                String uid = systemfile.getString("licenceno","");
                if (uid.isEmpty()){
                    formlayout.setVisibility(View.VISIBLE);
                }
                else {
                    Intent intent = new Intent(MainActivity.this,Mainscreen.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        },5300);

        widget = findViewById(R.id.widgetviewpager);
        tabHost = findViewById(R.id.logintab);
        tabHost.addTab(tabHost.newTab().setText("Login"));
        tabHost.addTab(tabHost.newTab().setText("Register"));
        tabHost.setTabGravity(TabLayout.GRAVITY_FILL);
        final loginadpter loginadpte = new loginadpter(getSupportFragmentManager(),this,tabHost.getTabCount());
        widget.setAdapter(loginadpte);
        widget.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabHost));
        tabHost.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                widget.setCurrentItem(tab.getPosition());
                if (tab.getPosition()==0 || tab.getPosition()==1) {
                    loginadpte.notifyDataSetChanged();
                    if (tab.getPosition()==0){
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    public class loginadpter extends FragmentPagerAdapter{
        private Context context;
        private int count;
        public int c;

        public loginadpter(@NonNull FragmentManager fm,Context context,int cout) {
            super(fm);
            this.context = context;
            this.count = cout;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){

                case 0:
                    loginfragment loginfragment = new loginfragment();
                    return loginfragment;
                case 1:
                    register register = new register();
                    return register;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}