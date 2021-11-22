package com.example.luckywheels.fragments.EnterFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.luckywheels.Models.UserModel;
import com.example.luckywheels.R;
import com.example.luckywheels.Utils.NetworkUtils;
import com.example.luckywheels.Utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    EditText login_email, login_password;
    Button loginBtn;
    ProgressBar progressBar;
    private UserModel userModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        login_email = view.findViewById(R.id.login_email);
        login_password = view.findViewById(R.id.login_password);
        loginBtn = view.findViewById(R.id.login_btn);
        progressBar = view.findViewById(R.id.login_progressbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(SharedPrefs.getBoolean(getContext(),SharedPrefs.KEY_CONNECTION_STATE) == false){
            Toast.makeText(getContext(), R.string.connectionToast, Toast.LENGTH_SHORT).show();
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    String user_email =  login_email.getText().toString().trim();
                    String user_password = login_password.getText().toString().trim();
                    login( user_email, user_password);
                }


            }
        });
    }

    private void login(String userEmail, String userPassword) {
        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);
        String email = login_email.getText().toString().trim();
        String password = login_password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkUtils.LOG_IN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("error");
                            String  message = jsonObject.getString("message");

                            if (code.equals("false")) {
                                progressBar.setVisibility(View.GONE);
                                loginBtn.setVisibility(View.VISIBLE);
                                int id = jsonObject.getInt("id");
                                String firstName =  jsonObject.getString("first_name");
                                String middleName = jsonObject.getString("middle_name");
                                String lastName = jsonObject.getString("last_name");
                                String email =  jsonObject.getString("email");
                                int points = jsonObject.getInt("points");
                                int balance = jsonObject.getInt("balance");
                                String ref_code = jsonObject.getString("referral_code");
                                int ref_times = jsonObject.getInt("ref_times");

                                userModel = new UserModel(id,firstName, middleName, lastName, email, points, balance, ref_code,ref_times);
                                saveUserCredentials(userModel);
                                SharedPrefs.savePref(getContext(), SharedPrefs.KEY_LOG_IN_STATE, true);
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getContext(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                loginBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error1", "Login Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
                            Toast.makeText(getContext(), "Login Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Login Error2! " + error.toString()
                                + "\nCause: " + error.getCause()
                                + "\nmessage: " + error.getMessage()
                        );
                        Toast.makeText(getContext(), "Login Error2! " + error.toString()
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", userEmail);
                params.put("password", userPassword);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void saveUserCredentials(UserModel userModel) {
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_ID, userModel.getUserId());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_FIRST_NAME, userModel.getFirstName());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_MIDDLE_NAME, userModel.getMiddleName());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_LAST_NAME, userModel.getLastName());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_EMAIL, userModel.getEmail());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_PASSWORD, userModel.getPassword());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_POINTS, userModel.getPoints());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_BALANCE, userModel.getBalance());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_REF_CODE, userModel.getReferralCode());
        SharedPrefs.save(getContext(), SharedPrefs.KEY_USER_REF_TIMES, userModel.getRefTimes());

    }
    private boolean validateInput() {
        return true;
    }
}
