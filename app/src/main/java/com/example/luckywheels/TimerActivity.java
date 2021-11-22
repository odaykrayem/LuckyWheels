package com.example.luckywheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luckywheels.Services.CountdownService;
import com.example.luckywheels.Utils.Constants;
import com.example.luckywheels.Utils.SharedPrefs;

import java.util.Locale;

import butterknife.BindView;

public class TimerActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = Constants.TIMER;

    private TextView mTvCountdown;
    private Button mBtnStartPause;
    private Button mBtnReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    //
    Intent intent;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private String TAG = "Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(intent != null){
//            stopService(intent);
//        }

        setContentView(R.layout.activity_timer);
        //initiate for the first time activity running
//        if(!(SharedPrefs.getLong(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT, Constants.TIMER+1) < START_TIME_IN_MILLIS)
//        || (SharedPrefs.getLong(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT) == 0)){
//            SharedPrefs.save(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT, START_TIME_IN_MILLIS);
//        }

        mTvCountdown = findViewById(R.id.tv_countdown);
        mBtnStartPause = findViewById(R.id.btn_start_pause);
        mBtnReset = findViewById(R.id.btn_reset);

     mBtnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
//                if(mTimerRunning){
//                    pauseTimer();
//                }else{
//                    startTimer();
//                }
            }
        });
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

    }
    private void startTimer(){
//        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                mTimeLeftInMillis = millisUntilFinished;
//                updateCountdownText();
//            }
//
//            @Override
//            public void onFinish() {
//                mTimerRunning = false;
//                mBtnStartPause.setText("Start");
//                // we can't start timer on zero
//                mBtnStartPause.setVisibility(View.INVISIBLE);
//                mBtnReset.setVisibility(View.VISIBLE);
//
//            }
//        }.start();
        mTimerRunning = true;
        mBtnStartPause.setText("pause");
        mBtnReset.setVisibility(View.VISIBLE);
        intent = new Intent(TimerActivity.this, CountdownService.class);
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));
        Log.i(TAG, "Service started");

    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        String time;
        if(intent.getBooleanExtra("timer_finish", false) ){
            Toast.makeText(this, "btn is visible", Toast.LENGTH_LONG).show();
//            unregisterReceiver(broadcastReceiver);
            return;
        }else{
             time = intent.getStringExtra("time");
        }
        Log.d("Hello", "Time " + time);
        mTvCountdown.setText(time);
    }

    private void updateCountdownText() {
        int hours = (int) ((mTimeLeftInMillis/1000) / 60) / 60 ;
        int minutes = (int) (mTimeLeftInMillis / 1000 / 60 ) % 60;
        int seconds = (int) (mTimeLeftInMillis / 1000 ) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);

        mTvCountdown.setText(timeLeftFormatted);

    }

    private void pauseTimer(){
//       mCountDownTimer.cancel();
       mTimerRunning = false;
       mBtnStartPause.setText("Start");
       mBtnReset.setVisibility(View.VISIBLE);
        stopService(intent);
    }
    private void resetTimer(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
//        updateCountdownText();
        mBtnReset.setVisibility(View.INVISIBLE);
        mBtnStartPause.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));
//        stopService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(broadcastReceiver);

    }

    @Override
    protected void onStop() {
        super.onStop();
//        registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));
//        startService(intent);
    }
}