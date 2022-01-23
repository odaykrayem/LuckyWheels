package com.example.luckywheels.fragments.HomeFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
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
import com.example.luckywheels.Adapters.WinnersAdapter;
import com.example.luckywheels.Adapters.WinnersListAdapter;
import com.example.luckywheels.Models.ParticipantModel;
import com.example.luckywheels.Models.WinnerModel;
import com.example.luckywheels.R;
import com.example.luckywheels.Utils.NetworkUtils;
import com.example.luckywheels.Utils.SharedPrefs;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class LotteryWinnersListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view
    private ArrayList<WinnerModel> winnerModelArrayList;
    private WinnersListAdapter winnersListAdapter;
    private RecyclerView winnerListRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout note;
    Toast toast;
    private ArrayList<ParticipantModel> winnersModelArrayList;
    private WinnersAdapter winnersAdapter;
//    AdView mAdView;
 private View view;

    final String TAG = "winners_FR";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lottery_winner_list,container,false);
        this.view = view;
        winnersModelArrayList = new ArrayList<>();
        note = view.findViewById(R.id.note);
        winnerListRV = view.findViewById(R.id.rv_winners);
        nestedSV = view.findViewById(R.id.nested_sV);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                getWinners(view);

            }
        });

        LayoutInflater inflater1 = getLayoutInflater();
        View layout = inflater1.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.toast_layout_root));

//        ImageView image = (ImageView) layout.findViewById(R.id.image);
//        image.setImageResource(R.drawable.ic_contest_money);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(getContext().getResources().getText(R.string.winner_toast));

        toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        return view;
    }

    private void getWinners(View view) {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // creating a variable for our json object request and passing our url to it.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkUtils.GET_WINNERS_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        winnersModelArrayList.clear();
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            String code = jsonObject.getString("error");
                            if (code.equals("false")) {
                                JSONArray operations = jsonObject.getJSONArray("list");
                                if(getContext() != null){
                                    Toast.makeText(getContext(), R.string.get_winners, Toast.LENGTH_LONG).show();
                                }
                                for (int i = 0; i < operations.length(); i++) {
                                    JSONObject object = operations.getJSONObject(i);
                                    Log.e(TAG, object.toString());
                                    // on below line we are extracting data from our json object.
                                    winnersModelArrayList.add(new ParticipantModel(
                                            object.getString("user_name"),
                                            object.getInt("user_id"),
                                            object.getString("email"),
                                            object.getInt("prize"),
                                            object.getString("draw_date")));
                                    System.out.println("jsonObject" + object.toString());
                                    if(view != null){
                                        if(object != null){
                                            if(getContext() != null){
                                                if(object.getInt("user_id") == SharedPrefs.getInt(getContext(),SharedPrefs.KEY_USER_ID)){
                                                    final KonfettiView konfettiView = view.findViewById(R.id.viewKonfetti);

//                                            Toast.makeText(getContext(), "u are winner", Toast.LENGTH_SHORT).show();
                                                    toast.show();

                                                    konfettiView.build()
                                                            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                                                            .setDirection(0.0, 359.0)
                                                            .setSpeed(1f, 3f)
                                                            .setFadeOutEnabled(true)
                                                            .setTimeToLive(2000L)
                                                            .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                                                            .addSizes(new Size(12, 5f))
                                                            .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                                                            .streamFor(200, 2000L);
                                                }
                                            }
                                        }
                                    }

                                }
                                if (winnersModelArrayList.size() > 0) {
                                    note.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);

                            // passing array list to our adapter class.
                            winnersAdapter = new WinnersAdapter(winnersModelArrayList, getContext());

                            // setting layout manager to our recycler view.
                            winnerListRV.setLayoutManager(new LinearLayoutManager(getContext()));

                            // setting adapter to our recycler view.
                            winnerListRV.setAdapter(winnersAdapter);

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWinners(view);
        //initialize for ads
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //bind view and load banner ads for it

    }

    @Override
    public void onRefresh() {

        getWinners(view);
    }


}
