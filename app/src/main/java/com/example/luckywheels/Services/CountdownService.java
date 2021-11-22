package com.example.luckywheels.Services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.example.luckywheels.Utils.Constants;
import com.example.luckywheels.Utils.SharedPrefs;

public class CountdownService extends Service {

    public static final String BROADCAST_ACTION = "com.example.luckywheels.Services";
    private Intent intent = new Intent(BROADCAST_ACTION);
    private String TAG = "BroadcastService";
    private Handler handler = new Handler();
    //    private long initial_time;
    private static final long START_TIME_IN_MILLIS = Constants.TIMER;
    private long mTimeLeftInMillis;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Starting Timer..");

        if((SharedPrefs.getLong(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT) < START_TIME_IN_MILLIS) &&
                (SharedPrefs.getLong(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT) != 0)){
            mTimeLeftInMillis = SharedPrefs.getLong(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT);

        }else{
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            SharedPrefs.save(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT, mTimeLeftInMillis);
        }

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                SharedPrefs.savePref(getApplicationContext(), SharedPrefs.SPIN_TIMER_STATE, true);
                SharedPrefs.save(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT, mTimeLeftInMillis);

                intent.putExtra("time", millisUntilFinished);
                intent.putExtra("timer_finish", false);
                sendBroadcast(intent);
                Log.e(TAG, "Time remaining" + mTimeLeftInMillis);

            }

            @Override
            public void onFinish() {
                SharedPrefs.save(getApplicationContext(), SharedPrefs.TIMER_TIME_LEFT, START_TIME_IN_MILLIS);
                SharedPrefs.savePref(getApplicationContext(), SharedPrefs.SPIN_TIMER_STATE, false);
                intent.putExtra("timer_finish", true);
                sendBroadcast(intent);
                Log.e("timer finish ", "timer finish in service");
            }
        }.start();
    }


//    private Runnable sendUpdatesToUI = new Runnable() {
//        public void run() {
//            DisplayLoggingInfo();
//            handler.postDelayed(this, 1000); // 1 seconds
//        }
//    };
//
//    private void DisplayLoggingInfo() {
//        int hours = (int) ((mTimeLeftInMillis / 1000) / 60) / 60;
//        int minutes = (int) (mTimeLeftInMillis / 1000 / 60) % 60;
//        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
//
//        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
//
//
//    }

    @Override
    public void onDestroy() {
        mCountDownTimer.cancel();
        super.onDestroy();
//        handler.removeCallbacks(sendUpdatesToUI);
        SharedPrefs.savePref(getApplicationContext(), SharedPrefs.SPIN_TIMER_STATE, false);
        Log.e(TAG, "B-Service destroyed");
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


}