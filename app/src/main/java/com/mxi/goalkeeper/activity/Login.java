package com.mxi.goalkeeper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mxi.goalkeeper.R;
import com.mxi.goalkeeper.database.SQLiteTD;
import com.mxi.goalkeeper.network.AppController;
import com.mxi.goalkeeper.network.CommanClass;
import com.mxi.goalkeeper.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    EditText et_email, et_password;
    Button btn_login, btn_facebook;
    TextView tv_sign_up, tv_forgot_password;
    LinearLayout ll_linear;
    CommanClass cc;
    ProgressDialog pDialog;
    SQLiteTD dbcom;
    //GoogleCloudMessaging gcmObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cc = new CommanClass(Login.this);
        dbcom = new SQLiteTD(Login.this);
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        cc.savePrefString("device_id", android_id);

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_facebook = (Button) findViewById(R.id.btn_facebook);
        tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);

        tv_sign_up.setText(Html.fromHtml("Don't have an account?" + " " + "<u>" + getString(R.string.sign_up) + "</u>"));

        btn_login.setOnClickListener(this);
        btn_facebook.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        tv_sign_up.setOnTouchListener(this);
    }

    private void SignIn() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (!cc.isConnectingToInternet()) {

            cc.showSnackbar(ll_linear, getString(R.string.no_internet));
        } else if (email.equals("")) {

            cc.showSnackbar(ll_linear, getString(R.string.enter_email));
        } else if (password.equals("")) {

            cc.showSnackbar(ll_linear, getString(R.string.enter_password));
        } else {
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            pDialog.setCancelable(false);
            cc.savePrefString("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.e("fcmId", refreshedToken);
            // new GetGcmId(email, password).execute();
            makeJsonlogin(email, password, refreshedToken);


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                SignIn();
                break;
            case R.id.tv_forgot_password:
                ForgotPassword();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_sign_up:
                startActivity(new Intent(Login.this, SignUpNext.class));
                finish();
                break;
        }
        return false;
    }
    //===================================Forgot Password Dialog ==========================================================================

    private void ForgotPassword() {
        final Dialog dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.forgot_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

        final EditText et_email = (EditText) dialog.findViewById(R.id.et_email);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));
                } else if (email.equals("")) {
                    cc.showToast(getString(R.string.enter_email));
                } else {
                    pDialog = new ProgressDialog(Login.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.show();
                    pDialog.setCancelable(false);
                    makeJsonForgotPassword(email);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }

    //========================================FORGOT PASSWORD=======================================================================================

    private void makeJsonForgotPassword(final String email) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_forgot_password,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:forgot_pass", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            if (jsonObject.getString("status").equals("200")) {
                                cc.showToast(jsonObject.getString("message"));
                                startActivity(new Intent(Login.this, ForgotPassword.class));

                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("forgot_pass Error", e.toString());
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
                params.put("email", email);

                Log.i("request forgot_pass", params.toString());

                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }


    //========================================LOGIN=======================================================================================

    private void makeJsonlogin(final String email, final String password, final String refreshedToken) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_login,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        jsonParseLogin(response);
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
                params.put("email", email);
                params.put("password", password);
                params.put("device_id", cc.loadPrefString("device_id"));
                params.put("device_type", "Android");
                params.put("type_id", refreshedToken);

                Log.i("request login", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                //  headers.put("user_token", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }


        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseLogin(String response) {
        Log.i("Register: result", "Response from server: " + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {
                dbcom.deleteTable();
                JSONObject user_data = jsonObject.getJSONObject("user_data");
                cc.savePrefString("user_token", user_data.getString("user_token"));
                cc.savePrefString("user_type_count", user_data.getString("user_type_count"));

                cc.savePrefString("user_id", user_data.getString("user_id"));
                cc.savePrefString("you_are", user_data.getString("you are"));
                cc.savePrefString("first_name", user_data.getString("first_name"));
                cc.savePrefString("last_name", user_data.getString("last_name"));
                cc.savePrefString("email", user_data.getString("email"));
                cc.savePrefString("civic_number", user_data.getString("civic_number"));
//                cc.savePrefString("apartment", user_data.getString("apartment"));
//                cc.savePrefString("street", user_data.getString("street"));
//                cc.savePrefString("city", user_data.getString("city"));

                cc.savePrefString("address", user_data.getString("address"));
                cc.savePrefString("lat", user_data.getString("lat"));
                cc.savePrefString("long", user_data.getString("long"));

                cc.savePrefString("state_id", user_data.getString("state_id"));
                cc.savePrefString("country_id", user_data.getString("country_id"));

//                cc.savePrefString("postal_code", user_data.getString("postal_code"));

                cc.savePrefString("phone_number", user_data.getString("phone_number"));
                cc.savePrefString("gender", user_data.getString("gender"));
                cc.savePrefString("birth_date", user_data.getString("birth_date"));
                cc.savePrefString("mixedgender", user_data.getString("mixedgender"));
                cc.savePrefString("login_type", user_data.getString("login_type"));
                cc.savePrefString("game_type", user_data.getString("game_type"));
                cc.savePrefString("ground_size", user_data.getString("ground_size"));
                cc.savePrefString("profile_pic", user_data.getString("Image"));
                cc.savePrefString("calibre", user_data.getString("calibre"));
                cc.savePrefString("travel_distance", user_data.getString("travel_distance"));
                cc.savePrefString("social_login_id", user_data.getString("social_login_id"));
                cc.savePrefString("verification_code", user_data.getString("verification_code"));
                cc.savePrefString("status", user_data.getString("status"));
                cc.savePrefString("created_date", user_data.getString("created_date"));
                cc.savePrefString("modified_date", user_data.getString("modified_date"));

                cc.savePrefString("user_type", user_data.getString("user_type"));

                List<String> user_list = Arrays.asList(user_data.getString("user_type").split(","));
                for (int j = 0; j < user_list.size(); j++) {

                    if (user_list.get(j).equals("Team_Manager")) {
                        cc.savePrefString("isManager", "Team_Manager");
                    }
                }
                cc.savePrefString("self_rate", user_data.getString("self_rate"));

                JSONArray week_day = user_data.getJSONArray("week_day");
                for (int i = 0; i < week_day.length(); i++) {
                    JSONObject week = week_day.getJSONObject(i);

                    dbcom.nseartAvailabilities(week.getString("week_day"), week.getString("start_time"),
                            week.getString("end_time"),cc.loadPrefString("user_id"));
                }
                cc.savePrefBoolean("islogin", true);
                cc.showToast(jsonObject.getString("message"));
                Intent mIntent = new Intent(Login.this,
                        MainActivity.class);
                startActivity(mIntent);
                finish();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Login Error", e.toString());
        }
    }

   /* public class GetGcmId extends AsyncTask<String, Void, Void> {

        String regId, email, password;

        public GetGcmId(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // progress_bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                if (gcmObj == null) {
                    gcmObj = GoogleCloudMessaging
                            .getInstance(getApplicationContext());
                }
                regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);

            } catch (IOException ex) {
                Log.e("Error in get GCM", ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //progress_bar.setVisibility(View.INVISIBLE);

            if (!TextUtils.isEmpty(regId)) {
                Log.e("gcm_id", regId);

                makeJsonlogin(email, password, regId);

            } else {
                Toast.makeText(Login.this, "Can't get GCM reg. ID", Toast.LENGTH_LONG);

            }

        }
    }*/

}
