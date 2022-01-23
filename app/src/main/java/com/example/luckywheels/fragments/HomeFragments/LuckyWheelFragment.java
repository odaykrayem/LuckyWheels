package com.example.luckywheels.fragments.HomeFragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luckywheels.HomeActivity;
import com.example.luckywheels.R;
import com.example.luckywheels.Services.CountdownService;
import com.example.luckywheels.Services.OneMinuteCountDownService;
import com.example.luckywheels.Utils.Constants;
import com.example.luckywheels.Utils.NetworkUtils;
import com.example.luckywheels.Utils.SharedPrefs;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static com.example.luckywheels.Utils.NetworkUtils.checkInternetConnection;

public class LuckyWheelFragment extends Fragment {

    private static final String[] sectors = {
            "32 red", "15 black", "19 red", "4 black",
            "21 red", "2 black", "25 red", "17 black",
            "34 red", "6 black", "27 red", "13 black",
            "36 red", "11 black", "30 red", "8 black",
            "23 red", "10 black", "5 red", "24 black",
            "16 red", "33 black", "1 red", "20 black",
            "14 red", "31 black", "9 red", "22 black",
            "18 red", "29 black", "7 red", "28 black",
            "12 red", "35 black", "3 red", "26 black",
            "zero"};
    private static final int[] pointsArray = {
            32, 15, 19, 4,
            21, 2, 25, 17,
            34, 6, 27, 13,
            36, 11, 30, 8,
            23, 10, 5, 24,
            16, 33, 1, 20,
            14, 31, 9, 22,
            18, 29, 7, 28,
            12, 35, 3, 26,
            50};

    Button spinBtn;
    TextView resultTV;
    ImageView wheelIV;
    private TextView countdownTV;

    //we create a random instance to make our wheel spin randomly
    private static final Random RANDOM = new Random();
    private int degree = 0, degreeOld = 0;

    //we have 37 sectors on the wheel, we divide 360 by this value to have angle for each sector,
    //wee divide by two to have a half sector
    private static final float HALF_SECTOR = 360f / 37f / 2f;

    private String TAG = "L_W fragment";

//    RewardedAd mRewardedAd;
    static private InterstitialAd mInterstitialAd ;
    AdRequest interstatialAdRequest;
//    Intent intent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lucky_wheel, container, false);
        spinBtn = view.findViewById(R.id.btn_spin);
        resultTV = view.findViewById(R.id.tv_result);
        wheelIV = view.findViewById(R.id.iv_wheel);
        countdownTV = view.findViewById(R.id.tv_countdown);
//        if (SharedPrefs.getBoolean(getContext(), SharedPrefs.SPIN_TIMER_STATE)) {
//            spinBtn.setVisibility(View.INVISIBLE);
//            countdownTV.setVisibility(View.VISIBLE);
//            Intent intent = new Intent(getContext(), CountdownService.class);
////            getActivity().startService(intent);
//            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));
//            SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, true);
//
//        } else {
//            spinBtn.setVisibility(View.VISIBLE);
//            countdownTV.setVisibility(View.INVISIBLE);
//        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (SharedPrefs.getBoolean(getContext(), SharedPrefs.SPIN_TIMER_STATE)) {
            spinBtn.setVisibility(View.INVISIBLE);
            countdownTV.setVisibility(View.VISIBLE);
            if(SharedPrefs.getString(getContext(),SharedPrefs.TIME_DOWN_COUNTER_TYPE,Constants.TIMER_TYPE_ONE_M).equals(Constants.TIMER_TYPE_ONE_D) ){
                Log.e("one day view created", String.valueOf(SharedPrefs.getString(getContext(),SharedPrefs.TIME_DOWN_COUNTER_TYPE,Constants.TIMER_TYPE_ONE_M).equals(Constants.TIMER_TYPE_ONE_D) ));
                Intent intent = new Intent(getContext(), CountdownService.class);
                getActivity().startService(intent);
                getActivity().registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));
            }else{
                Intent intent = new Intent(getContext(), OneMinuteCountDownService.class);
                getActivity().startService(intent);
                getActivity().registerReceiver(broadcastReceiver, new IntentFilter(OneMinuteCountDownService.BROADCAST_ACTION));
            }
            SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, true);
        } else {
            spinBtn.setVisibility(View.VISIBLE);
            countdownTV.setVisibility(View.INVISIBLE);
        }
        interstatialAdRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getContext(),getResources().getString(R.string.wheel_interstatial_unit_id), interstatialAdRequest,
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
                                            Log.e("TAG", "The ad was dismissed.");
                                        }
                                        @Override
                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                            // Called when fullscreen content failed to show.
                                            mInterstitialAd = null;
                                            Log.e("TAG", "The ad failed to show.");
                                        }
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;

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

        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.checkInternetConnection(getContext())){
                    if(mInterstitialAd != null ){
                        if(SharedPrefs.getInt(getContext(),SharedPrefs.CLICKS_NUMBER,0) == Constants.CLICKS_TIMES){
                            SharedPrefs.save(getContext(),SharedPrefs.CLICKS_NUMBER, 0);
                            SharedPrefs.save(getContext(),SharedPrefs.TIME_DOWN_COUNTER_TYPE, Constants.TIMER_TYPE_ONE_D);
                            Toast.makeText(getContext(), "ONE DAy", Toast.LENGTH_SHORT).show();
                            mInterstitialAd.show(getActivity());
                            Intent intent = new Intent(getContext(), CountdownService.class);
                            getActivity().startService(intent);
                            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));
                        }else{
                            int clicks = SharedPrefs.getInt(getContext(),SharedPrefs.CLICKS_NUMBER,0) + 1;
                            SharedPrefs.save(getContext(),SharedPrefs.CLICKS_NUMBER, clicks);
                            SharedPrefs.save(getContext(),SharedPrefs.TIME_DOWN_COUNTER_TYPE, Constants.TIMER_TYPE_ONE_M);

                            mInterstitialAd.show(getActivity());
                            Intent intent = new Intent(getContext(), OneMinuteCountDownService.class);
                            getActivity().startService(intent);
                            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(OneMinuteCountDownService.BROADCAST_ACTION));

                        }
                        SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, true);
                        spinBtn.setVisibility(View.INVISIBLE);
                        countdownTV.setVisibility(View.VISIBLE);
                        startRotationAnimation(wheelIV);

                    }else{
                        Toast.makeText(getActivity(), R.string.wait_for_ad, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getContext(), CountdownService.class);
//                    getActivity().startService(intent);
//                    getActivity().registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));
//                    SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, true);
//
//                    spinBtn.setVisibility(View.INVISIBLE);
//                    countdownTV.setVisibility(View.VISIBLE);
//
////                Toast.makeText(getContext(), SharedPrefs.getLong(getContext(), SharedPrefs.TIMER_TIME_LEFT) + "", Toast.LENGTH_SHORT).show();
//                    startRotationAnimation(wheelIV);
                    }
                }else{
                    Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
                }


            }

        });
    }

    private void startRotationAnimation(ImageView iv) {
        degreeOld = degree % 360;
        //we calculate random angel for rotation of our wheel
        degree = RANDOM.nextInt(360) + 720;
        //rotation effect o the center of the wheel
        RotateAnimation rotateAnimation = new RotateAnimation(degreeOld, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(7200);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // we empty the result text view when the animation start
                resultTV.setText("");
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                //we display the correct sector pointed by the triangle at the end of the rotation
//                resultTV.setText(getSector(360 - (degree % 360)));
                String textResult =  " لقد ربحت " +String.valueOf(getPointsFromSector(360 - (degree % 360))) +" نقطة " ;
                        resultTV.setText(textResult);
                Toast.makeText(getContext(), textResult, Toast.LENGTH_SHORT).show();
                if(checkInternetConnection(getContext())){
                    addPointsToUser(getPointsFromSector(360 - (degree % 360)));
                }else{
                    Toast.makeText(getContext(), R.string.connection_points_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        //we start the animation
        iv.startAnimation(rotateAnimation);
    }


    private void addPointsToUser(int extraPoints) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkUtils.EDIT_POINTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString("error");
//                            String  message = jsonObject.getString("message");

                            if (error.equals("true")) {
                                Toast.makeText(getContext(), R.string.try_again, Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error1", "Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
                            Toast.makeText(getContext(), "Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Register Error2! " + error.toString()
                                + "\nCause: " + error.getCause()
                                + "\nmessage: " + error.getMessage()
                        );

                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(SharedPrefs.getInt(getContext(),SharedPrefs.KEY_USER_ID)));
                params.put("edit_points_operation", "increase");
                params.put("points", String.valueOf(extraPoints));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
//    private String getSector(int degrees) {
//        int i = 0;
//        String text = null;
//        do {
//            //start and end of each sector on the wheel
//            float start = HALF_SECTOR * (i * 2 + 1);
//            float end = HALF_SECTOR * (i * 2 + 3);
//            if (degrees >= start && degrees < end) {
//                // degrees are in the [start;end[
//                //so text is equal to sectors[i];
////                text = sectors[i];
//                text = sectors[i];
//
//            }
//            i++;
//        } while (text == null && i < sectors.length);
//        return text;
//    }
    private int getPointsFromSector(int degrees) {
        int i = 0;
        int points = -1;
        do {
            //start and end of each sector on the wheel
            float start = HALF_SECTOR * (i * 2 + 1);
            float end = HALF_SECTOR * (i * 2 + 3);
            if (degrees >= start && degrees < end) {
                // degrees are in the [start;end[
                //so text is equal to sectors[i];
                points = pointsArray[i];
            }
            i++;
        } while (points < 0 && i < pointsArray.length);
        return points;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        Long time = 0L;
        boolean isTimerFinished;
        if (intent.getExtras() != null) {
            time = intent.getLongExtra("time", Constants.TIMER);
            isTimerFinished = intent.getBooleanExtra("timer_finish", false);
            if (!isTimerFinished) {
                int hours = (int) ((time / 1000) / 60) / 60;
                int minutes = (int) (time / 1000 / 60) % 60;
                int seconds = (int) (time / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                countdownTV.setText(timeLeftFormatted);
                Log.e(TAG, "Time remaining Shared Prefs:" + time);
                SharedPrefs.save(getContext(), SharedPrefs.TIMER_TIME_LEFT, time);
                spinBtn.setVisibility(View.INVISIBLE);
                spinBtn.setEnabled(false);
                countdownTV.setVisibility(View.VISIBLE);
            } else {
//                Toast.makeText(getContext(), "btn is visible", Toast.LENGTH_LONG).show();
                getActivity().unregisterReceiver(broadcastReceiver);
                SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, false);
                if(SharedPrefs.getString(getContext(),SharedPrefs.TIME_DOWN_COUNTER_TYPE)==Constants.TIMER_TYPE_ONE_D){
                    getActivity().stopService(new Intent(getContext(), CountdownService.class));
                }else{
                    getActivity().stopService(new Intent(getContext(), OneMinuteCountDownService.class));
                }
                spinBtn.setVisibility(View.VISIBLE);
                spinBtn.setEnabled(true);
                countdownTV.setVisibility(View.INVISIBLE);
                return;
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(SharedPrefs.getString(getContext(),SharedPrefs.TIME_DOWN_COUNTER_TYPE)==Constants.TIMER_TYPE_ONE_D){
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));
        }else{
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(OneMinuteCountDownService.BROADCAST_ACTION));
        }
        SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, true);

        interstatialAdRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getContext(),getResources().getString(R.string.wheel_interstatial_unit_id), interstatialAdRequest,
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
//                                            Intent intent = new Intent(getContext(), CountdownService.class);
//                                            getActivity().startService(intent);
//                                            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));
//                                            SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, true);
//
//                                            spinBtn.setVisibility(View.INVISIBLE);
//                                            countdownTV.setVisibility(View.VISIBLE);

//                             Toast.makeText(getContext(), SharedPrefs.getLong(getContext(), SharedPrefs.TIMER_TIME_LEFT) + "", Toast.LENGTH_SHORT).show();
//                                            startRotationAnimation(wheelIV);
                                //the interstatialAd wasn't loaded successfuly
                                Log.e("TAG", "The ad was dismissed.");

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                mInterstitialAd = null;
                                Log.e("TAG", "The ad failed to show.");

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;

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
        Log.i(TAG, "Register broadcast receiver");
    }

    @Override
    public void onStart() {
        if(SharedPrefs.getString(getContext(),SharedPrefs.TIME_DOWN_COUNTER_TYPE)==Constants.TIMER_TYPE_ONE_D){
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(CountdownService.BROADCAST_ACTION));

        }else{
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(OneMinuteCountDownService.BROADCAST_ACTION));

        }
        SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, true);

        interstatialAdRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(),getResources().getString(R.string.wheel_interstatial_unit_id), interstatialAdRequest,
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
                                Log.e("TAG", "The ad was dismissed.");

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                mInterstitialAd = null;
                                Log.e("TAG", "The ad failed to show.");

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;

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
        Log.i(TAG, "Register broadcast receiver");
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
          if(SharedPrefs.getBoolean(getContext(), SharedPrefs.RECEIVER_STATE)){
              getActivity().unregisterReceiver(broadcastReceiver);
              SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, false);

              AdRequest interstatialAdRequest = new AdRequest.Builder().build();

              InterstitialAd.load(getContext(),getResources().getString(R.string.wheel_interstatial_unit_id), interstatialAdRequest,
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
                                      mInterstitialAd = null;
                                      Log.e("TAG", "The ad was dismissed.");
                                  }
                                  @Override
                                  public void onAdFailedToShowFullScreenContent(AdError adError) {
                                      mInterstitialAd = null;
                                      Log.e("TAG", "The ad failed to show.");
                                  }
                                  @Override
                                  public void onAdShowedFullScreenContent() {
                                      mInterstitialAd = null;
                                      Log.e("TAG", "The ad was shown.");
                                  }
                              });

                              Log.e("ad load", "onAdLoaded");
                          }
                          @Override
                          public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                              Log.e("ad error", loadAdError.getMessage());
                              mInterstitialAd = null;
                          }
                      });
              Log.i(TAG, "Unregister broadcast receiver");
          }
    }

    @Override
    public void onStop() {
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
            SharedPrefs.savePref(getContext(), SharedPrefs.RECEIVER_STATE, false);

        } catch (Exception e) {
            // receiver was probably unregistered
        }
        super.onStop();

    }

    @Override
    public void onDestroy() {
//        getActivity().stopService(new Intent(getContext(), CountdownService.class));
        Log.i(TAG, "Service stopped");
        super.onDestroy();

    }

}
