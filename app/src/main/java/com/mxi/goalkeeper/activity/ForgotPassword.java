package com.mxi.goalkeeper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    EditText et_otp, et_password, et_email;
    Button btn_login;
    LinearLayout ll_linear;
    CommanClass cc;
    ProgressDialog pDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        cc = new CommanClass(ForgotPassword.this);

        btn_login = (Button) findViewById(R.id.btn_login);
        et_otp = (EditText) findViewById(R.id.et_otp);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Change Password");
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                String otp = et_otp.getText().toString().trim();
                String new_pass = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                if (!cc.isConnectingToInternet()) {
                    cc.showSnackbar(ll_linear, getString(R.string.no_internet));
                } else if (otp.equals("")) {
                    cc.showSnackbar(ll_linear, "Please enter OTP");
                } else if (new_pass.equals("")) {
                    cc.showSnackbar(ll_linear, "Please enter new password");
                } else if (email.equals("")) {
                    cc.showSnackbar(ll_linear, "Please enter email");
                } else {
                    pDialog = new ProgressDialog(ForgotPassword.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.show();
                    pDialog.setCancelable(false);
                    makeJsonVerifyOTP(otp, new_pass, email);
                }
                break;
        }
    }

    //========================================FORGOT PASSWORD=======================================================================================

    private void makeJsonVerifyOTP(final String otp, final String new_pass, final String email) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_verify_otp,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:verify_otp", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            if (jsonObject.getString("status").equals("200")) {

                                cc.showToast(jsonObject.getString("message"));
                                startActivity(new Intent(ForgotPassword.this, Login.class));
                                finish();
                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("verify_otp Error", e.toString());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(ll_linear, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp_code", otp);
                params.put("new_password", new_pass);
                params.put("email", email);

                Log.i("request:verify_otp", params.toString());

                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

}
