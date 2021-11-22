package com.example.luckywheels.fragments.HomeFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luckywheels.Adapters.WinnersListAdapter;
import com.example.luckywheels.Models.WinnerModel;
import com.example.luckywheels.R;
import com.example.luckywheels.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LotteryWinnersListFragment extends Fragment {

    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view
    private ArrayList<WinnerModel> winnerModelArrayList;
    private WinnersListAdapter winnersListAdapter;
    private RecyclerView winnerListRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;

    private final String TAG = "LW List";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lottery_winner_list,container,false);
        // creating a new array list.
        winnerModelArrayList = new ArrayList<>();

        winnerListRV = view.findViewById(R.id.rv_winners_list);
        loadingPB = view.findViewById(R.id.idPBLoading);
        nestedSV = view.findViewById(R.id.idNestedSV);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromAPI();

    }
    private void getDataFromAPI() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // creating a variable for our json object request and passing our url to it.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkUtils.WINNERS_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            System.out.println(message);
                            String code = jsonObject.getString("code");
                            JSONArray operations =  jsonObject.getJSONArray("data");
                            if(code.equals("200")) {
                                for (int i = 0; i < operations.length(); i++) {
                                    JSONObject object = operations.getJSONObject(i);
                                    boolean status = object.getString("status").equals("1");
                                    // on below line we are extracting data from our json object.
                                    winnerModelArrayList.add(new WinnerModel(
                                            object.getString("name"),
                                            object.getString("prize"),
                                            object.getString("date")));

                                    Log.e(TAG, object.toString());
                                }

                            }
                            // passing array list to our adapter class.
                            winnersListAdapter = new WinnersListAdapter(winnerModelArrayList, getContext());

                            // setting layout manager to our recycler view.
                            winnerListRV.setLayoutManager(new LinearLayoutManager(getContext()));

                            // setting adapter to our recycler view.
                            winnerListRV.setAdapter(winnersListAdapter);
                            loadingPB.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            loadingPB.setVisibility(View.GONE);
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
                loadingPB.setVisibility(View.GONE);

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
                return parameters;
            }
        };
        //adding our string request to queue
        queue.add(stringRequest);
    }
}
