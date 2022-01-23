package com.example.luckywheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.luckywheels.Adapters.EnetrScreenTabsAdapter;
import com.example.luckywheels.R;
import com.example.luckywheels.Utils.SharedPrefs;
import com.example.luckywheels.fragments.EnterFragments.LoginFragment;
import com.example.luckywheels.fragments.EnterFragments.RegisterFragment;
import com.google.android.material.tabs.TabLayout;


public class EnterScreenActivity extends AppCompatActivity {
    private static final String ADD_TAG_IF_NECESSARY = "switch fragment";
    TabLayout tabLayout;
    ViewPager viewPager;
    LoginFragment loginTabFragment;
    RegisterFragment registerTabFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_screen);

        // check if the user is already logged in
        Boolean loginState = SharedPrefs.getBoolean(this, SharedPrefs.KEY_LOG_IN_STATE, false);
        if(loginState ){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // check if the user is already logged in
        int userId = SharedPrefs.getInt(this, SharedPrefs.LOGGED_IN_KEY, -1);
        if(userId != -1){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        tabLayout = findViewById(R.id.login_tab_layout);
        viewPager = findViewById(R.id.login_view_pager);

        loginTabFragment = new LoginFragment();
        registerTabFragment = new RegisterFragment();




        final EnetrScreenTabsAdapter enetrScreenTabsAdapter = new EnetrScreenTabsAdapter(getSupportFragmentManager(), 0);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        enetrScreenTabsAdapter.addFragment(loginTabFragment, getString(R.string.login));
        enetrScreenTabsAdapter.addFragment(registerTabFragment, getString(R.string.register));
        viewPager.setAdapter(enetrScreenTabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }


    public void replaceFragment(Fragment fragment) {
        final EnetrScreenTabsAdapter enetrScreenTabsAdapter = new EnetrScreenTabsAdapter(getSupportFragmentManager(), 0);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        enetrScreenTabsAdapter.addFragment(loginTabFragment, getString(R.string.login));
        enetrScreenTabsAdapter.addFragment(registerTabFragment, getString(R.string.register));
        viewPager.setAdapter(enetrScreenTabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

}