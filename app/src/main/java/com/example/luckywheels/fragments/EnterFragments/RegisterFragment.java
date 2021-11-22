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
import com.example.luckywheels.EnterScreenActivity;
import com.example.luckywheels.HomeActivity;
import com.example.luckywheels.Models.UserModel;
import com.example.luckywheels.R;
import com.example.luckywheels.Utils.NetworkUtils;
import com.example.luckywheels.Utils.SharedPrefs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    EditText reg_first_name, reg_middle_name, reg_last_name,reg_email, reg_ref_code, reg_password, reg_confirm_password;
    TextInputLayout txtInLayoutRegPassword;
    Button registerBtn;
    ProgressBar progressBar;
    private UserModel userModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        reg_first_name = view.findViewById(R.id.reg_first_name);
        reg_middle_name = view.findViewById(R.id.reg_middle_name);
        reg_last_name = view.findViewById(R.id.reg_last_name);
        reg_email = view.findViewById(R.id.reg_email);
        reg_ref_code = view.findViewById(R.id.reg_ref_code);
        reg_password = view.findViewById(R.id.reg_password);
        reg_confirm_password = view.findViewById(R.id.reg_password_confirm);
        txtInLayoutRegPassword = view.findViewById(R.id.txtInLayoutRegPassword);
        registerBtn = view.findViewById(R.id.btn_reg);
        progressBar = view.findViewById(R.id.reg_progressbar);
        return view;     }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    register();
                }


            }
        });

    }

    private void register(){
        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setVisibility(View.GONE);
        String firstName = reg_first_name.getText().toString().trim();
        String middleName = reg_middle_name.getText().toString().trim();
        String lastName = reg_last_name.getText().toString().trim();
        String email = reg_email.getText().toString().trim();
        String ref_code = reg_ref_code.getText().toString().trim();
        String password = reg_password.getText().toString().trim();

        userModel = new UserModel(firstName,
                middleName, lastName, email, ref_code,password);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkUtils.REGISTER_URL,
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
                                String ref_message = jsonObject.getString("ref_code_msg");
                                progressBar.setVisibility(View.GONE);
                                registerBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                Toast.makeText(getContext(), ref_message, Toast.LENGTH_LONG).show();
                                 reg_first_name.setText("");
                                 reg_middle_name.setText("");
                                  reg_last_name.setText("");
                                 reg_email.setText("");
                                 reg_ref_code.setText("");
                                 reg_password.setText("");
                                Fragment var1 = new LoginFragment();
                                ((EnterScreenActivity)getActivity()).replaceFragment(var1);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                registerBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error1", "Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage());
                            Toast.makeText(getContext(), "Register Error1! " + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            registerBtn.setVisibility(View.VISIBLE);

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
                        Toast.makeText(getContext(), "Register Error2! " + error.toString()
                                + "\nCause " + error.getCause()
                                + "\nmessage" + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        registerBtn.setVisibility(View.VISIBLE);
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", userModel.getFirstName());
                params.put("middle_name", userModel.getMiddleName());
                params.put("last_name", userModel.getLastName());
                params.put("email", userModel.getEmail());
                params.put("password", userModel.getPassword());
                if(!reg_ref_code.getText().toString().trim().equals("")){
                    params.put("code_from_user", userModel.getRegRefCode());
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
    private boolean validateInput() {
        if (reg_first_name.getText().toString().trim().isEmpty()) {
            reg_first_name.setError("Please fill out this field");
            progressBar.setVisibility(View.VISIBLE);
            return false;
        } else {
            String regex = "^[a-zA-Z]+$";
            if (!reg_first_name.getText().toString().trim().matches(regex) || reg_first_name.getText().toString().trim().length() < 3) {
                reg_first_name.setError("Please fill out this field with valid first name > 3 letters");
                return false;
            }
        }
        if (reg_middle_name.getText().toString().trim().isEmpty()) {

            reg_middle_name.setError("Please fill out this field");
            return false;
        } else {
            String regex = "^[a-zA-Z]+$";
            if (!reg_middle_name.getText().toString().trim().matches(regex) || reg_middle_name.getText().toString().trim().length() < 3) {
                reg_middle_name.setError("Please fill out this field with a valid mid name > 3 letters");
                return false;
            }
        }
        if (reg_last_name.getText().toString().trim().isEmpty()) {

            reg_last_name.setError("Please fill out this field");
            return false;
        } else {
            String regex = "^[a-zA-Z]+$";
            if (!reg_last_name.getText().toString().trim().matches(regex) || reg_last_name.getText().toString().trim().length() < 3) {
                reg_last_name.setError("Please fill out this field with a valid last name > 3 letters");
                return false;
            }
        }
        if (reg_email.getText().toString().trim().isEmpty()) {

            reg_email.setError("Please fill out this field");
            return false;
        } else {
            String regex = "^(.+)@(.+)$";
            if (!reg_email.getText().toString().trim().matches(regex) ) {
                reg_last_name.setError("Please fill out this field with a valid email ");
                return false;
            }
        }

        if (reg_password.getText().toString().trim().isEmpty()) {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(false);
            reg_password.setError("Please fill out this field");
            return false;
        } else if (reg_password.getText().toString().trim().length() < 4) {
            reg_password.setError("password should be more than 3 characters");
            return false;
        }
        else{txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(true);
            //Here you can write the codes for checking password
        }
        if (reg_confirm_password.getText().toString().trim().isEmpty()) {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(false);
            reg_confirm_password.setError("Please fill out this field");
            return false;
        } else {
            txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(true);
        }
        if (!reg_password.getText().toString().trim().isEmpty()) {
            if (!reg_password.getText().toString().trim().matches(reg_confirm_password.getText().toString().trim())) {
                reg_confirm_password.setError("Please fill out this  with the same password");
                reg_confirm_password.setText("");
                return false;
            }
        }
        return true;

    }


}
