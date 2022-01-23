package com.example.luckywheels.fragments.HomeFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luckywheels.Adapters.ContestsAdapter;
import com.example.luckywheels.HomeActivity;
import com.example.luckywheels.Models.ContestModel;
import com.example.luckywheels.R;
import com.example.luckywheels.Utils.NetworkUtils;
import com.example.luckywheels.Utils.SharedPrefs;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LotteryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ContestsAdapter.ItemClickListener {

    RecyclerView mContestsRV;
    ContestsAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout note;
    String user_id;
    RewardedAd mRewardedAd;
    AdRequest adRequest;
    ProgressDialog pDialog;
    final String TAG = "contests_FR";

    //todo : you just need to get this from server ....the rest is handled by mAdapter :)
    ArrayList<ContestModel> contestsArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_id = String.valueOf(SharedPrefs.getInt(getActivity(), SharedPrefs.KEY_USER_ID));
        adRequest = new AdRequest.Builder().build();
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
//                loadRewardAd(contestModel, user_id);
                pDialog = new ProgressDialog(getContext());
                pDialog.setMessage("Processing Please wait...");
                pDialog.show();
                loadRewardAd();
//                Log.e(TAG, String.valueOf(contestModel.getId()));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lottery, container, false);
        mContestsRV = view.findViewById(R.id.contests_rv);
        contestsArrayList = new ArrayList<>();
        note = view.findViewById(R.id.note);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);

        //here we're telling the recycler view that we will organize the items depending on this layout manager (so it should be in a grid)
        mContestsRV.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                getContests(user_id);

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getContests(user_id);
    }

    //    private void loadRewardAd(int contest_id,String user_id){
//        RewardedAd.load(getActivity(), getResources().getString(R.string.contests_rewarded_ad_unit_id),
//                adRequest, new RewardedAdLoadCallback() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error.
//                        Log.d(TAG, loadAdError.getMessage());
//                        mRewardedAd = null;
//                    }
//
//                    @Override
//                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                        mRewardedAd = rewardedAd;
//                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                            @Override
//                            public void onAdShowedFullScreenContent() {
//                                // Called when ad is shown.
//                                Log.e(TAG, "Ad was shown.");
//                                mRewardedAd = null;
////                                addUserToParticipantList(contestModel.getId());
//                            }
//
//                            @Override
//                            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                // Called when ad fails to show.
//                                Log.d(TAG, "Ad failed to show.");
//
//                            }
//
//                            @Override
//                            public void onAdDismissedFullScreenContent() {
//                                // Called when ad is dismissed.
//                                // Set the ad reference to null so you don't show the ad a second time.
//                                Log.d(TAG, "Ad was dismissed.");
//                                addUserToParticipantList(contest_id, user_id);
//                                mRewardedAd = null;
//                            }
//
//                        });
//                        Log.d(TAG, "Ad was loaded.");
//                    }
//                });
//    }
    private void loadRewardAd() {
        RewardedAd.load(getActivity(), getResources().getString(R.string.contests_rewarded_ad_unit_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        pDialog.dismiss();
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.e(TAG, "Ad was shown.");
                                mRewardedAd = null;
//                                addUserToParticipantList(contestModel.getId());
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                            }

                        });
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }

    private void getContests(String user_id) {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // creating a variable for our json object request and passing our url to it.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkUtils.GET_ALL_CONTESTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        contestsArrayList.clear();
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString("error");
                            String message = jsonObject.getString("message");

                            if (error.equals("false")) {
                                JSONArray contests = jsonObject.getJSONArray("list");
                                if (getContext() != null) {
                                    Toast.makeText(getContext(), R.string.get_contest, Toast.LENGTH_LONG).show();

                                }
                                for (int i = 0; i < contests.length(); i++) {
                                    JSONObject object = contests.getJSONObject(i);
                                    Log.e(TAG, object.toString());
                                    // on below line we are extracting data from our json object.
                                    contestsArrayList.add(new ContestModel(
                                            object.getInt("id"),
                                            object.getInt("prize"),
                                            object.getString("draw_date"),
                                            object.getString("draw_time")));
                                    System.out.println("jsonObject" + object.toString());
                                }
                                if (contestsArrayList.size() > 0) {
                                    note.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);

                            // passing array list to our adapter class.
                            mAdapter = new ContestsAdapter(getContext(), contestsArrayList, LotteryFragment.this);

                            // setting layout manager to our recycler view.
                            mContestsRV.setLayoutManager(new LinearLayoutManager(getContext()));

                            // setting adapter to our recycler view.
                            mContestsRV.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            mSwipeRefreshLayout.setRefreshing(false);

                            Toast.makeText(getContext(), "Fail to get data.." + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("error1:::" + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);

                System.out.println("error2" + error.toString()

                        + "\nCause " + error.getCause()
                        + "\nmessage" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id", user_id);
                return parameters;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our string request to queue
        queue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        getContests(user_id);
    }

    @Override
    public void itemClicked(int contest_id) {
        Context context = getContext();
        if (!NetworkUtils.checkInternetConnection(context)) {
            Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
        } else {
            if (mRewardedAd != null) {

                mRewardedAd.show(getActivity(), rewardItem -> {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
//                            int rewardAmount = rewardItem.getAmount();
//                            String rewardType = rewardItem.getType();
                });
                addUserToParticipantList(contest_id);


            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
                Toast.makeText(context, R.string.wait_for_ad, Toast.LENGTH_SHORT).show();
                loadRewardAd();
            }


        }
    }
    private void addUserToParticipantList(int contest_id) {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // creating a variable for our json object request and passing our url to it.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkUtils.MAKE_USER_PARTICIPANT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString("error");
                            String message = jsonObject.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {

                            Toast.makeText(getContext(), "Fail to get data.." + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("error1:::" + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // handling on error listener method.
                Toast.makeText(getContext(), "Fail to get data.." + error.toString()

                        + "\nCause " + error.getCause()
                        + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("error2" + error.toString()

                        + "\nCause " + error.getCause()
                        + "\nmessage" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id", user_id);
                Log.e(TAG, String.valueOf(SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_ID)));
                parameters.put("contest_id", String.valueOf(contest_id));
                return parameters;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our string request to queue
        queue.add(stringRequest);

    }

}