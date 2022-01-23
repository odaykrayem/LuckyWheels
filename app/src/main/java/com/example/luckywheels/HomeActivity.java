package com.example.luckywheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.luckywheels.UIUtils.EndDrawerToggle;
import com.example.luckywheels.Utils.Constants;
import com.example.luckywheels.Utils.SharedPrefs;
import com.example.luckywheels.fragments.HomeFragments.LotteryFragment;
import com.example.luckywheels.fragments.HomeFragments.LotteryWinnersListFragment;
import com.example.luckywheels.fragments.HomeFragments.LuckyWheelFragment;
import com.example.luckywheels.fragments.HomeFragments.ProfileFragment;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    LuckyWheelFragment luckyWheelFragment;

    @BindView(R.id.menu_btn_lucky_wheel)
    Button luckyWheelBtn;
    @BindView(R.id.menu_btn_lottery)
    Button lotteryBtn;
    @BindView(R.id.menu_btn_winners_list)
    Button winnersListBtn;
    @BindView(R.id.menu_btn_profile)
    Button profileBtn;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    Toolbar toolbar;
    AdView mAdView;
    static private InterstitialAd mInterstitialAd ;
    static private String  destination;

//    private static final long START_TIME_IN_MILLIS = Constants.TIMER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        //set listener for adView to make some actions
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        AdRequest interstatialAdRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getResources().getString(R.string.home_interstatial_unit_id), interstatialAdRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        //handle interstatialAd events
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed
                                mInterstitialAd = null;
                                goToFragment(destination);

                                //the interstatialAd wasn't loaded successfuly
                                Log.e("TAG", "The ad was dismissed.");

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                mInterstitialAd = null;
                                goToFragment(destination);

                                Log.e("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                goToFragment(destination);

                                Log.e("TAG", "The ad was shown.");
                            }
                        });

                        Log.e("ad load", "onAdLoaded");

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.e("ad error", loadAdError.getMessage());

                        mInterstitialAd = null;
                    }
                });
        //initiate for the first time activity running
//        if(!(SharedPrefs.getLong(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT, Constants.TIMER+1) < START_TIME_IN_MILLIS) ||
//                (SharedPrefs.getLong(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT) == 0)
//        ){
//            SharedPrefs.save(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT, START_TIME_IN_MILLIS);
//        }
        luckyWheelFragment = new LuckyWheelFragment();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_lucky_wheel_string);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        EndDrawerToggle toggle = new EndDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LuckyWheelFragment()).commit();
            luckyWheelBtn.setPressed(true);
            lotteryBtn.setPressed(false);
            winnersListBtn.setPressed(false);
            profileBtn.setPressed(false);

            toolbar.setTitle(R.string.menu_lucky_wheel_string);

            sp.LuckyWheelBtnState = luckyWheelBtn.isPressed();
            sp.LotteryBtnState =  lotteryBtn.isPressed();
            sp.LotteryListBtnState = winnersListBtn.isPressed();
            sp.profileBtnState = profileBtn.isPressed();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        AdRequest interstatialAdRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getResources().getString(R.string.home_interstatial_unit_id), interstatialAdRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        //handle interstatialAd events
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed
                                mInterstitialAd = null;
                                goToFragment(destination);

                                //the interstatialAd wasn't loaded successfuly
                                Log.e("TAG", "The ad was dismissed.");

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                mInterstitialAd = null;
                                goToFragment(destination);

                                Log.e("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                goToFragment(destination);

                                Log.e("TAG", "The ad was shown.");
                            }
                        });

                        Log.e("ad load", "onAdLoaded");

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.e("ad error", loadAdError.getMessage());

                        mInterstitialAd = null;
                    }
                });
    }

    public void onClickMenuButton(View view){
        int id = view.getId();
        switch (id){
            case R.id.menu_btn_lucky_wheel:
            {
                destination = Constants.WHEEL_DES;
                if(mInterstitialAd != null){
                    mInterstitialAd.show(HomeActivity.this);
                }else{
                    goToFragment(destination);
                }
                break;
            }
            case R.id.menu_btn_lottery:
            {
                destination = Constants.CONTESTS_DES;
                if(mInterstitialAd != null){
                    mInterstitialAd.show(HomeActivity.this);
                }else{
                    goToFragment(destination);
                }
                break;
            }
            case R.id.menu_btn_winners_list:
            {
                destination = Constants.WINNERS_DES;
                if(mInterstitialAd != null){
                    mInterstitialAd.show(HomeActivity.this);
                }else{
                    goToFragment(destination);
                }
                break;                    }
            case R.id.menu_btn_profile:
            {
                destination = Constants.PROFILE_DES;
                if(mInterstitialAd != null){
                    mInterstitialAd.show(HomeActivity.this);
                }else{
                    goToFragment(destination);
                }
                break;
            }
            default:
                break;
        }

    }


   private void updateSP(){
       sp.LuckyWheelBtnState = luckyWheelBtn.isPressed();
       sp.LotteryBtnState =  lotteryBtn.isPressed();
       sp.LotteryListBtnState = winnersListBtn.isPressed();
       sp.profileBtnState = profileBtn.isPressed();
   }
   private void goToFragment(String fragmentName){
        switch (fragmentName){
            case Constants.PROFILE_DES:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                toolbar.setTitle(R.string.menu_profile_string);
                luckyWheelBtn.setPressed(false);
                lotteryBtn.setPressed(false);
                winnersListBtn.setPressed(false);
                profileBtn.setPressed(true);
                drawer.closeDrawer(GravityCompat.START);
                updateSP();
                break;
            }
            case Constants.WHEEL_DES:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LuckyWheelFragment()).commit();
                toolbar.setTitle(R.string.menu_lucky_wheel_string);
                luckyWheelBtn.setPressed(true);
                lotteryBtn.setPressed(false);
                winnersListBtn.setPressed(false);
                profileBtn.setPressed(false);
                drawer.closeDrawer(GravityCompat.START);
                updateSP();
                break;
            }
            case Constants.CONTESTS_DES:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LotteryFragment()).commit();
                toolbar.setTitle(R.string.menu_lottery_string);
                luckyWheelBtn.setPressed(false);
                lotteryBtn.setPressed(true);
                winnersListBtn.setPressed(false);
                profileBtn.setPressed(false);
                drawer.closeDrawer(GravityCompat.START);
                updateSP();
                break;
            }
            case Constants.WINNERS_DES:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LotteryWinnersListFragment()).commit();
                toolbar.setTitle(R.string.menu_lottery_winner_list_string);
                luckyWheelBtn.setPressed(false);
                lotteryBtn.setPressed(false);
                winnersListBtn.setPressed(true);
                profileBtn.setPressed(false);
                drawer.closeDrawer(GravityCompat.START);
                updateSP();
                break;
            }
            default:
                break;
        }
   }


    public void goToLuckyWheel(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LuckyWheelFragment()).commit();
//        Toast.makeText(HomeActivity.this, "Lucky Wheel", Toast.LENGTH_LONG).show();
        toolbar.setTitle(R.string.menu_lucky_wheel_string);
        luckyWheelBtn.setPressed(true);
        lotteryBtn.setPressed(false);
        winnersListBtn.setPressed(false);
        profileBtn.setPressed(false);


        drawer.closeDrawer(GravityCompat.START);
        sp.LuckyWheelBtnState = luckyWheelBtn.isPressed();
        sp.LotteryBtnState =  lotteryBtn.isPressed();
        sp.LotteryListBtnState = winnersListBtn.isPressed();
        sp.profileBtnState = profileBtn.isPressed();
    }
    public void goToLottery(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LotteryFragment()).commit();
//        Toast.makeText(HomeActivity.this, "Lottery", Toast.LENGTH_LONG).show();
        toolbar.setTitle(R.string.menu_lottery_string);

        luckyWheelBtn.setPressed(false);
        lotteryBtn.setPressed(true);
        winnersListBtn.setPressed(false);
        profileBtn.setPressed(false);

        drawer.closeDrawer(GravityCompat.START);
        sp.LuckyWheelBtnState = luckyWheelBtn.isPressed();
        sp.LotteryBtnState =  lotteryBtn.isPressed();
        sp.LotteryListBtnState = winnersListBtn.isPressed();
        sp.profileBtnState = profileBtn.isPressed();

    }
    public void goToWinnersList(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LotteryWinnersListFragment()).commit();
        toolbar.setTitle(R.string.menu_lottery_winner_list_string);
        luckyWheelBtn.setPressed(false);
        lotteryBtn.setPressed(false);
        winnersListBtn.setPressed(true);
        profileBtn.setPressed(false);

        drawer.closeDrawer(GravityCompat.START);
        sp.LuckyWheelBtnState = luckyWheelBtn.isPressed();
        sp.LotteryBtnState =  lotteryBtn.isPressed();
        sp.LotteryListBtnState = winnersListBtn.isPressed();
        sp.profileBtnState = profileBtn.isPressed();

    }
    public void gotoProfile(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
//        Toast.makeText(HomeActivity.this, "Profile", Toast.LENGTH_LONG).show();
        toolbar.setTitle(R.string.menu_profile_string);
        luckyWheelBtn.setPressed(false);
        lotteryBtn.setPressed(false);
        winnersListBtn.setPressed(false);
        profileBtn.setPressed(true);

        drawer.closeDrawer(GravityCompat.START);
        sp.LuckyWheelBtnState = luckyWheelBtn.isPressed();
        sp.LotteryBtnState =  lotteryBtn.isPressed();
        sp.LotteryListBtnState = winnersListBtn.isPressed();
        sp.profileBtnState = profileBtn.isPressed();

    }

}