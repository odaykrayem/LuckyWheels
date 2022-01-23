package com.example.luckywheels.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luckywheels.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WithdrawalBalanceDialog extends Dialog {


    private static final String DIALOG_PROFILE_TAG = "dialog_profile_tag";
    public Activity c;
    public Dialog d;
    public Button addBtn, cancelBtn;
    public EditText balanceET, bankCodeET;
    public ProgressBar progressBar;
    LinearLayout buttonsLayout;

    final Calendar myCalendar = Calendar.getInstance();

    private static String TAG = "Dialog";

    public WithdrawalBalanceDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_withdrawal_balance);
        addBtn = (Button) findViewById(R.id.btn_add);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
//            addBtn.setOnClickListener(this);
//            cancelBtn.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
        buttonsLayout = findViewById(R.id.buttons_layout);
        balanceET = findViewById(R.id.dialog_balance_value);
        bankCodeET = findViewById(R.id.dialog_bank_code_et);


        buttonsLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    if (NetworkUtils.checkInternetConnection(getContext())) {
                        transferPointsToBalance(balanceET.getText().toString().trim(), bankCodeET.getText().toString().trim() , getCurrentDate());
                    } else {
                        Toast.makeText(getContext(), R.string.check_connection, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

    }

    private String getCurrentDate() {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, myCalendar
                .get(Calendar.YEAR));
        myCalendar.set(Calendar.MONTH, myCalendar
                .get(Calendar.MONTH));
        myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar
                .get(Calendar.DAY_OF_MONTH));
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(myCalendar.getTime());
    }

    private boolean checkFields() {
        if (balanceET.getText().toString().trim().isEmpty()) {
            balanceET.setError(getContext().getResources().getString(R.string._reg_fill_field_error));
            return false;
        } else {
            String regex = "[0-9]+";
            if (!balanceET.getText().toString().trim().matches(regex)) {
                balanceET.setError(getContext().getResources().getString(R.string.transfer_points_error));
                return false;
            }
        }
        if (bankCodeET.getText().toString().trim().isEmpty()) {
            bankCodeET.setError(getContext().getResources().getString(R.string._reg_fill_field_error));
            return false;
        }
        return true;

    }

    private void transferPointsToBalance(String balance, String bank_code, String currentDate) {
        buttonsLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // creating a variable for our json object request and passing our url to it.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NetworkUtils.WITHDRAWAL_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e(DIALOG_PROFILE_TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString("error");
                            String message = jsonObject.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            if (error.equals("false")) {
                                dismiss();
                            }
                            progressBar.setVisibility(View.GONE);
                            buttonsLayout.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            buttonsLayout.setVisibility(View.VISIBLE);

                            Toast.makeText(getContext(), "Fail to get data.." + e.toString()
                                    + "\nCause " + e.getCause()
                                    + "\nmessage" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                buttonsLayout.setVisibility(View.VISIBLE);

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
                parameters.put("user_id", String.valueOf(SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_ID)));
                Log.e(DIALOG_PROFILE_TAG, String.valueOf(SharedPrefs.getInt(getContext(), SharedPrefs.KEY_USER_ID)));
                parameters.put("balance", balance);
                parameters.put("bank_code", bank_code);
                parameters.put("date", currentDate);

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
