package com.example.luckywheels.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import com.example.gridsample.R;
//import com.example.gridsample.model.User;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class ContestsAdapter extends RecyclerView.Adapter<ContestsAdapter.ViewHolder>{

    Context context;
    private ArrayList<ContestModel> contests;
    static String TAG = "Contests Adapter";

    public ContestsAdapter(Context context, ArrayList<ContestModel> contests) {
        this.context = context;
        this.contests = contests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.contest_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ContestModel contestModel = contests.get(position);

//        if(contestModel. == null){
//            holder.userImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_person_24));
//        }else{
//            Glide.with(context)
//                    .load(user.getImageUrl())
//                    .into(holder.userImage);
//        }

        holder.contestPrizeTV.setText(String.valueOf(contestModel.getPrize()));
        if(contestModel.isAlreadyParticipant()){
            holder.participateBtn.setEnabled(false);
        }else{
            holder.participateBtn.setEnabled(true);
        }
        holder.participateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetworkUtils.checkInternetConnection(context)){
                    Toast.makeText(context, R.string.checkInternetConn, Toast.LENGTH_SHORT).show();
                }else{
                    addUserToParticipantList(contestModel.getId());
                    notifyDataSetChanged();
                    //ToDO : refresh recycle view and make other contests disabled
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: handle on item click here
            }
        });



    }

    private void addUserToParticipantList(int contest_id) {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);
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
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {

                            Toast.makeText(context, "Fail to get data.." + e.toString()
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
                Toast.makeText(context, "Fail to get data.." + error.toString()

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
                parameters.put("user_id", String.valueOf(SharedPrefs.getInt(context,SharedPrefs.KEY_USER_ID)));
                Log.e(TAG, String.valueOf(SharedPrefs.getInt(context,SharedPrefs.KEY_USER_ID)));
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

    @Override
    public int getItemCount() {
        return contests.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView contestIV;
        public TextView contestPrizeTV;
        public Button participateBtn;

        public ViewHolder(View itemView) {
            super(itemView);
//            this.contestIV = itemView.findViewById(R.id.contest_image);
            this.contestPrizeTV = itemView.findViewById(R.id.contest_prize_tv);
            this.participateBtn = itemView.findViewById(R.id.contest_participate_btn);

        }
    }
}

