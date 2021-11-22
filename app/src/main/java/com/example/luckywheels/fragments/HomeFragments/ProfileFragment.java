package com.example.luckywheels.fragments.HomeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luckywheels.BuildConfig;
import com.example.luckywheels.EnterScreenActivity;
import com.example.luckywheels.R;
import com.example.luckywheels.Utils.NetworkUtils;
import com.example.luckywheels.Utils.SharedPrefs;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String PROFILE_TAG = "profile_tag";
    MaterialTextView nameTV, emailTV, balanceTV, pointsTV, refCodeTV, refCodeTimesTV;
    Button logoutBtn;
    ImageButton copyBtn;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int userId, balance, points, refCodeTimes;
    String userName,email,refCode;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = SharedPrefs.getInt(getContext(),SharedPrefs.KEY_USER_ID);
        userName =   SharedPrefs.getString(getContext(),SharedPrefs.LOGGED_IN_USER_NAME);
        email =   SharedPrefs.getString(getContext(),SharedPrefs.KEY_USER_EMAIL);
        refCode = SharedPrefs.getString(getContext(),SharedPrefs.KEY_USER_REF_CODE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameTV = view.findViewById(R.id.profile_name_tv);
        emailTV = view.findViewById(R.id.profile_eamil_tv);
        balanceTV = view.findViewById(R.id.profile_balance_tv);
        pointsTV = view.findViewById(R.id.profile_points_tv);
        refCodeTV = view.findViewById(R.id.profile_ref_code_tv);
        refCodeTimesTV = view.findViewById(R.id.profile_ref_code_times_tv);
        logoutBtn = view.findViewById(R.id.logout_btn);
        copyBtn = view.findViewById(R.id.copy_btn);

        nameTV.setText(SharedPrefs.getString(getContext(),SharedPrefs.KEY_USER_FIRST_NAME));
        emailTV.setText(SharedPrefs.getString(getContext(),SharedPrefs.KEY_USER_EMAIL));
        refCodeTV.setText(SharedPrefs.getString(getContext(),SharedPrefs.KEY_USER_REF_CODE));

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                updateUserDataFromNetwork();
            }
        });


        return  view;
    }


    private void updateUserDataFromNetwork() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkUtils.GET_USER_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString("error");
                            String message = jsonObject.getString("message");
                            Log.e("ddd",jsonObject.toString());
                            if(error.equals("false")) {
                                int balance =  jsonObject.getInt("balance");
                                Log.e(PROFILE_TAG, String.valueOf(jsonObject.getInt("balance")));
                                int points =  jsonObject.getInt("points");
                                int refTimes =  jsonObject.getInt("ref_times");

                                SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_BALANCE, balance );
                                SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_POINTS, points );
                                SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_REF_TIMES, refTimes );

                                balanceTV.setText(String.valueOf(balance));
                                pointsTV.setText(String.valueOf(points));
                                refCodeTimesTV.setText(String.valueOf(refTimes));

                                mSwipeRefreshLayout.setRefreshing(false);

                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                mSwipeRefreshLayout.setRefreshing(false);

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "update data Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "update data Error2! " + error.toString()
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_ID)));
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1500,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUserDataFromNetwork();
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefs.savePref(getContext(), SharedPrefs.KEY_LOG_IN_STATE, false);
                Intent intent = new Intent(getContext(), EnterScreenActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               share();
            }
        });
    }

    @Override
    public void onRefresh() {
        updateUserDataFromNetwork();
    }
    public void share(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //todo add the google play store link to the share message
        String shareBody1 = "Hey, I am . If you want to read it download the app.\n\n*" + "*\n\n*Read on $app name eBooks:*\n$play store link";
//        + "market://details?id=" + BuildConfig.APPLICATION_ID +
        String shareBody =   "مرحباً، أنا استخدم هذا التطبيق للعب والربح بشكل حقيقي، وأنت أيضاً يمكنك البدء بتحقيق الأرباح من خلال تحميل التطبيق عبر الرابط:" +"\n"+
                "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID + "\n"+  "وهذا كود الإحالة الخاص بي يمكنك استخدامه عند إنشاء حسابك الجديد:" + "\n\n"+ SharedPrefs.getString(getContext(),SharedPrefs.KEY_USER_REF_CODE);

        String shareSubject = " ** دولاب الحظ **";

        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);


        getContext().startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }
}
