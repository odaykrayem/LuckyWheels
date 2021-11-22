package com.example.luckywheels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
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

    private static final long START_TIME_IN_MILLIS = Constants.TIMER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        //initiate for the first time activity running
        if(!(SharedPrefs.getLong(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT, Constants.TIMER+1) < START_TIME_IN_MILLIS) ||
                (SharedPrefs.getLong(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT) == 0)
        ){
            SharedPrefs.save(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT, START_TIME_IN_MILLIS);
        }
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

    public void goToLuckyWheel(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LuckyWheelFragment()).commit();
        Toast.makeText(HomeActivity.this, "Lucky Wheel", Toast.LENGTH_LONG).show();
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
        Toast.makeText(HomeActivity.this, "Lottery", Toast.LENGTH_LONG).show();
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
        Toast.makeText(HomeActivity.this, "Winners List", Toast.LENGTH_LONG).show();
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
        Toast.makeText(HomeActivity.this, "Profile", Toast.LENGTH_LONG).show();
        toolbar.setTitle(R.string.menu_lottery_winner_list_string);
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