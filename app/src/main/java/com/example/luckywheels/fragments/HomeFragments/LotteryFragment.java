package com.example.luckywheels.fragments.HomeFragments;

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
import com.example.luckywheels.Models.ContestModel;
import com.example.luckywheels.R;
import com.example.luckywheels.Utils.NetworkUtils;
import com.example.luckywheels.Utils.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LotteryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView mContestsRV;
    ContestsAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout note;
    final String TAG = "contests_FR";

    //todo : you just need to get this from server ....the rest is handled by mAdapter :)
    ArrayList<ContestModel> contestsArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lottery,container,false);
        mContestsRV = view.findViewById(R.id.contests_rv);
        contestsArrayList = new ArrayList<>();
        note = view.findViewById(R.id.note);

        // notice that the spanCount here is the number of columns you wish to make in this grid
        // if you want to make this more professional you can calculate the exact number of
        // columns screen can handle depending on screen width and item width but I took the fastest way @_@
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false);

        //here we're telling the recycler view that we will organize the items depending on this layout manager (so it should be in a grid)
        mContestsRV.setLayoutManager(layoutManager);
        //todo this one should be called after data change or when data arrive from an api call
        mContestsRV.setAdapter(mAdapter);
        mSwipeRefreshLayout =  view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                getContests();

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getContests();
    }

    private void getContests() {
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

                            JSONArray contests = jsonObject.getJSONArray("list");
                            if (error.equals("false")) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                for (int i = 0; i < contests.length(); i++) {
                                    JSONObject object = contests.getJSONObject(i);
                                    Log.e(TAG, object.toString());
                                    // on below line we are extracting data from our json object.
                                    contestsArrayList.add(new ContestModel(
                                            object.getInt("id"),
                                            object.getInt("prize"),
                                            object.getString("draw_date"),
                                            object.getBoolean("is_participant")));
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
                            mAdapter = new ContestsAdapter(getContext(), contestsArrayList);

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
                parameters.put("user_id", String.valueOf(SharedPrefs.getInt(getContext(),SharedPrefs.KEY_USER_ID)));
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
        getContests();
    }
}